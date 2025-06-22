package shirates.core.vision.driver.commandextension

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
import shirates.core.vision.VisionServerProxy
import shirates.core.vision.driver.VisionContext
import shirates.core.vision.driver.lastElement
import java.awt.Color

/**
 * rightItem
 */
fun VisionElement.rightItem(
    pos: Int = 1,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    include: Boolean = false,
    textToLineHeightRatio: Double = testContext.visionTextToLineHeightRatio,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    filterMargin: Int = 5,
    colorModel: ColorModel = testContext.visionColorModel,
): VisionElement {

    return rightLeftCore(
        relative = RelativeDirection.right,
        pos = pos,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        mergeIncluded = include,
        textToLineHeightRatio = textToLineHeightRatio,
        binaryThreshold = binaryThreshold,
        filterMargin = filterMargin,
        colorModel = colorModel,
    )
}

/**
 * leftItem
 */
fun VisionElement.leftItem(
    pos: Int = 1,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    include: Boolean = false,
    textToLineHeightRatio: Double = testContext.visionTextToLineHeightRatio,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    filterMargin: Int = 5,
    colorModel: ColorModel = testContext.visionColorModel,
): VisionElement {

    return rightLeftCore(
        relative = RelativeDirection.left,
        pos = pos,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        mergeIncluded = include,
        textToLineHeightRatio = textToLineHeightRatio,
        binaryThreshold = binaryThreshold,
        filterMargin = filterMargin,
        colorModel = colorModel,
    )
}

