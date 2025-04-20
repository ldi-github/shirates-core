package shirates.core.vision.unittest.configuration.repository

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.driver.TestMode
import shirates.core.exception.TestConfigException
import shirates.core.utility.toPath
import shirates.core.vision.configration.repository.VisionClassifierRepository

class VisionClassifierRepositoryTest {

    @Test
    fun setup_valid() {

        // Arrange
        val classifier = VisionClassifierRepository.setupClassifier(
            "valid",
            createBinary = null,
            visionDirectory = "unitTestData/files/vision",
            force = true
        )
        // Act
        classifier.setup(force = true)
        // Assert
        val labelMap = classifier.getLabelInfoMap()
        assertThat(labelMap.keys).contains("[Label1]", "[Label2]", "[Label3]")
        assertThat(labelMap["[Label1]"]?.files?.map { it.learningImageFile }).contains(
            "unitTestData/files/vision/classifiers/valid/[Label1]/img@a.png".toPath().toString(),
            "unitTestData/files/vision/classifiers/valid/[Label1]/img@i.png".toPath().toString(),
        )
        assertThat(labelMap["[Label2]"]?.files?.map { it.learningImageFile }).contains(
            "unitTestData/files/vision/classifiers/valid/[Label2]/img.png".toPath().toString(),
        )
        assertThat(labelMap["[Label3]"]?.files?.map { it.learningImageFile }).contains(
            "unitTestData/files/vision/classifiers/valid/subdirectory1/[Label3]/img@a.png".toPath().toString(),
            "unitTestData/files/vision/classifiers/valid/subdirectory1/[Label3]/img@i.png".toPath().toString(),
        )
    }

    @Test
    fun setup_invalid() {

        // Arrange
        val classifier = VisionClassifierRepository.setupClassifier(
            "invalid",
            createBinary = null,
            visionDirectory = "unitTestData/files/vision",
            force = true
        )
        // Act, Assert
        assertThatThrownBy {
            classifier.setup(force = true)
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessageStartingWith("Label directory is duplicated. A label can belong to only one directory. (label=[Label1], dirs=")
    }

    @Test
    fun getFile() {

        // Arrange
        val classifier = VisionClassifierRepository.setupClassifier(
            "valid",
            createBinary = null,
            visionDirectory = "unitTestData/files/vision",
            force = true
        )
        TestMode.runAsAndroid {
            run {
                // Act
                val file = classifier.getFile("[Label1]")
                // Assert
                assertThat(file).isEqualTo(
                    "unitTestData/files/vision/classifiers/valid/[Label1]/img@a.png".toPath().toString()
                )
            }
            run {
                // Act
                val file = classifier.getFile("[Label2]")
                // Assert
                assertThat(file).isEqualTo(
                    "unitTestData/files/vision/classifiers/valid/[Label2]/img.png".toPath().toString()
                )
            }
            run {
                // Act
                val file = classifier.getFile("[Label3]")
                // Assert
                assertThat(file).isEqualTo(
                    "unitTestData/files/vision/classifiers/valid/subdirectory1/[Label3]/img@a.png".toPath().toString()
                )
            }
        }
        TestMode.runAsIos {
            run {
                // Act
                val file = classifier.getFile("[Label1]")
                // Assert
                assertThat(file).isEqualTo(
                    "unitTestData/files/vision/classifiers/valid/[Label1]/img@i.png".toPath().toString()
                )
            }
            run {
                // Act
                val file = classifier.getFile("[Label2]")
                // Assert
                assertThat(file).isEqualTo(
                    "unitTestData/files/vision/classifiers/valid/[Label2]/img.png".toPath().toString()
                )
            }
            run {
                // Act
                val file = classifier.getFile("[Label3]")
                // Assert
                assertThat(file).isEqualTo(
                    "unitTestData/files/vision/classifiers/valid/subdirectory1/[Label3]/img@i.png".toPath().toString()
                )
            }
        }

    }
}