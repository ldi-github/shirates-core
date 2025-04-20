package shirates.core.vision.batch

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestMode
import shirates.core.utility.file.FileLockUtility.lockFile
import shirates.core.utility.file.exists
import shirates.core.utility.toPath
import shirates.core.vision.configration.repository.VisionClassifierRepository

object CreateMLUtility {

    const val ML_IMAGE_CLASSIFIER_SCRIPT = "MLImageClassifier.swift"
    const val RESOURCE_NAME_ML_IMAGE_CLASSIFIER_SCRIPT = "createml/$ML_IMAGE_CLASSIFIER_SCRIPT"

    /**
     * runLearning
     */
    fun runLearning(
        visionDirectory: String = PropertiesManager.visionDirectory,
        force: Boolean = false,
        createBinary: Boolean? = null
    ) {
        lockFile(filePath = visionDirectory.toPath()) {
            runLearningCore(
                visionDirectory = visionDirectory,
                force = force,
                createBinary = createBinary
            )
        }
    }

    private fun runLearningCore(
        visionDirectory: String,
        force: Boolean,
        createBinary: Boolean?
    ) {
        if (visionDirectory.exists().not()) {
            return
        }
        if (TestMode.isRunningOnMacOS.not()) {
            throw NotImplementedError("CreateMLUtility is for only MacOS.")
        }
        val classifierNames = VisionClassifierRepository.getClassifierNames(visionDirectory = visionDirectory)
        for (classifierName in classifierNames) {
            VisionClassifierRepository.runLearning(
                visionDirectory = visionDirectory,
                classifierName = classifierName,
                createBinary = createBinary,
                force = force,
            )
        }
    }

}