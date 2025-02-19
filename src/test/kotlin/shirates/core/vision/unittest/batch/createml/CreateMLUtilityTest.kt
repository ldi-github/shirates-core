package shirates.core.vision.unittest.batch.createml

import org.apache.commons.io.FileUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.logging.TestLog
import shirates.core.utility.file.resolve
import shirates.core.utility.file.toFile
import shirates.core.utility.format
import shirates.core.utility.image.BufferedImageUtility
import shirates.core.utility.image.saveImage
import shirates.core.vision.batch.CreateMLUtility
import java.util.*

class CreateMLUtilityTest {

    private fun getFileListInClassifiersDirectoryInVision(
        classifiersDirectoryInVision: String,
    ): String {

        val files = classifiersDirectoryInVision.toFile().walkTopDown()
            .filter { it.isFile && it.name != ".DS_Store" }
            .map { "${Date(it.lastModified()).format("yyyy/MM/dd HH:mm:ss.SSS")} ${it}" }
        val result = files.joinToString("\n")
        return result
    }

    @Order(10)
    @Test
    fun runLearning() {

        // Arrange
        val visionDirectory = "unitTestData/vision/batch/leaning1/vision1"
        val visionDirectoryInWork = "build/tmp/vision1"

        fun getSkipMessageCount(): Int {
            return TestLog.lines.count {
                it.message.contains("Learning skipped. Updated file not found. (visionDirectory=$visionDirectory)")
            }
        }

        run {
            // Arrange
            FileUtils.deleteDirectory(visionDirectoryInWork.toFile())
            // Action
            CreateMLUtility.runLearning(
                visionDirectory = visionDirectory,
                visionDirectoryInWork = visionDirectoryInWork,
            )
            // Assert
            val fileList = getFileListInClassifiersDirectoryInVision(
                classifiersDirectoryInVision = "build/tmp/vision1/classifiers",
            ).split("\n").map { it.substringAfter("/shirates-core/").trim() }
            assertThat(fileList.count()).isEqualTo(19)
            val expected = """
build/tmp/vision1/classifiers/CheckStateClassifier/MLImageClassifier.swift
build/tmp/vision1/classifiers/CheckStateClassifier/test/[ON]/radio_bright.png
build/tmp/vision1/classifiers/CheckStateClassifier/test/[OFF]/radio_bright.png
build/tmp/vision1/classifiers/CheckStateClassifier/CheckStateClassifier.mlmodel
build/tmp/vision1/classifiers/CheckStateClassifier/training/[ON]/radio_bright_binary.png
build/tmp/vision1/classifiers/CheckStateClassifier/training/[ON]/radio_bright.png
build/tmp/vision1/classifiers/CheckStateClassifier/training/[OFF]/radio_bright_binary.png
build/tmp/vision1/classifiers/CheckStateClassifier/training/[OFF]/radio_bright.png
build/tmp/vision1/classifiers/CheckStateClassifier/createML.log
build/tmp/vision1/classifiers/DefaultClassifier/MLImageClassifier.swift
build/tmp/vision1/classifiers/DefaultClassifier/test/@a_[Android Files App]_[Images Button]/img.png
build/tmp/vision1/classifiers/DefaultClassifier/test/@i_[iOS Settings App]_[Accessibility Icon]/img.png
build/tmp/vision1/classifiers/DefaultClassifier/training/@a_[Android Files App]_[Images Button]/img_binary.png
build/tmp/vision1/classifiers/DefaultClassifier/training/@a_[Android Files App]_[Images Button]/img.png
build/tmp/vision1/classifiers/DefaultClassifier/training/@i_[iOS Settings App]_[Accessibility Icon]/img_binary.png
build/tmp/vision1/classifiers/DefaultClassifier/training/@i_[iOS Settings App]_[Accessibility Icon]/img.png
build/tmp/vision1/classifiers/DefaultClassifier/DefaultClassifier.mlmodel
build/tmp/vision1/classifiers/DefaultClassifier/createML.log
build/tmp/vision1/classifiers/fileList.txt
""".trimIndent().split("\n").map { it.trim() }
            assertThat(fileList).containsSubsequence(expected)
            assertThat(getSkipMessageCount()).isEqualTo(0)
        }
        run {
            // Action
            CreateMLUtility.runLearning(
                visionDirectory = visionDirectory,
                visionDirectoryInWork = visionDirectoryInWork,
            )
            // Assert
            assertThat(getSkipMessageCount()).isEqualTo(1)  // skipped
        }
        run {
            // Arrange (Update file)
            val file = visionDirectory.resolve("classifiers/CheckStateClassifier/[OFF]/radio_bright.png")
            val image = BufferedImageUtility.getBufferedImage(file)
            image.saveImage(file = file)
            // Act
            CreateMLUtility.runLearning(
                visionDirectory = visionDirectory,
                visionDirectoryInWork = visionDirectoryInWork,
            )
            // Assert
            assertThat(getSkipMessageCount()).isEqualTo(1)  // executed
        }
        run {
            // Action
            CreateMLUtility.runLearning(
                visionDirectory = visionDirectory,
                visionDirectoryInWork = visionDirectoryInWork,
            )
            // Assert
            assertThat(getSkipMessageCount()).isEqualTo(2)  // skipped
        }
        run {
            // Act (force = true)
            CreateMLUtility.runLearning(
                visionDirectory = visionDirectory,
                visionDirectoryInWork = visionDirectoryInWork,
                force = true
            )
            // Assert
            assertThat(getSkipMessageCount()).isEqualTo(2)  // executed
        }
    }

    @Order(20)
    @Test
    fun runLearning_warn() {

        fun getWarningMessageCount(): Int {
            return TestLog.lines.count {
                it.message.contains("CAUTION!  Learning has a problem. Accuracy is not 100%.")
            }
        }

        // Arrange
        val visionDirectory = "unitTestData/vision/batch/learning_error/vision_warn1"
        val visionDirectoryInWork = "build/tmp/vision1_error"
        assertThat(getWarningMessageCount()).isEqualTo(0)

        run {
            // Arrange
            FileUtils.deleteDirectory(visionDirectoryInWork.toFile())
            // Action
            CreateMLUtility.runLearning(
                visionDirectory = visionDirectory,
                visionDirectoryInWork = visionDirectoryInWork,
                createBinary = false
            )
            // Assert
            assertThat(getWarningMessageCount()).isEqualTo(1)
        }
    }

}