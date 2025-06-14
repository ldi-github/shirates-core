package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.testContext
import shirates.core.logging.TestLog
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.image.SegmentContainer
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement
import java.awt.image.BufferedImage

/**
 * cell
 */
fun VisionElement.cell(
    index: Int = 0,
    mergeIncluded: Boolean = false,
    containerImage: BufferedImage? = CodeExecutionContext.lastScreenshotImage,
    containerImageFile: String? = null,
    containerX: Int = 0,
    containerY: Int = 0,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    skinThickness: Int = 2,
    binaryThreshold: Int = PropertiesManager.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = 0.0,
    minimumWidth: Int = 5,
    minimumHeight: Int = 5,
    outputDirectory: String = TestLog.directoryForLog.resolve("${TestLog.currentLineNo}").toString(),
    croppingMargin: Int = PropertiesManager.segmentCroppingMargin,
): VisionElement {

    val sc = SegmentContainer(
        mergeIncluded = mergeIncluded,
        containerImage = containerImage,
        containerImageFile = containerImageFile,
        containerX = containerX,
        containerY = containerY,
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

