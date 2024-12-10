package shirates.core.vision.driver

import shirates.core.exception.TestDriverException
import shirates.core.logging.TestLog
import shirates.core.utility.toPath
import shirates.core.vision.SrvisionProxy
import shirates.core.vision.VisionElement

fun VisionElement.classify(
    mlmodelFile: String = "vision/mlmodels/GeneralClassifier/GeneralClassifier.mlmodel".toPath().toString(),
): String {

    if (this.imageFile == null) {
        throw TestDriverException("Failed to classify. `imageFile` is not set. (VisionElement:$this)")
    }

    val result = SrvisionProxy.callImageClassifier(
        inputFile = TestLog.directoryForLog.resolve(this.imageFile!!).toString(),
        mlmodelFile = mlmodelFile,
    )

    return result.primaryClassification.identifier
}