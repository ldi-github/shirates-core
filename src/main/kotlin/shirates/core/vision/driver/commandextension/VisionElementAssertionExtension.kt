package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
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
 * (textContains)
 */
fun VisionElement.textIs(
    containedText: String,
    joinText: Boolean = false,
    digitOnly: Boolean = false,
    message: String? = null,
): VisionElement {

    visionContext.recognizeText()

    val command = "textIs"

    val assertMessage =
        message ?: message(id = command, subject = subject, expected = containedText, replaceRelative = true)

    val expectedForCompare = containedText.process(digitOnly = digitOnly)
    val actual = if (joinText) {
        if (digitOnly) joinedDigit else this.joinedText
    } else {
        if (digitOnly) digit else text
    }
    val actualForCompare = actual.process(digitOnly = digitOnly)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = containedText) {
        actualForCompare.thisContains(expected = expectedForCompare, message = assertMessage, strict = false)
    }
    return this
}

/**
 * imageLabelIs
 * (image label contains)
 */
fun VisionElement.imageLabelIs(
    containedText: String,
    classifierName: String = "DefaultClassifier",
    waitSeconds: Double = testContext.syncWaitSeconds,
    message: String? = null,
): VisionElement {

    val command = "imageLabelIs"
    val assertMessage =
        message ?: message(id = command, subject = subject, expected = containedText, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {

        this.checkImageLabelContains(
            containedText = containedText,
            message = assertMessage,
            classifierName = classifierName,
            waitSeconds = waitSeconds,
            fullLabel = false
        )
    }
    return this
}

/**
 * imageFullLabelIs
 */
fun VisionElement.imageFullLabelIs(
    label: String,
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
            fullLabel = true
        )
    }
    return this
}

/**
 * checkIsON
 */
fun VisionElement.checkIsON(
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
            waitSeconds = waitSeconds
        )
    }
    return this
}

/**
 * checkIsOFF
 */
fun VisionElement.checkIsOFF(
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
            waitSeconds = waitSeconds
        )
    }
    return this
}

/**
 * buttonStateIs
 */
fun VisionElement.buttonStateIs(
    expectedLabel: String,
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
            fullLabel = false
        )
    }
    return this
}

/**
 * existOnLine
 */
