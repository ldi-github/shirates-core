package shirates.core.vision.driver

import shirates.core.driver.TestMode
import shirates.core.vision.VisionElement
import shirates.core.vision.VisionServerProxy
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
    val primaryClassifications = result.classifyImageResults.map { it.primaryClassification }
        .sortedByDescending { it.confidence }
    val first = primaryClassifications.first()

    return first.identifier
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