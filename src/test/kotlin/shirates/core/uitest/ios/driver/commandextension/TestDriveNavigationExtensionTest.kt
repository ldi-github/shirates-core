package shirates.core.uitest.ios.driver.commandextension

import goPreviousApp
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.appIs
import shirates.core.driver.commandextension.launchApp
import shirates.core.driver.commandextension.launchAppByShell
import shirates.core.driver.commandextension.screenIs
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveNavigationExtensionTest : UITest() {

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
                    it.goPreviousApp()
                }.expectation {
                    it.appIs("[Settings]")
                }
            }
        }
    }
    
}