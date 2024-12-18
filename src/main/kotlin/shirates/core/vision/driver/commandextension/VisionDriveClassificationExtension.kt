package shirates.core.vision.driver.commandextension

import shirates.core.logging.TestLog
import shirates.core.utility.toPath
import shirates.core.vision.SrvisionProxy
import shirates.core.vision.VisionDrive


private fun VisionDrive.classifyCore(
    imageFile: String,
    mlmodelFile: String
): String {

    if (imageFile.isBlank()) {
        throw IllegalArgumentException("imageFile is blank.")
    }

    screenshot()

    val result = SrvisionProxy.callImageClassifier(
        inputFile = TestLog.directoryForLog.resolve(imageFile).toString(),
        mlmodelFile = mlmodelFile,
    )

    return result.primaryClassification.identifier
}

/**
 * classifyGeneral
 */
fun VisionDrive.classifyGeneral(
    imageFile: String
): String {

    val mlmodelFile = "vision/mlmodels/GeneralClassifier/GeneralClassifier.mlmodel".toPath().toString()

    return classifyCore(
        imageFile = imageFile,
        mlmodelFile = mlmodelFile,
    )
}
