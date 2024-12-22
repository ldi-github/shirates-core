package shirates.core.unittest.vision

import com.google.common.io.Files
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.TestLog
import shirates.core.utility.image.BufferedImageUtility
import shirates.core.utility.image.Rectangle
import shirates.core.utility.toPath

class VisionElementTest {

    @Test
    fun getCell() {

        // Arrange
        val imageFileName = "[iOS Settings Top Screen].png"
        val imageFilePath = TestLog.directoryForLog.resolve(imageFileName).toString()
        val templateImageFile = "vision/screens/ios/[iOS Settings Top Screen].png"
        TestLog.directoryForLog.toFile().mkdirs()
        Files.copy(
            templateImageFile.toPath().toFile(),
            imageFilePath.toPath().toFile(),
        )
        CodeExecutionContext.lastScreenshotName = imageFileName
        CodeExecutionContext.lastScreenshotImage = BufferedImageUtility.getBufferedImage(imageFilePath)
        val baseRect = Rectangle(x = 239, y = 759, width = 99, height = 41) // VPN
//        val baseRect = Rectangle(x = 230, y = 995, width = 294, height = 44)  // Screen Time
        // Act
        val v = baseRect.toVisionElement()
        val cell = v.getCell()
        cell.save()
        // Assert
        assertThat(cell.rect.toString()).isEqualTo("[45, 499, 1129, 845](w=1085, h=347)")
    }
}