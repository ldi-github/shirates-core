package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.testContext
import shirates.core.logging.TestLog
import shirates.core.utility.image.SegmentContainer
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import java.awt.image.BufferedImage

/**
 * descendants
 */
fun VisionDrive.descendants(
    mergeIncluded: Boolean = false,
    containerImage: BufferedImage? = this.getThisOrIt().image,
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
): List<VisionElement> {

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

    return sc.visionElements
}

