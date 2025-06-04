package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.image.SegmentContainer
import shirates.core.utility.string.forVisionComparison
import shirates.core.utility.time.StopWatch
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.commandextension.helper.HorizontalBand
import shirates.core.vision.driver.commandextension.helper.VerticalBand
import shirates.core.vision.driver.lastElement

/**
 * rightItem
 */
fun VisionElement.rightItem(
    pos: Int = 1,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    segmentMinimumHeight: Int = this.rect.height / 2,
    include: Boolean = false,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = testContext.visionFindImageAspectRatioTolerance,
    centerBase: Boolean = false,
    filterMargin: Int = 5,
): VisionElement {

    return rightLeftCore(
        relative = RelativeDirection.right,
        pos = pos,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        segmentMinimumHeight = segmentMinimumHeight,
        mergeIncluded = include,
        binaryThreshold = binaryThreshold,
        aspectRatioTolerance = aspectRatioTolerance,
        centerBase = centerBase,
        filterMargin = filterMargin
    )
}

/**
 * leftItem
 */
fun VisionElement.leftItem(
    pos: Int = 1,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    segmentMinimumHeight: Int = this.rect.height / 2,
    include: Boolean = false,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = testContext.visionFindImageAspectRatioTolerance,
    centerBase: Boolean = true,
    filterMargin: Int = 5,
): VisionElement {

    return rightLeftCore(
        relative = RelativeDirection.left,
        pos = pos,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        segmentMinimumHeight = segmentMinimumHeight,
        mergeIncluded = include,
        binaryThreshold = binaryThreshold,
        aspectRatioTolerance = aspectRatioTolerance,
        centerBase = centerBase,
        filterMargin = filterMargin
    )
}

internal fun VisionElement.rightLeftCore(
    relative: RelativeDirection,
    pos: Int,
    segmentMarginHorizontal: Int,
    segmentMarginVertical: Int,
    segmentMinimumHeight: Int,
    mergeIncluded: Boolean,
    binaryThreshold: Int,
    aspectRatioTolerance: Double,
    centerBase: Boolean,
    filterMargin: Int,
): VisionElement {

    val relativeExpression = if (relative.isLeft) ":leftItem($pos)" else ":rightItem($pos)"
    val selector = this.selector?.getChainedSelector(relativeExpression)
    if (TestMode.isNoLoadRun) {
        val v = VisionElement.emptyElement
        v.selector = selector
        return v
    }

    val sw = StopWatch("rightLeftCore")

    var baseElement = this
    if (centerBase) {
        /**
         * get baseElement
         */
        val thisSegmentContainer = SegmentContainer(
            mergeIncluded = mergeIncluded,
            containerImage = this.image,
            containerX = this.rect.left,
            containerY = this.rect.top,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            binaryThreshold = binaryThreshold,
            aspectRatioTolerance = aspectRatioTolerance,
        ).split()
            .saveImages()

        sw.lap("get baseElement")

        val visionElements = thisSegmentContainer.visionElements.filter { segmentMinimumHeight <= it.rect.height }
            .sortedBy { it.rect.left }
        val largestWidth = visionElements.maxOfOrNull { it.rect.width }
        baseElement = if (largestWidth == null) {
            if (relative.isLeft) visionElements.firstOrNull() ?: this
            else visionElements.lastOrNull() ?: this
        } else {
            visionElements.first { it.rect.width == largestWidth }
        }
    }

    /**
     * split screenshot into segments
     */
    val segmentContainer = SegmentContainer(
        mergeIncluded = mergeIncluded,
        containerImage = CodeExecutionContext.lastScreenshotImage,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        binaryThreshold = binaryThreshold,
    ).split()
        .saveImages()
    val count = segmentContainer.visionElements.count()
    sw.lap("split screenshot into segments. visionElements:$count")

    /**
     * filter items
     */
    var elms = segmentContainer.visionElements.filter {
        (it.rect.top <= baseElement.rect.bottom + filterMargin && baseElement.rect.top - filterMargin <= it.rect.bottom)
    }
    if (mergeIncluded.not()) {
        elms = elms.filter { baseElement.bounds.isIncludedIn(it.bounds).not() }
    }

    /**
     * merge
     */
    val mergeContainer = SegmentContainer(
        mergeIncluded = true,
        containerImage = CodeExecutionContext.lastScreenshotImage,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        binaryThreshold = binaryThreshold,
    )
    for (e in elms) {
        mergeContainer.addSegment(rect = e.rect)
    }
    val mergedElements = mergeContainer.visionElements

    val sortedElements =
        if (relative.isRight) {
            val minLeft = if (this.isMerged.not() && centerBase) baseElement.rect.centerX else baseElement.rect.right
            mergedElements.filter { it.isSameRect(baseElement).not() && minLeft < it.rect.left }
                .sortedWith(compareBy<VisionElement> { it.rect.left }.thenBy { Math.abs(it.rect.centerY - this.rect.centerY) })
        } else {
            val maxRight = if (this.isMerged.not() && centerBase) baseElement.rect.centerX else baseElement.rect.left
            mergedElements.filter { it.isSameRect(baseElement).not() && it.rect.right < maxRight }
                .sortedWith(compareByDescending<VisionElement> { it.rect.left }.thenBy { Math.abs(it.rect.centerY - this.rect.centerY) })
        }
    val v =
        if (sortedElements.isEmpty() || sortedElements.count() < pos) VisionElement(capture = false)
        else sortedElements[pos - 1]

    v.selector = selector
    lastElement = v

    sw.stop()

    return v
}

