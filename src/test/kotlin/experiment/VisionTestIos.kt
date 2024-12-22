package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.driver.right
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class VisionTestIos : VisionTest() {

    @Test
    fun visionTest() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[iOS Settings Top Screen]")
                    it.cellOf("VPN") {
                        exist(expression = "Sign in to your iPhone")
                        dontExist(expression = "Screen Time")
                    }
                    it.detectRectMax(textIncluded = "Screen Time")
                        .save()
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