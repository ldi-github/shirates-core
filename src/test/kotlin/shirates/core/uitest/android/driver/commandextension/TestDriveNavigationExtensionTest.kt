package shirates.core.uitest.android.driver.commandextension

import goPreviousTask
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.appIs
import shirates.core.driver.commandextension.launchAppByShell
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestDriveNavigationExtensionTest : UITest() {

    @Test
    fun goPreviousTaskTest() {

        scenario {
            case(1) {
                condition {
                    it.appIs("[Settings]")
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
                    it.launchAppByShell("Chrome")
                        .appIs("[Chrome]")
                }.action {
                    it.goPreviousTask()
                }.expectation {
                    it.appIs("[Settings]")
                }
            }
        }
    }


}