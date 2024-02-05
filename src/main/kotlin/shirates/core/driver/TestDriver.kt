package shirates.core.driver

import io.appium.java_client.AppiumBy
import io.appium.java_client.AppiumDriver
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.nativekey.AndroidKey
import io.appium.java_client.android.nativekey.KeyEvent
import io.appium.java_client.ios.IOSDriver
import org.openqa.selenium.By
import org.openqa.selenium.Capabilities
import org.openqa.selenium.OutputType
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.DesiredCapabilities
import shirates.core.Const
import shirates.core.UserVar
import shirates.core.configuration.*
import shirates.core.configuration.repository.ImageFileRepository
import shirates.core.configuration.repository.ParameterRepository
import shirates.core.configuration.repository.ScreenRepository
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.TestMode.isRealDevice
import shirates.core.driver.TestMode.isSimulator
import shirates.core.driver.TestMode.isiOS
import shirates.core.driver.befavior.TapHelper
import shirates.core.driver.commandextension.*
import shirates.core.driver.eventextension.TestDriverOnScreenContext
import shirates.core.driver.eventextension.removeScreenHandler
import shirates.core.exception.*
import shirates.core.logging.*
import shirates.core.logging.Message.message
import shirates.core.proxy.AppiumProxy
import shirates.core.server.AppiumServerManager
import shirates.core.testcode.CAEPattern
import shirates.core.utility.*
import shirates.core.utility.android.AndroidDeviceUtility
import shirates.core.utility.android.AndroidMobileShellUtility
import shirates.core.utility.appium.setCapabilityStrict
import shirates.core.utility.element.ElementCategoryExpressionUtility
import shirates.core.utility.file.FileLockUtility.lockFile
import shirates.core.utility.image.*
import shirates.core.utility.ios.IosDeviceUtility
import shirates.core.utility.misc.AppNameUtility
import shirates.core.utility.misc.ProcessUtility
import shirates.core.utility.sync.RetryContext
import shirates.core.utility.sync.RetryUtility
import shirates.core.utility.sync.SyncUtility
import shirates.core.utility.time.StopWatch
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileNotFoundException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.time.Duration
import java.util.*

/**
 * TestDriver
 */
object TestDriver {

    var testContext: TestContext = TestContext()
    var lastTestContext: TestContext = TestContext()
    val initialCapabilities = mutableMapOf<String, String>()

    /**
     * appiumDriver
     */
    val appiumDriver: AppiumDriver
        get() {
            if (mAppiumDriver == null) {
                throw TestDriverException("appiumDriver is null.")
            }
            return mAppiumDriver!!
        }
    private var mAppiumDriver: AppiumDriver? = null

    /**
     * isInitialized
     */
    val isInitialized: Boolean
        get() {
            return mAppiumDriver != null
        }

    /**
     * canReuse
     */
    val canReuse: Boolean
        get() {
            val reuse = testContext.reuseDriver && (mAppiumDriver != null) && lastTestContext.isEmpty.not()
            if (reuse.not()) {
                return false
            }
            try {
                if (isAndroid) {
                    mAppiumDriver!!.pageSource  // throws on fails
                    mAppiumDriver!!.getScreenshotAs(OutputType.BYTES)   // throws on fails
                    androidDriver.pressKey(KeyEvent(AndroidKey.CLEAR))   // throws on fails
                }
            } catch (t: Throwable) {
                return false
            }
            return true
        }

    /**
     * Returns lastElement after syncing cache.
     */
    val it: TestElement
        get() {
            return refreshLastElement()
        }

    /**
     * rootElement
     */
    var rootElement: TestElement
        get() {
            if (testContext.useCache) {
                return TestElementCache.rootElement
            }
            val ms = Measure("rootElement")
            TestElementCache.rootElement = appiumDriver.findElement(By.xpath("//*")).toTestElement()
            ms.end()
            return TestElementCache.rootElement
        }
        set(value) {
            TestElementCache.rootElement = value
        }

    /**
     * refreshLastElement
     */
    fun refreshLastElement(): TestElement {

        if (TestMode.isNoLoadRun) {
            return lastElement
        }
        if (testContext.useCache.not()) {
            return lastElement
        }

        if (TestElementCache.synced) {
            TestLog.trace("TestElementCache.synced=${TestElementCache.synced}. syncCache skipped.")
            return lastElement
        }

        val ms = Measure("$lastElement")

        syncCache()

        if (lastElement.isEmpty.not()) {
            lastElement = lastElement.refreshThisElement()
        }
        if (lastElement.isEmpty) {
            lastElement = rootElement
        }

        ms.end()

        return lastElement
    }

    /**
     * lastElement
     */
    var lastElement: TestElement = TestElement.emptyElement

    /**
     * lastError
     */
    val lastError: Throwable?
        get() {
            return lastElement.lastError
        }

    /**
     * skipScenario
     */
    var skipScenario: Boolean = false

    /**
     * skipCase
     */
    var skipCase: Boolean = false

    /**
     * skip
     */
    val skip: Boolean
        get() {
            return skipScenario || skipCase
        }

    /**
     * implicitlyWaitSeconds
     */
    var implicitlyWaitSeconds: Double = 0.0
        set(value) {
            if (TestMode.isNoLoadRun) {
                return
            }

            if (mAppiumDriver == null) {
                throw InstantiationException("appiumDriver")
            }
            if (field == value) {
                return
            }
            field = value
            try {
                appiumDriver.manage().timeouts().implicitlyWait(Duration.ofMillis((value * 1000).toLong()))
            } catch (t: Throwable) {
                TestLog.warn("Failed to set implicitlyWaitSeconds. ${t.toString()}")
            }
        }

    /**
     * shouldTakeScreenshot
     */
    val shouldTakeScreenshot: Boolean
        get() {
            return CAEPattern.shouldTakeScreenshot && TestDriverCommandContext.shouldTakeScreenshot
        }

    /**
     * androidDriver
     */
    val androidDriver: AndroidDriver
        get() {
            if (isAndroid.not()) {
                throw TestDriverException("Property androidDriver is for Android")
            }
            return appiumDriver as AndroidDriver
        }

    /**
     * iosDriver
     */
    val iosDriver: IOSDriver
        get() {
            if (isiOS.not()) {
                throw TestDriverException("Property iosDriver is for iOS")
            }
            return appiumDriver as IOSDriver
        }

    /**
     * setupContext
     */
    fun setupContext(testContext: TestContext) {

        val profile = testContext.profile

        this.testContext = testContext

        TestLog.trace("Setting up test context. (configFile=${profile.testConfigPath}, profileName=${profile.profileName})")

        lastElement = TestElement.emptyElement

        TestLog.info("Initializing TestDriver.(profileName=${testContext.profile.profileName})")

        TestLog.info("noLoadRun: ${TestMode.isNoLoadRun}")
        TestLog.info("boundsToRectRatio: ${testContext.boundsToRectRatio}")
        TestLog.info("reuseDriver: ${testContext.reuseDriver}")
        TestLog.info("autoScreenshot: ${testContext.autoScreenshot}")
        TestLog.info("onChangedOnly: ${testContext.onChangedOnly}")
        TestLog.info("onCondition: ${testContext.onCondition}")
        TestLog.info("onAction: ${testContext.onAction}")
        TestLog.info("onExpectation: ${testContext.onExpectation}")
        TestLog.info("onExecOperateCommand: ${testContext.onExecOperateCommand}")
        TestLog.info("onCheckCommand: ${testContext.onCheckCommand}")
        TestLog.info("onScrolling: ${testContext.onScrolling}")
        TestLog.info("manualScreenshot: ${testContext.manualScreenshot}")
        TestLog.info("retryMaxCount: ${testContext.retryMaxCount}")
        TestLog.info("retryIntervalSeconds: ${testContext.retryIntervalSeconds}")
        TestLog.info("shortWaitSeconds: ${testContext.shortWaitSeconds}")
        TestLog.info("waitSecondsOnIsScreen: ${testContext.waitSecondsOnIsScreen}")
        TestLog.info("waitSecondsForLaunchAppComplete: ${testContext.waitSecondsForLaunchAppComplete}")
        TestLog.info("waitSecondsForAnimationComplete: ${testContext.waitSecondsForAnimationComplete}")
        if (isAndroid) {
            TestLog.info("waitSecondsForConnectionEnabled: ${testContext.waitSecondsForConnectionEnabled}")
        }
        TestLog.info("swipeDurationSeconds: ${testContext.swipeDurationSeconds}")
        TestLog.info("flickDurationSeconds: ${testContext.flickDurationSeconds}")
        TestLog.info("swipeMarginRatio: ${testContext.swipeMarginRatio}")
        TestLog.info("scrollVerticalStartMarginRatio: ${testContext.scrollVerticalStartMarginRatio}")
        TestLog.info("scrollVerticalEndMarginRatio: ${testContext.scrollVerticalEndMarginRatio}")
        TestLog.info("scrollHorizontalStartMarginRatio: ${testContext.scrollHorizontalStartMarginRatio}")
        TestLog.info("scrollHorizontalEndMarginRatio: ${testContext.scrollHorizontalEndMarginRatio}")
        TestLog.info("tapHoldSeconds: ${testContext.tapHoldSeconds}")
        TestLog.info("tapAppIconMethod: ${testContext.tapAppIconMethod}")
        TestLog.info("tapAppIconMacro: ${testContext.tapAppIconMacro}")
        TestLog.info("enableCache: ${testContext.enableCache}")
        TestLog.info("syncWaitSeconds: ${testContext.syncWaitSeconds}")
    }

