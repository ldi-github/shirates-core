package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.RelativeDirection
import shirates.core.driver.isAbove
import shirates.core.driver.isLeft
import shirates.core.driver.isRight
import shirates.core.utility.image.SegmentContainer
import shirates.core.utility.string.forVisionComparison
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.commandextension.helper.HorizontalBand
import shirates.core.vision.driver.commandextension.helper.VerticalBand
import shirates.core.vision.driver.lastElement

/**
 * rightItem
 */
fun VisionElement.rightItem(
    pos: Int = 1,
    verticalMargin: Int = this.rect.height / 2,
    segmentMarginHhorizontal: Int = PropertiesManager.segmentMarginHorizontal,
    segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
    include: Boolean = true
): VisionElement {

    return rightLeftCore(
        relative = RelativeDirection.right,
        pos = pos,
        verticalMargin = verticalMargin,
        segmentMarginHorizontal = segmentMarginHhorizontal,
        segmentMarginVertical = segmentMarginVertical,
        mergeIncluded = include,
    )
}

/**
 * leftItem
 */
fun VisionElement.leftItem(
    pos: Int = 1,
    verticalMargin: Int = this.rect.height / 2,
    segmentMarginHorizontal: Int = PropertiesManager.segmentMarginHorizontal,
    segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
    include: Boolean = true,
): VisionElement {

    return rightLeftCore(
        relative = RelativeDirection.left,
        pos = pos,
        verticalMargin = verticalMargin,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        mergeIncluded = include,
    )
}

internal fun VisionElement.rightLeftCore(
    relative: RelativeDirection,
    pos: Int,
    verticalMargin: Int,
    segmentMarginHorizontal: Int,
    segmentMarginVertical: Int,
    mergeIncluded: Boolean,
): VisionElement {

    val regionElement =
        if (relative == RelativeDirection.left) this.leftRegionElement(verticalMargin = verticalMargin)
        else this.rightRegionElement(verticalMargin = verticalMargin)
    val segmentContainer = SegmentContainer(
        mergeIncluded = mergeIncluded,
        containerImage = regionElement.image,
        containerX = regionElement.rect.x,
        containerY = regionElement.rect.y,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
    ).split()

    val horizontalBand = HorizontalBand(baseElement = this)
    for (v in segmentContainer.visionElements) {
        horizontalBand.merge(element = v, margin = 0)
    }
    val elms = horizontalBand.getElements().map { it as VisionElement }
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
    horizontalMargin: Int = this.rect.width / 2,
    segmentMarginHorizontal: Int = PropertiesManager.segmentMarginHorizontal,
    segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
    include: Boolean = true,
): VisionElement {

    return aboveBelowCore(
        relative = RelativeDirection.above,
        pos = pos,
        horizontalMargin = horizontalMargin,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        mergeIncluded = include,
    )
}

/**
 * belowItem
 */
fun VisionElement.belowItem(
    pos: Int = 1,
    horizontalMargin: Int = this.rect.width / 2,
    segmentMarginHorizontal: Int = PropertiesManager.segmentMarginHorizontal,
    segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
    include: Boolean = true,
): VisionElement {

    return aboveBelowCore(
        relative = RelativeDirection.below,
        pos = pos,
        horizontalMargin = horizontalMargin,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        mergeIncluded = include,
    )
}

internal fun VisionElement.aboveBelowCore(
    relative: RelativeDirection,
    pos: Int,
    horizontalMargin: Int,
    segmentMarginHorizontal: Int,
    segmentMarginVertical: Int,
    mergeIncluded: Boolean,
): VisionElement {

    val regionElement =
        if (relative == RelativeDirection.above) this.aboveRegionElement(horizontalMargin = horizontalMargin)
        else this.belowRegionElement(horizontalMargin = horizontalMargin)
    val segmentContainer = SegmentContainer(
        mergeIncluded = mergeIncluded,
        containerImage = regionElement.image,
        containerX = regionElement.rect.x,
        containerY = regionElement.rect.y,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
    ).split()

    val verticalBand = VerticalBand(baseElement = this)
    for (v in segmentContainer.visionElements) {
        verticalBand.merge(element = v, margin = 0)
    }
    val elms = verticalBand.getElements().map { it as VisionElement }
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

    val elements = getHorizontalElements()
        .filter { this.rect.right < it.rect.left }
        .sortedBy { it.rect.left }
    val v =
        if (elements.isEmpty() || elements.count() < pos) VisionElement(capture = false)
        else elements[pos - 1]

    v.selector = this.selector?.getChainedSelector(":rightText($pos)")
    lastElement = v

    return v
}

