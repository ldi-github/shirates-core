package shirates.core.vision.driver.commandextension

import shirates.core.driver.ErrorEventArgs
import shirates.core.driver.TestMode
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.TestLog
import shirates.core.macro.MacroRepository
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement
import java.lang.reflect.InvocationTargetException

/**
 * macro
 */
fun VisionDrive.macro(
    macroName: String,
    vararg args: Any,
    message: String = macroName,
    onError: ((e: ErrorEventArgs) -> Unit)? = null
): VisionElement {

    val msg =
        if (args.any()) "$message (${args.joinToString(", ")})"
        else message

    TestLog.operate(message = msg)

    if (TestMode.isNoLoadRun) {
        return lastElement
    }

    try {
        CodeExecutionContext.macroStack.add(macroName)
        MacroRepository.call(macroName = macroName, args = args)
    } catch (t: InvocationTargetException) {
        if (onError == null) {
            throw t.targetException
        }
        val e = ErrorEventArgs(error = t.targetException)
        onError.invoke(e)
        if (e.canceled.not()) {
            throw e.error
        }
    } finally {
        CodeExecutionContext.macroStack.removeLast()
    }

    return lastElement
}