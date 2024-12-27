package shirates.core.utility.image

import boofcv.io.image.UtilImageIO
import shirates.core.driver.Bounds
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.TestLog
import shirates.core.logging.printWarn
import shirates.core.utility.toPath
import shirates.core.vision.RecognizeTextObservation
import shirates.core.vision.VisionElement
import java.awt.image.BufferedImage
import java.nio.file.Files

class Segment(
    var left: Int = 0,
    var top: Int = 0,
    var width: Int,
    var height: Int,
    var container: SegmentContainer? = null,
    var screenshotImage: BufferedImage? = CodeExecutionContext.lastScreenshotImage,
    var screenshotFile: String? = CodeExecutionContext.lastScreenshotFile,
    var saveWithMargin: Boolean = true,
) {
    var recognizeTextObservation: RecognizeTextObservation? = null

    val text: String
        get() {
            return recognizeTextObservation?.text ?: ""
        }

    var segmentImage: BufferedImage? = null
        get() {
            if (field == null) {
                if (screenshotImage != null) {
                    captureImageWithMargin()
                } else if (segmentImageFile != null && Files.exists(segmentImageFile!!.toPath()).not()) {
                    field = BufferedImageUtility.getBufferedImage(segmentImageFile!!)
                }
            }
            return field
        }

    var segmentImageFile: String? = null

    var segmentMarginHorizontal: Int
        get() {
            if (container != null) {
                return container!!.segmentMarginHorizontal
            }
            return _segmentMarginHorizontal ?: 0
        }
        set(value) {
            _segmentMarginHorizontal = value
        }
    private var _segmentMarginHorizontal: Int? = null

    var segmentMarginVertical: Int
        get() {
            if (container != null) {
                return container!!.segmentMarginVertical
            }
            return _segmentMarginVertical ?: 0
        }
        set(value) {
            _segmentMarginVertical = value
        }
    private var _segmentMarginVertical: Int? = null

    val containerImage: BufferedImage?
        get() {
            return container?.containerImage
        }

    val containerX: Int
        get() {
            return container?.containerX ?: 0
        }

    val containerY: Int
        get() {
            return container?.containerY ?: 0
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
        return "[$left, $top, $right, $bottom](w=$width, h=$height, ratio=$aspectRatio, text=`$text`)"
    }

    /**
     * canMerge
     */
    fun canMerge(
        segment: Segment,
        marginHorizontal: Int,
        marginVertical: Int,
        mergeIncluded: Boolean = false,
    ): Boolean {

        val thisBounds = this.boundsOnSegmentContainer
        val thatBounds = segment.boundsOnSegmentContainer

        // Margin offset
        thatBounds.left = thatBounds.left - marginHorizontal - 1
        if (thatBounds.left <= 0) thatBounds.left = 0

        thatBounds.top = thatBounds.top - marginVertical - 1
        if (thatBounds.top <= 0) thatBounds.top = 0

        if (mergeIncluded.not()) {
            if (thisBounds.isIncludedIn(thatBounds) || thatBounds.isIncludedIn(thisBounds)) {
                return false
            }
        }

        thatBounds.width += (marginHorizontal + 1) * 2
        thatBounds.height += (marginVertical + 1) * 2

        if (thisBounds.isOverlapping(thatBounds)) {
            return true
        }

        return false
    }

    /**
     * rectOnSegmentContainer
     */
    val rectOnSegmentContainer: Rectangle
        get() {
            return Rectangle(x = left, y = top, width = width, height = height)
        }

    /**
     * boundsOnSegmentContainer
     */
    val boundsOnSegmentContainer: Bounds
        get() {
            return rectOnSegmentContainer.toBoundsWithRatio()
        }

    /**
     * rectOnScreen
     */
    val rectOnScreen: Rectangle
        get() {
            return Rectangle(x = containerX + left, y = containerY + top, width = width, height = height)
        }

    /**
     * toRect
     */
    fun toRect(): Rectangle {
        return Rectangle(x = left, y = top, width = width, height = height)
    }

    /**
     * createVisionElement
     */
    fun createVisionElement(): VisionElement {

        val v = VisionElement()
        v.segment = this

        v.visionContext.screenshotImage = this.screenshotImage
        v.visionContext.screenshotFile = this.screenshotFile

        v.visionContext.localRegionImage = this.screenshotImage
        v.visionContext.localRegionFile = this.screenshotFile

        v.visionContext.localRegionX = this.containerX
        v.visionContext.localRegionY = this.containerY
        v.visionContext.rectOnLocalRegion = this.rectOnSegmentContainer
        v.visionContext.imageMarginHorizontal = this.segmentMarginHorizontal
        v.visionContext.imageMarginVertical = this.segmentMarginVertical
        v.visionContext.image
        v.visionContext.rectOnScreen
        if (this.recognizeTextObservation != null) {
            v.visionContext.recognizeTextObservations.add(this.recognizeTextObservation!!)
        }

        return v
    }

    /**
     * captureAndSave
     */
    fun captureAndSave(outputDirectory: String? = TestLog.directoryForLog.toString()) {

        if (saveWithMargin) {
            captureImageWithMargin()
        }

        this.segmentImageFile = outputDirectory.toPath()
            .resolve("${this}_hmargin=${segmentMarginHorizontal}_vmargin=${segmentMarginVertical}.png").toString()
        try {
            UtilImageIO.saveImage(segmentImage, segmentImageFile)
        } catch (t: Throwable) {
            printWarn(t.toString())
        }
    }

    /**
     * captureImageWithMargin
     */
    fun captureImageWithMargin() {

        if (containerImage == null) {
            throw IllegalStateException("containerImage is null")
        }

        if (saveWithMargin.not()) return

        val cImage = containerImage!!

        var left = left - segmentMarginHorizontal
        if (left < 0) left = 0

        var top = top - segmentMarginVertical
        if (top < 0) top = 0

        var right = left + width + segmentMarginHorizontal * 2
        if (right > cImage.width - 1) {
            right = cImage.width - 1
        }

        var bottom = top + height + segmentMarginVertical * 2
        if (bottom > cImage.height - 1) {
            bottom = cImage.height - 1
        }

        val width = right - left + 1
        val height = bottom - top + 1

        this.segmentImage = cImage.getSubimage(left, top, width, height)
    }

}