package shirates.core.vision.unittest.configuration.repository

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestMode
import shirates.core.exception.TestConfigException
import shirates.core.utility.file.resolve
import shirates.core.utility.toPath
import shirates.core.vision.configration.repository.VisionClassifier
import kotlin.io.path.name

class VisionClassifierRepositoryTest {

    @Test
    fun setup_valid() {

        // Arrange
        val classifierDirectory = "unitTestData/files/vision/valid"
        val repository = VisionClassifier()
        // Act
        repository.setup(
            visionClassifierDirectory = classifierDirectory,
            buildClassifierDirectory = PropertiesManager.visionBuildDirectory.resolve("vision/$classifierDirectory"),
            createBinary = null
        )
        // Assert
        val labelMap = repository.labelFileInfoMap
        assertThat(labelMap.keys).contains("[Label1]", "[Label2]", "[Label3]")
        assertThat(labelMap["[Label1]"]?.files?.map { it.learningImageFile }).contains(
            "unitTestData/files/vision/valid/[Label1]/img@a.png".toPath().toString(),
            "unitTestData/files/vision/valid/[Label1]/img@i.png".toPath().toString(),
        )
        assertThat(labelMap["[Label2]"]?.files?.map { it.learningImageFile }).contains(
            "unitTestData/files/vision/valid/[Label2]/img.png".toPath().toString(),
        )
        assertThat(labelMap["[Label3]"]?.files?.map { it.learningImageFile }).contains(
            "unitTestData/files/vision/valid/subdirectory1/[Label3]/img@a.png".toPath().toString(),
            "unitTestData/files/vision/valid/subdirectory1/[Label3]/img@i.png".toPath().toString(),
        )
        assertThat(repository.classifierName).isEqualTo(repository.buildClassifierDirectory.toPath().name)
    }

    @Test
    fun setup_invalid() {

        // Arrange
        val classifierDirectory = "unitTestData/files/vision/invalid"
        val repository = VisionClassifier()
        // Act, Assert
        assertThatThrownBy {
            repository.setup(
                visionClassifierDirectory = classifierDirectory,
                buildClassifierDirectory = PropertiesManager.visionBuildDirectory.resolve("vision/$classifierDirectory"),
                createBinary = null
            )
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessageStartingWith("Label directory is duplicated. A label can belong to only one directory. (label=[Label1], dirs=")
    }

    @Test
    fun getFile() {

        // Arrange
        val classifierDirectory = "unitTestData/files/vision/valid"
        val repository = VisionClassifier()
        repository.setup(
            visionClassifierDirectory = classifierDirectory,
            buildClassifierDirectory = PropertiesManager.visionBuildDirectory.resolve("vision/$classifierDirectory"),
            createBinary = null
        )

        TestMode.runAsAndroid {
            run {
                // Act
                val file = repository.getFile("[Label1]")
                // Assert
                assertThat(file).isEqualTo("unitTestData/files/vision/valid/[Label1]/img@a.png".toPath().toString())
            }
            run {
                // Act
                val file = repository.getFile("[Label2]")
                // Assert
                assertThat(file).isEqualTo("unitTestData/files/vision/valid/[Label2]/img.png".toPath().toString())
            }
            run {
                // Act
                val file = repository.getFile("[Label3]")
                // Assert
                assertThat(file).isEqualTo(
                    "unitTestData/files/vision/valid/subdirectory1/[Label3]/img@a.png".toPath().toString()
                )
            }
        }
        TestMode.runAsIos {
            run {
                // Act
                val file = repository.getFile("[Label1]")
                // Assert
                assertThat(file).isEqualTo("unitTestData/files/vision/valid/[Label1]/img@i.png".toPath().toString())
            }
            run {
                // Act
                val file = repository.getFile("[Label2]")
                // Assert
                assertThat(file).isEqualTo("unitTestData/files/vision/valid/[Label2]/img.png".toPath().toString())
            }
            run {
                // Act
                val file = repository.getFile("[Label3]")
                // Assert
                assertThat(file).isEqualTo(
                    "unitTestData/files/vision/valid/subdirectory1/[Label3]/img@i.png".toPath().toString()
                )
            }
        }


    }
}