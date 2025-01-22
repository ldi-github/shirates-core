package shirates.core.vision.driver.commandextension

import shirates.core.logging.TestLog
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionServerProxy


private fun VisionDrive.classifyCore(
    imageFile: String,
    mlmodelFile: String
): String {

    if (imageFile.isBlank()) {
        throw IllegalArgumentException("imageFile is blank.")
    }

    screenshot()

    val result = VisionServerProxy.classifyImage(
        inputFile = TestLog.directoryForLog.resolve(imageFile).toString(),
        mlmodelFile = mlmodelFile,
    )

    return result.primaryClassification.identifier
}
