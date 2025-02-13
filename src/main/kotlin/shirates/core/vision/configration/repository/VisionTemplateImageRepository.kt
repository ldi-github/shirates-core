package shirates.core.vision.configration.repository

import shirates.core.configuration.PropertiesManager
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
        binaryThreshold: Int = PropertiesManager.visionFindImageBinaryThreshold,
    ): BufferedImage? {

        if (imageFile.exists().not()) {
            return null
        }
        if (templateImageMap.containsKey(imageFile)) {
            return templateImageMap[imageFile]!!
        }

        val segmentContainer = SegmentContainer(
            mergeIncluded = true,
            containerImageFile = imageFile,
            containerX = 0,
            containerY = 0,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            skinThickness = skinThickness,
            binaryThreshold = binaryThreshold,
        ).split(segmentationPng = false)
//            .saveImages()

        val image = segmentContainer.segments.sortedByDescending { it.toRect().area }.firstOrNull()?.segmentImage
            ?: return null
        templateImageMap[imageFile] = image

        return image
    }
}