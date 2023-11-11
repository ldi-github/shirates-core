package shirates.core.utility.image

import java.awt.image.BufferedImage

object ImageMatchUtility {

    /**
     * evaluateImageEqualsTo
     */
    fun evaluateImageEqualsTo(
        image: BufferedImage?,
        templateImage: BufferedImage?,
        threshold: Double
    ): ImageMatchResult {

        val result = BoofCVUtility.evaluateImageEqualsTo(
            image = image,
            templateImage = templateImage,
            threshold = threshold
        )
        if (image != null && templateImage != null) {
            // Allow up to 0.01 difference in image height
            val widthRatio = Math.abs(image.width - templateImage.width).toDouble() / templateImage.width
            if (widthRatio > 0.01) {
                result.result = false
            }

            // Allow up to 0.01 difference in image width
            val heightRatio = Math.abs(image.height - templateImage.height).toDouble() / templateImage.height
            if (heightRatio > 0.01) {
                result.result = false
            }
        }
        return result
    }

    /**
     * evaluateImageContainedIn
     */
    fun evaluateImageContainedIn(
        scale: Double,
        image: BufferedImage?,
        templateImage: BufferedImage?,
        threshold: Double
    ): ImageMatchResult {

        return BoofCVUtility.evaluateImageContainedIn(
            image = image,
            templateImage = templateImage,
            scale = scale,
            threshold = threshold
        )
    }
}