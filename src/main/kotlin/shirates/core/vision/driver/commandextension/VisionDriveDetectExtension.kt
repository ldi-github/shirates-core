package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.driver.commandextension.getSelector
import shirates.core.exception.TestDriverException
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.SegmentContainer
import shirates.core.utility.image.saveImage
import shirates.core.utility.time.StopWatch
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.VisionImageFilterContext
import shirates.core.vision.driver.VisionContext
import shirates.core.vision.driver.doUntilTrue
import shirates.core.vision.driver.lastElement
import shirates.core.vision.driver.silent

/**
 * setOCRLanguage
 */
fun VisionDrive.setOCRLanguage(ocrLanguage: String): VisionElement {

    PropertiesManager.visionOCRLanguage = ocrLanguage
    testContext.visionOCRLanguage = ocrLanguage

    return lastElement
}

/**
 * detectElements
 */
fun VisionDrive.detectElements(
    expression: String,
    language: String = PropertiesManager.visionOCRLanguage,
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
): List<VisionElement> {

    val sel = getSelector(expression = expression)
    val list = rootElement.visionContext.detectElements(
        selector = sel,
        language = language,
        looseMatch = looseMatch,
        mergeBoundingBox = mergeBoundingBox,
        lineSpacingRatio = lineSpacingRatio
    )
    return list
}

/**
 * detect
 */
fun VisionDrive.detect(
    expression: String,
    language: String = PropertiesManager.visionOCRLanguage,
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
    last: Boolean = false,
    allowScroll: Boolean? = CodeExecutionContext.withScroll,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    waitSeconds: Double = testContext.waitSecondsForAnimationComplete,
    throwsException: Boolean = true,
): VisionElement {

    val swDetect = StopWatch("detect")
    val sel = getSelector(expression = expression)

    try {
        val v = detectCore(
            selector = sel,
            language = language,
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            autoImageFilter = autoImageFilter,
            last = last,
            allowScroll = allowScroll,
            swipeToSafePosition = swipeToSafePosition,
            waitSeconds = waitSeconds,
            throwsException = throwsException,
        )
        lastElement = v
        return v
    } finally {
        swDetect.stop()
    }
}

/**
 * detectLast
 */
fun VisionDrive.detectLast(
    expression: String,
    language: String = PropertiesManager.visionOCRLanguage,
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
    allowScroll: Boolean? = CodeExecutionContext.withScroll,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    waitSeconds: Double = testContext.waitSecondsForAnimationComplete,
    throwsException: Boolean = true,
): VisionElement {

    return detect(
        expression = expression,
        language = language,
        looseMatch = looseMatch,
        mergeBoundingBox = mergeBoundingBox,
        lineSpacingRatio = lineSpacingRatio,
        autoImageFilter = autoImageFilter,
        last = true,
        allowScroll = allowScroll,
        swipeToSafePosition = swipeToSafePosition,
        waitSeconds = waitSeconds,
        throwsException = throwsException,
    )
}

internal fun looseMatch(
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
        containerX = visionElement.rect.left,
        containerY = visionElement.rect.top,
        screenshotFile = visionElement.screenshotFile,
        screenshotImage = visionElement.screenshotImage,
        segmentMarginHorizontal = visionElement.rect.height / 2,
        segmentMarginVertical = PropertiesManager.segmentMarginVertical,
        minimumHeight = visionElement.rect.height / 2,
    ).split()
        .saveImages()
    val elements = segmentContainer.visionElements.sortedBy { it.rect.left }.toMutableList()
    if (elements.isEmpty()) {
        return visionElement
    }

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

    v2.recognizeTextLocal(
        language = language,
    )
    if (v2.visionContext.recognizeTextObservations.isNotEmpty()) {
        v2.recognizeTextObservation!!.text = expectedText
        v2.recognizeTextObservation!!.localRegionX = offsetRect.left
    }
    v2.selector = visionElement.selector
    return v2

//    visionElement.visionContext.rectOnLocalRegion = rect
//    return visionElement
}

