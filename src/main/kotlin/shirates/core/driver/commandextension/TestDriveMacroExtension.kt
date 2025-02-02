package shirates.core.driver.commandextension

import shirates.core.driver.*
import shirates.core.logging.TestLog
import shirates.core.macro.MacroRepository
import shirates.core.testcode.CodeExecutionContext
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

    lastElement = rootElement
    return lastElement
}
