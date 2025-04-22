package shirates.core.vision.driver

import shirates.core.driver.TestMode
import shirates.core.vision.VisionElement
import shirates.core.vision.VisionServerProxy
import shirates.core.vision.configration.repository.VisionClassifierRepository
import shirates.core.vision.result.DistanceResult
import shirates.core.vision.utility.label.LabelUtility

/**
 * classifyFull
 */
fun VisionElement.classifyFull(
    classifierName: String = "DefaultClassifier"
): String {

    if (TestMode.isNoLoadRun) {
        return ""
    }
    if (this.image == null) {
        return ""
    }
    if (this.imageFile == null) {
        this.saveImage()
    }

    val result = VisionServerProxy.classifyImageWithShard(
        inputFile = this.imageFile!!,
        classifierName = classifierName,
    )
    val classifierRepository = VisionClassifierRepository.getClassifier(classifierName)
    val classifications = result.getCandidates(classifierRepository.shardCount)
    if (classifications.isEmpty()) {
        return "?"
    }
    if (classifications.count() == 1) {
        val id = classifications.first().identifier
        val label = LabelUtility.getShortLabel(fullLabel = id)
        return label
    }

    val distanceList = mutableListOf<LabelDistance>()

    for (classification in classifications) {
        val label = LabelUtility.getShortLabel(fullLabel = classification.identifier)
        val files = classifierRepository.getFiles(label = label)
        for (file in files) {
            val distanceResult = VisionServerProxy.getDistance(imageFile1 = file, imageFile2 = this.imageFile!!)
            val labelDistance = LabelDistance(label = label, distanceResult = distanceResult)
            distanceList.add(labelDistance)
        }
    }
    distanceList.sortBy { it.distanceResult.distance }
    val first = distanceList.firstOrNull() ?: return "?"
    return first.label
}

private class LabelDistance(
    val label: String,
    val distanceResult: DistanceResult
) {
    override fun toString(): String {
        return "label=$label, distance=${distanceResult.distance}, imageFile1=${distanceResult.imageFile1}, imageFile2=${distanceResult.imageFile2}"
    }
}

/**
 * classify
 */
fun VisionElement.classify(
    classifierName: String = "DefaultClassifier"
): String {

    if (TestMode.isNoLoadRun) {
        return ""
    }

    val fullLabel = classifyFull(classifierName = classifierName)
    return LabelUtility.getShortLabel(fullLabel)
}