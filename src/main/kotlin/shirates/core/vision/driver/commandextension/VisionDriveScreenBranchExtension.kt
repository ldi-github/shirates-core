package shirates.core.vision.driver.commandextension

import shirates.core.driver.branchextension.result.ScreenCompareResult
import shirates.core.vision.VisionDrive

/**
 * ifScreenIs
 */
fun VisionDrive.ifScreenIs(
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
fun VisionDrive.ifScreenIsNot(
    vararg screenNames: String,
    onTrue: () -> Unit
): ScreenCompareResult {

    val result = ScreenCompareResult()
    result.ifScreenIsNot(screenNames = screenNames, onTrue = onTrue)

    return result
}
