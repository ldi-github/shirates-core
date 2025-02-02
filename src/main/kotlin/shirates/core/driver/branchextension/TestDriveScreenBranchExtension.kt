package shirates.core.driver.branchextension

import shirates.core.driver.TestDrive
import shirates.core.driver.branchextension.result.ScreenCompareResult

/**
 * ifScreenIs
 */
fun TestDrive.ifScreenIs(
    vararg screenNames: String,
    onTrue: () -> Unit
): ScreenCompareResult {

    val result = ScreenCompareResult()
    result.ifScreenIs(screenNames = screenNames, onTrue = onTrue)

    return result
}

/**
 * ifScreenIsNot
 */
fun TestDrive.ifScreenIsNot(
    vararg screenNames: String,
    onTrue: () -> Unit
): ScreenCompareResult {

    val result = ScreenCompareResult()
    result.ifScreenIsNot(screenNames = screenNames, onTrue = onTrue)

    return result
}
