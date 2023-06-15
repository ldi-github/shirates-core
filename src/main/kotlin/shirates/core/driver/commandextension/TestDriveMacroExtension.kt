package shirates.core.driver.commandextension

import shirates.core.driver.*
import shirates.core.logging.CodeExecutionContext
import shirates.core.macro.MacroRepository
import java.lang.reflect.InvocationTargetException

/**
 * macro
 */
fun TestDrive.macro(
    macroName: String,
    vararg args: Any,
    message: String = macroName,
    onError: ((e: ErrorEventArgs) -> Unit)? = null
): TestElement {

    val testElement = TestDriver.it
    val command = "macro"

    val msg =
        if (args.any()) "$message (${args.joinToString(", ")})"
        else message

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = msg, suppressBeforeScreenshot = true) {
        val original = CodeExecutionContext.isInMacro
        try {
            CodeExecutionContext.isInMacro = true
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
            CodeExecutionContext.isInMacro = original
        }
    }

    return lastElement
}
