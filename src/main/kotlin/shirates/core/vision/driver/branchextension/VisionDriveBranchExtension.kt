import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.testContext
import shirates.core.logging.Message.message
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.branchextension.ifFalse
import shirates.core.vision.driver.branchextension.ifTrue
import shirates.core.vision.driver.branchextension.result.VisionDriveBooleanCompareResult
import shirates.core.vision.driver.commandextension.detect
import shirates.core.vision.driver.commandextension.findImage

/**
 * ifImageExist
 */
fun VisionDrive.ifImageExist(
    label: String,
    threshold: Double = testContext.visionFindImageThreshold,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    skinThickness: Int = 2,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = testContext.visionFindImageAspectRatioTolerance,
    waitSeconds: Double = 0.0,
    onTrue: (() -> Unit)
): VisionDriveBooleanCompareResult {

    val command = "ifImageExist"

    val v = findImage(
        label = label,
        threshold = threshold,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        mergeIncluded = mergeIncluded,
        skinThickness = skinThickness,
        binaryThreshold = binaryThreshold,
        aspectRatioTolerance = aspectRatioTolerance,
        waitSeconds = waitSeconds,
        throwsException = false
    )
    val matched = v.isFound
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
 * ifImageExistNot
 */
fun VisionDrive.ifImageExistNot(
    label: String,
    threshold: Double = testContext.visionFindImageThreshold,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    skinThickness: Int = 2,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = testContext.visionFindImageAspectRatioTolerance,
    waitSeconds: Double = 0.0,
    onTrue: () -> Unit
): VisionDriveBooleanCompareResult {

    val command = "ifImageExistNot"

    val v = findImage(
        label = label,
        threshold = threshold,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        mergeIncluded = mergeIncluded,
        skinThickness = skinThickness,
        binaryThreshold = binaryThreshold,
        aspectRatioTolerance = aspectRatioTolerance,
        waitSeconds = waitSeconds,
        throwsException = false
    )
    val matched = v.isFound.not()
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
 * ifTrue
 */
fun VisionDrive.ifTrue(
    value: Boolean,
    message: String = message(id = "ifTrue"),
    onTrue: (VisionElement) -> Unit
): VisionDriveBooleanCompareResult {

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
    onFalse: (VisionElement) -> Unit
): VisionDriveBooleanCompareResult {

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
    language: String = testContext.visionOCRLanguage,
    looseMatch: Boolean = testContext.visionLooseMatch,
    mergeBoundingBox: Boolean = testContext.visionMergeBoundingBox,
    lineSpacingRatio: Double = testContext.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
    waitSeconds: Double = 0.0,
    onTrue: (VisionElement) -> Unit = {}
): VisionDriveBooleanCompareResult {

    val command = "ifCanDetect"

    val v = detect(
        expression = expression,
        language = language,
        looseMatch = looseMatch,
        mergeBoundingBox = mergeBoundingBox,
        lineSpacingRatio = lineSpacingRatio,
        autoImageFilter = autoImageFilter,
        waitSeconds = waitSeconds,
        throwsException = false,
    )
    val matched = v.isFound

    val result = VisionDriveBooleanCompareResult(value = matched, command = command)
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
    language: String = testContext.visionOCRLanguage,
    looseMatch: Boolean = testContext.visionLooseMatch,
    mergeBoundingBox: Boolean = testContext.visionMergeBoundingBox,
    lineSpacingRatio: Double = testContext.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
    onTrue: (VisionElement) -> Unit
): VisionDriveBooleanCompareResult {

    val command = "ifCanDetectNot"

    val v = detect(
        expression = expression,
        language = language,
        looseMatch = looseMatch,
        mergeBoundingBox = mergeBoundingBox,
        lineSpacingRatio = lineSpacingRatio,
        autoImageFilter = autoImageFilter,
        throwsException = false,
    )
    val matched = v.isEmpty

    val result = VisionDriveBooleanCompareResult(value = matched, command = command)
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

