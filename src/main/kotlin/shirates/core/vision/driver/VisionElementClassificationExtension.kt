package shirates.core.vision.driver

import shirates.core.configuration.PropertiesManager
import shirates.core.exception.TestDriverException
import shirates.core.utility.file.resolve
import shirates.core.utility.string.normalize
import shirates.core.vision.VisionElement
import shirates.core.vision.VisionServerProxy
import java.text.Normalizer

/**
 * classifyFull
 */
fun VisionElement.classifyFull(
    classifierName: String = "DefaultClassifier"
): String {

    if (this.image == null) {
        throw TestDriverException("Failed to classify. `image` is not set. (VisionElement:$this)")
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

    return result.primaryClassification.identifier.normalize(Normalizer.Form.NFKC)
}

/**
 * classify
 */
fun VisionElement.classify(
    classifierName: String = "DefaultClassifier"
): String {

    val fullLabel = classifyFull(classifierName = classifierName)
    val lastIndex = fullLabel.lastIndexOf('[')
    if (lastIndex == -1) {
        return fullLabel
    }
    val label = fullLabel.substring(lastIndex)
    return label
}