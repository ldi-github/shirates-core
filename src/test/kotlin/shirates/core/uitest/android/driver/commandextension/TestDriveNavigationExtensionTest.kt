package shirates.core.uitest.android.driver.commandextension

import goPreviousApp
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.appIs
import shirates.core.driver.commandextension.launchApp
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestDriveNavigationExtensionTest : UITest() {

    @Test
    fun goPreviousAppTest() {

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
                    it.launchApp("[Maps]")
                }.expectation {
                    it.appIs("[Maps]")
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