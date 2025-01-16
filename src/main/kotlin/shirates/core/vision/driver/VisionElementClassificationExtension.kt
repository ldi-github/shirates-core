package shirates.core.vision.driver

import shirates.core.configuration.PropertiesManager
import shirates.core.exception.TestDriverException
import shirates.core.utility.file.resolve
import shirates.core.vision.SrvisionProxy
import shirates.core.vision.VisionElement

fun VisionElement.classify(
    classifierName: String = "DefaultClassifier"
): String {

    if (this.image == null) {
        throw TestDriverException("Failed to classify. `image` is not set. (VisionElement:$this)")
    }
    if (this.imageFile == null) {
        this.saveImage()
    }

    val mlmodelFile =
        PropertiesManager.visionBuildDirectory.resolve("vision/mlmodels/$classifierName/$classifierName.mlmodel")

    val result = SrvisionProxy.classifyImage(
        inputFile = this.imageFile!!,
        mlmodelFile = mlmodelFile,
    )

    return result.primaryClassification.identifier
}