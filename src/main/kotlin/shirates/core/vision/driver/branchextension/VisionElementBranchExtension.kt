package shirates.core.vision.driver.branchextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestMode
import shirates.core.logging.Message.message
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.branchextension.result.VisionDriveBooleanCompareResult
import shirates.core.vision.driver.classify

/**
 * ifCheckIsON
 */
fun VisionElement.ifCheckIsON(
    threshold: Double = PropertiesManager.visionFindImageThreshold,
    classifierName: String = "CheckStateClassifier",
    onSwitchON: ((VisionElement) -> Unit)
): VisionElement {

    if (TestMode.isNoLoadRun) {
        onSwitchON.invoke(this)
        return this
    }

    val label = this.classify(classifierName = classifierName, threshold = threshold)

    if (label == "[ON]") {
        onSwitchON.invoke(this)
    }
    return this
}

/**
 * ifCheckIsOFF
 */
fun VisionElement.ifCheckIsOFF(
    threshold: Double = PropertiesManager.visionFindImageThreshold,
    classifierName: String = "CheckStateClassifier",
    onSwitchOFF: ((VisionElement) -> Unit)
): VisionElement {

    if (TestMode.isNoLoadRun) {
        onSwitchOFF.invoke(this)
        return this
    }

    val label = this.classify(classifierName = classifierName, threshold = threshold)

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
    threshold: Double = PropertiesManager.visionFindImageThreshold,
    classifierName: String = "DefaultClassifier",
    onTrue: (() -> Unit)
): VisionDriveBooleanCompareResult {

    val command = "ifImageIs"

    if (TestMode.isNoLoadRun) {
        onTrue.invoke()
        return VisionDriveBooleanCompareResult(value = true, command = command)
    }

    val classifyResult = this.classify(classifierName = classifierName, threshold = threshold)

    val matched = classifyResult == label
    val result = VisionDriveBooleanCompareResult(value = matched, command = command)
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
    threshold: Double = PropertiesManager.visionFindImageThreshold,
    classifierName: String = "DefaultClassifier",
    onTrue: (() -> Unit)
): VisionDriveBooleanCompareResult {

    val command = "ifImageIsNot"

    if (TestMode.isNoLoadRun) {
        onTrue.invoke()
        return VisionDriveBooleanCompareResult(value = true, command = command)
    }

    val classifyResult = this.classify(classifierName = classifierName, threshold = threshold)

    val matched = classifyResult != label
    val result = VisionDriveBooleanCompareResult(value = matched, command = command)
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
