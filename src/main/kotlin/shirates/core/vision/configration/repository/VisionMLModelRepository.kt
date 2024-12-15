package shirates.core.vision.configration.repository

import shirates.core.configuration.PropertiesManager
import shirates.core.exception.TestConfigException
import shirates.core.utility.toPath
import java.nio.file.Files
import kotlin.io.path.name

object VisionMLModelRepository {

    val mlmodelClassifiers = mutableMapOf<String, VisionClassifierRepository>()

    /**
     * setup
     */
    fun setup(
        mlmodelsDirectory: String = PropertiesManager.visionDirectory.toPath().resolve("mlmodels").toString()
    ) {
        if (Files.exists(mlmodelsDirectory.toPath()).not()) {
            throw TestConfigException("Directory not found. (mlmodelsDirectory=$mlmodelsDirectory)")
        }

        val dirs = mlmodelsDirectory.toPath().toFile().walkTopDown()
            .filter { it.extension == "swift" }.map { it.parentFile.toString() }.toList()

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
     * generalClassifierRepository
     */
    val generalClassifierRepository: VisionClassifierRepository
        get() {
            return getRepository("GeneralClassifier")
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
     * checkboxStateClassifierRepository
     */
    val checkboxStateClassifierRepository: VisionClassifierRepository
        get() {
            return getRepository("CheckboxStateClassifier")
        }

    /**
     * radioButtonStateClassifierRepository
     */
    val radioButtonStateClassifierRepository: VisionClassifierRepository
        get() {
            return getRepository("RadioButtonStateClassifier")
        }

    /**
     * switchStateClassifierRepository
     */
    val switchStateClassifierRepository: VisionClassifierRepository
        get() {
            return getRepository("SwitchStateClassifier")
        }

}