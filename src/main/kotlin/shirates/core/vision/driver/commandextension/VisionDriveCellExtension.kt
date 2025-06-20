package shirates.core.vision.driver.commandextension

import shirates.core.driver.testContext
import shirates.core.testcode.CodeExecutionContext
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement

/**
 * onCellOf
 */
fun VisionDrive.onCellOf(
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
    val cell = v.cell()
    cell.onThisElementRegion {
        withoutScroll {
            func?.invoke(cell)
        }
    }
    return lastElement
}

/**
 * onCellOfWithScrollDown
 */
fun VisionDrive.onCellOfWithScrollDown(
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
    val cell = baseElement.cell()
    cell.onThisElementRegion {
        withoutScroll {
            func?.invoke(cell)
        }
    }
    return lastElement
}

