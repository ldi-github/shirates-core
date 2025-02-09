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
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.SegmentContainer
import shirates.core.utility.time.StopWatch
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.doUntilTrue
import shirates.core.vision.driver.lastElement
import shirates.core.vision.driver.silent

/**
 * setOCRLanguage
 */
fun VisionDrive.setOCRLanguage(ocrLanguage: String): VisionElement {

    PropertiesManager.visionOCRLanguage = ocrLanguage

    return lastElement
}

/**
 * detect
 */
fun VisionDrive.detect(
    expression: String,
    language: String = PropertiesManager.visionOCRLanguage,
    allowScroll: Boolean? = null,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    waitSeconds: Double = 0.0,
    removeRedundantText: Boolean = true,
    throwsException: Boolean = true,
): VisionElement {

    val swDetect = StopWatch("detect")
    val sel = getSelector(expression = expression)

    try {
        val v = detectCore(
            selector = sel,
            language = language,
            allowScroll = allowScroll,
            swipeToSafePosition = swipeToSafePosition,
            waitSeconds = waitSeconds,
            removeRedundantText = removeRedundantText,
            throwsException = throwsException,
        )
        lastElement = v
        return v
    } finally {
        swDetect.printInfo()
    }
}

internal fun removeRedundantText(
    visionElement: VisionElement,
    expectedText: String,
    language: String,
): VisionElement {
    /**
     * Remove redundant text
     * e.g. "# Airplane mode" -> "Airplane mode"
     */
    val segmentContainer = SegmentContainer(
        mergeIncluded = true,
        containerImage = visionElement.image,
        containerX = visionElement.rect.x,
        containerY = visionElement.rect.y,
        screenshotFile = visionElement.screenshotFile,
        screenshotImage = visionElement.screenshotImage,
        segmentMarginHorizontal = visionElement.rect.height / 2,
        segmentMarginVertical = PropertiesManager.segmentMarginVertical,
        minimumHeight = visionElement.rect.height / 2,
    ).split()
    val elements = segmentContainer.visionElements.sortedBy { it.rect.left }.toMutableList()
    var offsetRect = Rectangle()
    if (elements.count() > 1) {
        val firstElement = elements[0]
        val secondElement = elements[1]
        elements.remove(firstElement)
        offsetRect = secondElement.rect
    }
    val v2: VisionElement
    if (elements.count() == 1) {
        v2 = elements[0]
    } else {
        val expectedTokens = expectedText.split(" ").filter { it.isNotBlank() }
        val minCount = Math.min(expectedTokens.count(), elements.count())
        var rect = elements[0].rect
        for (i in 1 until minCount) {
            rect = rect.mergeWith(elements[i].rect)
        }
        v2 = rect.toVisionElement()
    }

    v2.recognizeTextLocal(language = language)
    if (v2.recognizeTextObservation != null) {
        v2.recognizeTextObservation!!.text = expectedText
        v2.recognizeTextObservation!!.localRegionX = offsetRect.x
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
    swipeToSafePosition: Boolean,
    removeRedundantText: Boolean,
    throwsException: Boolean,
): VisionElement {

    var v = VisionElement.emptyElement
    fun action() {
        v = detectCoreCore(
            selector = selector,
            language = language,
            allowScroll = allowScroll,
            waitSeconds = waitSeconds,
            swipeToSafePosition = swipeToSafePosition,
            removeRedundantText = removeRedundantText,
            throwsException = throwsException,
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
    swipeToSafePosition: Boolean,
    removeRedundantText: Boolean,
    throwsException: Boolean,
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
            screenshot(force = true)
        }
    ) {
        v = CodeExecutionContext.workingRegionElement.visionContext.detect(
            selector = selector,
            language = language,
            removeRedundantText = removeRedundantText,
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
            removeRedundantText = removeRedundantText,
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
    swipeToSafePosition: Boolean,
    removeRedundantText: Boolean,
    throwsException: Boolean,
): VisionElement {

    var v = VisionElement.emptyElement

    val actionFunc = {
        v = detectCore(
            selector = selector,
            language = language,
            allowScroll = false,
            waitSeconds = 0.0,
            swipeToSafePosition = swipeToSafePosition,
            removeRedundantText = removeRedundantText,
            throwsException = false,
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
    removeRedundantText: Boolean = true,
): Boolean {
    if (CodeExecutionContext.isInCell && this is VisionElement) {
//        return this.innerWidget(expression = expression).isFound
        throw NotImplementedError("Not implemented yet.")
    }

    var v = VisionElement.emptyElement
    val context = TestDriverCommandContext(null)
    val logLine = context.execBooleanCommand(subject = selector.toString()) {

        v = detectCore(
            selector = selector,
            language = language,
            allowScroll = allowScroll,
            waitSeconds = waitSeconds,
            swipeToSafePosition = false,
            removeRedundantText = removeRedundantText,
            throwsException = false,
        )
    }
    lastElement = v
    if (logLine != null) {
        logLine.message += " (result=${v.isFound})"
    }
    return v.isFound
}

internal fun VisionDrive.canDetectCore(
    selector: Selector,
    language: String,
    waitSeconds: Double,
    allowScroll: Boolean?,
    removeRedundantText: Boolean,
): Boolean {

    val v = detectCore(
        selector = selector,
        language = language,
        allowScroll = allowScroll,
        waitSeconds = waitSeconds,
        swipeToSafePosition = false,
        removeRedundantText = removeRedundantText,
        throwsException = false,
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
                removeRedundantText = false,
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
