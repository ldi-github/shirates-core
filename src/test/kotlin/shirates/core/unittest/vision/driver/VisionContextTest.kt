package shirates.core.unittest.vision.driver

import com.google.common.io.Files
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.TestLog
import shirates.core.utility.image.*
import shirates.core.utility.toPath
import shirates.core.vision.driver.VisionContext
import shirates.core.vision.result.RecognizeTextResult

class VisionContextTest {

    @Test
    fun createFromImageFile() {

        // Arrange
        val imageFile = "unitTestData/files/srvision/android/[Android Settings Screen].png"
        // Act
        val c = VisionContext.createFromImageFile(imageFile = imageFile)
        // Assert
        assertThat(c.screenshotFile).isEqualTo(imageFile.toPath().toString())
        assertThat(c.screenshotImage.isSame(BufferedImageUtility.getBufferedImage(filePath = imageFile))).isTrue()
        assertThat(c.localRegionFile).isEqualTo(imageFile.toPath().toString())
        assertThat(c.localRegionImage.isSame(c.screenshotImage)).isTrue()
        assertThat(c.rectOnLocalRegion.toString()).isEqualTo(c.screenshotImage?.rect.toString())
    }

    @Test
    fun loadTextRecognizerJson() {

        // Arrange
        val jsonFile =
            "unitTestData/files/visionElementCache/[Network & internet Screen]/[Network & internet Screen].json"
        val screenshotFile =
            "unitTestData/files/visionElementCache/[Network & internet Screen]/[Network & internet Screen].png"
        val screenshotImage = BufferedImageUtility.getBufferedImage(filePath = screenshotFile)
        CodeExecutionContext.lastScreenshotImage = screenshotImage
        CodeExecutionContext.lastScreenshotName = "[Network & internet Screen].png"
        TestLog.directoryForLog.toFile().mkdirs()
        Files.copy(screenshotFile.toPath().toFile(), CodeExecutionContext.lastScreenshotFile.toPath().toFile())
        val json = jsonFile.toPath().toFile().readText()
        val recognizeTextResult = RecognizeTextResult(json)
        // Act
        val c = VisionContext(capture = false)
            .loadTextRecognizerResult(
                inputFile = CodeExecutionContext.lastScreenshotFile!!,
                language = null,
                recognizeTextResult = recognizeTextResult,
            )
        // Assert
        assertThat(c.visionElements.count()).isEqualTo(18)
        run {
            val v = c.visionElements[0]
            assertThat(v.text).isEqualTo("1:10 GO •:")
            assertThat(v.rect.toString()).isEqualTo(Rectangle(40, 49, 252, 44).toString())
            assertThat(v.textObservation?.text).isEqualTo("1:10 GO •:")
            assertThat(v.textObservation?.jsonString).isEqualTo(json)
            assertThat(v.textObservation?.confidence).isEqualTo(0.5f)
            assertThat(v.textObservation?.screenshotFile).isEqualTo(CodeExecutionContext.lastScreenshotFile)
            assertThat(v.textObservation?.screenshotImage.isSame(screenshotImage)).isTrue()
            assertThat(v.textObservation?.rectOnScreen?.toString()).isEqualTo(
                Rectangle(40, 49, 252, 44).toString()
            )
            assertThat(v.textObservation?.localRegionFile).isEqualTo(CodeExecutionContext.lastScreenshotFile)
            assertThat(v.textObservation?.localRegionImage).isEqualTo(CodeExecutionContext.lastScreenshotImage)
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
        val recognizeTextResult = RecognizeTextResult(json)
        val c = VisionContext(screenshotFile = screenshotFile)
            .loadTextRecognizerResult(
                inputFile = screenshotFile,
                language = null,
                recognizeTextResult = recognizeTextResult,
            )
        run {
            // Act
            val v = c.detect(text = "SIMs")
            // Assert
            assertThat(v.isFound).isTrue()
        }
    }
}