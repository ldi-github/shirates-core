package shirates.core.vision.driver.commandextension

import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.testContext
import shirates.core.logging.Message.message
import shirates.core.vision.VisionElement

/**
 * checkIsON
 */
fun VisionElement.checkIsON(
    waitSeconds: Double = testContext.waitSecondsOnIsScreen
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
    waitSeconds: Double = testContext.waitSecondsOnIsScreen
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
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
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
