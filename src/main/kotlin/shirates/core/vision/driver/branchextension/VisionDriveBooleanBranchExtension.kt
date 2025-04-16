package shirates.core.vision.driver.branchextension

import shirates.core.driver.TestDriver
import shirates.core.logging.Message.message
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.branchextension.result.VisionDriveBooleanCompareResult

fun Boolean.ifTrue(
    message: String = message(id = "ifTrue"),
    onTrue: (VisionElement) -> Unit
): VisionDriveBooleanCompareResult {

    val command = "ifTrue"
    val value = this
    val result = VisionDriveBooleanCompareResult(value = value, command = command)
    result.ifTrue(
        message = message,
        onTrue = {
            onTrue(TestDriver.lastVisionElement)
        })

    return result
}

/**
 * ifFalse
 */
fun Boolean.ifFalse(
    message: String = message(id = "ifFalse"),
    onFalse: (VisionElement) -> Unit
): VisionDriveBooleanCompareResult {

    val command = "ifFalse"
    val value = this
    val result = VisionDriveBooleanCompareResult(value = value, command = command)
    result.ifFalse(
        message = message,
        onFalse = {
            onFalse(TestDriver.lastVisionElement)
        })

    return result
}
