package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.driver.commandextension.getSelector
import shirates.core.exception.TestDriverException
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.printInfo
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.image.SegmentContainer
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
    language: String = PropertiesManager.visionOCRLanguage,
    allowScroll: Boolean? = null,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    waitSeconds: Double = 0.0,
    throwsException: Boolean = true,
): VisionElement {

    val swDetect = StopWatch("detect")
    val sel = getSelector(expression = expression)

    try {
        var v = detectCore(
            selector = sel,
            language = language,
            allowScroll = allowScroll,
            swipeToSafePosition = swipeToSafePosition,
            waitSeconds = waitSeconds,
            throwsException = throwsException,
        )
        if (v.text.indexOf(expression) > 0) {
            val v2 = removeRedundantText(v)
            v = v2
        }
        lastElement = v
        return v
    } finally {
        swDetect.printInfo()
    }
}

private fun removeRedundantText(visionElement: VisionElement): VisionElement {
    /**
     * Remove redundant text
     * e.g. "# Airplane mode" -> "Airplane mode"
     */
    val segmentContainer = SegmentContainer(
        mergeIncluded = true,
        containerImage = visionElement.image,
        containerX = visionElement.rect.x,
        containerY = visionElement.rect.y,
        segmentMarginHorizontal = 0,
        segmentMarginVertical = 0,
        minimumHeight = visionElement.rect.height / 2,
    ).split()
    val elements = segmentContainer.visionElements.sortedBy { it.rect.left }.toMutableList()
    if (elements.size > 1) {
        elements.removeFirst()
    }

    var rect = elements[0].rect
    for (i in 1 until elements.size) {
        rect = rect.mergeWith(elements[i].rect)
    }
    val v2 = rect.toVisionElement()
    v2.visionContext.recognizeTextObservations = visionElement.visionContext.recognizeTextObservations

    if (v2.recognizeTextObservation != null && visionElement.selector?.text.isNullOrBlank().not()) {
        val index = visionElement.text.indexOf(visionElement.selector!!.text!!)
        if (index > 0) {
            v2.recognizeTextObservation!!.text = visionElement.text.substring(index)
        }
    }

    v2.selector = visionElement.selector
    return v2

//    visionElement.visionContext.rectOnLocalRegion = rect
//    return visionElement
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

    if (selector.isTextSelector.not()) {
        val v = VisionElement.emptyElement
        v.selector = selector
        v.lastError = TestDriverException(
            message = message(
                id = "elementNotFound",
                subject = "$selector",
                arg1 = selector.getElementExpression()
            )
        )
        if (throwsException) {
            throw v.lastError!!
        }
        return v
    }

    /**
     * Try to detect in current context
     */
    var v = VisionElement.emptyElement
    doUntilTrue(
        waitSeconds = waitSeconds,
        intervalSeconds = 1.0,
        retryOnError = false,
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

    if (v.isFound.not()) {
        v.lastError = TestDriverException(
            message = message(
                id = "elementNotFound",
                subject = "$selector",
                arg1 = selector.getElementExpression()
            )
        )
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
    language: String = PropertiesManager.visionOCRLanguage,
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
    language: String = PropertiesManager.visionOCRLanguage,
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
    language: String = PropertiesManager.visionOCRLanguage,
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
    language: String = PropertiesManager.visionOCRLanguage,
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
 * detectWithoutScroll
 */
fun VisionDrive.detectWithoutScroll(
    expression: String,
    language: String = PropertiesManager.visionOCRLanguage,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    waitSeconds: Double = 0.0,
    throwsException: Boolean = true,
): VisionElement {

    return detect(
        expression = expression,
        language = language,
        allowScroll = false,
        swipeToSafePosition = swipeToSafePosition,
        waitSeconds = waitSeconds,
        throwsException = throwsException,
    )
}

/**
 * canDetect
 */
fun VisionDrive.canDetect(
    expression: String,
    language: String = PropertiesManager.visionOCRLanguage,
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
    language: String = PropertiesManager.visionOCRLanguage,
    waitSeconds: Double = 0.0,
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
    allowScroll: Boolean?,
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
    language: String = PropertiesManager.visionOCRLanguage,
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
    language: String = PropertiesManager.visionOCRLanguage,
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

/**
 * canDetectWithScrollUp
 */
fun VisionDrive.canDetectWithScrollUp(
    expression: String,
    language: String = PropertiesManager.visionOCRLanguage,
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
    language: String = PropertiesManager.visionOCRLanguage,
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
    language: String = PropertiesManager.visionOCRLanguage,
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
    allowScroll: Boolean?,
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
    language: String = PropertiesManager.visionOCRLanguage,
    allowScroll: Boolean? = null,
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
