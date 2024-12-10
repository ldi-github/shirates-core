package shirates.core.utility.image

import boofcv.io.image.UtilImageIO
import shirates.core.driver.Bounds
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.printWarn
import shirates.core.utility.toPath
import shirates.core.vision.VisionElement
import java.awt.image.BufferedImage

class Segment(
    var left: Int = 0,
    var top: Int = 0,
    var width: Int,
    var height: Int,
    var container: SegmentContainer? = null,
    var screenshotImage: BufferedImage? = CodeExecutionContext.lastScreenshotImage,
    var screenshotFile: String? = CodeExecutionContext.lastScreenshotFile
) {
    var segmentImage: BufferedImage? = null

    var segmentImageFile: String? = null

    var segmentMargin: Int
        get() {
            if (container != null) {
                return container!!.segmentMargin
            }
            return _segmentMargin ?: 0
        }
        set(value) {
            _segmentMargin = value
        }
    private var _segmentMargin: Int? = null

    val containerImage: BufferedImage?
        get() {
            return container?.containerImage
        }

    val containerRect: Rectangle?
        get() {
            return container?.containerRect
        }

    /**
     * right
     */
    val right: Int
        get() {
            if (width == 0) {
                return left
            }
            return left + width - 1
        }

    /**
     * bottom
     */
    val bottom: Int
        get() {
            if (height == 0) {
                return top
            }
            return top + height - 1
        }

    /**
     * area
     */
    val area: Int
        get() {
            return width * height
        }

    /**
     * aspectRatio
     */
    val aspectRatio: Float
        get() {
            return width.toFloat() / height.toFloat()
        }

    override fun toString(): String {
        return "[$left, $top, $right, $bottom](w=$width, h=$height, ratio=$aspectRatio)"
    }

    /**
     * canMerge
     */
    fun canMerge(segment: Segment, margin: Int): Boolean {

        val thisBounds = this.bounds
        val thatBounds = segment.bounds

        thatBounds.left = thatBounds.left - margin - 1
        if (thatBounds.left <= 0) thatBounds.left = 0

        thatBounds.top = thatBounds.top - margin - 1
        if (thatBounds.top <= 0) thatBounds.top = 0

        if (thisBounds.isIncludedIn(thatBounds) || thatBounds.isIncludedIn(thisBounds)) {
            return false
        }

        thatBounds.width += (margin + 1) * 2
        thatBounds.height += (margin + 1) * 2

        if (thisBounds.isOverlapping(thatBounds)) {
            return true
        }

        return false
    }

    /**
     * rect
     */
    val rect: Rectangle
        get() {
            return Rectangle(x = left, y = top, width = width, height = height)
        }

    /**
     * bounds
     */
    val bounds: Bounds
        get() {
            return rect.toBoundsWithRatio()
        }

    /**
     * rectOnScreenshotImage
     */
    val rectOnScreenshotImage: Rectangle
        get() {
            val offsetX = containerRect?.x ?: 0
            val offsetY = containerRect?.y ?: 0
            return Rectangle(x = left + offsetX, y = top + offsetY, width = width, height = height)
        }

    /**
     * boundOnScreenshotImage
     */
    val boundOnScreenshotImage: Bounds
        get() {
            return rectOnScreenshotImage.toBoundsWithRatio()
        }

    /**
     * createVisionElement
     */
    fun createVisionElement(

    ): VisionElement {

        val v = VisionElement()
        v.segment = this
        v.screenshotImage = screenshotImage
        v.screenshotFile = screenshotFile


        v.rect
        v.rect

        return v
    }

    /**
     * captureAndSave
     */
    fun captureAndSave(outputDirectory: String) {

        captureImage()

        this.segmentImageFile = outputDirectory.toPath().resolve("${this}.png").toString()
        try {
            UtilImageIO.saveImage(segmentImage, segmentImageFile)
        } catch (t: Throwable) {
            printWarn(t.toString())
        }
    }

    /**
     * captureImage
     */
    fun captureImage() {

        if (containerImage == null) {
            throw IllegalStateException("containerImage is null")
        }

        val cImage = containerImage!!

        var left = left - segmentMargin
        if (left < 0) left = 0

        var top = top - segmentMargin
        if (top < 0) top = 0

        var right = left + width + segmentMargin * 2
        if (right > cImage.width - 1) {
            right = cImage.width - 1
        }

        var bottom = top + height + segmentMargin * 2
        if (bottom > cImage.height - 1) {
            bottom = cImage.height - 1
        }

        val width = right - left + 1
        val height = bottom - top + 1

        this.segmentImage = cImage.getSubimage(left, top, width, height)
    }

}