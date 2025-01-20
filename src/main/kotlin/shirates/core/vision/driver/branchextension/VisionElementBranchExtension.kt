package shirates.core.vision.driver.branchextension

import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestMode
import shirates.core.driver.branchextension.result.BooleanCompareResult
import shirates.core.logging.Message.message
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.classify

/**
 * ifCheckIsON
 */
fun VisionElement.ifCheckIsON(
    classifierName: String = "CheckStateClassifier",
    onSwitchON: ((VisionElement) -> Unit)
): VisionElement {

    val label = this.classify(classifierName = classifierName)

    if (label == "[ON]") {
        onSwitchON.invoke(this)
    }
    return this
}

/**
 * ifCheckIsOFF
 */
fun VisionElement.ifCheckIsOFF(
    classifierName: String = "CheckStateClassifier",
    onSwitchOFF: ((VisionElement) -> Unit)
): VisionElement {

    val label = this.classify(classifierName = classifierName)

    if (label == "[OFF]") {
        onSwitchOFF.invoke(this)
    }
    return this
}

/**
 * ifImageIs
 */
fun VisionElement.ifImageIs(
    label: String,
    classifierName: String = "DefaultClassifier",
    onTrue: (() -> Unit)
): BooleanCompareResult {

    val command = "ifImageIs"

    val classifyResult = this.classify(classifierName = classifierName)

    val matched = classifyResult == label
    val result = BooleanCompareResult(value = matched, command = command)
    val message = message(id = command, subject = label)

    if (matched || TestMode.isNoLoadRun) {
        val context = TestDriverCommandContext(null)
        context.execBranch(command = command, condition = message) {
            onTrue.invoke()
        }
    }
    result.setExecuted(condition = message, matched = matched, message = message)
    return result
}

/**
 * ifImageIsNot
 */
fun VisionElement.ifImageIsNot(
    label: String,
    classifierName: String = "DefaultClassifier",
    onTrue: (() -> Unit)
): BooleanCompareResult {

    val command = "ifImageIsNot"

    val classifyResult = this.classify(classifierName = classifierName)

    val matched = classifyResult != label
    val result = BooleanCompareResult(value = matched, command = command)
    val message = message(id = command, subject = label)

    if (matched || TestMode.isNoLoadRun) {
        val context = TestDriverCommandContext(null)
        context.execBranch(command = command, condition = message) {
            onTrue.invoke()
        }
    }
    result.setExecuted(condition = message, matched = matched, message = message)
    return result
}
