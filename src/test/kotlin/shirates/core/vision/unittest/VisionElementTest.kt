package shirates.core.vision.unittest

import com.google.common.io.Files
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Selector
import shirates.core.logging.TestLog
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.image.BufferedImageUtility
import shirates.core.utility.image.Rectangle
import shirates.core.utility.toPath
import shirates.core.vision.VisionElement

class VisionElementTest {

    @Test
    fun subject() {

        // Arrange
        val v = VisionElement(capture = false)
        v.selector = Selector("<Text1>:belowText")
        // Act, Assert
        assertThat(v.subject).isEqualTo("<Text1>:belowText")
    }

    @Test
    fun getCell() {

        // Arrange
        val imageFileName = "[iOS Settings Top Screen].png"
        val imageFilePath = TestLog.directoryForLog.resolve(imageFileName).toString()
        val templateImageFile = "vision/screens/@i/[iOS Settings Top Screen].png"
        TestLog.directoryForLog.toFile().mkdirs()
        Files.copy(
            templateImageFile.toPath().toFile(),
            imageFilePath.toPath().toFile(),
        )
        CodeExecutionContext.lastScreenshotName = imageFileName
        CodeExecutionContext.lastScreenshotImage = BufferedImageUtility.getBufferedImage(imageFilePath)
        val baseRect = Rectangle(left = 239, top = 759, width = 99, height = 41) // VPN
        // Act
        val v = baseRect.toVisionElement()
        val cell = v.getCell(horizontalMargin = 20, verticalMargin = 20)
        cell.saveImage()
        // Assert
        assertThat(cell.rect.toString()).isEqualTo("[46, 500, 1132, 847](w=1087, h=348)")
    }
}