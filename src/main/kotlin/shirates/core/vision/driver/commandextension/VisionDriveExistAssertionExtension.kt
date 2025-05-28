package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.TestDriver
import shirates.core.driver.TestDriver.lastVisionElement
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.testContext
import shirates.core.exception.TestNGException
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.time.StopWatch
import shirates.core.utility.toPath
import shirates.core.vision.Candidate
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.VisionObservation
import shirates.core.vision.driver.lastElement

/**
 * exist
 */
fun VisionDrive.exist(
    expression: String,
    language: String = PropertiesManager.visionOCRLanguage,
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
    last: Boolean = false,
    waitSeconds: Double = testContext.waitSecondsForAnimationComplete,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    message: String? = null,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement

    val command = "exist"
    val assertMessage = message ?: message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        v = existCore(
            message = assertMessage,
            selector = sel,
            language = language,
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            autoImageFilter = autoImageFilter,
            last = last,
            waitSeconds = waitSeconds,
            swipeToSafePosition = swipeToSafePosition,
        )
    }

    if (func != null) {
        func(v)
    }
    return v
}

internal fun VisionDrive.existCore(
    message: String,
    selector: Selector,
    language: String,
    looseMatch: Boolean,
    mergeBoundingBox: Boolean,
    lineSpacingRatio: Double,
    autoImageFilter: Boolean,
    last: Boolean,
    waitSeconds: Double,
    swipeToSafePosition: Boolean,
): VisionElement {

    fun detectAction(): VisionElement {

        return detectCore(
            selector = selector,
            language = language,
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            autoImageFilter = autoImageFilter,
            last = last,
            allowScroll = null,
            waitSeconds = waitSeconds,
            swipeToSafePosition = swipeToSafePosition,
            throwsException = false,
        )
    }

    var v = detectAction()
    if (v.isEmpty) {
        screenshot(force = true)
        v = detectAction()
    }

    lastVisionElement = v

    if (v.isFound) {
        TestLog.ok(message = message)
        if (v != rootElement) {
            if (selector.text != null && v.text != selector.text) {
                TestLog.info(message = "There are differences in text.  (expected: \"${selector.text}\", actual: \"${v.text}\")")
            } else if (selector.textStartsWith != null && v.text.contains(selector.textStartsWith!!).not()) {
                TestLog.info(message = "There are differences in text.  (expected to be contained: \"${selector.textStartsWith}\", actual: \"${v.text}\")")
            } else if (selector.textEndsWith != null && v.text.contains(selector.textEndsWith!!).not()) {
                TestLog.info(message = "There are differences in text.  (expected to be contained: \"${selector.textEndsWith}\", actual: \"${v.text}\")")
            } else if (selector.textContains != null && v.text.contains(selector.textContains!!).not()) {
                TestLog.info(message = "There are differences in text.  (expected to be contained: \"${selector.textContains}\", actual: \"${v.text}\")")
            }
        }
    } else {
        TestDriver.visionRootElement.visionContext.printRecognizedTextInfo()
        val error = TestNGException(message = message)
        v.lastError = error
        v.lastResult = LogType.NG
        throw error
    }
    return v
}

/**
 * existWithoutScroll
 */
fun VisionDrive.existWithoutScroll(
    expression: String,
    language: String = PropertiesManager.visionOCRLanguage,
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
    waitSeconds: Double = testContext.waitSecondsForAnimationComplete,
    swipeToSafePosition: Boolean = false,
    message: String? = null,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    withoutScroll {
        exist(
            expression = expression,
            language = language,
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            autoImageFilter = autoImageFilter,
            waitSeconds = waitSeconds,
            swipeToSafePosition = swipeToSafePosition,
            message = message,
            func = func
        )
    }
    return lastElement
}


/**
 * existAll
 */
fun VisionDrive.existAll(
    vararg expressions: String,
    autoImageFilter: Boolean = false,
): VisionElement {

    for (expression in expressions) {
        this.exist(
            expression = expression,
            autoImageFilter = autoImageFilter,
        )
    }
    return lastElement
}

/**
 * dontExistAll
 */
fun VisionDrive.dontExistAll(
    vararg expressions: String,
    autoImageFilter: Boolean = false,
): VisionElement {

    for (expression in expressions) {
        this.dontExist(
            expression = expression,
            autoImageFilter = autoImageFilter
        )
    }
    return lastElement
}

/**
 * existWithScrollDown
 */
