package shirates.core.vision.driver

import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.TestLog
import shirates.core.utility.image.*
import shirates.core.vision.VisionElement

/**
 * right
 */
fun VisionElement.right(
    pos: Int = 1,
    verticalMargin: Int = this.rect.height / 2,
    segmentMargin: Int = 20
): VisionElement {

    if (this.isEmpty) {
        return this
    }

    val lastScreenshotImage = CodeExecutionContext.lastScreenshotImage!!

    /**
     * get rightSideRect
     */
    val r = this.rect

    var left = r.right + 1
    if (left > lastScreenshotImage.right) {
        left = lastScreenshotImage.right
    }

    var top = r.top - verticalMargin
    if (top < 0) top = 0

    val right = lastScreenshotImage.right

    var bottom = r.top + verticalMargin
    if (bottom > lastScreenshotImage.bottom) bottom = lastScreenshotImage.bottom

    val width = right - left
    val height = (bottom - top + 1) + verticalMargin * 2

    val rightSideRect = Rectangle(x = left, y = top, width = width, height = height)
    val rightSideImage = lastScreenshotImage.cropImage(rightSideRect)!!

    /**
     * save rightSideRect
     */
    val rightImageFile = TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_right_side.png").toString()
    rightSideImage.saveImage(rightImageFile)

    /**
     * parse segments
     */
    val segmentContainer = SegmentContainer(
        containerImage = rightSideImage,
        containerImageFile = rightImageFile,
        containerRect = rightSideRect,
        segmentMargin = segmentMargin,
    ).parse()
    if (segmentContainer.segments.isEmpty()) {
        return VisionElement.emptyElement
    }

    /**
     * create VisionElement from primary segment
     */
    val v = segmentContainer.visionElements.firstOrNull() ?: VisionElement.emptyElement
    v.selector = this.selector?.getChainedSelector(":right")

    lastElement = v
    return v
}
