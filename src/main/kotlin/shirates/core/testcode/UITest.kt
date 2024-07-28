package shirates.core.testcode

import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.opentest4j.TestAbortedException
import shirates.core.Const
import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.TestConfig
import shirates.core.configuration.TestProfile
import shirates.core.configuration.repository.DatasetRepositoryManager
import shirates.core.configuration.repository.ImageFileRepository
import shirates.core.configuration.repository.ParameterRepository
import shirates.core.configuration.repository.ScreenRepository
import shirates.core.customobject.CustomFunctionRepository
import shirates.core.driver.*
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.TestMode.isNoLoadRun
import shirates.core.driver.TestMode.isiOS
import shirates.core.driver.commandextension.*
import shirates.core.exception.*
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.macro.MacroRepository
import shirates.core.server.AppiumServerManager
import shirates.core.utility.file.FileLockUtility.lockFile
import shirates.core.utility.load.CpuLoadService
import shirates.core.utility.sync.WaitUtility
import shirates.core.utility.time.StopWatch
import shirates.core.utility.toPath
import shirates.spec.report.TestListReport
import java.lang.reflect.InvocationTargetException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

/**
 * UITest
 */
@ExtendWith(UITestCallbackExtension::class)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
abstract class UITest : TestDrive {

    /**
     * ExtensionContext
     */
    var extensionContext: ExtensionContext? = null

    /**
     * testConfig
     */
    var testConfig: TestConfig? = null

    /**
     * testSkipped
     */
    var testSkipped = false

    /**
     * temporary variables for user
     */
    var e1 = TestElement()
    var e2 = TestElement()
    var e3 = TestElement()

    var b1 = false
    var b2 = false
    var b3 = false

    var i1: Int? = null
    var i2: Int? = null
    var i3: Int? = null

    var d1: Date? = null
    var d2: Date? = null
    var d3: Date? = null

    var s1: String? = null
    var s2: String? = null
    var s3: String? = null

    var o1: Any? = null
    var o2: Any? = null
    var o3: Any? = null

    val TestFunctionDescription: String
        get() {
            var description = "Test function: ${currentTestMethodName}"
            if (currentDisplayName.isNotBlank()) {
                description += " [$currentDisplayName]"
            }
            return description
        }

    val isClassManual: Boolean
        get() {
            if (extensionContext == null) return false
            return extensionContext.isClassAnnotated(Manual::class) || extensionContext.isClassAnnotated(NoLoadRun::class)
        }

    val isMethodManual: Boolean
        get() {
            if (extensionContext == null) return false
            return extensionContext.isMethodAnnotated(Manual::class) || extensionContext.isMethodAnnotated(NoLoadRun::class)
        }

    var isSkippingScenario: Boolean = false

    var isSkippingCase: Boolean = false

    val isSkipping: Boolean
        get() {
            return isSkippingScenario || isSkippingCase
        }

    fun clearTempStorages() {

        e1 = TestElement()
        e2 = TestElement()
        e3 = TestElement()

        b1 = false
        b2 = false
        b3 = false

        i1 = null
        i2 = null
        i3 = null

        s1 = null
        s2 = null
        s3 = null

        o1 = null
        o2 = null
        o3 = null
    }

    /**
     * beforeAll
     */
    open fun beforeAll(context: ExtensionContext?) {

        TestLog.trace()
    }

    /**
     * beforeAllAfterSetup
     */
    open fun beforeAllAfterSetup(context: ExtensionContext?) {

        TestLog.trace()
    }

    /**
     * beforeEach
     */
    open fun beforeEach(context: ExtensionContext?) {

        TestLog.trace()
    }

    /**
     * afterEach
     */
    open fun afterEach(context: ExtensionContext?) {

        TestLog.trace()
    }

    /**
     * afterAll
     */
    open fun afterAll(context: ExtensionContext?) {

        TestLog.trace()
    }

