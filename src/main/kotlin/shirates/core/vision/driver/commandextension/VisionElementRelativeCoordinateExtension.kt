package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.RelativeDirection
import shirates.core.driver.isAbove
import shirates.core.driver.isLeft
import shirates.core.driver.isRight
import shirates.core.logging.CodeExecutionContext
import shirates.core.utility.image.SegmentContainer
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.commandextension.helper.HorizontalBand
import shirates.core.vision.driver.commandextension.helper.VerticalBand
import shirates.core.vision.driver.lastElement

/**
 * rightItem
 */
fun VisionElement.rightItem(
    pos: Int = 1,
    horizontalMargin: Int = PropertiesManager.segmentMarginHorizontal,
    verticalMargin: Int = PropertiesManager.segmentMarginVertical,
    include: Boolean = true
): VisionElement {

    return rightLeftCore(
        relative = RelativeDirection.right,
        pos = pos,
        segmentMarginHorizontal = horizontalMargin,
        segmentMarginVertical = verticalMargin,
        mergeIncluded = include,
    )
}

/**
 * leftItem
 */
fun VisionElement.leftItem(
    pos: Int = 1,
    horizontalMargin: Int = PropertiesManager.segmentMarginHorizontal,
    verticalMargin: Int = PropertiesManager.segmentMarginVertical,
    include: Boolean = true,
): VisionElement {

    return rightLeftCore(
        relative = RelativeDirection.left,
        pos = pos,
        segmentMarginHorizontal = horizontalMargin,
        segmentMarginVertical = verticalMargin,
        mergeIncluded = include,
    )
}

internal fun VisionElement.rightLeftCore(
    relative: RelativeDirection,
    pos: Int,
    segmentMarginHorizontal: Int,
    segmentMarginVertical: Int,
    mergeIncluded: Boolean,
): VisionElement {

    if (isKeyboardShown) {
        hideKeyboard()
    }
    screenshot()

    val segmentContainer = SegmentContainer(
        mergeIncluded = mergeIncluded,
        containerImage = CodeExecutionContext.lastScreenshotImage,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
    ).analyze()

    val horizontalBand = HorizontalBand(baseElement = this)
    for (v in segmentContainer.visionElements) {
        horizontalBand.merge(element = v, margin = 0)
    }
    val elms = horizontalBand.getElements()
    val sortedElements =
        if (relative.isRight)
            elms.filter { this.rect.right <= it.rect.left }
                .sortedBy { this.rect.left }
        else
            elms.filter { it.rect.right <= this.rect.right }
                .sortedBy { this.rect.left }
    val v =
        if (sortedElements.isEmpty() || sortedElements.count() < pos) VisionElement(capture = false)
        else sortedElements[pos - 1]

    val relativeExpression = if (relative.isLeft) ":leftItem($pos)" else ":rightItem($pos)"
    v.selector = this.selector?.getChainedSelector(relativeExpression)
    lastElement = v

    return v
}

/**
 * aboveItem
 */
fun VisionElement.aboveItem(
    pos: Int = 1,
    horizontalMargin: Int = PropertiesManager.segmentMarginHorizontal,
    verticalMargin: Int = PropertiesManager.segmentMarginVertical,
    include: Boolean = true,
): VisionElement {

    return aboveBelowCore(
        relative = RelativeDirection.above,
        pos = pos,
        horizontalMargin = horizontalMargin,
        verticalMargin = verticalMargin,
        mergeIncluded = include,
    )
}

/**
 * belowItem
 */
fun VisionElement.belowItem(
    pos: Int = 1,
    horizontalMargin: Int = PropertiesManager.segmentMarginHorizontal,
    verticalMargin: Int = PropertiesManager.segmentMarginVertical,
    include: Boolean = true,
): VisionElement {

    return aboveBelowCore(
        relative = RelativeDirection.below,
        pos = pos,
        horizontalMargin = horizontalMargin,
        verticalMargin = verticalMargin,
        mergeIncluded = include,
    )
}

