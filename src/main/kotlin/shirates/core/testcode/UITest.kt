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
        TestLog.info(Const.SEPARATOR_LONG)
        TestLog.info(TestFunctionDescription)

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

        try {
            val configPath = configFile.toPath()
            val testConfigName = configPath.toFile().nameWithoutExtension

            // testResults
            if (testConfigName.isNotBlank()) {
                TestLog.setupTestResults(
                    testResults = PropertiesManager.testResults,
                    testConfigName = testConfigName
                )
            }

            // TestList
            val testListPath = TestLog.getTestListPath()
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

            // appPackageFile
            if (profile.appPackageFile.isNullOrBlank().not()) {
                ParameterRepository.write("appPackageFile", profile.appPackageFile!!)
            }

            profile.getMetadataFromFileName()
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

            // setup ScreenRepository
            ScreenRepository.setup(
                screensDirectory = configPath.parent.resolve("screens"),
                importDirectories = profile.testConfig!!.importScreenDirectories
            )

            // setup ImageFileRepository
            ImageFileRepository.setup(
                screenDirectory = ScreenRepository.screensDirectory,
                importDirectories = ScreenRepository.importDirectories
            )

            // testContext
            val testContext = TestContext(profile = profile)

            // CustomFunctionRepository
            CustomFunctionRepository.initialize()

            // MacroRepository
            MacroRepository.setup()

            // testContext
            TestDriver.setupContext(testContext = testContext)

            if (TestMode.isNoLoadRun) {
                return
            }

            // profile
            profile.completeProfile()
            profile.validate()

            // Appium Server
            AppiumServerManager.setupAppiumServerProcess(
                sessionName = TestLog.currentTestClassName,
                profile = profile
            )

            // AppiumDriver
            val lastProfile = TestDriver.lastTestContext.profile
            if (profile.isSameProfile(lastProfile) && TestDriver.canReuse) {
                TestLog.info(
                    message(id = "reusingAppiumDriverSession", arg1 = configPath.toString(), arg2 = profileName)
                )
                TestDriver.testContext = TestDriver.lastTestContext
            } else {
                TestDriver.createAppiumDriver()
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
        if (testConfig!!.profileMap.containsKey(profileName)) {
            return testConfig!!.profileMap[profileName]!!
        }
        val defaultProfile = testConfig!!.profileMap["_default"]!!
        defaultProfile.profileName = profileName
        return defaultProfile
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

        TestLog.ok(message = message, scriptCommand = scriptCommand, log = true)
    }

    /**
     * NG
     */
    open fun NG(message: String = "") {

        throw TestNGException(message)
    }

    /**
     * SKIP_CASE
     */
    open fun SKIP_CASE(
        message: String = message(id = "SKIP_CASE")
    ) {
        if (TestMode.isNoLoadRun) {
            TestLog.skipCase(message)
            return
        }
        driver.screenshotCore()
        TestLog.skipCase(message)
        driver.skipCase = true
        testSkipped = true
    }

    /**
     * SKIP_SCENARIO
     */
    open fun SKIP_SCENARIO(
        message: String = message(id = "SKIP_SCENARIO")
    ) {
        if (TestMode.isNoLoadRun) {
            TestLog.skipScenario(message)
            return
        }
        driver.screenshotCore()
        TestLog.skipScenario(message)
        driver.skipScenario = true
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
        useHandler: Boolean? = null,
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

        CodeExecutionContext.isInScenario = true

        val sw = StopWatch(title = "Running scenario").start()
        try {
            withContext(
                useCache = useCache,
                useHandler = useHandler
            ) {
                scenarioCore(
                    scenarioId = scenarioId,
                    order = order,
                    desc = desc,
                    launchApp = launchApp,
                    testProc = testProc
                )
            }
        } catch (t: Throwable) {
            val message = t.message ?: ""
            if (t is RerunScenarioException ||
                message.contains("Read timed out") ||
                message.contains("AppiumProxy.getSource() timed out") ||
                message.contains("Could not start a new session. Response code 500.") ||
                message.contains(" is still running after")
            ) {
                TestLog.getLinesOfCurrentTestScenario().forEach {
                    it.deleted = true
                }

                if (t is RerunScenarioException && t.cause != null) {
                    TestLog.warn(t.cause?.message ?: t.cause.toString())
                } else {
                    TestLog.warn(message)
                }

                /**
                 * Rerun scenario after resetting appium session
                 */
                TestLog.write("Rerunning scenario ...")
                TestDriver.resetAppiumSession()
                scenarioCore(
                    scenarioId = scenarioId,
                    order = order,
                    desc = desc,
                    launchApp = launchApp,
                    testProc = testProc
                )
            } else {
                throw t
            }
        } finally {
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
        driver.skipScenario = false
        CodeExecutionContext.lastScreenshotXmlSource = ""
        testSkipped = false

        val lastScenarioLog = TestLog.lastScenarioLog
        if (lastScenarioLog?.testScenarioId == scenarioId && lastScenarioLog?.deleted == false) {
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
            if (TestLog.lines.any { it.result.isEffectiveType }.not()) {
                throw NotImplementedError(message(id = "noTestResultFound"))
            }
        } catch (t: TestNGException) {
            val scriptCommand = t.commandContext?.beginLogLine?.scriptCommand ?: ""
            TestLog.ng(exception = t, scriptCommand = scriptCommand)
        } catch (t: TestFailException) {
            scenarioLog.resultMessage = t.message
            throw t
        } catch (t: TestAbortedException) {
            TestLog.warn(t.message!!)
            throw t
        } catch (t: NotImplementedError) {
            TestLog.notImpl(t)
            throw TestAbortedException(t.message!!, t)
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
        useHandler: Boolean? = null,
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
                useCache = useCache,
                useHandler = useHandler
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

        driver.skipCase = false
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
        useHandler: Boolean? = null,
        conditionFunc: () -> Unit
    ): CAEPattern {

        TestLog.trace()

        return CAEPattern.condition(
            useCache = useCache,
            useHandler = useHandler,
            conditionFunc = conditionFunc
        )
    }

    /**
     * action
     */
    fun action(
        useCache: Boolean? = null,
        useHandler: Boolean? = null,
        actionFunc: () -> Unit
    ): CAEPattern {

        TestLog.trace()

        return CAEPattern.condition(
            useCache = useCache,
            useHandler = useHandler,
            conditionFunc = actionFunc
        )
    }

    /**
     * expectation
     */
    fun expectation(
        useCache: Boolean? = null,
        useHandler: Boolean? = null,
        expectationFunc: () -> Unit
    ): CAEPattern {

        TestLog.trace()

        return CAEPattern.condition(
            useCache = useCache,
            useHandler = useHandler,
            conditionFunc = expectationFunc
        )
    }

}