package shirates.core.vision.driver

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestMode
import shirates.core.logging.TestLog
import shirates.core.utility.image.SegmentContainer
import shirates.core.utility.image.saveImage
import shirates.core.vision.VisionElement
import shirates.core.vision.VisionServerProxy
import shirates.core.vision.configration.repository.VisionClassifierRepository
import shirates.core.vision.result.DistanceResult
import shirates.core.vision.utility.label.LabelUtility

/**
 * classifyFull
 */
fun VisionElement.classifyFull(
    classifierName: String = "DefaultClassifier",
    threshold: Double = PropertiesManager.visionFindImageThreshold
): String {

    if (TestMode.isNoLoadRun) {
        return ""
    }
    if (this.image == null) {
        return ""
    }

    val thisImageFileName = "${TestLog.currentLineNo}_${this.selector?.getEscapedFileName()}"
    if (this.imageFile == null) {
        this.saveImage(thisImageFileName)
    }

    val normalizedImage = SegmentContainer.getNormalizedImage(imageFile = this.imageFile!!)
    val normalizedImageFile = normalizedImage!!.saveImage(
        file = TestLog.directoryForLog.resolve("${thisImageFileName}_normalized").toString()
    )

    val result = VisionServerProxy.classifyImageWithShard(
        inputFile = normalizedImageFile,
        classifierName = classifierName,
    )
    val classifierRepository = VisionClassifierRepository.getClassifier(classifierName)
    val classifications = result.getCandidates(classifierRepository.shardCount)
    if (classifications.isEmpty()) {
        return "?"
    }
    if (classifications.count() == 1 && classifications[0].confidence == 1.0f) {
        val fullLabel = classifications[0].identifier
        return fullLabel
    }

    val distanceList = mutableListOf<LabelDistance>()

    for (classification in classifications) {
        val fullLabel = classification.identifier
        val label = LabelUtility.getShortLabel(fullLabel = fullLabel)
        val files = classifierRepository.getFiles(label = label)
        for (file in files) {
            val distanceResult = VisionServerProxy.getDistance(imageFile1 = file, imageFile2 = normalizedImageFile)
            val labelDistance = LabelDistance(label = label, fullLabel = fullLabel, distanceResult = distanceResult)
            distanceList.add(labelDistance)
        }
    }
    distanceList.sortBy { it.distanceResult.distance }
    val first = distanceList.firstOrNull { it.distanceResult.distance <= threshold } ?: return "?"
    return first.fullLabel
}

private class LabelDistance(
    val label: String,
    val fullLabel: String,
    val distanceResult: DistanceResult
) {
    override fun toString(): String {
        return "label=$label, distance=${distanceResult.distance}, imageFile1=${distanceResult.imageFile1}, imageFile2=${distanceResult.imageFile2}, fullLabel=$fullLabel"
    }
}

/**
 * classify
 */
fun VisionElement.classify(
    classifierName: String = "DefaultClassifier",
    threshold: Double = PropertiesManager.visionFindImageThreshold
): String {

    if (TestMode.isNoLoadRun) {
        return ""
    }

    val fullLabel = classifyFull(classifierName = classifierName, threshold = threshold)
    return LabelUtility.getShortLabel(fullLabel)
}