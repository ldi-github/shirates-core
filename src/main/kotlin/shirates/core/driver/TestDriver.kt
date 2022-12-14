package shirates.core.driver

import io.appium.java_client.AppiumDriver
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.nativekey.AndroidKey
import io.appium.java_client.android.nativekey.KeyEvent
import io.appium.java_client.ios.IOSDriver
import org.openqa.selenium.Capabilities
import org.openqa.selenium.OutputType
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.DesiredCapabilities
import shirates.core.configuration.*
import shirates.core.configuration.repository.ImageFileRepository
import shirates.core.configuration.repository.ParameterRepository
import shirates.core.configuration.repository.ScreenRepository
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.TestMode.isiOS
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestConfigException
import shirates.core.exception.TestDriverException
import shirates.core.exception.TestEnvironmentException
import shirates.core.exception.TestNGException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.proxy.AppiumProxy
import shirates.core.server.AppiumServerManager
import shirates.core.storage.app
import shirates.core.testcode.CAEPattern
import shirates.core.utility.android.AdbUtility
import shirates.core.utility.android.AndroidMobileShellUtility
import shirates.core.utility.appium.setCapabilityStrict
import shirates.core.utility.getUdid
import shirates.core.utility.image.*
import shirates.core.utility.sync.RetryContext
import shirates.core.utility.sync.RetryUtility
import shirates.core.utility.sync.SyncUtility
import shirates.core.utility.time.StopWatch
import shirates.core.utility.toBufferedImage
import java.io.File
import java.io.FileNotFoundException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.time.Duration

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
            TestLog.trace()

            if (TestMode.isNoLoadRun) {
                return lastElement
            }

            if (TestElementCache.synced) {
                TestLog.trace("TestElementCache.synced=${TestElementCache.synced}. syncCache skipped.")
                return lastElement
            }

            syncCache()

            if (lastElement.isEmpty.not()) {
                lastElement = lastElement.refreshThisElement()
            }
            if (lastElement.isEmpty) {
                lastElement = rootElement
            }

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
        TestLog.info("waitSecondsForAnimationComplete: ${testContext.waitSecondsForAnimationComplete}")
        if (isAndroid) {
            TestLog.info("waitSecondsForConnectionEnabled: ${testContext.waitSecondsForConnectionEnabled}")
        }
        TestLog.info("swipeDurationSeconds: ${testContext.swipeDurationSeconds}")
        TestLog.info("flickDurationSeconds: ${testContext.flickDurationSeconds}")
        TestLog.info("swipeMarginRatio: ${testContext.swipeMarginRatio}")
        TestLog.info("scrollVerticalMarginRatio: ${testContext.scrollVerticalMarginRatio}")
        TestLog.info("scrollHorizontalMarginRatio: ${testContext.scrollHorizontalMarginRatio}")
        TestLog.info("tapHoldSeconds: ${testContext.tapHoldSeconds}")
        TestLog.info("tapAppIconMethod: ${testContext.tapAppIconMethod}")
        TestLog.info("tapAppIconMacro: ${testContext.tapAppIconMacro}")
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
        imageMatchResult: ImageMatchResult,
        assertMessage: String,
        log: Boolean = CodeExecutionContext.shouldOutputLog,
        dontExist: Boolean = false
    ) {
        if (dontExist && imageMatchResult.result.not() || dontExist.not() && imageMatchResult.result) {
            e.lastResult = TestLog.getOKType()
            if (log) {
                TestLog.ok(message = assertMessage)
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
        invalidateCache()
        try {
            mAppiumDriver?.quit()
        } catch (t: Throwable) {
            if (t.message!!.contains("Connection refused").not()) {
                throw t
            }
        } finally {
            mAppiumDriver = null
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
        }

    /**
     * expandExpression
     */
    fun expandExpression(
        expression: String,
        screenName: String = currentScreen
    ): Selector {

        if (screenName.isBlank()) {
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
            else ScreenInfo()
        }

    private var lastFireIrregularHandlerXmlSource = ""

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

        lastFireIrregularHandlerXmlSource = ""

        for (i in 1..5) {
            val handled = fireIrregularHandlerCore()
            if (handled == false) {
                break
            }
        }
    }

    private fun fireIrregularHandlerCore(): Boolean {
        try {
            isFiringIrregularHandler = true

            if (TestElementCache.synced && TestElementCache.sourceXml == lastFireIrregularHandlerXmlSource) {
                TestLog.trace("firing irregularHandler skipped")
                return false
            }
            syncCache()
            lastFireIrregularHandlerXmlSource = TestElementCache.sourceXml

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

        val handled = TestElementCache.sourceXml == lastFireIrregularHandlerXmlSource
        return handled
    }

    internal fun createAppiumDriver(profile: TestProfile = testContext.profile) {

        if (TestMode.isNoLoadRun) {
            return
        }

        val capabilities = DesiredCapabilities()
        setCapabilities(profile, capabilities)

        val initFunc: (RetryContext<Unit>) -> Unit =
            getInitFunc(profile = profile, capabilities = capabilities)

        val retryPredicate: (RetryContext<Unit>) -> Boolean =
            getRetryPredicate(profile = profile, capabilities = capabilities)

        val retryMaxCount = AppiumServerManager.appiumSessionStartupTimeoutSeconds / testContext.retryIntervalSeconds
        val retryContext = RetryUtility.exec(
            retryMaxCount = retryMaxCount.toLong(),
            retryTimeoutSeconds = testContext.retryTimeoutSeconds,
            retryFunc = initFunc,
            retryPredicate = retryPredicate
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
            profile.implicitlyWaitSeconds?.toDoubleOrNull() ?: shirates.core.Const.IMPLICITLY_WAIT_SECONDS
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

        TestLog.info("AppiumDriver initialized.")
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
            val message = context.exception?.message ?: ""
            if (message.contains("The requested resource could not be found")) {
                val msg = message(id = "TheRequestedResourceCouldNotBeFound", subject = profile.appiumServerUrl)
                context.wrappedError = TestEnvironmentException(
                    message = msg,
                    cause = context.exception
                )
                true
            } else if (message.contains("Unable to find an active device or emulator")) {
                context.wrappedError = TestEnvironmentException(
                    message = message(id = "unableToFindAnActiveDevice", arg1 = profile.profileName),
                    cause = context.exception
                )
                val p = profile
                TestLog.info("deviceName:${p.deviceName}")
                TestLog.info("platformName:${p.platformName}")
                TestLog.info("platformVersion:${p.platformVersion}")
                true
            } else if (message.contains("'app' option is required for reinstall")) {
                val pkg = capabilities.getCapability("appPackage")
                context.wrappedError = TestEnvironmentException(
                    message = message(id = "appIsNotInstalled", subject = "$pkg"),
                    cause = context.exception
                )
                false
            } else if (message.contains("Could not find 'adb.exe' in PATH. Please set the ANDROID_HOME or ANDROID_SDK_ROOT environment variables")) {
                context.wrappedError = TestEnvironmentException(
                    message = message(id = "adbNotFound"),
                    cause = context.exception
                )
                false
            } else if (message.contains("Could not find a connected Android device")) {
                context.wrappedError = TestEnvironmentException(
                    message = message(id = "couldNotFindConnectedDevice", arg1 = profile.profileName),
                    cause = context.exception
                )
                true
            } else if (message.contains("device unauthorized")) {
                context.wrappedError = TestEnvironmentException(
                    message = message(id = "deviceUnauthorized"),
                    cause = context.exception
                )
                true
            } else if (message.contains("was not in the list of connected devices")) {
                val udid = profile.capabilities.getOrDefault("udid", "(not set)")?.toString()
                context.wrappedError = TestEnvironmentException(
                    message = message(id = "deviceNotConnected", subject = profile.deviceName, arg1 = udid),
                    cause = context.exception
                )
                true
            } else if (message.contains("App with bundle identifier") && message.contains("unknown")) {
                val bundleId = profile.capabilities.getOrDefault("bundleId", "(not set)")?.toString()
                context.wrappedError = TestConfigException(
                    message = message(id = "unknownBundleId", subject = bundleId),
                    cause = context.exception
                )
                false
            } else if (message.contains("Error getting device platform version.")) {
                val msg = message(id = "adbFailedGettingDevicePlatformVersion")
                context.wrappedError = TestEnvironmentException(message = msg, cause = context.exception)
                false
            } else if (message.startsWith("Could not start a new session. Possible causes are invalid address")) {
                val msg = message(id = "couldNotStartANewSession")
                context.wrappedError = TestEnvironmentException(message = msg, cause = context.exception)
                false
            } else if (
                message.contains("socket hang up") ||
                message.contains("cannot be proxied to UiAutomator2 server because the instrumentation process is not running (probably crashed)")
            ) {
                TestLog.warn(message)
                TestLog.outputLogDump()

                if (isAndroid) {
                    val udid = initialCapabilities["deviceUDID"] ?: testContext.profile.udid
                    if (udid.isBlank()) {
                        throw TestDriverException("deviceUDID not found.")
                    }
                    TestLog.info("udid=$udid")

                    // Restart device
                    AdbUtility.reboot(udid = udid)

                    // Restart Appium Server
                    AppiumServerManager.restartAppiumProcess()
                }
                true
            } else {
                true
            }
        }
        return retryPredicate
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
        TestLog.info("[Health check] start")

        syncCache(force = true)     // throws on fails
        androidDriver.getScreenshotAs(OutputType.BYTES)   // throws on fails
        androidDriver.pressKey(KeyEvent(AndroidKey.CLEAR))   // throws on fails

        TestLog.info("[Health check] end")
    }

    private var isRefreshing = false

    /**
     * refreshCache
     */
    fun refreshCache(currentScreenRefresh: Boolean = true): TestDriver {

        if (isInitialized.not()) {
            return this
        }

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
                }

                if (lastElement.selector != null) {
                    val e = rootElement.findInDescendantsAndSelf(selector = lastElement.selector!!)
                    e.lastError = lastElement.lastError
                    if (e.isEmpty.not()) {
                        lastElement = e
                    }
                } else {
                    lastElement = rootElement
                }

            }
        } finally {
            isRefreshing = false
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
        scrollStartMarginRatio: Double = testContext.scrollVerticalMarginRatio,
        scrollMaxCount: Int = testContext.scrollMaxCount,
        waitSeconds: Double = testContext.syncWaitSeconds,
        throwsException: Boolean = true,
        syncCache: Boolean = true,
        log: Boolean = false
    ): TestElement {

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
                scrollMaxCount = scrollMaxCount,
                waitSeconds = waitSeconds,
                throwsException = throwsException,
                syncCache = syncCache
            )
        }

        return e
    }

    internal fun select(
        selector: Selector,
        scroll: Boolean = false,
        direction: ScrollDirection = ScrollDirection.Down,
        scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
        scrollStartMarginRatio: Double = testContext.scrollVerticalMarginRatio,
        scrollMaxCount: Int = testContext.scrollMaxCount,
        waitSeconds: Double = testContext.syncWaitSeconds,
        throwsException: Boolean = true,
        syncCache: Boolean = true,
    ): TestElement {

        if (selector.isRelative) {
            return TestElementCache.select(selector = selector, throwsException = false)
        }
        if (selector.isImageSelector) {
            throw TestDriverException(message(id = "imageSelectorNotSupported"))
        }

        lastElement = TestElement.emptyElement

        if (syncCache) {
            syncCache()
        }

        // Search in current screen
        var selectedElement = TestElementCache.select(
            selector = selector,
            throwsException = false
        )
        if (selector.isNegation.not()) {
            if (selectedElement.isFound) {
                lastElement = selectedElement
                return lastElement
            }
        }

        // Search in scroll
        if (scroll) {
            return selectWithScroll(
                selector = selector,
                direction = direction,
                durationSeconds = scrollDurationSeconds,
                startMarginRatio = scrollStartMarginRatio,
                scrollMaxCount = scrollMaxCount,
                throwsException = throwsException
            )
        }

        // Wait for seconds
        if (waitSeconds > 0 && isInitialized) {
            val actionFunc = {
                val e = TestElementCache.select(selector = selector, throwsException = false)
                selectedElement = e
                if (selector.isNegation) {
                    e.isEmpty
                } else {
                    e.isFound
                }
            }

            // Search until it is(not) found
            val r = SyncUtility.doUntilTrue(
                waitSeconds = waitSeconds,
                actionFunc = actionFunc
            )
            if (r.hasError) {
                lastElement.lastError = r.error
            }
        }

        lastElement = selectedElement

        if (selectedElement.hasError) {
            selectedElement.lastResult = LogType.ERROR
            if (throwsException) {
                throw selectedElement.lastError!!
            }
        }

        return lastElement
    }

    /**
     * findImage
     */
    fun findImage(
        expression: String,
        scroll: Boolean = false,
        direction: ScrollDirection = ScrollDirection.Down,
        scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
        scrollStartMarginRatio: Double = testContext.scrollVerticalMarginRatio,
        scrollMaxCount: Int = testContext.scrollMaxCount,
        throwsException: Boolean = true,
        syncCache: Boolean = true,
    ): ImageMatchResult {

        val sel = expandExpression(expression = expression)

        return findImage(
            selector = sel,
            scroll = scroll,
            direction = direction,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollMaxCount = scrollMaxCount,
            throwsException = throwsException,
            syncCache = syncCache
        )
    }

    internal fun findImage(
        selector: Selector,
        scroll: Boolean = false,
        direction: ScrollDirection = ScrollDirection.Down,
        scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
        scrollStartMarginRatio: Double = testContext.scrollVerticalMarginRatio,
        scrollMaxCount: Int = testContext.scrollMaxCount,
        throwsException: Boolean = true,
        syncCache: Boolean = true,
    ): ImageMatchResult {

        lastElement = TestElement.emptyElement

        if (syncCache) {
            syncCache()
        }

        if (selector.image.isNullOrBlank()) {
            throw IllegalArgumentException(message(id = "imageFileNotFound", subject = selector.expression))
        }
        val filePath = ImageFileRepository.getFilePath(selector.image!!)
        if (Files.exists(filePath).not()) {
            throw FileNotFoundException(message(id = "imageFileNotFound", subject = selector.image))
        }

        // Search in current screen
        var r = rootElement.isContainingImage(selector.image!!)
        if (r.result) {
            return r
        }

        // Search in scroll
        if (scroll) {
            val actionFunc = {
                r = rootElement.isContainingImage(selector.image!!)
                r.result
            }

            testDrive.doUntilScrollStop(
                maxLoopCount = scrollMaxCount,
                direction = direction,
                durationSeconds = scrollDurationSeconds,
                startMarginRatio = scrollStartMarginRatio,
                actionFunc = actionFunc
            )
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
     * restartApp
     */
    fun restartApp(nickname: String? = null): TestDriver {

        if (TestMode.isNoLoadRun) {
            return this
        }

        if (nickname == null) {
            it.terminateApp()
            it.tapAppIcon()
            return this
        }

        val packageOrBundleId = app(datasetName = nickname, attributeName = "packageOrBundleId")
        it.terminateApp(packageOrBundleId)

        val appIconName = NicknameUtility.getNicknameText(nickname)
        it.tapAppIcon(appIconName = appIconName)

        return this
    }

    /**
     * selectWithScroll
     */
    fun selectWithScroll(
        selector: Selector,
        direction: ScrollDirection = ScrollDirection.Down,
        durationSeconds: Double = testContext.swipeDurationSeconds,
        startMarginRatio: Double =
            if (direction.isDown || direction.isUp) testContext.scrollVerticalMarginRatio
            else testContext.scrollHorizontalMarginRatio,
        scrollMaxCount: Int = testContext.scrollMaxCount,
        throwsException: Boolean = true
    ): TestElement {

        var e = TestElementCache.select(selector = selector, throwsException = false)
        if (e.isSafe) {
            it.syncCache()
            e = TestElementCache.select(selector = selector, throwsException = false)
            if (e.isSafe) {
                lastElement = e
                return lastElement
            }
        }

        val actionFunc = {
            e = TestElementCache.select(selector = selector, throwsException = false)
            e.isSafe
        }

        testDrive.doUntilScrollStop(
            maxLoopCount = scrollMaxCount,
            direction = direction,
            durationSeconds = durationSeconds,
            startMarginRatio = startMarginRatio,
            actionFunc = actionFunc
        )

        if (e.isEmpty) {
            // Try select after scroll stops
            it.syncCache()
            e = TestElementCache.select(selector = selector, throwsException = false)
        }

        lastElement = e

        if (lastElement.hasError) {
            lastElement.lastResult = LogType.ERROR
            if (throwsException) {
                throw lastElement.lastError!!
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
        scrollStartMarginRatio: Double = testContext.scrollVerticalMarginRatio,
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
                startMarginRatio = scrollStartMarginRatio,
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
        sync: Boolean = true,
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
        if (force.not()) {
            if (CodeExecutionContext.shouldOutputLog.not()) {
                return this
            }
            if (shouldTakeScreenshot.not()) {
                return this
            }
        }

        if (sync) {
            syncCache()
        }
        if (force.not() && onChangedOnly) {
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

        try {
            val screenshot = mAppiumDriver!!.getScreenshotAs(OutputType.BYTES)
            val screenshotImage = screenshot.toBufferedImage()

            if (onChangedOnly && screenshotImage.isSame(CodeExecutionContext.lastScreenshotImage)) {
                return this
            }

            CodeExecutionContext.lastScreenshotImage = screenshotImage
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

        if (withXmlSource) {
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
    fun refreshCurrentScreen(): String {

        val screenInfoList = ScreenRepository.screenInfoList

        for (i in 0 until screenInfoList.count()) {
            val screenInfo = screenInfoList[i]
            val screenName = screenInfo.key

            if (screenName.isBlank()) {
                continue
            }
            if (screenInfo.identityElements.isEmpty()) {
                continue
            }

            val match = isScreen(screenName = screenName)
            if (match) {
                currentScreen = screenName
                return currentScreen
            }
        }

        currentScreen = ""
        return currentScreen
    }

    /**
     * isScreen
     */
    fun isScreen(
        screenName: String
    ): Boolean {

        if (TestMode.isNoLoadRun) {
            return true
        }

        NicknameUtility.validateScreenName(screenName)
        val repo = ScreenRepository.get(screenName)

        var r = TestDriveObject.canSelectAll(selectors = repo.identitySelectors)
        if (r && repo.satelliteSelectors.any()) {
            fun hasAnySatellite(): Boolean {
                for (expression in repo.satelliteExpressions) {
                    if (TestDriveObject.canSelect(expression = expression, screenName = screenName)) {
                        return true
                    }
                }
                return false
            }
            r = r && hasAnySatellite()
        }

        return r
    }

    internal var isSyncing = false

    /**
     * syncCache
     */
    fun syncCache(
        force: Boolean = false,
        syncWaitSeconds: Double = testContext.syncWaitSeconds,
        maxLoopCount: Int = testContext.syncMaxLoopCount,
        syncIntervalSeconds: Double = testContext.syncIntervalSeconds
    ): TestDriver {

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
                    if (TestElementCache.sourceXml == lastXml) {
                        /**
                         * Synced
                         */
                        TestElementCache.synced = true
                        refreshCurrentScreen()
                        val screenName = if (currentScreen.isNotBlank()) currentScreen else "?"
                        TestLog.info(
                            "Synced. (elapsed=${sw.elapsedSeconds}, currentScreen=$screenName)",
                            log = enableSyncLog
                        )
                        return@execSilentCommand
                    }

                    lastXml = TestElementCache.sourceXml

                    if (sw.elapsedSeconds > syncWaitSeconds) {
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
    fun getFocusedWebElement(
        waitSeconds: Double = testContext.waitSecondsOnIsScreen
    ): WebElement {

        syncCache()

        var m: WebElement? = null
        val r = SyncUtility.doUntilTrue(waitSeconds = waitSeconds) {
            try {
                m = mAppiumDriver!!.switchTo().activeElement() as WebElement
            } catch (t: Throwable) {
                throw TestDriverException(message(id = "activeElementNotFound", submessage = "$t"), cause = t)
            }
            m != null
        }
        r.throwIfError()
        return m!!
    }

    private fun getFocusedElementCore(): TestElement {

        if (isAndroid) {
            val focused = TestElementCache.allElements.firstOrNull() { it.focused == "true" }
            return focused ?: TestElement()
        } else {
            val a = getFocusedWebElement()
            val type = a.getAttribute("type")
            val sel = Selector("className=$type")
            val name = a.getAttribute("name")
            if (name.isNullOrBlank()) {
                val location = a.location
                val x = location.x
                val y = location.y
                val size = a.size
                val width = size.width
                val height = size.height
                val xpath = "//*[@x='$x' and @y='$y' and @width='$width' and @height='$height']"
                return TestElementCache.select("xpath=$xpath")
            } else {
                sel.id = name
                return TestElementCache.select(selector = sel)
            }
        }
    }

    /**
     * getFocusedElement
     */
    fun getFocusedElement(
        waitSeconds: Double = testContext.waitSecondsOnIsScreen,
        throwsException: Boolean = false
    ): TestElement {

        if (TestMode.isNoLoadRun) {
            return lastElement
        }

        var element = TestElement()
        val r = SyncUtility.doUntilTrue(
            waitSeconds = waitSeconds
        ) {
            element = getFocusedElementCore()
            element.isFound
        }

        if (element.isEmpty && throwsException) {
            throw TestDriverException(message(id = "focusedElementNotFound"), cause = r.error)
        }

        element.refreshSelector()
        return element
    }

    /**
     * invalidateCache
     */
    fun invalidateCache(): TestDriver {

        TestElementCache.synced = false

        return this
    }

}
