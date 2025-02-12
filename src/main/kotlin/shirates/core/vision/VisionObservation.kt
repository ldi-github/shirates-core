package shirates.core.vision

import shirates.core.logging.printWarn
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.image.BufferedImageUtility
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.cropImage
import shirates.core.utility.toPath
import shirates.core.vision.driver.VisionContext
import shirates.core.vision.driver.commandextension.helper.IRect
import java.awt.image.BufferedImage
import java.nio.file.Files

open class VisionObservation(
    open var screenshotFile: String? = CodeExecutionContext.lastScreenshotFile,
    open var screenshotImage: BufferedImage? = CodeExecutionContext.lastScreenshotImage,

    open var localRegionFile: String? = null,
    open var localRegionImage: BufferedImage? = null,

    open var localRegionX: Int,
    open var localRegionY: Int,
    open var rectOnLocalRegion: Rectangle? = null,

    open var horizontalMargin: Int = 0,
    open var verticalMargin: Int = 0,

    open var imageFile: String? = null
) : IRect {
    private var _image: BufferedImage? = null

    /**
     * rectOnScreen
     */
    val rectOnScreen: Rectangle?
        get() {
            val rect = rectOnLocalRegion ?: return null
            return Rectangle(
                left = localRegionX + rect.left,
                top = localRegionY + rect.top,
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

            if (imageFile != null && Files.exists(imageFile!!.toPath())) {
                if (Files.exists(imageFile!!.toPath())) {
                    _image = BufferedImageUtility.getBufferedImage(filePath = imageFile!!)
                    return _image
                } else {
                    printWarn("File not found: imageFile=$imageFile")
                }
            }

            // load screenshotImage from file
            if (screenshotImage == null && screenshotFile != null) {
                screenshotImage = BufferedImageUtility.getBufferedImage(filePath = screenshotFile!!)
            }

            if (screenshotImage != null && rectOnScreen != null) {
                _image = screenshotImage!!.cropImage(
                    rect = rectOnScreen!!,
                    horizontalMargin = horizontalMargin,
                    verticalMargin = verticalMargin
                )
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
        c.localRegionFile = this.localRegionFile
        c.localRegionImage = this.localRegionImage
        c.localRegionX = this.localRegionX
        c.localRegionY = this.localRegionY
        c.rectOnLocalRegion = this.rectOnLocalRegion
        c.horizontalMargin = this.horizontalMargin
        c.verticalMargin = this.verticalMargin
        c.imageFile = this.imageFile
        if (this is RecognizeTextObservation) {
            c.jsonString = this.jsonString
            c.recognizeTextObservations.add(this)
        }
        return c
    }

    /**
     * toVisionElement
     */
    fun toVisionElement(): VisionElement {

        val c = this.toVisionContext()

        val v = VisionElement(capture = false)
        v.visionContext = c

        v.observation = this
        return v
    }

    override fun getRectInfo(): Rectangle {
        return rectOnScreen ?: Rectangle()
    }
}