internal fun VisionElement.getSafeElement(swipeToSafePosition: Boolean): VisionElement {

    var v = this
    if (swipeToSafePosition) {
        v = this.swipeToSafePosition()
    }
    return v
}

/**
 * aboveItem
 */
fun VisionElement.aboveItem(
    pos: Int = 1,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    segmentMinimumWidth: Int = this.rect.width / 2,
    include: Boolean = false,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = testContext.visionFindImageAspectRatioTolerance,
    swipeToSafePosition: Boolean = CodeExecutionContext.withScroll ?: CodeExecutionContext.swipeToSafePosition
): VisionElement {

    val thisElement = getSafeElement(swipeToSafePosition = swipeToSafePosition)
    return thisElement.aboveBelowCore(
        relative = RelativeDirection.above,
        pos = pos,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        segmentMinimumWidth = segmentMinimumWidth,
        mergeIncluded = include,
        binaryThreshold = binaryThreshold,
        aspectRatioTolerance = aspectRatioTolerance,
    )
}

/**
 * aboveLineItem
 */
fun VisionElement.aboveLineItem(
    pos: Int = 1,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    segmentMinimumWidth: Int = this.rect.width / 2,
    include: Boolean = false,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = testContext.visionFindImageAspectRatioTolerance,
): VisionElement {

    val belowItem = this.aboveItem(
        pos = pos,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        segmentMinimumWidth = segmentMinimumWidth,
        include = include,
        binaryThreshold = binaryThreshold,
        aspectRatioTolerance = aspectRatioTolerance,
    )
    return belowItem.lineRegionElement()
}

/**
 * belowItem
 */
fun VisionElement.belowItem(
    pos: Int = 1,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    segmentMinimumWidth: Int = this.rect.width / 2,
    include: Boolean = false,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = testContext.visionFindImageAspectRatioTolerance,
    swipeToSafePosition: Boolean = CodeExecutionContext.withScroll ?: CodeExecutionContext.swipeToSafePosition
): VisionElement {

    val thisElement = getSafeElement(swipeToSafePosition = swipeToSafePosition)
    return thisElement.aboveBelowCore(
        relative = RelativeDirection.below,
        pos = pos,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        segmentMinimumWidth = segmentMinimumWidth,
        mergeIncluded = include,
        binaryThreshold = binaryThreshold,
        aspectRatioTolerance = aspectRatioTolerance,
    )
}

