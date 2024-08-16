package shirates.core.driver.commandextension

import shirates.core.configuration.ImageInfo
import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.configuration.isValidNickname
import shirates.core.configuration.repository.ScreenRepository
import shirates.core.driver.*
import shirates.core.exception.TestNGException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.image.ImageMatchResult
import shirates.core.utility.load.CpuLoadService
import shirates.core.utility.misc.AppNameUtility
import shirates.core.utility.sync.SyncContext
import shirates.core.utility.sync.SyncUtility
import shirates.core.utility.time.StopWatch

/**
 * keyboardIsShown
 */
fun TestDrive.keyboardIsShown(): TestElement {

    val testElement = TestDriver.it

    val command = "keyboardIsShown"
    val assertMessage = message(id = command)

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = "keyboard") {
        if (isKeyboardShown) {
            TestLog.ok(message = assertMessage)
        } else {
            lastElement.lastError = TestNGException(assertMessage)
            throw lastElement.lastError!!
        }
    }

    return lastElement
}

/**
 * keyboardIsNotShown
 */
fun TestDrive.keyboardIsNotShown(): TestElement {

    val testElement = TestDriver.it

    val command = "keyboardIsNotShown"
    val assertMessage = message(id = command)

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = "keyboard") {
        if (isKeyboardShown.not()) {
            TestLog.ok(message = assertMessage)
        } else {
            lastElement.lastError = TestNGException(assertMessage)
            throw lastElement.lastError!!
        }
    }

    return lastElement
}

/**
 * packageIs
 */
fun TestDrive.packageIs(expected: String): TestElement {

    val testElement = TestDriver.it

    val command = "packageIs"
    val assertMessage = message(id = command, expected = expected)

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(
        command = command,
        message = assertMessage,
        subject = testElement.subject,
        arg1 = expected
    ) {
        if (TestMode.isiOS) {
            throw NotImplementedError("packageIs function is for Android.")
        }

        val actual = rootElement.packageName
        if (actual == expected) {
            TestLog.ok(
                message = assertMessage,
                arg1 = expected
            )
        } else {
            lastElement.lastError = TestNGException("$assertMessage (actual=\"$actual\")")
            throw lastElement.lastError!!
        }
    }

    return lastElement
}

/**
 * appIs
 *
 * @param appNameOrAppId
 * Nickname [App1]
 * or appName App1
 * or packageOrBundleId com.example.app1
 */
fun TestDrive.appIs(
    appNameOrAppId: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    useCache: Boolean = testContext.useCache,
    onIrregular: (() -> Unit)? = { TestDriver.fireIrregularHandler() }
): TestElement {

    val testElement = TestDriver.it

    val command = "appIs"
    val subject = Selector(appNameOrAppId).toString()
    val assertMessage = message(id = command, subject = subject)

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {

        var result = false
        val actionFunc = {
            result = isApp(appNameOrAppId)
            result
        }

        actionFunc()

        if (result.not()) {
            val sc = doUntilActionResultTrue(
                waitSeconds = waitSeconds,
                useCache = useCache,
                actionFunc = actionFunc,
                onIrregular = onIrregular
            )
            if (sc.isTimeout) {
                TestLog.warn(message(id = "timeout", subject = "appIs", submessage = "${sc.error?.message}"))
                // Retry once on an unexpectedly long processing times occurred
                actionFunc()
            } else {
                sc.throwIfError()
            }
        }

        if (result) {
            TestLog.ok(message = assertMessage, arg1 = appNameOrAppId)
        } else {
            lastElement.lastResult = LogType.NG

            val appName = AppNameUtility.getCurrentAppName()
            val actual = if (appName.isBlank()) "?" else appName
            val ex = TestNGException("$assertMessage (actual=$actual)")
            throw ex
        }
    }

    lastElement = rootElement
    return lastElement
}

/**
 * screenIs
 */
