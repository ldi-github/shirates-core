package shirates.core.vision.driver

import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.driver.TestDriver.expandExpression
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.commandextension.hideKeyboard
import shirates.core.driver.commandextension.isKeyboardShown
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.printInfo
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.rect
import shirates.core.utility.sync.WaitUtility
import shirates.core.utility.time.StopWatch
import shirates.core.vision.SrvisionProxy
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.branchextension.lastScreenshotImage

/**
 * recognizeText
 */
fun VisionDrive.recognizeText(): VisionElement {

    screenshot(force = true)
    SrvisionProxy.callTextRecognizer()

    return lastElement
}

/**
 * detect
 */
fun VisionDrive.detect(
    expression: String,
    rect: Rectangle = lastScreenshotImage!!.rect,
    allowScroll: Boolean = true,
    swipeToCenter: Boolean = false,
    throwsException: Boolean = false,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
//    frame: Bounds? = viewBounds,
): VisionElement {

    val swDetect = StopWatch("detect")
    try {
        val sel = getSelector(expression = expression)

        val v = detectCore(
            selector = sel,
            rect = rect,
            waitSeconds = waitSeconds,
            allowScroll = allowScroll,
            swipeToCenter = swipeToCenter,
            throwsException = throwsException,
        )
        lastVisionElement = v
        return v
    } finally {
        swDetect.printInfo()
    }
}

internal fun VisionDrive.detectCore(
    selector: Selector,
    rect: Rectangle = lastScreenshotImage!!.rect,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    allowScroll: Boolean = false,
    swipeToCenter: Boolean = false,
    throwsException: Boolean = false,
): VisionElement {

    var v = VisionElement.emptyElement
    try {
        v = VisionElementCache.detect(selector = selector, throwsException = false)
        if (v.isFound) {
            return v
        }

        recognizeText()
        v = VisionElementCache.detect(selector = selector, throwsException = false)
        if (v.isFound) {
            return v
        }

        if (waitSeconds > 0.0) {
            if (allowScroll) {
                v = detectWithScroll(
                    selector = selector,
                    rect = rect,
                    swipeToCenter = swipeToCenter,
                    throwsException = false,
                )
            } else {
                WaitUtility.doUntilTrue(
                    waitSeconds = waitSeconds,
                    onBeforeRetry = {
                        screenshot(force = true)
                    }
                ) {
                    recognizeText()
                    v = VisionElementCache.detect(selector = selector)
                    v.isFound
                }
            }
        }
        if (v.hasError) {
            v.lastResult = LogType.ERROR
            if (throwsException) {
                throw v.lastError!!
            }
        }
        return v

    } finally {
        if (TestMode.isNoLoadRun) {
            v.selector = selector
        }
    }
}

/**
 * detectWithScroll
 */
fun VisionDrive.detectWithScroll(
    expression: String,
    direction: ScrollDirection = ScrollDirection.Down,
    rect: Rectangle = lastScreenshotImage!!.rect,
    scrollDurationSeconds: Double = TestDriver.testContext.swipeDurationSeconds,
    startMarginRatio: Double = TestDriver.testContext.getScrollStartMarginRatio(direction),
    endMarginRatio: Double = TestDriver.testContext.getScrollEndMarginRatio(direction),
    scrollMaxCount: Int = TestDriver.testContext.scrollMaxCount,
    swipeToCenter: Boolean = false,
    throwsException: Boolean = true
): VisionElement {
    val sel = expandExpression(expression = expression)
    val context = TestDriverCommandContext(null)
    var v = VisionElement.emptyElement
    context.execSelectCommand(selector = sel, subject = sel.nickname) {
        v = this.detectWithScroll(
            selector = sel,
            rect = rect,
            direction = direction,
            durationSeconds = scrollDurationSeconds,
            startMarginRatio = startMarginRatio,
            endMarginRatio = endMarginRatio,
            scrollMaxCount = scrollMaxCount,
            swipeToCenter = swipeToCenter,
            throwsException = throwsException
        )
    }
    return v
}

internal fun VisionDrive.detectWithScroll(
    selector: Selector,
    rect: Rectangle = lastScreenshotImage!!.rect,
    direction: ScrollDirection = CodeExecutionContext.scrollDirection ?: ScrollDirection.Down,
    durationSeconds: Double = CodeExecutionContext.scrollDurationSeconds,
    startMarginRatio: Double = CodeExecutionContext.scrollStartMarginRatio,
    endMarginRatio: Double = CodeExecutionContext.scrollEndMarginRatio,
    intervalSeconds: Double = CodeExecutionContext.scrollIntervalSeconds,
    scrollMaxCount: Int = CodeExecutionContext.scrollMaxCount,
    swipeToCenter: Boolean,
    throwsException: Boolean = true
): VisionElement {

    if (testDrive.isKeyboardShown) {
        testDrive.hideKeyboard(waitSeconds = 0.2)
    }

    var v = VisionElement.emptyElement
    val actionFunc = {
        v = detectCore(
            selector = selector,
            rect = rect,
            waitSeconds = 0.0,
            allowScroll = true,
            swipeToCenter = swipeToCenter,
            throwsException = false,
        )
        val stopScroll = v.isFound
        stopScroll
    }

    doUntilScrollStop(
        rect = rect,
        maxLoopCount = scrollMaxCount,
        direction = direction,
        durationSeconds = durationSeconds,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio,
        repeat = 1,
        intervalSeconds = intervalSeconds,
        actionFunc = actionFunc
    )

    if (v.isFound && swipeToCenter) {
        v = v.swipeToCenter()
    }

    lastVisionElement = v
    if (v.hasError) {
        v.lastResult = LogType.ERROR
        if (throwsException) {
            throw v.lastError!!
        }
    }

    return v
}


/**
 * detectWithScrollDown
 */
fun VisionDrive.detectWithScrollDown(
    expression: String,
    rect: Rectangle = lastScreenshotImage!!.rect,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    swipeToCenter: Boolean = false,
    throwsException: Boolean = true,
    log: Boolean? = null,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val command = "detectWithScrollDown"

    val selector = getSelector(expression = expression)
    val message = message(id = command, subject = selector.toString())
    val context = TestDriverCommandContext(null)
    var v = VisionElement.emptyElement
    v.selector = selector
    context.execOperateCommand(command = command, message = message, subject = selector.toString(), log = log) {

        v = detectCore(
            selector = selector,
            rect = rect,
            waitSeconds = 0.0,
            allowScroll = false,
            swipeToCenter = false,
            throwsException = false,
        )
        if (v.isEmpty) {
            detectWithScroll(
                selector = selector,
                rect = rect,
                direction = ScrollDirection.Down,
                durationSeconds = scrollDurationSeconds,
                intervalSeconds = scrollIntervalSeconds,
                startMarginRatio = scrollStartMarginRatio,
                endMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
                swipeToCenter = swipeToCenter,
                throwsException = throwsException,
            )
        }
    }
    if (func != null) {
        func(v)
    }
    lastVisionElement = v
    return v
}