internal fun VisionElement.aboveBelowCore(
    relative: RelativeDirection,
    pos: Int,
    segmentMarginHorizontal: Int,
    segmentMarginVertical: Int,
    segmentMinimumWidth: Int,
    mergeIncluded: Boolean,
    binaryThreshold: Int,
    aspectRatioTolerance: Double,
): VisionElement {

    val relativeExpression = if (relative.isAbove) ":aboveItem($pos)" else ":belowItem($pos)"
    val selector = this.selector?.getChainedSelector(relativeExpression)
    if (TestMode.isNoLoadRun) {
        val v = VisionElement.emptyElement
        v.selector = selector
        return v
    }

    /**
     * split screenshot into segments
     */
    val thisSegmentContainer = SegmentContainer(
        mergeIncluded = mergeIncluded,
        containerImage = this.image,
        containerX = this.rect.left,
        containerY = this.rect.top,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        binaryThreshold = binaryThreshold,
        aspectRatioTolerance = aspectRatioTolerance,
    ).split()
        .saveImages()
    val visionElements = thisSegmentContainer.visionElements.filter { segmentMinimumWidth <= it.rect.width }
    val baseElement =
        if (relative.isAbove) visionElements.firstOrNull() ?: this
        else visionElements.lastOrNull() ?: this

    /**
     * filter items
     */
    val segmentContainer = SegmentContainer(
        mergeIncluded = mergeIncluded,
        containerImage = CodeExecutionContext.lastScreenshotImage,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        binaryThreshold = binaryThreshold,
    ).split()
        .saveImages()
    var elms = segmentContainer.visionElements.filter {
        (it.rect.left <= baseElement.rect.right && baseElement.rect.left <= it.rect.right)
    }
    if (mergeIncluded.not()) {
        elms = elms.filter { baseElement.bounds.isIncludedIn(it.bounds).not() }
    }

    /**
     * merge
     */
    val mergeContainer = SegmentContainer(
        mergeIncluded = true,
        containerImage = CodeExecutionContext.lastScreenshotImage,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        binaryThreshold = binaryThreshold,
    )
    for (e in elms) {
        mergeContainer.addSegment(rect = e.rect)
    }
    val mergedElements = mergeContainer.visionElements

    val sortedElements =
        if (relative.isBelow)
            mergedElements.filter { it.isSameRect(baseElement).not() && baseElement.rect.bottom < it.rect.top }
                .sortedWith(compareBy<VisionElement> { it.rect.top }.thenBy { Math.abs(it.rect.centerX - this.rect.centerX) })
        else
            mergedElements.filter { it.isSameRect(baseElement).not() && it.rect.bottom < baseElement.rect.top }
                .sortedWith(compareByDescending<VisionElement> { it.rect.top }.thenBy { Math.abs(it.rect.centerX - this.rect.centerX) })
    val v =
        if (sortedElements.isEmpty() || sortedElements.count() < pos) VisionElement(capture = false)
        else sortedElements[pos - 1]

    v.selector = selector
    lastElement = v

    return v
}

/**
 * belowLineItem
 */
fun VisionElement.belowLineItem(
    pos: Int = 1,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    segmentMinimumWidth: Int = this.rect.width / 2,
    include: Boolean = false,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = testContext.visionFindImageAspectRatioTolerance,
): VisionElement {

    val belowItem = this.belowItem(
        pos = pos,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        segmentMinimumWidth = segmentMinimumWidth,
        include = include,
        binaryThreshold = binaryThreshold,
        aspectRatioTolerance = aspectRatioTolerance,
    )
    return belowItem.lineRegionElement()
}

/**
 * rightText
 */
fun VisionElement.rightText(
    pos: Int = 1,
): VisionElement {

    val selector = this.selector?.getChainedSelector(":rightText($pos)")
    var v = VisionElement.emptyElement
    v.selector = selector
    if (TestMode.isNoLoadRun) {
        return v
    }

    if (pos == 0) return this
    else if (pos < 0) {
        v = this.rightText(pos = -pos)
        v.selector = this.selector?.getChainedSelector(":leftText($pos)")
        return v
    }
    val elements = getHorizontalElements()
        .filter { this.rect.right < it.rect.left && this.bounds.isCenterIncludedIn(it.bounds).not() }
        .sortedBy { it.rect.left }
    v = if (elements.isEmpty() || elements.count() < pos)
        VisionElement(capture = false)
    else elements[pos - 1]

    v.selector = selector
    lastElement = v

    return v
}

