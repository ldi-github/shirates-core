package shirates.core.uitest.android.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.appIs
import shirates.core.driver.commandextension.launchApp
import shirates.core.driver.commandextension.terminateApp
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveLaunchAppExtensionTest : UITest() {

    @Test
    fun launchApp1() {

        scenario {
            case(1) {
                condition {
                    it.terminateApp()
                }.action {
                    it.launchApp()
                }.expectation {
                    it.appIs("Settings")
                }
            }
            case(2) {
                condition {
                    it.terminateApp("[Settings]")
                }.action {
                    it.launchApp("[Settings]")
                }.expectation {
                    it.appIs("[Settings]")
                }
            }
            case(3) {
                condition {
                    it.terminateApp("com.android.settings")
                }.action {
                    it.launchApp("com.android.settings")
                }.expectation {
                    it.appIs("com.android.settings")
                }
            }
            case(4) {
                condition {
                    it.terminateApp("com.android.settings")
                }.action {
                    it.launchApp("com.android.settings/.Settings")
                }.expectation {
                    it.appIs("com.android.settings")
                }
            }
        }
    }

}