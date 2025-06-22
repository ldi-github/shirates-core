package shirates.core.vision.unittest.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.driver.testContext
import shirates.core.driver.vision
import shirates.core.testcode.UnitTest
import shirates.core.utility.image.ColorModel
import shirates.core.vision.driver.commandextension.colorModel
import shirates.core.vision.driver.commandextension.colorModelBinary
import shirates.core.vision.driver.commandextension.colorModelGray16

class VisionDriveColorModelExtensionTest : UnitTest() {

    @Test
    fun setColorModel() {

        run {
            // Act
            vision.colorModel(ColorModel.GRAY_256)
            // Assert
            assertThat(testContext.visionColorModel).isEqualTo(ColorModel.GRAY_256)
        }
        run {
            // Act
            vision.colorModelBinary()
            // Assert
            assertThat(testContext.visionColorModel).isEqualTo(ColorModel.BINARY)
        }
        run {
            // Act
            vision.colorModelGray16()
            // Assert
            assertThat(testContext.visionColorModel).isEqualTo(ColorModel.GRAY_16)
        }
        run {
            // Act
            vision.colorModelGray16()
            // Assert
            assertThat(testContext.visionColorModel).isEqualTo(ColorModel.GRAY_16)
        }
    }
}