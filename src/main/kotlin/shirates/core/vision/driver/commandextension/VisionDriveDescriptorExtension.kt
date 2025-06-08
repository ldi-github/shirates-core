package shirates.core.vision.driver.commandextension

import shirates.core.driver.TestDriverCommandContext
import shirates.core.logging.TestLog
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement
import shirates.core.vision.driver.silent

/**
 * output
 */
fun VisionDrive.output(
    message: Any
): VisionElement {

    val testElement = getThisOrIt()

    val context = TestDriverCommandContext(null)
    context.execLogCommand(message = message.toString(), subject = message.toString()) {
        TestLog.output(message = message.toString())
    }

    return testElement
}

/**
 * comment
 */
fun VisionDrive.comment(
    message: String
): VisionElement {

    val testElement = getThisOrIt()

    val context = TestDriverCommandContext(null)
    context.execLogCommand(message = message, subject = message) {
        TestLog.comment(message = message)
    }

    return testElement
}

/**
 * describe
 */
fun VisionDrive.describe(
    message: String
): VisionElement {

    val testElement = getThisOrIt()

    val context = TestDriverCommandContext(null)
    context.execLogCommand(message = message, subject = message) {
        TestLog.describe(message = message)
    }

    return testElement
}

/**
 * caption
 */
fun VisionDrive.caption(
    message: String
): VisionElement {

    val testElement = getThisOrIt()

    val context = TestDriverCommandContext(null)
    context.execLogCommand(message = message, subject = message) {
        TestLog.caption(message = message)
    }

    return testElement
}

/**
 * target
 */
fun VisionDrive.target(
    targetItem: String
): VisionElement {

    val testElement = getThisOrIt()

    val context = TestDriverCommandContext(null)
    context.execLogCommand(message = targetItem, subject = targetItem) {
        TestLog.target(targetName = targetItem)
    }

    return testElement
}

/**
 * manual
 */
fun VisionDrive.manual(
    message: String,
    arg1: String = "",
    arg2: String = ""
): VisionElement {

    val testElement = getThisOrIt()

    val context = TestDriverCommandContext(null)
    context.execLogCommand(message = message, subject = message) {
        TestLog.manual(
            message = message,
            scriptCommand = "manual",
            subject = message,
            arg1 = arg1,
            arg2 = arg2
        )
    }

    return testElement
}

/**
 * procedure
 */
fun VisionDrive.procedure(
    message: String,
    proc: () -> Unit
): VisionElement {

    val testElement = getThisOrIt()

    val context = TestDriverCommandContext(null)
    context.execProcedureCommand(message = message, subject = message) {
        silent {
            proc()
        }
    }

    return testElement
}

/**
 * knownIssue
 */
fun VisionDrive.knownIssue(
    message: String,
    ticketUrl: String
): VisionElement {

    val testElement = getThisOrIt()

    TestLog.knownIssue(
        message = message,
        ticketUrl = ticketUrl
    )
    return testElement
}

/**
 * codeblock
 */
fun VisionDrive.codeblock(
    func: () -> Unit
): VisionElement {

    func()

    return lastElement
}

