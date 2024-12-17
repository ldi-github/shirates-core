package shirates.core.testcode

import org.junit.jupiter.api.extension.*
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestDriver
import shirates.core.driver.TestMode
import shirates.core.driver.sourceXml
import shirates.core.driver.testDrive
import shirates.core.logging.LogFileFormat
import shirates.core.logging.TestLog
import shirates.core.utility.misc.EnvUtility
import shirates.core.utility.toPath

class UnitTestCallbackExtension : BeforeAllCallback, AfterAllCallback, BeforeEachCallback,
    AfterEachCallback, TestExecutionExceptionHandler {

    var unitTest: UnitTest? = null

    companion object {
        var extensionContext: ExtensionContext? = null
    }

    /**
     * beforeAll
     */
    override fun beforeAll(context: ExtensionContext?) {

        extensionContext = context

        unitTest = null
        PropertiesManager.testrun = null
        TestLog.clear()

        val tc = context!!.requiredTestClass
        TestLog.currentTestClass = tc

        // Can't get testBase instance at this point
//        unitTest = getTestBase(context)
    }

    /**
     * beforeEach
     */
    override fun beforeEach(context: ExtensionContext?) {

        extensionContext = context
        TestMode.clear()
        PropertiesManager.clear()
        EnvUtility.reset()

        TestLog.setupTestResults(
            testResults = PropertiesManager.testResults,
            testConfigName = "unittest"
        )

        // beforeAll (first call of beforeEach)
        if (unitTest == null) {
            unitTest = getTestBase(context)
            unitTest?.beforeAll(context)
        }

        val testMethodName = context!!.requiredTestMethod.name
        TestLog.info("@Test fun $testMethodName()")
        if (testMethodName != context.displayName.trimEnd(')').trimEnd('(')) {
            TestLog.info("DisplayName: ${context.displayName}")
        }

        unitTest?.beforeEach(context)
    }

    /**
     * afterEach
     */
    override fun afterEach(context: ExtensionContext?) {

        extensionContext = context

        unitTest?.afterEach(context)

        val testMethodName = context?.requiredTestMethod?.name
        TestLog.info("end of fun $testMethodName()")
    }

    /**
     * afterAll
     */
    override fun afterAll(context: ExtensionContext?) {

        unitTest?.afterAll(context)

        TestLog.outputLogDetail(LogFileFormat.Text)
        TestLog.outputLogSimple(LogFileFormat.Text)

        TestLog.currentTestClass = null
        extensionContext = null
        unitTest = null
    }

    /**
     * getTestBase
     */
    fun getTestBase(context: ExtensionContext?): UnitTest? {

        if (context == null || context.testInstance == null) {
            return null
        }
        if (context.testInstances.isEmpty) {
            return null
        }
        val testInstance = context.testInstance.get()

        if (testInstance is UnitTest) {
            return testInstance
        }
        return null
    }

    /**
     * handleTestExecutionException
     */
    override fun handleTestExecutionException(context: ExtensionContext?, throwable: Throwable?) {

        if (throwable != null) {
            TestLog.error(throwable)
            TestLog.info(testDrive.sourceXml)
            val filePath = "${TestLog.directoryForLog}/${TestLog.currentLineNo}_xmlsource.xml".toPath()
            TestDriver.outputXmlSource(filePath = filePath)

            throw throwable
        }
    }
}