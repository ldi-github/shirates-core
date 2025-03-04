package shirates.core.vision.driver.commandextension

import shirates.core.driver.TestDriver
import shirates.core.driver.testContext
import shirates.core.vision.VisionElement

/**
 * tapBelow
 */
fun VisionElement.tapBelow(
    pos: Int = 1,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    segmentMinimumWidth: Int = this.rect.width / 2,
    include: Boolean = false,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = testContext.visionFindImageAspectRatioTolerance,
    holdSeconds: Double = TestDriver.testContext.tapHoldSeconds,
): VisionElement {

    var v = this.belowItem(
        pos = pos,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        segmentMinimumWidth = segmentMinimumWidth,
        include = include,
        binaryThreshold = binaryThreshold,
        aspectRatioTolerance = aspectRatioTolerance,
    )
    v = v.tap(holdSeconds = holdSeconds)
    return v
}

/**
 * tapAbove
 */
fun VisionElement.tapAbove(
    pos: Int = 1,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    segmentMinimumWidth: Int = this.rect.width / 2,
    include: Boolean = false,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = testContext.visionFindImageAspectRatioTolerance,
    holdSeconds: Double = TestDriver.testContext.tapHoldSeconds,
): VisionElement {

    var v = this.aboveItem(
        pos = pos,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        segmentMinimumWidth = segmentMinimumWidth,
        include = include,
        binaryThreshold = binaryThreshold,
        aspectRatioTolerance = aspectRatioTolerance,
    )
    v = v.tap(holdSeconds = holdSeconds)
    return v
}

/**
 * tapRight
 */
fun VisionElement.tapRight(
    pos: Int = 1,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    segmentMinimumHeight: Int = this.rect.height / 2,
    include: Boolean = false,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = testContext.visionFindImageAspectRatioTolerance,
    holdSeconds: Double = TestDriver.testContext.tapHoldSeconds,
): VisionElement {

    var v = this.rightItem(
        pos = pos,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        segmentMinimumHeight = segmentMinimumHeight,
        include = include,
        binaryThreshold = binaryThreshold,
        aspectRatioTolerance = aspectRatioTolerance,
    )
    v = v.tap(holdSeconds = holdSeconds)
    return v
}

/**
 * tapLeft
 */
fun VisionElement.tapLeft(
    pos: Int = 1,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    segmentMinimumHeight: Int = this.rect.height / 2,
    include: Boolean = false,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = testContext.visionFindImageAspectRatioTolerance,
    holdSeconds: Double = TestDriver.testContext.tapHoldSeconds,
): VisionElement {

    var v = this.leftItem(
        pos = pos,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        segmentMinimumHeight = segmentMinimumHeight,
        include = include,
        binaryThreshold = binaryThreshold,
        aspectRatioTolerance = aspectRatioTolerance,
    )
    v = v.tap(holdSeconds = holdSeconds)
    return v
}
