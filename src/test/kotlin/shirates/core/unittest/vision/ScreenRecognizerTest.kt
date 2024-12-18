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

    @Test
    fun getScreenTextDistanceInfo() {

        // Arrange
        ScreenTextRepository.setup(
            screenTextFile = "unitTestData/files/visionScreenText/screenText.json"
        )
        // Act
        val info = ScreenRecognizer.getScreenTextDistanceInfo(
            screenImageFile = "vision/screens/android/[Android Settings Top Screen].png"
        )
        // Assert
        assertThat(info?.screenTextInfo?.screenName).isEqualTo("[Android Settings Top Screen]")
        assertThat(info?.distance).isEqualTo(0)
        assertThat(info?.diffRate).isEqualTo(0.0)
        assertThat(info?.targetText).isEqualTo(
            """
12:17 0 0 0
Settings
Q Search settings
= Network & internet
Mobile, Wi-Fi, hotspot
Connected devices
Bluetooth, pairing
88:
Apps
Assistant, recent apps, default apps
Notifications
Notification history, conversations
Battery
100%
Storage
50% used - 7.98 GB free
Sound & vibration
Volume, haptics, Do Not Disturb
""".trimIndent()
        )
    }

}