fun TestDrive.screenIs(
    screenName: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    useCache: Boolean = testContext.useCache,
    onIrregular: (() -> Unit)? = { TestDriver.fireIrregularHandler() },
    func: (() -> Unit)? = null
): TestElement {

    val testElement = TestDriver.it

    val command = "screenIs"
    val assertMessage = message(id = command, subject = screenName)

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = screenName) {

        var result = false
        TestDriver.currentScreen = "?"
        val actionFunc = {
            result = isScreen(screenName = screenName)
            result
        }

        actionFunc()

        if (result.not()) {
            val sc = doUntilActionResultTrue(
                waitSeconds = waitSeconds,
                useCache = useCache,
                actionFunc = actionFunc,
                onIrregular = onIrregular
            )
            if (sc.isTimeout) {
                TestLog.info(message(id = "timeout", subject = "screenIs"))
                // Retry once on an unexpectedly long processing times occurred
                actionFunc()
            } else {
                sc.throwIfError()
            }
        }

        if (result) {
            TestLog.ok(message = assertMessage, arg1 = screenName)
            if (useCache.not()) {
                invalidateCache()   // matched, but not synced yet.
            }
        } else {
            lastElement.lastResult = LogType.NG

            val identity = ScreenRepository.get(screenName).identityElements.joinToString("")
            val message = "$assertMessage(currentScreen=${TestDriver.currentScreen}, expected identity=$identity)"
            val ex = TestNGException(message, lastElement.lastError)
            throw ex
        }
    }
    if (func != null) {
        func()
    }

    lastElement = rootElement
    return lastElement
}

/**
 * screenIs
 */
fun TestDrive.screenIs(
    screenName: String,
    waitSeconds: Int,
    useCache: Boolean = testContext.useCache,
    func: (() -> Unit)? = null
): TestElement {

    return screenIs(
        screenName = screenName,
        waitSeconds = waitSeconds.toDouble(),
        useCache = useCache,
        func = func
    )
}

private fun TestDrive.doUntilActionResultTrue(
    actionFunc: () -> Boolean,
    waitSeconds: Double,
    useCache: Boolean,
    onIrregular: (() -> Unit)?
): SyncContext {
    CpuLoadService.waitForCpuLoadUnder()

    val sc = SyncUtility.doUntilTrue(
        waitSeconds = waitSeconds,
        intervalSeconds = PropertiesManager.screenshotIntervalSeconds,
        refreshCache = useCache,
        throwOnError = false
    ) {
        val r = actionFunc()
        TestDriver.refreshCurrentScreen(log = false)
        TestLog.info("currentScreen=$screenName")
        if (r.not()) {
            testDrive.screenshot()
            if (onIrregular != null) {
                onIrregular.invoke()
                refreshCache()
            }
            if (testContext.enableIrregularHandler && testContext.onScreenErrorHandler != null) {
                testDrive.withoutScroll {
                    testContext.onScreenErrorHandler!!.invoke()
                }
                refreshCache()
            }
        }
        r
    }
    return sc
}

/**
 * screenIsOf
 */
fun TestDrive.screenIsOf(
    vararg screenNames: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    useCache: Boolean = testContext.useCache,
    onIrregular: (() -> Unit)? = { TestDriver.fireIrregularHandler() },
    func: (() -> Unit)? = null
): TestElement {

    val testElement = TestDriver.it

    val command = "screenIsOf"
    val assertMessage = message(id = command, subject = screenNames.joinToString(","))

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = screenNames.joinToString(",")) {
        screenIsOfCore(
            screenNames = screenNames,
            waitSeconds = waitSeconds,
            assertMessage = assertMessage,
            useCache = useCache,
            onIrregular = onIrregular
        )
    }
    if (func != null) {
        func()
    }

    return lastElement
}

/**
 * screenIs
 */
fun TestDrive.screenIsOf(
    vararg screenNames: String,
    waitSeconds: Int,
    useCache: Boolean = testContext.useCache,
    onIrregular: (() -> Unit)? = { TestDriver.fireIrregularHandler() },
    func: (() -> Unit)? = null
): TestElement {

    return screenIsOf(
        screenNames = screenNames,
        waitSeconds = waitSeconds.toDouble(),
        useCache = useCache,
        onIrregular = onIrregular,
        func = func
    )
}

