package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.commandextension.select
import shirates.core.driver.commandextension.thisIs
import shirates.core.driver.testContext
import shirates.core.driver.testDrive
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Message.message
import shirates.core.utility.string.fullWidth2HalfWidth
import shirates.core.utility.string.preprocessForComparison
import shirates.core.vision.VisionElement

/**
 * textIs
 */
fun VisionElement.textIs(
    expected: String,
    strict: Boolean = PropertiesManager.strictCompareMode,
    remove: String? = null,
    ignoreFullWidthHalfWidth: Boolean = false,
): VisionElement {

    val command = "textIs"

    fun String.process(): String {
        var s = this.preprocessForComparison(strict = strict)
        if (ignoreFullWidthHalfWidth) {
            s = s.fullWidth2HalfWidth()
        }
        return s
    }

    val context = TestDriverCommandContext(null)
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)
    fun strictCompare() {
        val e = testDrive.select(expression = expected, throwsException = false, useCache = false)
        val isIncluded = e.bounds.isIncludedIn(CodeExecutionContext.regionRect.toBoundsWithRatio())
        val actual = if (isIncluded) e.textOrLabelOrValue.process() else ""
        val expectedForCompare = expected.process()
        context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
            actual.thisIs(expected = expectedForCompare, message = assertMessage, strict = strict)
        }
    }

    if (strict) {
        /**
         * strict compare by parameter `strict` (direct access)
         */
        strictCompare()
        return this
    }

    visionContext.recognizeText()

    var actual = text
    if (remove != null) {
        /**
         * remove characters (in AI-OCR miss-recognized character list)
         */
        var removeChars = remove
        if (ignoreFullWidthHalfWidth) {
            removeChars = removeChars.fullWidth2HalfWidth()
        }
        actual = actual.filterNot { it in removeChars }
    }

    val expectedForCompare = expected.process()
    val actualForCompare = actual.process()
    if (actualForCompare == expectedForCompare) {
        /**
         * AI-OCR recognized text matched
         */
        context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
            actualForCompare.thisIs(expected = expectedForCompare, message = assertMessage, strict = strict)
        }
    } else {
        /**
         * strict compare by fallback (direct access)
         * AI-OCR might fail to detect texts or miss recognized text.
         */
        strictCompare()
    }

    return this
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
