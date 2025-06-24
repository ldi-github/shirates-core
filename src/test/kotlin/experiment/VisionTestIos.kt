package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.testContext
import shirates.core.logging.printInfo
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

    @Test
    fun colorScale_GRAY_32() {

        // default colorScale is GRAY_32
        printInfo("${testContext.visionColorScale}")

        scenario {
            case(1) {
                condition {
                    it.screenIs("[iOS Settings Top Screen]")
                }.action {
                    v1 = detect("StandBy").leftItem()
                }.expectation {
                    v1.imageIs("[StandBy Icon]")    // NG (on iOS 26 Liquid Glass), OK (on iOS 18 or older)
                }
            }
        }
    }

    @Test
    fun colorScale_GRAY_16() {

        // change colorScale to GRAY_16
        colorScaleGray16()
        printInfo("${testContext.visionColorScale}")

        scenario {
            case(1) {
                condition {
                    it.screenIs("[iOS Settings Top Screen]")
                }.action {
                    v1 = detect("StandBy").leftItem()
                }.expectation {
                    v1.imageIs("[StandBy Icon]")    // OK
                }
            }
        }
    }

}