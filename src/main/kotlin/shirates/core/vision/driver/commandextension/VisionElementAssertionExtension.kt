package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.commandextension.select
import shirates.core.driver.commandextension.thisIs
import shirates.core.driver.testContext
import shirates.core.driver.testDrive
import shirates.core.logging.Message.message
import shirates.core.testcode.preprocessForComparison
import shirates.core.vision.VisionElement

/**
 * textIs
 */
fun VisionElement.textIs(
    expected: String,
    strict: Boolean = PropertiesManager.strictCompareMode
): VisionElement {

    val command = "textIs"

    if (text.isBlank()) {
        recognizeText()
    }
    var actual = text
    if (actual.preprocessForComparison(strict = strict) != expected.preprocessForComparison(strict = strict)) {
        val e = testDrive.select(expression = expected, throwsException = false, useCache = false)
        if (e.textOrLabel.isNotBlank()) {
            actual = e.textOrLabel
        }
    }

    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
        actual.thisIs(expected = expected, message = assertMessage, strict = strict)
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