/**
 * rightText
 */
fun VisionElement.rightText(
    expression: String,
): VisionElement {

    val selector = this.selector?.getChainedSelector(":rightText($expression)")
    if (TestMode.isNoLoadRun) {
        val v = VisionElement.emptyElement
        v.selector = selector
        return v
    }

    val elements = getHorizontalElements()
        .filter { this.rect.right < it.rect.left && this.bounds.isCenterIncludedIn(it.bounds).not() }
        .filter { it.text.forVisionComparison().contains(expression.forVisionComparison()) }
        .sortedBy { it.rect.left }
    val v = elements.firstOrNull() ?: VisionElement(capture = false)

    v.selector = selector
    lastElement = v

    return v
}

/**
 * leftText
 */
fun VisionElement.leftText(
    pos: Int = 1,
): VisionElement {

    val selector = this.selector?.getChainedSelector(":leftText($pos)")
    if (TestMode.isNoLoadRun) {
        val v = VisionElement.emptyElement
        v.selector = selector
        return v
    }

    val v: VisionElement
    if (pos == 0) return this
    else if (pos < 0) {
        v = this.rightText(pos = -pos)
        v.selector = this.selector?.getChainedSelector(":leftText($pos)")
        return v
    }
    val elements = getHorizontalElements()
        .filter { it.rect.right < this.rect.left && this.bounds.isCenterIncludedIn(it.bounds).not() }
        .sortedBy { it.rect.left }
    v = if (elements.isEmpty() || elements.count() < pos)
        VisionElement(capture = false)
    else elements[elements.count() - pos]

    v.selector = selector
    lastElement = v

    return v
}

/**
 * leftText
 */
fun VisionElement.leftText(
    expression: String,
): VisionElement {

    val selector = this.selector?.getChainedSelector(":leftText($expression)")
    if (TestMode.isNoLoadRun) {
        val v = VisionElement.emptyElement
        v.selector = selector
        return v
    }

    val elements = getHorizontalElements()
        .filter { it.rect.right < this.rect.left && this.bounds.isCenterIncludedIn(it.bounds).not() }
        .filter { it.text.forVisionComparison().contains(expression.forVisionComparison()) }
        .sortedBy { it.rect.left }
    val v = elements.lastOrNull() ?: VisionElement(capture = false)

    v.selector = selector
    lastElement = v

    return v
}

private fun VisionElement.getHorizontalElements(): List<VisionElement> {
    rootElement.visionContext.recognizeText()

    val horizontalBand = HorizontalBand(baseElement = this)
    for (v in rootElement.visionTextElements) {
        horizontalBand.merge(element = v, margin = 0)
    }
    val elms = horizontalBand.getElements().map { it as VisionElement }
    val sortedElements = elms.sortedByDescending { it.rect.left }
    return sortedElements
}

private fun VisionElement.getVerticalElements(): List<VisionElement> {
    rootElement.visionContext.recognizeText()

    val verticalBand = VerticalBand(baseElement = this)
    for (v in rootElement.visionTextElements) {
        verticalBand.merge(element = v, margin = 0)
    }
    val elms = verticalBand.getElements().map { it as VisionElement }
    val sortedElements = elms.sortedByDescending { it.rect.top }
    return sortedElements
}

/**
 * aboveText
 */
