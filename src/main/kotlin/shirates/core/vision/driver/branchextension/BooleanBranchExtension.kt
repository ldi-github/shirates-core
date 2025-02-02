package shirates.core.vision.driver.branchextension

import shirates.core.driver.TestDriver
import shirates.core.driver.branchextension.result.BooleanCompareResult
import shirates.core.logging.Message.message
import shirates.core.vision.VisionElement

fun Boolean.ifTrue(
    message: String = message(id = "ifTrue"),
    onTrue: (VisionElement) -> Unit
): BooleanCompareResult {

    val command = "ifTrue"
    val value = this
    val result = BooleanCompareResult(value = value, command = command)
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
): BooleanCompareResult {

    val command = "ifFalse"
    val value = this
    val result = BooleanCompareResult(value = value, command = command)
    result.ifFalse(
        message = message,
        onFalse = {
            onFalse(TestDriver.lastVisionElement)
        })

    return result
}
