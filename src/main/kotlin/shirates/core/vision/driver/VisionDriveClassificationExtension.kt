package shirates.core.vision.driver

import shirates.core.logging.TestLog
import shirates.core.utility.toPath
import shirates.core.vision.SrvisionProxy
import shirates.core.vision.VisionDrive


/**
 * classify
 */
fun VisionDrive.classify(
    imageFile: String,
    mlmodelFile: String = "vision/mlmodels/GeneralClassifier/GeneralClassifier.mlmodel".toPath().toString(),
): String {

    val result = SrvisionProxy.callImageClassifier(
        inputFile = TestLog.directoryForLog.resolve(imageFile).toString(),
        mlmodelFile = mlmodelFile,
    )

    return result.primaryClassification.identifier
}

/**
 * classifyScreen
 */
fun VisionDrive.classifyScreen(
    imageFile: String
): String {

    val mlmodelFile = "vision/mlmodels/ScreenClassifier/ScreenClassifier.mlmodel".toPath().toString()

    return classify(
        imageFile = imageFile,
        mlmodelFile = mlmodelFile,
    )
}
