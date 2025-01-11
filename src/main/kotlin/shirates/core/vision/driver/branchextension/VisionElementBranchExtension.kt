package shirates.core.vision.driver.branchextension

import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestMode
import shirates.core.driver.branchextension.result.BooleanCompareResult
import shirates.core.logging.Message.message
import shirates.core.utility.toPath
import shirates.core.vision.VisionElement
import shirates.core.vision.configration.repository.VisionMLModelRepository
import shirates.core.vision.driver.classify

/**
 * ifCheckIsON
 */
fun VisionElement.ifCheckIsON(
    mlmodelFile: String? = null,
    onSwitchON: ((VisionElement) -> Unit)
): VisionElement {

    val rep = VisionMLModelRepository.getRepository(classifierName = "CheckStateClassifier")
    val file = mlmodelFile ?: rep.imageClassifierDirectory.toPath().resolve("${rep.classifierName}.mlmodel").toString()

    val label = this.classify(mlmodelFile = file)

    if (label == "ON") {
        onSwitchON.invoke(this)
    }
    return this
}

/**
 * ifCheckIsOFF
 */
fun VisionElement.ifCheckIsOFF(
    mlmodelFile: String? = null,
    onSwitchOFF: ((VisionElement) -> Unit)
): VisionElement {

    val rep = VisionMLModelRepository.getRepository(classifierName = "CheckStateClassifier")
    val file = mlmodelFile ?: rep.imageClassifierDirectory.toPath().resolve("${rep.classifierName}.mlmodel").toString()

    val label = this.classify(mlmodelFile = file)

    if (label == "OFF") {
        onSwitchOFF.invoke(this)
    }
    return this
}

/**
 * ifImageIs
 */
fun VisionElement.ifImageIs(
    label: String,
    onTrue: (() -> Unit)
): BooleanCompareResult {

    val command = "ifImageIs"

    val classifyResult = this.classify()

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
    onTrue: (() -> Unit)
): BooleanCompareResult {

    val command = "ifImageIsNot"

    val classifyResult = this.classify()

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