internal fun VisionDrive.detectCore(
    selector: Selector,
    language: String,
    looseMatch: Boolean,
    mergeBoundingBox: Boolean,
    lineSpacingRatio: Double,
    autoImageFilter: Boolean,
    last: Boolean,
    allowScroll: Boolean?,
    waitSeconds: Double,
    swipeToSafePosition: Boolean,
    throwsException: Boolean,
): VisionElement {

    val sw = StopWatch("detectCore")

    var v = VisionElement.emptyElement
    if (TestMode.isNoLoadRun.not()) {

        val action = {
            v = detectCoreCore(
                selector = selector,
                language = language,
                looseMatch = looseMatch,
                mergeBoundingBox = mergeBoundingBox,
                lineSpacingRatio = lineSpacingRatio,
                autoImageFilter = autoImageFilter,
                last = last,
                allowScroll = allowScroll,
                waitSeconds = waitSeconds,
                swipeToSafePosition = swipeToSafePosition,
                throwsException = throwsException,
            )
        }
        action()
        if (v.isFound && swipeToSafePosition && allowScroll != false) {
            silent {
                v.swipeToSafePosition()
            }
            action()
        }
    }
    if (TestMode.isNoLoadRun) {
        v.selector = selector
    }
    lastElement = v
    sw.stop()
    return v
}

private fun detectInVisionContext(
    selector: Selector,
    language: String,
    looseMatch: Boolean,
    mergeBoundingBox: Boolean,
    lineSpacingRatio: Double,
    last: Boolean,
    autoImageFilter: Boolean,
): VisionElement {

    var v = VisionElement.emptyElement

    fun action() {
        v = vision.rootElement.visionContext.detect(
            selector = selector,
            language = language,
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            last = last,
        )
    }
    action()
    if (v.isEmpty && (autoImageFilter || CodeExecutionContext.visionImageFilterContext.hasFilter)) {

        var imageFilterContext = CodeExecutionContext.visionImageFilterContext
        if (imageFilterContext.hasFilter.not()) {
            imageFilterContext = VisionImageFilterContext().enhanceFaintAreas()
        }
        val filteredImage =
            imageFilterContext.processFilter(bufferedImage = CodeExecutionContext.lastScreenshotImage!!)
        val fileName = "${TestLog.currentLineNo}_filtered.png"
        val filteredFile = TestLog.directoryForLog.resolve(fileName).toString()
        filteredImage.saveImage(filteredFile)
        val filteredVisionContext = VisionContext(capture = true)
        filteredVisionContext.screenshotImage = filteredImage
        filteredVisionContext.screenshotFile = filteredFile
        vision.rootElement.visionContext = filteredVisionContext
    }
    return v
}

private fun VisionDrive.detectCoreCore(
    selector: Selector,
    language: String,
    looseMatch: Boolean,
    mergeBoundingBox: Boolean,
    lineSpacingRatio: Double,
    autoImageFilter: Boolean,
    last: Boolean,
    allowScroll: Boolean?,
    waitSeconds: Double,
    swipeToSafePosition: Boolean,
    throwsException: Boolean,
): VisionElement {

    if (lastElement.isEmpty) {
        invalidateScreen()
    }
    if (CodeExecutionContext.isScreenDirty) {
        screenshot()
    }

    if (selector.isTextSelector.not()) {
        val v = VisionElement.emptyElement
        v.selector = selector
        v.lastError = TestDriverException(
            message = message(
                id = "elementNotFound",
                subject = "$selector",
                arg1 = selector.getElementExpression(),
                arg2 = CodeExecutionContext.lastCandidateElementsString
            )
        )
        if (throwsException) {
            throw v.lastError!!
        }
        return v
    }

    var v = VisionElement.emptyElement

    doUntilTrue(
        waitSeconds =
            if (allowScroll == true) Math.min(testContext.waitSecondsForAnimationComplete, waitSeconds)
            else waitSeconds,
        intervalSeconds = 1.0,
        retryOnError = false,
        throwOnFinally = false,
        onBeforeRetry = {
            screenshot(force = true)
        }
    ) {
        v = detectInVisionContext(
            selector = selector,
            language = language,
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            last = last,
            autoImageFilter = autoImageFilter,
        )
        v.isFound
    }
    if (v.isFound) {
        return v
    }

    if (allowScroll == true && CodeExecutionContext.isScrolling.not()) {
        /**
         * Try to detect with scroll
         */
        printInfo("Text not found. Trying to detect with scroll. selector=$selector")
        v = detectWithScrollCore(
            selector = selector,
            language = language,
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            autoImageFilter = autoImageFilter,
            last = last,
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
                arg1 = selector.getElementExpression(),
                arg2 = CodeExecutionContext.lastCandidateElementsString
            )
        )
        if (throwsException) {
            TestDriver.visionRootElement.visionContext.printRecognizedTextInfo()
            throw v.lastError!!
        }
    }
    return v
}

