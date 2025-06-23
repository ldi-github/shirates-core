package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.TestDriver
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.commandextension.thisContains
import shirates.core.driver.testContext
import shirates.core.exception.TestNGException
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.string.forVisionComparison
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement

private fun String.process(digitOnly: Boolean): String {
    var s = this.forVisionComparison()
    if (digitOnly) {
        s = s.replace("[^\\d\\s]".toRegex(), "")
            .replace("[\\s+]".toRegex(), " ")
    }
    return s
}

/**
 * textIs
 */
fun VisionElement.textIs(
    expected: String,
    message: String? = null,
): VisionElement {

    val command = "textIs"
    val assertMessage =
        message ?: message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val v = this

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {

        val result = v.textForComparison.contains(expected.forVisionComparison())
        if (result) {
            TestLog.ok(message = assertMessage)
        } else {
            val errorMessage = "$assertMessage (actual=${v.text})"
            TestDriver.lastElement.lastError = TestNGException(errorMessage)
            throw TestDriver.lastElement.lastError!!
        }
    }
    return v
}

/**
 * imageIs
 * (image label contains)
 */
fun VisionElement.imageIs(
    label: String,
    threshold: Double = PropertiesManager.visionFindImageThreshold,
    classifierName: String = "DefaultClassifier",
    waitSeconds: Double = testContext.syncWaitSeconds,
    message: String? = null,
): VisionElement {

    val command = "imageIs"
    val assertMessage =
        message ?: message(id = command, subject = subject, expected = label, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {

        this.checkImageLabelContains(
            containedText = label,
            message = assertMessage,
            classifierName = classifierName,
            waitSeconds = waitSeconds,
            fullLabel = false,
            threshold = threshold
        )
    }
    return this
}

/**
 * imageFullLabelIs
 */
fun VisionElement.imageFullLabelIs(
    label: String,
    threshold: Double = PropertiesManager.visionFindImageThreshold,
    classifierName: String = "DefaultClassifier",
    waitSeconds: Double = testContext.syncWaitSeconds,
    message: String? = null,
): VisionElement {

    val command = "imageFullLabelIs"
    val assertMessage =
        message ?: message(id = command, subject = subject, expected = label, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {

        this.checkImageLabelContains(
            containedText = label,
            message = assertMessage,
            classifierName = classifierName,
            waitSeconds = waitSeconds,
            fullLabel = true,
            threshold = threshold
        )
    }
    return this
}

/**
 * checkIsON
 */
fun VisionElement.checkIsON(
    threshold: Double = 1.0,
    classifierName: String = "CheckStateClassifier",
    containedText: String = "[ON]",
    waitSeconds: Double = testContext.syncWaitSeconds,
    message: String? = null,
): VisionElement {

    val command = "checkIsON"
    val assertMessage = message ?: message(id = command, subject = subject, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {
        this.checkIsCore(
            containedText = containedText,
            message = assertMessage,
            classifierName = classifierName,
            waitSeconds = waitSeconds,
            threshold = threshold
        )
    }
    return this
}

/**
 * checkIsOFF
 */
fun VisionElement.checkIsOFF(
    threshold: Double = 1.0,
    classifierName: String = "CheckStateClassifier",
    containedText: String = "[OFF]",
    waitSeconds: Double = testContext.syncWaitSeconds,
    message: String? = null,
): VisionElement {

    val command = "checkIsOFF"
    val assertMessage = message ?: message(id = command, subject = subject, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {
        this.checkIsCore(
            containedText = containedText,
            message = assertMessage,
            classifierName = classifierName,
            waitSeconds = waitSeconds,
            threshold = threshold
        )
    }
    return this
}

/**
 * buttonStateIs
 */
fun VisionElement.buttonStateIs(
    expectedLabel: String,
    threshold: Double = 1.0,
    waitSeconds: Double = testContext.syncWaitSeconds,
    classifierName: String = "ButtonStateClassifier",
    message: String? = null,
): VisionElement {

    val command = "buttonStateIs"
    val assertMessage =
        message ?: message(id = command, subject = subject, expected = expectedLabel, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {
        this.checkImageLabelContains(
            containedText = expectedLabel,
            message = assertMessage,
            waitSeconds = waitSeconds,
            classifierName = classifierName,
            fullLabel = false,
            threshold = threshold
        )
    }
    return this
}

/**
 * buttonIsActive
 */
fun VisionElement.buttonIsActive(
    threshold: Double = 1.0,
    classifierName: String = "ButtonStateClassifier",
    expectedLabel: String = "[Active]",
    waitSeconds: Double = testContext.syncWaitSeconds,
    message: String? = null,
): VisionElement {

    val command = "buttonIsActive"
    val assertMessage = message ?: message(id = command, subject = subject, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {
        this.buttonStateIs(
            expectedLabel = expectedLabel,
            message = assertMessage,
            classifierName = classifierName,
            waitSeconds = waitSeconds,
            threshold = threshold
        )
    }
    return this
}

/**
 * buttonIsNotActive
 */
fun VisionElement.buttonIsNotActive(
    threshold: Double = 1.0,
    classifierName: String = "ButtonStateClassifier",
    expectedLabel: String = "[NotActive]",
    waitSeconds: Double = testContext.syncWaitSeconds,
    message: String? = null,
): VisionElement {

    val command = "buttonIsNotActive"
    val assertMessage = message ?: message(id = command, subject = subject, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {
        this.buttonStateIs(
            expectedLabel = expectedLabel,
            message = assertMessage,
            classifierName = classifierName,
            waitSeconds = waitSeconds,
            threshold = threshold
        )
    }
    return this
}

/**
 * itemStateIs
 */
fun VisionElement.itemStateIs(
    expectedLabel: String,
    threshold: Double = 1.0,
    waitSeconds: Double = testContext.syncWaitSeconds,
    classifierName: String = "ItemStateClassifier",
    message: String? = null,
): VisionElement {

    val command = "itemStateIs"
    val assertMessage =
        message ?: message(id = command, subject = subject, expected = expectedLabel, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {
        this.checkImageLabelContains(
            containedText = expectedLabel,
            message = assertMessage,
            waitSeconds = waitSeconds,
            classifierName = classifierName,
            fullLabel = false,
            threshold = threshold
        )
    }
    return this
}

/**
 * itemIsActive
 */
fun VisionElement.itemIsActive(
    threshold: Double = 1.0,
    classifierName: String = "ItemStateClassifier",
    expectedLabel: String = "[Active]",
    waitSeconds: Double = testContext.syncWaitSeconds,
    message: String? = null,
): VisionElement {

    val command = "itemIsActive"
    val assertMessage = message ?: message(id = command, subject = subject, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {
        this.itemStateIs(
            expectedLabel = expectedLabel,
            message = assertMessage,
            classifierName = classifierName,
            waitSeconds = waitSeconds,
            threshold = threshold
        )
    }
    return this
}

/**
 * itemIsNotActive
 */
fun VisionElement.itemIsNotActive(
    threshold: Double = 1.0,
    classifierName: String = "ItemStateClassifier",
    expectedLabel: String = "[NotActive]",
    waitSeconds: Double = testContext.syncWaitSeconds,
    message: String? = null,
): VisionElement {

    val command = "itemIsNotActive"
    val assertMessage = message ?: message(id = command, subject = subject, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {
        this.itemStateIs(
            expectedLabel = expectedLabel,
            message = assertMessage,
            classifierName = classifierName,
            waitSeconds = waitSeconds,
            threshold = threshold
        )
    }
    return this
}

/**
 * existOnLine
 */
fun VisionElement.existOnLine(
    expression: String,
    language: String = testContext.visionOCRLanguage,
    looseMatch: Boolean = testContext.visionLooseMatch,
    mergeBoundingBox: Boolean = testContext.visionMergeBoundingBox,
    lineSpacingRatio: Double = testContext.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
    last: Boolean = false,
    lineHeightRatio: Double = testContext.visionTextToLineHeightRatio,
    lineHeight: Int = (this.rect.height * lineHeightRatio).toInt(),
    allowScroll: Boolean? = CodeExecutionContext.withScroll,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    waitSeconds: Double = testContext.waitSecondsForAnimationComplete,
    message: String? = null,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement
    val thisObject = this

    val command = "existOnLine"
    val assertMessage = message ?: message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        thisObject.onLine(
            lineHeight = lineHeight,
        ) {
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
                allowScroll = allowScroll,
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
 * existOnColumn
 */
fun VisionElement.existOnColumn(
    expression: String,
    language: String = testContext.visionOCRLanguage,
    looseMatch: Boolean = testContext.visionLooseMatch,
    mergeBoundingBox: Boolean = testContext.visionMergeBoundingBox,
    lineSpacingRatio: Double = testContext.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
    last: Boolean = false,
    columnWidth: Int = this.rect.width * 2,
    allowScroll: Boolean? = CodeExecutionContext.withScroll,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    waitSeconds: Double = testContext.waitSecondsForAnimationComplete,
    message: String? = null,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement
    val thisObject = this

    val command = "existOnColumn"
    val assertMessage = message ?: message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        thisObject.onColumn(
            columnWidth = columnWidth,
        ) {
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
                allowScroll = allowScroll,
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
 * existOnRight
 */
fun VisionElement.existOnRight(
    expression: String,
    language: String = testContext.visionOCRLanguage,
    looseMatch: Boolean = testContext.visionLooseMatch,
    mergeBoundingBox: Boolean = testContext.visionMergeBoundingBox,
    lineSpacingRatio: Double = testContext.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
    last: Boolean = false,
    lineHeight: Int = this.rect.height * 2,
    allowScroll: Boolean? = CodeExecutionContext.withScroll,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    waitSeconds: Double = testContext.waitSecondsForAnimationComplete,
    message: String? = null,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement
    val thisObject = this

    val command = "existOnRight"
    val assertMessage = message ?: message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        thisObject.onRight(
            lineHeight = lineHeight,
        ) {
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
                allowScroll = allowScroll,
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
 * existOnLeft
 */
fun VisionElement.existOnLeft(
    expression: String,
    language: String = testContext.visionOCRLanguage,
    looseMatch: Boolean = testContext.visionLooseMatch,
    mergeBoundingBox: Boolean = testContext.visionMergeBoundingBox,
    lineSpacingRatio: Double = testContext.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
    last: Boolean = false,
    lineHeight: Int = this.rect.height * 2,
    allowScroll: Boolean? = CodeExecutionContext.withScroll,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    waitSeconds: Double = testContext.waitSecondsForAnimationComplete,
    message: String? = null,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement
    val thisObject = this

    val command = "existOnLeft"
    val assertMessage = message ?: message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        thisObject.onLeft(
            lineHeight = lineHeight,
        ) {
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
                allowScroll = allowScroll,
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
 * existOnAbove
 */
fun VisionElement.existOnAbove(
    expression: String,
    language: String = testContext.visionOCRLanguage,
    looseMatch: Boolean = testContext.visionLooseMatch,
    mergeBoundingBox: Boolean = testContext.visionMergeBoundingBox,
    lineSpacingRatio: Double = testContext.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
    last: Boolean = false,
    columnWidth: Int = this.rect.width * 2,
    allowScroll: Boolean? = CodeExecutionContext.withScroll,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    waitSeconds: Double = testContext.waitSecondsForAnimationComplete,
    message: String? = null,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement
    val thisObject = this

    val command = "existOnAbove"
    val assertMessage = message ?: message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        thisObject.onAbove(
            columnWidth = columnWidth,
        ) {
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
                allowScroll = allowScroll,
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
 * existOnBelow
 */
fun VisionElement.existOnBelow(
    expression: String,
    language: String = testContext.visionOCRLanguage,
    looseMatch: Boolean = testContext.visionLooseMatch,
    mergeBoundingBox: Boolean = testContext.visionMergeBoundingBox,
    lineSpacingRatio: Double = testContext.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
    last: Boolean = false,
    columnWidth: Int = this.rect.width * 2,
    allowScroll: Boolean? = CodeExecutionContext.withScroll,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    waitSeconds: Double = testContext.waitSecondsForAnimationComplete,
    message: String? = null,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement
    val thisObject = this

    val command = "existOnBelow"
    val assertMessage = message ?: message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        thisObject.onBelow(
            columnWidth = columnWidth,
        ) {
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
                allowScroll = allowScroll,
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
 * existImageOnLine
 */
fun VisionElement.existImageOnLine(
    label: String,
    threshold: Double = testContext.visionFindImageThreshold,
    lineHeight: Int = this.rect.height * 2,
    verticalOffset: Int = 0,
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

    val command = "existImageOnLine"
    val assertMessage = message ?: message(id = command, subject = label)
    var v = VisionElement.emptyElement
    val thisObject = this

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = label) {

        thisObject.onLine(
            lineHeight = lineHeight,
            verticalOffset = verticalOffset,
        ) {
            v = findImage(
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
        }

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
 * existImageOnColumn
 */
fun VisionElement.existImageOnColumn(
    label: String,
    threshold: Double = testContext.visionFindImageThreshold,
    columnWidth: Int = this.rect.width * 2,
    horizontalOffset: Int = 0,
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

    val command = "existImageOnColumn"
    val assertMessage = message ?: message(id = command, subject = label)
    var v = VisionElement.emptyElement
    val thisObject = this

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = label) {

        thisObject.onColumn(
            columnWidth = columnWidth,
            horizontalOffset = horizontalOffset,
        ) {
            v = findImage(
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
        }

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
 * existImageOnLeft
 */
fun VisionElement.existImageOnLeft(
    label: String,
    threshold: Double = testContext.visionFindImageThreshold,
    lineHeight: Int = this.rect.height * 2,
    verticalOffset: Int = 0,
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

    val command = "existImageOnLeft"
    val assertMessage = message ?: message(id = command, subject = label)
    var v = VisionElement.emptyElement
    val thisObject = this

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = label) {

        thisObject.onLeft(
            lineHeight = lineHeight,
            verticalOffset = verticalOffset,
        ) {
            v = findImage(
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
        }

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
 * existImageOnRight
 */
fun VisionElement.existImageOnRight(
    label: String,
    threshold: Double = testContext.visionFindImageThreshold,
    lineHeight: Int = this.rect.height * 2,
    verticalOffset: Int = 0,
    skinThickness: Int = 2,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = testContext.visionFindImageAspectRatioTolerance,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    waitSeconds: Double = testContext.syncWaitSeconds,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    message: String? = null,
): VisionElement {

    val command = "existImageOnRight"
    val assertMessage = message ?: message(id = command, subject = label)
    var v = VisionElement.emptyElement
    val thisObject = this

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = label) {

        thisObject.onRight(
            lineHeight = lineHeight,
            verticalOffset = verticalOffset,
        ) {
            v = findImage(
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
        }

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
 * existImageOnAbove
 */
fun VisionElement.existImageOnAbove(
    label: String,
    threshold: Double = testContext.visionFindImageThreshold,
    columnWidth: Int = this.rect.width * 2,
    horizontalOffset: Int = 0,
    skinThickness: Int = 2,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = testContext.visionFindImageAspectRatioTolerance,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    waitSeconds: Double = testContext.syncWaitSeconds,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    message: String? = null,
): VisionElement {

    val command = "existImageOnAbove"
    val assertMessage = message ?: message(id = command, subject = label)
    var v = VisionElement.emptyElement
    val thisObject = this

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = label) {

        thisObject.onAbove(
            columnWidth = columnWidth,
            horizontalOffset = horizontalOffset,
        ) {
            v = findImage(
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
        }

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
 * existImageOnBelow
 */
fun VisionElement.existImageOnBelow(
    label: String,
    threshold: Double = testContext.visionFindImageThreshold,
    columnWidth: Int = this.rect.height * 2,
    horizontalOffset: Int = 0,
    skinThickness: Int = 2,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = testContext.visionFindImageAspectRatioTolerance,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    waitSeconds: Double = testContext.syncWaitSeconds,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    message: String? = null,
): VisionElement {

    val command = "existImageOnBelow"
    val assertMessage = message ?: message(id = command, subject = label)
    var v = VisionElement.emptyElement
    val thisObject = this

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = label) {

        thisObject.onBelow(
            columnWidth = columnWidth,
            horizontalOffset = horizontalOffset,
        ) {
            v = findImage(
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
        }

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
 * aboveTextIs
 */
fun VisionElement.aboveTextIs(
    expected: String,
    message: String? = null,
    swipeToSafePosition: Boolean = CodeExecutionContext.withScroll ?: CodeExecutionContext.swipeToSafePosition
): VisionElement {

    val command = "aboveTextIs"
    val assertMessage = message ?: message(id = command, subject = this.subject, expected = expected)
    val v = aboveText(swipeToSafePosition = swipeToSafePosition)
    v.textForComparison.thisContains(expected.forVisionComparison(), message = assertMessage)
    return v
}

/**
 * belowTextIs
 */
fun VisionElement.belowTextIs(
    expected: String,
    message: String? = null,
    swipeToSafePosition: Boolean = CodeExecutionContext.withScroll ?: CodeExecutionContext.swipeToSafePosition
): VisionElement {

    val command = "belowTextIs"
    val assertMessage = message ?: message(id = command, subject = this.subject, expected = expected)
    val v = belowText(swipeToSafePosition = swipeToSafePosition)
    v.textForComparison.thisContains(expected.forVisionComparison(), message = assertMessage)
    return v
}

/**
 * rightTextIs
 */
fun VisionElement.rightTextIs(
    expected: String,
    message: String? = null,
): VisionElement {

    val command = "rightTextIs"
    val assertMessage = message ?: message(id = command, subject = this.subject, expected = expected)

    val sel = Selector(expression = expected)
    var v = VisionElement.emptyElement

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage) {

        v = rightText(1)
        if (expected.isEmpty() && v.text == "") {
            TestLog.ok(message = assertMessage)
            return@execCheckCommand
        }
        if (sel.evaluateText(text = v.text, looseMatch = false)) {
            TestLog.ok(message = assertMessage)
            return@execCheckCommand
        }

        v = rightText()
        val result = sel.evaluateText(text = v.text, looseMatch = false)
        if (result) {
            TestLog.ok(message = assertMessage)
        } else {
            val errorMessage = "$assertMessage (actual=${v.text})"
            TestDriver.lastElement.lastError = TestNGException(errorMessage)
            throw TestDriver.lastElement.lastError!!
        }
    }
    return v
}

/**
 * leftTextIs
 */
fun VisionElement.leftTextIs(
    expected: String,
    message: String? = null,
): VisionElement {

    val command = "leftTextIs"
    val assertMessage = message ?: message(id = command, subject = this.subject, expected = expected)

    val sel = Selector(expression = expected)
    var v = VisionElement.emptyElement

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage) {

        v = leftText(1)
        if (expected.isEmpty() && v.text == "") {
            TestLog.ok(message = assertMessage)
            return@execCheckCommand
        }
        if (sel.evaluateText(text = v.text, looseMatch = false)) {
            TestLog.ok(message = assertMessage)
            return@execCheckCommand
        }

        v = leftText()
        val result = sel.evaluateText(text = v.text, looseMatch = false)
        if (result) {
            TestLog.ok(message = assertMessage)
        } else {
            val errorMessage = "$assertMessage (actual=${v.text})"
            TestDriver.lastElement.lastError = TestNGException(errorMessage)
            throw TestDriver.lastElement.lastError!!
        }
    }
    return v
}
