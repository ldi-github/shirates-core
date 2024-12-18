package shirates.core.unittest.vision.configuration.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.vision.configration.repository.ScreenTextRepository

class ScreenTextRepositoryTest {

    @Test
    fun setup() {

        // Act
        ScreenTextRepository.setup(
            screenTextFile = "unitTestData/files/visionScreenText/screenText.json"
        )
        // Assert
        val r = ScreenTextRepository
        assertThat(r.screenTextMap.keys.count()).isEqualTo(3)
        assertThat(r.screenTextMap.containsKey("[Android Settings Top Screen]")).isTrue()
        assertThat(r.screenTextMap.containsKey("[Developer Screen]")).isTrue()
        assertThat(r.screenTextMap.containsKey("[iOS Settings Top Screen]")).isTrue()
        assertThat(r.screenInfoList.size).isEqualTo(3)
    }

    @Test
    fun clear() {

        // Arrange
        ScreenTextRepository.setup(
            screenTextFile = "unitTestData/files/visionScreenText/screenText.json"
        )
        assertThat(ScreenTextRepository.screenTextMap.keys).isNotEmpty()
        assertThat(ScreenTextRepository.screenInfoList).isNotEmpty()
        // Act
        ScreenTextRepository.clear()
        // Assert
        assertThat(ScreenTextRepository.screenTextMap.keys).isEmpty()
        assertThat(ScreenTextRepository.screenInfoList).isEmpty()
    }

    @Test
    fun getScreenTextList() {

        // Arrange
        ScreenTextRepository.setup(
            screenTextFile = "unitTestData/files/visionScreenText/screenText.json"
        )
        // Act
        val list = ScreenTextRepository.getScreenTextList("[iOS Settings Top Screen]")
        // Assert
        assertThat(list.count()).isEqualTo(15)
    }

    @Test
    fun screenTextInfo() {

        // Arrange
        val expectedJoinedText =
            """
23:30
Settings
Sign in to your iPhone
Set up iCloud, the App Store, and more.
VPN
Screen Time
General
Accessibility
Privacy & Security
Passwords
Safari
News
Translate
Maps
Shortouts
        """.trimIndent()
        ScreenTextRepository.setup(
            screenTextFile = "unitTestData/files/visionScreenText/screenText.json"
        )
        // Act
        val screenTextInfo = ScreenTextRepository.getScreenTextInfo("[iOS Settings Top Screen]")!!
        // Assert
        assertThat(screenTextInfo.screenName).isEqualTo("[iOS Settings Top Screen]")
        assertThat(screenTextInfo.texts.count()).isEqualTo(15)
        assertThat(screenTextInfo.joinedText).isEqualTo(expectedJoinedText)
        assertThat(screenTextInfo.joinedTextLength).isEqualTo(expectedJoinedText.length)
    }
}