internal fun TestDrive.screenIsOfCore(
    vararg screenNames: String,
    assertMessage: String,
    waitSeconds: Double,
    useCache: Boolean = testContext.useCache,
    onIrregular: (() -> Unit)?
) {
    var isScreenResult = false
    var matchedScreenName = ""
    val checkScreen = {
        for (name in screenNames) {
            isScreenResult = isScreen(name)
            if (isScreenResult) {
                matchedScreenName = name
                break
            }
        }
        isScreenResult
    }

    checkScreen()

    if (isScreenResult.not()) {
        SyncUtility.doUntilTrue(waitSeconds = waitSeconds) {
            checkScreen()
            if (isScreenResult.not()) {
                onIrregular?.invoke()
            }
            isScreenResult
        }
    }

    if (isScreenResult) {
        TestLog.ok(message = assertMessage, arg1 = matchedScreenName)
        if (useCache.not()) {
            invalidateCache()   // matched, but not synced yet.
        }
    } else {
        lastElement.lastResult = LogType.NG

        val screenIdentities = mutableMapOf<String, String>()
        for (name in screenNames) {
            val identity = ScreenRepository.get(name).identityElements.joinToString("")
            screenIdentities[name] = identity
        }
        val expected = screenIdentities.map { "${it.key}.identity=${it.value}" }.joinToString(",")
        val message = "$assertMessage(currentScreen=${TestDriver.currentScreen}, expected $expected)"
        val ex = TestNGException(message, lastElement.lastError)
        throw ex
    }
}

internal fun TestDrive.existCore(
    assertMessage: String,
    selector: Selector,
    allowScroll: Boolean = true,
    throwsException: Boolean = true,
    waitSeconds: Double = testContext.syncWaitSeconds,
    useCache: Boolean = testContext.useCache,
    swipeToCenter: Boolean,
    safeElementOnly: Boolean,
    mustValidateImage: Boolean,
    log: Boolean = CodeExecutionContext.shouldOutputLog
): TestElement {

    if (selector.capturable == "??") {
        TestLog.conditionalAuto(message = assertMessage)
        return lastElement
    }

    if (selector.isImageSelector) {
        val e = existImageCore(
            sel = selector,
            allowScroll = allowScroll,
            assertMessage = assertMessage,
            throwsException = false,
            waitSeconds = waitSeconds,
            useCache = useCache,
            swipeToCenter = swipeToCenter,
            mustValidateImage = mustValidateImage
        )
        if (e.hasError && throwsException) {
            throw e.lastError!!
        }
    }

    var e = actionWithOnExistErrorHandler(
        message = assertMessage,
        throwsException = throwsException
    ) {
        TestDriver.findImageOrSelectCore(
            selector = selector,
            allowScroll = allowScroll,
            swipeToCenter = false,
            throwsException = false,
            waitSeconds = waitSeconds,
            useCache = useCache,
            safeElementOnly = safeElementOnly
        )
    }

    if (e.hasError && throwsException) {
        throw e.lastError!!
    }
    if (e.isFound && swipeToCenter) {
        val direction = CodeExecutionContext.scrollDirection ?: ScrollDirection.Down
        e = e.swipeToCenter(axis = direction.toAxis())
    }
    if (log) {
        TestLog.ok(message = assertMessage)
    }

    return e
}

private fun TestDrive.actionWithOnExistErrorHandler(
    message: String,
    throwsException: Boolean,
    action: () -> TestElement
): TestElement {

    var e = action()
    screenshot()

    TestDriver.postProcessForAssertion(
        selectResult = e,
        assertMessage = message,
        mustValidateImage = throwsException,
        log = false
    )

    if (e.hasError && throwsException && testContext.enableIrregularHandler && testContext.onExistErrorHandler != null) {
        /**
         * Retrying with error handler
         */
        TestLog.info("Calling onExistErrorHandler.")
        testDrive.suppressHandler {
            testDrive.withoutScroll {
                testContext.onExistErrorHandler!!.invoke()
            }
        }
        e = action()
        screenshot()

        TestDriver.postProcessForAssertion(
            selectResult = e,
            assertMessage = message,
            mustValidateImage = throwsException,
            log = false
        )
    }
    return e
}

/**
 * exist
 */
