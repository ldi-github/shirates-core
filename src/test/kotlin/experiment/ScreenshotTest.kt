package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.vision.driver.commandextension.screenshot
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class ScreenshotTest : VisionTest() {

    @Test
    fun test1() {

        scenario {
            case(1) {
                action {
                    screenshot()
                    screenshot()
                }
            }
            case(2) {
                action {
                    screenshot(force = true)
                }
            }
        }
    }
}