fun VisionDrive.existWithScrollDown(
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
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    message: String? = null,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val command = "existWithScrollDown"
    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement
    val assertMessage = message ?: message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        withScrollDown(
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            v = existCore(
                message = assertMessage,
                selector = sel,
                language = language,
                looseMatch = looseMatch,
                mergeBoundingBox = mergeBoundingBox,
                lineSpacingRatio = lineSpacingRatio,
                autoImageFilter = autoImageFilter,
                last = false,
                waitSeconds = 0.0,
                swipeToSafePosition = swipeToSafePosition,
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
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    message: String? = null,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val command = "existWithScrollUp"
    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement
    val assertMessage = message ?: message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        withScrollUp(
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            v = existCore(
                message = assertMessage,
                selector = sel,
                language = language,
                looseMatch = looseMatch,
                mergeBoundingBox = mergeBoundingBox,
                lineSpacingRatio = lineSpacingRatio,
                autoImageFilter = autoImageFilter,
                last = false,
                waitSeconds = 0.0,
                swipeToSafePosition = swipeToSafePosition,
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
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    message: String? = null,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val command = "existWithScrollRight"
    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement
    val assertMessage = message ?: message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        withScrollRight(
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            v = existCore(
                message = assertMessage,
                selector = sel,
                language = language,
                looseMatch = looseMatch,
                mergeBoundingBox = mergeBoundingBox,
                lineSpacingRatio = lineSpacingRatio,
                autoImageFilter = autoImageFilter,
                last = false,
                waitSeconds = 0.0,
                swipeToSafePosition = swipeToSafePosition,
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
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    message: String? = null,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val command = "existWithScrollLeft"
    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement
    val assertMessage = message ?: message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        withScrollLeft(
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            v = existCore(
                message = assertMessage,
                selector = sel,
                language = language,
                looseMatch = looseMatch,
                mergeBoundingBox = mergeBoundingBox,
                lineSpacingRatio = lineSpacingRatio,
                autoImageFilter = autoImageFilter,
                last = false,
                waitSeconds = 0.0,
                swipeToSafePosition = swipeToSafePosition,
            )
        }
    }
    if (func != null) {
        func(v)
    }

    return v
}

/**
 * dontExist
 */
fun VisionDrive.dontExist(
    expression: String,
    language: String = PropertiesManager.visionOCRLanguage,
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
    waitSeconds: Double = 0.0,
    message: String? = null,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val sw = StopWatch("dontExist")

    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement

    val command = "dontExist"
    val assertMessage = message ?: message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        v = detectCore(
            selector = sel,
            language = language,
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            autoImageFilter = autoImageFilter,
            last = false,
            allowScroll = null,
            waitSeconds = waitSeconds,
            throwsException = false,
            swipeToSafePosition = false,
        )
    }
    if (v.isFound) {
        val error = TestNGException(message = assertMessage)
        v.lastError = error
        v.lastResult = LogType.NG
        throw error
    } else {
        TestLog.ok(message = assertMessage)
        v.lastError = null
        v.lastResult = LogType.NONE
    }

    if (func != null) {
        func(v)
    }

    sw.stop()

    return v
}

/**
 * dontExistWithoutScroll
 */
fun VisionDrive.dontExistWithoutScroll(
    expression: String,
    language: String = PropertiesManager.visionOCRLanguage,
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
    waitSeconds: Double = 0.0,
    message: String? = null,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    withoutScroll {
        dontExist(
            expression = expression,
            language = language,
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            autoImageFilter = autoImageFilter,
            waitSeconds = waitSeconds,
            message = message,
            func = func
        )
    }
    return lastElement
}


internal fun VisionObservation?.getSubMessage(threshold: Double): String {

    val candidate = this as Candidate? ?: return ""
    val dir = candidate.file?.toPath()?.parent
    val submessage = " (distance=${candidate.distance}, threshold:$threshold, file://$dir)"
    return submessage
}

/**
 * existImage
 */
fun VisionDrive.existImage(
    label: String,
    threshold: Double = testContext.visionFindImageThreshold,
    skinThickness: Int = 2,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = testContext.visionFindImageAspectRatioTolerance,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    waitSeconds: Double = testContext.waitSecondsForAnimationComplete,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    message: String? = null,
): VisionElement {

    val command = "existImage"
    val assertMessage = message ?: message(id = command, subject = label)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = label) {

        val v = findImage(
            label = label,
            threshold = threshold,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            mergeIncluded = mergeIncluded,
            skinThickness = skinThickness,
            binaryThreshold = binaryThreshold,
            aspectRatioTolerance = aspectRatioTolerance,
            waitSeconds = waitSeconds,
            swipeToSafePosition = swipeToSafePosition,
            throwsException = false,
        )

        v.selector = Selector(expression = label)
        lastElement = v

        if (v.isFound.not()) {
            val submessage = v.observation.getSubMessage(threshold = threshold)
            val error = TestNGException(message = "$assertMessage$submessage")
            v.lastError = error
            v.lastResult = LogType.NG
            throw error
        }
        TestLog.ok(message = assertMessage)
    }

    return lastElement
}

/**
 * existImageWithoutScroll
 */
fun VisionDrive.existImageWithoutScroll(
    label: String,
    threshold: Double = testContext.visionFindImageThreshold,
    skinThickness: Int = 2,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = testContext.visionFindImageAspectRatioTolerance,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    waitSeconds: Double = testContext.waitSecondsForAnimationComplete,
    swipeToSafePosition: Boolean = false,
    message: String? = null,
): VisionElement {

    withoutScroll {
        existImage(
            label = label,
            threshold = threshold,
            skinThickness = skinThickness,
            binaryThreshold = binaryThreshold,
            aspectRatioTolerance = aspectRatioTolerance,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            mergeIncluded = mergeIncluded,
            waitSeconds = waitSeconds,
            swipeToSafePosition = swipeToSafePosition,
            message = message,
        )
    }

    return lastElement
}

/**
 * dontExistImage
 */
fun VisionDrive.dontExistImage(
    label: String,
    threshold: Double = testContext.visionFindImageThreshold,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    skinThickness: Int = 2,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = testContext.visionFindImageAspectRatioTolerance,
    waitSeconds: Double = 0.0,
    message: String? = null,
): VisionElement {

    val command = "dontExistImage"
    val assertMessage = message ?: message(id = command, subject = label)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = label) {

        val v = findImage(
            label = label,
            threshold = threshold,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            mergeIncluded = mergeIncluded,
            skinThickness = skinThickness,
            binaryThreshold = binaryThreshold,
            aspectRatioTolerance = aspectRatioTolerance,
            waitSeconds = waitSeconds,
            swipeToSafePosition = false,
            throwsException = false,
        )

        v.selector = Selector(expression = label)
        lastElement = v

        if (v.isFound) {
            val submessage = v.observation.getSubMessage(threshold = threshold)
            val error = TestNGException(message = "$assertMessage$submessage")
            v.lastError = error
            v.lastResult = LogType.NG
            throw error
        }
        TestLog.ok(message = assertMessage)
    }

    return lastElement
}

/**
 * dontExistImageWithoutScroll
 */
fun VisionDrive.dontExistImageWithoutScroll(
    label: String,
    threshold: Double = testContext.visionFindImageThreshold,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    skinThickness: Int = 2,
    waitSeconds: Double = 0.0,
    message: String? = null,
): VisionElement {

    withoutScroll {
        dontExistImage(
            label = label,
            threshold = threshold,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            mergeIncluded = mergeIncluded,
            skinThickness = skinThickness,
            waitSeconds = waitSeconds,
            message = message,
        )
    }

    return lastElement
}

/**
 * existImageWithScrollDown
 */
fun VisionDrive.existImageWithScrollDown(
    label: String,
    threshold: Double = testContext.visionFindImageThreshold,
    skinThickness: Int = 2,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    waitSeconds: Double = 0.0,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    message: String? = null,
): VisionElement {

    withScrollDown {
        existImage(
            label = label,
            threshold = threshold,
            skinThickness = skinThickness,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            mergeIncluded = mergeIncluded,
            waitSeconds = waitSeconds,
            swipeToSafePosition = swipeToSafePosition,
            message = message,
        )
    }

    return lastElement
}

/**
 * existImageWithScrollUp
 */
fun VisionDrive.existImageWithScrollUp(
    label: String,
    threshold: Double = testContext.visionFindImageThreshold,
    skinThickness: Int = 2,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    waitSeconds: Double = 0.0,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    message: String? = null,
): VisionElement {

    withScrollUp {
        existImage(
            label = label,
            threshold = threshold,
            skinThickness = skinThickness,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            mergeIncluded = mergeIncluded,
            waitSeconds = waitSeconds,
            swipeToSafePosition = swipeToSafePosition,
            message = message,
        )
    }

    return lastElement
}