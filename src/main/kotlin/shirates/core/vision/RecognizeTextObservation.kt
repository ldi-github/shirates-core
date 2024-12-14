package shirates.core.vision

import shirates.core.utility.image.Rectangle
import java.awt.image.BufferedImage

class RecognizeTextObservation(
    val text: String,
    val confidence: Float,
    val jsonString: String,

    override var screenshotFile: String?,
    override var screenshotImage: BufferedImage?,

    override var localRegionFile: String?,
    override var localRegionImage: BufferedImage?,

    override var localRegionX: Int,
    override var localRegionY: Int,
    override var rectOnLocalRegionImage: Rectangle?,

    ) : VisionObservation(

    screenshotFile = screenshotFile,
    screenshotImage = screenshotImage,

    localRegionFile = localRegionFile,
    localRegionImage = localRegionImage,

    localRegionX = localRegionX,
    localRegionY = localRegionY,
    rectOnLocalRegionImage = rectOnLocalRegionImage,
) {

    override fun toString(): String {
        return "text=\"$text\", rectOnLocalRegionImage=\"$rectOnLocalRegionImage\", confidence=\"$confidence\""
    }
}