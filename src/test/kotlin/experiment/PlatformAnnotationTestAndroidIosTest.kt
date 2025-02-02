package experiment

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestMode
import shirates.core.testcode.ios
import shirates.core.vision.testcode.VisionTest

@ios
class PlatformAnnotationTestAndroidIosTest : VisionTest() {

    @Test
    fun ios() {

        assertThat(TestMode.isiOS).isTrue()
        assertThat(PropertiesManager.os).isEqualTo(TestMode.IOS)
        assertThat(PropertiesManager.configFile).isEqualTo("testConfig/ios/testConfig@i.json")
        assertThat(PropertiesManager.profile).isEqualTo("")
        assertThat(PropertiesManager.getDefaultProfileName()).isEqualTo("iPhone 16(iOS 18.2)")
        assertThat(testProfile.profileName).isEqualTo("iPhone 16(iOS 18.2)")
        assertThat(testProfile.isiOS).isTrue()
    }
}