fun VisionElement.existOnLine(
    expression: String,
    language: String = PropertiesManager.visionOCRLanguage,
    verticalMargin: Int = this.rect.height / 2,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    waitSeconds: Double = 0.0,
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
            verticalMargin = verticalMargin,
        ) {
            v = existCore(
                message = assertMessage,
                selector = sel,
                language = language,
                waitSeconds = waitSeconds,
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
    language: String = PropertiesManager.visionOCRLanguage,
    horizontalMargin: Int = this.rect.width / 2,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    waitSeconds: Double = 0.0,
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
            horizontalMargin = horizontalMargin,
        ) {
            v = existCore(
                message = assertMessage,
                selector = sel,
                language = language,
                waitSeconds = waitSeconds,
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
    language: String = PropertiesManager.visionOCRLanguage,
    verticalMargin: Int = this.rect.height / 2,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    waitSeconds: Double = 0.0,
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
            verticalMargin = verticalMargin,
        ) {
            v = existCore(
                message = assertMessage,
                selector = sel,
                language = language,
                waitSeconds = waitSeconds,
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
    language: String = PropertiesManager.visionOCRLanguage,
    verticalMargin: Int = this.rect.height / 2,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    waitSeconds: Double = 0.0,
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
            verticalMargin = verticalMargin,
        ) {
            v = existCore(
                message = assertMessage,
                selector = sel,
                language = language,
                waitSeconds = waitSeconds,
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
    language: String = PropertiesManager.visionOCRLanguage,
    horizontalMargin: Int = this.rect.width / 2,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    waitSeconds: Double = 0.0,
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
            horizontalMargin = horizontalMargin,
        ) {
            v = existCore(
                message = assertMessage,
                selector = sel,
                language = language,
                waitSeconds = waitSeconds,
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
    language: String = PropertiesManager.visionOCRLanguage,
    horizontalMargin: Int = this.rect.width / 2,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    waitSeconds: Double = 0.0,
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
            horizontalMargin = horizontalMargin,
        ) {
            v = existCore(
                message = assertMessage,
                selector = sel,
                language = language,
                waitSeconds = waitSeconds,
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
    threshold: Double? = PropertiesManager.visionFindImageThreshold,
    verticalMargin: Int = this.rect.height / 2,
    verticalOffset: Int = 0,
    skinThickness: Int = 2,
    segmentMarginHorizontal: Int = PropertiesManager.segmentMarginHorizontal,
    segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    waitSeconds: Double = 0.0,
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
            verticalMargin = verticalMargin,
            verticalOffset = verticalOffset,
        ) {
            v = findImage(
                label = label,
                segmentMarginHorizontal = segmentMarginHorizontal,
                segmentMarginVertical = segmentMarginVertical,
                mergeIncluded = mergeIncluded,
                skinThickness = skinThickness,
                waitSeconds = waitSeconds,
                threshold = threshold,
                swipeToSafePosition = swipeToSafePosition
            )
        }

        v.selector = Selector(expression = label)
        lastElement = v

        if (v.isFound.not()) {
            val error = TestNGException(message = "$assertMessage ($v)")
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
    threshold: Double? = PropertiesManager.visionFindImageThreshold,
    horizontalMargin: Int = this.rect.height / 2,
    horizontalOffset: Int = 0,
    skinThickness: Int = 2,
    segmentMarginHorizontal: Int = PropertiesManager.segmentMarginHorizontal,
    segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    waitSeconds: Double = 0.0,
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
            horizontalMargin = horizontalMargin,
            horizontalOffset = horizontalOffset,
        ) {
            v = findImage(
                label = label,
                segmentMarginHorizontal = segmentMarginHorizontal,
                segmentMarginVertical = segmentMarginVertical,
                mergeIncluded = mergeIncluded,
                skinThickness = skinThickness,
                waitSeconds = waitSeconds,
                threshold = threshold,
                swipeToSafePosition = swipeToSafePosition
            )
        }

        v.selector = Selector(expression = label)
        lastElement = v

        if (v.isFound.not()) {
            val error = TestNGException(message = "$assertMessage ($v)")
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
    threshold: Double? = PropertiesManager.visionFindImageThreshold,
    verticalMargin: Int = this.rect.height / 2,
    verticalOffset: Int = 0,
    skinThickness: Int = 2,
    segmentMarginHorizontal: Int = PropertiesManager.segmentMarginHorizontal,
    segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    waitSeconds: Double = 0.0,
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
            verticalMargin = verticalMargin,
            verticalOffset = verticalOffset,
        ) {
            v = findImage(
                label = label,
                segmentMarginHorizontal = segmentMarginHorizontal,
                segmentMarginVertical = segmentMarginVertical,
                mergeIncluded = mergeIncluded,
                skinThickness = skinThickness,
                waitSeconds = waitSeconds,
                threshold = threshold,
                swipeToSafePosition = swipeToSafePosition
            )
        }

        v.selector = Selector(expression = label)
        lastElement = v

        if (v.isFound.not()) {
            val error = TestNGException(message = "$assertMessage ($v)")
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
    threshold: Double? = PropertiesManager.visionFindImageThreshold,
    verticalMargin: Int = this.rect.height / 2,
    verticalOffset: Int = 0,
    skinThickness: Int = 2,
    segmentMarginHorizontal: Int = PropertiesManager.segmentMarginHorizontal,
    segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
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
            verticalMargin = verticalMargin,
            verticalOffset = verticalOffset,
        ) {
            v = findImage(
                label = label,
                segmentMarginHorizontal = segmentMarginHorizontal,
                segmentMarginVertical = segmentMarginVertical,
                mergeIncluded = mergeIncluded,
                skinThickness = skinThickness,
                waitSeconds = waitSeconds,
                threshold = threshold,
                swipeToSafePosition = swipeToSafePosition
            )
        }

        v.selector = Selector(expression = label)
        lastElement = v

        if (v.isFound.not()) {
            val error = TestNGException(message = "$assertMessage ($v)")
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
    threshold: Double? = PropertiesManager.visionFindImageThreshold,
    horizontalMargin: Int = this.rect.height / 2,
    horizontalOffset: Int = 0,
    skinThickness: Int = 2,
    segmentMarginHorizontal: Int = PropertiesManager.segmentMarginHorizontal,
    segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
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
            horizontalMargin = horizontalMargin,
            horizontalOffset = horizontalOffset,
        ) {
            v = findImage(
                label = label,
                segmentMarginHorizontal = segmentMarginHorizontal,
                segmentMarginVertical = segmentMarginVertical,
                mergeIncluded = mergeIncluded,
                skinThickness = skinThickness,
                waitSeconds = waitSeconds,
                threshold = threshold,
                swipeToSafePosition = swipeToSafePosition
            )
        }

        v.selector = Selector(expression = label)
        lastElement = v

        if (v.isFound.not()) {
            val error = TestNGException(message = "$assertMessage ($v)")
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
    threshold: Double? = PropertiesManager.visionFindImageThreshold,
    horizontalMargin: Int = this.rect.height / 2,
    horizontalOffset: Int = 0,
    skinThickness: Int = 2,
    segmentMarginHorizontal: Int = PropertiesManager.segmentMarginHorizontal,
    segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
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
            horizontalMargin = horizontalMargin,
            horizontalOffset = horizontalOffset,
        ) {
            v = findImage(
                label = label,
                segmentMarginHorizontal = segmentMarginHorizontal,
                segmentMarginVertical = segmentMarginVertical,
                mergeIncluded = mergeIncluded,
                skinThickness = skinThickness,
                waitSeconds = waitSeconds,
                threshold = threshold,
                swipeToSafePosition = swipeToSafePosition
            )
        }

        v.selector = Selector(expression = label)
        lastElement = v

        if (v.isFound.not()) {
            val error = TestNGException(message = "$assertMessage ($v)")
            v.lastError = error
            v.lastResult = LogType.NG
            throw error
        }
        TestLog.ok(message = assertMessage)
    }

    return lastElement
}

internal fun VisionElement.textIsCore(
    command: String,
    expected: String,
    joinText: Boolean,
    digitOnly: Boolean,
    assertMessage: String,
): VisionElement {

    val context = TestDriverCommandContext(null)

    var v = this
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
        v = v.textIs(
            containedText = expected,
            joinText = joinText,
            digitOnly = digitOnly,
            message = assertMessage,
        )
    }
    lastElement = v
    return v
}

/**
 * aboveTextIs
 */
fun VisionElement.aboveTextIs(
    expected: String,
    joinText: Boolean = false,
    digitOnly: Boolean = false,
    message: String? = null,
): VisionElement {

    val command = "aboveTextIs"
    val assertMessage = message ?: message(id = command, subject = this.subject, expected = expected)
    val v = aboveText()
    v.textIsCore(
        command = command,
        expected = expected,
        joinText = joinText,
        digitOnly = digitOnly,
        assertMessage = assertMessage,
    )
    return v
}

/**
 * belowTextIs
 */
fun VisionElement.belowTextIs(
    expected: String,
    joinText: Boolean = false,
    digitOnly: Boolean = false,
    message: String? = null,
): VisionElement {

    val command = "belowTextIs"
    val assertMessage = message ?: message(id = command, subject = this.subject, expected = expected)
    val v = belowText()
    v.textIsCore(
        command = command,
        expected = expected,
        joinText = joinText,
        digitOnly = digitOnly,
        assertMessage = assertMessage,
    )
    return v
}

/**
 * rightTextIs
 */
fun VisionElement.rightTextIs(
    expected: String,
    joinText: Boolean = false,
    digitOnly: Boolean = false,
    message: String? = null,
): VisionElement {

    val command = "rightTextIs"
    val assertMessage = message ?: message(id = command, subject = this.subject, expected = expected)
    val v = rightText()
    v.textIsCore(
        command = command,
        expected = expected,
        joinText = joinText,
        digitOnly = digitOnly,
        assertMessage = assertMessage,
    )
    return v
}

/**
 * leftTextIs
 */
fun VisionElement.leftTextIs(
    expected: String,
    joinText: Boolean = false,
    digitOnly: Boolean = false,
    message: String? = null,
): VisionElement {

    val command = "leftTextIs"
    val assertMessage = message ?: message(id = command, subject = this.subject, expected = expected)
    val v = leftText()
    v.textIsCore(
        command = command,
        expected = expected,
        joinText = joinText,
        digitOnly = digitOnly,
        assertMessage = assertMessage,
    )
    return v
}
