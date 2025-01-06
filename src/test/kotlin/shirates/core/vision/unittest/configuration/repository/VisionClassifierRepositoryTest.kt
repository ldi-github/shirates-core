package shirates.core.vision.unittest.configuration.repository

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.driver.TestMode
import shirates.core.exception.TestConfigException
import shirates.core.utility.toPath
import shirates.core.vision.configration.repository.VisionClassifierRepository
import kotlin.io.path.name

class VisionClassifierRepositoryTest {

    @Test
    fun setup_valid() {

        // Arrange
        val visionDirectory = "unitTestData/files/vision/valid"
        val repository = VisionClassifierRepository()
        // Act
        repository.setup(imageClassifierDirectory = visionDirectory)
        // Assert
        val labelMap = repository.labelMap
        assertThat(labelMap.keys).contains("[Label1]", "[Label2]", "[Label3]")
        assertThat(labelMap["[Label1]"]?.files).contains(
            "unitTestData/files/vision/valid/[Label1]/img@a.png".toPath().toString(),
            "unitTestData/files/vision/valid/[Label1]/img@i.png".toPath().toString(),
        )
        assertThat(labelMap["[Label2]"]?.files).contains(
            "unitTestData/files/vision/valid/[Label2]/img.png".toPath().toString(),
        )
        assertThat(labelMap["[Label3]"]?.files).contains(
            "unitTestData/files/vision/valid/subdirectory1/[Label3]/img@a.png".toPath().toString(),
            "unitTestData/files/vision/valid/subdirectory1/[Label3]/img@i.png".toPath().toString(),
        )
        assertThat(repository.classifierName).isEqualTo(repository.imageClassifierDirectory.toPath().name)
    }

    @Test
    fun setup_invalid() {

        // Arrange
        val visionDirectory = "unitTestData/files/vision/invalid"
        val repository = VisionClassifierRepository()
        // Act, Assert
        assertThatThrownBy {
            repository.setup(imageClassifierDirectory = visionDirectory)
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessageStartingWith("Label directory is duplicated. A label can be belong to only one directory. (label=[Label1], dirs=")
    }

    @Test
    fun getFile() {

        // Arrange
        val visionDirectory = "unitTestData/files/vision/valid"
        val repository = VisionClassifierRepository()
        repository.setup(imageClassifierDirectory = visionDirectory)

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