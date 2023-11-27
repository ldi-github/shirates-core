package shirates.core.driver.branchextension

import shirates.core.driver.TestDriver
import shirates.core.driver.TestElement
import shirates.core.driver.branchextension.result.BooleanCompareResult
import shirates.core.logging.Message.message

/**
 * ifTrue
 */
fun Boolean.ifTrue(
    message: String = message(id = "ifTrue"),
    onTrue: (TestElement) -> Unit
): BooleanCompareResult {

    val command = "ifTrue"
    val value = this
    val result = BooleanCompareResult(value = value, command = command)
    result.ifTrue(
        message = message,
        onTrue = {
            onTrue(TestDriver.lastElement)
        })

    return result
}

/**
 * ifFalse
 */
fun Boolean.ifFalse(
    message: String = message(id = "ifFalse"),
    onFalse: (TestElement) -> Unit
): BooleanCompareResult {

    val command = "ifFalse"
    val value = this
    val result = BooleanCompareResult(value = value, command = command)
    result.ifFalse(
        message = message,
        onFalse = {
            onFalse(TestDriver.lastElement)
        })

    return result
}

