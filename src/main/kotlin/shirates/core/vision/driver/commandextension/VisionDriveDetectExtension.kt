package shirates.core.vision.driver.commandextension

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
import shirates.core.utility.sync.WaitUtility
import shirates.core.utility.time.StopWatch
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement

/**
 * detect
 */
fun VisionDrive.detect(
    expression: String,
    removeChars: String? = null,
    language: String = PropertiesManager.logLanguage,
    directAccessCompletion: Boolean = true,
    allowScroll: Boolean? = null,
    swipeToCenter: Boolean = false,
    throwsException: Boolean = false,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    intervalSeconds: Double = testContext.syncIntervalSeconds,
//    frame: Bounds? = viewBounds,
): VisionElement {

    val swDetect = StopWatch("detect")
    val sel = getSelector(expression = expression)
    if (sel.isTextSelector.not()) {
        val v = VisionElement.emptyElement
        v.selector = sel
        return v
    }
    try {
        val v = detectCore(
            selector = sel,
            removeChars = removeChars,
            language = language,
            waitSeconds = waitSeconds,
            intervalSeconds = intervalSeconds,
            allowScroll = allowScroll,
            swipeToCenter = swipeToCenter,
            throwsException = throwsException,
            directAccessCompletion = directAccessCompletion
        )
        lastElement = v
        return v
    } finally {
        swDetect.printInfo()
    }
}

