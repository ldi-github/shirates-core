package shirates.core.driver.branchextension

import shirates.core.driver.TestDriver
import shirates.core.driver.TestElement
import shirates.core.driver.branchextension.result.BooleanCompareResult
import shirates.core.utility.image.ImageMatchResult

/**
 * ifTrue
 */
fun ImageMatchResult.ifTrue(onTrue: (TestElement) -> Unit): BooleanCompareResult {

    val command = "ifTrue"
    val value = this.result
    val result = BooleanCompareResult(value = value, command = command)
    result.ifTrue(onTrue = {
        onTrue(TestDriver.lastElement)
    })

    return result
}

/**
 * ifFalse
 */
fun ImageMatchResult.ifFalse(onFalse: (TestElement) -> Unit): BooleanCompareResult {

    val command = "ifFalse"
    val value = this.result
    val result = BooleanCompareResult(value = value, command = command)
    result.ifFalse(onFalse = {
        onFalse(TestDriver.lastElement)
    })

    return result
}