internal fun VisionDrive.detectWithScrollCore(
    selector: Selector,
    language: String,
    looseMatch: Boolean,
    mergeBoundingBox: Boolean,
    lineSpacingRatio: Double,
    autoImageFilter: Boolean,
    last: Boolean,
    direction: ScrollDirection,
    scrollDurationSeconds: Double = CodeExecutionContext.scrollDurationSeconds,
    scrollIntervalSeconds: Double = CodeExecutionContext.scrollIntervalSeconds,
    startMarginRatio: Double = CodeExecutionContext.scrollStartMarginRatio,
    endMarginRatio: Double = CodeExecutionContext.scrollEndMarginRatio,
    scrollMaxCount: Int = CodeExecutionContext.scrollMaxCount,
    swipeToSafePosition: Boolean,
    throwsException: Boolean,
): VisionElement {

    var v = VisionElement.emptyElement

    val actionFunc = {
        v = detectInVisionContext(
            selector = selector,
            language = language,
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            last = last,
            autoImageFilter = autoImageFilter,
        )
        val stopScroll = v.isFound
        stopScroll
    }
    actionFunc()

    if (v.isFound.not() && CodeExecutionContext.isScrolling.not()) {
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
    }
    if (swipeToSafePosition) {
        v.swipeToSafePosition()
    }

    lastElement = v
    if (v.isEmpty) {
        v.lastError =
            TestDriverException(
                message = message(
                    id = "elementNotFound",
                    subject = selector.toString(),
                    arg1 = selector.expression,
                    arg2 = CodeExecutionContext.lastCandidateElementsString
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
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
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
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            autoImageFilter = autoImageFilter,
            throwsException = throwsException,
            allowScroll = true,
            swipeToSafePosition = false,
            waitSeconds = 0.0,
            looseMatch = looseMatch,
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
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
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
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            autoImageFilter = autoImageFilter,
            allowScroll = true,
            swipeToSafePosition = false,
            waitSeconds = 0.0,
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
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
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
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            autoImageFilter = autoImageFilter,
            throwsException = throwsException,
            allowScroll = true,
            swipeToSafePosition = false,
            waitSeconds = 0.0,
            looseMatch = looseMatch,
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
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
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
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            autoImageFilter = autoImageFilter,
            allowScroll = true,
            swipeToSafePosition = false,
            waitSeconds = 0.0,
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
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
    swipeToSafePosition: Boolean = false,
    waitSeconds: Double = 0.0,
    throwsException: Boolean = true,
): VisionElement {

    return detect(
        expression = expression,
        language = language,
        looseMatch = looseMatch,
        mergeBoundingBox = mergeBoundingBox,
        lineSpacingRatio = lineSpacingRatio,
        autoImageFilter = autoImageFilter,
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
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
    allowScroll: Boolean? = CodeExecutionContext.withScroll,
): Boolean {
    if (CodeExecutionContext.isInCell && this is VisionElement) {
//        return this.innerWidget(expression = expression).isFound
        throw NotImplementedError("Not implemented yet.")
    }

    val sel = getSelector(expression = expression)
    return canDetect(
        selector = sel,
        language = language,
        looseMatch = looseMatch,
        mergeBoundingBox = mergeBoundingBox,
        lineSpacingRatio = lineSpacingRatio,
        autoImageFilter = autoImageFilter,
        allowScroll = allowScroll,
    )
}

internal fun VisionDrive.canDetect(
    selector: Selector,
    language: String = PropertiesManager.visionOCRLanguage,
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = true,
    lineSpacingRatio: Double,
    autoImageFilter: Boolean = false,
    last: Boolean = false,
    allowScroll: Boolean?,
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
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            autoImageFilter = autoImageFilter,
            last = last,
            allowScroll = allowScroll,
            waitSeconds = 0.0,
            swipeToSafePosition = false,
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
    looseMatch: Boolean,
    mergeBoundingBox: Boolean,
    lineSpacingRatio: Double,
    autoImageFilter: Boolean,
    last: Boolean,
    waitSeconds: Double,
    allowScroll: Boolean?,
): Boolean {

    val v = detectCore(
        selector = selector,
        language = language,
        looseMatch = looseMatch,
        mergeBoundingBox = mergeBoundingBox,
        lineSpacingRatio = lineSpacingRatio,
        autoImageFilter = autoImageFilter,
        last = last,
        allowScroll = allowScroll,
        waitSeconds = waitSeconds,
        swipeToSafePosition = false,
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
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
): Boolean {
    return canDetect(
        expression = expression,
        language = language,
        allowScroll = false,
        looseMatch = looseMatch,
        mergeBoundingBox = mergeBoundingBox,
        lineSpacingRatio = lineSpacingRatio,
    )
}

/**
 * canDetectWithScrollDown
 */
fun VisionDrive.canDetectWithScrollDown(
    expression: String,
    language: String = PropertiesManager.visionOCRLanguage,
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
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
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
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
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
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
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
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
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
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
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
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
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
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
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            allowScroll = true,
        )
    }
    return result
}

internal fun VisionDrive.canDetectAllCore(
    selectors: Iterable<Selector>,
    language: String,
    looseMatch: Boolean,
    mergeBoundingBox: Boolean,
    lineSpacingRatio: Double,
    autoImageFilter: Boolean,
    last: Boolean,
    allowScroll: Boolean?,
): Boolean {

    val subject = selectors.joinToString()
    var foundAll = false
    val context = TestDriverCommandContext(null)
    val sw = StopWatch("canDetectAll($selectors)")
    val logLine = context.execBooleanCommand(subject = subject) {
        for (selector in selectors) {
            foundAll = canDetectCore(
                selector = selector,
                language = language,
                looseMatch = looseMatch,
                mergeBoundingBox = mergeBoundingBox,
                lineSpacingRatio = lineSpacingRatio,
                autoImageFilter = autoImageFilter,
                last = last,
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
    sw.stop()
    return foundAll
}

/**
 * canDetectAll
 */
fun VisionDrive.canDetectAll(
    vararg expressions: String,
    language: String = PropertiesManager.visionOCRLanguage,
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
    allowScroll: Boolean? = CodeExecutionContext.withScroll,
): Boolean {

    val selectors = expressions.map { getSelector(expression = it) }
    val subject = expressions.map { TestDriver.screenInfo.getSelector(expression = it) }.joinToString()
    var foundAll = false
    val context = TestDriverCommandContext(null)
    val logLine = context.execBooleanCommand(subject = subject) {
        foundAll = canDetectAllCore(
            selectors = selectors,
            language = language,
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            autoImageFilter = autoImageFilter,
            last = false,
            allowScroll = allowScroll,
        )
    }
    if (logLine != null) {
        logLine.message += " (result=$foundAll)"
    }
    return foundAll
}

/**
 * withImageFilter
 */
val VisionDrive.withImageFilter: VisionImageFilterContext
    get() {
        return VisionImageFilterContext()
    }
