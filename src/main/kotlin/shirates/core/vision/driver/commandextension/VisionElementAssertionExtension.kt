package shirates.core.vision.driver.commandextension

import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.commandextension.thisContains
import shirates.core.driver.testContext
import shirates.core.logging.Message.message
import shirates.core.utility.string.forVisionComparison
import shirates.core.vision.VisionElement

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
): VisionElement {

    visionContext.recognizeText()

    val command = "textIs"

    val assertMessage = message(id = command, subject = subject, expected = containedText, replaceRelative = true)

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
 * checkIsON
 */
fun VisionElement.checkIsON(
    classifierName: String = "CheckStateClassifier",
    waitSeconds: Double = testContext.syncWaitSeconds
): VisionElement {

    val command = "checkIsON"
    val assertMessage = message(id = command, subject = subject, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {
        this.checkIsCore(
            containedText = "ON",
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
    waitSeconds: Double = testContext.syncWaitSeconds
): VisionElement {

    val command = "checkIsOFF"
    val assertMessage = message(id = command, subject = subject, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {
        this.checkIsCore(
            containedText = "OFF",
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
): VisionElement {

    val command = "buttonStateIs"
    val assertMessage = message(id = command, subject = subject, expected = expectedLabel, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {
        this.checkImageLabelContains(
            containedText = expectedLabel,
            message = assertMessage,
            waitSeconds = waitSeconds,
            classifierName = classifierName
        )
    }
    return this
}
