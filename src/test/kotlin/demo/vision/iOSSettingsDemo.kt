package demo.vision

import ifCanDetect
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.memoTextAs
import shirates.core.driver.commandextension.thisIs
import shirates.core.vision.driver.*
import shirates.core.vision.driver.branchextension.ios
import shirates.core.vision.driver.commandextension.appIs
import shirates.core.vision.driver.commandextension.readMemo
import shirates.core.vision.driver.commandextension.screenIs
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class iOSSettingsDemo : VisionTest() {

    @Test
    @DisplayName("[Reset Local Data on Next Launch] exists on [Developer screen]")
    fun s10() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[iOS Settings Top Screen]")
                }.action {
                    it.tapWithScrollDown("Developer")
                }.expectation {
                    it.screenIs("[Developer Screen]")
                }
            }
            case(2) {
                expectation {
                    it.existWithScrollDown("Reset Local Data on Next Launch")
                }
            }

        }
    }

    @Test
    @DisplayName("get version in [About Screen]")
    fun s20() {

        scenario {
            case(1) {
                ios {
                    condition {
                        it.screenIs("[iOS Settings Top Screen]")
                            .tap("General")
                            .tap("About")
                    }.action {
                        screenshot()
                        it
                            .ifCanDetect("iOS Version") {
                                it.right(segmentMargin = 5)
                                    .recognizeText()
                                    .memoTextAs("iOS Version")
                            }
                            .ifCanDetect("Model Name") {
                                it.right(segmentMargin = 5)
                                    .recognizeText()
                                    .memoTextAs("Model Name")
                            }
                    }.expectation {
                        readMemo("iOS Version").thisIs("17.2", message = "iOS Version is `17.2`")
                        readMemo("Model Name").thisIs("iPhone 15", message = "Model Name is `iPhone 15`")
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("appIs")
    fun s30() {

        scenario {
            case(1) {
                ios {
                    it.appIs("Settings")
                }
            }
        }
    }
}