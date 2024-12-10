package shirates.core.vision

import shirates.core.utility.image.Rectangle
import java.awt.image.BufferedImage

class RecognizeTextObservation(
    val text: String,
    val confidence: Float,
    val jsonString: String,
    override val rectOnLocalRegionImage: Rectangle?,
    override var localRegionImage: BufferedImage?,
    override var localRegionFile: String?,
    override var rectOnScreenshotImage: Rectangle?,
    override var screenshotImage: BufferedImage?,
    override var screenshotFile: String?,
) : VisionObservation(
    rectOnLocalRegionImage = rectOnLocalRegionImage,
    localRegionImage = localRegionImage,
    localRegionFile = localRegionFile,
    rectOnScreenshotImage = rectOnScreenshotImage,
    screenshotImage = screenshotImage,
    screenshotFile = screenshotFile,
) {

    override fun toString(): String {
        return "text=\"$text\", rect=\"$rectOnLocalRegionImage\", confidence=\"$confidence\""
    }
}