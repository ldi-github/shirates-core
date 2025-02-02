package shirates.core.vision.batch

import shirates.core.utility.file.parent
import shirates.core.utility.file.resolve
import shirates.core.utility.file.toFile
import shirates.core.utility.toPath
import shirates.core.vision.batch.CreateMLUtility.classifiersDirectoryInWork
import kotlin.io.path.name

class MlModelImageFileEntry(
    val visionDirectory: String,
    val classifiersDirectoryInVision: String,
    val scriptFileInVision: String,
    val imageFileInVision: String,
    val workDirectory: String,
) {
    /**
     * vision directories/files
     */
    val classifierDirectoryInVision: String
        get() {
            return scriptFileInVision.parent()
        }
    val classifierName: String
        get() {
            return classifierDirectoryInVision.toFile().name
        }
    val labelDirectoryInVision: String
        get() {
            return imageFileInVision.parent()
        }
    val label: String
        get() {
            return labelDirectoryInVision.toFile().name
        }
    val combinedLabel: String
        get() {
            val imageFilePath = imageFileInVision.toPath().toString()
            val imageFileName = imageFileInVision.toPath().name
            val classifierDirectoryPath = classifierDirectoryInVision.toPath().toString()
            val combinedLabel = imageFilePath.replace(classifierDirectoryPath, "").removeSuffix(imageFileName)
                .replace("/", "").replace("\\", "")
            return combinedLabel
        }

    /**
     * work directories/files
     */
    val scriptFileInWork: String
        get() {
            return classifierDirectoryInWork.resolve(scriptFileInVision.toPath().name)
        }
    val classifierDirectoryInWork: String
        get() {
            return classifiersDirectoryInWork.resolve(classifierName)
        }

    /**
     * training directories/files
     */
    val trainingDirectory: String
        get() {
            return classifierDirectoryInWork.resolve("training")
        }
    val combinedLabelDirectoryInTraining: String
        get() {
            return trainingDirectory.resolve(combinedLabel)
        }
    val imageFileInTraining: String
        get() {
            return combinedLabelDirectoryInTraining.resolve(imageFileInVision.toPath().name)
        }

    /**
     * test directories/files
     */
    val testDirectory: String
        get() {
            return classifierDirectoryInWork.resolve("test")
        }
    val combinedLabelDirectoryInTest: String
        get() {
            return testDirectory.resolve(combinedLabel)
        }
    val imageFileInTest: String
        get() {
            return combinedLabelDirectoryInTest.resolve(imageFileInVision.toPath().name)
        }

    override fun toString(): String {
        return imageFileInVision
    }
}