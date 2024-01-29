package shirates.core.driver.commandextension

import shirates.core.configuration.ImageInfo
import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.configuration.isValidNickname
import shirates.core.configuration.repository.ScreenRepository
import shirates.core.driver.*
import shirates.core.exception.TestDriverException
import shirates.core.exception.TestNGException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.misc.AppNameUtility
import shirates.core.utility.sync.SyncUtility
import shirates.core.utility.time.StopWatch

/**
 * appIs
 *
 * @param appNameOrAppId
 * Nickname [App1]
 * or appName App1
 * or packageOrBundleId com.example.app1
 */
fun TestDrive.appIs(
    appNameOrAppId: String
): TestElement {

    val testElement = TestDriver.it

    val command = "appIs"
    val subject = Selector(appNameOrAppId).toString()
    val assertMessage = message(id = command, subject = subject)

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {
        if (isApp(appNameOrAppId)) {
            TestLog.ok(
                message = assertMessage,
                arg1 = appNameOrAppId
            )
        } else {
            val appName = AppNameUtility.getCurrentAppName()
            val actual = if (appName.isBlank()) "?" else appName
            lastElement.lastError = TestNGException("$assertMessage (actual=$actual)")
            throw lastElement.lastError!!
        }
    }

    return lastElement
}

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
 * screenIs
 */
fun TestDrive.screenIs(
    screenName: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    useCache: Boolean = testContext.useCache,
    onIrregular: (() -> Unit)? = null,
    func: (() -> Unit)? = null
): TestElement {

    val testElement = TestDriver.it

    val command = "screenIs"
    val assertMessage = message(id = command, subject = screenName)

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = screenName) {
        screenIsCore(
            expectedScreenName = screenName,
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
fun TestDrive.screenIs(
    screenName: String,
    waitSeconds: Int,
    useCache: Boolean = testContext.useCache,
    func: (() -> Unit)? = null
): TestElement {

    return screenIs(screenName = screenName, waitSeconds = waitSeconds.toDouble(), useCache = useCache, func = func)
}

internal fun TestDrive.screenIsCore(
    expectedScreenName: String,
    assertMessage: String,
    waitSeconds: Double,
    onIrregular: (() -> Unit)? = null,
    useCache: Boolean
) {

    var isScreenResult = false
    TestDriver.currentScreen = "?"
    val actionFunc = {
        isScreenResult = isScreen(screenName = expectedScreenName)
        isScreenResult
    }

    actionFunc()

    if (isScreenResult.not()) {

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
                    testContext.onScreenErrorHandler!!.invoke()
                    refreshCache()
                }
            }
            r
        }
        if (sc.isTimeout) {
            TestLog.warn(message(id = "timeout", subject = "screenIs", submessage = "${sc.error?.message}"))
            // Retry once on an unexpectedly long processing times occurred
            actionFunc()
        } else {
            sc.throwIfError()
        }
    }

    if (isScreenResult) {
        TestLog.ok(message = assertMessage, arg1 = expectedScreenName)
        if (useCache.not()) {
            invalidateCache()   // matched, but not synced yet.
        }
    } else {
        lastElement.lastResult = LogType.NG

        val identity = ScreenRepository.get(expectedScreenName).identityElements.joinToString("")
        val message = "$assertMessage(currentScreen=${TestDriver.currentScreen}, expected identity=$identity)"
        val ex = TestNGException(message, lastElement.lastError)
        throw ex
    }
}

/**
 * screenIsOf
 */
