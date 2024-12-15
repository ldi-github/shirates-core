package shirates.core.unittest.vision.driver

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.utility.image.BufferedImageUtility
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.cropImage
import shirates.core.utility.image.isSame
import shirates.core.utility.toPath
import shirates.core.vision.driver.VisionContext

class VisionContextTest {

    @Test
    fun loadTextRecognizerJson() {

        // Arrange
        val jsonFile =
            "unitTestData/files/visionElementCache/[Network & internet Screen]/[Network & internet Screen].json"
        val screenshotFile =
            "unitTestData/files/visionElementCache/[Network & internet Screen]/[Network & internet Screen].png"
        val screenshotImage = BufferedImageUtility.getBufferedImage(filePath = screenshotFile)
        val json = jsonFile.toPath().toFile().readText()
        // Act
        val c = VisionContext()
            .loadTextRecognizerResult(
                inputFile = screenshotFile,
                language = null,
                jsonString = json,
            )
        // Assert
        assertThat(c.jsonString).isEqualTo(json)
        assertThat(c.visionElements.count()).isEqualTo(18)
        run {
            val v = c.visionElements[0]
            assertThat(v.text).isEqualTo("2:31 0 0")
            assertThat(v.rect.toString()).isEqualTo(Rectangle(38, 48, 195, 43).toString())
            assertThat(v.textObservation?.text).isEqualTo("2:31 0 0")
            assertThat(v.textObservation?.jsonString).isEqualTo(json)
            assertThat(v.textObservation?.confidence).isEqualTo(0.3f)
            assertThat(v.textObservation?.screenshotFile).isEqualTo(screenshotFile)
            assertThat(v.textObservation?.screenshotImage.isSame(screenshotImage)).isTrue()
            assertThat(v.textObservation?.rectOnScreenshotImage?.toString()).isEqualTo(
                Rectangle(38, 48, 195, 43).toString()
            )
            assertThat(v.textObservation?.localRegionFile).isEqualTo(null)
            assertThat(v.textObservation?.localRegionImage).isEqualTo(null)
            assertThat(v.textObservation?.image?.isSame(screenshotImage.cropImage(rect = v.rect))).isTrue()
        }
    }

    @Test
    fun detect_text() {

        // Arrange
        val jsonFile =
            "unitTestData/files/visionElementCache/[Network & internet Screen]/[Network & internet Screen].json"
        val screenshotFile =
            "unitTestData/files/visionElementCache/[Network & internet Screen]/[Network & internet Screen].png"
        val json = jsonFile.toPath().toFile().readText()
        val c = VisionContext()
            .loadTextRecognizerResult(
                inputFile = screenshotFile,
                language = null,
                jsonString = json,
            )
        run {
            // Act
            val v = c.detect(text = "SIMs")
            // Assert
            assertThat(v.isFound).isTrue()
        }
    }
}