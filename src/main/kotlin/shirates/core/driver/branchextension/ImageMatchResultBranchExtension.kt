package shirates.core.driver.branchextension

import shirates.core.driver.TestDriver
import shirates.core.driver.TestElement
import shirates.core.driver.branchextension.result.BooleanCompareResult
import shirates.core.logging.Message.message
import shirates.core.utility.image.ImageMatchResult

/**
 * ifTrue
 */
fun ImageMatchResult.ifTrue(
    message: String? = null,
    onTrue: (TestElement) -> Unit
): BooleanCompareResult {

    val command = "ImageMatchResult.ifTrue"
    val value = this.result
    val msg = message ?: message(id = command, subject = templateSubject)

    val result = BooleanCompareResult(value = value, command = command)
    result.ifTrue(
        message = msg,
        onTrue = {
            onTrue(TestDriver.lastElement)
        })

    return result
}

/**
 * ifFalse
 */
fun ImageMatchResult.ifFalse(
    message: String? = null,
    onFalse: (TestElement) -> Unit
): BooleanCompareResult {

    val command = "ImageMatchResult.ifFalse"
    val value = this.result
    val msg = message ?: message(id = command, subject = templateSubject)

    val result = BooleanCompareResult(value = value, command = command)
    result.ifFalse(
        message = msg,
        onFalse = {
            onFalse(TestDriver.lastElement)
        })

    return result
}