fun TestDrive.exist(
    expression: String,
    allowScroll: Boolean = true,
    throwsException: Boolean = true,
    waitSeconds: Double = testContext.syncWaitSeconds,
    useCache: Boolean = testContext.useCache,
    safeElementOnly: Boolean = true,
    mustValidateImage: Boolean = false,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    if (CodeExecutionContext.isInCell && this is TestElement) {
        return existInCell(
            expression = expression,
            throwsException = throwsException,
            mustValidateImage = mustValidateImage
        )
    }

    TestDriver.refreshCurrentScreenWithNickname(expression)

    val sel = getSelector(expression = expression)
    var e = TestElement(selector = sel)

    val testElement = TestDriver.it

    val command = "exist"
    val assertMessage = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        e = existCore(
            assertMessage = assertMessage,
            selector = sel,
            allowScroll = allowScroll,
            throwsException = throwsException,
            waitSeconds = waitSeconds,
            useCache = useCache,
            swipeToCenter = false,
            safeElementOnly = safeElementOnly,
            mustValidateImage = mustValidateImage
        )
    }

    if (func != null) {
        func(e)
    }

    return e
}

/**
 * existWithoutScroll
 */
fun TestDrive.existWithoutScroll(
    expression: String,
    throwsException: Boolean = true,
    waitSeconds: Double = testContext.syncWaitSeconds,
    useCache: Boolean = testContext.useCache,
    func: (TestElement.() -> Unit)? = null
): TestElement {
    return exist(
        expression = expression,
        allowScroll = false,
        throwsException = throwsException,
        waitSeconds = waitSeconds,
        useCache = useCache,
        func = func
    )
}

/**
 * verify
 */
fun TestDrive.verify(
    message: String,
    func: (() -> Unit)
): TestElement {
    val testElement = TestDriver.it

    val command = "verify"

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = message) {

        val startCount = TestLog.allLines.count()
        val original = CodeExecutionContext.isInOperationCommand
        try {
            CodeExecutionContext.isInOperationCommand = true
            func()
            CodeExecutionContext.isInOperationCommand = original

            val endCount = TestLog.allLines.count()
            val takeCount = endCount - startCount
            val lines = TestLog.allLines.takeLast(takeCount)
            if (lines.any() { it.logType == LogType.CHECK || it.logType.isOKType }) {
                TestLog.ok(message = message)
            } else {
                TestLog.info("verify block should include one or mode assertion.")
                TestLog.manual(message = message)
            }
        } catch (t: NotImplementedError) {
            throw t
        } catch (t: Throwable) {
            val ex = TestNGException(message = message, cause = t)
            throw ex
        } finally {
            CodeExecutionContext.isInOperationCommand = original
        }
    }

    return lastElement
}

/**
 * existImage
 */
fun TestDrive.existImage(
    expression: String,
    swipeToCenter: Boolean = false,
    threshold: Double = PropertiesManager.imageMatchingThreshold,
    throwsException: Boolean = true,
    waitSeconds: Double = testContext.syncWaitSeconds,
    useCache: Boolean = testContext.useCache,
    mustValidateImage: Boolean = false,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    TestDriver.refreshCurrentScreenWithNickname(expression = expression)

    val command = "existImage"
    val sel = getSelectorForExistImage(expression = expression)
    val assertMessage = message(id = command, subject = "$sel")
    var e = TestElement(selector = sel)

    if (TestMode.isNoLoadRun) {
        if (TestMode.isManual) {
            TestLog.manual(
                message = assertMessage,
                scriptCommand = command,
                subject = "$sel"
            )
        } else {
            TestLog.conditionalAuto(
                message = assertMessage,
                scriptCommand = command,
                subject = "$sel"
            )
        }
        return e
    }

    val testElement = TestDriver.it
    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        e = existImageCore(
            sel = sel,
            threshold = threshold,
            assertMessage = assertMessage,
            throwsException = throwsException,
            waitSeconds = waitSeconds,
            useCache = useCache,
            swipeToCenter = swipeToCenter,
            mustValidateImage = mustValidateImage
        )
    }

    if (func != null) {
        func(e)
    }

    return e
}

/**
 * dontExistImage
 */
