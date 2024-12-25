package shirates.core.unittest.utility.image

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.utility.image.BufferedImageUtility
import shirates.core.utility.image.isInBorder

class BufferedImageExtensionTest {

    @Test
    fun isInBorder() {

        run {
            // Arrange
            val image =
                BufferedImageUtility.getBufferedImage("unitTestData/files/vision_recognize_text/recognized_text_invalid.png")
            // Act
            val actual = image.isInBorder()
            // Assert
            assertThat(actual).isFalse()
        }
        run {
            // Arrange
            val image =
                BufferedImageUtility.getBufferedImage("unitTestData/files/vision_recognize_text/recognized_text_valid.png")
            // Act
            val actual = image.isInBorder()
            // Assert
            assertThat(actual).isTrue()
        }
    }
}