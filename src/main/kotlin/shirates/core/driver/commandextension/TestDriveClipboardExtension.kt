package shirates.core.driver.function

import shirates.core.driver.TestDrive
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestElement
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.getTestElement
import shirates.core.logging.Message.message
import shirates.core.storage.Clipboard

/**
 * clearClipboard
 */
fun TestDrive.clearClipboard(): TestElement {

    val testElement = getTestElement()

    val command = "clearClipboard"
    val message = message(id = command)

    val context = TestDriverCommandContext(testElement)
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
fun TestDrive.writeClipboard(text: String): TestElement {

    val testElement = getTestElement()

    val command = "writeClipboard"
    val message = message(id = command, value = text)

    val context = TestDriverCommandContext(testElement)
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
fun TestDrive.readClipboard(): String {

    val testElement = getTestElement()

    val command = "readClipboard"
    val message = message(id = command)

    var result = ""
    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message, subject = testElement.subject) {
        result = Clipboard.read()
    }
    if (TestMode.isNoLoadRun) {
        result = Clipboard.read()
    }

    return result
}
