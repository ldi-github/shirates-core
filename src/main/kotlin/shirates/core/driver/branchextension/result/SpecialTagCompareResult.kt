package shirates.core.driver.branchextension.result

import shirates.core.driver.TestDrive
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestMode
import shirates.core.driver.testContext
import shirates.core.exception.BranchException
import shirates.core.logging.Message.message

/**
 * SpecialTagCompareResult
 */
class SpecialTagCompareResult() : CompareResult(), TestDrive {

    /**
     * specialTag
     */
    fun specialTag(
        specialTag: String,
        onTrue: () -> Unit
    ): SpecialTagCompareResult {

        val matched = testContext.profile.hasSpecialTag(specialTag)
        if (matched || TestMode.isNoLoadRun) {
            val context = TestDriverCommandContext(null)
            context.execSpecial(subject = "special", condition = specialTag) {
                onTrue.invoke()
            }
        }
        setExecuted(condition = specialTag, matched = matched, message = specialTag)

        return this
    }

    /**
     * ifElse
     */
    fun ifElse(
        message: String = message(id = "ifElse"),
        onElse: () -> Unit
    ): SpecialTagCompareResult {

        if (history.isEmpty() && TestMode.isNoLoadRun.not()) {
            throw BranchException(message(id = "ifElseIsNotPermitted"))
        }

        val command = "ifElse"
        val condition = "else"

        if (hasExecuted(condition) && TestMode.isNoLoadRun.not()) {
            throw BranchException(message(id = "branchConditionAlreadyUsed", subject = condition))
        }

        val msg =
            if (history.any() && TestMode.isNoLoadRun.not())
                history.map { it.message }.joinToString("\n") + "\n$message"
            else message
        val matched = history.any() { it.matched }.not()
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

