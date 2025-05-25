package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class VisionTestIos : VisionTest() {

    @Test
    fun visionTest() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[iOS Settings Top Screen]")
                }.expectation {
                    it.onCellOf("VPN") {
                        exist(expression = "Sign in to your iPhone")    // OK
                        dontExist(expression = "Screen Time")   // OK
//                        exist(expression = "Screen Time")   // NG
                    }
                }
            }
            case(2) {
                action {
                    it.detect("Accessibility").tap()
                    it.detect("Display & Text Size").tap()
                    it.detect("Larger Text").tap()
                }.expectation {
                    it.detect("Larger Accessibility Sizes")
                        .rightItem()
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