/**
 * rightText
 */
fun VisionElement.rightText(
    expression: String,
): VisionElement {

    val elements = getHorizontalElements()
        .filter { this.rect.right < it.rect.left }
        .filter { it.text.forVisionComparison().contains(expression.forVisionComparison()) }
        .sortedBy { it.rect.left }
    val v = elements.firstOrNull() ?: VisionElement(capture = false)

    v.selector = this.selector?.getChainedSelector(":rightText($expression)")
    lastElement = v

    return v
}

/**
 * leftText
 */
fun VisionElement.leftText(
    pos: Int = 1,
): VisionElement {

    val elements = getHorizontalElements()
        .filter { it.rect.right < this.rect.left }
        .sortedBy { it.rect.left }
    val v =
        if (elements.isEmpty() || elements.count() < pos) VisionElement(capture = false)
        else elements[elements.count() - pos]

    v.selector = this.selector?.getChainedSelector(":leftText($pos)")
    lastElement = v

    return v
}

/**
 * leftText
 */
fun VisionElement.leftText(
    expression: String,
): VisionElement {

    val elements = getHorizontalElements()
        .filter { it.rect.right < this.rect.left }
        .filter { it.text.forVisionComparison().contains(expression.forVisionComparison()) }
        .sortedBy { it.rect.left }
    val v = elements.lastOrNull() ?: VisionElement(capture = false)

    v.selector = this.selector?.getChainedSelector(":leftText($expression)")
    lastElement = v

    return v
}