    internal fun postProcessForAssertion(
        selectResult: TestElement,
        assertMessage: String,
        log: Boolean = CodeExecutionContext.shouldOutputLog,
        dontExist: Boolean = false
    ) {

        val e = selectResult
        if (dontExist && e.isEmpty || dontExist.not() && e.isFound) {
            e.lastResult = TestLog.getOKType()
            if (log) {
                TestLog.ok(message = assertMessage)
            }
        } else {
            e.lastResult = LogType.NG
            val selectorString = "${selectResult.selector} ($currentScreen})"
            e.lastError = TestNGException(assertMessage, TestDriverException(selectorString))
        }
    }

    internal fun postProcessForImageAssertion(
        e: TestElement,
        assertMessage: String,
        log: Boolean = CodeExecutionContext.shouldOutputLog,
        dontExist: Boolean = false
    ) {
        val imageMatchResult = e.imageMatchResult
        if (imageMatchResult != null) {
            if (dontExist && imageMatchResult.result.not() || dontExist.not() && imageMatchResult.result) {
                e.lastResult = TestLog.getOKType()
                if (log) {
                    TestLog.ok(message = assertMessage)
                }
            }
        } else {
            e.lastResult = LogType.NG
            val selectorString = "${e.selector} ($currentScreen})"
            e.lastError = TestNGException(message = assertMessage, cause = TestDriverException(selectorString))
        }
    }

    /**
     * clearContext
     */
    fun clearContext() {

        skipScenario = false
        skipCase = false
        lastTestContext = testContext
        testContext = TestContext()
    }

    /**
     * quit
     */
    fun quit() {

        TestLog.info("Quitting TestDriver.")
        val ms = Measure()
        invalidateCache()
        try {
            mAppiumDriver?.quit()
        } catch (t: Throwable) {
            if (t.message!!.contains("Connection refused").not()) {
                throw t
            }
        } finally {
            mAppiumDriver = null
            ms.end()
        }

        clearContext()
    }

    /**
     * currentScreen
     */
    var currentScreen: String = ""
        get() {
            if (TestMode.testTimeScreen != null) {
                return TestMode.testTimeScreen!!
            } else {
                return field
            }
        }
        set(value) {
            field = value
            fireScreenHandler(screenName = value)
        }

    internal fun getOverlayElements(): MutableList<TestElement> {
        val list = mutableListOf<TestElement>()
        for (overlaySelector in screenInfo.scrollInfo.overlayElements) {
            val o = select(expression = overlaySelector, throwsException = false)
            if (o.isFound) {
                list.add(o)
            }
        }
        return list
    }

    /**
     * expandExpression
     */
    fun expandExpression(
        expression: String,
        screenName: String = currentScreen
    ): Selector {

        if (screenName.isBlank() || ScreenRepository.has(screenName).not()) {
            return screenInfo.expandExpression(expression = expression)
        }
        return ScreenRepository.getScreenInfo(screenName).expandExpression(expression)
    }

    /**
     * screenInfo
     */
    val screenInfo: ScreenInfo
        get() {
            return if (ScreenRepository.has(currentScreen)) ScreenRepository.get(currentScreen)
            else if (ScreenRepository.has("[screen-base]")) ScreenRepository.getScreenInfo("[screen-base]")
            else ScreenInfo()
        }

    private var lastFireIrregularHandlerScreenshotImage: BufferedImage? = null

    var isFiringIrregularHandler = false

    /**
     * fireIrregularHandler
     */
    fun fireIrregularHandler(force: Boolean = false) {

        if (force.not()) {
            if (isFiringIrregularHandler || testContext.enableIrregularHandler.not() || TestMode.isNoLoadRun || isInitialized.not()) {
                return
            }
        }

        lastFireIrregularHandlerScreenshotImage = null

        fireIrregularHandlerCore()
    }

    private fun fireIrregularHandlerCore(): Boolean {
        try {
            isFiringIrregularHandler = true

            if (testContext.useCache) {
                if (TestElementCache.synced && CodeExecutionContext.lastScreenshotImage == lastFireIrregularHandlerScreenshotImage) {
                    TestLog.trace("firing irregularHandler skipped")
                    return false
                }
                syncCache()
                lastFireIrregularHandlerScreenshotImage = CodeExecutionContext.lastScreenshotImage
            }

            val originalLastElement = lastElement
            try {
                testContext.irregularHandler?.invoke()
            } catch (t: Throwable) {
                TestLog.error(t)
                throw t
            } finally {
                lastElement = originalLastElement.refreshThisElement()
            }
        } finally {
            isFiringIrregularHandler = false
        }

        val handled = CodeExecutionContext.lastScreenshotImage == lastFireIrregularHandlerScreenshotImage
        return handled
    }

    var firingScreenHandlerScreens = mutableListOf<String>()

    /**
     * fireScreenHandler
     */
    fun fireScreenHandler(screenName: String): TestDriverOnScreenContext {

        val context = TestDriverOnScreenContext(screenName = screenName)

        if (firingScreenHandlerScreens.contains(screenName)) {
            return context
        }

        try {
            firingScreenHandlerScreens.add(screenName)

            if (testContext.enableScreenHandler.not()) {
                return context
            }

            if (testContext.screenHandlers.containsKey(screenName)) {
                val handler = testContext.screenHandlers[screenName]!!
                context.fired = true
                handler(context)
                if (context.keep.not()) {
                    testDrive.removeScreenHandler(screenName)
                }
            }
        } finally {
            if (firingScreenHandlerScreens.contains(screenName)) {
                firingScreenHandlerScreens.remove(screenName)
            }
        }
        return context
    }

    /**
     * createAppiumDriver
     */
    fun createAppiumDriver(profile: TestProfile = testContext.profile) {

        if (TestMode.isNoLoadRun) {
            return
        }

        val ms = Measure()

        lockFile(filePath = UserVar.downloads.resolve(".createAppiumDriver")) {
            createAppiumDriverCore(profile = profile)
        }

        ms.end()
        TestLog.info("AppiumDriver initialized.")
    }

    private fun wdaInstallOptimization(profile: TestProfile) {
        TestLog.info("Optimizing installing WebDriverAgent.")
        val wdaDirectory = IosDeviceUtility.getWebDriverAgentDirectory()
        if (Files.exists(wdaDirectory.toPath()).not()) {
            TestLog.info("WebDriverAgent directory not found. Optimization skipped.")
            return
        }
        // Check existence of app file
        val appFiles = File(wdaDirectory).walkTopDown().filter { it.name == "WebDriverAgentRunner-Runner.app" }
        if (appFiles.any().not()) {
            TestLog.info("WebDriverAgentRunner-Runner.app not found. Optimization skipped.")
            return
        }

        TestLog.info("Using prebuiltWDA in $wdaDirectory")
        profile.capabilities.set("appium:usePrebuiltWDA", true)
        profile.capabilities.set("appium:derivedDataPath", wdaDirectory)
    }

