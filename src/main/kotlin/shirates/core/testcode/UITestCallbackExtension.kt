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
import shirates.core.driver.TestMode.isNoLoadRun
import shirates.core.driver.TestMode.isStub
import shirates.core.exception.TestConfigException
import shirates.core.exception.TestFailException
import shirates.core.logging.LogFileFormat
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.server.AppiumServerManager
import shirates.core.utility.android.AndroidMobileShellUtility
import shirates.core.utility.load.CpuLoadService
import shirates.core.utility.misc.EnvUtility
import shirates.core.utility.time.StopWatch
import shirates.core.vision.VisionElement
import shirates.core.vision.testcode.VisionTest
import java.nio.file.Files

/**
 * UITestCallbackExtension
 */
class UITestCallbackExtension : BeforeAllCallback, AfterAllCallback, BeforeEachCallback,
    AfterEachCallback, TestExecutionExceptionHandler {

    var beforeAllExecuted = false
    var beforeAllAfterSetupExecuted = false
    var testClassWatch = StopWatch("testClassWatch")
    var testFunctionWatch = StopWatch("testFunctionWatch")

    companion object {
        var uiTestBase: UITestBase? = null

        var androidAnnotation: android? = null
        var iosAnnotation: ios? = null

        var failOfTestContext = false
        var failAnnotation: Fail? = null
        var deletedAnnotation: Deleted? = null
        var manualAnnotation: Manual? = null
        var environmentAnnotation: Environment? = null

        var enableCache: Boolean? = null

        val isClassManual: Boolean
            get() {
                return uiTestBase?.isClassManual ?: false
            }
        val isMethodManual: Boolean
            get() {
                return uiTestBase?.isMethodManual ?: false
            }
        val isSkippingScenario: Boolean
            get() {
                return uiTestBase?.isSkippingScenario ?: false
            }
        val isSkippingCase: Boolean
            get() {
                return uiTestBase?.isSkippingCase ?: false
            }
        val isManualingScenario: Boolean
            get() {
                return uiTestBase?.isManualingScenario ?: false
            }
        val isManualingCase: Boolean
            get() {
                return uiTestBase?.isManualingCase ?: false
            }
        val isVisionTest: Boolean
            get() {
                return uiTestBase is VisionTest
            }
    }

    /**
     * beforeAll
     */
    override fun beforeAll(context: ExtensionContext?) {

        beforeAllExecuted = false
        failOfTestContext = false
        failAnnotation = null

        testClassWatch = StopWatch("testClassWatch").start()

        androidAnnotation = context.getClassAnnotation(android::class)
        iosAnnotation = context.getClassAnnotation(ios::class)
        if (androidAnnotation != null && iosAnnotation != null) {
            throw TestConfigException("Do not use @android annotation and @ios annotation at the same time.")
        }
        if (androidAnnotation != null && context.getClassAnnotation(Testrun::class) != null) {
            throw TestConfigException("Do not use @android annotation and @Testrun annotation at the same time.")
        }
        if (iosAnnotation != null && context.getClassAnnotation(Testrun::class) != null) {
            throw TestConfigException("Do not use @ios annotation and @Testrun annotation at the same time.")
        }

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
        TestLog.write("/// shirates-core $version")
        TestLog.write("///")
        TestLog.write("powered by Appium (io.appium:java-client:$appiumClientVersion)")
        TestLog.write(Const.SEPARATOR_LONG)
        ParameterRepository.write("testClass", tc.name)
        ParameterRepository.write("sheetName", sheetName)
        ParameterRepository.write("logLanguage", TestLog.logLanguage)

        // can not get UITestBase instance at this point.
//        uiTestBase = getUITestBase(context)

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
        deletedAnnotation = context.getMethodAnnotation(Deleted::class) ?: context.getClassAnnotation(Deleted::class)
        manualAnnotation = context.getMethodAnnotation(Manual::class) ?: context.getClassAnnotation(Manual::class)
        environmentAnnotation =
            context.getMethodAnnotation(Environment::class) ?: context.getClassAnnotation(Environment::class)

        if (context.isClassAnnotated(DisableCache::class) && context.isClassAnnotated(EnableCache::class)) {
            throw TestConfigException("Do not use @EnableCache and @DisableCache on a class.")
        }
        if (context.isMethodAnnotated(DisableCache::class) && context.isMethodAnnotated(EnableCache::class)) {
            throw TestConfigException("Do not use @EnableCache and @DisableCache on a function.")
        }

        val isVision = context!!.testInstance.get() is VisionTest

        if (context.isClassAnnotated(EnableCache::class)) {
            if (isVision) {
                throw TestConfigException("@EnableCache is not supported on VisionTest class.")
            }
            enableCache = true
        } else if (context.isClassAnnotated(DisableCache::class)) {
            if (isVision) {
                throw TestConfigException("@DisableCache is not supported on VisionTest class.")
            }
            enableCache = false
        }
        if (context.isMethodAnnotated(EnableCache::class)) {
            if (isVision) {
                throw TestConfigException("@EnableCache is not supported on VisionTest class.")
            }
            enableCache = true
        } else if (context.isMethodAnnotated(DisableCache::class)) {
            if (isVision) {
                throw TestConfigException("@DisableCache is not supported on VisionTest class.")
            }
            enableCache = false
        }

        val requiredContext = context.requiredContext
        if (requiredContext.isRequired.not()) {
            throw TestAbortedException("Test skipped. (${requiredContext.clazz?.simpleName})")
        }

        val testMethodName = context.requiredTestMethod.name
        val displayName = context.displayName ?: testMethodName
        val order = context.requiredTestMethod.annotations.firstOrNull { it is Order } as Order?
        val orderValue = order?.value

        TestLog.resetTestScenarioInfo()
        TestDriver.clearContext()

        uiTestBase = getUITest(context)

        if (uiTestBase != null) {
            val testBase = uiTestBase!!
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
                val testMode = if (TestMode.isVisionTest) "Vision" else "Classic"
                ParameterRepository.write("testMode", testMode)
                val profile = uiTestBase!!.testProfile
                val testConfig = profile.testConfig
                ParameterRepository.write("testrun", PropertiesManager.testrunFile)
                if (profile.testConfigName.isNullOrBlank().not() && testConfig?.testConfigFile.isNullOrBlank().not()) {
                    ParameterRepository.write(
                        "testConfigName",
                        "${profile.testConfigName ?: ""}(${testConfig?.testConfigFile ?: ""})"
                    )
                }
                if (profile.profileName.isBlank().not()) {
                    ParameterRepository.write("profileName", profile.profileName)
                }
                if (profile.appIconName.isNullOrBlank().not()) {
                    ParameterRepository.write("appIconName", profile.appIconName ?: "")
                }
                if (profile.noLoadRun?.toBoolean() == true) {
                    ParameterRepository.write("noLoadRun", "true")
                }
                if (PropertiesManager.specReportExcludeDetail) {
                    ParameterRepository.write("specReport.exclude.detail", "true")
                }
            }

            // EventHandlers
            val eventContext = TestDriverEventContext()
            testBase.setEventHandlers(eventContext)
            testContext.irregularHandler = eventContext.irregularHandler
            testContext.onLaunchHandler = eventContext.onLaunchHandler
            testContext.onSelectErrorHandler = eventContext.onSelectErrorHandler
            testContext.onExistErrorHandler = eventContext.onExistErrorHandler
            testContext.onScreenErrorHandler = eventContext.onScreenErrorHandler
            testContext.onRequestingRerunHandler = eventContext.onRequestingRerunHandler
            testContext.onRerunScenarioHandler = eventContext.onRerunScenarioHandler
            testContext.onRefreshCurrentScreenHandler = eventContext.onRefreshCurrentScreenHandler
            if (TestMode.isVisionTest) {
                if (testContext.irregularHandler != null) {
                    throw NotImplementedError("irregularHandler is not supported on VisionTest")
                }
                if (testContext.onLaunchHandler != null) {
                    throw NotImplementedError("onLaunchHandler is not supported on VisionTest")
                }
                if (testContext.onSelectErrorHandler != null) {
                    throw NotImplementedError("onSelectErrorHandler is not supported on VisionTest")
                }
                if (testContext.onExistErrorHandler != null) {
                    throw NotImplementedError("onExistErrorHandler is not supported on VisionTest")
                }
                if (testContext.onScreenErrorHandler != null) {
                    throw NotImplementedError("onScreenErrorHandler is not supported on VisionTest")
                }
                if (testContext.onRequestingRerunHandler != null) {
                    throw NotImplementedError("onRequestingRerunHandler is not supported on VisionTest")
                }
                if (testContext.onRerunScenarioHandler != null) {
                    throw NotImplementedError("onRerunScenarioHandler is not supported on VisionTest")
                }
                if (testContext.onRefreshCurrentScreenHandler != null) {
                    throw NotImplementedError("onRefreshCurrentScreenHandler is not supported on VisionTest")
                }
            }

            // printCapabilities
            if (TestLog.capabilityPrinted.not()) {
                TestDriver.printCapabilities()

                // Settings(Android only)
                if (isAndroid && testContext.profile.settings.any()) {
                    TestLog.write("settings")
                    for (s in testContext.profile.settings) {
                        if (isNoLoadRun.not()) {
                            AndroidMobileShellUtility.setValue(name = s.key, value = s.value)
                        }
                        ParameterRepository.write(s.key, s.value.trimEnd())
                    }
                }
                // others
                TestLog.write("(others)")
                ParameterRepository.write("isEmulator", TestDriveObject.isEmulator.toString())
                if (isStub) {
                    ParameterRepository.write("isStub", isStub.toString())
                }
                if (isNoLoadRun.not()) {
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

        if (enableCache == true) {
            testContext.enableCache = true
        } else if (enableCache == false) {
            testContext.enableCache = false
        }

        testContext.saveState()
        try {
            uiTestBase?.beforeEach(context)
        } catch (t: Throwable) {
            if (PropertiesManager.enableRerunScenario) {
                TestLog.warn("$t ${t.stackTraceToString()}")
                uiTestBase?.beforeEach(context)
            } else {
                TestLog.error("$t ${t.stackTraceToString()}")
                throw t
            }
        }
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

        uiTestBase?.afterEach(context)

        if (failAnnotation != null) {
            val ex = TestFailException(message = failAnnotation!!.message)
            throw ex
        }

        val hasNoException = context?.executionException?.isEmpty == true

        failOfTestContext = false
        failAnnotation = null
        enableCache = null
        testContext.resumeState()

        testFunctionWatch.stop()
        val duration = "%.1f".format(testFunctionWatch.elapsedSeconds)
        TestLog.info(message(id = "testFunctionExecuted", arg1 = duration))
        TestLog.info("End of ${uiTestBase?.TestFunctionDescription}")

        TestDriver.visionRootElement.visionContext.clear()  // For avoiding memory leak
        TestDriver.visionRootElement = VisionElement.emptyElement
        System.gc()

        if (hasNoException && scenarioLines.any { it.logType == LogType.SCENARIO }.not()) {
            val ex = TestAbortedException("scenario not implemented.")
            TestLog.warn(ex.message ?: ex.stackTraceToString())
            throw ex
        }
    }

    /**
     * afterAll
     */
    override fun afterAll(context: ExtensionContext?) {

        uiTestBase?.afterAll(context)

        if (CpuLoadService.thread != null) {
            CpuLoadService.stopService()
        }

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

            // Spec-Report
            if (TestLog.lines.any() { it.logType == LogType.SCENARIO }) {
                TestLog.outputSpecReport()
            } else {
                println("No scenario found. Outputting Spec-Report skipped.")
            }

            // TestList
            TestLog.outputTestList()

            // TestClassList
            TestLog.outputTestClassList()
        }

        // close
        val appiumClose = AppiumServerManager.appiumClose
        if (appiumClose) {
            TestDriver.quit()
            AppiumServerManager.close()
        }

        TestLog.currentTestClass = null

        testClassWatch.stop()
        val duration = "%.1f".format(testClassWatch.elapsedSeconds)
        TestLog.info(message(id = "testClassExecuted", arg1 = duration))

        uiTestBase?.finally()

        TestDriver.visionRootElement.visionContext.clear()  // For avoiding memory leak
        TestDriver.visionRootElement = VisionElement.emptyElement
        System.gc()
    }

    /**
     * handleTestExecutionException
     */
    override fun handleTestExecutionException(context: ExtensionContext?, throwable: Throwable?) {

        TestLog.trace()

        if (throwable != null) {
            TestDriver.screenshotCore(sync = false)
            throw throwable
        }
    }

    /**
     * getUITestBase
     */
    fun getUITest(context: ExtensionContext?): UITestBase? {

        if (context == null || context.testInstance == null) {
            return null
        }
        if (context.testInstances.isEmpty) {
            return null
        }
        val testInstance = context.testInstance.get()

        if (testInstance is UITestBase) {
            testInstance.extensionContext = context
            return testInstance
        }
        return null
    }

}