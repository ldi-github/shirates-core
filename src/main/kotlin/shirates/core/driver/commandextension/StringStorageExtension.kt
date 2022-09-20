package shirates.core.driver.commandextension

import shirates.core.driver.TestDriverCommandContext
import shirates.core.logging.Message.message
import shirates.core.storage.Clipboard
import shirates.core.storage.Memo

/**
 * memoAs
 */
fun String.memoTextAs(key: String): String {

    val value = this
    val command = "memoTextAs"
    val message = message(id = command, key = key, value = value)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, fireEvent = false) {
        Memo.write(key = key, text = value)
    }

    return this
}

/**
 * writeToClipboard
 */
fun String.writeToClipboard(): String {

    val value = this
    val command = "writeToClipboard"
    val message = message(id = command, subject = value)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, fireEvent = false) {
        Clipboard.write(text = value)
    }

    return this
}