    /**
     * finally
     */
    open fun finally() {

        TestLog.trace()
    }

    /**
     * setup
     */
    open fun setup() {

        TestLog.trace()

        setupFromTestrun(PropertiesManager.testrunFile)
    }

    /**
     * setEventHandlers
     */
    open fun setEventHandlers(context: TestDriverEventContext) {

    }

    /**
     * setupFromTestrun
     */
    fun setupFromTestrun(
        testrunFile: String = PropertiesManager.testrunFile,
        profileName: String = PropertiesManager.profile
    ) {
        TestLog.info("")
        TestLog.info(Const.SEPARATOR_LONG)
        TestLog.info(TestFunctionDescription)
        TestLog.info(Const.SEPARATOR_LONG)

        PropertiesManager.setup(testrunFile = testrunFile)
        TestMode.testTimeNoLoadRun = PropertiesManager.getPropertyValue("noLoadRun") == "true"
        prepareTestLog()

        if (Files.exists(testrunFile.toPath())) {
            TestLog.info(message(id = "initializingWithTestRunFile", subject = testrunFile))
        }

        val configFile: String = PropertiesManager.configFile
        setupCore(configFile = configFile, profileName = profileName)
    }

    private fun prepareTestLog() {

        TestLog.enableTestList = PropertiesManager.enableTestList
        TestLog.enableTestClassList = PropertiesManager.enableTestClassList
        TestLog.enableSpecReport = PropertiesManager.enableSpecReport
        TestLog.enableTrace = PropertiesManager.enableTrace
        TestLog.enableXmlSourceDump = PropertiesManager.enableXmlSourceDump
    }

    protected fun setupFromConfig(configFile: String, profileName: String) {

        prepareTestLog()
        setupCore(configFile = configFile, profileName = profileName)
    }

