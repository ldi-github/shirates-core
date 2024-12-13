package shirates.core.vision.driver.commandextension

import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.memoTextAs
import shirates.core.logging.Message.message
import shirates.core.storage.Clipboard
import shirates.core.storage.Memo
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.getThisOrIt

/**
 * clearMemo
 */
fun VisionDrive.clearMemo(): VisionElement {

    val v = getThisOrIt()

    val command = "clearMemo"
    val message = message(id = command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(
        command = command,
        message = message,
        subject = v.subject
    ) {
        Memo.clear()
    }

    return v
}

/**
 * writeMemo
 */
fun VisionDrive.writeMemo(key: String, text: String): VisionElement {

    val v = getThisOrIt()

    val command = "writeMemo"
    val message = message(id = command, key = key, value = text)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(
        command = command,
        message = message,
        subject = v.subject
    ) {
        Memo.write(key = key, text = text)
    }
    if (TestMode.isNoLoadRun) {
        Memo.write(key = key, text = text)
    }

    return v
}

/**
 * readMemo
 */
fun VisionDrive.readMemo(key: String): String {

    val v = getThisOrIt()

    val command = "readMemo"
    val value = Memo.read(key = key)
    val message = message(id = command, key = key, value = value)

    var result = ""
    if (TestMode.isNoLoadRun) {
        result = Memo.read(key = key)
    }
    val context = TestDriverCommandContext(null)
    context.execOperateCommand(
        command = command,
        message = message,
        subject = v.subject
    ) {
        result = Memo.read(key = key)
    }

    return result
}

/**
 * memoTextAs
 */
fun VisionDrive.memoTextAs(
    key: String
): VisionElement {

    val v = getThisOrIt()

    val command = "memoTextAs"
    val message = message(id = command, key = key, value = v.text)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(
        command = command,
        message = message,
        subject = v.subject,
        arg1 = v.text
    ) {
        v.text.memoTextAs(key)
    }

    return v
}


/**
 * clipboardText
 */
fun VisionDrive.clipboardText(): VisionElement {

    val v = getThisOrIt()

    val command = "clipboardText"
    val message = message(id = command, subject = v.subject, arg1 = v.text)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(
        command = command,
        message = message,
        subject = v.subject,
        arg1 = v.text
    ) {
        Clipboard.write(v.text)
    }

    return v
}
