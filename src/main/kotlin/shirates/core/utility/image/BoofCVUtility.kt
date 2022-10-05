package shirates.core.utility.image

import boofcv.factory.template.FactoryTemplateMatching
import boofcv.factory.template.TemplateScoreType
import boofcv.struct.feature.Match
import boofcv.struct.image.GrayF32
import shirates.core.configuration.PropertiesManager
import shirates.core.logging.TestLog
import java.awt.image.BufferedImage
import kotlin.math.absoluteValue

object BoofCVUtility {

    /**
     * findMatches
     */
    fun findMatches(
        image: GrayF32,
        templateImage: GrayF32,
        expectedMatches: Int = 1
    ): List<Match> {

        val matcher = FactoryTemplateMatching.createMatcher(
            TemplateScoreType.SUM_SQUARE_ERROR,
            GrayF32::class.java
        )

        matcher.setImage(image)
        matcher.setTemplate(templateImage, null, expectedMatches)
        matcher.process()
        return matcher.getResults().toList()
    }

    /**
     * evaluateImageEqualsTo
     */
    fun evaluateImageEqualsTo(
        image: BufferedImage?,
        templateImage: BufferedImage?,
        scale: Double,
        threshold: Double
    ): ImageMatchResult {
        if (image == null || templateImage == null || image.width != templateImage.width || image.height != templateImage.height) {
            return ImageMatchResult(
                result = false,
                scale = scale,
                threshold = threshold,
                image = image,
                templateImage = templateImage
            )
        }

        val match = findMatches(
            image = image.toGrayF32()!!,
            templateImage = templateImage.toGrayF32()!!
        ).firstOrNull()

        val result =
            if (match == null) false
            else (match.score.absoluteValue <= threshold)
        val imageMatchResult = ImageMatchResult(
            result = result,
            x = match?.x ?: Int.MIN_VALUE,
            y = match?.y ?: Int.MIN_VALUE,
            score = match?.score ?: Double.MIN_VALUE,
            scale = scale,
            threshold = threshold,
            image = image,
            templateImage = templateImage
        )
        TestLog.info(imageMatchResult.toString())
        return imageMatchResult
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
        if (scale < 0.0) {
            throw IllegalArgumentException("scale=$scale")
        }

        if (image == null || templateImage == null) {
            return ImageMatchResult(
                result = false,
                scale = scale,
                threshold = threshold,
                image = image,
                templateImage = templateImage
            )
        }
        val img1 = image.resizeNotSmallerThanTemplate(templateImage)

        val scaledImage = img1.resize(scale = scale)
        val scaledTemplateImage = templateImage.resize(scale = scale)

        /**
         * First match
         *
         * Get list of approximate　coordinate of the object
         * in the scaled (reduced) image in short time.
         */
        val matchedList = findMatches(
            image = scaledImage.toGrayF32()!!,
            templateImage = scaledTemplateImage.toGrayF32()!!,
            expectedMatches = PropertiesManager.imageMatchingCandidateCount
        )

        var result: Boolean
        val nonScaledTemplateImage = templateImage
        val sortedMatchList = matchedList.toMutableList().sortedBy { it.score.absoluteValue }

        for (m in sortedMatchList) {
            val startX = (m.x / scale).toInt()
            val startY = (m.y / scale).toInt()
            val nonScaledImage = img1.cut(
                x = startX,
                y = startY,
                width = nonScaledTemplateImage.width,
                height = nonScaledTemplateImage.height,
                margin = 10
            )

            /**
             * Second match
             *
             * Cut and reduce the size of image.
             * Get result of template matching in original scale.
             */
            val foundMatches = findMatches(
                image = nonScaledImage.toGrayF32()!!,
                templateImage = nonScaledTemplateImage.toGrayF32()!!
            )
            val match = foundMatches.firstOrNull()

            result =
                if (match == null) false
                else (match.score.absoluteValue <= threshold)
            val resultX = if (match == null) Int.MIN_VALUE else match.x + startX
            val resultY = if (match == null) Int.MIN_VALUE else match.y + startY
            val imageMatchResult = ImageMatchResult(
                result = result,
                x = resultX,
                y = resultY,
                score = match?.score ?: Double.MIN_VALUE,
                scale = scale,
                threshold = threshold,
                image = image,
                templateImage = templateImage
            )
            if (result || PropertiesManager.enableImageMatchDebugLog) {
                TestLog.info(imageMatchResult.toString())
            }
            if (result) {
                return imageMatchResult
            }
        }

        val imageMatchResult = ImageMatchResult(
            result = false,
            scale = scale,
            threshold = threshold,
            image = image,
            templateImage = templateImage
        )
        TestLog.info(imageMatchResult.toString())
        return imageMatchResult
    }
}