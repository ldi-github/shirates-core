package shirates.core.vision.uitest.ios.driver.commandextension

import goPreviousApp
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.classic
import shirates.core.vision.driver.commandextension.appIs
import shirates.core.vision.driver.commandextension.launchApp
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class VisionDriveNavigationExtensionTest : VisionTest() {

    @Test
    fun goLeftTaskTest() {

        scenario {
            case(1) {
                condition {
                    it.launchApp("[Settings]")
                }.expectation {
                    it.appIs("[Settings]")
                }
            }
            case(2) {
                action {
                    it.launchApp("[Safari]")
                }.expectation {
                    it.appIs("[Safari]")
                }
            }
            case(3) {
                action {
                    classic.goPreviousApp()
                }.expectation {
                    it.appIs("[Settings]")
                }
            }
        }
    }

}