package shirates.core.unittest.configuration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.ImageInfo
import shirates.core.configuration.PropertiesManager
import shirates.core.testcode.UnitTest

class ImageInfoTest : UnitTest() {

    @Test
    fun init_test() {

        val defaultScale = PropertiesManager.imageMatchingScale
        val defaultThreshold = PropertiesManager.imageMatchingThreshold

        run {
            // Act
            val imageInfo = ImageInfo("image1.png?s=0.5&t=20.0")
            // Assert
            assertThat(imageInfo.imageExpression).isEqualTo("image1.png?s=0.5&t=20.0")
            assertThat(imageInfo.fileName).isEqualTo("image1.png")
            assertThat(imageInfo.scale).isEqualTo(0.5)
            assertThat(imageInfo.threshold).isEqualTo(20.0)
            assertThat(imageInfo.toString()).isEqualTo("image1.png?scale=0.5&threshold=20.0")
        }
        run {
            // Act
            val imageInfo = ImageInfo("image1.png")
            // Assert
            assertThat(imageInfo.imageExpression).isEqualTo("image1.png")
            assertThat(imageInfo.fileName).isEqualTo("image1.png")
            assertThat(imageInfo.scale).isEqualTo(defaultScale)
            assertThat(imageInfo.threshold).isEqualTo(defaultThreshold)
            assertThat(imageInfo.toString()).isEqualTo("image1.png?scale=$defaultScale&threshold=$defaultThreshold")
        }
        run {
            // Act
            val imageInfo = ImageInfo("image1.png?s=1.0")
            // Assert
            assertThat(imageInfo.imageExpression).isEqualTo("image1.png?s=1.0")
            assertThat(imageInfo.fileName).isEqualTo("image1.png")
            assertThat(imageInfo.scale).isEqualTo(1.0)
            assertThat(imageInfo.threshold).isEqualTo(PropertiesManager.imageMatchingThreshold)
            assertThat(imageInfo.toString()).isEqualTo("image1.png?threshold=$defaultThreshold")
        }
        run {
            // Act
            val imageInfo = ImageInfo("image1.png?scale=1.0")
            // Assert
            assertThat(imageInfo.imageExpression).isEqualTo("image1.png?scale=1.0")
            assertThat(imageInfo.fileName).isEqualTo("image1.png")
            assertThat(imageInfo.scale).isEqualTo(1.0)
            assertThat(imageInfo.threshold).isEqualTo(PropertiesManager.imageMatchingThreshold)
            assertThat(imageInfo.toString()).isEqualTo("image1.png?threshold=$defaultThreshold")
        }
        run {
            // Act
            val imageInfo = ImageInfo("image1.png?s=0.5")
            // Assert
            assertThat(imageInfo.imageExpression).isEqualTo("image1.png?s=0.5")
            assertThat(imageInfo.fileName).isEqualTo("image1.png")
            assertThat(imageInfo.scale).isEqualTo(0.5)
            assertThat(imageInfo.threshold).isEqualTo(PropertiesManager.imageMatchingThreshold)
            assertThat(imageInfo.toString()).isEqualTo("image1.png?scale=0.5&threshold=$defaultThreshold")
        }
        run {
            // Act
            val imageInfo = ImageInfo("image1.png?scale=0.5")
            // Assert
            assertThat(imageInfo.imageExpression).isEqualTo("image1.png?scale=0.5")
            assertThat(imageInfo.fileName).isEqualTo("image1.png")
            assertThat(imageInfo.scale).isEqualTo(0.5)
            assertThat(imageInfo.threshold).isEqualTo(PropertiesManager.imageMatchingThreshold)
            assertThat(imageInfo.toString()).isEqualTo("image1.png?scale=0.5&threshold=$defaultThreshold")
        }
        run {
            // Act
            val imageInfo = ImageInfo("image1.png?t=2.3")
            // Assert
            assertThat(imageInfo.imageExpression).isEqualTo("image1.png?t=2.3")
            assertThat(imageInfo.fileName).isEqualTo("image1.png")
            assertThat(imageInfo.scale).isEqualTo(PropertiesManager.imageMatchingScale)
            assertThat(imageInfo.threshold).isEqualTo(2.3)
            assertThat(imageInfo.toString()).isEqualTo("image1.png?scale=$defaultScale&threshold=2.3")
        }
        run {
            // Act
            val imageInfo = ImageInfo("image1.png?threshold=2.3")
            // Assert
            assertThat(imageInfo.imageExpression).isEqualTo("image1.png?threshold=2.3")
            assertThat(imageInfo.fileName).isEqualTo("image1.png")
            assertThat(imageInfo.scale).isEqualTo(PropertiesManager.imageMatchingScale)
            assertThat(imageInfo.threshold).isEqualTo(2.3)
            assertThat(imageInfo.toString()).isEqualTo("image1.png?scale=$defaultScale&threshold=2.3")
        }
        run {
            // Act
            val imageInfo = ImageInfo("image1.png?ABC")
            // Assert
            assertThat(imageInfo.fileName).isEqualTo("image1.png")
            assertThat(imageInfo.scale).isEqualTo(PropertiesManager.imageMatchingScale)
            assertThat(imageInfo.threshold).isEqualTo(PropertiesManager.imageMatchingThreshold)
            assertThat(imageInfo.toString()).isEqualTo("image1.png?scale=$defaultScale&threshold=$defaultThreshold")
        }
        run {
            // Act
            val imageInfo = ImageInfo("image1.png?scale=1.0&threshold=0.0")
            // Assert
            assertThat(imageInfo.fileName).isEqualTo("image1.png")
            assertThat(imageInfo.scale).isEqualTo(1.0)
            assertThat(imageInfo.threshold).isEqualTo(0.0)
            assertThat(imageInfo.toString()).isEqualTo("image1.png")
        }
    }
}