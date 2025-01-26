package shirates.core.uitest.android.testcode

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

        assertThat(TestMode.isAndroid).isTrue()
        assertThat(PropertiesManager.os).isEqualTo(TestMode.ANDROID)
        assertThat(PropertiesManager.configFile).isEqualTo("testConfig/android/testConfig@a.json")
        assertThat(PropertiesManager.profile).isEqualTo("Pixel 8(Android 14)")
        assertThat(testProfile.isAndroid).isTrue()
    }
}