package shirates.core.unittest.utility.macos

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.UserVar
import shirates.core.driver.TestMode
import shirates.core.testcode.UnitTest
import shirates.core.utility.macos.VNCommandUtility
import shirates.core.utility.macos.VNCommandUtility.SWIFT_PATH
import shirates.core.utility.toPath
import java.nio.file.Files

class VNCommandUtilityTest : UnitTest() {

    @Order(10)
    @Test
    fun buildTest() {

        if (TestMode.isRunningOnMacOS.not()) {
            println("buildTest() Test skipped.")
            return
        }

        val swiftPath = SWIFT_PATH.toPath()
        val vnCommandPath = VNCommandUtility.VNCOMMAND_PATH.toPath()

        run {
            // Act
            VNCommandUtility.deleteFiles()
            // Assert
            assertThat(Files.exists(swiftPath)).isFalse()
            assertThat(Files.exists(vnCommandPath)).isFalse()
        }
        run {
            // Act
            VNCommandUtility.build()
            // Assert
            assertThat(Files.exists(swiftPath)).isTrue()
            assertThat(Files.exists(vnCommandPath)).isTrue()
        }
        run {
            // Arrange
            val swiftLastModified = swiftPath.toFile().lastModified()
            val vnCommandLastModified = vnCommandPath.toFile().lastModified()
            swiftPath.toFile().appendText("//")
            // Act
            VNCommandUtility.build()
            // Assert
            assertThat(Files.exists(swiftPath)).isTrue()
            assertThat(Files.exists(vnCommandPath)).isTrue()
            // Assert
            val swiftLastModified2 = swiftPath.toFile().lastModified()
            val vnCommandLastModified2 = vnCommandPath.toFile().lastModified()
            assertThat(swiftLastModified2).isNotEqualTo(swiftLastModified)
            assertThat(vnCommandLastModified2).isNotEqualTo(vnCommandLastModified)
            // Act
            VNCommandUtility.build()
            // Assert
            val swiftLastModified3 = swiftPath.toFile().lastModified()
            val vnCommandLastModified3 = vnCommandPath.toFile().lastModified()
            assertThat(swiftLastModified3).isEqualTo(swiftLastModified2)
            assertThat(vnCommandLastModified3).isEqualTo(vnCommandLastModified2)
        }
    }

    @Order(20)
    @Test
    fun recognizeText() {

        // Arrange
        VNCommandUtility.build()
        val imagePath = "unitTestData/files/vision/android_settings.png".toPath().toString()
        run {
            // Act
            val result = VNCommandUtility.recognizeText(
                imagePath = imagePath,
            )
            // Assert
            assertThat(result.imagePath).isEqualTo(imagePath)
            assertThat(result.items.count()).isEqualTo(19)
            run {
                val item = result.items.first() { it.text == "Network & internet" }
                assertThat(item.text).isEqualTo("Network & internet")
                assertThat(item.confidence).isEqualTo(1.0f)
                assertThat(item.rect).isEqualTo("[100.0, 686.0, 226.0, 24.0]")
                assertThat(item.rectangle.toString()).isEqualTo("[100, 686, 226, 24]")
            }
        }
        run {
            // Act
            val result = VNCommandUtility.recognizeText(
                imagePath = imagePath,
                language = "ja"
            )
            // Assert
            assertThat(result.imagePath).isEqualTo(imagePath)
            assertThat(result.items.count()).isEqualTo(21)
            run {
                val item = result.items.first() { it.text == "Network & internet" }
                assertThat(item.text).isEqualTo("Network & internet")
                assertThat(item.confidence).isEqualTo(1.0f)
                assertThat(item.rect).isEqualTo("[100.0, 686.0, 226.0, 24.0]")
                assertThat(item.rectangle.toString()).isEqualTo("[100, 686, 226, 24]")
            }
        }
        run {
            // Arrange
            val outputPath = UserVar.downloads.resolve("android_settings_recognize-text.json").toString()
            // Act
            val result = VNCommandUtility.recognizeText(
                imagePath = imagePath,
                outputPath = outputPath,
                language = "ja"
            )
            // Assert
            assertThat(result.imagePath).isEqualTo(imagePath)
            assertThat(result.items.count()).isEqualTo(21)
            assertThat(result.original).isEqualTo(outputPath.toPath().toFile().readText())
        }

    }

    @Order(30)
    @Test
    fun detectRectangles() {

        // Arrange
        VNCommandUtility.build()
        val imagePath = "unitTestData/files/vision/android_settings.png".toPath().toString()
        run {
            // Act
            val result = VNCommandUtility.detectRectangles(
                imagePath = imagePath,
            )
            // Assert
            assertThat(result.imagePath).isEqualTo(imagePath)
            assertThat(result.maximumObservations).isEqualTo(100)
            assertThat(result.minimumSize).isEqualTo(0.1f)
            assertThat(result.minimumAspectRatio).isEqualTo(0.01f)
            assertThat(result.maximumAspectRatio).isEqualTo(1.0f)

            run {
                val item = result.items.first() { it.rect == "[293.0, 113.1, 287.9, 357.5]" }
                assertThat(item.confidence).isEqualTo(1.0f)
            }
        }
        run {
            // Arrange
            val outputPath = UserVar.downloads.resolve("android_settings_detect-rectangles.json").toString()
            // Act
            val result = VNCommandUtility.detectRectangles(
                imagePath = imagePath,
                outputPath = outputPath,
            )
            // Assert
            assertThat(result.imagePath).isEqualTo(imagePath)
            assertThat(result.items.count()).isEqualTo(4)
            assertThat(result.original).isEqualTo(outputPath.toPath().toFile().readText())
        }
    }

}