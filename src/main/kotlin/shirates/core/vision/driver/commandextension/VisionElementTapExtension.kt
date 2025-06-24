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
    include: Boolean = false,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    holdSeconds: Double = TestDriver.testContext.tapHoldSeconds,
): VisionElement {

    var v = this.belowItem(
        pos = pos,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        include = include,
        binaryThreshold = binaryThreshold,
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
    include: Boolean = false,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    holdSeconds: Double = TestDriver.testContext.tapHoldSeconds,
): VisionElement {

    var v = this.aboveItem(
        pos = pos,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        include = include,
        binaryThreshold = binaryThreshold,
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
    include: Boolean = false,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    holdSeconds: Double = TestDriver.testContext.tapHoldSeconds,
): VisionElement {

    var v = this.rightItem(
        pos = pos,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        include = include,
        binaryThreshold = binaryThreshold,
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
    include: Boolean = false,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    holdSeconds: Double = TestDriver.testContext.tapHoldSeconds,
): VisionElement {

    var v = this.leftItem(
        pos = pos,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        include = include,
        binaryThreshold = binaryThreshold,
    )
    v = v.tap(holdSeconds = holdSeconds)
    return v
}

/**
 * tapOffset
 */
fun VisionElement.tapOffset(
    offsetX: Int = 0,
    offsetY: Int = 0
): VisionElement {

    val rect = this.rect
    var x =
        if (offsetX == 0) rect.centerX
        else if (offsetX < 0) rect.left
        else rect.right
    x += offsetX
    x = x / testContext.boundsToRectRatio

    var y =
        if (offsetY == 0) rect.centerY
        else if (offsetY < 0) rect.top
        else rect.bottom
    y += offsetY
    y = y / testContext.boundsToRectRatio

    tap(x = x, y = y)
    return this
}

/**
 * tapOffsetX
 */
fun VisionElement.tapOffsetX(
    offsetX: Int
): VisionElement {

    tapOffset(offsetX = offsetX)
    return this
}

/**
 * tapOffsetY
 */
fun VisionElement.tapOffsetY(
    offsetY: Int
): VisionElement {

    tapOffset(offsetY = offsetY)
    return this
}
