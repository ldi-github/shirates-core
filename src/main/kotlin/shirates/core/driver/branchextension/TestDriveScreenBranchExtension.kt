package shirates.core.driver.branchextension

import shirates.core.driver.TestDrive
import shirates.core.driver.branchextension.result.ScreenCompareResult

/**
 * ifScreenIs
 */
fun TestDrive.ifScreenIs(
    screenName: String,
    onTrue: () -> Unit
): ScreenCompareResult {

    val result = ScreenCompareResult()
    result.ifScreenIs(screenName = screenName, onTrue = onTrue)

    return result
}

/**
 * ifScreenIsNot
 */
fun TestDrive.ifScreenIsNot(
    screenName: String,
    onTrue: () -> Unit
): ScreenCompareResult {

    val result = ScreenCompareResult()
    result.ifScreenIsNot(screenName = screenName, onTrue = onTrue)

    return result
}
