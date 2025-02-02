package shirates.core.vision.uitest.ios.testcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestMode
import shirates.core.testcode.ios
import shirates.core.vision.testcode.VisionTest

@ios
class PlatformAnnotationIosTest : VisionTest() {

    @Test
    fun ios() {

        val envMap = System.getenv()

        assertThat(TestMode.isiOS).isTrue()

        assertThat(PropertiesManager.os).isEqualTo(TestMode.IOS)
        assertThat(PropertiesManager.configFile).isEqualTo("testConfig/ios/testConfig@i.json")

        val expectedProfile = if (envMap.containsKey("SR_profile")) envMap["SR_profile"] else ""
        assertThat(PropertiesManager.profile).isEqualTo(expectedProfile)

        val expectedDefaultProfileName =
            if (envMap.containsKey("SR_profile")) envMap["SR_profile"] else "iPhone 16(iOS 18.2)"
        assertThat(PropertiesManager.getDefaultProfileName()).isEqualTo(expectedDefaultProfileName)

        val expectedProfileName = if (expectedProfile == "") "iPhone 16(iOS 18.2)" else expectedProfile
        assertThat(testProfile.profileName).isEqualTo(expectedProfileName)

        assertThat(testProfile.isiOS).isTrue()
    }
}