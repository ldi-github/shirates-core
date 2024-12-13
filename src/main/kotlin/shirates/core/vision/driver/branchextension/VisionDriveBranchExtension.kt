import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestMode
import shirates.core.driver.branchextension.result.BooleanCompareResult
import shirates.core.driver.commandextension.getSelector
import shirates.core.logging.Message.message
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.detect


/**
 * ifCanDetect
 */
fun VisionDrive.ifCanDetect(
    expression: String,
    onTrue: (VisionElement) -> Unit = {}
): VisionDrive {

    val command = "ifCanDetect"

    val v = detect(expression = expression, throwsException = false, waitSeconds = 0.0)
    val matched = v.isFound

    val result = BooleanCompareResult(value = matched, command = command)
    val sel = getSelector(expression = expression)
    val message = message(id = command, subject = sel.toString())

    if (matched || TestMode.isNoLoadRun) {
        val context = TestDriverCommandContext(null)
        context.execBranch(command = command, condition = message) {

            onTrue.invoke(v)
        }
    }

    result.setExecuted(condition = message, matched = matched, message = message)
    return this
}

/**
 * ifCanDetectNot
 */
fun VisionDrive.ifCanDetectNot(
    expression: String,
    onTrue: (VisionElement) -> Unit
): VisionDrive {

    val command = "ifCanDetectNot"

    val v = detect(expression = expression, throwsException = false, waitSeconds = 0.0)
    val matched = v.isEmpty

    val result = BooleanCompareResult(value = matched, command = command)
    val sel = getSelector(expression = expression)
    val message = message(id = command, subject = sel.toString())

    if (matched || TestMode.isNoLoadRun) {

        val context = TestDriverCommandContext(null)
        context.execBranch(command = command, condition = message) {

            onTrue.invoke(v)
        }
    }

    result.setExecuted(condition = message, matched = matched, message = message)
    return this
}

