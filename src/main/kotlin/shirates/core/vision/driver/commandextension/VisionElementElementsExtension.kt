package shirates.core.vision.driver.commandextension

import shirates.core.driver.testContext
import shirates.core.logging.TestLog
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.image.*
import shirates.core.vision.VisionElement

/**
 * ancestors
 */
fun VisionElement.ancestors(
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
    filter: ((VisionElement) -> Boolean)? = null,
    colorPalette: ColorPalette = testContext.visionColorPalette,
): List<VisionElement> {

    val image = CodeExecutionContext.lastScreenshotImage!!.convertColorModel(colorPalette = colorPalette)
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
    if (filter != null) {
        ancestors = ancestors.filter { filter.invoke(it) }
    }
    ancestors = ancestors.sortedByDescending { it.rect.area }

    return ancestors
}

/**
 * ancestorsContains
 */
fun VisionElement.ancestorsContains(
    rect: Rectangle,
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
    filter: ((VisionElement) -> Boolean)? = null,
    colorPalette: ColorPalette = testContext.visionColorPalette,
): List<VisionElement> {

    val image = CodeExecutionContext.lastScreenshotImage!!.convertColorModel(colorPalette = colorPalette)
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
    var ancestors = sc.visionElements.filter { v.rect.isIncludedIn(it.rect) }.filter { rect.isIncludedIn(it.rect) }
    if (filter != null) {
        ancestors = ancestors.filter { filter.invoke(it) }
    }
    ancestors = ancestors.filter { it.rect.x1 != 0 && it.rect.y1 != 0 }
    ancestors = ancestors.sortedByDescending { it.rect.area }

    return ancestors
}

/**
 * ancestorsContains
 */
fun VisionElement.ancestorsContains(
    visionElement: VisionElement,
    mergeIncluded: Boolean = false,
    segmentMarginHorizontal: Int = 5,
    segmentMarginVertical: Int = 5,
    skinThickness: Int = 2,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = 0.0,
    minimumWidth: Int = 5,
    minimumHeight: Int = 5,
    outputDirectory: String = TestLog.directoryForLog.resolve("${TestLog.currentLineNo}").toString(),
    croppingMargin: Int = testContext.segmentCroppingMargin,
    filter: ((VisionElement) -> Boolean)? = null,
    colorPalette: ColorPalette = testContext.visionColorPalette,
): List<VisionElement> {

    val image = CodeExecutionContext.lastScreenshotImage!!.convertColorModel(colorPalette = colorPalette)
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
    var ancestors =
        sc.visionElements.filter { v.rect.isIncludedIn(it.rect) }.filter { visionElement.rect.isIncludedIn(it.rect) }
    if (filter != null) {
        ancestors = ancestors.filter { filter.invoke(it) }
    }
    ancestors = ancestors.sortedByDescending { it.rect.area }

    return ancestors
}
