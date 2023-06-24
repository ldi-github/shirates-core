package shirates.core.driver.commandextension

import shirates.core.driver.TestDrive
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestElement
import shirates.core.driver.TestMode
import shirates.core.logging.Message.message
import shirates.core.storage.Clipboard
import shirates.core.storage.Memo

/**
 * clearMemo
 */
fun TestDrive.clearMemo(): TestElement {

    val testElement = getThisOrRootElement()

    val command = "clearMemo"
    val message = message(id = command)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(
        command = command,
        message = message,
        subject = testElement.subject
    ) {
        Memo.clear()
    }

    return testElement
}

/**
 * writeMemo
 */
fun TestDrive.writeMemo(key: String, text: String): TestElement {

    val testElement = getThisOrRootElement()

    val command = "writeMemo"
    val message = message(id = command, key = key, value = text)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(
        command = command,
        message = message,
        subject = testElement.subject
    ) {
        Memo.write(key = key, text = text)
    }
    if (TestMode.isNoLoadRun) {
        Memo.write(key = key, text = text)
    }

    return testElement
}

/**
 * readMemo
 */
fun TestDrive.readMemo(key: String): String {

    val testElement = getThisOrRootElement()

    val command = "readMemo"
    val value = Memo.read(key = key)
    val message = message(id = command, key = key, value = value)

    var result = ""
    if (TestMode.isNoLoadRun) {
        result = Memo.read(key = key)
    }
    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(
        command = command,
        message = message,
        subject = testElement.subject
    ) {
        result = Memo.read(key = key)
    }

    return result
}

/**
 * memoTextAs
 */
fun TestDrive.memoTextAs(
    key: String
): TestElement {

    val testElement = getThisOrRootElement()

    val command = "memoTextAs"
    val message = message(id = command, key = key, value = testElement.textOrLabel)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(
        command = command,
        message = message,
        subject = testElement.subject,
        arg1 = testElement.textOrLabel
    ) {
        testElement.textOrLabel.memoTextAs(key)
    }

    return testElement
}


/**
 * clipboardText
 */
fun TestDrive.clipboardText(): TestElement {

    val testElement = getThisOrRootElement()

    val command = "clipboardText"
    val message = message(id = command, subject = testElement.subject, arg1 = testElement.textOrLabel)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(
        command = command,
        message = message,
        subject = testElement.subject,
        arg1 = testElement.textOrLabel
    ) {
        Clipboard.write(testElement.textOrLabel)
    }

    return testElement
}
