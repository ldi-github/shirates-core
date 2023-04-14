package shirates.core.testcode

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.extension.*
import org.opentest4j.TestAbortedException
import shirates.core.Const
import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Testrun
import shirates.core.configuration.repository.ParameterRepository
import shirates.core.driver.*
import shirates.core.driver.TestDriver.lastElement
import shirates.core.driver.TestMode.hasOsaifuKeitai
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.TestMode.isStub
import shirates.core.exception.TestFailException
import shirates.core.logging.LogFileFormat
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.server.AppiumServerManager
import shirates.core.utility.android.AndroidMobileShellUtility
import shirates.core.utility.misc.EnvUtility
import shirates.core.utility.time.StopWatch
import java.nio.file.Files

/**
 * UITestCallbackExtension
 */
class UITestCallbackExtension : BeforeAllCallback, AfterAllCallback, BeforeEachCallback,
    AfterEachCallback, TestExecutionExceptionHandler {

    var beforeAllExecuted = false
    var beforeAllAfterSetupExecuted = false
    var testClassWatch = StopWatch()
    var testFunctionWatch = StopWatch()

    companion object {
        var uiTest: UITest? = null
        var failOfTestContext = false
        var failAnnotation: Fail? = null
        var noLoadRunOfTestContext = false
        var disableCacheAnnotation = false
    }

    /**
     * beforeAll
     */
    override fun beforeAll(context: ExtensionContext?) {

        beforeAllExecuted = false
        failOfTestContext = false
        failAnnotation = null
        noLoadRunOfTestContext = false
        disableCacheAnnotation = false

        testClassWatch = StopWatch("testClassWatch").start()

        val tr = context!!.requiredTestClass.annotations.firstOrNull() { it is Testrun } as Testrun?
        PropertiesManager.testrun = tr
        if (tr != null) {
            PropertiesManager.setup(testrunFile = tr.testrunFile)
        } else {
            PropertiesManager.setup()
        }

        ParameterRepository.clear()
        TestLog.clear()
        TestDriver.clearLast()

        val tc = context.requiredTestClass
        TestLog.currentTestClass = tc

        val version = io.github.ldi_github.shirates_core.BuildConfig.version
        val appiumClientVersion = io.github.ldi_github.shirates_core.BuildConfig.appiumClientVersion
        val sheetNameAnnotation = tc.declaredAnnotations.firstOrNull { it is SheetName } as SheetName?
        val sheetName = sheetNameAnnotation?.sheetname ?: tc.simpleName

        TestLog.write(Const.SEPARATOR_LONG)
        TestLog.write("///")
        TestLog.write("/// Shirates $version")
        TestLog.write("///")
        TestLog.write("powered by Appium (io.appium:java-client:$appiumClientVersion)")
        TestLog.write(Const.SEPARATOR_LONG)
        ParameterRepository.write("testClass", tc.name)
        ParameterRepository.write("sheetName", sheetName)
        ParameterRepository.write("logLanguage", TestLog.logLanguage)

        // can not get UITest instance at this point.
//        uiTest = getUITest(context)

    }

    /**
     * beforeEach
     */
    override fun beforeEach(context: ExtensionContext?) {

        testFunctionWatch = StopWatch("testFunctionWatch").start()

        TestMode.clear()
        EnvUtility.reset()
        failOfTestContext = context.isMethodAnnotated(Fail::class)
        failAnnotation = context.getMethodAnnotation(Fail::class) ?: context.getClassAnnotation(Fail::class)
        noLoadRunOfTestContext = context.isAnnotated(NoLoadRun::class)
        disableCacheAnnotation = context.isAnnotated(DisableCache::class)

        val requiredContext = context!!.requiredContext
        if (requiredContext.isRequired.not()) {
            throw TestAbortedException("Test skipped. (${requiredContext.clazz?.simpleName})")
        }

        val testMethodName = context.requiredTestMethod.name
        val displayName = context.displayName ?: testMethodName
        val order = context.requiredTestMethod.annotations.firstOrNull { it is Order } as Order?
        val orderValue = order?.value

        TestLog.resetTestScenarioInfo()
        TestDriver.clearContext()

        uiTest = getUITest(context)

        if (uiTest != null) {
            val testBase = uiTest!!
            testBase.currentTestMethodName = testMethodName
            testBase.currentDisplayName = displayName
            testBase.currentOrder = orderValue
            testBase.clearTempStorages()

            // beforeAll
            if (beforeAllExecuted.not()) {
                testBase.beforeAll(context)
                beforeAllExecuted = true
            }

            // setup
            testBase.setup()

            // print config
            if (TestLog.configPrinted.not()) {
                val profile = testContext.profile
                val testConfig = profile.testConfig
                ParameterRepository.write("testrun", PropertiesManager.testrunFile)
                ParameterRepository.write("testConfigName", "${profile.testConfigName}(${testConfig?.testConfigFile})")
                ParameterRepository.write("profileName", profile.profileName)
                ParameterRepository.write("appIconName", "${profile.appIconName}")
                if (profile.noLoadRun?.toBoolean() == true) {
                    ParameterRepository.write("noLoadRun", "${profile.noLoadRun}")
                }
            }

            // EventHandlers
            val eventContext = TestDriverEventContext()
            testBase.setEventHandlers(eventContext)
            testContext.irregularHandler = eventContext.irregularHandler

            // printCapabilities
            if (TestLog.capabilityPrinted.not()) {
                TestDriver.printCapabilities()

                if (TestMode.isNoLoadRun.not()) {
                    // Settings(Android only)
                    if (isAndroid && testContext.profile.settings.any()) {
                        TestLog.write("settings")
                        for (s in testContext.profile.settings) {
                            val value = AndroidMobileShellUtility.setValue(name = s.key, value = s.value)
                            ParameterRepository.write(s.key, value.toString().trimEnd())
                        }
                    }
                    // others
                    TestLog.write("(others)")
                    ParameterRepository.write("isEmulator", TestDriveObject.isEmulator.toString())
                    if (isStub) {
                        ParameterRepository.write("isStub", isStub.toString())
                    }
                    ParameterRepository.write("hasOsaifuKeitai", hasOsaifuKeitai.toString())
                }
            }

            // beforeAllAfterSetup
            if (beforeAllAfterSetupExecuted.not()) {
                testBase.beforeAllAfterSetup(context)
                beforeAllAfterSetupExecuted = true
            }
        }

        TestLog.trace("@Test fun $testMethodName()")
        TestLog.trace(context.displayName)

        testContext.saveState()
        uiTest?.beforeEach(context)
        lastElement.lastError = null

        val lap = testFunctionWatch.lap("setupExecuted")
        val duration = "%.1f".format(lap.durationSeconds)
        TestLog.info(message(id = "setupExecuted", arg1 = duration))
    }

    /**
     * afterEach
     */
    override fun afterEach(context: ExtensionContext?) {

        TestMode.clear()

        val scenarioLines = TestLog.getLinesOfCurrentTestScenario()

        TestLog.stepNo = null
        TestLog.resetTestScenarioInfo()

        uiTest?.afterEach(context)

        if (failAnnotation != null) {
            val ex = TestFailException(message = failAnnotation!!.message)
            throw ex
        }

        val hasNoException = context?.executionException?.isEmpty == true
        if (hasNoException && scenarioLines.any { it.logType == LogType.SCENARIO }.not()) {
            val ex = TestAbortedException("scenario not implemented.")
            TestLog.warn(ex.message!!)
            throw ex
        }

        failOfTestContext = false
        failAnnotation = null
        noLoadRunOfTestContext = false
        disableCacheAnnotation = false
        testContext.resumeState()

        testFunctionWatch.stop()
        val duration = "%.1f".format(testFunctionWatch.elapsedSeconds)
        TestLog.info(message(id = "testFunctionExecuted", arg1 = duration))
        TestLog.info("End of ${uiTest?.TestFunctionDescription}")
    }

    /**
     * afterAll
     */
    override fun afterAll(context: ExtensionContext?) {

        uiTest?.afterAll(context)

        /**
         * output log
         */
        TestLog.printLogDirectory()
        if (Files.exists(TestLog.directoryForLog)) {
            // text log
            if (TestLog.enableTrace) {
                TestLog.outputLogTrace(LogFileFormat.Text)
            }
            TestLog.outputLogDetail(LogFileFormat.Text)
            TestLog.outputLogSimple(LogFileFormat.Text)
            TestLog.outputCommandList()

            // HTML report
            if (TestLog.enableTrace) {
                TestLog.outputLogTrace(LogFileFormat.Html)
            }
            TestLog.outputLogDetail(LogFileFormat.Html)
            TestLog.outputLogSimple(LogFileFormat.Html)
        }

        // Spec-Report
        if (TestLog.lines.any() { it.logType == LogType.SCENARIO }) {
            TestLog.outputSpecReport()
        } else {
            println("No scenario found. Outputting Spec-Report skipped.")
        }

        // TestList
        TestLog.outputTestList()

        // close
        val appiumClose = AppiumServerManager.appiumClose
        if (appiumClose) {
            TestDriver.quit()
            AppiumServerManager.close()
        }

        TestLog.currentTestClass = null
        noLoadRunOfTestContext = false

        testClassWatch.stop()
        val duration = "%.1f".format(testClassWatch.elapsedSeconds)
        TestLog.info(message(id = "testClassExecuted", arg1 = duration))

        uiTest?.finally()
    }

    /**
     * handleTestExecutionException
     */
    override fun handleTestExecutionException(context: ExtensionContext?, throwable: Throwable?) {

        TestLog.trace()

        if (throwable != null) {
            TestDriver.screenshotCore()
            throw throwable
        }
    }

    /**
     * getUITest
     */
    fun getUITest(context: ExtensionContext?): UITest? {

        if (context == null || context.testInstance == null) {
            return null
        }
        if (context.testInstances.isEmpty) {
            return null
        }
        val testInstance = context.testInstance.get()

        if (testInstance is UITest) {
            testInstance.extensionContext = context
            return testInstance
        }
        return null
    }

}