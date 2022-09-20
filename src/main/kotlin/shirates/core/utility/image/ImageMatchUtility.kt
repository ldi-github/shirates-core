package shirates.core.utility.image

import java.awt.image.BufferedImage

object ImageMatchUtility {

    /**
     * evaluateImageEqualsTo
     */
    fun evaluateImageEqualsTo(
        image: BufferedImage?,
        templateImage: BufferedImage?,
        scale: Double,
        threshold: Double
    ): ImageMatchResult {

        return BoofCVUtility.evaluateImageEqualsTo(
            image = image,
            templateImage = templateImage,
            scale = scale,
            threshold = threshold
        )
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