internal fun VisionDrive.detectCore(
    selector: Selector,
    removeChars: String?,
    language: String,
    waitSeconds: Double,
    intervalSeconds: Double,
    allowScroll: Boolean?,
    swipeToCenter: Boolean,
    throwsException: Boolean,
    directAccessCompletion: Boolean
): VisionElement {

    screenshot()

    val regionElement = CodeExecutionContext.regionElement
    if (regionElement.visionContext.visionElements.isEmpty()) {
        regionElement.recognizeText(language = language)
    }

    var v: VisionElement
    /**
     * Try to detect in visionContext
     */
    v = regionElement.visionContext.detect(
        selector = selector,
        removeChars = removeChars,
    )
    if (v.text == selector.text) {
        return v    // strict match
        /**
         * note: AI-OCR does not always recognize strictly.
         * - exact word or sentence boundaries
         * - upper case, lower case
         * - full-width, half-width
         * - white space
         * - character code (WAVE DASH, FULLWIDTH TILDE)
         * - etc
         */
    } else {
        /**
         * Try to detect in direct access
         */
        if (directAccessCompletion) {
            val sw = StopWatch("direct access attempted")
            val e = TestDriver.selectDirect(selector = selector, throwsException = false)
            sw.printInfo()
            if (e.isFound && e.bounds.isIncludedIn(viewBounds)) {
                v.testElement = e   // strict match
                return v
            }
        }
    }
    if (v.isFound) {
        printInfo("Text loose match. expression:\"${selector.expression}\", found:\"${v.text}\"")
        return v    // loose match
    }

    val isInGlobalRegion = CodeExecutionContext.isInLocalRegion.not()

    try {
        if (isInGlobalRegion && waitSeconds > 0.0) {
            if (allowScroll == true && CodeExecutionContext.withScroll == true) {
                /**
                 * Try to detect with scroll
                 */
                v = detectWithScroll(
                    selector = selector,
                    removeChars = removeChars,
                    language = language,
                    direction = CodeExecutionContext.scrollDirection ?: ScrollDirection.Down,
                    swipeToCenter = swipeToCenter,
                    throwsException = false,
                    directAccessCompletion = directAccessCompletion
                )
            } else {
                /**
                 * Try to detect without scroll
                 */
                WaitUtility.doUntilTrue(
                    waitSeconds = waitSeconds,
                    intervalSeconds = intervalSeconds,
                    throwOnFinally = throwsException,
                ) {
                    invalidateScreen()
                    screenshot()
                    TestDriver.visionRootElement.recognizeText(language = language)
                    v = TestDriver.visionRootElement.visionContext.detect(selector = selector)
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
        lastElement = v

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
    language: String = PropertiesManager.logLanguage,
    removeChars: String? = null,
    direction: ScrollDirection = ScrollDirection.Down,
    directAccessCompletion: Boolean = true,
    scrollDurationSeconds: Double = TestDriver.testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
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
            language = language,
            removeChars = removeChars,
            direction = direction,
            directAccessCompletion = directAccessCompletion,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
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
    removeChars: String?,
    language: String,
    directAccessCompletion: Boolean,
    direction: ScrollDirection,
    scrollDurationSeconds: Double = CodeExecutionContext.scrollDurationSeconds,
    scrollIntervalSeconds: Double = CodeExecutionContext.scrollIntervalSeconds,
    startMarginRatio: Double = CodeExecutionContext.scrollStartMarginRatio,
    endMarginRatio: Double = CodeExecutionContext.scrollEndMarginRatio,
    scrollMaxCount: Int = CodeExecutionContext.scrollMaxCount,
    swipeToCenter: Boolean,
    throwsException: Boolean,
): VisionElement {

    if (testDrive.isKeyboardShown) {
        testDrive.hideKeyboard(waitSeconds = 0.2)
    }

    var v = VisionElement.emptyElement
    val actionFunc = {
        v = detectCore(
            selector = selector,
            removeChars = removeChars,
            language = language,
            waitSeconds = 0.0,
            intervalSeconds = scrollIntervalSeconds,
            allowScroll = true,
            swipeToCenter = swipeToCenter,
            throwsException = false,
            directAccessCompletion = directAccessCompletion
        )
        val stopScroll = v.isFound
        stopScroll
    }

    doUntilScrollStop(
        maxLoopCount = scrollMaxCount,
        direction = direction,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio,
        repeat = 1,
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
    directAccessCompletion: Boolean = true,
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
            directAccessCompletion = directAccessCompletion,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
            swipeToCenter = swipeToCenter,
            intervalSeconds = scrollIntervalSeconds,
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
    removeChars: String? = null,
    language: String,
    directAccessCompletion: Boolean,
    scrollDurationSeconds: Double,
    scrollIntervalSeconds: Double,
    scrollStartMarginRatio: Double,
    scrollEndMarginRatio: Double,
    scrollMaxCount: Int,
    swipeToCenter: Boolean,
    intervalSeconds: Double,
    throwsException: Boolean,
): VisionElement {
    var v1 = v
    v1 = detectCore(
        selector = selector,
        removeChars = removeChars,
        language = language,
        directAccessCompletion = directAccessCompletion,
        waitSeconds = 0.0,
        allowScroll = false,
        swipeToCenter = false,
        intervalSeconds = intervalSeconds,
        throwsException = false,
    )
    if (v1.isEmpty) {
        v1 = detectWithScroll(
            selector = selector,
            language = language,
            removeChars = removeChars,
            directAccessCompletion = directAccessCompletion,
            direction = scrollDirection,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
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
    directAccessCompletion: Boolean = true,
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
            directAccessCompletion = directAccessCompletion,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
            swipeToCenter = swipeToCenter,
            intervalSeconds = scrollIntervalSeconds,
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
    directAccessCompletion: Boolean = true,
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
            directAccessCompletion = directAccessCompletion,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
            swipeToCenter = swipeToCenter,
            intervalSeconds = scrollIntervalSeconds,
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
    directAccessCompletion: Boolean = true,
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
            directAccessCompletion = directAccessCompletion,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
            swipeToCenter = swipeToCenter,
            intervalSeconds = scrollIntervalSeconds,
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
    removeChars: String? = null,
    language: String = PropertiesManager.logLanguage,
    directAccessCompletion: Boolean = true,
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
        removeChars = removeChars,
        language = language,
        directAccessCompletion = directAccessCompletion,
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
    removeChars: String? = null,
    language: String = PropertiesManager.logLanguage,
    directAccessCompletion: Boolean = true,
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
            removeChars = removeChars,
            language = language,
            directAccessCompletion = directAccessCompletion,
            waitSeconds = waitSeconds,
            allowScroll = allowScroll,
            swipeToCenter = swipeToCenter,
            intervalSeconds = 0.0,
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
    removeChars: String? = null,
    language: String,
    directAccessCompletion: Boolean = true,
    waitSeconds: Double,
    intervalSeconds: Double,
    allowScroll: Boolean,
): Boolean {

    val v = detectCore(
        selector = selector,
        removeChars = removeChars,
        language = language,
        directAccessCompletion = directAccessCompletion,
        waitSeconds = waitSeconds,
        allowScroll = allowScroll,
        swipeToCenter = false,
        intervalSeconds = intervalSeconds,
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
    waitSeconds: Double = 0.0,
    log: Boolean = false
): Boolean {
    return canDetect(
        expression = expression,
        language = language,
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
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            found = canDetectCore(
                selector = sel,
                language = language,
                waitSeconds = waitSeconds,
                intervalSeconds = scrollIntervalSeconds,
                allowScroll = true,
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
                waitSeconds = waitSeconds,
                allowScroll = allowScroll,
                intervalSeconds = 0.0,
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
        waitSeconds = waitSeconds,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        log = log,
    )
}