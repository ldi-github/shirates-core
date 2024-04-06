package shirates.core.unittest.utility.image

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.repository.ImageFileRepository
import shirates.core.driver.TestMode
import shirates.core.logging.TestLog
import shirates.core.testcode.UnitTest
import shirates.core.utility.image.BufferedImageUtility
import shirates.core.utility.image.isSame
import shirates.core.utility.toPath
import java.io.FileNotFoundException

class ImageFileRepositoryTest : UnitTest() {

    override fun beforeAll(context: ExtensionContext?) {
        TestMode.setAndroid()
    }

    @Test
    fun init_ImageFileEntry() {

        run {
            // Arrange
            val filePath = "unitTestConfig/android/image/screens/images/tower_of_the_sun_face.png".toPath()
            // Act
            val entry = ImageFileRepository.ImageFileEntry(filePath = filePath)
            // Assert
            assertThat(entry.fileName).isEqualTo("tower_of_the_sun_face.png")
            assertThat(entry.filePath).isEqualTo(filePath)
            assertThat(entry.bufferedImage).isNotNull()

            // Arrange
            val image =
                BufferedImageUtility.getBufferedImage("unitTestConfig/android/image/screens/images/tower_of_the_sun_face.png")
            // Act
            entry.bufferedImage = image
            // Assert
            assertThat(entry.bufferedImage).isNotNull()
        }
        run {
            // Arrange
            val filePath = "unitTestConfig/android/image/screens/images/NotExistFile.png".toPath()
            // Act
            val entry = ImageFileRepository.ImageFileEntry(filePath = filePath)
            // Assert
            assertThat(entry.fileName).isEqualTo("NotExistFile.png")
            assertThat(entry.filePath).isEqualTo(filePath)
            assertThatThrownBy {
                entry.bufferedImage
            }.isInstanceOf(FileNotFoundException::class.java)
                .hasMessage(filePath.toString())
        }

    }

    @Test
    fun setup() {

        run {
            // Act
            ImageFileRepository.setup(screenDirectory = "unitTestConfig/android/image/screens".toPath())
            // Assert
            run {
                val entry = ImageFileRepository.getImageFileEntry(imageExpression = "tower_of_the_sun_face.png")!!
                assertThat(entry.fileName).isEqualTo("tower_of_the_sun_face@a.png")
            }
            run {
                val entry =
                    ImageFileRepository.getImageFileEntry(imageExpression = "tower_of_the_sun_face.png?s=0.5&t=10")!!
                assertThat(entry.fileName).isEqualTo("tower_of_the_sun_face@a.png")
            }
            run {
                assertThatThrownBy {
                    ImageFileRepository.getImageFileEntry(imageExpression = "NotExist.png")
                }.isInstanceOf(FileNotFoundException::class.java)
                    .hasMessage("Image file not found. (expression=NotExist.png)")
            }
        }
    }

    @Test
    fun setFile() {

        run {
            // Arrange
            ImageFileRepository.clear()
            assertThat(ImageFileRepository.imageFileMap.count()).isEqualTo(0)
            val filePath = "unitTestConfig/android/image/screens/images/tower_of_the_sun.png".toPath()
            // Act
            val entry = ImageFileRepository.setFile(filePath = filePath)
            // Assert
            assertThat(entry.filePath).isEqualTo(filePath)
            assertThat(ImageFileRepository.imageFileMap.count()).isEqualTo(1)
            assertThat(ImageFileRepository.imageFileMap.values.first().count()).isEqualTo(1)
            assertThat(TestLog.lastTestLog?.message?.startsWith("Image file name duplicated.")).isFalse()

            // Act
            ImageFileRepository.setFile(filePath = filePath)    // Set same file
            // Assert
            assertThat(ImageFileRepository.imageFileMap.count()).isEqualTo(1)
            assertThat(ImageFileRepository.imageFileMap.values.first().count()).isEqualTo(1)

            // Arrange
            val filePath2 = "unitTestConfig/android/image2/screens/images/tower_of_the_sun.png".toPath()
            // Act
            ImageFileRepository.setFile(filePath = filePath2)    // Set another file, same fileName
            // Assert
            assertThat(ImageFileRepository.imageFileMap.count()).isEqualTo(1)
            assertThat(ImageFileRepository.imageFileMap.values.first().count()).isEqualTo(2)

            // Act
            ImageFileRepository.setFile(filePath = filePath2)    // Set same file
            // Assert
            assertThat(ImageFileRepository.imageFileMap.count()).isEqualTo(1)
            assertThat(ImageFileRepository.imageFileMap.values.first().count()).isEqualTo(2)
        }
    }

    @Test
    fun getImageFileEntry() {

        // Arrange
        ImageFileRepository.setup(screenDirectory = "unitTestConfig/android/image/screens".toPath())

        run {
            // Act
            val entry = ImageFileRepository.getImageFileEntry(imageExpression = "tower_of_the_sun.png")!!
            // Assert
            assertThat(entry.filePath).isEqualTo("unitTestConfig/android/image/screens/images/tower_of_the_sun.png".toPath())
        }
        run {
            // Act
            assertThatThrownBy {
                ImageFileRepository.getImageFileEntry(imageExpression = "NotExistFile.png")
            }.isInstanceOf(FileNotFoundException::class.java)
                .hasMessage("Image file not found. (expression=NotExistFile.png)")
        }
    }

    @Test
    fun getImageFileEntry_same_file_name() {

        // Arrange
        ImageFileRepository.setup(screenDirectory = "unitTestConfig/others/app1/screens".toPath())

        run {
            // Act
            val entry = ImageFileRepository.getImageFileEntry(
                imageExpression = "[Image1].png",
                screenDirectory = "unitTestConfig/others/app1/screens/screen1"
            )!!
            // Assert
            assertThat(entry.filePath).isEqualTo("unitTestConfig/others/app1/screens/screen1/image/[Image1].png".toPath())
        }
        run {
            // Act
            val entry = ImageFileRepository.getImageFileEntry(
                imageExpression = "[Image1].png",
                screenDirectory = "unitTestConfig/others/app1/screens/screen2"
            )!!
            // Assert
            assertThat(entry.filePath).isEqualTo("unitTestConfig/others/app1/screens/screen2/image/[Image1].png".toPath())
        }
    }

    @Test
    fun getFilePath() {

        // Arrange
        ImageFileRepository.setup(screenDirectory = "unitTestConfig/android/image/screens".toPath())

        run {
            // Act
            val filePath = ImageFileRepository.getFilePath("tower_of_the_sun.png?s=0.5&t=10")
            // Assert
            val expected =
                "unitTestConfig/android/image/screens/images/tower_of_the_sun.png".toPath()
            assertThat(filePath).isEqualTo(expected)
        }
        run {
            // Act
            val filePath = ImageFileRepository.getFilePath("tower_of_the_sun_face.png?s=0.5&t=10")
            // Assert
            val expected =
                "unitTestConfig/android/image/screens/images/@a/tower_of_the_sun_face@a.png".toPath()
            assertThat(filePath).isEqualTo(expected)
        }
    }

    @Test
    fun getImage() {

        run {
            // Arrange
            ImageFileRepository.setup(screenDirectory = "unitTestConfig/android/image/screens".toPath())
            // Act
            val bufferedImage = ImageFileRepository.getBufferedImage("tower_of_the_sun_face.png?s=0.5&t=10")
            // Assert
            val expected =
                BufferedImageUtility.getBufferedImage("unitTestConfig/android/image/screens/images/tower_of_the_sun_face.png")
            assertThat(bufferedImage.isSame(expected)).isTrue()
        }
    }
}