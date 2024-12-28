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
        // Act
        val v = baseRect.toVisionElement()
        val cell = v.getCell(horizontalMargin = 20, verticalMargin = 20)
        cell.save()
        // Assert
        assertThat(cell.rect.toString()).isEqualTo("[46, 500, 1122, 837](w=1077, h=338)")
    }
}