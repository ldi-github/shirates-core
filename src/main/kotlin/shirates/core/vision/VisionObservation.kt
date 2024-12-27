package shirates.core.vision

import shirates.core.logging.CodeExecutionContext
import shirates.core.utility.image.BufferedImageUtility
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.cropImage
import shirates.core.vision.driver.VisionContext
import java.awt.image.BufferedImage

open class VisionObservation(
    open var screenshotFile: String? = CodeExecutionContext.lastScreenshotFile,
    open var screenshotImage: BufferedImage? = CodeExecutionContext.lastScreenshotImage,

    open var localRegionFile: String? = null,
    open var localRegionImage: BufferedImage? = null,

    open var localRegionX: Int,
    open var localRegionY: Int,
    open var rectOnLocalRegion: Rectangle? = null,

    ) {
    private var _image: BufferedImage? = null

    /**
     * rectOnScreen
     */
    val rectOnScreen: Rectangle?
        get() {
            val rect = rectOnLocalRegion ?: return null
            return Rectangle(
                x = localRegionX + rect.left,
                y = localRegionY + rect.top,
                width = rect.width,
                height = rect.height
            )
        }

    /**
     * image
     */
    val image: BufferedImage?
        get() {
            if (_image != null) {
                return _image
            }

            // load localRegionImage from file
            if (localRegionImage == null && localRegionFile != null) {
                localRegionImage = BufferedImageUtility.getBufferedImage(filePath = localRegionFile!!)
            }
            // crop localImage
            if (localRegionImage != null && rectOnLocalRegion != null) {
                _image = localRegionImage!!.cropImage(rectOnLocalRegion!!)
                return _image
            }

            // load screenshotImage from file
            if (screenshotImage == null && screenshotFile != null) {
                screenshotImage = BufferedImageUtility.getBufferedImage(filePath = screenshotFile!!)
            }

            if (screenshotImage != null && rectOnScreen != null) {
                _image = screenshotImage!!.cropImage(rectOnScreen!!)
                return _image
            }

            return null
        }

    /**
     * toVisionContext
     */
    fun toVisionContext(): VisionContext {

        val c = VisionContext(capture = false)
        c.screenshotFile = this.screenshotFile
        c.screenshotImage = this.screenshotImage
        c.localRegionX = this.localRegionX
        c.localRegionY = this.localRegionY
        c.localRegionFile = this.localRegionFile
        c.localRegionImage = this.localRegionImage
        c.rectOnLocalRegion = this.rectOnLocalRegion
        c.localRegionImage = this.localRegionImage
        if (this is RecognizeTextObservation) {
            c.language = this.language
            c.jsonString = this.jsonString
            c.recognizeTextObservations.add(this)
        }
        return c
    }

    /**
     * createVisionElement
     */
    fun createVisionElement(): VisionElement {

        val c = this.toVisionContext()

        val v = VisionElement()
        v.visionContext = c

        v.observation = this
        return v
    }
}