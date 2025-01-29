package shirates.core.vision.uitest.android.testcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestMode
import shirates.core.testcode.android
import shirates.core.vision.testcode.VisionTest

@android
class PlatformAnnotationAndroidTest : VisionTest() {

    @Test
    fun android() {

        val envMap = System.getenv()

        assertThat(TestMode.isAndroid).isTrue()

        assertThat(PropertiesManager.os).isEqualTo(TestMode.ANDROID)
        assertThat(PropertiesManager.configFile).isEqualTo("testConfig/android/testConfig@a.json")

        val expectedProfile = if (envMap.containsKey("SR_profile")) envMap["SR_profile"] else ""
        assertThat(PropertiesManager.profile).isEqualTo(expectedProfile)

        val expectedDefaultProfileName =
            if (envMap.containsKey("SR_profile")) envMap["SR_profile"] else "Pixel 8(Android 14)"
        assertThat(PropertiesManager.getDefaultProfileName()).isEqualTo(expectedDefaultProfileName)

        val expectedProfileName = if (expectedProfile == "") "Pixel 8(Android 14)" else expectedProfile
        assertThat(testProfile.profileName).isEqualTo(expectedProfileName)

        assertThat(testProfile.isAndroid).isTrue()
    }
}