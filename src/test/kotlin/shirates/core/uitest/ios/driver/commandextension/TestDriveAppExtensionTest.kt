package shirates.core.uitest.ios.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveAppExtensionTest : UITest() {

    @Test
    fun isAppInstalled_appNickname() {

        // Act, Assert
        assertThat(it.isAppInstalled(appNickname = "[Settings]")).isTrue()

        // Act, Assert
        assertThat(it.isAppInstalled(appNickname = "[App1]")).isFalse()
    }

    @Test
    fun isAppInstalled_packageOrBundleId() {

        // Act, Assert
        assertThat(it.isAppInstalled(packageOrBundleId = "com.apple.Preferences")).isTrue()

        // Act, Assert
        assertThat(it.isAppInstalled(packageOrBundleId = "example.com.app1")).isFalse()

    }

    @Test
    fun terminateApp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.terminateApp()
                }.expectation {
                    it.screenName.thisIsNot("[iOS Settings Top Screen]")
                }
            }
        }
    }
}