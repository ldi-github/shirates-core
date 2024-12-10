package demo

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.ifCanSelect
import shirates.core.driver.branchextension.ios
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class iOSSettingsDemo : UITest() {

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
                        it
                            .restartApp()
                            .screenIs("[iOS Settings Top Screen]")
                            .tap("General")
                            .tap("About")
                    }.action {
                        ifCanSelect("iOS Version") {
                            it.next().next()
                                .memoTextAs("Version")
                        }
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