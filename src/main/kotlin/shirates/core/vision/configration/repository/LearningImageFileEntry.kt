package shirates.core.vision.configration.repository

import shirates.core.utility.file.parent
import shirates.core.utility.file.resolve
import shirates.core.utility.file.toFile
import shirates.core.utility.toPath
import kotlin.io.path.name

class LearningImageFileEntry(
    val learningImageFile: String,
    val visionClassifierShard: VisionClassifierShard,
) {
    /**
     * isTextIndex
     */
    val isTextIndex: Boolean
        get() {
            return learningImageFile.toPath().name.startsWith("#")
        }

    /**
     * visionLabelDirectory
     */
    val visionLabelDirectory: String
        get() {
            return learningImageFile.parent()
        }

    /**
     * label
     */
    val label: String
        get() {
            return visionLabelDirectory.toFile().name
        }

    /**
     * combinedLabel
     */
    val combinedLabel: String
        get() {
            val imageFilePath = learningImageFile.toPath().toString()
            val imageFileName = learningImageFile.toPath().name
            val classifierDirectoryPath = visionClassifierShard.visionClassifierDirectory
            val combinedLabel = imageFilePath.replace(classifierDirectoryPath, "").removeSuffix(imageFileName)
                .replace("/", "_").replace("\\", "_").trim('_')
            return combinedLabel
        }

    /**
     * testCombinedLabelDirectory
     */
    val testCombinedLabelDirectory: String
        get() {
            return visionClassifierShard.testDirectory.resolve(combinedLabel)
        }

    /**
     * trainingCombinedLabelDirectory
     */
    val trainingCombinedLabelDirectory: String
        get() {
            return visionClassifierShard.trainingDirectory.resolve(combinedLabel)
        }

    /**
     * trainingImageFile
     */
    val trainingImageFile: String
        get() {
            return trainingCombinedLabelDirectory.resolve(learningImageFile.toPath().name)
        }

    override fun toString(): String {
        return learningImageFile
    }
}