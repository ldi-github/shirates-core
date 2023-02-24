package shirates.core.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveLaunchAppExtensionTest : UITest() {

    @Test
    fun launchApp1() {

        scenario {
            case(1) {
                condition {
                    it.terminateApp("Settings")
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
                    it.appIs("Settings")
                }
            }
            case(3) {
                condition {
                    it.terminateApp("com.apple.Preferences")
                }.action {
                    it.launchApp("com.apple.Preferences")
                }.expectation {
                    it.appIs("Settings")
                }
            }
        }
    }

    @Test
    fun launchApp2() {

        scenario {
            case(1) {
                condition {
                    it.terminateApp("com.apple.Maps")
                }.action {
                    it.launchApp("com.apple.Maps")
                }.expectation {
                    it.appIs("com.apple.Maps")
                }
            }
            case(2) {
                condition {
                    it.terminateApp("[Maps]")
                }.action {
                    it.launchApp("[Maps]")
                }.expectation {
                    it.appIs("[Maps]")
                    it.appIs("Maps")
                }
            }
        }
    }

}