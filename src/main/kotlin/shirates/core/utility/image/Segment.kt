package shirates.core.utility.image

import boofcv.io.image.UtilImageIO
import shirates.core.driver.Bounds
import shirates.core.driver.testContext
import shirates.core.logging.TestLog
import shirates.core.logging.printWarn
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.toPath
import shirates.core.vision.RecognizeTextObservation
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.commandextension.helper.IRect
import java.awt.image.BufferedImage
import java.nio.file.Files

class Segment(
    override var left: Int = 0,
    override var top: Int = 0,
    override var width: Int,
    override var height: Int,
    var container: SegmentContainer? = null,
    var screenshotImage: BufferedImage? = CodeExecutionContext.lastScreenshotImage,
    var screenshotFile: String? = CodeExecutionContext.lastScreenshotFile,
    var croppingMargin: Int = testContext.segmentCroppingMargin,
) : IRect, RectangleInterface {
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
     * aspectRatio
     */
    val aspectRatio: Float
        get() {
            return width.toFloat() / height.toFloat()
        }

    override fun getRectInfo(): Rectangle {
        return rectOnScreen
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

        val thisRect = this
        val thatRect = segment.rectOnSegmentContainer

        // Margin offset
        thatRect.left = thatRect.left - marginHorizontal - 1
        if (thatRect.left <= 0) thatRect.left = 0

        thatRect.top = thatRect.top - marginVertical - 1
        if (thatRect.top <= 0) thatRect.top = 0

        if (mergeIncluded.not()) {
            if (thisRect.isIncludedIn(thatRect) || thatRect.isIncludedIn(thisRect)) {
                return false
            }
        }

        thatRect.width += (marginHorizontal + 1) * 2
        thatRect.height += (marginVertical + 1) * 2

        if (thisRect.isOverlapping(thatRect)) {
            return true
        }

        return false
    }

    /**
     * rectOnSegmentContainer
     */
    val rectOnSegmentContainer: Rectangle
        get() {
            return Rectangle(left = left, top = top, width = width, height = height)
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
            return Rectangle(left = containerX + left, top = containerY + top, width = width, height = height)
        }

    /**
     * toRect
     */
    fun toRect(): Rectangle {
        return Rectangle(left = left, top = top, width = width, height = height)
    }

    /**
     * createVisionElement
     */
    fun createVisionElement(): VisionElement {

        val v = VisionElement(capture = false)
        v.segment = this

        v.visionContext.screenshotImage = this.screenshotImage
        v.visionContext.screenshotFile = this.screenshotFile

        v.visionContext.localRegionImage = this.container?.containerImage
        v.visionContext.localRegionFile = this.container?.containerImageFile
        v.visionContext.localRegionX = this.containerX
        v.visionContext.localRegionY = this.containerY
        v.visionContext.rectOnLocalRegion = this.rectOnSegmentContainer

        v.visionContext.horizontalMargin = this.segmentMarginHorizontal
        v.visionContext.verticalMargin = this.segmentMarginVertical
        if (this.recognizeTextObservation != null) {
            v.visionContext.recognizeTextObservations.add(this.recognizeTextObservation!!)
        }
        return v
    }

    /**
     * captureAndSave
     */
    fun captureAndSave(
        outputDirectory: String? = TestLog.directoryForLog.toString()
    ): String {

        captureImageWithMargin()

        this.segmentImageFile = outputDirectory.toPath()
            .resolve("${this}_hmargin=${segmentMarginHorizontal}_vmargin=${segmentMarginVertical}.png").toString()
        try {
            UtilImageIO.saveImage(segmentImage, segmentImageFile)
        } catch (t: Throwable) {
            printWarn(t.toString())
            return ""
        }
        return segmentImageFile!!
    }

    /**
     * captureImageWithMargin
     */
    fun captureImageWithMargin() {

        if (containerImage == null) {
            throw IllegalStateException("containerImage is null")
        }

        val cImage = containerImage!!

        var left = left - croppingMargin
        if (left < 0) left = 0

        var top = top - croppingMargin
        if (top < 0) top = 0

        var right = left + width + croppingMargin * 2
        if (right > cImage.width - 1) {
            right = cImage.width - 1
        }

        var bottom = top + height + croppingMargin * 2
        if (bottom > cImage.height - 1) {
            bottom = cImage.height - 1
        }

        val width = right - left + 1
        val height = bottom - top + 1

        this.segmentImage = cImage.getSubimage(left, top, width, height)
    }

}