fun TestDrive.screenIsOf(
    vararg screenNames: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    useCache: Boolean = testContext.useCache,
    onIrregular: (() -> Unit)? = null,
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
    onIrregular: (() -> Unit)? = null,
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
    onIrregular: (() -> Unit)? = null
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
    throwsException: Boolean = true,
    waitSeconds: Double = testContext.syncWaitSeconds,
    useCache: Boolean = testContext.useCache,
    scroll: Boolean = false,
    direction: ScrollDirection = ScrollDirection.Down,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollStartMarginRatio(direction),
    scrollEndMarginRatio: Double = testContext.scrollEndMarginRatio(direction),
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = CodeExecutionContext.shouldOutputLog
): TestElement {

    if (selector.isImageSelector) {
        val e = existImageCore(
            sel = selector,
            assertMessage = assertMessage,
            throwsException = false,
            waitSeconds = waitSeconds,
            useCache = useCache,
            scroll = scroll,
            direction = direction
        )
        if (e.hasError && throwsException) {
            throw e.lastError!!
        }
    }

    val e = actionWithOnExistErrorHandler(
        message = assertMessage,
        throwsException = throwsException
    ) {
        TestDriver.select(
            selector = selector,
            scroll = scroll,
            direction = direction,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
            throwsException = false,
            waitSeconds = waitSeconds,
            useCache = useCache,
        )
    }

    if (e.hasError && throwsException) {
        throw e.lastError!!
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
        log = false
    )

    if (e.hasError && throwsException && testContext.enableIrregularHandler && testContext.onExistErrorHandler != null) {
        /**
         * Retrying with error handler
         */
        TestLog.info("Calling onExistErrorHandler.")
        testDrive.suppressHandler {
            testContext.onExistErrorHandler!!.invoke()
        }
        e = action()
        screenshot()

        TestDriver.postProcessForAssertion(
            selectResult = e,
            assertMessage = message,
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
    throwsException: Boolean = true,
    waitSeconds: Double = testContext.syncWaitSeconds,
    useCache: Boolean = testContext.useCache,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    if (CodeExecutionContext.isInCell && this is TestElement) {
        return existInCell(expression = expression, throwsException = throwsException)
    }

    TestDriver.refreshCurrentScreenWithNickname(expression)

    val sel = getSelector(expression = expression)
    var e = TestElement(selector = sel)

    val testElement = TestDriver.it

    val command = "exist"
    val assertMessage = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        val scroll = CodeExecutionContext.withScrollDirection != null
        val direction = CodeExecutionContext.withScrollDirection ?: ScrollDirection.Down

        e = existCore(
            assertMessage = assertMessage,
            selector = sel,
            throwsException = throwsException,
            waitSeconds = waitSeconds,
            useCache = useCache,
            scroll = scroll,
            direction = direction,
        )
    }

    if (func != null) {
        func(e)
    }

    return e
}

/**
 * existImage
 */
fun TestDrive.existImage(
    expression: String,
    threshold: Double = PropertiesManager.imageMatchingThreshold,
    throwsException: Boolean = false,
    waitSeconds: Double = testContext.syncWaitSeconds,
    useCache: Boolean = testContext.useCache,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    TestDriver.refreshCurrentScreenWithNickname(expression = expression)

    val command = "existImage"
    val sel = getSelectorForExistImage(expression = expression)
    val assertMessage = message(id = command, subject = "$sel")
    var e = TestElement(selector = sel)

    val scroll = CodeExecutionContext.withScrollDirection != null
    val direction = CodeExecutionContext.withScrollDirection ?: ScrollDirection.None

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
            scroll = scroll,
            direction = direction
        )

        if (e.imageMatchResult?.result == false) {
            if (throwsException) {
                throw TestDriverException("$assertMessage (${e.imageMatchResult})")
            }
            TestLog.warn("$assertMessage (${e.imageMatchResult})")
            TestLog.manual(assertMessage)
        }
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
    func: (TestElement.() -> Unit)? = null
): TestElement {

    TestDriver.refreshCurrentScreenWithNickname(expression = expression)

    val command = "dontExistImage"
    val sel = getSelectorForExistImage(expression = expression)
    val assertMessage = message(id = command, subject = "$sel")
    var e = TestElement(selector = sel)

    val scroll = CodeExecutionContext.withScrollDirection != null
    val direction = CodeExecutionContext.withScrollDirection ?: ScrollDirection.None

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
            scroll = scroll,
            direction = direction,
            dontExist = true,
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
    threshold: Double = PropertiesManager.imageMatchingThreshold,
    assertMessage: String,
    throwsException: Boolean,
    waitSeconds: Double,
    useCache: Boolean,
    scroll: Boolean = false,
    direction: ScrollDirection = ScrollDirection.Down,
    dontExist: Boolean = false,
    log: Boolean = CodeExecutionContext.shouldOutputLog
): TestElement {

    var e = TestElement(selector = sel)

    if (sel.isImageSelector) {
        if (PropertiesManager.enableImageAssertion.not()) {
            manual(message = assertMessage)
            return e
        }

        e = actionWithOnExistErrorHandler(
            throwsException = throwsException,
            message = assertMessage
        ) {
            findImage(
                sel = sel,
                threshold = threshold,
                scroll = scroll,
                direction = direction,
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
                threshold = threshold,
                waitSeconds = waitSeconds,
                scroll = scroll,
                direction = direction,
                useCache = useCache,
            )
        }
    }

    val isTemplateImageNull = e.imageMatchResult != null && e.imageMatchResult!!.templateImageFile == null
    if (PropertiesManager.enableImageAssertion.not() || isTemplateImageNull) {
        manual(assertMessage)
        return e
    }

    TestDriver.postProcessForImageAssertion(
        e = e,
        assertMessage = assertMessage,
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
        testContext.onExistErrorHandler!!.invoke()
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

private fun findImage(
    sel: Selector,
    threshold: Double = PropertiesManager.imageMatchingThreshold,
    scroll: Boolean,
    direction: ScrollDirection,
    waitSeconds: Double,
    useCache: Boolean,
): TestElement {

    val e = TestElement(selector = sel)

    val r = TestDriver.findImage(
        selector = sel,
        threshold = threshold,
        scroll = scroll,
        direction = direction,
        scrollDurationSeconds = testContext.swipeDurationSeconds,
        scrollStartMarginRatio = testContext.scrollStartMarginRatio(direction),
        scrollEndMarginRatio = testContext.scrollEndMarginRatio(direction),
        scrollMaxCount = testContext.scrollMaxCount,
        throwsException = false,
        waitSeconds = waitSeconds,
        useCache = useCache
    )
    if (r.templateImageFile == null) {
        TestLog.warn("File not found for $sel")
        return e
    }

    e.imageMatchResult = r
    if (r.result) {
        e.propertyCache["bounds"] =
            "[${r.x},${r.y}][${r.templateImage!!.width},${r.templateImage!!.height}]"
    }

    return e
}

private fun selectElementAndCompareImage(
    sel: Selector,
    threshold: Double = PropertiesManager.imageMatchingThreshold,
    waitSeconds: Double,
    scroll: Boolean,
    direction: ScrollDirection,
    useCache: Boolean,
): TestElement {

    // Select the element
    val e = TestDriver.select(
        selector = sel,
        scroll = scroll,
        direction = direction,
        throwsException = false,
        waitSeconds = waitSeconds,
        useCache = useCache
    )
    if (e.isFound.not()) {
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
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val testElement = getThisOrLastElement()

    val command = "existWithScrollDown"
    val sel = getSelector(expression = expression)
    var e = TestElement(selector = sel)
    val assertMessage = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        e = existCore(
            assertMessage = assertMessage,
            selector = sel,
            scroll = true,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
            direction = ScrollDirection.Down,
            throwsException = throwsException,
        )
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
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val testElement = TestDriver.it

    val command = "existWithScrollUp"
    val sel = getSelector(expression = expression)
    var e = TestElement(selector = sel)
    val assertMessage = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        e = existCore(
            assertMessage = assertMessage,
            selector = sel,
            scroll = true,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
            direction = ScrollDirection.Up,
            throwsException = throwsException,
        )
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
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val testElement = TestDriver.it

    val command = "existWithScrollRight"
    val sel = getSelector(expression = expression)
    var e = TestElement(selector = sel)
    val assertMessage = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        e = existCore(
            assertMessage = assertMessage,
            selector = sel,
            scroll = true,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
            direction = ScrollDirection.Right,
            throwsException = throwsException,
        )
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
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val testElement = TestDriver.it

    val command = "existWithScrollLeft"
    val sel = getSelector(expression = expression)
    var e = TestElement(selector = sel)
    val assertMessage = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        e = existCore(
            assertMessage = assertMessage,
            selector = sel,
            scroll = true,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
            direction = ScrollDirection.Left,
            throwsException = throwsException,
        )
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
                dontExist = false
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
            imageAssertionCoreCore(
                expectedSelector = selector,
                command = command,
                testElement = testElement,
                assertMessage = assertMessage,
                waitSeconds = waitSeconds,
                action = {
                    rootElement.isContainingImage(selector.image!!)
                },
                negation = true
            )
        } else {
            e = dontExistCore(
                message = assertMessage,
                selector = selector,
                throwsException = throwsException,
                waitSeconds = waitSeconds,
                useCache = useCache,
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
    scroll: Boolean = false,
    direction: ScrollDirection = ScrollDirection.Down,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollStartMarginRatio(direction),
    scrollEndMarginRatio: Double = testContext.scrollEndMarginRatio(direction),
    scrollMaxCount: Int = testContext.scrollMaxCount,
): TestElement {

    TestDriver.syncCache()

    var e = TestDriver.select(
        selector = selector,
        scroll = scroll,
        direction = direction,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        waitSeconds = 0.0,
        throwsException = false,
        useCache = useCache,
    )

    if (waitSeconds > 0.0 && scroll.not() && e.isFound) {

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
        dontExist = true
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
    func: (TestElement.() -> Unit)? = null
): TestElement {

    if (CodeExecutionContext.isInCell && this is TestElement) {
        return dontExistInCell(expression = expression, throwsException = throwsException)
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
        useCache = useCache
    )

    if (func != null) {
        func(e)
    }

    return e
}

/**
 * dontExistWithScrollDown
 */
fun TestDrive.dontExistWithScrollDown(
    expression: String,
    throwsException: Boolean = true,
    useCache: Boolean = testContext.useCache
): TestElement {

    val testElement = TestDriver.it

    val command = "dontExistWithScrollDown"
    val selector = TestDriver.screenInfo.getSelector(expression = expression)
    var e = TestElement(selector = selector)
    val assertMessage = message(id = command, "$selector")

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$selector") {
        e = dontExistCore(
            message = assertMessage,
            selector = selector,
            throwsException = throwsException,
            useCache = useCache,
            scroll = true,
            direction = ScrollDirection.Down,
        )
    }

    return e
}

/**
 * dontExistWithScrollUp
 */
fun TestDrive.dontExistWithScrollUp(
    expression: String,
    throwsException: Boolean = true,
    useCache: Boolean = testContext.useCache,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
): TestElement {

    val testElement = TestDriver.it

    val command = "dontExistWithScrollUp"
    val selector = TestDriver.screenInfo.getSelector(expression = expression)
    var e = TestElement(selector = selector)
    val assertMessage = message(id = command, "$selector")

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$selector") {
        e = dontExistCore(
            message = assertMessage,
            selector = selector,
            throwsException = throwsException,
            useCache = useCache,
            scroll = true,
            direction = ScrollDirection.Up,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount
        )
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
            dontExist = true
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

