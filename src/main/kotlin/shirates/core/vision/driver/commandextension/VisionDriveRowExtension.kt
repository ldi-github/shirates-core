package shirates.core.vision.driver.commandextension

import shirates.core.driver.testContext
import shirates.core.testcode.CodeExecutionContext
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement

/**
 * onRowOf
 */
fun VisionDrive.onRowOf(
    expression: String,
    language: String = testContext.visionOCRLanguage,
    looseMatch: Boolean = testContext.visionLooseMatch,
    mergeBoundingBox: Boolean = testContext.visionMergeBoundingBox,
    lineSpacingRatio: Double = testContext.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
    allowScroll: Boolean? = true,
    swipeToSafePosition: Boolean = CodeExecutionContext.withScroll ?: CodeExecutionContext.swipeToSafePosition,
    throwsException: Boolean = true,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val v = detect(
        expression = expression,
        language = language,
        looseMatch = looseMatch,
        mergeBoundingBox = mergeBoundingBox,
        lineSpacingRatio = lineSpacingRatio,
        autoImageFilter = autoImageFilter,
        allowScroll = allowScroll,
        swipeToSafePosition = swipeToSafePosition,
        throwsException = throwsException,
    )
    target(expression)
    val row = v.row()
    row.onThisElementRegion {
        withoutScroll {
            func?.invoke(row)
        }
    }
    return lastElement
}

/**
 * onRowOfWithScrollDown
 */
fun VisionDrive.onRowOfWithScrollDown(
    expression: String,
    language: String = testContext.visionOCRLanguage,
    looseMatch: Boolean = testContext.visionLooseMatch,
    mergeBoundingBox: Boolean = testContext.visionMergeBoundingBox,
    lineSpacingRatio: Double = testContext.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
    swipeToSafePosition: Boolean = true,
    throwsException: Boolean = true,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val v = detectWithScrollDown(
        expression = expression,
        language = language,
        looseMatch = looseMatch,
        mergeBoundingBox = mergeBoundingBox,
        lineSpacingRatio = lineSpacingRatio,
        autoImageFilter = autoImageFilter,
        throwsException = throwsException,
    )
    val baseElement =
        if (swipeToSafePosition) v.swipeToSafePosition()
        else v
    target(expression)
    val row = baseElement.row()
    row.onThisElementRegion {
        withoutScroll {
            func?.invoke(row)
        }
    }
    return lastElement
}
