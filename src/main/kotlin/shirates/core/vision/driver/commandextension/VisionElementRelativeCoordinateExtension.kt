package shirates.core.vision.driver.commandextension

import shirates.core.Const
import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.logging.TestLog
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.escapeFileName
import shirates.core.utility.file.toFile
import shirates.core.utility.image.*
import shirates.core.utility.string.forVisionComparison
import shirates.core.utility.time.StopWatch
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.commandextension.helper.HorizontalBand
import shirates.core.vision.driver.commandextension.helper.VerticalBand
import shirates.core.vision.driver.lastElement
import java.awt.Color

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
    textToLineHeightRatio: Double = PropertiesManager.visionTextToLineHeightRatio,
    numberOfColors: Int = Const.VISION_NUMBER_OF_COLORS_16,
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
        filterMargin = filterMargin,
        textToLineHeightRatio = textToLineHeightRatio,
        numberOfColors = numberOfColors,
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
    textToLineHeightRatio: Double = PropertiesManager.visionTextToLineHeightRatio,
    numberOfColors: Int = Const.VISION_NUMBER_OF_COLORS_16,
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
        filterMargin = filterMargin,
        textToLineHeightRatio = textToLineHeightRatio,
        numberOfColors = numberOfColors,
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
    textToLineHeightRatio: Double,
    numberOfColors: Int = Const.VISION_NUMBER_OF_COLORS_16,
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
        val name = selector?.toString()?.escapeFileName() ?: CodeExecutionContext.lastScreenshotFile!!.toFile().name
        val outputDir = TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_${name}_baseElement").toString()
        val thisSegmentContainer = SegmentContainer(
            mergeIncluded = mergeIncluded,
            containerImage = this.image,
            containerX = this.rect.left,
            containerY = this.rect.top,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            binaryThreshold = binaryThreshold,
            aspectRatioTolerance = aspectRatioTolerance,
            outputDirectory = outputDir,
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
     * masking image
     */
    val lastScreenshot = CodeExecutionContext.lastScreenshotImage!!
    val grayImage = lastScreenshot.convertColorModel(numColors = numberOfColors)
    val lineHeight = (this.rect.height * textToLineHeightRatio).toInt()
    val bandRect = Rectangle(0, this.rect.centerY - lineHeight / 2, lastScreenshot.width, lineHeight)
    val binaryImage = BinarizationUtility.getBinaryAsGrayU8(image = grayImage, threshold = binaryThreshold)
        .toBufferedImage()!!
    val g2d = binaryImage.createGraphics()
    g2d.color = Color.BLACK
    g2d.fillRect(0, 0, bandRect.width, bandRect.top)
    g2d.fillRect(0, bandRect.bottom, bandRect.width, grayImage.height - bandRect.bottom)

    /**
     * split to vision elements
     */
    val name = selector?.toString()?.escapeFileName() ?: CodeExecutionContext.lastScreenshotFile!!.toFile().name
    val outputDir = TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_$name").toString()
    val segmentContainer = SegmentContainer(
        mergeIncluded = mergeIncluded,
        containerImage = binaryImage,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        skinThickness = 1,
        binaryThreshold = binaryThreshold,
        outputDirectory = outputDir,
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

    if (this.isEmpty || swipeToSafePosition.not()) {
        return this
    }
    val v = this.swipeToSafePosition()
    return v
}

/**
 * aboveItem
 */
fun VisionElement.aboveItem(
    pos: Int = 1,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    include: Boolean = false,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    swipeToSafePosition: Boolean = CodeExecutionContext.withScroll ?: CodeExecutionContext.swipeToSafePosition,
    numberOfColors: Int = Const.VISION_NUMBER_OF_COLORS_16,
    removeEdgeLine: Boolean = false,
): VisionElement {

    val thisElement = getSafeElement(swipeToSafePosition = swipeToSafePosition)
    return thisElement.aboveBelowCore(
        relative = RelativeDirection.above,
        pos = pos,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        mergeIncluded = include,
        binaryThreshold = binaryThreshold,
        numberOfColors = numberOfColors,
        removeEdgeLine = removeEdgeLine,
    )
}

/**
 * aboveLineItem
 */
fun VisionElement.aboveRow(
    pos: Int = 1,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    numberOfColors: Int = Const.VISION_NUMBER_OF_COLORS_16,
    lineThreshold: Double = PropertiesManager.visionLineThreshold,
): VisionElement {

    val rs = RowSplitter(
        containerImage = CodeExecutionContext.lastScreenshotImage,
        containerImageFile = CodeExecutionContext.lastScreenshotFile,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        binaryThreshold = binaryThreshold,
        lineThreshold = lineThreshold,
    ).split()

    if (pos == 0) {
        return VisionElement.emptyElement
    }
    if (pos < 0) {
        return belowRow(
            pos = -pos,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            binaryThreshold = binaryThreshold,
            numberOfColors = numberOfColors,
            lineThreshold = lineThreshold,
        )
    }
    val aboveRows = rs.rows.filter { it.rect.bottom < this.rect.top }.sortedByDescending { it.rect.top }

    if (pos <= aboveRows.count()) {
        val v = aboveRows[pos - 1].rect.toVisionElement()
        return v
    }
    return VisionElement.emptyElement
}

/**
 * belowItem
 */
fun VisionElement.belowItem(
    pos: Int = 1,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    include: Boolean = false,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    swipeToSafePosition: Boolean = CodeExecutionContext.withScroll ?: CodeExecutionContext.swipeToSafePosition,
    numberOfColors: Int = Const.VISION_NUMBER_OF_COLORS_16,
): VisionElement {

    val thisElement = getSafeElement(swipeToSafePosition = swipeToSafePosition)
    return thisElement.aboveBelowCore(
        relative = RelativeDirection.below,
        pos = pos,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        mergeIncluded = include,
        binaryThreshold = binaryThreshold,
        numberOfColors = numberOfColors,
    )
}

internal fun VisionElement.aboveBelowCore(
    relative: RelativeDirection,
    pos: Int,
    segmentMarginHorizontal: Int,
    segmentMarginVertical: Int,
    mergeIncluded: Boolean,
    binaryThreshold: Int,
    numberOfColors: Int = Const.VISION_NUMBER_OF_COLORS_16,
    removeEdgeLine: Boolean = false,
): VisionElement {

    val relativeExpression = if (relative.isAbove) ":aboveItem($pos)" else ":belowItem($pos)"
    val selector = this.selector?.getChainedSelector(relativeExpression)
    if (TestMode.isNoLoadRun) {
        val v = VisionElement.emptyElement
        v.selector = selector
        return v
    }

    val sw = StopWatch("aboveBelowCore")

    val baseElement = this

    /**
     * split screenshot into segments
     */
    val image = CodeExecutionContext.lastScreenshotImage!!.convertColorModel(numColors = numberOfColors)
    val binary = BinarizationUtility.getBinaryAsGrayU8(
        image = image,
        invert = false,
//        skinThickness = skinThickness,
        threshold = binaryThreshold
    ).toBufferedImage()!!
    var containerImage = binary
    if (removeEdgeLine) {
        containerImage = containerImage.horizontalLineRemoved().verticalLineRemoved()
    }

    val name = selector?.toString()?.escapeFileName() ?: CodeExecutionContext.lastScreenshotFile!!.toFile().name
    val outputDir = TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_$name").toString()
    val segmentContainer = SegmentContainer(
        mergeIncluded = mergeIncluded,
        containerImage = containerImage,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        binaryThreshold = binaryThreshold,
        outputDirectory = outputDir,
    ).split()
        .saveImages()
    val count = segmentContainer.visionElements.count()
    sw.lap("split screenshot into segments. visionElements:$count")

    /**
     * filter items
     */
    var elms = if (relative.isAbove) segmentContainer.visionElements.filter {
        it.rect.top <= baseElement.rect.bottom
    } else segmentContainer.visionElements.filter {
        baseElement.rect.top <= it.rect.bottom
    }
    elms = elms.filter { it.rect.left <= baseElement.rect.right && baseElement.rect.left <= it.rect.right }
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
        if (relative.isAbove) {
            mergedElements.filter { it.isSameRect(baseElement).not() && it.rect.bottom < baseElement.rect.top }
                .sortedWith(compareByDescending<VisionElement> { it.rect.bottom }.thenBy { Math.abs(it.rect.centerX - this.rect.centerX) })
        } else {
            mergedElements.filter { it.isSameRect(baseElement).not() && baseElement.rect.bottom < it.rect.top }
                .sortedWith(compareBy<VisionElement> { it.rect.top }.thenBy { Math.abs(it.rect.centerX - this.rect.centerX) })
        }
    val v =
        if (sortedElements.isEmpty() || sortedElements.count() < pos) VisionElement(capture = false)
        else sortedElements[pos - 1]

    v.selector = selector
    lastElement = v

    sw.stop()

    return v
}

/**
 * belowRow
 */
fun VisionElement.belowRow(
    pos: Int = 1,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    numberOfColors: Int = Const.VISION_NUMBER_OF_COLORS_16,
    lineThreshold: Double = PropertiesManager.visionLineThreshold,
): VisionElement {

    val rs = RowSplitter(
        containerImage = CodeExecutionContext.lastScreenshotImage,
        containerImageFile = CodeExecutionContext.lastScreenshotFile,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        binaryThreshold = binaryThreshold,
        lineThreshold = lineThreshold,
    ).split()

    if (pos == 0) {
        return VisionElement.emptyElement
    }
    if (pos < 0) {
        return aboveRow(
            pos = -pos,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            binaryThreshold = binaryThreshold,
            numberOfColors = numberOfColors,
            lineThreshold = lineThreshold,
        )
    }
    val belowRows = rs.rows.filter { this.rect.bottom < it.rect.top }

    if (pos <= belowRows.count()) {
        val v = belowRows[pos - 1].rect.toVisionElement()
        return v
    }
    return VisionElement.emptyElement
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
        v = this.leftText(pos = -pos)
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