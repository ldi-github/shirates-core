package shirates.core.vision.unittest.driver

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.utility.image.BufferedImageUtility
import shirates.core.utility.image.isSame
import shirates.core.utility.image.rect
import shirates.core.utility.toPath
import shirates.core.vision.driver.VisionContext

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

//    @Test
//    fun loadTextRecognizerJson() {
//
//        // Arrange
//        val jsonFile =
//            "unitTestData/files/visionElementCache/[Network & internet Screen]/[Network & internet Screen].json"
//        val screenshotFile =
//            "unitTestData/files/visionElementCache/[Network & internet Screen]/[Network & internet Screen].png"
//        val screenshotImage = BufferedImageUtility.getBufferedImage(filePath = screenshotFile)
//        CodeExecutionContext.lastScreenshotImage = screenshotImage
//        CodeExecutionContext.lastScreenshotName = "[Network & internet Screen].png"
//        TestLog.directoryForLog.toFile().mkdirs()
//        Files.copy(screenshotFile.toPath().toFile(), CodeExecutionContext.lastScreenshotFile.toPath().toFile())
//        val json = jsonFile.toPath().toFile().readText()
//        val recognizeTextResult = RecognizeTextResult(json)
//        // Act
//        val c = VisionContext(capture = false)
//            .loadTextRecognizerResult(
//                inputFile = CodeExecutionContext.lastScreenshotFile!!,
//                recognizeTextResult = recognizeTextResult,
//            )
//        // Assert
//        assertThat(c.getVisionElements().count()).isEqualTo(18)
//        run {
//            val v = c.getVisionElements()[0]
//            assertThat(v.text).isEqualTo("1:10 GO •:")
//            assertThat(v.textForComparison).isEqualTo("1:10 GO •:".forVisionComparison())
//            assertThat(v.rect.toString()).isEqualTo(Rectangle(40, 49, 252, 44).toString())
//            assertThat(v.recognizeTextObservation?.text).isEqualTo("1:10 GO •:")
//            assertThat(v.recognizeTextObservation?.jsonString).isEqualTo(json)
//            assertThat(v.recognizeTextObservation?.confidence).isEqualTo(0.5f)
//            assertThat(v.recognizeTextObservation?.screenshotFile).isEqualTo(CodeExecutionContext.lastScreenshotFile)
//            assertThat(v.recognizeTextObservation?.screenshotImage.isSame(screenshotImage)).isTrue()
//            assertThat(v.recognizeTextObservation?.rectOnScreen?.toString()).isEqualTo(
//                Rectangle(40, 49, 252, 44).toString()
//            )
//            assertThat(v.recognizeTextObservation?.localRegionFile).isEqualTo(CodeExecutionContext.lastScreenshotFile)
//            assertThat(v.recognizeTextObservation?.localRegionImage).isEqualTo(null)
//            assertThat(v.recognizeTextObservation?.image?.isSame(screenshotImage.cropImage(rect = v.rect))).isTrue()
//        }
//    }

//    @Test
//    fun detect_text() {
//
//        // Arrange
//        val jsonFile =
//            "unitTestData/files/visionElementCache/[Network & internet Screen]/[Network & internet Screen].json"
//        val screenshotFile =
//            "unitTestData/files/visionElementCache/[Network & internet Screen]/[Network & internet Screen].png"
//        val json = jsonFile.toPath().toFile().readText()
//        val recognizeTextResult = RecognizeTextResult(json)
//        val c = VisionContext(screenshotFile = screenshotFile)
//            .loadTextRecognizerResult(
//                inputFile = screenshotFile,
//                recognizeTextResult = recognizeTextResult,
//            )
//        run {
//            // Arrange
//            TestDriver.visionRootElement = VisionElement(capture = true)
//            TestDriver.visionRootElement.visionContext.screenshotFile = screenshotFile
//            CodeExecutionContext.workingRegionElement = TestDriver.visionRootElement
//            // Act
//            val v = c.detect(selector = Selector("SIMs"), last = false, looseMatch = true)
//            // Assert
//            assertThat(v.isFound).isTrue()
//        }
//    }
}