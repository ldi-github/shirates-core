package shirates.core.unittest.utility.macos

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.utility.macos.RecognizeTextResult
import shirates.core.utility.toPath

class RecognizeTextResultTest {

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