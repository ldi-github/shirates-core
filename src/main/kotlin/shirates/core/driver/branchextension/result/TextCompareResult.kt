package shirates.core.driver.branchextension.result

/**
 * TextCompareResult
 */
class TextCompareResult(val text: String?) : CompareResult() {

    /**
     * ifStringIs
     */
    fun ifStringIs(value: String?, onTrue: () -> Unit): TextCompareResult {

        val condition = "ifStringIs"

        if (value == null) {
            return this
        }

        val matched = value == text
        if (matched) {
            onTrue.invoke()
        }
        setExecuted(condition = condition, matched = matched)

        return this
    }

    /**
     * ifStartsWith
     */
    fun ifStartsWith(value: String?, onTrue: () -> Unit): TextCompareResult {

        val condition = "ifStartsWith"

        if (value == null) {
            return this
        }
        if (text == null) {
            return this
        }

        val matched = text.startsWith(value)
        if (matched) {
            onTrue.invoke()
        }
        setExecuted(condition = condition, matched = matched)

        return this
    }

    /**
     * ifContains
     */
    fun ifContains(value: String?, onTrue: () -> Unit): TextCompareResult {

        val condition = "ifContains"

        if (value == null) {
            return this
        }
        if (text == null) {
            return this
        }

        val matched = text.contains(value)
        if (matched) {
            onTrue.invoke()
        }
        setExecuted(condition = condition, matched = matched)

        return this
    }

    /**
     * ifEndsWith
     */
    fun ifEndsWith(value: String?, onTrue: () -> Unit): TextCompareResult {

        val condition = "ifEndsWith"

        if (value == null) {
            return this
        }
        if (text == null) {
            return this
        }

        val matched = text.endsWith(value)
        if (matched) {
            onTrue.invoke()
        }
        setExecuted(condition = condition, matched = matched)

        return this
    }

    /**
     * ifMatches
     */
    fun ifMatches(regex: String?, onTrue: () -> Unit): TextCompareResult {

        val condition = "ifMatches"

        if (text == null) {
            return this
        }
        if (regex == null) {
            return this
        }

        val matched = text.matches(Regex(regex))
        if (matched) {
            onTrue.invoke()
        }
        setExecuted(condition = condition, matched = matched)

        return this
    }

    /**
     * ifElse
     */
    fun ifElse(onElse: () -> Unit): TextCompareResult {

        val condition = "ifElse"

        if (anyMatched) {
            return this
        }

        onElse.invoke()
        setExecuted(condition = condition, matched = true)

        return this
    }

    /**
     * elseIfStringIs
     */
    fun elseIfStringIs(value: String?, onTrue: () -> Unit): TextCompareResult {

        if (anyMatched) {
            return this
        }

        return ifStringIs(value = value, onTrue = onTrue)
    }

    /**
     * elseIfStartsWith
     */
    fun elseIfStartsWith(value: String?, onTrue: () -> Unit): TextCompareResult {

        if (anyMatched) {
            return this
        }

        return ifStartsWith(value = value, onTrue = onTrue)
    }

    /**
     * elseIfContains
     */
    fun elseIfContains(value: String?, onTrue: () -> Unit): TextCompareResult {

        if (anyMatched) {
            return this
        }

        return ifContains(value = value, onTrue = onTrue)
    }

    /**
     * elseIfEndsWith
     */
    fun elseIfEndsWith(value: String?, onTrue: () -> Unit): TextCompareResult {

        if (anyMatched) {
            return this
        }

        return ifEndsWith(value = value, onTrue = onTrue)
    }

    /**
     * elseIfMatches
     */
    fun elseIfMatches(regex: String?, onTrue: () -> Unit): TextCompareResult {

        if (anyMatched) {
            return this
        }

        return ifMatches(regex = regex, onTrue = onTrue)
    }

}


