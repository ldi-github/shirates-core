package shirates.core.vision.configration.repository

import shirates.core.configuration.PropertiesManager
import shirates.core.exception.TestConfigException
import shirates.core.logging.printInfo
import shirates.core.utility.toPath
import java.nio.file.Files
import kotlin.io.path.name

object VisionMLModelRepository {

    val mlmodelClassifiers = mutableMapOf<String, VisionClassifierRepository>()

    /**
     * setup
     */
    fun setup(
        classifiersDirectory: String = PropertiesManager.visionBuildDirectory.toPath()
            .resolve("vision").resolve("classifiers").toString()
    ) {
        if (Files.exists(classifiersDirectory.toPath()).not()) {
            printInfo("Directory not found. (classifiersDirectory=$classifiersDirectory)")
            return
        }

        val dirs = classifiersDirectory.toPath().toFile().walkTopDown()
            .filter { it.extension == "mlmodel" }.map { it.parentFile.toString() }.toList()

        for (mlmodelDir in dirs) {
            val classifierName = mlmodelDir.toPath().name
            val repository = VisionClassifierRepository()
            repository.setup(mlmodelDir)
            mlmodelClassifiers[classifierName] = repository
        }
    }

    /**
     * getRepository
     */
    fun getRepository(
        classifierName: String
    ): VisionClassifierRepository {
        if (mlmodelClassifiers.containsKey(classifierName).not()) {
            throw TestConfigException("Classifier not found. (classifierName=$classifierName)")
        }
        return mlmodelClassifiers[classifierName]!!
    }

    /**
     * defaultClassifierRepository
     */
    val defaultClassifierRepository: VisionClassifierRepository
        get() {
            return getRepository("DefaultClassifier")
        }

    /**
     * screenClassifierRepository
     */
    val screenClassifierRepository: VisionClassifierRepository
        get() {
            return getRepository("ScreenClassifier")
        }

    /**
     * buttonStateClassifierRepository
     */
    val buttonStateClassifierRepository: VisionClassifierRepository
        get() {
            return getRepository("ButtonStateClassifier")
        }

    /**
     * checkStateClassifierRepository
     */
    val checkStateClassifierRepository: VisionClassifierRepository
        get() {
            return getRepository("CheckStateClassifier")
        }

//    /**
//     * radioButtonStateClassifierRepository
//     */
//    val radioButtonStateClassifierRepository: VisionClassifierRepository
//        get() {
//            return getRepository("RadioButtonStateClassifier")
//        }
//
//    /**
//     * switchStateClassifierRepository
//     */
//    val switchStateClassifierRepository: VisionClassifierRepository
//        get() {
//            return getRepository("SwitchStateClassifier")
//        }

}