package shirates.core.vision

import shirates.core.logging.CodeExecutionContext
import shirates.core.utility.image.BufferedImageUtility
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.cropImage
import java.awt.image.BufferedImage

open class VisionObservation(
    open val rectOnLocalRegionImage: Rectangle? = null,
    open var localRegionImage: BufferedImage? = null,
    open var localRegionFile: String? = null,
    open var rectOnScreenshotImage: Rectangle? = null,
    open var screenshotImage: BufferedImage? = CodeExecutionContext.lastScreenshotImage,
    open var screenshotFile: String? = CodeExecutionContext.lastScreenshotFile,
) {
    private var _image: BufferedImage? = null

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

        val v = VisionElement()
        v.observation = this
        v.screenshotImage = this.screenshotImage
        v.screenshotFile = this.screenshotFile
        return v
    }
}