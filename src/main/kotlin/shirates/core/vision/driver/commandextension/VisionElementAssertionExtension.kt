package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.commandextension.*
import shirates.core.driver.testContext
import shirates.core.driver.testDrive
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Message.message
import shirates.core.utility.string.forVisionComparison
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement

/**
 * textIs
 */
fun VisionElement.textIs(
    expected: String,
    strict: Boolean = PropertiesManager.strictCompareMode,
    ignoreCase: Boolean = true,
    ignoreFullWidth: Boolean = true,
    remove: String? = null,
    regionText: Boolean = false,
    digitOnly: Boolean = false,
): VisionElement {

    val command = "textIs"

    fun String.process(): String {
        var s = this.forVisionComparison(
            ignoreCase = ignoreCase,
            ignoreFullWidthHalfWidth = ignoreFullWidth,
            remove = remove,
        )
        if (digitOnly) {
            s = s.replace("[^\\d\\s]".toRegex(), "")
                .replace("[\\s+]".toRegex(), " ")
        }
        return s
    }

    val context = TestDriverCommandContext(null)
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    fun strictCheckInDirectAccessMode() {
        val e = testDrive.select(expression = selector!!.expression!!, throwsException = false, useCache = false)
        val isIncluded = e.bounds.isIncludedIn(CodeExecutionContext.regionRect.toBoundsWithRatio())
        val actual = if (isIncluded) e.textOrLabelOrValue.process() else ""
        val expectedForCompare = expected.process()
        context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
            actual.thisIs(expected = expectedForCompare, message = assertMessage, strict = strict)
        }
    }
    if (strict && selector?.expression != null) {
        /**
         * strict check in direct access mode
         */
        strictCheckInDirectAccessMode()
        return this
    }

    visionContext.recognizeText()

    val expectedForCompare = expected.process()
    val actual = if (regionText) {
        if (digitOnly) regionDigit else this.regionText
    } else {
        if (digitOnly) digitText else text
    }
    val actualForCompare = actual.process()

    if (actualForCompare == expectedForCompare) {
        /**
         * OK
         * AI-OCR recognized text exactly
         */
        context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
            actualForCompare.thisIs(expected = expectedForCompare, message = assertMessage, strict = strict)
        }
    } else {
        /**
         * NG
         * AI-OCR might fail to detect texts or miss recognized text.
         */
        if (selector?.expression != null) {
            /**
             * This is fallback.
             */
            strictCheckInDirectAccessMode()
        } else {
            context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
                actualForCompare.thisIs(expected = expectedForCompare, message = assertMessage, strict = strict)
            }
        }
    }
    return this
}

/**
 * regionTextIs
 */
fun VisionElement.regionTextIs(
    expected: String,
    strict: Boolean = PropertiesManager.strictCompareMode,
    ignoreCase: Boolean = true,
    ignoreFullWidth: Boolean = true,
    remove: String? = null,
    digitOnly: Boolean = false,
): VisionElement {

    return textIs(
        expected = expected,
        strict = strict,
        ignoreCase = ignoreCase,
        ignoreFullWidth = ignoreFullWidth,
        remove = remove,
        digitOnly = digitOnly,
        regionText = true
    )
}

internal fun VisionElement.textIsCore(
    command: String,
    expected: String,
    strict: Boolean = PropertiesManager.strictCompareMode,
    ignoreCase: Boolean = true,
    ignoreFullWidth: Boolean = true,
    remove: String? = null,
    regionText: Boolean = false,
    digitOnly: Boolean = false,
): VisionElement {

    val context = TestDriverCommandContext(null)
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    var v = this
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
        v = v.textIs(
            expected = expected,
            strict = strict,
            ignoreCase = ignoreCase,
            ignoreFullWidth = ignoreFullWidth,
            remove = remove,
            regionText = regionText,
            digitOnly = digitOnly,
        )
    }
    lastElement = v
    return v
}

/**
 * belowTextIs
 */
fun VisionElement.belowTextIs(
    expected: String,
    useCache: Boolean = false,
    strict: Boolean = PropertiesManager.strictCompareMode,
    ignoreCase: Boolean = true,
    ignoreFullWidth: Boolean = true,
    remove: String? = null,
    digitOnly: Boolean = false,
): VisionElement {

    if (useCache) {
        val me = testDrive.select(this.selector.toString())
        val e = me.belowLabel()
        e.textIs(expected = expected, strict = strict)
        val v = e.toVisionElement()
        lastElement = v
        return v
    }

    val command = "belowTextIs"
    val v = belowText()
    v.textIsCore(
        command = command,
        expected = expected,
        strict = strict,
        ignoreCase = ignoreCase,
        ignoreFullWidth = ignoreFullWidth,
        remove = remove,
        digitOnly = digitOnly,
    )
    return v
}

