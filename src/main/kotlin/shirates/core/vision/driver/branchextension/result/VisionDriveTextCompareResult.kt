package shirates.core.vision.driver.branchextension.result

import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestMode
import shirates.core.driver.branchextension.result.CompareResult
import shirates.core.exception.BranchException
import shirates.core.logging.Message.message
import shirates.core.utility.string.forClassicComparison
import shirates.core.vision.VisionDrive

/**
 * TextCompareResult
 */
class VisionDriveTextCompareResult(val text: String?) : CompareResult(), VisionDrive {

    private fun ifStringCore(
        matched: Boolean,
        command: String,
        message: String,
        onTrue: () -> Unit
    ) {
        if (matched || TestMode.isNoLoadRun) {
            val context = TestDriverCommandContext(null)
            context.execBranch(command = command, condition = message) {
                onTrue.invoke()
            }
        }

        setExecuted(condition = message, matched = matched, message = message)
    }

    /**
     * ifStringIs
     */
    fun ifStringIs(
        value: String?,
        message: String? = null,
        onTrue: () -> Unit
    ): VisionDriveTextCompareResult {

        val command = "ifStringIs"
        val v = value.forClassicComparison()
        val t = text.forClassicComparison()
        val msg = message ?: message(id = command, subject = t, value = v)

        val matched = (v == t)
        ifStringCore(matched = matched, command = command, message = msg, onTrue = onTrue)

        return this
    }

    /**
     * ifStringIsNot
     */
    fun ifStringIsNot(
        value: String?,
        message: String? = null,
        onTrue: () -> Unit
    ): VisionDriveTextCompareResult {

        val command = "ifStringIsNot"
        val v = value.forClassicComparison()
        val t = text.forClassicComparison()
        val msg = message ?: message(id = command, subject = t, value = v)

        val matched = (v != t)
        ifStringCore(matched = matched, command = command, message = msg, onTrue = onTrue)

        return this
    }

    /**
     * ifStartsWith
     */
    fun ifStartsWith(
        value: String?,
        message: String? = null,
        onTrue: () -> Unit
    ): VisionDriveTextCompareResult {

        val command = "ifStartsWith"
        val v = value.forClassicComparison()
        val t = text.forClassicComparison()
        val msg = message ?: message(id = command, subject = t, value = v)

        val matched =
            if (v.isEmpty()) false
            else t.startsWith(v)
        ifStringCore(matched = matched, command = command, message = msg, onTrue = onTrue)

        return this
    }

    /**
     * ifContains
     */
    fun ifContains(
        value: String?,
        message: String? = null,
        onTrue: () -> Unit
    ): VisionDriveTextCompareResult {

        val command = "ifContains"
        val v = value.forClassicComparison()
        val t = text.forClassicComparison()
        val msg = message ?: message(id = command, subject = t, value = v)

        val matched =
            if (v.isEmpty()) false
            else t.contains(v)
        ifStringCore(matched = matched, command = command, message = msg, onTrue = onTrue)

        return this
    }

    /**
     * ifEndsWith
     */
    fun ifEndsWith(
        value: String?,
        message: String? = null,
        onTrue: () -> Unit
    ): VisionDriveTextCompareResult {

        val command = "ifEndsWith"
        val v = value.forClassicComparison()
        val t = text.forClassicComparison()
        val msg = message ?: message(id = command, subject = t, value = v)

        val matched =
            if (v.isEmpty()) false
            else t.endsWith(v)
        ifStringCore(matched = matched, command = command, message = msg, onTrue = onTrue)

        return this
    }

    /**
     * ifMatches
     */
    fun ifMatches(
        regex: String?,
        message: String? = null,
        onTrue: () -> Unit
    ): VisionDriveTextCompareResult {

        val command = "ifMatches"
        val t = text.forClassicComparison()
        val msg = message ?: message(id = command, subject = t, value = regex)

        val matched =
            if (regex.isNullOrEmpty()) false
            else t.matches(regex.toRegex())
        ifStringCore(matched = matched, command = command, message = msg, onTrue = onTrue)

        return this
    }

    /**
     * ifElse
     */
    fun ifElse(
        message: String = message(id = "ifElse"),
        onElse: () -> Unit
    ): VisionDriveTextCompareResult {

        val command = "ifElse"

        if (history.isEmpty() && TestMode.isNoLoadRun.not()) {
            throw BranchException(message(id = "ifElseIsNotPermitted"))
        }

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

    /**
     * elseIfStringIs
     */
    fun elseIfStringIs(
        value: String?,
        message: String? = null,
        onTrue: () -> Unit
    ): VisionDriveTextCompareResult {

        val command = "elseIfStringIs"
        val v = value.forClassicComparison()
        val t = text.forClassicComparison()
        val msg = message ?: message(id = command, subject = t, value = v)

        val matched = (v == t) && anyMatched.not()
        ifStringCore(matched = matched, command = command, message = msg, onTrue = onTrue)

        return this
    }

    /**
     * elseIfStartsWith
     */
    fun elseIfStartsWith(
        value: String?,
        message: String? = null,
        onTrue: () -> Unit
    ): VisionDriveTextCompareResult {

        val command = "elseIfStartsWith"
        val v = value.forClassicComparison()
        val t = text.forClassicComparison()
        val msg = message ?: message(id = command, subject = t, value = v)

        val matched =
            if (v.isEmpty()) false
            else t.startsWith(v) && anyMatched.not()
        ifStringCore(matched = matched, command = command, message = msg, onTrue = onTrue)

        return this
    }

    /**
     * elseIfContains
     */
    fun elseIfContains(
        value: String?,
        message: String? = null,
        onTrue: () -> Unit
    ): VisionDriveTextCompareResult {

        val command = "elseIfContains"
        val v = value.forClassicComparison()
        val t = text.forClassicComparison()
        val msg = message ?: message(id = command, subject = t, value = v)

        val matched =
            if (v.isEmpty()) false
            else t.contains(v) && anyMatched.not()
        ifStringCore(matched = matched, command = command, message = msg, onTrue = onTrue)

        return this
    }

    /**
     * elseIfEndsWith
     */
    fun elseIfEndsWith(
        value: String?,
        message: String? = null,
        onTrue: () -> Unit
    ): VisionDriveTextCompareResult {

        val command = "elseIfEndsWith"
        val v = value.forClassicComparison()
        val t = text.forClassicComparison()
        val msg = message ?: message(id = command, subject = t, value = v)

        val matched =
            if (v.isEmpty()) false
            else t.endsWith(v) && anyMatched.not()
        ifStringCore(matched = matched, command = command, message = msg, onTrue = onTrue)

        return this
    }

    /**
     * elseIfMatches
     */
    fun elseIfMatches(
        regex: String?,
        message: String? = null,
        onTrue: () -> Unit
    ): VisionDriveTextCompareResult {

        val command = "elseIfMatches"
        val t = text.forClassicComparison()
        val msg = message ?: message(id = command, subject = t, value = regex)

        val matched =
            if (regex.isNullOrEmpty()) false
            else t.matches(regex.toRegex()) && anyMatched.not()
        ifStringCore(matched = matched, command = command, message = msg, onTrue = onTrue)

        return this
    }

}