fun TestDrive.dontExistImage(
    expression: String,
    threshold: Double = PropertiesManager.imageMatchingThreshold,
    throwsException: Boolean = true,
    waitSeconds: Double = testContext.syncWaitSeconds,
    useCache: Boolean = testContext.useCache,
    mustValidateImage: Boolean = false,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    TestDriver.refreshCurrentScreenWithNickname(expression = expression)

    val command = "dontExistImage"
    val sel = getSelectorForExistImage(expression = expression)
    val assertMessage = message(id = command, subject = "$sel")
    var e = TestElement(selector = sel)

    val testElement = TestDriver.it
    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        e = existImageCore(
            sel = sel,
            threshold = threshold,
            assertMessage = assertMessage,
            throwsException = false,
            waitSeconds = waitSeconds,
            useCache = useCache,
            swipeToCenter = false,
            dontExist = true,
            mustValidateImage = mustValidateImage
        )

        if (e.imageMatchResult?.result == true) {
            if (throwsException) {
                throw TestNGException("$assertMessage (${e.imageMatchResult})")
            }
        }
    }

    if (func != null) {
        func(e)
    }

    return e
}

private fun TestDrive.existImageCore(
    sel: Selector,
    allowScroll: Boolean = true,
    threshold: Double = PropertiesManager.imageMatchingThreshold,
    assertMessage: String,
    throwsException: Boolean,
    waitSeconds: Double,
    useCache: Boolean,
    swipeToCenter: Boolean,
    dontExist: Boolean = false,
    mustValidateImage: Boolean,
    log: Boolean = CodeExecutionContext.shouldOutputLog
): TestElement {

    var e = TestElement(selector = sel)

    if (sel.isImageSelector) {
        if (PropertiesManager.enableImageAssertion.not()) {
            conditionalAuto(message = assertMessage)
            return e
        }

        e = actionWithOnExistErrorHandler(
            throwsException = throwsException,
            message = assertMessage
        ) {
            findImageAsElement(
                sel = sel,
                allowScroll = allowScroll,
                threshold = threshold,
                waitSeconds = waitSeconds,
                useCache = useCache,
            )
        }
    } else {
        e = actionWithOnExistErrorHandler(
            throwsException = throwsException,
            message = assertMessage
        ) {
            selectElementAndCompareImage(
                sel = sel,
                allowScroll = allowScroll,
                threshold = threshold,
                waitSeconds = waitSeconds,
                swipeToCenter = swipeToCenter,
                useCache = useCache
            )
        }
    }

    TestDriver.postProcessForAssertion(
        selectResult = e,
        assertMessage = assertMessage,
        mustValidateImage = mustValidateImage,
        auto = "CA",
        log = log,
        dontExist = dontExist
    )

    if (e.hasError && throwsException) {
        throw e.lastError!!
    }

    return e
}

private fun TestDrive.getSelectorForExistImage(expression: String): Selector {
    // Try getting registered selector in current screen
    val s = runCatching { getSelector(expression = expression) }.getOrNull()
    // Handle irregular
    if (s == null && testContext.enableIrregularHandler && testContext.onExistErrorHandler != null) {
        testDrive.withoutScroll {
            testContext.onExistErrorHandler!!.invoke()
        }
    }
    val sel = s ?: try {
        // Try getting registered selector in current screen again
        getSelector(expression = expression)
    } catch (t: Throwable) {
        // If there is no selector, create an image selector.
        Selector(expression.removeSuffix(".png") + ".png")
    }
    return sel
}

private fun findImageAsElement(
    sel: Selector,
    allowScroll: Boolean = true,
    threshold: Double = PropertiesManager.imageMatchingThreshold,
    waitSeconds: Double,
    useCache: Boolean,
): TestElement {

    val r = TestDriver.findImageCore(
        selector = sel,
        allowScroll = allowScroll,
        threshold = threshold,
        throwsException = false,
        waitSeconds = waitSeconds,
        useCache = useCache
    )
    val de = r.toDummyElement(selector = sel)
    if (r.imageFileEntries.isEmpty()) {
        TestLog.warn("File not found for $sel")
        return de
    }

    if (r.result) {
        de.propertyCache["bounds"] =
            "[${r.x},${r.y}][${r.templateImage!!.width},${r.templateImage!!.height}]"
    }

    return de
}

private fun selectElementAndCompareImage(
    sel: Selector,
    allowScroll: Boolean = true,
    threshold: Double = PropertiesManager.imageMatchingThreshold,
    waitSeconds: Double,
    swipeToCenter: Boolean,
    useCache: Boolean
): TestElement {

    // Select the element
    val e = TestDriver.findImageOrSelectCore(
        selector = sel,
        allowScroll = allowScroll,
        throwsException = false,
        waitSeconds = waitSeconds,
        swipeToCenter = swipeToCenter,
        useCache = useCache,
        safeElementOnly = false
    )
    if (e.isFound.not()) {
        e.imageMatchResult = ImageMatchResult(result = false, templateSubject = null)
        return e
    }
    // Compare the image of the element to the template image
    e.imageMatchResult = e.isImage(expression = "$sel", threshold = threshold, cropImage = true)
    TestLog.info(e.imageMatchResult.toString())

    return e
}

