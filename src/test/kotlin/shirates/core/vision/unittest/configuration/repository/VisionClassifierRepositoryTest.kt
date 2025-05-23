package shirates.core.vision.unittest.configuration.repository

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestMode
import shirates.core.exception.TestConfigException
import shirates.core.testcode.UnitTest
import shirates.core.utility.toPath
import shirates.core.vision.configration.repository.VisionClassifierRepository

class VisionClassifierRepositoryTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        PropertiesManager.clear()
    }

    @Test
    fun setup_valid() {

        // Arrange
        val classifier = VisionClassifierRepository.createClassifier(
            "valid",
            createBinary = null,
            visionDirectory = "unitTestData/files/vision",
        )
        // Act
        classifier.runLearning(force = true, setupOnly = true)
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
        PropertiesManager.setPropertyValue("visionClassifierShardNodeCount", "1")
        // Act, Assert
        assertThatThrownBy {
            val classifier = VisionClassifierRepository.createClassifier(
                "invalid",
                createBinary = null,
                visionDirectory = "unitTestData/files/vision",
            )
            classifier.runLearning(force = true, setupOnly = true)
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessageStartingWith("Label directory is duplicated. A label can belong to only one directory. (label=[Label1], dirs=")
    }

    @Test
    fun getFile() {

        // Arrange
        val classifier = VisionClassifierRepository.createClassifier(
            "valid",
            createBinary = null,
            visionDirectory = "unitTestData/files/vision",
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

    @Test
    fun getShardNodeCount() {

        // Arrange
        val v = VisionClassifierRepository
        run {
            // Act
            val actual = v.getShardNodeCount(classifierName = "CheckStateClassifier")
            // Assert
            assertThat(actual).isEqualTo(1)
        }
        run {
            // Act
            val actual = v.getShardNodeCount(classifierName = "ScreenClassifier")
            // Assert
            assertThat(actual).isEqualTo(1)
        }
        run {
            // Arrange
            PropertiesManager.setPropertyValue(
                "visionClassifierShardNodeCount",
                "DefaultClassifier=10:ScreenClassifier=20:ButtonStateClassifier=30:CheckStateClassifier=40"
            )
            // Act, Assert
            assertThat(v.getShardNodeCount(classifierName = "DefaultClassifier")).isEqualTo(10)
            assertThat(v.getShardNodeCount(classifierName = "ScreenClassifier")).isEqualTo(20)
            assertThat(v.getShardNodeCount(classifierName = "ButtonStateClassifier")).isEqualTo(30)
            assertThat(v.getShardNodeCount(classifierName = "CheckStateClassifier")).isEqualTo(40)
            assertThat(v.getShardNodeCount(classifierName = "")).isEqualTo(1)
            assertThat(v.getShardNodeCount(classifierName = "not registered classifier")).isEqualTo(1)
        }
        run {
            // Arrange
            PropertiesManager.setPropertyValue("visionClassifierShardNodeCount", "5")
            // Act, Assert
            assertThat(v.getShardNodeCount(classifierName = "")).isEqualTo(5)
            assertThat(v.getShardNodeCount(classifierName = "DefaultClassifier")).isEqualTo(5)
        }
        run {
            // Arrange
            PropertiesManager.setPropertyValue(
                "visionClassifierShardNodeCount",
                "DefaultClassifier=:ScreenClassifier=20:ButtonStateClassifier=30:CheckStateClassifier=40"
            )
            // Act, Assert
            assertThat(v.getShardNodeCount(classifierName = "")).isEqualTo(1)
        }
        run {
            // Arrange
            PropertiesManager.setPropertyValue(
                "visionClassifierShardNodeCount",
                "DefaultClassifier=:ScreenClassifier=20:ButtonStateClassifier=30:CheckStateClassifier=40"
            )
            // Act, Assert
            assertThatThrownBy {
                v.getShardNodeCount(classifierName = "DefaultClassifier")
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessage("visionClassifierShardNodeCount is invalid. (visionClassifierShardNodeCount: DefaultClassifier=:ScreenClassifier=20:ButtonStateClassifier=30:CheckStateClassifier=40)")
        }
        run {
            // Arrange
            PropertiesManager.setPropertyValue(
                "visionClassifierShardNodeCount",
                "DefaultClassifier=0:ScreenClassifier=20:ButtonStateClassifier=30:CheckStateClassifier=40"
            )
            assertThatThrownBy {
                v.getShardNodeCount(classifierName = "DefaultClassifier")
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessage("visionClassifierShardNodeCount must be greater than 0. (visionClassifierShardNodeCount: DefaultClassifier=0:ScreenClassifier=20:ButtonStateClassifier=30:CheckStateClassifier=40)")
        }


    }
}