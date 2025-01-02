package demo.vision

import ifCanDetect
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisStartsWith
import shirates.core.vision.driver.branchextension.ios
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/ios/iOSSettingsVision/testrun.properties")
class iOSSettingsVisionDemo : VisionTest() {

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
                                it.rightText()
                                    .memoTextAs("iOS Version")
                            }
                            .ifCanDetect("Model Name") {
                                it.rightText()
                                    .memoTextAs("Model Name")
                            }
                    }.expectation {
                        readMemo("iOS Version").thisStartsWith("18.2", message = "iOS Version is `18.2`")
                        readMemo("Model Name").thisStartsWith("iPhone 16", message = "Model Name is `iPhone 16`")
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