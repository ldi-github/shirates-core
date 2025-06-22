package shirates.core.vision.driver.commandextension

import shirates.core.driver.testContext
import shirates.core.logging.TestLog
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.image.*
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement

/**
 * cell
 */
fun VisionElement.cell(
    index: Int = 0,
    mergeIncluded: Boolean = false,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    skinThickness: Int = 2,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = 0.0,
    minimumWidth: Int = 5,
    minimumHeight: Int = 5,
    outputDirectory: String = TestLog.directoryForLog.resolve("${TestLog.currentLineNo}").toString(),
    croppingMargin: Int = testContext.segmentCroppingMargin,
    colorScale: ColorScale = testContext.visionColorScale,
): VisionElement {

    val image = CodeExecutionContext.lastScreenshotImage!!.convertColorScale(colorScale = colorScale)
    val binary = BinarizationUtility.getBinaryAsGrayU8(
        image = image,
        invert = false,
//        skinThickness = skinThickness,
        threshold = binaryThreshold
    ).toBufferedImage()!!

    val sc = SegmentContainer(
        mergeIncluded = mergeIncluded,
        containerImage = binary,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        skinThickness = skinThickness,
        binaryThreshold = binaryThreshold,
        aspectRatioTolerance = aspectRatioTolerance,
        minimumWidth = minimumWidth,
        minimumHeight = minimumHeight,
        outputDirectory = outputDirectory,
        croppingMargin = croppingMargin,
    ).split()

    val v = this
    var ancestors = sc.visionElements.filter { v.rect.isIncludedIn(it.rect) }
    ancestors = ancestors.sortedByDescending { it.rect.area }

    if (ancestors.count() - 1 < index) {
        return VisionElement.emptyElement
    }
    return ancestors[index]
}

/**
 * onCell
 */
fun VisionElement.onCell(
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val cell = this.cell()
    if (this.selector?.expression != null) {
        target(this.selector!!.expression!!)
    }
    cell.onThisElementRegion {
        withoutScroll {
            func?.invoke(cell)
        }
    }
    return lastElement
}