/**
 * existWithScrollDown
 */
fun TestDrive.existWithScrollDown(
    expression: String,
    throwsException: Boolean = true,
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    swipeToCenter: Boolean = false,
    mustValidateImage: Boolean = false,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val command = "existWithScrollDown"
    val sel = getSelector(expression = expression)
    var e = TestElement(selector = sel)
    val assertMessage = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(this.toTestElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)
        withScrollDown(
            scrollableElement = sc,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            e = existCore(
                assertMessage = assertMessage,
                selector = sel,
                swipeToCenter = swipeToCenter,
                safeElementOnly = true,
                throwsException = throwsException,
                mustValidateImage = mustValidateImage
            )
        }
    }
    if (func != null) {
        func(e)
    }

    return e
}

/**
 * existWithScrollUp
 */
fun TestDrive.existWithScrollUp(
    expression: String,
    throwsException: Boolean = true,
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    swipeToCenter: Boolean = false,
    mustValidateImage: Boolean = false,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val command = "existWithScrollUp"
    val sel = getSelector(expression = expression)
    var e = TestElement(selector = sel)
    val assertMessage = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(this.toTestElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)
        withScrollUp(
            scrollableElement = sc,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            e = existCore(
                assertMessage = assertMessage,
                selector = sel,
                swipeToCenter = swipeToCenter,
                safeElementOnly = true,
                throwsException = throwsException,
                mustValidateImage = mustValidateImage
            )
        }
    }
    if (func != null) {
        func(e)
    }

    return e
}

/**
 * existWithScrollRight
 */
fun TestDrive.existWithScrollRight(
    expression: String,
    throwsException: Boolean = true,
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    swipeToCenter: Boolean = false,
    mustValidateImage: Boolean = false,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val command = "existWithScrollRight"
    val sel = getSelector(expression = expression)
    var e = TestElement(selector = sel)
    val assertMessage = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(this.toTestElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)
        withScrollRight(
            scrollableElement = sc,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            e = existCore(
                assertMessage = assertMessage,
                selector = sel,
                swipeToCenter = swipeToCenter,
                safeElementOnly = true,
                throwsException = throwsException,
                mustValidateImage = mustValidateImage
            )
        }
    }
    if (func != null) {
        func(e)
    }

    return e
}

/**
 * existWithScrollLeft
 */
fun TestDrive.existWithScrollLeft(
    expression: String,
    throwsException: Boolean = true,
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    swipeToCenter: Boolean = false,
    mustValidateImage: Boolean = false,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val command = "existWithScrollLeft"
    val sel = getSelector(expression = expression)
    var e = TestElement(selector = sel)
    val assertMessage = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(this.toTestElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)
        withScrollLeft(
            scrollableElement = sc,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            e = existCore(
                assertMessage = assertMessage,
                selector = sel,
                swipeToCenter = swipeToCenter,
                safeElementOnly = true,
                throwsException = throwsException,
                mustValidateImage = mustValidateImage
            )
        }
    }
    if (func != null) {
        func(e)
    }

    return e
}

/**
 * existInScanResults
 */
fun TestDrive.existInScanResults(
    expression: String,
    throwsException: Boolean = true,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val testElement = rootElement

    val command = "existInScanResults"
    val sel = getSelector(expression = expression)
    var e = TestElement(selector = sel)
    val assertMessage = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {
        useCache {
            for (scanResult in TestElementCache.scanResults) {
                TestLog.trace("from [scanResults]")

                e = scanResult.element.findInDescendantsAndSelf(
                    selector = sel,
                )
                if (e.isEmpty.not()) {
                    TestLog.trace("found in scanResults")
                    break
                }
            }

            TestDriver.postProcessForAssertion(
                selectResult = e,
                assertMessage = assertMessage,
                dontExist = false,
                mustValidateImage = false
            )

            if (e.hasError && throwsException) {
                throw e.lastError!!
            }
        }
    }
    if (func != null) {
        func(e)
    }

    lastElement = e
    return lastElement
}

