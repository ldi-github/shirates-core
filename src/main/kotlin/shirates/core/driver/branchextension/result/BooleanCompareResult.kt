package shirates.core.driver.branchextension.result

import shirates.core.driver.TestDrive
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestMode
import shirates.core.exception.BranchException
import shirates.core.logging.Message.message

/**
 * BooleanCompareResult
 */
class BooleanCompareResult(val value: Boolean, val command: String) : CompareResult(), TestDrive {

    private fun ifCore(
        condition: String,
        matched: Boolean,
        message: String,
        func: () -> Unit
    ) {
        if (hasExecuted(condition) && TestMode.isNoLoadRun.not()) {
            throw BranchException(message(id = "branchConditionAlreadyUsed", subject = condition))
        }

        if (matched || TestMode.isNoLoadRun) {
            val context = TestDriverCommandContext(null)
            context.execBranch(command = command, condition = message) {
                func.invoke()
            }
        }

        setExecuted(condition = condition, matched = matched, message = message)
    }

    /**
     * ifTrue
     */
    fun ifTrue(
        message: String = message(id = command),
        onTrue: () -> Unit
    ): BooleanCompareResult {

        val condition = "true"
        val matched = value

        ifCore(condition = condition, matched = matched, message = message, func = onTrue)

        return this
    }

    /**
     * ifFalse
     */
    fun ifFalse(
        message: String = message(id = command),
        onFalse: () -> Unit
    ): BooleanCompareResult {

        val condition = "false"
        val matched = !value

        ifCore(condition = condition, matched = matched, message = message, func = onFalse)

        return this
    }

    /**
     * ifElse
     */
    fun ifElse(
        message: String = message(id = "ifElse"),
        onElse: () -> Unit
    ): BooleanCompareResult {

        if (history.isEmpty() && TestMode.isNoLoadRun.not()) {
            throw BranchException(message(id = "ifElseIsNotPermitted"))
        }

        val condition = "else"
        val matched = history.any() { it.matched }.not()

        if (hasExecuted(condition) && TestMode.isNoLoadRun.not()) {
            throw BranchException(message(id = "branchConditionAlreadyUsed", subject = condition))
        }

        val msg =
            if (history.any() && TestMode.isNoLoadRun.not())
                history.map { it.message }.joinToString("\n") + "\n$message"
            else message
        if (matched || TestMode.isNoLoadRun) {
            val context = TestDriverCommandContext(null)
            context.execBranch(command = command, condition = msg) {
                onElse.invoke()
            }
        }

        setExecuted(condition = condition, matched = matched, message = msg)

        return this
    }

}

