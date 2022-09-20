package shirates.core.uitest.android.driver.commandextension.work04

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
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
        assertThat(it.isAppInstalled(packageOrBundleId = "com.android.settings")).isTrue()

        // Act, Assert
        assertThat(it.isAppInstalled(packageOrBundleId = "example.com.app1")).isFalse()

    }

    @Test
    fun terminateApp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.terminateApp()
                        .refreshCache()
                }.expectation {
                    it.screenName.thisIsNot("[Android Settings Top Screen]")
                }
            }
        }
    }
}