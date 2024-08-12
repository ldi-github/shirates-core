package shirates.core.uitest.ios.driver.commandextension

import goPreviousTask
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.appIs
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
                    it.screenIs("[iOS Settings Top Screen]")
                    it.launchAppByShell("Maps")
                        .appIs("[Maps]")
                }.action {
                    it.goPreviousTask()
                }.expectation {
                    it.appIs("[Settings]")
                }
            }
            case(2) {
                condition {
                    it.launchAppByShell("Safari")
                        .appIs("[Safari]")
                }.action {
                    it.goPreviousTask()
                }.expectation {
                    it.appIs("[Settings]")
                }
            }
        }
    }
}