    private fun createAppiumDriverCore(profile: TestProfile) {

        if (isiOS && PropertiesManager.enableWdaInstallOptimization) {
            wdaInstallOptimization(profile = profile)
        }

        val capabilities = DesiredCapabilities()
        setCapabilities(profile, capabilities)

        val retryMaxCount = 3
        val retryContext = RetryUtility.exec(
            retryMaxCount = retryMaxCount.toLong(),
            retryTimeoutSeconds = testContext.retryTimeoutSeconds,
            retryPredicate = getRetryPredicate(profile = profile, capabilities = capabilities),
            onBeforeRetry = getBeforeRetry(profile = profile, capabilities = capabilities),
            action = getInitFunc(profile = profile, capabilities = capabilities)
        )

        // Write log on error
        val exception = retryContext.exception
        if (exception != null) {
            if (retryContext.wrappedError != null) {
                TestLog.error(retryContext.wrappedError!!)
                TestLog.write(retryContext.wrappedError.toString())
                throw retryContext.wrappedError!!
            } else {
                TestLog.error(exception)
                TestLog.write(exception.toString())
                throw exception
            }
        }

        // Save capabilities
        initialCapabilities.clear()
        val capMap = appiumDriver.capabilities.asMap()
        for (key in capMap.keys) {
            initialCapabilities[key] = capMap[key].toString()
        }

        // implicitlyWaitSeconds
        implicitlyWaitSeconds =
            profile.implicitlyWaitSeconds?.toDoubleOrNull() ?: Const.IMPLICITLY_WAIT_SECONDS
        TestLog.info("implicitlyWaitSeconds: ${implicitlyWaitSeconds}")

        // Settings(Android)
        if (isAndroid) {
            for (s in profile.settings) {
                val value = AndroidMobileShellUtility.setValue(name = s.key, value = s.value)
                TestLog.info("(settings) ${s.key}: ${value}".trimEnd())
            }
        }

        // Check Felica
        if (isAndroid) {
            try {
                val map = AndroidMobileShellUtility.getMap(command = "pm", "list", "packages")
                val packages = AndroidMobileShellUtility.executeMobileShell(map)
                    .toString().replace("package:", "").trim().split("\n").sorted()
                profile.packages.addAll(packages)
                profile.hasFelica = profile.packages.any() { it.startsWith("com.felicanetworks.") }
            } catch (t: Throwable) {
                TestLog.warn(message(id = "findingFelicaPackageFailed", arg1 = "${t.message}"))
            }
        }

        profile.completeProfile()
    }

    private fun setCapabilities(
        profile: TestProfile,
        capabilities: DesiredCapabilities
    ) {
        val keys = profile.capabilities.keys
        if (isiOS) {
            if (keys.contains("appium:avd")) {
                keys.remove("appium:avd")
            }
        }
        for (key in keys) {
            // comment mark
            if (key.startsWith("#")) continue
            if (key.startsWith("//")) continue

            capabilities.setCapabilityStrict(key, profile.capabilities[key])
        }
        val capabilityNames = capabilities.capabilityNames
        // newCommandTimeout
        if (capabilityNames.any() { it.endsWith("newCommandTimeout") }.not()) {
            capabilities.setCapabilityStrict("newCommandTimeout", 300)
        }
        // App package
        if (profile.packageOrBundleId == profile.startupPackageOrBundleId &&
            profile.appPackageFile.isNullOrBlank().not()
        ) {
            capabilities.setCapabilityStrict("app", profile.appPackageFullPath)
        }
    }

    private fun getInitFunc(
        profile: TestProfile,
        capabilities: DesiredCapabilities
    ): (RetryContext<Unit>) -> Unit {

        val appiumServerUrl = URL(profile.appiumServerUrl)
        TestLog.info(message(id = "connectingToAppiumServer", subject = "$appiumServerUrl"))

        val initFunc: (RetryContext<Unit>) -> Unit = { context ->
            val connectionRefused = context.lastException?.message?.contains("Connection refused") ?: false
            if (connectionRefused) {
                AppiumServerManager.close()
            }
            val force = connectionRefused
            AppiumServerManager.setupAppiumServerProcess(
                sessionName = TestLog.currentTestClassName,
                profile = profile,
                force = force
            )
            if (isAndroid) {
                mAppiumDriver = AndroidDriver(appiumServerUrl, capabilities)
                healthCheckForAndroid(profile = profile)
            } else {
                TestLog.info(message(id = "initializingIosDriverMayTakeMinutes"))
                mAppiumDriver = IOSDriver(appiumServerUrl, capabilities)
            }
        }
        return initFunc
    }

    private fun getRetryPredicate(
        profile: TestProfile,
        capabilities: DesiredCapabilities
    ): (RetryContext<Unit>) -> Boolean {

        val retryPredicate: (RetryContext<Unit>) -> Boolean = { context ->
            retryPredicateCore(profile = profile, context = context, capabilities = capabilities)
        }
        return retryPredicate
    }

    private fun restartAndroid(
        profile: TestProfile,
        terminateEmulatorProcess: Boolean
    ) {
        val ms = Measure()

        // Restart device
        restartAndroidDevice(profile = profile, terminateEmulatorProcess = terminateEmulatorProcess)

        // Restart Appium Server
        TestLog.info("Restarting AppiumServer.")
        AppiumServerManager.restartAppiumProcess()

        ms.end()
    }

    private fun restartAndroidDevice(profile: TestProfile, terminateEmulatorProcess: Boolean) {

        TestLog.info("profile=${profile.profileName}")
        TestLog.info("terminateEmulatorProcess=$terminateEmulatorProcess")

        if (profile.udid.isBlank()) {
            TestLog.info("Starting Android device. (${profile.profileName})")
            AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(testProfile = profile)
            return
        }

        val pid = run {
            val port = profile.udid.removePrefix("emulator-").toIntOrNull()
            if (port == null) {
                TestLog.warn("Could not get port number. (udid=${profile.udid})")
                null
            } else {
                ProcessUtility.getPid(port = port)
            }
        }
        TestLog.info("pid=$pid")

        if (terminateEmulatorProcess && pid != null) {
            TestLog.info("Terminating Android device. (pid=$pid)")
            ProcessUtility.terminateProcess(pid = pid)
        }
        if (terminateEmulatorProcess || pid == null) {
            TestLog.info("Starting Android device. (${profile.profileName})")
            AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(testProfile = profile)
        } else {
            TestLog.info("Rebooting Android device. (${profile.udid})")
            try {
                if (TestMode.isEmulator) {
                    AndroidDeviceUtility.restartEmulator(profile)
                } else {
                    AndroidDeviceUtility.reboot(udid = profile.udid)
                }
            } catch (t: Throwable) {
                if (t.message!!.contains("device offline")) {
                    AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(testProfile = profile)
                } else {
                    throw TestDriverException("Failed rebooting Android device. ($t)", cause = t)
                }
            }
        }

    }

    private fun restartIos(profile: TestProfile) {

        if (isSimulator) {
            TestLog.info("Restarting Simulator.")
            IosDeviceUtility.restartSimulator(udid = profile.udid)
        } else {
            // NOP
        }
    }

    private fun getBeforeRetry(
        profile: TestProfile,
        capabilities: DesiredCapabilities
    ): (RetryContext<Unit>) -> Unit {

        val beforeRetry: (RetryContext<Unit>) -> Unit = { context ->
            val message = context.exception?.message ?: ""
            if (message.isNotBlank()) {
                TestLog.info("Retry cause: $message")
            }

            if (isAndroid) {
                // ex.
                // socket hang up
                // cannot be proxied to UiAutomator2 server because the instrumentation process is not running (probably crashed)

                val terminateEmulatorProcess = (context.retryCount == context.retryMaxCount)
                restartAndroid(profile = profile, terminateEmulatorProcess = terminateEmulatorProcess)
            } else if (isiOS) {
                val udid = initialCapabilities["udid"] ?: profile.udid
                if (udid.isBlank()) {
                    throw TestDriverException("udid not found.")
                }
                TestLog.info("udid found in initialCapabilities. (udid=$udid)")

                restartIos(profile)
            }
        }
        return beforeRetry
    }

