package shirates.core.vision.unittest.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.driver.testContext
import shirates.core.driver.vision
import shirates.core.testcode.UnitTest
import shirates.core.utility.image.ColorScale
import shirates.core.vision.driver.commandextension.colorScale
import shirates.core.vision.driver.commandextension.colorScaleBinary
import shirates.core.vision.driver.commandextension.colorScaleGray16
import shirates.core.vision.driver.commandextension.colorScaleGray32

class VisionDriveColorScaleExtensionTest : UnitTest() {

    @Test
    fun colorScaleTest() {

        run {
            // Act
            vision.colorScale(ColorScale.GRAY_256)
            // Assert
            assertThat(testContext.visionColorScale).isEqualTo(ColorScale.GRAY_256)
        }
        run {
            // Act
            vision.colorScaleBinary()
            // Assert
            assertThat(testContext.visionColorScale).isEqualTo(ColorScale.BINARY)
        }
        run {
            // Act
            vision.colorScaleGray16()
            // Assert
            assertThat(testContext.visionColorScale).isEqualTo(ColorScale.GRAY_16)
        }
        run {
            // Act
            vision.colorScaleGray32()
            // Assert
            assertThat(testContext.visionColorScale).isEqualTo(ColorScale.GRAY_32)
        }
    }
}