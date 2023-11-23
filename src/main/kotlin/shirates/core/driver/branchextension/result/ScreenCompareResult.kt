package shirates.core.driver.branchextension.result

import shirates.core.driver.TestDriver
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestMode
import shirates.core.logging.Message.message

/**
 * ScreenCompareResult
 */
class ScreenCompareResult() : CompareResult() {

    private fun anyScreenMatched(
        vararg screenNames: String
    ): Boolean {

        for (screenName in screenNames) {
            if (TestDriver.isScreen(screenName)) {
                return true
            }
        }
        return false
    }

    private fun getSubject(
        vararg screenNames: String
    ): String {

        if (screenNames.count() >= 2) {
            return "(${screenNames.joinToString(" or ")})"
        }
        return screenNames[0]
    }

    /**
     * ifScreenIs
     */
    fun ifScreenIs(
        vararg screenNames: String,
        onTrue: () -> Unit
    ): ScreenCompareResult {

        if (screenNames.isEmpty()) {
            throw IllegalArgumentException("screenNames is required.")
        }

        val matched = anyScreenMatched(screenNames = screenNames)
        if (matched.not() && TestMode.isNoLoadRun.not()) {
            return this
        }

        val command = "ifScreenIs"
        val subject = getSubject(screenNames = screenNames)
        val condition = message(id = command, subject = subject)

        val context = TestDriverCommandContext(null)
        context.execBranch(command = command, condition = condition) {

            onTrue.invoke()
            setExecuted(condition = condition, matched = true)
        }

        return this
    }

    /**
     * ifScreenIsNot
     */
    fun ifScreenIsNot(
        vararg screenNames: String,
        onTrue: () -> Unit
    ): ScreenCompareResult {

        if (screenNames.isEmpty()) {
            throw IllegalArgumentException("screenNames is required.")
        }

        val matched = anyScreenMatched(screenNames = screenNames)
        if (matched && TestMode.isNoLoadRun.not()) {
            return this
        }

        val command = "ifScreenIsNot"
        val subject = getSubject(screenNames = screenNames)
        val condition = message(id = "ifScreenIsNot", subject = subject)

        val context = TestDriverCommandContext(null)
        context.execBranch(command = command, condition = condition) {

            onTrue.invoke()
            setExecuted(condition = condition, matched = true)
        }

        return this
    }

    /**
     * ifElse
     */
    fun ifElse(
        onOthers: () -> Unit
    ): ScreenCompareResult {

        if (anyMatched && TestMode.isNoLoadRun.not()) {
            return this
        }

        val command = "ifElse"
        val condition = message(id = "ifElse")

        val context = TestDriverCommandContext(null)
        context.execBranch(command = command, condition = condition) {

            onOthers.invoke()
            setExecuted(condition = condition, matched = true)
        }

        return this
    }

}