/**
 * aboveTextIs
 */
fun VisionElement.aboveTextIs(
    expected: String,
    useCache: Boolean = false,
    strict: Boolean = PropertiesManager.strictCompareMode,
    ignoreCase: Boolean = true,
    ignoreFullWidth: Boolean = true,
    remove: String? = null,
    digitOnly: Boolean = false,
): VisionElement {

    if (useCache) {
        val me = testDrive.select(this.selector.toString())
        val e = me.aboveLabel()
        e.textIs(expected = expected, strict = strict)
        val v = e.toVisionElement()
        lastElement = v
        return v
    }

    val command = "aboveTextIs"
    val v = aboveText()
    v.textIsCore(
        command = command,
        expected = expected,
        strict = strict,
        ignoreCase = ignoreCase,
        ignoreFullWidth = ignoreFullWidth,
        remove = remove,
        digitOnly = digitOnly,
    )
    return v
}

/**
 * rightTextIs
 */
fun VisionElement.rightTextIs(
    expected: String,
    useCache: Boolean = false,
    strict: Boolean = PropertiesManager.strictCompareMode,
    ignoreCase: Boolean = true,
    ignoreFullWidth: Boolean = true,
    remove: String? = null,
    digitOnly: Boolean = false,
): VisionElement {

    if (useCache) {
        val me = testDrive.select(this.selector.toString())
        val e = me.rightLabel()
        e.textIs(expected = expected, strict = strict)
        val v = e.toVisionElement()
        lastElement = v
        return v
    }

    val command = "rightTextIs"
    val v = rightText()
    v.textIsCore(
        command = command,
        expected = expected,
        strict = strict,
        ignoreCase = ignoreCase,
        ignoreFullWidth = ignoreFullWidth,
        remove = remove,
        digitOnly = digitOnly,
    )
    return v
}

/**
 * leftTextIs
 */
fun VisionElement.leftTextIs(
    expected: String,
    useCache: Boolean = false,
    strict: Boolean = PropertiesManager.strictCompareMode,
    ignoreCase: Boolean = true,
    ignoreFullWidth: Boolean = true,
    remove: String? = null,
    digitOnly: Boolean = false,
): VisionElement {

    if (useCache) {
        val me = testDrive.select(this.selector.toString())
        val e = me.leftLabel()
        e.textIs(expected = expected, strict = strict)
        val v = e.toVisionElement()
        lastElement = v
        return v
    }

    val command = "leftTextIs"
    val v = leftText()
    v.textIsCore(
        command = command,
        expected = expected,
        strict = strict,
        ignoreCase = ignoreCase,
        ignoreFullWidth = ignoreFullWidth,
        remove = remove,
        digitOnly = digitOnly,
    )
    return v
}

/**
 * checkIsON
 */
fun VisionElement.checkIsON(
    waitSeconds: Double = testContext.syncWaitSeconds
): VisionElement {

    val command = "checkIsON"
    val assertMessage = message(id = command, subject = subject, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {
        this.checkSwitchState(
            containedText = "ON",
            message = assertMessage,
            waitSeconds = waitSeconds
        )
    }

    return this
}

/**
 * checkIsOFF
 */
fun VisionElement.checkIsOFF(
    waitSeconds: Double = testContext.syncWaitSeconds
): VisionElement {

    val command = "checkIsOFF"
    val assertMessage = message(id = command, subject = subject, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {
        this.checkSwitchState(
            containedText = "OFF",
            message = assertMessage,
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
    mlmodelFile: String = "vision/mlmodels/basic/ButtonStateClassifier/ButtonStateClassifier.mlmodel"
): VisionElement {

    val command = "buttonStateIs"
    val assertMessage = message(id = command, subject = subject, expected = expectedLabel, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {
        this.checkImageLabelContains(
            containedText = expectedLabel,
            message = assertMessage,
            waitSeconds = waitSeconds,
            mlmodelFile = mlmodelFile
        )
    }

    return this
}
