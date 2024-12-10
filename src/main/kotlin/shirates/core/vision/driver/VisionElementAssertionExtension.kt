package shirates.core.vision.driver

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
        this.checkImageLabelContains(
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
        this.checkImageLabelContains(
            containedText = "OFF",
            message = assertMessage,
            waitSeconds = waitSeconds
        )
    }

    return this
}


