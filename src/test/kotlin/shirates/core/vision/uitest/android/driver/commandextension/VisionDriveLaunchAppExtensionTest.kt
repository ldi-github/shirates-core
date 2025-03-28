package shirates.core.vision.uitest.android.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.appIs
import shirates.core.vision.driver.commandextension.launchApp
import shirates.core.vision.driver.commandextension.terminateApp
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class VisionDriveLaunchAppExtensionTest : VisionTest() {

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

    @Test
    fun launchApp2() {

        scenario {
            case(1) {
                condition {
                    it.terminateApp("com.android.chrome")
                    it.terminateApp("com.android.settings")
                }.action {
                    it.launchApp("com.android.chrome")
                }.expectation {
                    it.appIs("Chrome")
                }
            }
            case(2) {
                action {
                    it.launchApp("com.android.settings", waitSeconds = 0.0)
                }.expectation {
                    it.appIs("Settings", waitSeconds = 0.0)
                }
            }
        }
    }

}