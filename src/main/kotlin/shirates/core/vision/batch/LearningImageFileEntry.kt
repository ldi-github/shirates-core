package shirates.core.vision.batch

import shirates.core.utility.file.parent
import shirates.core.utility.file.resolve
import shirates.core.utility.file.toFile
import shirates.core.utility.toPath
import kotlin.io.path.name

class LearningImageFileEntry(
    val imageFileInVision: String,
    val classifierEntry: ClassifierEntry,
) {
    /**
     * isTextIndex
     */
    val isTextIndex: Boolean
        get() {
            return imageFileInVision.toPath().name.startsWith("#")
        }

    /**
     * labelDirectoryInVision
     */
    val labelDirectoryInVision: String
        get() {
            return imageFileInVision.parent()
        }

    /**
     * label
     */
    val label: String
        get() {
            return labelDirectoryInVision.toFile().name
        }

    /**
     * combinedLabel
     */
    val combinedLabel: String
        get() {
            val imageFilePath = imageFileInVision.toPath().toString()
            val imageFileName = imageFileInVision.toPath().name
            val classifierDirectoryPath = classifierEntry.classifierDirectoryInVision
            val combinedLabel = imageFilePath.replace(classifierDirectoryPath, "").removeSuffix(imageFileName)
                .replace("/", "_").replace("\\", "_").trim('_')
            return combinedLabel
        }

    /**
     * combinedLabelDirectoryInTest
     */
    val combinedLabelDirectoryInTest: String
        get() {
            return classifierEntry.testDirectory.resolve(combinedLabel)
        }

    /**
     * combinedLabelDirectoryInTraining
     */
    val combinedLabelDirectoryInTraining: String
        get() {
            return classifierEntry.trainingDirectory.resolve(combinedLabel)
        }

    /**
     * imageFileInTraining
     */
    val imageFileInTraining: String
        get() {
            return combinedLabelDirectoryInTraining.resolve(imageFileInVision.toPath().name)
        }

    override fun toString(): String {
        return imageFileInVision
    }
}