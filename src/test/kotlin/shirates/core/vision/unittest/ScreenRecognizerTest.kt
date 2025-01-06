package shirates.core.vision.unittest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.utility.toPath
import shirates.core.vision.ScreenRecognizer
import shirates.core.vision.SrvisionProxy

class ScreenRecognizerTest {

    @Test
    fun recognizeScreen() {

        // Arrange
        SrvisionProxy.setupImageFeaturePrintConfig("vision/screens".toPath().toString())
        // Act
        val screenName = ScreenRecognizer.recognizeScreen(
            screenImageFile = "vision/screens/android/[Android Settings Top Screen].png"
        )
        // Assert
        assertThat(screenName).isEqualTo("[Android Settings Top Screen]")
    }

}