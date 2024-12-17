package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.vision.driver.commandextension.checkIsOFF
import shirates.core.vision.driver.commandextension.detect
import shirates.core.vision.driver.commandextension.scrollToBottom
import shirates.core.vision.driver.commandextension.tap
import shirates.core.vision.driver.right
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class VisionTestIos : VisionTest() {

    @Test
    fun visionTest() {

        scenario {
            case(1) {
                condition {
                }.action {
                    it.detect("Accessibility").tap()
                    it.detect("Display & Text Size").tap()
                    it.detect("Larger Text").tap()
                }.expectation {
                    it.detect("Larger Accessibility Sizes")
                        .right()
                        .checkIsOFF()
                }
            }
        }
    }

    @Test
    fun scrollToBottom() {

        scenario {
            case(1) {
                action {
                    it.scrollToBottom()
                }
            }
        }
    }
}