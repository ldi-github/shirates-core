package shirates.core.vision.uitest.android.driver.commandextension

import goPreviousApp
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.testDrive
import shirates.core.vision.driver.commandextension.appIs
import shirates.core.vision.driver.commandextension.launchApp
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestDriveNavigationExtensionTest : VisionTest() {

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
                    testDrive.goPreviousApp()
                }.expectation {
                    it.appIs("[Settings]")
                }
            }
        }
    }

}