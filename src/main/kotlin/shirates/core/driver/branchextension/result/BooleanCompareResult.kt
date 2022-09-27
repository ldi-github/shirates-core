package shirates.core.driver.branchextension.result

import shirates.core.driver.TestDrive
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
        onTrue: () -> Unit
    ): BooleanCompareResult {

        val condition = "true"

        if (TestMode.isNoLoadRun.not()) {
            if (hasExecuted(condition)) {
                throw BranchException(message(id = "branchConditionAlreadyUsed", subject = condition))
            }
        }

        val matched = value
        if (matched || TestMode.isNoLoadRun) {
            onTrue.invoke()
        }
        setExecuted(condition = condition, matched = matched)

        return this
    }

    /**
     * ifFalse
     */
    fun ifFalse(
        onFalse: () -> Unit
    ): BooleanCompareResult {

        val condition = "false"

        if (TestMode.isNoLoadRun.not()) {
            if (hasExecuted(condition)) {
                throw BranchException(message(id = "branchConditionAlreadyUsed", subject = condition))
            }
        }

        val matched = !value
        if (matched || TestMode.isNoLoadRun) {
            onFalse.invoke()
        }
        setExecuted(condition = condition, matched = matched)

        return this
    }

    /**
     * ifElse
     */
    fun ifElse(
        onElse: () -> Unit
    ): BooleanCompareResult {

        if (TestMode.isNoLoadRun) {
            onElse()
            return this
        }

        if (command == "onScreen") {
            if (history.isEmpty()) {
                onElse.invoke()
                setExecuted(condition = "not", matched = true)
            }
            return this
        }

        if (history.isEmpty()) {
            throw BranchException(message(id = "ifElseIsNotPermitted"))
        }

        if (hasExecuted("true")) {
            return ifFalse(onElse)
        } else {
            return ifTrue(onElse)
        }
    }

    /**
     * not
     */
    fun not(
        onElse: () -> Unit
    ): BooleanCompareResult {

        return ifElse(onElse)
    }
}

