package shirates.core.vision.driver.commandextension

import shirates.core.driver.TestDriverCommandContext
import shirates.core.utility.misc.ShellUtility
import shirates.core.vision.VisionDrive

/**
 * shell
 */
fun VisionDrive.shell(
    vararg args: String,
): ShellUtility.ShellResult {

    var shellResult: ShellUtility.ShellResult? = null
    val testElement = getThisOrIt()

    val command = "shell"
    val message = args.joinToString(" ")

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = testElement.subject) {

        shellResult = ShellUtility.executeCommand(args = args, log = false)
    }

    return shellResult!!
}

/**
 * shellAsync
 */
fun VisionDrive.shellAsync(
    vararg args: String,
): ShellUtility.ShellResult {

    var shellResult: ShellUtility.ShellResult? = null
    val testElement = getThisOrIt()

    val command = "shellAsync"
    val message = args.joinToString(" ")

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = testElement.subject) {

        shellResult = ShellUtility.executeCommandAsync(args = args, log = false)
    }

    return shellResult!!
}