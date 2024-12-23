package shirates.core.unittest.vision

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.vision.ScreenRecognizer

class ScreenRecognizerTest {

    @Test
    fun recognizeScreen() {

        // Act
        val screenName = ScreenRecognizer.recognizeScreen(
            screenImageFile = "vision/screens/android/[Android Settings Top Screen].png"
        )
        // Assert
        assertThat(screenName).isEqualTo("[Android Settings Top Screen]")
    }

}