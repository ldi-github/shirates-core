package shirates.core.unittest.utility.macos

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.opentest4j.TestAbortedException
import shirates.core.driver.TestMode
import shirates.core.testcode.UnitTest
import shirates.core.utility.macos.DetectRectanglesResult
import shirates.core.utility.macos.VNCommandUtility
import shirates.core.utility.toPath

class DetectRectanglesResultTest : UnitTest() {

    override fun beforeAll(context: ExtensionContext?) {

        if (TestMode.isRunningOnMacOS.not()) {
            throw TestAbortedException("This feature is for macOS only")
        }

        VNCommandUtility.build()
    }

    @Test
    fun init_test() {

        run {
            // Arrange
            val jsonString =
                "unitTestData/files/vision/android_settings_detect-rectangles.json".toPath().toFile().readText()
            // Act
            val result = DetectRectanglesResult(jsonString)
            // Assert
            assertThat(result.items.count()).isEqualTo(4)
            // Assert
            assertThat(result.imagePath).endsWith("unitTestData/files/vision/android_settings.png")
            assertThat(result.items[0].confidence).isEqualTo(1.0f)
            assertThat(result.items[0].rect).isEqualTo("[293.0, 113.1, 287.9, 357.5]")
            assertThat(result.items[0].rectangle.toString()).isEqualTo("[293, 113, 287, 357]")
        }
    }

}