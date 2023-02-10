package tutorial.basic

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.appIs
import shirates.core.driver.commandextension.launchApp
import shirates.core.driver.commandextension.terminateApp
import shirates.core.testcode.UITest

@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class LaunchApp2 : UITest() {

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
                    it.appIs("com.apple.Preferences")
                }
            }
            case(2) {
                condition {
                    terminateApp("[Safari]")
                }.action {
                    it.launchApp("[Safari]")
                }.expectation {
                    it.appIs("[Safari]")
                }
            }
            case(3) {
                condition {
                    terminateApp("com.apple.mobilesafari")
                }.action {
                    it.launchApp("com.apple.mobilesafari")
                }.expectation {
                    it.appIs("com.apple.mobilesafari")
                }
            }
            case(4) {
                condition {
                    terminateApp("Safari")
                }.action {
                    it.launchApp("Safari")
                }.expectation {
                    it.appIs("Safari")
                }
            }
            case(5) {
                condition {
                    terminateApp("[Freeform]")
                }.action {
                    it.launchApp("[Freeform]")
                }.expectation {
                    it.appIs("[Freeform]")
                }
            }
        }
    }

}