    private fun setupCore(
        configFile: String,
        profileName: String
    ) {
        if (TestMode.isNoLoadRun.not() && TestMode.isRunningOnWindows && TestMode.isiOS) {
            throw TestAbortedException("Running iOS test is not supported on Windows")
        }

        if (configFile.isBlank()) {
            throw IllegalArgumentException("configFile is required.")
        }

        if (profileName.isBlank()) {
            throw IllegalArgumentException("profileName is required.")
        }

        DatasetRepositoryManager.clear()

        if (PropertiesManager.enableWaitCpuLoad) {
            CpuLoadService.startService()
        }

        try {
            val configPath = configFile.toPath()
            var testConfigName = configPath.toFile().nameWithoutExtension
            if (isNoLoadRun) {
                testConfigName = testConfigName.split("@").first()
            }

            // testResults
            if (testConfigName.isNotBlank()) {
                TestLog.setupTestResults(
                    testResults = PropertiesManager.testResults,
                    testConfigName = testConfigName
                )
            }

            // TestList
            val testListPath = TestLog.directoryForTestList.resolve("TestList_${testConfigName}.xlsx")
            if (Files.exists(testListPath)) {
                val testListReport = lockFile(testListPath) {
                    TestListReport().loadFileOnExist(testListPath = testListPath, withLock = true)
                }
                if (testListReport.enableControl == "X") {
                    val isRequired = testListReport.isRequired(
                        testClassName = this.javaClass.simpleName,
                        function = this.currentTestMethodName
                    )
                    if (isRequired.not()) {
                        throw TestAbortedException("Test skipped. (${this.javaClass.simpleName}#${this.currentTestMethodName}, TestList=$testListPath)")
                    }
                }
            }

            // DirectoryForLog
            TestLog.createDirectoryForLog()
            TestLog.printLogDirectory()

            // TestReportIndex
            TestLog.createOrUpdateTestReportIndex(filterName = "simple")
            TestLog.createOrUpdateTestReportIndex(filterName = "detail")

            // setup config
            val profile = setupConfigAndProfile(
                configPath = configPath,
                profileName = profileName
            )

            // setup ScreenRepository
            ScreenRepository.setup(
                screensDirectory = configPath.parent.resolve("screens"),
                importDirectories = profile.testConfig!!.importScreenDirectories
            )

            if (isNoLoadRun.not()) {
                // appPackageFile
                if (profile.appPackageFile.isNullOrBlank().not()) {
                    ParameterRepository.write("appPackageFile", profile.appPackageFile!!)
                }
                // appEnvironment
                if (profile.appEnvironment.isBlank().not()) {
                    ParameterRepository.write("appEnvironment", profile.appEnvironment)
                }
                // appVersion
                if (profile.appVersion.isBlank().not()) {
                    ParameterRepository.write("appVersion", profile.appVersion)
                }
                // appBuild
                if (profile.appBuild.isBlank().not()) {
                    ParameterRepository.write("appBuild", profile.appBuild)
                }

                // setup ImageFileRepository
                ImageFileRepository.setup(
                    screenDirectory = ScreenRepository.screensDirectory,
                    importDirectories = ScreenRepository.importDirectories
                )

                // CustomFunctionRepository
                CustomFunctionRepository.initialize()

                // MacroRepository
                MacroRepository.setup()
            }

            // testContext
            val testContext = TestContext(profile = profile)
            TestDriver.setupContext(testContext = testContext)

            if (isNoLoadRun) {
                return
            }

            // profile
            profile.completeProfileWithTestMode()
            if (testContext.isLocalServer) {
                CpuLoadService.waitForCpuLoadUnder()
                profile.completeProfileWithDeviceInformation()
            }
            profile.validate()

            // Appium Server
            if (testContext.isLocalServer) {
                CpuLoadService.waitForCpuLoadUnder()
                AppiumServerManager.setupAppiumServerProcess(
                    sessionName = TestLog.currentTestClassName,
                    profile = profile
                )
            }

            // AppiumDriver
            CpuLoadService.waitForCpuLoadUnder()
            val lastProfile = TestDriver.lastTestContext.profile
            if (lastProfile.profileName.isBlank()) {
                // First time
                TestDriver.createAppiumDriver()
            } else {
                // Second time or later
                if (profile.isSameProfile(lastProfile) && TestDriver.canReuse) {
                    // Reuse Appium session if possible
                    TestLog.info(
                        message(id = "reusingAppiumDriverSession", arg1 = configPath.toString(), arg2 = profileName)
                    )
                    TestDriver.testContext = TestDriver.lastTestContext
                } else {
                    if (testContext.isLocalServer) {
                        // Reset Appium session
                        TestDriver.resetAppiumSession()
                    } else {
                        TestDriver.createAppiumDriver()
                    }
                }
            }
        } catch (t: TestAbortedException) {
            TestLog.info(t.message ?: t.cause.toString())
            throw t
        } catch (t: Throwable) {
            TestLog.error(t.toString(), t)
            throw t
        }
    }

    private fun setupConfigAndProfile(
        configPath: Path,
        profileName: String
    ): TestProfile {
        TestLog.info("Loading config.(configFile=$configPath, profileName=$profileName)")
        testConfig = TestConfig(configPath.toString())
        val profile = if (testConfig!!.profileMap.containsKey(profileName)) {
            testConfig!!.profileMap[profileName]!!
        } else {
            val defaultProfile = testConfig!!.profileMap["_default"]!!
            defaultProfile.profileName = profileName
            defaultProfile
        }
        profile.getMetadataFromFileName()

        return profile
    }

    /**
     * NOTIMPL
     */
    open fun NOTIMPL(message: String = "") {

        if (TestMode.isNoLoadRun) {
            return
        }
        if (message.isBlank()) {
            throw NotImplementedError(message(id = "NOTIMPL"))
        } else {
            throw NotImplementedError(message)
        }
    }

