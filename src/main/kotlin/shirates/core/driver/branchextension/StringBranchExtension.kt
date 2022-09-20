package shirates.core.driver.branchextension

import shirates.core.driver.branchextension.result.TextCompareResult

/**
 * ifStringIs
 */
fun String?.ifStringIs(value: String?, onTrue: () -> Unit): TextCompareResult {

    val result = TextCompareResult(text = this)
    result.ifStringIs(value = value, onTrue = onTrue)

    return result
}

/**
 * ifStartsWith
 */
fun String?.ifStartsWith(value: String?, onTrue: () -> Unit): TextCompareResult {

    val result = TextCompareResult(text = this)
    result.ifStartsWith(value = value, onTrue = onTrue)

    return result
}

/**
 * ifContains
 */
fun String?.ifContains(value: String?, onTrue: () -> Unit): TextCompareResult {

    val result = TextCompareResult(text = this)
    result.ifContains(value = value, onTrue = onTrue)

    return result
}

/**
 * ifEndsWith
 */
fun String?.ifEndsWith(value: String?, onTrue: () -> Unit): TextCompareResult {

    val result = TextCompareResult(text = this)
    result.ifEndsWith(value = value, onTrue = onTrue)

    return result
}

/**
 * ifMatches
 */
fun String?.ifMatches(regex: String, onTrue: () -> Unit): TextCompareResult {

    val result = TextCompareResult(text = this)
    result.ifMatches(regex = regex, onTrue = onTrue)

    return result
}

