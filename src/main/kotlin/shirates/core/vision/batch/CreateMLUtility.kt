package shirates.core.vision.batch

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestMode
import shirates.core.utility.file.FileLockUtility.lockFile
import shirates.core.utility.file.exists
import shirates.core.utility.toPath
import shirates.core.vision.configration.repository.VisionClassifierRepository
import java.io.FileNotFoundException

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
        if (visionDirectory.isBlank()) {
            throw FileNotFoundException("visionDirectory is blank. Check the testrun properties file.")
        }
        if (visionDirectory.exists().not()) {
            throw FileNotFoundException("visionDirectory not found. (visionDirectory=$visionDirectory)")
        }
        if (TestMode.isRunningOnMacOS.not()) {
            throw NotImplementedError("CreateMLUtility is for only MacOS.")
        }

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