import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestElement
import shirates.core.driver.TestMode
import shirates.core.driver.branchextension.ifFalse
import shirates.core.driver.branchextension.ifTrue
import shirates.core.driver.branchextension.result.BooleanCompareResult
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.testContext
import shirates.core.logging.Message.message
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.commandextension.detect
import shirates.core.vision.driver.commandextension.findImage

/**
 * ifImageExist
 */
fun VisionDrive.ifImageExist(
    label: String,
    segmentMarginHorizontal: Int = PropertiesManager.segmentMarginHorizontal,
    segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    skinThickness: Int = 2,
    waitSeconds: Double = testContext.syncWaitSeconds,
    distance: Double? = 0.1,
    onTrue: (() -> Unit)
): BooleanCompareResult {

    val command = "ifImageExist"

    val v = findImage(
        label = label,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        mergeIncluded = mergeIncluded,
        skinThickness = skinThickness,
        waitSeconds = waitSeconds,
        threshold = distance,
    )
    val matched = v.isFound
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
 * ifImageExistNot
 */
fun VisionDrive.ifImageExistNot(
    label: String,
    segmentMarginHorizontal: Int = PropertiesManager.segmentMarginHorizontal,
    segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    skinThickness: Int = 2,
    waitSeconds: Double = testContext.syncWaitSeconds,
    distance: Double? = 0.1,
    onTrue: () -> Unit
): BooleanCompareResult {

    val command = "ifImageExistNot"

    val v = findImage(
        label = label,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        mergeIncluded = mergeIncluded,
        skinThickness = skinThickness,
        waitSeconds = waitSeconds,
        threshold = distance,
    )
    val matched = v.isFound.not()
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
 * ifTrue
 */
fun VisionDrive.ifTrue(
    value: Boolean,
    message: String = message(id = "ifTrue"),
    onTrue: (TestElement) -> Unit
): BooleanCompareResult {

    return value.ifTrue(
        message = message,
        onTrue = onTrue
    )
}

/**
 * ifFalse
 */
fun VisionDrive.ifFalse(
    value: Boolean,
    message: String = message(id = "ifFalse"),
    onFalse: (TestElement) -> Unit
): BooleanCompareResult {

    return value.ifFalse(
        message = message,
        onFalse = onFalse
    )
}

/**
 * ifCanDetect
 */
fun VisionDrive.ifCanDetect(
    expression: String,
    onTrue: (VisionElement) -> Unit = {}
): BooleanCompareResult {

    val command = "ifCanDetect"

    val v = detect(expression = expression, throwsException = false, waitSeconds = 0.0)
    val matched = v.isFound

    val result = BooleanCompareResult(value = matched, command = command)
    val sel = getSelector(expression = expression)
    val message = message(id = command, subject = sel.toString())

    if (matched || TestMode.isNoLoadRun) {
        val context = TestDriverCommandContext(null)
        context.execBranch(command = command, condition = message) {

            onTrue.invoke(v)
        }
    }

    result.setExecuted(condition = message, matched = matched, message = message)
    return result
}

/**
 * ifCanDetectNot
 */
fun VisionDrive.ifCanDetectNot(
    expression: String,
    onTrue: (VisionElement) -> Unit
): BooleanCompareResult {

    val command = "ifCanDetectNot"

    val v = detect(expression = expression, throwsException = false, waitSeconds = 0.0)
    val matched = v.isEmpty

    val result = BooleanCompareResult(value = matched, command = command)
    val sel = getSelector(expression = expression)
    val message = message(id = command, subject = sel.toString())

    if (matched || TestMode.isNoLoadRun) {

        val context = TestDriverCommandContext(null)
        context.execBranch(command = command, condition = message) {

            onTrue.invoke(v)
        }
    }

    result.setExecuted(condition = message, matched = matched, message = message)
    return result
}

