package shirates.core.utility.image

import java.awt.image.BufferedImage

class CropInfo(
    var rect: Rectangle = Rectangle(),
    var trimObject: TrimObject = TrimObject()
) {

    var originalImage: BufferedImage? = null

    var croppedImage: BufferedImage? = null
    var croppedImageFile: String? = null


    val isCropped: Boolean
        get() {
            return croppedImage != null
        }

    val trimmedRect: Rectangle
        get() {
            return rect.trimBy(this.trimObject)
        }
}