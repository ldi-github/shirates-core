package shirates.core.vision.driver

import shirates.core.configuration.Selector
import shirates.core.configuration.repository.ScreenRepository
import shirates.core.driver.*
import shirates.core.driver.TestDriver.currentScreen
import shirates.core.driver.commandextension.doUntilActionResultTrue
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestDriverException
import shirates.core.exception.TestNGException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.rect
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.branchextension.isScreen
import shirates.core.vision.driver.branchextension.lastScreenshotImage

internal fun VisionDrive.checkImageLabelContains(
    containedText: String,
    message: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen
): VisionElement {

    var v = getThisOrLastVisionElement()
    var contains = false

    doUntilTrue(
        waitSeconds = waitSeconds,
        onBeforeRetry = {
            screenshot(force = true)
            v = v.createFromScreenshot()
        }
    ) {
        val label = v.classify()

        printInfo("label: $label")
        contains = label.contains(containedText)
        contains
    }

    contains.thisIsTrue(message = message)

    lastVisionElement = v
    return v
}

/**
 * screenIs
 */
fun VisionDrive.screenIs(
    screenName: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    useCache: Boolean = testContext.useCache,
    onIrregular: (() -> Unit)? = { TestDriver.fireIrregularHandler() },
    func: (() -> Unit)? = null
): VisionElement {

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
            val sc = testDrive.doUntilActionResultTrue(
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
//            if (useCache.not()) {
//                invalidateCache()   // matched, but not synced yet.
//            }
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

    return lastElement
}

/**
 * screenIs
 */
fun VisionDrive.screenIs(
    screenName: String,
    waitSeconds: Int,
    useCache: Boolean = testContext.useCache,
    func: (() -> Unit)? = null
): VisionElement {

    return screenIs(
        screenName = screenName,
        waitSeconds = waitSeconds.toDouble(),
        useCache = useCache,
        func = func
    )
}


internal fun VisionDrive.existCore(
    assertMessage: String,
    selector: Selector,
    bounds: Bounds = lastScreenshotImage!!.rect.toBoundsWithRatio(),
    allowScroll: Boolean = true,
    throwsException: Boolean = true,
    waitSeconds: Double = testContext.syncWaitSeconds,
    swipeToCenter: Boolean,
    log: Boolean = CodeExecutionContext.shouldOutputLog
): VisionElement {

    if (selector.capturable == "??") {
        TestLog.conditionalAuto(message = assertMessage)
        return lastElement
    }

//    if (selector.isImageSelector) {
//        val e = existImageCore(
//            sel = selector,
//            allowScroll = allowScroll,
//            assertMessage = assertMessage,
//            throwsException = false,
//            waitSeconds = waitSeconds,
//            useCache = useCache,
//            swipeToCenter = swipeToCenter,
//            mustValidateImage = mustValidateImage
//        )
//        if (e.hasError && throwsException) {
//            throw e.lastError!!
//        }
//    }

    var v = actionWithOnExistErrorHandler(
        message = assertMessage,
        throwsException = throwsException
    ) {
        detectCore(
            selector = selector,
            rect = bounds.toRectWithRatio(),
            waitSeconds = waitSeconds,
            allowScroll = allowScroll,
            swipeToCenter = swipeToCenter,
            throwsException = throwsException
        )
    }

    if (v.hasError && throwsException) {
        throw v.lastError!!
    }
    if (v.isFound && swipeToCenter) {
        v = v.swipeToCenter()
    }
    if (log) {
        TestLog.ok(message = assertMessage)
    }

    return v
}

private fun VisionDrive.actionWithOnExistErrorHandler(
    message: String,
    throwsException: Boolean,
    action: () -> VisionElement
): VisionElement {

    var v = action()
    screenshot()

    postProcessForAssertion(
        detectResult = v,
        assertMessage = message,
        log = false
    )

    if (v.hasError && throwsException && testContext.enableIrregularHandler && testContext.onExistErrorHandler != null) {
        /**
         * Retrying with error handler
         */
        TestLog.info("Calling onExistErrorHandler.")
        testDrive.suppressHandler {
            testDrive.withoutScroll {
                testContext.onExistErrorHandler!!.invoke()
            }
        }
        v = action()
        screenshot()

        postProcessForAssertion(
            detectResult = v,
            assertMessage = message,
            log = false
        )
    }
    return v
}

internal fun postProcessForAssertion(
    detectResult: VisionElement,
    assertMessage: String,
    auto: String = "A",
    log: Boolean = CodeExecutionContext.shouldOutputLog,
    dontExist: Boolean = false
) {
    val v = detectResult

    fun setNG() {
        v.lastResult = LogType.NG
        val selectorString = "${v.selector} ($currentScreen})"
        v.lastError = TestNGException(message = assertMessage, cause = TestDriverException(selectorString))
    }

    val result = v.isFound && dontExist.not() || v.isFound.not() && dontExist
    if (result) {
        v.lastResult = TestLog.getOKType()
        if (log) {
            TestLog.ok(message = assertMessage, auto = auto)
        }
        return
    } else {
        setNG()
        return
    }
}

/**
 * existWithScrollDown
 */
fun VisionDrive.existWithScrollDown(
    expression: String,
    throwsException: Boolean = true,
    rect: Rectangle = lastScreenshotImage!!.rect,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    swipeToCenter: Boolean = false,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val command = "existWithScrollDown"
    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement
    val assertMessage = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        withScrollDown(
            rect = rect,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            v = existCore(
                assertMessage = assertMessage,
                selector = sel,
                swipeToCenter = swipeToCenter,
                throwsException = throwsException,
            )
        }
    }
    if (func != null) {
        func(v)
    }

    return v
}

/**
 * existWithScrollUp
 */
fun VisionDrive.existWithScrollUp(
    expression: String,
    throwsException: Boolean = true,
    rect: Rectangle = lastScreenshotImage!!.rect,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    swipeToCenter: Boolean = false,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val command = "existWithScrollUp"
    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement
    val assertMessage = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        withScrollUp(
            rect = rect,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            v = existCore(
                assertMessage = assertMessage,
                selector = sel,
                swipeToCenter = swipeToCenter,
                throwsException = throwsException,
            )
        }
    }
    if (func != null) {
        func(v)
    }

    return v
}

/**
 * existWithScrollRight
 */
fun VisionDrive.existWithScrollRight(
    expression: String,
    throwsException: Boolean = true,
    rect: Rectangle = lastScreenshotImage!!.rect,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    swipeToCenter: Boolean = false,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val command = "existWithScrollRight"
    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement
    val assertMessage = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        withScrollRight(
            rect = rect,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            v = existCore(
                assertMessage = assertMessage,
                selector = sel,
                swipeToCenter = swipeToCenter,
                throwsException = throwsException,
            )
        }
    }
    if (func != null) {
        func(v)
    }

    return v
}

/**
 * existWithScrollLeft
 */
fun VisionDrive.existWithScrollLeft(
    expression: String,
    throwsException: Boolean = true,
    rect: Rectangle = lastScreenshotImage!!.rect,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    swipeToCenter: Boolean = false,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val command = "existWithScrollLeft"
    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement
    val assertMessage = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        withScrollLeft(
            rect = rect,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            v = existCore(
                assertMessage = assertMessage,
                selector = sel,
                swipeToCenter = swipeToCenter,
                throwsException = throwsException,
            )
        }
    }
    if (func != null) {
        func(v)
    }

    return v
}