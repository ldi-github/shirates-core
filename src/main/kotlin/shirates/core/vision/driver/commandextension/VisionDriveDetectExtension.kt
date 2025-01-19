package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.driver.commandextension.getSelector
import shirates.core.exception.TestDriverException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.printInfo
import shirates.core.utility.time.StopWatch
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.doUntilTrue
import shirates.core.vision.driver.lastElement
import shirates.core.vision.driver.silent

/**
 * detect
 */
fun VisionDrive.detect(
    expression: String,
    language: String = PropertiesManager.logLanguage,
    allowScroll: Boolean? = null,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    throwsException: Boolean = true,
): VisionElement {

    val swDetect = StopWatch("detect")
    val sel = getSelector(expression = expression)
    if (sel.isTextSelector.not()) {
        val v = VisionElement.emptyElement
        v.selector = sel
        return v
    }
    val v: VisionElement
    try {
        v = detectCore(
            selector = sel,
            language = language,
            allowScroll = allowScroll,
            swipeToSafePosition = swipeToSafePosition,
            waitSeconds = waitSeconds,
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
    allowScroll: Boolean?,
    waitSeconds: Double,
    throwsException: Boolean,
    swipeToSafePosition: Boolean,
): VisionElement {

    var v = VisionElement.emptyElement
    fun action() {
        v = detectCoreCore(
            selector = selector,
            language = language,
            allowScroll = allowScroll,
            waitSeconds = waitSeconds,
            throwsException = throwsException,
            swipeToSafePosition = swipeToSafePosition,
        )
    }
    action()
    if (v.isFound && swipeToSafePosition && CodeExecutionContext.withScroll != false) {
        silent {
            v.swipeToSafePosition()
        }
        action()
    }
    if (TestMode.isNoLoadRun) {
        v.selector = selector
    }
    lastElement = v
    return v
}

private fun VisionDrive.detectCoreCore(
    selector: Selector,
    language: String,
    allowScroll: Boolean?,
    waitSeconds: Double,
    throwsException: Boolean,
    swipeToSafePosition: Boolean,
): VisionElement {

    if (lastElement.isEmpty) {
        invalidateScreen()
    }
    screenshot()

    /**
     * Try to detect in current context
     */
    var v = VisionElement.emptyElement
    doUntilTrue(
        waitSeconds = waitSeconds,
        intervalSeconds = 1.0,
        throwOnFinally = false,
        onBeforeRetry = {
            screenshot()
        }
    ) {
        v = CodeExecutionContext.workingRegionElement.visionContext.detect(
            selector = selector,
            language = language,
        )
        v.isFound
    }
    if (v.isFound) {
        return v
    }

    if (allowScroll != false && CodeExecutionContext.withScroll == true && CodeExecutionContext.isScrolling.not()) {
        /**
         * Try to detect with scroll
         */
        printInfo("Text not found. Trying to detect with scroll. selector=$selector")
        v = detectWithScrollCore(
            selector = selector,
            language = language,
            direction = CodeExecutionContext.scrollDirection ?: ScrollDirection.Down,
            throwsException = false,
            swipeToSafePosition = swipeToSafePosition,
        )
    }

    if (v.hasError) {
        v.lastResult = LogType.ERROR
        if (throwsException) {
            throw v.lastError!!
        }
    }
    return v
}

internal fun VisionDrive.detectWithScrollCore(
    selector: Selector,
    language: String,
    direction: ScrollDirection,
    scrollDurationSeconds: Double = CodeExecutionContext.scrollDurationSeconds,
    scrollIntervalSeconds: Double = CodeExecutionContext.scrollIntervalSeconds,
    startMarginRatio: Double = CodeExecutionContext.scrollStartMarginRatio,
    endMarginRatio: Double = CodeExecutionContext.scrollEndMarginRatio,
    scrollMaxCount: Int = CodeExecutionContext.scrollMaxCount,
    throwsException: Boolean,
    swipeToSafePosition: Boolean,
): VisionElement {

    var v = VisionElement.emptyElement

    val actionFunc = {
        v = detectCore(
            selector = selector,
            language = language,
            allowScroll = false,
            throwsException = false,
            waitSeconds = 0.0,
            swipeToSafePosition = swipeToSafePosition,
        )
        val stopScroll = v.isFound
        stopScroll
    }
    actionFunc()

    if (v.isFound.not() && CodeExecutionContext.isScrolling.not()) {
        CodeExecutionContext.isScrolling = true
        try {
            /**
             * detect with scroll
             */
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
        } finally {
            CodeExecutionContext.isScrolling = false
        }
    }
    lastElement = v
    if (v.isEmpty) {
        v.lastError =
            TestDriverException(
                message = message(
                    id = "elementNotFound",
                    subject = selector.toString(),
                    arg1 = selector.expression,
                )
            )
    }
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
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    var v = VisionElement.emptyElement
    withScrollDown(
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
    ) {
        v = detect(
            expression = expression,
            language = language,
            allowScroll = true,
            swipeToSafePosition = false,
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
 * detectWithScrollUp
 */
fun VisionDrive.detectWithScrollUp(
    expression: String,
    language: String = PropertiesManager.logLanguage,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    var v = VisionElement.emptyElement
    withScrollUp(
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
    ) {
        v = detect(
            expression = expression,
            language = language,
            allowScroll = true,
            swipeToSafePosition = false,
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
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    var v = VisionElement.emptyElement
    withScrollRight(
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
    ) {
        v = detect(
            expression = expression,
            language = language,
            allowScroll = true,
            swipeToSafePosition = false,
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
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    var v = VisionElement.emptyElement
    withScrollLeft(
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
    ) {
        v = detect(
            expression = expression,
            language = language,
            allowScroll = true,
            swipeToSafePosition = false,
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
    allowScroll: Boolean = true,
): Boolean {
    if (CodeExecutionContext.isInCell && this is VisionElement) {
//        return this.innerWidget(expression = expression).isFound
        throw NotImplementedError("Not implemented yet.")
    }

    val sel = getSelector(expression = expression)
    return canDetect(
        selector = sel,
        language = language,
        allowScroll = allowScroll,
    )
}

/**
 * canDetect
 */
fun VisionDrive.canDetect(
    selector: Selector,
    language: String = PropertiesManager.logLanguage,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    allowScroll: Boolean = true,
): Boolean {
    if (CodeExecutionContext.isInCell && this is VisionElement) {
//        return this.innerWidget(expression = expression).isFound
        throw NotImplementedError("Not implemented yet.")
    }

    var found = false
    val context = TestDriverCommandContext(null)
    val logLine = context.execBooleanCommand(subject = selector.toString()) {

        found = detectCore(
            selector = selector,
            language = language,
            allowScroll = allowScroll,
            waitSeconds = waitSeconds,
            throwsException = false,
            swipeToSafePosition = false,
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
    waitSeconds: Double,
    allowScroll: Boolean,
): Boolean {

    val v = detectCore(
        selector = selector,
        language = language,
        allowScroll = allowScroll,
        waitSeconds = waitSeconds,
        throwsException = false,
        swipeToSafePosition = false,
    )
    lastElement = v

    return v.isFound
}

/**
 * canDetectWithoutScroll
 */
fun VisionDrive.canDetectWithoutScroll(
    expression: String,
    language: String = PropertiesManager.logLanguage,
): Boolean {
    return canDetect(
        expression = expression,
        language = language,
        allowScroll = false,
    )
}

/**
 * canDetectWithScrollDown
 */
fun VisionDrive.canDetectWithScrollDown(
    expression: String,
    language: String = PropertiesManager.logLanguage,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
): Boolean {

    var result = false
    withScrollDown(
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
    ) {
        result = canDetect(
            expression = expression,
            language = language,
            allowScroll = true,
        )
    }
    return result
}

private fun VisionDrive.canDetectWithScroll(
    direction: ScrollDirection,
    expression: String,
    language: String,
    scrollDurationSeconds: Double,
    scrollIntervalSeconds: Double,
    scrollStartMarginRatio: Double,
    scrollEndMarginRatio: Double,
    scrollMaxCount: Int,
    swipeToSafePosition: Boolean
): Boolean {
    val sel = getSelector(expression = expression)
    var found = false
    val context = TestDriverCommandContext(null)
    val logLine = context.execBooleanCommand(subject = sel.toString()) {

        withScroll(
            direction = direction,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
            swipeToSafePosition = swipeToSafePosition
        ) {
            found = canDetectCore(
                selector = sel,
                language = language,
                waitSeconds = 0.0,
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
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
): Boolean {

    var result = false
    withScrollUp(
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
    ) {
        result = canDetect(
            expression = expression,
            language = language,
            allowScroll = true,
        )
    }
    return result
}

/**
 * canDetectWithScrollRight
 */
fun VisionDrive.canDetectWithScrollRight(
    expression: String,
    language: String = PropertiesManager.logLanguage,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
): Boolean {

    var result = false
    withScrollRight(
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
    ) {
        result = canDetect(
            expression = expression,
            language = language,
            allowScroll = true,
        )
    }
    return result
}

/**
 * canDetectWithScrollLeft
 */
fun VisionDrive.canDetectWithScrollLeft(
    expression: String,
    language: String = PropertiesManager.logLanguage,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
): Boolean {

    var result = false
    withScrollLeft(
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
    ) {
        result = canDetect(
            expression = expression,
            language = language,
            allowScroll = true,
        )
    }
    return result
}

internal fun VisionDrive.canDetectAllCore(
    selectors: Iterable<Selector>,
    language: String,
    allowScroll: Boolean,
): Boolean {

    val subject = selectors.joinToString()
    var foundAll = false
    val context = TestDriverCommandContext(null)
    val logLine = context.execBooleanCommand(subject = subject) {
        for (selector in selectors) {
            foundAll = canDetectCore(
                selector = selector,
                language = language,
                waitSeconds = 0.0,
                allowScroll = allowScroll,
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
    language: String = PropertiesManager.logLanguage,
    allowScroll: Boolean = false,
): Boolean {

    val selectors = expressions.map { getSelector(expression = it) }
    val subject = expressions.map { TestDriver.screenInfo.getSelector(expression = it) }.joinToString()
    var foundAll = false
    val context = TestDriverCommandContext(null)
    val logLine = context.execBooleanCommand(subject = subject) {
        foundAll = canDetectAllCore(
            selectors = selectors,
            language = language,
            allowScroll = allowScroll,
        )
    }
    if (logLine != null) {
        logLine.message += " (result=$foundAll)"
    }
    return foundAll
}

internal fun VisionDrive.canDetectAllWithScroll(
    vararg expressions: String,
    direction: ScrollDirection = CodeExecutionContext.scrollDirection ?: ScrollDirection.Down,
    language: String,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    swipeToSafePosition: Boolean
): Boolean {

    val subject = expressions.map { TestDriver.screenInfo.getSelector(expression = it) }.joinToString()
    var foundAll = false
    val context = TestDriverCommandContext(null)
    val logLine = context.execBooleanCommand(subject = subject) {
        for (expression in expressions) {
            val result = canDetectWithScroll(
                direction = direction,
                expression = expression,
                language = language,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
                swipeToSafePosition = swipeToSafePosition
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
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
): Boolean {

    val scrollDirection = ScrollDirection.Down

    return canDetectAllWithScroll(
        expressions = expressions,
        direction = scrollDirection,
        language = language,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        swipeToSafePosition = false
    )
}

/**
 * canDetectAllWithScrollUp
 */
fun VisionDrive.canDetectAllWithScrollUp(
    vararg expressions: String,
    language: String = PropertiesManager.logLanguage,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
): Boolean {

    val scrollDirection = ScrollDirection.Up

    return canDetectAllWithScroll(
        expressions = expressions,
        direction = scrollDirection,
        language = language,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        swipeToSafePosition = false
    )
}

/**
 * canDetectAllWithScrollRight
 */
fun VisionDrive.canDetectAllWithScrollRight(
    vararg expressions: String,
    language: String = PropertiesManager.logLanguage,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
): Boolean {

    val scrollDirection = ScrollDirection.Right

    return canDetectAllWithScroll(
        expressions = expressions,
        direction = scrollDirection,
        language = language,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        swipeToSafePosition = false
    )
}

/**
 * canDetectAllWithScrollLeft
 */
fun VisionDrive.canDetectAllWithScrollLeft(
    vararg expressions: String,
    language: String = PropertiesManager.logLanguage,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
): Boolean {

    val scrollDirection = ScrollDirection.Left

    return canDetectAllWithScroll(
        expressions = expressions,
        direction = scrollDirection,
        language = language,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        swipeToSafePosition = false
    )
}