    /**
     * OK
     */
    open fun OK(message: String = "", scriptCommand: String = "ok") {

        if (TestMode.isNoLoadRun) {
            describe(message = message)
            return
        }
        val log = CodeExecutionContext.isInOperationCommand.not()
        TestLog.ok(message = message, scriptCommand = scriptCommand, log = log)
    }

    /**
     * NG
     */
    open fun NG(message: String = "") {

        if (TestMode.isNoLoadRun) {
            describe(message = message)
            return
        }
        throw TestNGException(message)
    }

    /**
     * SKIP_CASE
     */
    open fun SKIP_CASE(
        message: String = message(id = "SKIP_CASE")
    ) {
        if (TestMode.isNoLoadRun) {
            isSkippingCase = true
            TestLog.skipCase(message)
            return
        }
        driver.screenshotCore()
        TestLog.skipCase(message)
        isSkippingCase = true
        testSkipped = true
    }

    /**
     * SKIP_SCENARIO
     */
    open fun SKIP_SCENARIO(
        message: String = message(id = "SKIP_SCENARIO")
    ) {
        if (TestMode.isNoLoadRun) {
            isSkippingScenario = true
            TestLog.skipScenario(message)
            return
        }
        driver.screenshotCore()
        TestLog.skipScenario(message)
        isSkippingScenario = true
        testSkipped = true
    }

    var currentTestMethodName = ""
    var currentDisplayName = ""
    var currentOrder = null as Int?

    /**
     * scenario
     */
    fun scenario(
        scenarioId: String? = currentTestMethodName,
        order: Int? = currentOrder,
        desc: String = currentDisplayName,
        launchApp: Boolean = true,
        useCache: Boolean? = null,
        testProc: () -> Unit
    ) {
        if (CodeExecutionContext.isInScenario) {
            throw TestDriverException(
                message(
                    id = "callingSomethingInSomethingNotAllowed",
                    arg1 = "scenario",
                    arg2 = "scenario"
                )
            )
        }

        fun isRerunRequested(t: Throwable?): Boolean {
            if (t == null) {
                return false
            }

            if (TestMode.isNoLoadRun) {
                TestLog.info("isNoLoadRun=${TestMode.isNoLoadRun}")
                return false
            }

            TestLog.info("enableRerunScenario=${PropertiesManager.enableRerunScenario}")

            if (PropertiesManager.enableRerunScenario.not()) {
                return false
            }
            if (t is TestAbortedException) {
                return false
            }
            if (t is RerunScenarioException) {
                return true
            }
            if (testContext.isRerunRequested != null) {
                val r = testContext.isRerunRequested!!.invoke(t)
                TestLog.info("testContext.isRerunRequested=${testContext.isRerunRequested}")
                return r
            }

            val m = t.message ?: t.javaClass.simpleName

            val rerunScenarioWords = Const.RERUN_SCENARIO_WORDS.split("||")
            val containsMessage = rerunScenarioWords.any() { m.contains(it) }
            if (containsMessage) {
                return true
            }
            if (isAndroid) {
                val rerunRequested = PropertiesManager.enableAlwaysRerunOnErrorAndroid
                TestLog.info("enableAlwaysRerunOnErrorAndroid=${rerunRequested}")
                return rerunRequested
            } else {
                val rerunRequested = PropertiesManager.enableAlwaysRerunOnErrorIos
                TestLog.info("enableAlwaysRerunOnErrorAndroid=${rerunRequested}")
                return rerunRequested
            }
        }

        val sw = StopWatch(title = "Running scenario").start()

        CodeExecutionContext.isInScenario = true

        try {
            val context = WaitUtility.doUntilTrue(
                waitSeconds = PropertiesManager.scenarioTimeoutSeconds,
                maxLoopCount = PropertiesManager.scenarioMaxCount,
                retryOnError = true,
                throwOnFinally = false,
                onTimeout = { wc ->
                    TestLog.warn("Scenario timed out. (${sw.elapsedSeconds} > ${wc.waitSeconds})")
                    val t = wc.error?.cause ?: wc.error
                    TestLog.error(message(id = "rerunFailed", submessage = "${t?.message}"))
                    wc.error = t
                },
                onMaxLoop = { wc ->
                    TestLog.warn("Scenario count reached scenarioMaxCount. (scenarioMaxCount=${PropertiesManager.scenarioMaxCount})}")
                    val t = wc.error?.cause ?: wc.error
                    TestLog.error(message(id = "rerunFailed", submessage = "${t?.message}"))
                    wc.error = t
                },
                onError = { wc ->
                    val t = wc.error!!
                    if (t is TestAbortedException) {
                        throw t
                    }
                    val rerunRequested = isRerunRequested(t)
                    wc.retryOnError = rerunRequested
                    if (rerunRequested) {
                        TestLog.getLinesOfCurrentTestScenario().forEach {
                            it.deleted = true
                        }
                        TestLog.warn(message(id = "rerunningScenarioRequested", submessage = t.message ?: ""))

                        if (testContext.onRerunScenarioHandler != null) {
                            testDrive.withoutScroll {
                                testContext.onRerunScenarioHandler!!.invoke(t)
                            }
                        }
                    } else {
                        if ((t is AssertionError).not()) {
                            TestLog.error("${t.message}\n${t.stackTraceToString()}", exception = t)
                        }
                    }
                    CodeExecutionContext.scenarioRerunCause = wc.error
                },
                onBeforeRetry = { wc ->
                    TestLog.write("--------------------------------------------------")
                    TestLog.write("Rerunning scenario ...")
                    TestDriver.resetAppiumSession()
                }
            ) {
                withContext(
                    useCache = useCache,
                ) {
                    scenarioCore(
                        scenarioId = scenarioId,
                        order = order,
                        desc = desc,
                        launchApp = launchApp,
                        testProc = testProc
                    )
                }
                true
            }
            if (context.hasError && isNoLoadRun.not()) {
                val e = context.error!!
                throw e
            }
        } finally {
            if (UITestCallbackExtension.deletedAnnotation != null) {
                val scenarioLines = TestLog.lines.filter { it.testScenarioId == scenarioId }
                for (line in scenarioLines) {
                    line.result = LogType.DELETED
                }
            }
            CodeExecutionContext.isInScenario = false

            sw.stop()
            val duration = "%.1f".format(sw.elapsedSeconds)
            TestLog.info(message(id = "scenarioExecuted", arg1 = duration))
        }
    }