fun VisionElement.aboveText(
    pos: Int = 1,
    swipeToSafePosition: Boolean = CodeExecutionContext.withScroll ?: CodeExecutionContext.swipeToSafePosition
): VisionElement {

    val thisElement = getSafeElement(swipeToSafePosition = swipeToSafePosition)

    val selector = thisElement.selector?.getChainedSelector(":rightText($pos)")
    if (TestMode.isNoLoadRun) {
        val v = VisionElement.emptyElement
        v.selector = selector
        return v
    }

    val v: VisionElement
    if (pos == 0) return thisElement
    else if (pos < 0) {
        v = thisElement.belowText(pos = -pos)
        v.selector = thisElement.selector?.getChainedSelector(":aboveText($pos)")
        return v
    }
    val elements = getVerticalElements()
        .filter { it.rect.bottom < thisElement.rect.top && thisElement.bounds.isCenterIncludedIn(it.bounds).not() }
        .sortedByDescending { it.rect.top }
    v = if (elements.isEmpty() || elements.count() < pos)
        VisionElement(capture = false)
    else elements[pos - 1]
    v.selector = selector
    lastElement = v

    return v
}

/**
 * aboveText
 */
fun VisionElement.aboveText(
    expression: String,
    swipeToSafePosition: Boolean = CodeExecutionContext.withScroll ?: CodeExecutionContext.swipeToSafePosition
): VisionElement {

    val thisElement = getSafeElement(swipeToSafePosition = swipeToSafePosition)

    val selector = thisElement.selector?.getChainedSelector(":aboveText($expression)")
    if (TestMode.isNoLoadRun) {
        val v = VisionElement.emptyElement
        v.selector = selector
        return v
    }

    val elements = thisElement.getVerticalElements()
        .filter { it.rect.bottom < thisElement.rect.top && thisElement.bounds.isCenterIncludedIn(it.bounds).not() }
        .filter { it.text.forVisionComparison().contains(expression.forVisionComparison()) }
        .sortedBy { it.rect.top }
    val v = elements.lastOrNull() ?: VisionElement(capture = false)

    v.selector = selector
    lastElement = v

    return v
}

/**
 * belowText
 */
fun VisionElement.belowText(
    pos: Int = 1,
    swipeToSafePosition: Boolean = CodeExecutionContext.withScroll ?: CodeExecutionContext.swipeToSafePosition
): VisionElement {

    val thisElement = getSafeElement(swipeToSafePosition = swipeToSafePosition)

    val selector = thisElement.selector?.getChainedSelector(":belowText($pos)")
    if (TestMode.isNoLoadRun) {
        val v = VisionElement.emptyElement
        v.selector = selector
        return v
    }

    val v: VisionElement
    if (pos == 0) return thisElement
    else if (pos < 0) {
        v = thisElement.aboveText(pos = -pos)
        v.selector = thisElement.selector?.getChainedSelector(":belowText($pos)")
        return v
    }

    val elements = thisElement.getVerticalElements()
        .filter { thisElement.rect.bottom < it.rect.top && thisElement.bounds.isCenterIncludedIn(it.bounds).not() }
        .sortedBy { it.rect.top }
    v = if (elements.isEmpty() || elements.count() < pos)
        VisionElement(capture = false)
    else elements[pos - 1]
    v.selector = thisElement.selector?.getChainedSelector(":belowText($pos)")
    lastElement = v

    return v
}

/**
 * belowText
 */
fun VisionElement.belowText(
    expression: String,
    swipeToSafePosition: Boolean = CodeExecutionContext.withScroll ?: CodeExecutionContext.swipeToSafePosition
): VisionElement {

    val thisElement = getSafeElement(swipeToSafePosition = swipeToSafePosition)

    val selector = thisElement.selector?.getChainedSelector(":belowText($expression)")
    if (TestMode.isNoLoadRun) {
        val v = VisionElement.emptyElement
        v.selector = selector
        return v
    }

    val elements = thisElement.getVerticalElements()
        .filter { thisElement.rect.bottom < it.rect.top && thisElement.bounds.isCenterIncludedIn(it.bounds).not() }
        .filter { it.text.forVisionComparison().contains(expression.forVisionComparison()) }
        .sortedBy { it.rect.top }
    val v = elements.firstOrNull() ?: VisionElement(capture = false)

    v.selector = thisElement.selector?.getChainedSelector(":belowText($expression)")
    lastElement = v

    return v
}