/**
 * existAll
 */
fun TestDrive.existAll(
    vararg expressions: String,
    waitSeconds: Double = testContext.syncWaitSeconds,
    useCache: Boolean = testContext.useCache,
): TestElement {

    var wsec = waitSeconds

    val sw = StopWatch()
    sw.start()

    for (expression in expressions) {
        this.exist(
            expression = expression,
            waitSeconds = wsec,
            useCache = useCache,
        )
        if (sw.elapsedMillis > waitSeconds * 1000) {
            wsec = 0.0
        }
    }

    return lastElement
}

/**
 * existAllWithScrollDown
 */
fun TestDrive.existAllWithScrollDown(
    vararg expressions: String,
): TestElement {

    for (expression in expressions) {
        this.existWithScrollDown(expression = expression)
    }

    return lastElement
}

/**
 * existAllInScanResults
 */
fun TestDrive.existAllInScanResults(
    vararg expressions: String,
): TestElement {

    for (expression in expressions) {
        this.existInScanResults(expression = expression)
    }

    return lastElement
}

internal fun TestDrive.dontExist(
    selector: Selector,
    throwsException: Boolean = true,
    waitSeconds: Double = 0.0,
    useCache: Boolean = testContext.useCache,
    mustValidateImage: Boolean = false,
    safeElementOnly: Boolean
): TestElement {

    val testElement = TestDriver.it
    var e = TestElement(selector = selector)

    val command = "dontExist"
    val assertMessage = message(id = command, subject = "$selector")

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$selector") {
        if (selector.isImageSelector) {
            if (PropertiesManager.enableImageAssertion.not()) {
                manual(message = assertMessage)
                return@execCheckCommand
            }
            imageAssertionCore(
                command = command,
                expectedSelector = selector,
                testElement = testElement,
                edgeSelector = null,
                assertMessage = assertMessage,
                negation = true,
                action = {
                    rootElement.isContainingImage(selector.image!!)
                },
            )
        } else {
            e = dontExistCore(
                message = assertMessage,
                selector = selector,
                throwsException = throwsException,
                waitSeconds = waitSeconds,
                useCache = useCache,
                safeElementOnly = safeElementOnly,
                mustValidateImage = mustValidateImage
            )
        }
    }

    return e
}

internal fun TestDrive.dontExistCore(
    message: String,
    selector: Selector,
    throwsException: Boolean = true,
    waitSeconds: Double = testContext.syncWaitSeconds,
    useCache: Boolean = testContext.useCache,
    safeElementOnly: Boolean,
    mustValidateImage: Boolean
): TestElement {

    TestDriver.syncCache()

    var e = TestDriver.findImageOrSelectCore(
        selector = selector,
        swipeToCenter = false,
        waitSeconds = 0.0,
        throwsException = false,
        useCache = useCache,
        safeElementOnly = safeElementOnly
    )

    if (waitSeconds > 0.0 && CodeExecutionContext.withScroll != true && e.isFound) {

        SyncUtility.doUntilTrue(
            waitSeconds = waitSeconds
        ) {
            e = TestElementCache.select(
                selector = selector,
                throwsException = false,
            )
            if (e.isFound) {
                refreshCache()
            }
            e.isEmpty
        }
    }

    TestDriver.postProcessForAssertion(
        selectResult = e,
        assertMessage = message,
        dontExist = true,
        mustValidateImage = mustValidateImage
    )

    lastElement = e
    if ((e.isFound || e.lastResult == LogType.ERROR) && throwsException) {
        throw e.lastError!!
    }

    return e
}

/**
 * dontExist
 */