    private fun scenarioCore(
        scenarioId: String?,
        order: Int?,
        desc: String,
        launchApp: Boolean,
        testProc: () -> Unit
    ) {
        isSkippingScenario = false
        CodeExecutionContext.lastScreenshotXmlSource = ""
        testSkipped = false

        val lastScenarioLog = TestLog.lastScenarioLog
        if (lastScenarioLog?.testScenarioId == scenarioId && CodeExecutionContext.isScenarioRerunning.not()) {
            throw TestDriverException(message(id = "multipleScenarioNotAllowed"))
        }

        TestDriver.syncCache()

        TestLog.info("Running scenario ..................................................")

        if (isAndroid) {
            val pack = rootElement.getProperty("package")
            TestLog.info("Startup package: $pack")
        }

        TestLog.testScenarioId = scenarioId!!
        val scenarioLog = TestLog.scenario(testScenarioId = scenarioId, order = order, log = true, desc = desc)

        if (UITestCallbackExtension.deletedAnnotation != null) {
            TestLog.write(
                message = UITestCallbackExtension.deletedAnnotation!!.description,
                logType = LogType.IMPORTANT
            )
        }

        try {
            val fail = UITestCallbackExtension.failAnnotation
            val isFailing = fail != null && TestMode.isNoLoadRun.not()

            var failMessage = ""
            if (isFailing) {
                failMessage = "@Fail(${fail!!.getMesssage()})"
                TestLog.error(message = failMessage)
                TestMode.testTimeNoLoadRun = true
            }

            if (TestMode.isNoLoadRun) {
                TestLog.info("No-Load-Run mode")
            }

            if (launchApp && testDrive.isAppInstalled()) {
                if (isiOS && isRealDevice) {
                    testDrive.tapAppIcon()
                } else {
                    testDrive.launchApp()
                }
            }

            testProc()

            if (isFailing) {
                throw TestFailException(failMessage)
            }
            if (TestLog.lines.any { it.logType == LogType.CASE }.not()) {
                throw NotImplementedError("No case found in scenario.")
            }
            val lines = TestLog.getLinesOfCurrentTestScenario()
            if (lines.any { it.result.isEffectiveType }.not() && TestMode.isNoLoadRun.not()) {
                throw NotImplementedError(message(id = "noTestResultFound"))
            }
        } catch (t: TestNGException) {
            val scriptCommand = t.commandContext?.beginLogLine?.scriptCommand ?: ""
            TestLog.ng(exception = t, scriptCommand = scriptCommand)
        } catch (t: TestFailException) {
            scenarioLog.resultMessage = t.message
            throw t
        } catch (t: TestAbortedException) {
            TestLog.warn(t.message ?: t.stackTraceToString())
            throw t
        } catch (t: NotImplementedError) {
            TestLog.notImpl(t)
            throw TestAbortedException(t.message ?: t.stackTraceToString(), t)
        } catch (t: UnsatisfiedLinkError) {
            TestLog.error(t)
            driver.screenshotCore()
            throw t

        } catch (t: NoClassDefFoundError) {
            TestLog.error(t)
            driver.screenshotCore()
            throw t
        } catch (t: RerunScenarioException) {
            throw t
        } catch (t: Throwable) {
            TestLog.error(message = t.message ?: t.toString(), exception = t)
            driver.screenshotCore()
            throw t
        } finally {
            scenarioLog.processingTime = TestLog.lastTestLog!!.logDateTime.time - scenarioLog.logDateTime.time

            val lines = TestLog.getLinesOfCurrentTestScenario()
            val skipLine =
                lines.firstOrNull() { it.logType == LogType.SKIP_CASE || it.logType == LogType.SKIP_SCENARIO }
            if (skipLine != null) {
                if (scenarioLog.result != LogType.NG && scenarioLog.result != LogType.ERROR) {
                    scenarioLog.result = LogType.SKIP
                    scenarioLog.resultMessage = skipLine.message
                }
            }
        }

        if (TestMode.isNoLoadRun) {
            throw TestAbortedException("No-Load-Run mode")
        }

        val lastScenario = TestLog.lastScenarioLog
        if (lastScenario?.exception != null) {
            when (lastScenario.result) {
                LogType.NG -> {
                    throw AssertionError(lastScenario.resultMessage, lastScenario.exception)
                }

                LogType.NONE -> {
                    throw TestAbortedException(message(id = "noTestResultFound"))
                }

                LogType.SKIP -> {
                    throw TestAbortedException(lastScenario.resultMessage, lastScenario.exception)
                }

                LogType.MANUAL -> {
                    throw TestAbortedException(lastScenario.resultMessage, lastScenario.exception)
                }

                LogType.NOTIMPL -> {
                    throw TestAbortedException(lastScenario.resultMessage, lastScenario.exception)
                }

                LogType.KNOWNISSUE -> {
                    throw TestAbortedException(lastScenario.resultMessage, lastScenario.exception)
                }

                LogType.ERROR -> {
                    throw lastScenario.exception!!
                }

                else -> {
                    TestLog.warn("Unkown LogType. (${lastScenario.result})")
                }
            }
        }

        val fail = UITestCallbackExtension.failAnnotation
        if (fail != null && TestMode.isNoLoadRun.not()) {
            throw TestDriverException("@Fail(${fail.getMesssage()})")
        }

        if (testSkipped) {
            throw TestAbortedException(message(id = "testSkipped"))
        }

    }

