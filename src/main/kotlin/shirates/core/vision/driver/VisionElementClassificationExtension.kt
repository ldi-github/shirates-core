package shirates.core.vision.driver

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestMode
import shirates.core.utility.file.resolve
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

    val mlmodelFile =
        PropertiesManager.visionBuildDirectory.resolve("vision/classifiers/$classifierName/$classifierName.mlmodel")

    val result = VisionServerProxy.classifyImage(
        inputFile = this.imageFile!!,
        mlmodelFile = mlmodelFile,
    )

    return result.primaryClassification.identifier
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