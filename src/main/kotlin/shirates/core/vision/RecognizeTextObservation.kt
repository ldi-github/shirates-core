package shirates.core.vision

import shirates.core.utility.image.Rectangle
import java.awt.image.BufferedImage

class RecognizeTextObservation(
    val text: String,
    val confidence: Float,
    val jsonString: String,
    val language: String?,

    override var screenshotFile: String?,
    override var screenshotImage: BufferedImage?,

    override var localRegionFile: String?,
    override var localRegionImage: BufferedImage?,

    override var localRegionX: Int,
    override var localRegionY: Int,

    override var rectOnLocalRegion: Rectangle?,

    ) : VisionObservation(

    screenshotFile = screenshotFile,
    screenshotImage = screenshotImage,

    localRegionFile = localRegionFile,
    localRegionImage = localRegionImage,

    localRegionX = localRegionX,
    localRegionY = localRegionY,
    rectOnLocalRegion = rectOnLocalRegion,
) {

    override fun toString(): String {
        return "text=\"$text\", rectOnLocalRegionImage=\"$rectOnLocalRegion\", confidence=\"$confidence\""
    }
}