    fun retryPredicateCore(
        profile: TestProfile,
        context: RetryContext<Unit>,
        capabilities: DesiredCapabilities
    ): Boolean {
        val message = context.exception?.message ?: ""
        if (message.contains("The requested resource could not be found")) {
            val msg = message(id = "TheRequestedResourceCouldNotBeFound", subject = profile.appiumServerUrl)
            context.wrappedError = TestEnvironmentException(
                message = msg,
                cause = context.exception
            )
            return true
        } else if (message.contains("Unable to find an active device or emulator")) {
            context.wrappedError = TestEnvironmentException(
                message = message(id = "unableToFindAnActiveDevice", arg1 = profile.profileName),
                cause = context.exception
            )
            val p = profile
            TestLog.info("deviceName:${p.deviceName}")
            TestLog.info("platformName:${p.platformName}")
            TestLog.info("platformVersion:${p.platformVersion}")
            return true
        } else if (message.contains("'app' option is required for reinstall")) {
            val pkg = capabilities.getCapability("appPackage")
            context.wrappedError = TestEnvironmentException(
                message = message(id = "appIsNotInstalled", subject = "$pkg"),
                cause = context.exception
            )
            return false
        } else if (message.contains("Could not find 'adb.exe' in PATH. Please set the ANDROID_HOME or ANDROID_SDK_ROOT environment variables")) {
            context.wrappedError = TestEnvironmentException(
                message = message(id = "adbNotFound"),
                cause = context.exception
            )
            return false
        } else if (message.contains("Could not find a connected Android device")) {
            context.wrappedError = TestEnvironmentException(
                message = message(id = "couldNotFindConnectedDevice", arg1 = profile.profileName),
                cause = context.exception
            )
            return true
        } else if (message.contains("device unauthorized")) {
            context.wrappedError = TestEnvironmentException(
                message = message(id = "deviceUnauthorized"),
                cause = context.exception
            )
            return true
        } else if (message.contains("was not in the list of connected devices")) {
            val udid = profile.capabilities.getOrDefault("udid", "(not set)")?.toString()
            context.wrappedError = TestEnvironmentException(
                message = message(id = "deviceNotConnected", subject = profile.deviceName, arg1 = udid),
                cause = context.exception
            )
            return true
        } else if (message.contains("App with bundle identifier") && message.contains("unknown")) {
            val bundleId = profile.capabilities.getOrDefault("bundleId", "(not set)")?.toString()
            context.wrappedError = TestConfigException(
                message = message(id = "unknownBundleId", subject = bundleId),
                cause = context.exception
            )
            return false
        } else if (message.contains("Error getting device platform version.")) {
            val msg = message(id = "adbFailedGettingDevicePlatformVersion")
            context.wrappedError = TestEnvironmentException(message = msg, cause = context.exception)
            return false
        } else if (message.startsWith("Could not start a new session. Possible causes are invalid address")) {
            val msg = message(id = "couldNotStartANewSession")
            context.wrappedError = TestEnvironmentException(message = msg, cause = context.exception)
            return false
        } else {
            TestLog.warn(message)
            TestLog.outputLogDump()
            return true
        }
    }

    private fun healthCheckForAndroid(profile: TestProfile) {
        /**
         * Check udid.
         * Sometimes udid may not be captured.
         */
        val udid = mAppiumDriver!!.capabilities.getUdid()
        if (udid.isBlank()) {
            val msg = "udid not found in capablities."
            TestLog.warn(msg)
            throw TestDriverException(msg)
        }
        if (profile.udid.isNotBlank() && profile.udid != udid) {
            throw TestDriverException("Connected udid is not correct. (profile.udid=${profile.udid}, actual=$udid)")
        }

        /**
         * Health check
         */
        if (PropertiesManager.enableHealthCheck) {
            TestLog.info("[Health check] start")

            try {
                syncCache(force = true, syncWaitSeconds = testContext.waitSecondsOnIsScreen)     // throws on fail
                val tapTestElement = testDrive.select(PropertiesManager.tapTestSelector)
                if (tapTestElement.isFound) {
                    TestLog.info("tap${tapTestElement.selector}")
                    testDrive.silent {
                        tapTestElement.tap()   // throws on fail
                    }
                }
                androidDriver.getScreenshotAs(OutputType.BYTES)   // throws on fail
                androidDriver.pressKey(KeyEvent(AndroidKey.CLEAR))   // throws on fail
            } catch (t: Throwable) {
                val e = TestDriverException("[Health Check] failed. ${t.message ?: ""}", cause = t)
                TestLog.warn(e.message)
                throw e
            }

            TestLog.info("[Health check] end")
        }
    }

    private var isRefreshing = false

    /**
     * refreshCache
     */
    fun refreshCache(currentScreenRefresh: Boolean = true): TestDriver {

        if (isInitialized.not()) {
            return this
        }
        if (testContext.useCache.not()) {
            return this
        }

        val ms = Measure("refreshCache")
        try {
            isRefreshing = true

            TestDriveObject.suppressHandler {
                val lastXml = TestElementCache.sourceXml
                CodeExecutionContext.lastScreenshotImage = null

                rootElement = AppiumProxy.getSource()

                TestElementCache.synced = (TestElementCache.sourceXml == lastXml)

                if (isAndroid) {
                    rootElement.sourceCaptureFailed = false
                } else {
                    val windowElements = TestElementCache.allElements.filter { it.type == "XCUIElementTypeWindow" }
                    rootElement.sourceCaptureFailed = windowElements.count() < 3
                }

                if (currentScreenRefresh) {
                    refreshCurrentScreen()
                } else {
                    currentScreen = "?"
                }

                if (lastElement.selector != null) {
                    lastElement = lastElement.refreshLastElement()
                } else {
                    lastElement = rootElement
                }

            }
        } finally {
            isRefreshing = false
            ms.end()
        }

        return this
    }

    /**
     * refreshCacheOnInvalidated
     */
    fun refreshCacheOnInvalidated() {

        if (TestElementCache.synced) {
            return
        }

        refreshCache()
    }

    /**
     * clearLast
     */
    fun clearLast() {

        lastElement = TestElement.emptyElement
    }

    /**
     * capabilities
     */
    val capabilities: Capabilities
        get() {
            if (isInitialized.not()) {
                throw TestDriverException("Failed to get capabilities. TestDriver is not initialized.")
            }
            return mAppiumDriver!!.capabilities
        }

    /**
     * select
     */
    fun select(
        expression: String,
        scroll: Boolean = false,
        direction: ScrollDirection = ScrollDirection.Down,
        scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
        scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
        scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
        scrollMaxCount: Int = testContext.scrollMaxCount,
        waitSeconds: Double = testContext.syncWaitSeconds,
        throwsException: Boolean = true,
        frame: Bounds? = null,
        useCache: Boolean = testContext.useCache,
        log: Boolean = false
    ): TestElement {

        refreshCurrentScreenWithNickname(expression)

        val sel = expandExpression(expression = expression)
        var e = TestElement(selector = sel)
        val context = TestDriverCommandContext(lastElement)
        context.execSelectCommand(selector = sel, subject = sel.nickname, log = log) {
            e = select(
                selector = sel,
                scroll = scroll,
                direction = direction,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
                waitSeconds = waitSeconds,
                throwsException = throwsException,
                frame = frame,
                useCache = useCache,
            )
        }

        return e
    }

    internal fun select(
        selector: Selector,
        scroll: Boolean = false,
        direction: ScrollDirection = ScrollDirection.Down,
        scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
        scrollStartMarginRatio: Double = testContext.scrollStartMarginRatio(direction),
        scrollEndMarginRatio: Double = testContext.scrollEndMarginRatio(direction),
        scrollMaxCount: Int = testContext.scrollMaxCount,
        waitSeconds: Double = testContext.syncWaitSeconds,
        throwsException: Boolean = true,
        frame: Bounds? = null,
        useCache: Boolean = testContext.useCache,
    ): TestElement {

        fun executeSelect(): TestElement {
            return selectCore(
                selector = selector,
                useCache = useCache,
                scroll = scroll,
                direction = direction,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
                throwsException = throwsException,
                waitSeconds = waitSeconds,
                frame = frame
            )
        }

        /**
         * Execute with error handler
         */
        if (throwsException && testContext.enableIrregularHandler && testContext.onSelectErrorHandler != null) {
            return try {
                executeSelect()
            } catch (t: Throwable) {
                TestLog.info(t.message!!)
                testDrive.suppressHandler {
                    testContext.onSelectErrorHandler!!.invoke()
                }
                executeSelect()
            }
        }

        return executeSelect()

    }

