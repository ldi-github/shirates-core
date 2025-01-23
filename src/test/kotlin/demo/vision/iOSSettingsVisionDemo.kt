package demo.vision

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/vision/ios/iOSSettings/testrun.properties")
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
    @DisplayName("Verify version and model in [About Screen]")
    fun s20() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[iOS Settings Top Screen]")
                }.expectation {
                    it.exist("General")
                }
            }
            case(2) {
                action {
                    it.tap("General")
                }.expectation {
                    it.screenIs("[General Screen]")
                        .exist("About")
                }
            }
            case(3) {
                action {
                    it.tap("About")
                }.expectation {
                    it.screenIs("[About Screen]")
                        .detect("iOS Version").rightTextIs("18.2")
                        .detect("Model Name").rightTextIs("iPhone 16", message = "[Model Name] is 'iPhone 16'")
                }
            }
        }
    }

}