    /**
     * case
     */
    open fun case(
        stepNo: Int,
        desc: String? = null,
        useCache: Boolean? = null,
        proc: () -> Unit
    ) {

        if (CodeExecutionContext.isInCase) {
            throw TestDriverException(
                message(
                    id = "callingSomethingInSomethingNotAllowed",
                    arg1 = "case",
                    arg2 = "case"
                )
            )
        }

        try {
            CodeExecutionContext.isInCase = true
            withContext(
                useCache = useCache
            ) {
                caseCore(stepNo, desc, proc)
            }
        } finally {
            CodeExecutionContext.isInCase = false
        }
    }

    private fun caseCore(
        stepNo: Int,
        desc: String?,
        proc: () -> Unit
    ) {

        isSkippingCase = false
        val caseLog = TestLog.case(stepNo = stepNo, log = true, desc = desc)

        if (TestLog.lastScenarioLog?.testScenarioId != currentTestMethodName) {
            val ex =
                TestConfigException(message(id = "callCaseFunctionInScenarioFunction", subject = currentTestMethodName))
            TestLog.error(message = ex.message, exception = ex)
            throw ex
        }

        TestDriver.syncCache()

        try {
            try {
                proc()
            } catch (ex: InvocationTargetException) {
                throw ex.targetException
            } catch (ex: Throwable) {
                throw ex
            }
        } catch (t: ExpectationNotImplementedException) {
            TestLog.notImpl(t)

        } catch (t: NotImplementedError) {
            throw t

        } catch (t: TestNGException) {
            throw t

        } catch (t: UnsatisfiedLinkError) {
            throw t

        } catch (t: NoClassDefFoundError) {
            throw t

        } catch (t: Throwable) {
            throw t

        } finally {
            caseLog.processingTime = TestLog.lastTestLog!!.logDateTime.time - caseLog.logDateTime.time

            val lines = TestLog.getLinesOfCurrentTestCaseId()
            val skipLine =
                lines.firstOrNull() { it.logType == LogType.SKIP_CASE || it.logType == LogType.SKIP_SCENARIO }
            if (skipLine != null) {
                caseLog.result = LogType.SKIP
                caseLog.resultMessage = skipLine.message
            }
        }
    }