    private fun selectCore(
        selector: Selector,
        frame: Bounds?,
        useCache: Boolean,
        scroll: Boolean,
        direction: ScrollDirection,
        scrollDurationSeconds: Double,
        scrollStartMarginRatio: Double,
        scrollEndMarginRatio: Double,
        scrollMaxCount: Int,
        throwsException: Boolean,
        waitSeconds: Double
    ): TestElement {
        if (selector.isRelative) {
            return TestElementCache.select(
                selector = selector,
                throwsException = false
            )
        }
        if (selector.isImageSelector) {
            throw TestDriverException(message(id = "imageSelectorNotSupported"))
        }

        lastElement = TestElement.emptyElement

        var selectedElement: TestElement

        val originalForceUseCache = testContext.forceUseCache
        try {
            val hasMatches = selector.hasMatches
            testContext.forceUseCache = hasMatches

            // Search in current screen
            selectedElement = if (useCache || hasMatches) {
                syncCache()
                TestElementCache.select(
                    selector = selector,
                    throwsException = false,
                    frame = frame
                )
            } else {
                selectDirect(
                    selector = selector,
                    throwsException = false
                )
            }
            if (selector.isNegation.not()) {
                if (selectedElement.isFound && selectedElement.isInView) {
                    lastElement = selectedElement
                    return lastElement
                }
            }

            // Search in scroll
            if (scroll) {
                return selectWithScroll(
                    selector = selector,
                    frame = frame,
                    direction = direction,
                    durationSeconds = scrollDurationSeconds,
                    startMarginRatio = scrollStartMarginRatio,
                    endMarginRatio = scrollEndMarginRatio,
                    scrollMaxCount = scrollMaxCount,
                    throwsException = throwsException,
                )
            }

            // Wait for seconds
            if (waitSeconds > 0 && isInitialized) {
                // Search until it is(not) found
                val r = SyncUtility.doUntilTrue(
                    waitSeconds = waitSeconds,
                    refreshCache = useCache,
                    throwOnError = false,
                    onBeforeRetry = { sc ->
                        if (testContext.enableIrregularHandler && testContext.onSelectErrorHandler != null) {
                            testContext.onSelectErrorHandler!!.invoke()
                        }
                    }
                ) { sc ->
                    val e = if (useCache) {
                        TestElementCache.select(
                            selector = selector,
                            throwsException = false
                        )
                    } else {
                        selectDirect(
                            selector = selector,
                            throwsException = false,
                        )
                    }
                    selectedElement = e
                    val foundAndIsInView = e.isFound && e.isInView
                    val result = if (selector.isNegation) {
                        foundAndIsInView.not()
                    } else {
                        foundAndIsInView
                    }

                    result
                }
                if (r.isTimeout) {
                    if (PropertiesManager.enableWarnOnSelectTimeout) {
                        TestLog.warn(
                            message(
                                id = "timeoutInSelect",
                                subject = "select",
                                arg1 = "$selector",
                                submessage = "${r.error?.message}"
                            )
                        )
                    }
                }

                if (r.hasError) {
                    lastElement.lastError = r.error

                    if (selectedElement.hasImageMatchResult && selectedElement.imageMatchResult!!.result.not()) {
                        TestLog.warn(message(id = "imageNotFound", subject = "${selectedElement.imageMatchResult}"))
                    }
                }
            }

            lastElement = selectedElement

            if (selectedElement.hasError) {
                selectedElement.lastResult = LogType.ERROR
                if (throwsException) {
                    throw selectedElement.lastError!!
                }
            }

        } finally {
            testContext.forceUseCache = originalForceUseCache
        }

        return lastElement
    }

    internal fun selectDirect(
        selector: Selector,
        throwsException: Boolean,
        frame: Bounds? = null
    ): TestElement {

        if (TestMode.isNoLoadRun) {
            lastElement = TestElement(selector = selector)
            return lastElement
        }

        val ms = Measure("selectDirect")

        val e = try {
            var elm: TestElement? = null
            if (isAndroid || selector.xpath != null || selector.className == "XCUIElementTypeApplication") {
                val fullXPathCondition = selector.getFullXPathCondition()
                if (fullXPathCondition.isNotBlank()) {
                    elm = selectDirectByXPath(
                        selector = selector,
                        fullXPathCondition = fullXPathCondition,
                        frame = frame
                    )
                }
            } else if (isiOS) {
                val iosClassChain = selector.getIosClassChain()
                if (iosClassChain.isNotBlank()) {
                    elm = selectDirectByIosClassChain(selector = selector, frame = frame)
                }
            }
            if (elm == null) {
                val baseElement =
                    testDrive.findWebElement(selector = selector, widgetOnly = false)
                if (baseElement.isEmpty) {
                    elm = baseElement
                } else {
                    if (selector.relativeSelectors.any()) {
                        val exps = mutableListOf(baseElement.classAlias)
                        for (sel in selector.relativeSelectors) {
                            val c = sel.command!!
                            if (c.lowercase().contains("input")) {
                                exps.add(".input")
                            }
                            if (c.lowercase().contains("label")) {
                                exps.add(".label")
                            }
                            if (c.lowercase().contains("button")) {
                                exps.add(".button")
                            }
                            if (c.lowercase().contains("image")) {
                                exps.add(".image")
                            }
                            if (c.lowercase().contains("switch")) {
                                exps.add(".switch")
                            }
                        }
                        val expandedExps = exps.map { ElementCategoryExpressionUtility.expandWidget(it) }
                        val exp =
                            if (expandedExps.isEmpty()) ".widget"
                            else "className=(${expandedExps.joinToString("|")})"
                        val scopeElements = testDrive.findWebElements(expression = exp)
                            .filter { it.isInView }
                        elm = baseElement.getRelative(scopeElements = scopeElements)
                    } else {
                        elm = baseElement
                    }
                }
            }
            if (elm.isFound && frame != null && elm.bounds.isIncludedIn(frame).not()) {
                elm = TestElement.emptyElement
            }
            elm.selector = selector
            elm
        } catch (t: Throwable) {
            TestLog.warn("Exception in selectDirect. selector=$selector, ${t.message}")
            TestElement(selector = selector)
        }
        if (e.isEmpty) {
            e.lastError = TestDriverException(
                message = message(
                    id = "elementNotFound",
                    subject = "$selector",
                    arg1 = selector.getElementExpression()
                )
            )
            if (throwsException) {
                throw e.lastError!!
            }
        }
        ms.end()
        return e
    }

    private fun selectDirectByXPath(
        selector: Selector,
        fullXPathCondition: String,
        frame: Bounds? = null
    ): TestElement {

        val ms = Measure("selectDirectByXPath")
        try {
            if (selector.pos == null || selector.pos == 1) {
                if (isiOS) {
                    val xpath = "//*$fullXPathCondition[@visible='true']"
                    return testDrive.findWebElementBy(
                        locator = By.xpath(xpath),
                        timeoutMilliseconds = 0,
                        frame = frame
                    )
                } else {
                    val xpath = "//*$fullXPathCondition"
                    return testDrive.findWebElementBy(
                        locator = By.xpath(xpath),
                        timeoutMilliseconds = 0,
                        frame = frame
                    )
                }
            }

            val xpath = "//*$fullXPathCondition"
            val elements = testDrive.findWebElementsBy(
                locator = By.xpath(xpath),
                timeoutMilliseconds = 0,
                frame = frame
            )
            val pos = selector.pos!!
            if (pos <= elements.count()) {
                return elements[pos - 1]
            } else {
                return TestElement.emptyElement
            }
        } finally {
            ms.end()
        }
    }

    private fun getPredicateFromClassChain(classChain: String): String {

        return classChain.removePrefix("**/*").removePrefix("[`").removeSuffix("`]")
    }

    internal fun selectDirectByIosClassChain(
        selector: Selector,
        frame: Bounds? = null
    ): TestElement {

        val classChain = selector.getIosClassChain()
        val ms = Measure(classChain)
        val element = testDrive.findWebElementBy(
            locator = AppiumBy.iOSClassChain(classChain),
            timeoutMilliseconds = 0,
            frame = frame
        )
        ms.end()
        return element
    }

    /**
     * findImage
     */
    fun findImage(
        expression: String,
        scroll: Boolean = false,
        direction: ScrollDirection = ScrollDirection.Down,
        scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
        scrollStartMarginRatio: Double = testContext.scrollStartMarginRatio(direction),
        scrollEndMarginRatio: Double = testContext.scrollEndMarginRatio(direction),
        scrollMaxCount: Int = testContext.scrollMaxCount,
        throwsException: Boolean = true,
        useCache: Boolean = testContext.useCache,
    ): ImageMatchResult {

        val sel = expandExpression(expression = expression)

        return findImage(
            selector = sel,
            scroll = scroll,
            direction = direction,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
            throwsException = throwsException,
            useCache = useCache
        )
    }

