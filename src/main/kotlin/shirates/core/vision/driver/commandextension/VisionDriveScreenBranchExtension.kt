package shirates.core.vision.driver.commandextension

import shirates.core.vision.VisionDrive
import shirates.core.vision.driver.branchextension.result.VisionDriveScreenCompareResult

/**
 * ifScreenIs
 */
fun VisionDrive.ifScreenIs(
    screenName: String,
    onTrue: () -> Unit
): VisionDriveScreenCompareResult {

    val result = VisionDriveScreenCompareResult()
    result.ifScreenIs(screenName = screenName, onTrue = onTrue)

    return result
}

/**
 * ifScreenIsOf
 */
fun VisionDrive.ifScreenIsOf(
    vararg screenNames: String,
    onTrue: () -> Unit
): VisionDriveScreenCompareResult {

    val result = VisionDriveScreenCompareResult()
    result.ifScreenIsOf(screenNames = screenNames, onTrue = onTrue)

    return result
}

/**
 * ifScreenIsNot
 */
fun VisionDrive.ifScreenIsNot(
    screenName: String,
    onTrue: () -> Unit
): VisionDriveScreenCompareResult {

    val result = VisionDriveScreenCompareResult()
    result.ifScreenIsNot(screenName = screenName, onTrue = onTrue)

    return result
}