internal fun VisionElement.aboveBelowCore(
    relative: RelativeDirection,
    pos: Int,
    horizontalMargin: Int,
    verticalMargin: Int,
    mergeIncluded: Boolean,
): VisionElement {

    if (isKeyboardShown) {
        hideKeyboard()
    }
    screenshot()

    val segmentContainer = SegmentContainer(
        mergeIncluded = mergeIncluded,
        containerImage = CodeExecutionContext.lastScreenshotImage,
        segmentMarginHorizontal = horizontalMargin,
        segmentMarginVertical = verticalMargin,
    ).analyze()

    val verticalBand = VerticalBand(baseElement = this)
    for (v in segmentContainer.visionElements) {
        verticalBand.merge(element = v, margin = 0)
    }
    val elms = verticalBand.getElements()
    val sortedElements =
        if (relative.isAbove)
            elms.filter { it.rect.bottom < this.rect.top }
                .sortedByDescending { it.rect.top }
        else
            elms.filter { this.rect.bottom < it.rect.top }
                .sortedBy { it.rect.top }
    val v =
        if (sortedElements.isEmpty() || sortedElements.count() < pos) VisionElement(capture = false)
        else sortedElements[pos - 1]

    val relativeExpression = if (relative.isAbove) ":aboveItem($pos)" else ":belowItem($pos)"
    v.selector = this.selector?.getChainedSelector(relativeExpression)
    lastElement = v

    return v
}

/**
 * rightText
 */
fun VisionElement.rightText(
    pos: Int = 1,
): VisionElement {

    return rightLeftTextCore(
        relative = RelativeDirection.right,
        pos = pos,
    )
}

/**
 * leftText
 */
fun VisionElement.leftText(
    pos: Int = 1,
): VisionElement {

    return rightLeftTextCore(
        relative = RelativeDirection.left,
        pos = pos,
    )
}

internal fun VisionElement.rightLeftTextCore(
    relative: RelativeDirection,
    pos: Int,
): VisionElement {

    rootElement.recognizeText()

    val horizontalBand = HorizontalBand(baseElement = this)
    for (v in rootElement.visionContext.visionElements) {
        horizontalBand.merge(element = v, margin = 0)
    }
    val elms = horizontalBand.getElements()
    val sortedElements =
        if (relative.isLeft)
            elms.filter { it.rect.right < this.rect.left }
                .sortedByDescending { it.rect.left }
        else
            elms.filter { this.rect.right < it.rect.left }
                .sortedBy { it.rect.left }
    val v =
        if (sortedElements.isEmpty() || sortedElements.count() < pos) VisionElement(capture = false)
        else sortedElements[pos - 1]

    val relativeExpression = if (relative.isAbove) ":aboveText($pos)" else ":belowText($pos)"
    v.selector = this.selector?.getChainedSelector(relativeExpression)
    lastElement = v

    return v
}

/**
 * aboveText
 */
fun VisionElement.aboveText(
    pos: Int = 1,
): VisionElement {

    return aboveBelowTextCore(
        relative = RelativeDirection.above,
        pos = pos,
    )
}

/**
 * belowText
 */
fun VisionElement.belowText(
    pos: Int = 1,
): VisionElement {

    return aboveBelowTextCore(
        relative = RelativeDirection.below,
        pos = pos,
    )
}

internal fun VisionElement.aboveBelowTextCore(
    relative: RelativeDirection,
    pos: Int,
): VisionElement {

    rootElement.recognizeText()

    val verticalBand = VerticalBand(baseElement = this)
    for (v in rootElement.visionContext.visionElements) {
        verticalBand.merge(element = v, margin = 0)
    }
    val elms = verticalBand.getElements()
    val sortedElements =
        if (relative.isAbove)
            elms.filter { it.rect.bottom < this.rect.top }
                .sortedByDescending { it.rect.top }
        else
            elms.filter { this.rect.bottom < it.rect.top }
                .sortedBy { it.rect.top }
    val v =
        if (sortedElements.isEmpty() || sortedElements.count() < pos) VisionElement(capture = false)
        else sortedElements[pos - 1]

    val relativeExpression = if (relative.isAbove) ":aboveText($pos)" else ":belowText($pos)"
    v.selector = this.selector?.getChainedSelector(relativeExpression)
    lastElement = v

    return v
}