    internal fun findImage(
        selector: Selector,
        threshold: Double = PropertiesManager.imageMatchingThreshold,
        scroll: Boolean,
        direction: ScrollDirection,
        scrollDurationSeconds: Double,
        scrollStartMarginRatio: Double,
        scrollEndMarginRatio: Double,
        scrollMaxCount: Int,
        throwsException: Boolean,
        waitSeconds: Double = testContext.syncWaitSeconds,
        useCache: Boolean
    ): ImageMatchResult {

        lastElement = TestElement.emptyElement

        if (selector.image.isNullOrBlank()) {
            if (selector.nickname.isNullOrBlank()) {
                throw IllegalArgumentException(message(id = "imageFileNotFound", subject = selector.expression))
            }
            selector.image = "${selector.nickname}.png"
        }
        val filePath = runCatching {
            ImageFileRepository.getFilePath(selector.image!!)
        }.getOrNull()
        if (filePath == null || Files.exists(filePath).not()) {
            if (throwsException) {
                throw FileNotFoundException(message(id = "imageFileNotFound", subject = selector.image))
            }
            return ImageMatchResult(result = false, templateSubject = selector.image)
        }

        // Search in current screen
        if (useCache) {
            syncCache()
        }

        var r = rootElement.isContainingImage(selector.image!!, threshold = threshold)
        if (r.result) {
            return r
        }
        if (scroll.not() && testContext.enableIrregularHandler && testContext.onExistErrorHandler != null) {
            // Handle irregular
            testDrive.suppressHandler {
                testContext.onExistErrorHandler!!.invoke()
            }
            // Retry
            r = rootElement.isContainingImage(selector.image!!)
            if (r.result) {
                return r
            }
        }

        if (scroll) {
            // Search in scroll
            val actionFunc = {
                r = rootElement.isContainingImage(selector.image!!)
                r.result
            }

            testDrive.doUntilScrollStop(
                repeat = 1,
                maxLoopCount = scrollMaxCount,
                direction = direction,
                durationSeconds = scrollDurationSeconds,
                startMarginRatio = scrollStartMarginRatio,
                endMarginRatio = scrollEndMarginRatio,
                actionFunc = actionFunc
            )
        } else {
            // Wait for image displayed
            SyncUtility.doUntilTrue(
                waitSeconds = waitSeconds,
                intervalSeconds = testContext.shortWaitSeconds,
                refreshCache = useCache
            ) { sc ->
                if (sc.refreshCache.not()) {
                    screenshot(force = true)
                }
                r = rootElement.isContainingImage(selector.image!!)
                r.result
            }
        }

        if (r.result.not() && throwsException) {
            val msg = message(id = "imageNotFound", subject = selector.toString())
            throw TestDriverException(message = msg)
        }

        return r
    }

    /**
     * printCapabilities
     */
    fun printCapabilities() {

        if (TestMode.isNoLoadRun || mAppiumDriver == null) {
            return
        }

        TestLog.write("(capabilities)")
        for (i in appiumDriver.capabilities.asMap()) {
            if (i.key != "appium:desired") {
                ParameterRepository.write(i.key, "${i.value}")
            }
        }
    }

    /**
     * selectWithScroll
     */
    fun selectWithScroll(
        selector: Selector,
        frame: Bounds? = viewBounds,
        direction: ScrollDirection = ScrollDirection.Down,
        durationSeconds: Double = testContext.swipeDurationSeconds,
        startMarginRatio: Double = testContext.scrollStartMarginRatio(direction),
        endMarginRatio: Double = testContext.scrollEndMarginRatio(direction),
        scrollMaxCount: Int = testContext.scrollMaxCount,
        throwsException: Boolean = true
    ): TestElement {

        var e = TestElement()
        val actionFunc = {
            val ms = Measure("$selector")

            e = select(
                selector = selector,
                waitSeconds = 0.0,
                throwsException = false,
                frame = frame
            )
            ms.end()
            e.isSafe
        }

        testDrive.doUntilScrollStop(
            repeat = 1,
            maxLoopCount = scrollMaxCount,
            direction = direction,
            durationSeconds = durationSeconds,
            startMarginRatio = startMarginRatio,
            endMarginRatio = endMarginRatio,
            actionFunc = actionFunc
        )

        lastElement = e
        if (e.hasError) {
            e.lastResult = LogType.ERROR
            if (throwsException) {
                throw e.lastError!!
            }
        }

        return lastElement
    }

    /**
     * selectWithScroll
     */
    fun selectWithScroll(
        expression: String,
        direction: ScrollDirection = ScrollDirection.Down,
        scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
        startMarginRatio: Double = testContext.scrollStartMarginRatio(direction),
        endMarginRatio: Double = testContext.scrollEndMarginRatio(direction),
        scrollMaxCount: Int = testContext.scrollMaxCount,
        throwsException: Boolean = true
    ): TestElement {
        val sel = expandExpression(expression = expression)
        val context = TestDriverCommandContext(lastElement)
        var e = TestElement(selector = sel)
        context.execSelectCommand(selector = sel, subject = sel.nickname) {
            e = selectWithScroll(
                selector = sel,
                direction = direction,
                durationSeconds = scrollDurationSeconds,
                startMarginRatio = startMarginRatio,
                endMarginRatio = endMarginRatio,
                scrollMaxCount = scrollMaxCount,
                throwsException = throwsException
            )
        }
        return e
    }

    /**
     * screenshot(for manual)
     */
    fun screenshot(
        force: Boolean = false,
        onChangedOnly: Boolean = testContext.onChangedOnly,
        filename: String? = null,
        withXmlSource: Boolean = TestLog.enableXmlSourceDump
    ): TestDriver {

        if (!force && !testContext.manualScreenshot) {
            TestLog.info("screenshot skipped (manualScreenshot=false)")
            return this
        }

        return screenshotCore(
            force = force,
            onChangedOnly = onChangedOnly,
            filename = filename,
            withXmlSource = withXmlSource
        )
    }

    internal fun autoScreenshot(
        force: Boolean = false,
        sync: Boolean = true,
        onChangedOnly: Boolean = testContext.onChangedOnly,
        filename: String? = null,
        withXmlSource: Boolean = TestLog.enableXmlSourceDump
    ) {
        if (testContext.autoScreenshot.not()) {
            return
        }

        screenshotCore(
            force = force,
            sync = sync,
            onChangedOnly = onChangedOnly,
            filename = filename,
            withXmlSource = withXmlSource
        )
    }

    internal fun screenshotCore(
        force: Boolean = false,
        sync: Boolean = testContext.useCache,
        onChangedOnly: Boolean = testContext.onChangedOnly,
        filename: String? = null,
        withXmlSource: Boolean = TestLog.enableXmlSourceDump
    ): TestDriver {

        if (TestMode.isNoLoadRun) {
            return this
        }
        if (isInitialized.not()) {
            return this
        }

        val screenshotTime = Date()

        if (force.not()) {
            if (CodeExecutionContext.shouldOutputLog.not()) {
                return this
            }
            if (shouldTakeScreenshot.not()) {
                return this
            }
            if (CodeExecutionContext.lastScreenshotTime != null) {
                val diff = screenshotTime.time - CodeExecutionContext.lastScreenshotTime!!.time
                val intervalMilliseconds = PropertiesManager.screenshotIntervalSeconds * 1000
                if (diff < intervalMilliseconds) {
                    TestLog.trace("screenshot() skipped. ($diff < $intervalMilliseconds)")
                    return this
                }
            }
        }

        if (sync) {
            syncCache()
        }
        if (testContext.useCache && force.not() && onChangedOnly) {
            val sourceXml = TestElementCache.sourceXml
            val xmlHasDifference = sourceXml != CodeExecutionContext.lastScreenshotXmlSource
            if (xmlHasDifference.not()) {
                return this
            }
        }

        var screenshotFileName = filename ?: (TestLog.lines.count() + 1).toString()
        if (screenshotFileName.lowercase().contains(".png").not()) {
            screenshotFileName += ".png"
        }

        if (mAppiumDriver == null) {
            throw TestDriverException("appiumDriver is null")
        }

        if (testContext.waitSecondsForAnimationComplete != 0.0) {
            Thread.sleep((testContext.waitSecondsForAnimationComplete * 1000).toLong())
        }

        try {
            val screenshot = mAppiumDriver!!.getScreenshotAs(OutputType.BYTES)
            val screenshotImage = screenshot.toBufferedImage()

            if (onChangedOnly && screenshotImage.isSame(CodeExecutionContext.lastScreenshotImage)) {
                return this
            }

            CodeExecutionContext.lastScreenshotImage = screenshotImage
            CodeExecutionContext.lastScreenshotTime = screenshotTime

            val screenshotFile = TestLog.directoryForLog.resolve(screenshotFileName).toFile()
            screenshotImage.resizeAndSaveImage(
                scale = PropertiesManager.screenshotScale,
                resizedFile = screenshotFile,
                log = false
            )
            CodeExecutionContext.lastScreenshot = screenshotFileName
            CodeExecutionContext.lastScreenshotXmlSource = TestElementCache.sourceXml
        } catch (t: Throwable) {
            TestLog.warn("screenshot $t + ${t.stackTrace}")
        }

        val screenshotLine = TestLog.write(
            message = "screenshot",
            logType = LogType.SCREENSHOT,
            scriptCommand = "screenshot"
        )
        screenshotLine.screenshot = screenshotFileName
        screenshotLine.lastScreenshot = screenshotFileName
        screenshotLine.subject = screenshotFileName

        if (withXmlSource && testContext.useCache) {
            val fileName = screenshotFileName.replace(".png", ".xml")
            outputXmlSource(filePath = TestLog.directoryForLog.resolve(fileName))
        }

        return this
    }

