package shirates.core.vision.driver.commandextension

import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestMode
import shirates.core.logging.Message.message
import shirates.core.storage.Clipboard
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement

/**
 * clearClipboard
 */
fun VisionDrive.clearClipboard(): VisionElement {

    val testElement = getThisOrIt()

    val command = "clearClipboard"
    val message = message(id = command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = testElement.subject) {
        Clipboard.write("")
    }
    if (TestMode.isNoLoadRun) {
        Clipboard.write("")
    }

    return testElement
}

/**
 * writeClipboard
 */
fun VisionDrive.writeClipboard(text: String): VisionElement {

    val testElement = getThisOrIt()

    val command = "writeClipboard"
    val message = message(id = command, value = text)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = testElement.subject) {
        Clipboard.write(text)
    }
    if (TestMode.isNoLoadRun) {
        Clipboard.write(text)
    }

    return testElement
}

/**
 * readClipboard
 */
fun VisionDrive.readClipboard(): String {

    val testElement = getThisOrIt()

    val command = "readClipboard"
    val message = message(id = command)

    var result = ""
    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = testElement.subject) {
        result = Clipboard.read()
    }
    if (TestMode.isNoLoadRun) {
        result = Clipboard.read()
    }

    return result
}
