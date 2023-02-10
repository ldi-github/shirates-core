package tutorial.basic

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.appIs
import shirates.core.driver.commandextension.launchApp
import shirates.core.driver.commandextension.terminateApp
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class LaunchApp1 : UITest() {

    @Test
    fun launchApp() {

        scenario {
            case(1) {
                condition {
                    terminateApp()
                }.action {
                    it.launchApp()
                }.expectation {
                    it.appIs("[Settings]")
                    it.appIs("Settings")
                    it.appIs("com.android.settings")
                }
            }
            case(2) {
                condition {
                    terminateApp("[Chrome]")
                }.action {
                    it.launchApp("[Chrome]")
                }.expectation {
                    it.appIs("[Chrome]")
                }
            }
            case(3) {
                condition {
                    terminateApp("com.android.chrome")
                }.action {
                    it.launchApp("com.android.chrome")
                }.expectation {
                    it.appIs("com.android.chrome")
                }
            }
            case(4) {
                condition {
                    terminateApp("Chrome")
                }.action {
                    it.launchApp("Chrome")
                }.expectation {
                    it.appIs("Chrome")
                }
            }
            case(5) {
                condition {
                    terminateApp("[Play Store]")
                }.action {
                    it.launchApp("[Play Store]")
                }.expectation {
                    it.appIs("[Play Store]")
                }
            }
        }
    }

}