    /**
     * cropImage
     */
    fun cropImage(cropInfo: CropInfo, refresh: Boolean = true): CropInfo {

        syncCache()

        if (refresh || CodeExecutionContext.lastScreenshotImage == null) {
            refreshImage()
        }

        cropInfo.originalImage = CodeExecutionContext.lastScreenshotImage!!
        cropInfo.croppedImage = CodeExecutionContext.lastScreenshotImage!!.cropImage(rect = cropInfo.trimmedRect)

        return cropInfo
    }

    /**
     * outputXmlSource
     */
    fun outputXmlSource(filePath: Path) {

        try {
            val source = TestElementCache.sourceXml
            filePath.toFile().writeText(source)

            val dir = filePath.parent
            outputScanResultXml(dir)

        } catch (e: Exception) {
            TestLog.warn("output xml file failed(${e})")
        }
    }

    /**
     * outputScanResultXml
     */
    fun outputScanResultXml(dir: Path = TestLog.directoryForLog) {
        if (TestElementCache.scanResults.any()) {
            val joinedXml = StringBuilder()
            var lineNo = 0
            for (i in 0 until TestElementCache.scanResults.count()) {
                val sr = TestElementCache.scanResults[i]
                val scanResultSource = sr.sourceXml
                lineNo = sr.lineNo
                val name = "${lineNo}.${i + 1}.xml"
                joinedXml.appendLine(name)
                joinedXml.appendLine(scanResultSource)
                joinedXml.appendLine()
                val filePath = dir.resolve(name)
                val scanResultFile = File(filePath.toUri())
                scanResultFile.writeText(scanResultSource)
            }
            val joinedPath = dir.resolve("${lineNo}.all.xml")
            val joinedFile = File(joinedPath.toUri())
            joinedFile.writeText(joinedXml.toString())
        }
    }

    /**
     * switchScreen
     */
    fun switchScreen(screenName: String): TestDriver {

        val context = TestDriverCommandContext(lastElement)
        context.execSilentCommand(arg1 = screenName) {
            NicknameUtility.validateScreenName(screenName)

            TestLog.trace("screenName='$screenName'")
            currentScreen = screenName
        }

        return this
    }

    /**
     * refreshCurrentScreen
     */
    fun refreshCurrentScreen(
        screenInfoList: List<ScreenInfo> = ScreenRepository.screenInfoSearchList,
        log: Boolean = true
    ): String {

        val ms = Measure("refreshCurrentScreen")

        val originalScreen = currentScreen

        val newScreen = refreshCurrentScreenInCandidates(screenInfoList = screenInfoList)
        val changed = newScreen != "?" && newScreen != originalScreen
        if (changed) {
            currentScreen = newScreen
            screenshot()
            TestLog.info("currentScreen=$currentScreen", log = log)
        }

        ms.end()

        return currentScreen
    }

    private fun refreshCurrentScreenInCandidates(
        screenInfoList: List<ScreenInfo>
    ): String {
        var newScreen = "?"
        if (screenInfoList.isEmpty()) {
            return newScreen
        }
        val sortedList = screenInfoList.sortedByDescending { it.searchWeight }
        for (i in 0 until sortedList.count()) {
            val screenInfo = sortedList[i]
            val screenName = screenInfo.key
            TestLog.trace("// Trying $screenName")

            if (screenName.isBlank()) {
                continue
            }
            if (screenInfo.identityElements.isEmpty()) {
                continue
            }

            val match = isScreen(screenName = screenName)
            if (match) {
                newScreen = screenName
                break
            }
        }
        return newScreen
    }

    /**
     * refreshCurrentScreenWithNickname
     */
    fun refreshCurrentScreenWithNickname(expression: String) {

        if (screenInfo.selectors.any() { it.key == expression }) {
            return
        }

        val ms = Measure(expression)

        if (expression.isValidNickname()) {
            val nickname = expression
            val screenCandidates = ScreenRepository.nicknameIndex[nickname]
            if (screenCandidates != null) {
                refreshCurrentScreen(screenCandidates)
            }
        }
        ms.end()
    }

    private val screenNameHistory = mutableListOf<String>()

    private fun setScreenHistory(screenName: String) {

        val ix = screenNameHistory.indexOf(screenName)
        if (ix >= 0) {
            screenNameHistory.removeAt(ix)
        }
        screenNameHistory.add(0, screenName)
    }

    private fun getScreenInfoHistory(): List<ScreenInfo> {
        val list = mutableListOf<ScreenInfo>()
        for (screenName in screenNameHistory) {
            if (ScreenRepository.has(screenName)) {
                list.add(ScreenRepository[screenName])
            }
        }
        return list
    }

    /**
     * isScreen
     */
    fun isScreen(
        screenName: String,
        log: Boolean = PropertiesManager.enableIsScreenLog
    ): Boolean {

        if (TestMode.isNoLoadRun) {
            return true
        }

        val ms = Measure("■■■ Trying isScreen($screenName)")
        try {
            NicknameUtility.validateScreenName(screenName)
            val screenInfo = ScreenRepository.get(screenName)

            var r = TestDriveObject.canSelectAll(selectors = screenInfo.identitySelectors, frame = null)
            if (r && screenInfo.satelliteSelectors.any()) {
                fun hasAnySatellite(): Boolean {
                    for (expression in screenInfo.satelliteExpressions) {
                        if (TestDriveObject.canSelect(
                                expression = expression,
                                screenName = screenName
                            )
                        ) {
                            return true
                        }
                    }
                    return false
                }
                r = r && hasAnySatellite()
            }
            if (r) {
                currentScreen = screenName
                setScreenHistory(screenName)
                TestLog.trace("Screen found. ■■■ $screenName ■■■")
            }

            if (log) {
                TestLog.info("isScreen($screenName) is $r. (currentScreen=$currentScreen)")
            }

            return r
        } finally {
            ms.end()
        }
    }

    internal var isSyncing = false

    /**
     * syncCache
     */
    fun syncCache(
        force: Boolean = false,
        syncWaitSeconds: Double = testContext.syncWaitSeconds,
        maxLoopCount: Int = testContext.syncMaxLoopCount,
        syncIntervalSeconds: Double = testContext.syncIntervalSeconds,
        syncOnTimeout: Boolean = true
    ): TestDriver {

        if (testContext.useCache.not()) {
            return this
        }

        if (isSyncing) {
            TestLog.trace("syncCache called recursively.")
            if (force.not()) {
                return this
            }
        }

        if (TestElementCache.synced && force.not()) {
            return this
        }

        try {
            isSyncing = true
            invalidateCache()

            val context = TestDriverCommandContext(lastElement)
            context.execSilentCommand() {
                var lastXml = TestElementCache.sourceXml
                val sw = StopWatch().start()
                val enableSyncLog = PropertiesManager.enableSyncLog || TestLog.enableTrace

                for (i in 1..maxLoopCount) {
                    TestLog.info("Syncing ($i)", log = enableSyncLog)
                    refreshCache(currentScreenRefresh = false)
                    if (lastXml.isBlank()) {
                        TestLog.info("imageProfile: ${testDrive.imageProfile}")
                    }

                    if (isAndroid) {
                        if (PropertiesManager.enableAutoSyncAndroid.not()) {
                            lastXml = TestElementCache.sourceXml
                        }
                    } else {
                        if (PropertiesManager.enableAutoSyncIos.not()) {
                            lastXml = TestElementCache.sourceXml
                        }
                    }

                    TestElementCache.synced = (TestElementCache.sourceXml == lastXml)

                    if (TestElementCache.synced) {
                        /**
                         * Synced
                         */
                        TestLog.info(
                            "Synced. (elapsed=${sw.elapsedSeconds})",
                            log = enableSyncLog
                        )
                        refreshCurrentScreen()
                        return@execSilentCommand
                    }

                    lastXml = TestElementCache.sourceXml

                    if (sw.elapsedSeconds > syncWaitSeconds) {
                        if (syncOnTimeout) {
                            /**
                             * Synced(Timeout)
                             */
                            TestElementCache.synced = true
                            refreshCurrentScreen()
                            val screenName = if (currentScreen.isNotBlank()) currentScreen else "?"
                            TestLog.info(
                                "Synchronization timed out (elapsed=${sw.elapsedSeconds} > syncWaitSeconds=$syncWaitSeconds, currentScreen=$screenName)",
                                log = enableSyncLog
                            )
                            return@execSilentCommand
                        } else {
                            throw TestDriverException("Synchronization timed out (elapsed=${sw.elapsedSeconds} > syncWaitSeconds=$syncWaitSeconds)")
                        }
                    }

                    /**
                     * Before next loop
                     */
                    TestLog.info("elapsed=${sw.elapsedSeconds}, syncWaitSeconds=$syncWaitSeconds", log = enableSyncLog)
                    if (isAndroid) {
                        Thread.sleep((syncIntervalSeconds * 1000).toLong())
                        // Wait is required for Android because getSource of AndroidDriver is too fast.
                    }
                }
                TestElementCache.synced = true
                refreshCurrentScreen()
                TestLog.info(
                    "Loop count has been exceeded. (maxLoopCount=$maxLoopCount, elapsed=${sw.elapsedSeconds}, currentScreen=$currentScreen)",
                    log = enableSyncLog
                )
            }
        } finally {
            isSyncing = false
        }

        return this
    }

