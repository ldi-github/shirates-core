package shirates.core.vision.configration.repository

import shirates.core.driver.testContext
import shirates.core.utility.file.exists
import shirates.core.utility.image.SegmentContainer
import java.awt.image.BufferedImage

object VisionTemplateImageRepository {

    val templateImageMap = mutableMapOf<String, BufferedImage>()

    /**
     * getNormalizedTemplateImage
     */
    fun getNormalizedTemplateImage(
        imageFile: String,
        segmentMarginHorizontal: Int,
        segmentMarginVertical: Int,
        skinThickness: Int = 2,
        binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
        aspectRatioTolerance: Double = testContext.visionFindImageAspectRatioTolerance,
    ): BufferedImage? {

        if (imageFile.exists().not()) {
            return null
        }
        if (templateImageMap.containsKey(imageFile)) {
            return templateImageMap[imageFile]!!
        }

        val image = SegmentContainer.getNormalizedImage(
            imageFile = imageFile,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            skinThickness = skinThickness,
            binaryThreshold = binaryThreshold,
            aspectRatioTolerance = aspectRatioTolerance,
        ) ?: return null
        templateImageMap[imageFile] = image

        return image
    }
}