private fun VisionElement.getHorizontalElements(): List<VisionElement> {
    rootElement.visionContext.recognizeText()

    val horizontalBand = HorizontalBand(baseElement = this)
    for (v in rootElement.visionContext.getVisionElements()) {
        horizontalBand.merge(element = v, margin = 0)
    }
    val elms = horizontalBand.getElements().map { it as VisionElement }
    val sortedElements = elms.sortedByDescending { it.rect.left }
    return sortedElements
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

    rootElement.visionContext.recognizeText()

    val verticalBand = VerticalBand(baseElement = this)
    for (v in rootElement.visionContext.getVisionElements()) {
        verticalBand.merge(element = v, margin = 0)
    }
    val elms = verticalBand.getElements().map { it as VisionElement }
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

/**
 * leftImage
 */
fun VisionElement.leftImage(
    label: String,
    pos: Int = 1,
    threshold: Double? = PropertiesManager.visionFindImageThreshold,
    segmentMarginHorizontal: Int = 0,
    segmentMarginVertical: Int = 0,
    mergeIncluded: Boolean = true,
    skinThickness: Int = 2,
): VisionElement {

    if (pos <= 0) {
        throw IllegalArgumentException("pos must be greater than or equal to 0")
    }

    var imageElements: List<VisionElement> = mutableListOf()
    onLeft {
        imageElements = findImages(
            label = label,
            threshold = threshold,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            mergeIncluded = mergeIncluded,
            skinThickness = skinThickness,
        )
    }
    if (imageElements.isEmpty() || imageElements.count() < pos) {
        return VisionElement.emptyElement
    }
    imageElements = imageElements.filter { it.rect.left <= this.rect.left }
    imageElements = imageElements.sortedByDescending { it.rect.left }
    val v = imageElements[pos - 1]
    v.selector = Selector(":leftImage(\"$label\", $pos)")
    return v
}

/**
 * rightImage
 */
fun VisionElement.rightImage(
    label: String,
    pos: Int = 1,
    threshold: Double? = PropertiesManager.visionFindImageThreshold,
    segmentMarginHorizontal: Int = 0,
    segmentMarginVertical: Int = 0,
    mergeIncluded: Boolean = true,
    skinThickness: Int = 2,
): VisionElement {

    if (pos <= 0) {
        throw IllegalArgumentException("pos must be greater than or equal to 0")
    }

    var imageElements: List<VisionElement> = mutableListOf()
    onRight {
        imageElements = findImages(
            label = label,
            threshold = threshold,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            mergeIncluded = mergeIncluded,
            skinThickness = skinThickness,
        )
    }
    if (imageElements.isEmpty() || imageElements.count() < pos) {
        return VisionElement.emptyElement
    }
    imageElements = imageElements.filter { this.rect.left < it.rect.left }
    imageElements = imageElements.sortedBy { it.rect.left }
    val v = imageElements[pos - 1]
    v.selector = Selector(":rightImage(\"$label\", $pos)")
    return v
}

/**
 * aboveImage
 */
fun VisionElement.aboveImage(
    label: String,
    pos: Int = 1,
    threshold: Double? = PropertiesManager.visionFindImageThreshold,
    segmentMarginHorizontal: Int = 0,
    segmentMarginVertical: Int = 0,
    mergeIncluded: Boolean = true,
    skinThickness: Int = 2,
): VisionElement {

    if (pos <= 0) {
        throw IllegalArgumentException("pos must be greater than or equal to 0")
    }

    var imageElements: List<VisionElement> = mutableListOf()
    onAbove {
        imageElements = findImages(
            label = label,
            threshold = threshold,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            mergeIncluded = mergeIncluded,
            skinThickness = skinThickness,
        )
    }
    if (imageElements.isEmpty() || imageElements.count() < pos) {
        return VisionElement.emptyElement
    }
    imageElements = imageElements.filter { it.rect.top <= this.rect.top }
    imageElements = imageElements.sortedByDescending { it.rect.top }
    val v = imageElements[pos - 1]
    v.selector = Selector(":aboveImage(\"$label\", $pos)")
    return v
}

/**
 * belowImage
 */
fun VisionElement.belowImage(
    label: String,
    pos: Int = 1,
    threshold: Double? = PropertiesManager.visionFindImageThreshold,
    segmentMarginHorizontal: Int = 0,
    segmentMarginVertical: Int = 0,
    mergeIncluded: Boolean = true,
    skinThickness: Int = 2,
): VisionElement {

    if (pos <= 0) {
        throw IllegalArgumentException("pos must be greater than or equal to 0")
    }

    var imageElements: List<VisionElement> = mutableListOf()
    onBelow {
        imageElements = findImages(
            label = label,
            threshold = threshold,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            mergeIncluded = mergeIncluded,
            skinThickness = skinThickness,
        )
    }
    if (imageElements.isEmpty() || imageElements.count() < pos) {
        return VisionElement.emptyElement
    }
    imageElements = imageElements.filter { this.rect.top < it.rect.top }
    imageElements = imageElements.sortedBy { it.rect.top }
    val v = imageElements[pos - 1]
    v.selector = Selector(":belowImage(\"$label\", $pos)")
    return v
}

/**
 * leftRadioButton
 */
fun VisionElement.leftRadioButton(
    label: String = "[RadioButton]",
    pos: Int = 1,
    threshold: Double? = 1.0,
    segmentMarginHorizontal: Int = 0,
    segmentMarginVertical: Int = 0,
    mergeIncluded: Boolean = true,
    skinThickness: Int = 2,
): VisionElement {

    if (pos <= 0) {
        throw IllegalArgumentException("pos must be greater than or equal to 0")
    }

    var imageElements: List<VisionElement> = mutableListOf()
    onLine {
        imageElements = findImages(
            label = label,
            threshold = threshold,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            mergeIncluded = mergeIncluded,
            skinThickness = skinThickness,
        )
    }
    if (imageElements.isEmpty() || imageElements.count() < pos) {
        return VisionElement.emptyElement
    }
    imageElements = imageElements.filter { it.rect.left <= this.rect.left }
    imageElements = imageElements.sortedByDescending { it.rect.left }
    val v = imageElements[pos - 1]
    v.selector = Selector(":leftRadioButton($pos)")
    return v
}