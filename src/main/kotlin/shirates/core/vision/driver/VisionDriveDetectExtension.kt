package shirates.core.vision.driver

import shirates.core.configuration.PropertiesManager
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
fun VisionDrive.recognizeText(
    language: String = PropertiesManager.logLanguage,
): VisionElement {

    screenshot(force = true)

    VisionContext.current = SrvisionProxy.callTextRecognizer(
        language = language
    )

    return lastElement
}

/**
 * detect
 */
fun VisionDrive.detect(
    expression: String,
    language: String = PropertiesManager.logLanguage,
    rect: Rectangle = lastScreenshotImage!!.rect,
    allowScroll: Boolean = true,
    swipeToCenter: Boolean = false,
    throwsException: Boolean = false,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
//    frame: Bounds? = viewBounds,
): VisionElement {

    val swDetect = StopWatch("detect")
    try {
//        val sel = getSelector(expression = expression)
        val sel = Selector(expression = expression)

        val v = detectCore(
            selector = sel,
            language = language,
            rect = rect,
            waitSeconds = waitSeconds,
            allowScroll = allowScroll,
            swipeToCenter = swipeToCenter,
            throwsException = throwsException,
        )
        lastElement = v
        return v
    } finally {
        swDetect.printInfo()
    }
}

internal fun VisionDrive.detectCore(
    selector: Selector,
    language: String,
    rect: Rectangle,
    waitSeconds: Double,
    allowScroll: Boolean,
    direction: ScrollDirection = CodeExecutionContext.scrollDirection ?: ScrollDirection.Down,
    swipeToCenter: Boolean,
    throwsException: Boolean,
): VisionElement {

    if (VisionContext.current.isEmpty) {
        recognizeText(language = language)
    }

    var v = VisionElement.emptyElement
    try {
        v = VisionContext.current.detect(selector = selector)

        if (v.isFound) {
            return v
        }

        if (waitSeconds > 0.0) {
            if (allowScroll && CodeExecutionContext.withScroll != false) {
                v = detectWithScroll(
                    selector = selector,
                    rect = rect,
                    direction = direction,
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
                    recognizeText(language = language)
                    v = VisionContext.current.detect(selector = selector)
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
    language: String = PropertiesManager.logLanguage,
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
        recognizeText(language = language)
        v = detectCore(
            selector = selector,
            language = language,
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

    lastElement = v
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
    language: String = PropertiesManager.logLanguage,
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

        v = detectWithScrollCore(
            scrollDirection = ScrollDirection.Down,
            v = v,
            selector = selector,
            language = language,
            rect = rect,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
            swipeToCenter = swipeToCenter,
            throwsException = throwsException,
        )
    }
    if (func != null) {
        func(v)
    }
    lastElement = v
    return v
}

private fun VisionDrive.detectWithScrollCore(
    scrollDirection: ScrollDirection,
    v: VisionElement,
    selector: Selector,
    language: String,
    rect: Rectangle,
    scrollDurationSeconds: Double,
    scrollIntervalSeconds: Double,
    scrollStartMarginRatio: Double,
    scrollEndMarginRatio: Double,
    scrollMaxCount: Int,
    swipeToCenter: Boolean,
    throwsException: Boolean
): VisionElement {
    var v1 = v
    v1 = detectCore(
        selector = selector,
        language = language,
        rect = rect,
        waitSeconds = 0.0,
        allowScroll = false,
        swipeToCenter = false,
        throwsException = false,
    )
    if (v1.isEmpty) {
        v1 = detectWithScroll(
            selector = selector,
            rect = rect,
            direction = scrollDirection,
            durationSeconds = scrollDurationSeconds,
            intervalSeconds = scrollIntervalSeconds,
            startMarginRatio = scrollStartMarginRatio,
            endMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
            swipeToCenter = swipeToCenter,
            throwsException = throwsException,
        )
    }
    return v1
}

/**
 * detectWithScrollUp
 */
fun VisionDrive.detectWithScrollUp(
    expression: String,
    language: String = PropertiesManager.logLanguage,
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

    val command = "detectWithScrollUp"

    val selector = getSelector(expression = expression)
    val message = message(id = command, subject = selector.toString())
    val context = TestDriverCommandContext(null)
    var v = VisionElement.emptyElement
    v.selector = selector
    context.execOperateCommand(command = command, message = message, subject = selector.toString(), log = log) {

        v = detectWithScrollCore(
            scrollDirection = ScrollDirection.Up,
            v = v,
            selector = selector,
            language = language,
            rect = rect,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
            swipeToCenter = swipeToCenter,
            throwsException = throwsException,
        )
    }
    if (func != null) {
        func(v)
    }
    lastElement = v
    return v
}

/**
 * detectWithScrollRight
 */
fun VisionDrive.detectWithScrollRight(
    expression: String,
    language: String = PropertiesManager.logLanguage,
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

    val command = "detectWithScrollRight"

    val selector = getSelector(expression = expression)
    val message = message(id = command, subject = selector.toString())
    val context = TestDriverCommandContext(null)
    var v = VisionElement.emptyElement
    v.selector = selector
    context.execOperateCommand(command = command, message = message, subject = selector.toString(), log = log) {

        v = detectWithScrollCore(
            scrollDirection = ScrollDirection.Right,
            v = v,
            selector = selector,
            language = language,
            rect = rect,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
            swipeToCenter = swipeToCenter,
            throwsException = throwsException,
        )
    }
    if (func != null) {
        func(v)
    }
    lastElement = v
    return v
}

/**
 * detectWithScrollLeft
 */
fun VisionDrive.detectWithScrollLeft(
    expression: String,
    language: String = PropertiesManager.logLanguage,
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

    val command = "detectWithScrollLeft"

    val selector = getSelector(expression = expression)
    val message = message(id = command, subject = selector.toString())
    val context = TestDriverCommandContext(null)
    var v = VisionElement.emptyElement
    v.selector = selector
    context.execOperateCommand(command = command, message = message, subject = selector.toString(), log = log) {

        v = detectWithScrollCore(
            scrollDirection = ScrollDirection.Left,
            v = v,
            selector = selector,
            language = language,
            rect = rect,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
            swipeToCenter = swipeToCenter,
            throwsException = throwsException,
        )
    }
    if (func != null) {
        func(v)
    }
    lastElement = v
    return v
}

/**
 * canDetect
 */
fun VisionDrive.canDetect(
    expression: String,
    language: String = PropertiesManager.logLanguage,
    rect: Rectangle = lastScreenshotImage!!.rect,
    allowScroll: Boolean = true,
    waitSeconds: Double = 0.0,
    swipeToCenter: Boolean = false,
    log: Boolean = false
): Boolean {
    if (CodeExecutionContext.isInCell && this is VisionElement) {
//        return this.innerWidget(expression = expression).isFound
        throw NotImplementedError("Not implemented yet.")
    }

    val sel = getSelector(expression = expression)
    return canDetect(
        selector = sel,
        language = language,
        rect = rect,
        allowScroll = allowScroll,
        waitSeconds = waitSeconds,
        swipeToCenter = swipeToCenter,
        log = log
    )
}

/**
 * canDetect
 */
fun VisionDrive.canDetect(
    selector: Selector,
    language: String = PropertiesManager.logLanguage,
    rect: Rectangle = lastScreenshotImage!!.rect,
    allowScroll: Boolean = true,
    waitSeconds: Double = 0.0,
    swipeToCenter: Boolean = false,
    log: Boolean = false
): Boolean {
    if (CodeExecutionContext.isInCell && this is VisionElement) {
//        return this.innerWidget(expression = expression).isFound
        throw NotImplementedError("Not implemented yet.")
    }

    var found = false
    val context = TestDriverCommandContext(null)
    val logLine = context.execBooleanCommand(subject = selector.toString(), log = log) {

        found = detectCore(
            selector = selector,
            language = language,
            rect = rect,
            waitSeconds = waitSeconds,
            allowScroll = allowScroll,
            swipeToCenter = swipeToCenter,
            throwsException = false
        ).isFound
    }
    if (logLine != null) {
        logLine.message += " (result=$found)"
    }
    return found
}

internal fun VisionDrive.canDetectCore(
    selector: Selector,
    language: String,
    rect: Rectangle,
    waitSeconds: Double,
    allowScroll: Boolean,
    scrollDirection: ScrollDirection = CodeExecutionContext.scrollDirection ?: ScrollDirection.Down,
): Boolean {

    val v = detectCore(
        selector = selector,
        language = language,
        rect = rect,
        waitSeconds = waitSeconds,
        allowScroll = allowScroll,
        direction = scrollDirection,
        swipeToCenter = false,
        throwsException = false,
    )

    return v.isFound
}

/**
 * canDetectWithoutScroll
 */
fun VisionDrive.canDetectWithoutScroll(
    expression: String,
    language: String = PropertiesManager.logLanguage,
    rect: Rectangle = lastScreenshotImage!!.rect,
    waitSeconds: Double = 0.0,
    log: Boolean = false
): Boolean {
    return canDetect(
        expression = expression,
        language = language,
        rect = rect,
        allowScroll = false,
        waitSeconds = waitSeconds,
        swipeToCenter = false,
        log = log
    )
}

/**
 * canDetectWithScrollDown
 */
fun VisionDrive.canDetectWithScrollDown(
    expression: String,
    language: String = PropertiesManager.logLanguage,
    rect: Rectangle = lastScreenshotImage!!.rect,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {

    val direction = ScrollDirection.Down
    return canDetectWithScroll(
        direction = direction,
        expression = expression,
        language = language,
        waitSeconds = waitSeconds,
        rect = rect,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        log = log,
    )
}

private fun VisionDrive.canDetectWithScroll(
    direction: ScrollDirection,
    expression: String,
    language: String,
    waitSeconds: Double,
    rect: Rectangle,
    scrollDurationSeconds: Double,
    scrollIntervalSeconds: Double,
    scrollStartMarginRatio: Double,
    scrollEndMarginRatio: Double,
    scrollMaxCount: Int,
    log: Boolean,
): Boolean {
    val sel = getSelector(expression = expression)
    var found = false
    val context = TestDriverCommandContext(null)
    val logLine = context.execBooleanCommand(subject = sel.toString(), log = log) {

        withScroll(
            direction = direction,
            rect = rect,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            found = canDetectCore(
                selector = sel,
                language = language,
                rect = rect,
                waitSeconds = waitSeconds,
                allowScroll = true,
                scrollDirection = direction
            )
        }
    }
    if (logLine != null) {
        logLine.message += " (result=$found)"
    }
    return found
}

/**
 * canDetectWithScrollUp
 */
fun VisionDrive.canDetectWithScrollUp(
    expression: String,
    language: String = PropertiesManager.logLanguage,
    rect: Rectangle = lastScreenshotImage!!.rect,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {

    val direction = ScrollDirection.Up
    return canDetectWithScroll(
        direction = direction,
        expression = expression,
        language = language,
        waitSeconds = waitSeconds,
        rect = rect,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        log = log,
    )
}

/**
 * canDetectWithScrollRight
 */
fun VisionDrive.canDetectWithScrollRight(
    expression: String,
    language: String = PropertiesManager.logLanguage,
    rect: Rectangle = lastScreenshotImage!!.rect,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {

    val direction = ScrollDirection.Right
    return canDetectWithScroll(
        direction = direction,
        expression = expression,
        language = language,
        waitSeconds = waitSeconds,
        rect = rect,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        log = log,
    )
}

/**
 * canDetectWithScrollLeft
 */
fun VisionDrive.canDetectWithScrollLeft(
    expression: String,
    language: String = PropertiesManager.logLanguage,
    rect: Rectangle = lastScreenshotImage!!.rect,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {

    val direction = ScrollDirection.Left
    return canDetectWithScroll(
        direction = direction,
        expression = expression,
        language = language,
        waitSeconds = waitSeconds,
        rect = rect,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        log = log,
    )
}

/**
 * canDetectAll
 */
internal fun VisionDrive.canDetectAll(
    selectors: Iterable<Selector>,
    language: String = PropertiesManager.logLanguage,
    allowScroll: Boolean = true,
    direction: ScrollDirection = CodeExecutionContext.scrollDirection ?: ScrollDirection.Down,
    rect: Rectangle = lastScreenshotImage!!.rect,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    log: Boolean = false
): Boolean {

    val subject = selectors.joinToString()
    var foundAll = false
    val context = TestDriverCommandContext(null)
    val logLine = context.execBooleanCommand(subject = subject, log = log) {
        for (selector in selectors) {
            foundAll = canDetectCore(
                selector = selector,
                language = language,
                rect = rect,
                waitSeconds = waitSeconds,
                allowScroll = allowScroll,
                scrollDirection = direction,
            )
            if (foundAll.not()) {
                break
            }
        }
    }
    if (logLine != null) {
        logLine.message += " (result=$foundAll)"
    }
    return foundAll
}

/**
 * canDetectAll
 */
fun VisionDrive.canDetectAll(
    vararg expressions: String,
    log: Boolean = false
): Boolean {

    val selectors = expressions.map { getSelector(expression = it) }
    val subject = expressions.map { TestDriver.screenInfo.getSelector(expression = it) }.joinToString()
    var foundAll = false
    val context = TestDriverCommandContext(null)
    val logLine = context.execBooleanCommand(subject = subject, log = log) {
        foundAll = canDetectAll(selectors = selectors)
    }
    if (logLine != null) {
        logLine.message += " (result=$foundAll)"
    }
    return foundAll
}

internal fun VisionDrive.canDetectAllWithScroll(
    vararg expressions: String,
    direction: ScrollDirection = CodeExecutionContext.scrollDirection ?: ScrollDirection.Down,
    language: String = PropertiesManager.logLanguage,
    rect: Rectangle = lastScreenshotImage!!.rect,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {

    val subject = expressions.map { TestDriver.screenInfo.getSelector(expression = it) }.joinToString()
    var foundAll = false
    val context = TestDriverCommandContext(null)
    val logLine = context.execBooleanCommand(subject = subject, log = log) {
        for (expression in expressions) {
            val result = canDetectWithScroll(
                direction = direction,
                expression = expression,
                language = language,
                waitSeconds = waitSeconds,
                rect = rect,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
                log = log
            )
            if (result.not()) {
                foundAll = false
                return@execBooleanCommand
            }
        }

        foundAll = true
    }
    if (logLine != null) {
        logLine.message += " (result=$foundAll)"
    }
    return foundAll
}

/**
 * canDetectAllWithScrollDown
 */
fun VisionDrive.canDetectAllWithScrollDown(
    vararg expressions: String,
    language: String = PropertiesManager.logLanguage,
    rect: Rectangle = lastScreenshotImage!!.rect,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {

    val scrollDirection = ScrollDirection.Down

    return canDetectAllWithScroll(
        expressions = expressions,
        direction = scrollDirection,
        language = language,
        rect = rect,
        waitSeconds = waitSeconds,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        log = log,
    )
}

/**
 * canDetectAllWithScrollUp
 */
fun VisionDrive.canDetectAllWithScrollUp(
    vararg expressions: String,
    language: String = PropertiesManager.logLanguage,
    rect: Rectangle = lastScreenshotImage!!.rect,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {

    val scrollDirection = ScrollDirection.Up

    return canDetectAllWithScroll(
        expressions = expressions,
        direction = scrollDirection,
        language = language,
        rect = rect,
        waitSeconds = waitSeconds,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        log = log,
    )
}

/**
 * canDetectAllWithScrollRight
 */
fun VisionDrive.canDetectAllWithScrollRight(
    vararg expressions: String,
    language: String = PropertiesManager.logLanguage,
    rect: Rectangle = lastScreenshotImage!!.rect,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {

    val scrollDirection = ScrollDirection.Right

    return canDetectAllWithScroll(
        expressions = expressions,
        direction = scrollDirection,
        language = language,
        rect = rect,
        waitSeconds = waitSeconds,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        log = log,
    )
}

/**
 * canDetectAllWithScrollLeft
 */
fun VisionDrive.canDetectAllWithScrollLeft(
    vararg expressions: String,
    language: String = PropertiesManager.logLanguage,
    rect: Rectangle = lastScreenshotImage!!.rect,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {

    val scrollDirection = ScrollDirection.Left

    return canDetectAllWithScroll(
        expressions = expressions,
        direction = scrollDirection,
        language = language,
        rect = rect,
        waitSeconds = waitSeconds,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        log = log,
    )
}