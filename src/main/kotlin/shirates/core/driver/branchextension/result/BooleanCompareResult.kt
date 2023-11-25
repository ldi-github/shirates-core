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

    /**
     * ifTrue
     */
    fun ifTrue(
        message: String = message(id = command),
        onTrue: () -> Unit
    ): BooleanCompareResult {

        val condition = "true"

        if (hasExecuted(condition) && TestMode.isNoLoadRun.not()) {
            throw BranchException(message(id = "branchConditionAlreadyUsed", subject = condition))
        }

        val matched = value
        if (matched || TestMode.isNoLoadRun) {
            val context = TestDriverCommandContext(null)
            context.execBranch(command = command, condition = message) {
                onTrue.invoke()
            }
        }

        setExecuted(condition = condition, matched = matched)

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

        if (hasExecuted(condition) && TestMode.isNoLoadRun.not()) {
            throw BranchException(message(id = "branchConditionAlreadyUsed", subject = condition))
        }

        val matched = !value
        if (matched || TestMode.isNoLoadRun) {
            val context = TestDriverCommandContext(null)
            context.execBranch(command = command, condition = message) {
                onFalse.invoke()
            }
        }

        setExecuted(condition = condition, matched = matched)

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

        if (hasExecuted(condition) && TestMode.isNoLoadRun.not()) {
            throw BranchException(message(id = "branchConditionAlreadyUsed", subject = condition))
        }

        val matched = history.any() { it.matched }.not()
        if (matched || TestMode.isNoLoadRun) {
            val context = TestDriverCommandContext(null)
            context.execBranch(command = command, condition = message) {
                onElse.invoke()
            }
        }

        setExecuted(condition = condition, matched = matched)

        return this
    }

}