    /**
     * skipCaseOn
     */
    fun skipCaseOn(
        submessage: String = "",
        predicate: (() -> Boolean)
    ): CAEPattern {

        TestLog.trace()

        if (predicate()) {
            SKIP_CASE(message(id = "skipCaseOn", submessage = submessage))
        }

        return CAEPattern
    }

    /**
     * skipCaseOnError
     */
    fun skipCaseOnError(
        testAction: () -> Unit
    ): CAEPattern {

        try {
            testAction()
        } catch (t: Throwable) {
            SKIP_CASE(message(id = "skipCaseOnError", submessage = t.message))
        }

        return CAEPattern
    }

    /**
     * condition
     */
    fun condition(
        useCache: Boolean? = null,
        conditionFunc: () -> Unit
    ): CAEPattern {

        TestLog.trace()

        return CAEPattern.condition(
            useCache = useCache,
            conditionFunc = conditionFunc
        )
    }

    /**
     * action
     */
    fun action(
        useCache: Boolean? = null,
        actionFunc: () -> Unit
    ): CAEPattern {

        TestLog.trace()

        return CAEPattern.action(
            useCache = useCache,
            actionFunc = actionFunc
        )
    }

    /**
     * expectation
     */
    fun expectation(
        useCache: Boolean? = null,
        expectationFunc: () -> Unit
    ): CAEPattern {

        TestLog.trace()

        return CAEPattern.expectation(
            useCache = useCache,
            expectationFunc = expectationFunc
        )
    }

}