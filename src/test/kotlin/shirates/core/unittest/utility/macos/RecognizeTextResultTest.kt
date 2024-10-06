package shirates.core.unittest.utility.macos

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.opentest4j.TestAbortedException
import shirates.core.driver.TestMode
import shirates.core.testcode.UnitTest
import shirates.core.utility.macos.RecognizeTextResult
import shirates.core.utility.macos.VNCommandUtility
import shirates.core.utility.toPath

class RecognizeTextResultTest : UnitTest() {

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
                "unitTestData/files/vision/android_settings_recognize-text.json".toPath().toFile().readText()
            // Act
            val result = RecognizeTextResult(jsonString)
            // Assert
            assertThat(result.items.count()).isEqualTo(21)
            // Assert
            assertThat(result.imagePath).endsWith("unitTestData/files/vision/android_settings.png")
            assertThat(result.items[0].text).isEqualTo("100%")
            assertThat(result.items[0].confidence).isEqualTo(1.0f)
            assertThat(result.items[0].rect).isEqualTo("[98.0, 228.0, 48.0, 18.0]")
            assertThat(result.items[0].rectangle.toString()).isEqualTo("[98, 228, 48, 18]")
        }
    }

}