    /**
     * refreshImage
     */
    fun refreshImage() {

        TestLog.info("refreshImage")
        CodeExecutionContext.lastScreenshotImage = mAppiumDriver!!.getScreenshotAs(OutputType.BYTES).toBufferedImage()
    }

    /**
     * getFocusedWebElement
     */
    fun getFocusedWebElement(): TestElement {

        return try {
            testDrive.implicitWaitMilliseconds((testContext.shortWaitSeconds * 1000).toInt()) {
                lastElement = (mAppiumDriver!!.switchTo().activeElement() as WebElement).toTestElement()
            }
            lastElement
        } catch (t: Throwable) {
            throw TestDriverException(message(id = "activeElementNotFound", submessage = "$t"), cause = t)
        }
    }

    private fun getFocusedElementCore(
        throwsException: Boolean = false
    ): TestElement {

        if (isAndroid) {
            val focused = select(expression = "xpath=//*[@focused='true']", throwsException = false)
            return focused
        } else {
            val e = try {
                (mAppiumDriver!!.switchTo().activeElement() as WebElement).toTestElement()
            } catch (t: Throwable) {
                if (throwsException) throw t
                return TestElement.emptyElement
            }
            return e
        }
    }

    /**
     * getFocusedElement
     */
    fun getFocusedElement(
        throwsException: Boolean = false
    ): TestElement {

        if (TestMode.isNoLoadRun) {
            return lastElement
        }

        refreshCache()

        val element = getFocusedElementCore(throwsException = throwsException)

        if (element.isEmpty && throwsException) {
            throw TestDriverException(message(id = "focusedElementNotFound"))
        }

        return element
    }

    /**
     * invalidateCache
     */
    fun invalidateCache(): TestDriver {

        TestElementCache.synced = false

        return this
    }

    /**
     * isAppCore
     *
     * @param appNameOrAppId
     * Nickname [App1]
     * or appName App1
     * or packageOrBundleId com.example.app1
     */
    fun isAppCore(
        appNameOrAppId: String
    ): Boolean {

        try {
            val packageOrBundleId = AppNameUtility.getPackageOrBundleId(appNameOrAppIdOrActivityName = appNameOrAppId)
            if (isAndroid) {
                return rootElement.packageName == packageOrBundleId
            }

            var appName = AppNameUtility.getAppNameFromPackageName(packageName = packageOrBundleId)
            if (appName.isBlank()) {
                appName = appNameOrAppId.split(".").last()
            }

            if (testContext.useCache) {
                syncCache()
            }
            val appElement = select(".XCUIElementTypeApplication")
            return appElement.name == appName
        } catch (t: Throwable) {
            TestLog.trace("In isAppCore: $t")
            return false
        }
    }

    /**
     * tapAppIconCore
     */
    fun tapAppIconCore(
        appIconName: String = shirates.core.driver.testContext.appIconName,
        tapAppIconMethod: TapAppIconMethod = shirates.core.driver.testContext.tapAppIconMethod
    ): TestElement {

        when (tapAppIconMethod) {

            TapAppIconMethod.googlePixel -> {
                TapHelper.tapAppIconAsGooglePixel(appIconName = appIconName)
                return lastElement
            }

            TapAppIconMethod.swipeLeftInHome -> {
                TapHelper.swipeLeftAndTapAppIcon(appIconName = appIconName)
            }

            else -> {
                if (isAndroid) {
                    if (testDrive.deviceManufacturer == "Google" || testDrive.deviceModel.contains("Android SDK")) {
                        TapHelper.tapAppIconAsGooglePixel(appIconName = appIconName)
                    } else {
                        TapHelper.swipeLeftAndTapAppIcon(appIconName = appIconName)
                    }
                } else if (isiOS) {
                    TapHelper.tapAppIconAsIos(appIconName = appIconName)
                }
            }
        }

        return lastElement
    }

    fun launchAppCore(
        packageOrBundleIdOrActivity: String,
        onLaunchHandler: (() -> Unit)? = testContext.onLaunchHandler
    ): TestElement {

        if (packageOrBundleIdOrActivity.isBlank()) {
            throw IllegalArgumentException("launchAppCore(blank)")
        }

        if (testProfile.udid.isBlank()) {
            testProfile.completeProfile()

            if (testProfile.udid.isBlank()) {
                resetAppiumSession()
            }
        }

        if (isAndroid) {
            TestDriveObjectAndroid.launchAndroidApp(
                udid = testProfile.udid,
                packageNameOrActivityName = packageOrBundleIdOrActivity,
                onLaunchHandler = onLaunchHandler
            )
            Thread.sleep(1500)
            refreshCache()
            SyncUtility.doUntilTrue {
                isAppCore(appNameOrAppId = packageOrBundleIdOrActivity)
            }
        } else if (isiOS) {
            if (isRealDevice) {
                throw NotImplementedError("TestDriver.launchApp is not supported on real device in iOS. (packageOrBundleId=$packageOrBundleIdOrActivity)")
            }

            val bundleId = packageOrBundleIdOrActivity

            try {
                testDrive.terminateApp(appNameOrAppId = bundleId)
                TestLog.info("Launching app. (bundleId=$bundleId)")
                TestDriveObjectIos.launchIosApp(
                    udid = testProfile.udid,
                    bundleId = bundleId,
                    onLaunchHandler = onLaunchHandler,
                    log = true
                )
                Thread.sleep(1500)
                refreshCache()
                onLaunchHandler?.invoke()
            } catch (t: Throwable) {
                TestLog.info("Launching app failed. Retrying. (bundleId=$bundleId) $t")

                SyncUtility.doUntilTrue(
                    waitSeconds = testContext.waitSecondsForLaunchAppComplete,
                    maxLoopCount = 2,
                ) {
                    /**
                     * Reset Appium session
                     */
                    if (isAndroid) {
                        AndroidDeviceUtility.reboot(testProfile)
                    } else if (isiOS) {
                        IosDeviceUtility.terminateSpringBoardByUdid(udid = testProfile.udid)
                    }
                    createAppiumDriver()

                    /**
                     * Retry launchApp
                     */
                    val retry =
                        try {
                            TestDriveObjectIos.launchIosApp(
                                udid = testProfile.udid,
                                bundleId = bundleId,
                                log = true
                            )
                            false
                        } catch (t: Throwable) {
                            if (PropertiesManager.enableWarnOnRetryError) {
                                TestLog.warn("Error: $t")
                            }
                            if (PropertiesManager.enableRetryLog) {
                                TestLog.info("Retrying launching app. (bundleId=$bundleId)")
                            }
                            true
                        }
                    retry
                }
            }
        }

        return lastElement
    }

    /**
     * resetAppiumSession
     */
    fun resetAppiumSession() {

        TestLog.info("Resetting Appium session.")

        /**
         * Shutdown AppiumServer
         */
        AppiumServerManager.close()

        /**
         * Restart device
         */
        if (isAndroid) {
            val device = AndroidDeviceUtility.currentAndroidDeviceInfo
            if (device != null) {
                AndroidDeviceUtility.reboot(testProfile = testProfile)
            }
        } else if (isiOS && isSimulator) {
//            IosDeviceUtility.restartSimulator(udid = testProfile.udid, log = true)
//            IosDeviceUtility.stopSimulator(udid = testProfile.udid)
            IosDeviceUtility.terminateSpringBoardByUdid(udid = testProfile.udid)
        }

        Thread.sleep(500)

        /**
         * Restart AppiumServer
         */
        AppiumServerManager.setupAppiumServerProcess(
            sessionName = TestLog.currentTestClassName,
            profile = testProfile
        )

        /**
         * Create AppiumDriver
         */
        createAppiumDriver()
    }

}
