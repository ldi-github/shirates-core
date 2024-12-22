package shirates.core.unittest.vision

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.vision.ScreenRecognizer
import shirates.core.vision.configration.repository.ScreenTextRepository

class ScreenRecognizerTest {

    @Test
    fun recognizeScreen() {

        // Arrange
        ScreenTextRepository.setup(
            screenTextFile = "unitTestData/files/visionScreenText/screenText.json"
        )
        // Act
        val screenName = ScreenRecognizer.recognizeScreen(
            screenImageFile = "vision/screens/android/[Android Settings Top Screen].png"
        )
        // Assert
        assertThat(screenName).isEqualTo("[Android Settings Top Screen]")
    }

}