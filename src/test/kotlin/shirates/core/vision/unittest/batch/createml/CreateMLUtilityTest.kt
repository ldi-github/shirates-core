package shirates.core.vision.unittest.batch.createml

import okio.FileNotFoundException
import org.apache.commons.io.FileUtils
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
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

    private fun getFileListInClassifier(
        visionClassifierDirectory: String,
    ): String {

        val files = visionClassifierDirectory.toFile().walkTopDown()
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
        val classifiersDirectory = "build/vision/classifiers"

        fun getSkipMessageCount(): Int {
            return TestLog.lines.count { it.message.contains("Learning skipped. Updated file not found.") }
        }

        run {
            // Arrange
            FileUtils.deleteDirectory(classifiersDirectory.toFile())
            // Action
            CreateMLUtility.runLearning(
                visionDirectory = visionDirectory,
                force = true
            )
            // Assert
            val buildClassifierDirectory = classifiersDirectory.resolve("CheckStateClassifier")
            val files = buildClassifierDirectory.toFile().walkTopDown()
                .map { it.toString().substringAfter("/shirates-core/").trim() }.toList()
            val expected = """
build/vision/classifiers/CheckStateClassifier
build/vision/classifiers/CheckStateClassifier/MLImageClassifier.swift
build/vision/classifiers/CheckStateClassifier/test
build/vision/classifiers/CheckStateClassifier/test/[ON]
build/vision/classifiers/CheckStateClassifier/test/[ON]/radio_bright.png
build/vision/classifiers/CheckStateClassifier/test/[OFF]
build/vision/classifiers/CheckStateClassifier/test/[OFF]/radio_bright.png
build/vision/classifiers/CheckStateClassifier/CheckStateClassifier.mlmodel
build/vision/classifiers/CheckStateClassifier/training
build/vision/classifiers/CheckStateClassifier/training/[ON]
build/vision/classifiers/CheckStateClassifier/training/[ON]/radio_bright_binary2.png
build/vision/classifiers/CheckStateClassifier/training/[ON]/radio_bright_binary.png
build/vision/classifiers/CheckStateClassifier/training/[ON]/radio_bright.png
build/vision/classifiers/CheckStateClassifier/training/[OFF]
build/vision/classifiers/CheckStateClassifier/training/[OFF]/radio_bright_binary2.png
build/vision/classifiers/CheckStateClassifier/training/[OFF]/radio_bright_binary.png
build/vision/classifiers/CheckStateClassifier/training/[OFF]/radio_bright.png
build/vision/classifiers/CheckStateClassifier/createML.log
build/vision/classifiers/CheckStateClassifier/fileList.txt
""".trimIndent().split("\n").map { it.trim() }
            assertThat(files).containsSubsequence(expected)
            assertThat(getSkipMessageCount()).isEqualTo(0)
        }
        run {
            // Action
            CreateMLUtility.runLearning(
                visionDirectory = visionDirectory,
            )
            // Assert
            assertThat(getSkipMessageCount()).isEqualTo(2)  // skipped
        }
        run {
            // Arrange (Update file)
            val file = visionDirectory.resolve("classifiers/CheckStateClassifier/[OFF]/radio_bright.png")
            val image = BufferedImageUtility.getBufferedImage(file)
            image.saveImage(file = file)
            // Act
            CreateMLUtility.runLearning(
                visionDirectory = visionDirectory,
            )
            // Assert
            assertThat(getSkipMessageCount()).isEqualTo(3)  // executed
        }
        run {
            // Action
            CreateMLUtility.runLearning(
                visionDirectory = visionDirectory,
            )
            // Assert
            assertThat(getSkipMessageCount()).isEqualTo(5)  // skipped
        }
        run {
            // Act (force = true)
            CreateMLUtility.runLearning(
                visionDirectory = visionDirectory,
                force = true
            )
            // Assert
            assertThat(getSkipMessageCount()).isEqualTo(5)  // executed
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
                createBinary = false
            )
            // Assert
            assertThat(getWarningMessageCount()).isEqualTo(1)
        }
    }

    @Order(30)
    @Test
    fun runLearning_error() {

        run {
            // Arrange
            val visionDirectory = ""
            assertThatThrownBy {
                CreateMLUtility.runLearning(
                    visionDirectory = visionDirectory,
                    createBinary = false
                )
            }.isInstanceOf(FileNotFoundException::class.java)
                .hasMessage("visionDirectory is blank. Check the testrun properties file.")
        }
        run {
            // Arrange
            val visionDirectory = "no/exist/path"
            assertThatThrownBy {
                CreateMLUtility.runLearning(
                    visionDirectory = visionDirectory,
                    createBinary = false
                )
            }.isInstanceOf(FileNotFoundException::class.java)
                .hasMessage("visionDirectory not found. (visionDirectory=no/exist/path)")
        }
    }

}