internal fun VisionElement.rightLeftCore(
    relative: RelativeDirection,
    pos: Int,
    segmentMarginHorizontal: Int,
    segmentMarginVertical: Int,
    textToLineHeightRatio: Double,
    mergeIncluded: Boolean,
    binaryThreshold: Int,
    filterMargin: Int,
    colorModel: ColorModel,
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
    if (selector?.expression != null && this.strictMatched.not() && selector.relativeSelectors.isEmpty()     // relativeSelectors not supported
    ) {
        baseElement = detect(selector.expression!!, looseMatch = false)
    }
    val lastScreenshot = CodeExecutionContext.lastScreenshotImage!!
    val grayImage = lastScreenshot.convertColorModel(colorModel = colorModel)
    val lineHeight = (textToLineHeightRatio * baseElement.rect.height).toInt()
    val rect = Rectangle(0, baseElement.rect.centerY - lineHeight / 2, grayImage.width, lineHeight)
    val binaryImage = BinarizationUtility.getBinaryAsGrayU8(image = grayImage, threshold = binaryThreshold)
        .toBufferedImage()!!.maskAboveBelow(rect)

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
    segmentContainer.saveImages()
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
    if (elms.isEmpty()) {
        return VisionElement.emptyElement
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
            val minLeft = baseElement.rect.right
            mergedElements.filter { it.isSameRect(baseElement).not() && minLeft < it.rect.left }
                .sortedWith(compareBy<VisionElement> { it.rect.left }.thenBy { Math.abs(it.rect.centerY - this.rect.centerY) })
        } else {
            val maxRight = baseElement.rect.left
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
    colorModel: ColorModel = testContext.visionColorModel,
    removeHorizontalLine: Boolean = false,
    horizontalLineThreshold: Double = PropertiesManager.visionHorizontalLineThreshold,
    removeVerticalLine: Boolean = true,
    verticalLineThreshold: Double = PropertiesManager.visionVerticalLineThreshold,
): VisionElement {

    val thisElement = getSafeElement(swipeToSafePosition = swipeToSafePosition)
    return thisElement.aboveBelowCore(
        relative = RelativeDirection.above,
        pos = pos,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        mergeIncluded = include,
        binaryThreshold = binaryThreshold,
        colorModel = colorModel,
        removeHorizontalLine = removeHorizontalLine,
        horizontalLineThreshold = horizontalLineThreshold,
        removeVerticalLine = removeVerticalLine,
        verticalLineThreshold = verticalLineThreshold,
    )
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
    colorModel: ColorModel = testContext.visionColorModel,
    removeHorizontalLine: Boolean = false,
    horizontalLineThreshold: Double = PropertiesManager.visionHorizontalLineThreshold,
    removeVerticalLine: Boolean = true,
    verticalLineThreshold: Double = PropertiesManager.visionVerticalLineThreshold,
): VisionElement {

    val thisElement = getSafeElement(swipeToSafePosition = swipeToSafePosition)
    return thisElement.aboveBelowCore(
        relative = RelativeDirection.below,
        pos = pos,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        mergeIncluded = include,
        binaryThreshold = binaryThreshold,
        colorModel = colorModel,
        removeHorizontalLine = removeHorizontalLine,
        horizontalLineThreshold = horizontalLineThreshold,
        removeVerticalLine = removeVerticalLine,
        verticalLineThreshold = verticalLineThreshold,
    )
}

internal fun VisionElement.aboveBelowCore(
    relative: RelativeDirection,
    pos: Int,
    segmentMarginHorizontal: Int,
    segmentMarginVertical: Int,
    mergeIncluded: Boolean,
    binaryThreshold: Int,
    colorModel: ColorModel,
    removeHorizontalLine: Boolean,
    horizontalLineThreshold: Double,
    removeVerticalLine: Boolean,
    verticalLineThreshold: Double,
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
     * masking image
     */
    val lastScreenshot = CodeExecutionContext.lastScreenshotImage!!
    val grayImage = lastScreenshot.convertColorModel(colorModel = colorModel)
    var binaryImage = BinarizationUtility.getBinaryAsGrayU8(image = grayImage, threshold = binaryThreshold)
        .toBufferedImage()!!
    if (removeHorizontalLine) {
        binaryImage = binaryImage.horizontalLineRemoved(horizontalLineThreshold = horizontalLineThreshold)
    }
    if (removeVerticalLine) {
        binaryImage = binaryImage.verticalLineRemoved(verticalLineThreshold = verticalLineThreshold)
    }
    val g2d = binaryImage.createGraphics()
    g2d.color = Color.BLACK
    val maskRect = if (relative.isAbove) {
        Rectangle(0, this.rect.bottom + 1, lastScreenshot.width, lastScreenshot.bottom - this.rect.bottom)
    } else {
        Rectangle(0, 0, lastScreenshot.width, this.rect.top)
    }
    g2d.fillRect(maskRect.left, maskRect.top, maskRect.width, maskRect.height)

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
    segmentContainer.saveImages()
    val count = segmentContainer.visionElements.count()
    sw.lap("split screenshot into segments. visionElements:$count")

    /**
     * filter items
     */
    var elms = segmentContainer.visionElements
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
    for (t in rootElement.visionTextElements) {
        mergeContainer.addSegment(rect = t.rect)
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
 * aboveLineItem
 */
fun VisionElement.aboveLineItem(
    pos: Int = 1,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    include: Boolean = false,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    swipeToSafePosition: Boolean = CodeExecutionContext.withScroll ?: CodeExecutionContext.swipeToSafePosition,
    colorModel: ColorModel = testContext.visionColorModel,
    removeHorizontalLine: Boolean = true,
    horizontalLineThreshold: Double = PropertiesManager.visionHorizontalLineThreshold,
    removeVerticalLine: Boolean = true,
    verticalLineThreshold: Double = PropertiesManager.visionVerticalLineThreshold,
): VisionElement {

    val aboveItem = this.aboveItem(
        pos = pos,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        include = include,
        binaryThreshold = binaryThreshold,
        swipeToSafePosition = swipeToSafePosition,
        colorModel = colorModel,
        removeHorizontalLine = removeHorizontalLine,
        horizontalLineThreshold = horizontalLineThreshold,
        removeVerticalLine = removeVerticalLine,
        verticalLineThreshold = verticalLineThreshold,
    )
    return aboveItem.lineRegionElement()
}

/**
 * belowLineItem
 */
fun VisionElement.belowLineItem(
    pos: Int = 1,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    include: Boolean = false,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    swipeToSafePosition: Boolean = CodeExecutionContext.withScroll ?: CodeExecutionContext.swipeToSafePosition,
    colorModel: ColorModel = testContext.visionColorModel,
    removeHorizontalLine: Boolean = true,
    horizontalLineThreshold: Double = PropertiesManager.visionHorizontalLineThreshold,
    removeVerticalLine: Boolean = true,
    verticalLineThreshold: Double = PropertiesManager.visionVerticalLineThreshold,
): VisionElement {

    val belowItem = this.belowItem(
        pos = pos,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        include = include,
        binaryThreshold = binaryThreshold,
        swipeToSafePosition = swipeToSafePosition,
        colorModel = colorModel,
        removeHorizontalLine = removeHorizontalLine,
        horizontalLineThreshold = horizontalLineThreshold,
        removeVerticalLine = removeVerticalLine,
        verticalLineThreshold = verticalLineThreshold,
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
        v = this.leftText(pos = -pos)
        v.selector = this.selector?.getChainedSelector(":leftText($pos)")
        return v
    }
    val rightRegionElement = this.rightRegionElement(lineHeight = this.rect.height)
    val textElements = rightRegionElement.splitTextElements()
        .filter { this.rect.right < it.rect.left && this.bounds.isCenterIncludedIn(it.bounds).not() }
        .sortedBy { it.rect.left }
    v = if (textElements.isEmpty() || textElements.count() < pos)
        VisionElement(capture = false)
    else textElements[pos - 1]

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

    val rightRegionElement = this.rightRegionElement(lineHeight = this.rect.height)
    var textElements = rightRegionElement.splitTextElements()
    textElements = textElements
        .filter { this.rect.right < it.rect.left && this.bounds.isCenterIncludedIn(it.bounds).not() }
        .filter { it.text.forVisionComparison().contains(expression.forVisionComparison()) }
        .sortedBy { it.rect.left }
    val v = textElements.firstOrNull() ?: VisionElement(capture = false)

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

    val leftRegionElement = this.leftRegionElement(lineHeight = this.rect.height)
    val textElements = leftRegionElement.splitTextElements()
        .filter { it.rect.right < this.rect.left && this.bounds.isCenterIncludedIn(it.bounds).not() }
        .sortedBy { it.rect.left }
    v = if (textElements.isEmpty() || textElements.count() < pos)
        VisionElement(capture = false)
    else textElements[textElements.count() - pos]

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

    val leftRectionElement = this.leftRegionElement(lineHeight = this.rect.height)
    val textElements = leftRectionElement.splitTextElements()
        .filter { it.rect.right < this.rect.left && this.bounds.isCenterIncludedIn(it.bounds).not() }
        .filter { it.text.forVisionComparison().contains(expression.forVisionComparison()) }
        .sortedBy { it.rect.left }
    val v = textElements.lastOrNull() ?: VisionElement(capture = false)

    v.selector = selector
    lastElement = v

    return v
}

private fun VisionElement.splitTextElements(): List<VisionElement> {

    val file = TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_split_text.png").toString()
    val subImage = this.image!!.convertColorModel(colorModel = testContext.visionColorModel)
    subImage.saveImage(file = file)

    val r = VisionServerProxy.recognizeText(inputFile = file, language = visionContext.language)
    val textElements =
        r.candidates.map {
            val rect = it.rect.offsetRect(offsetX = this.rect.left, offsetY = this.rect.top)
            val v = rect.toVisionElement()
            v.visionContext.recognizeTextObservations = mutableListOf(
                VisionContext.getObservation(
                    text = it.text,
                    r = r,
                    rect = rect,
                    screenshotFile = this.screenshotFile,
                    screenshotImage = this.screenshotImage,
                )
            )
            v
        }
    return textElements
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
    val textElements = this.aboveRegionElement(columnWidth = screenRect.width).splitTextElements()
        .filter { it.rect.bottom < thisElement.rect.top && thisElement.bounds.isCenterIncludedIn(it.bounds).not() }
        .sortedByDescending { it.rect.top }
    v = if (textElements.isEmpty() || textElements.count() < pos)
        VisionElement(capture = false)
    else textElements[pos - 1]
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

    val textElements = this.aboveRegionElement(columnWidth = screenRect.width).splitTextElements()
        .filter { it.rect.bottom < thisElement.rect.top && thisElement.bounds.isCenterIncludedIn(it.bounds).not() }
        .filter { it.text.forVisionComparison().contains(expression.forVisionComparison()) }
        .sortedBy { it.rect.top }
    val v = textElements.lastOrNull() ?: VisionElement(capture = false)

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

    val textElements = this.belowRegionElement(columnWidth = screenRect.width).splitTextElements()
        .filter { thisElement.rect.bottom < it.rect.top && thisElement.bounds.isCenterIncludedIn(it.bounds).not() }
        .sortedBy { it.rect.top }
    v = if (textElements.isEmpty() || textElements.count() < pos)
        VisionElement(capture = false)
    else textElements[pos - 1]
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

    val textElements = this.belowRegionElement(columnWidth = screenRect.width).splitTextElements()
        .filter { thisElement.rect.bottom < it.rect.top && thisElement.bounds.isCenterIncludedIn(it.bounds).not() }
        .filter { it.text.forVisionComparison().contains(expression.forVisionComparison()) }
        .sortedBy { it.rect.top }
    val v = textElements.firstOrNull() ?: VisionElement(capture = false)

    v.selector = thisElement.selector?.getChainedSelector(":belowText($expression)")
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

/**
 * aboveRow
 */
fun VisionElement.aboveRow(
    pos: Int = 1,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    colorModel: ColorModel = testContext.visionColorModel,
    horizontalLineThreshold: Double = testContext.visionHorizontalLineThreshold,
): VisionElement {

    val rs = RowSplitter(
        containerImage = CodeExecutionContext.lastScreenshotImage,
        containerImageFile = CodeExecutionContext.lastScreenshotFile,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        binaryThreshold = binaryThreshold,
        horizontalLineThreshold = horizontalLineThreshold,
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
            colorModel = colorModel,
            horizontalLineThreshold = horizontalLineThreshold,
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
 * belowRow
 */
fun VisionElement.belowRow(
    pos: Int = 1,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    colorModel: ColorModel = testContext.visionColorModel,
    horizontalLineThreshold: Double = testContext.visionHorizontalLineThreshold,
): VisionElement {

    val rs = RowSplitter(
        containerImage = CodeExecutionContext.lastScreenshotImage,
        containerImageFile = CodeExecutionContext.lastScreenshotFile,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        binaryThreshold = binaryThreshold,
        horizontalLineThreshold = horizontalLineThreshold,
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
            colorModel = colorModel,
            horizontalLineThreshold = horizontalLineThreshold,
        )
    }
    val belowRows = rs.rows.filter { this.rect.bottom < it.rect.top }

    if (pos <= belowRows.count()) {
        val v = belowRows[pos - 1].rect.toVisionElement()
        return v
    }
    return VisionElement.emptyElement
}
