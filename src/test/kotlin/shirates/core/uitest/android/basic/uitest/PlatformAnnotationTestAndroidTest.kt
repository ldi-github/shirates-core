package shirates.core.uitest.android.basic.uitest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Testrun
import shirates.core.driver.TestMode
import shirates.core.testcode.android
import shirates.core.vision.testcode.VisionTest

@android
@Testrun("testConfig/vision/android/androidSettings/testrun.properties")
class PlatformAnnotationTestAndroidTest : VisionTest() {

    @Test
    fun android() {

        assertThat(TestMode.isAndroid).isTrue()
        assertThat(PropertiesManager.os).isEqualTo(TestMode.ANDROID)
        assertThat(PropertiesManager.configFile).isEqualTo("testConfig/android/testConfig.json")
        assertThat(PropertiesManager.profile).isEqualTo("Pixel 8(Android 14)")
        assertThat(testProfile.isAndroid).isTrue()
    }
}