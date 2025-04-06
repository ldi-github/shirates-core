package shirates.core.vision.driver.commandextension

import shirates.core.driver.branchextension.result.ScreenCompareResult
import shirates.core.vision.VisionDrive

/**
 * ifScreenIs
 */
fun VisionDrive.ifScreenIs(
    screenName: String,
    onTrue: () -> Unit
): ScreenCompareResult {

    val result = ScreenCompareResult()
    result.ifScreenIs(screenName = screenName, onTrue = onTrue)

    return result
}

/**
 * ifScreenIsOf
 */
fun VisionDrive.ifScreenIsOf(
    vararg screenNames: String,
    onTrue: () -> Unit
): ScreenCompareResult {

    val result = ScreenCompareResult()
    result.ifScreenIsOf(screenNames = screenNames, onTrue = onTrue)

    return result
}

/**
 * ifScreenIsNot
 */
fun VisionDrive.ifScreenIsNot(
    screenName: String,
    onTrue: () -> Unit
): ScreenCompareResult {

    val result = ScreenCompareResult()
    result.ifScreenIsNot(screenName = screenName, onTrue = onTrue)

    return result
}
