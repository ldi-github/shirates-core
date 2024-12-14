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
    open var rectOnLocalRegionImage: Rectangle? = null,

    ) {
    private var _image: BufferedImage? = null

    /**
     * rectOnScreenshotImage
     */
    val rectOnScreenshotImage: Rectangle?
        get() {
            val rect = rectOnLocalRegionImage ?: return null
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
            if (localRegionImage != null && rectOnLocalRegionImage != null) {
                _image = localRegionImage!!.cropImage(rectOnLocalRegionImage!!)
                return _image
            }

            // load screenshotImage from file
            if (screenshotImage == null && screenshotFile != null) {
                screenshotImage = BufferedImageUtility.getBufferedImage(filePath = screenshotFile!!)
            }

            if (screenshotImage != null && rectOnScreenshotImage != null) {
                _image = screenshotImage!!.cropImage(rectOnScreenshotImage!!)
                return _image
            }

            return null
        }

    /**
     * createVisionElement
     */
    fun createVisionElement(): VisionElement {

        val c = VisionContext()
        c.screenshotFile = screenshotFile
        c.screenshotImage = screenshotImage

        c.localRegionFile = localRegionFile
        c.localRegionImage = localRegionImage

        c.localRegionX = localRegionX
        c.localRegionY = localRegionY
        c.rectOnLocalRegionImage = rectOnLocalRegionImage

        val v = VisionElement()
        v.visionContext = c
        v.observation = this
        return v
    }
}