fun TestDrive.dontExist(
    expression: String,
    throwsException: Boolean = true,
    waitSeconds: Double = 0.0,
    useCache: Boolean = testContext.useCache,
    safeElementOnly: Boolean = true,
    mustValidateImage: Boolean = false,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    if (CodeExecutionContext.isInCell && this is TestElement) {
        return dontExistInCell(
            expression = expression,
            throwsException = throwsException,
            mustValidateImage = mustValidateImage
        )
    }

    var sel = getSelector(expression = expression)
    if (sel.isImageSelector) {
        val imageInfo = ImageInfo(sel.image)
        sel =
            getSelector(expression = "${imageInfo.fileName}?s=1.0&t=0.0") // dontExist needs high accuracy comparing
        if (expression.isValidNickname()) {
            sel.nickname = expression
        }
    }

    val e = dontExist(
        selector = sel,
        throwsException = throwsException,
        waitSeconds = waitSeconds,
        useCache = useCache,
        safeElementOnly = safeElementOnly
    )

    if (func != null) {
        func(e)
    }

    return e
}

/**
 * dontExistWithoutScroll
 */
fun TestDrive.dontExistWithoutScroll(
    expression: String,
    throwsException: Boolean = true,
    waitSeconds: Double = 0.0,
    useCache: Boolean = testContext.useCache,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    var e = TestElement.emptyElement
    withoutScroll {
        e = dontExist(
            expression = expression,
            throwsException = throwsException,
            waitSeconds = waitSeconds,
            useCache = useCache,
            func = func
        )
    }
    return e
}

/**
 * dontExistWithScrollDown
 */
fun TestDrive.dontExistWithScrollDown(
    expression: String,
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
    mustValidateImage: Boolean = false,
    useCache: Boolean = testContext.useCache,
): TestElement {

    val command = "dontExistWithScrollDown"
    val selector = TestDriver.screenInfo.getSelector(expression = expression)
    var e = TestElement(selector = selector)
    val assertMessage = message(id = command, "$selector")

    val context = TestDriverCommandContext(this.toTestElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$selector") {
        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)
        withScrollDown(
            scrollableElement = sc,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            e = dontExistCore(
                message = assertMessage,
                selector = selector,
                throwsException = throwsException,
                useCache = useCache,
                safeElementOnly = true,
                mustValidateImage = mustValidateImage
            )
        }
    }

    return e
}

/**
 * dontExistWithScrollUp
 */
fun TestDrive.dontExistWithScrollUp(
    expression: String,
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
    useCache: Boolean = testContext.useCache,
    mustValidateImage: Boolean = false,
): TestElement {

    val command = "dontExistWithScrollUp"
    val selector = TestDriver.screenInfo.getSelector(expression = expression)
    var e = TestElement(selector = selector)
    val assertMessage = message(id = command, "$selector")

    val context = TestDriverCommandContext(this.toTestElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$selector") {
        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)
        withScrollUp(
            scrollableElement = sc,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            e = dontExistCore(
                message = assertMessage,
                selector = selector,
                throwsException = throwsException,
                useCache = useCache,
                safeElementOnly = true,
                mustValidateImage = mustValidateImage
            )
        }
    }

    return e
}

/**
 * dontExistAll
 */
fun TestDrive.dontExistAll(
    vararg expressions: String,
    useCache: Boolean = testContext.useCache
): TestElement {

    for (expression in expressions) {
        this.dontExist(expression = expression, useCache = useCache)
    }

    return lastElement
}

/**
 * dontExistAllInScanResult
 */
fun TestDrive.dontExistAllInScanResult(
    vararg expressions: String
): TestElement {

    for (expression in expressions) {
        this.dontExistInScanResults(expression = expression)
    }
    return lastElement
}

/**
 * dontExistInScanResults
 */
fun TestDrive.dontExistInScanResults(
    expression: String,
    throwsException: Boolean = true
): TestElement {

    val testElement = TestDriver.it

    val command = "dontExistInScanResults"
    val sel = getSelector(expression = expression)
    val assertMessage = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        var e = TestElement.emptyElement
        for (scanRoot in TestElementCache.scanResults) {
            TestLog.trace("from [scanResults]")

            e = scanRoot.element.findInDescendantsAndSelf(selector = sel)
            if (e.isEmpty.not()) {
                TestLog.trace("found in scanResults")
                break
            }
        }

        TestDriver.postProcessForAssertion(
            selectResult = e,
            assertMessage = assertMessage,
            dontExist = true,
            mustValidateImage = false
        )

        lastElement = e
        if (e.lastResult.isFailType && throwsException) {
            if (e.lastError is TestNGException) {
                (e.lastError as TestNGException).screenshot = false
            }
            throw e.lastError!!
        }
    }

    return lastElement
}

