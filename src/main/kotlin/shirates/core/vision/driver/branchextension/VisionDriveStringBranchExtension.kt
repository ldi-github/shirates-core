package shirates.core.vision.driver.branchextension

import shirates.core.vision.driver.branchextension.result.VisionDriveTextCompareResult

/**
 * ifStringIs
 */
fun String?.ifStringIs(value: String?, onTrue: () -> Unit): VisionDriveTextCompareResult {

    val result = VisionDriveTextCompareResult(text = this)
    result.ifStringIs(value = value, onTrue = onTrue)

    return result
}

/**
 * ifStringIsNot
 */
fun String?.ifStringIsNot(value: String?, onTrue: () -> Unit): VisionDriveTextCompareResult {

    val result = VisionDriveTextCompareResult(text = this)
    result.ifStringIsNot(value = value, onTrue = onTrue)

    return result
}

/**
 * ifStartsWith
 */
fun String?.ifStartsWith(value: String?, onTrue: () -> Unit): VisionDriveTextCompareResult {

    val result = VisionDriveTextCompareResult(text = this)
    result.ifStartsWith(value = value, onTrue = onTrue)

    return result
}

/**
 * ifContains
 */
fun String?.ifContains(value: String?, onTrue: () -> Unit): VisionDriveTextCompareResult {

    val result = VisionDriveTextCompareResult(text = this)
    result.ifContains(value = value, onTrue = onTrue)

    return result
}

/**
 * ifEndsWith
 */
fun String?.ifEndsWith(value: String?, onTrue: () -> Unit): VisionDriveTextCompareResult {

    val result = VisionDriveTextCompareResult(text = this)
    result.ifEndsWith(value = value, onTrue = onTrue)

    return result
}

/**
 * ifMatches
 */
fun String?.ifMatches(regex: String, onTrue: () -> Unit): VisionDriveTextCompareResult {

    val result = VisionDriveTextCompareResult(text = this)
    result.ifMatches(regex = regex, onTrue = onTrue)

    return result
}