//internal fun VisionElement.aboveBelowTextCore(
//    relative: RelativeDirection,
//    pos: Int,
//): VisionElement {
//
//    rootElement.visionContext.recognizeText()
//
//    val verticalBand = VerticalBand(baseElement = this)
//    for (v in rootElement.visionContext.getVisionElements()) {
//        verticalBand.merge(element = v, margin = 0)
//    }
//    val elms = verticalBand.getElements().map { it as VisionElement }
//    val sortedElements =
//        if (relative.isAbove)
//            elms.filter { it.rect.bottom < this.rect.top }
//                .sortedByDescending { it.rect.top }
//        else
//            elms.filter { this.rect.bottom < it.rect.top }
//                .sortedBy { it.rect.top }
//    val v =
//        if (sortedElements.isEmpty() || sortedElements.count() < pos) VisionElement(capture = false)
//        else sortedElements[pos - 1]
//
//    val relativeExpression = if (relative.isAbove) ":aboveText($pos)" else ":belowText($pos)"
//    v.selector = this.selector?.getChainedSelector(relativeExpression)
//    lastElement = v
//
//    return v
//}

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

    val selector = Selector(":leftImage(\"$label\", $pos)")
    if (TestMode.isNoLoadRun) {
        val v = VisionElement.emptyElement
        v.selector = selector
        return v
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
    imageElements = imageElements.filter { it.rect.left <= this.rect.left }
    if (imageElements.isEmpty() || imageElements.count() < pos) {
        return VisionElement.emptyElement
    }
    imageElements = imageElements.sortedByDescending { it.rect.left }
    val v = imageElements[pos - 1]
    v.selector = selector
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

    val selector = Selector(":rightImage(\"$label\", $pos)")
    if (TestMode.isNoLoadRun) {
        val v = VisionElement.emptyElement
        v.selector = selector
        return v
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
    imageElements = imageElements.filter { this.rect.left < it.rect.left }
    if (imageElements.isEmpty() || imageElements.count() < pos) {
        return VisionElement.emptyElement
    }
    imageElements = imageElements.sortedBy { it.rect.left }
    val v = imageElements[pos - 1]
    v.selector = selector
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

    val selector = Selector(":aboveImage(\"$label\", $pos)")
    if (TestMode.isNoLoadRun) {
        val v = VisionElement.emptyElement
        v.selector = selector
        return v
    }

    var imageElements: List<VisionElement> = mutableListOf()
    this.onAbove {
        imageElements = findImages(
            label = label,
            threshold = threshold,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            mergeIncluded = mergeIncluded,
            skinThickness = skinThickness,
        )
    }
    imageElements = imageElements.filter { it.rect.top <= this.rect.top }
    if (imageElements.isEmpty() || imageElements.count() < pos) {
        return VisionElement.emptyElement
    }
    imageElements = imageElements.sortedByDescending { it.rect.top }
    val v = imageElements[pos - 1]
    v.selector = selector
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

    val selector = Selector(":belowImage(\"$label\", $pos)")
    if (TestMode.isNoLoadRun) {
        val v = VisionElement.emptyElement
        v.selector = selector
        return v
    }

    var imageElements: List<VisionElement> = mutableListOf()
    this.onBelow {
        imageElements = findImages(
            label = label,
            threshold = threshold,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            mergeIncluded = mergeIncluded,
            skinThickness = skinThickness,
        )
    }
    imageElements = imageElements.filter { this.rect.top < it.rect.top }
    if (imageElements.isEmpty() || imageElements.count() < pos) {
        return VisionElement.emptyElement
    }
    imageElements = imageElements.sortedBy { it.rect.top }
    val v = imageElements[pos - 1]
    v.selector = selector
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

    val selector = Selector(":leftRadioButton($pos)")
    if (TestMode.isNoLoadRun) {
        val v = VisionElement.emptyElement
        v.selector = selector
        return v
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
    imageElements = imageElements.filter { it.rect.left <= this.rect.left }
    if (imageElements.isEmpty() || imageElements.count() < pos) {
        return VisionElement.emptyElement
    }
    imageElements = imageElements.sortedByDescending { it.rect.left }
    val v = imageElements[pos - 1]
    v.selector = selector
    return v
}