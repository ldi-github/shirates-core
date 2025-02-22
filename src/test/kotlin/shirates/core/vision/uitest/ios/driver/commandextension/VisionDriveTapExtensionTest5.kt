package shirates.core.vision.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.Want
import shirates.core.vision.classicScope
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class VisionDriveTapExtensionTest5 : VisionTest() {

    @Test
    @Order(10)
    fun tapWithScrollUp_textContains() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .flickBottomToTop()
                }.action {
                    it.tapWithScrollUp("enera")
                }.expectation {
                    it.screenIs("[General Screen]")
                }
            }
        }

        it.tap("Settings")
            .flickTopToBottom()
    }

    @Test
    @Order(20)
    fun tapWithScrollUp_textEndsWith() {

        classicScope {
            scenario {
                case(1) {
                    condition {
                        it.macro("[iOS Settings Top Screen]")
                            .flickBottomToTop()
                    }.action {
                        it.tapWithScrollUp("*eneral")
                    }.expectation {
                        it.screenIs("[General Screen]")
                    }
                }
            }

            it.tap("[<Settings]")
                .flickTopToBottom()
        }
    }

    @Test
    @Order(30)
    fun tapWithScrollUp_textMatches() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .flickBottomToTop()
                }.action {
                    it.tapWithScrollUp("textMatches=^Gen.*al$")
                }.expectation {
                    it.screenIs("[General Screen]")
                }
            }
        }

        it.tap("Settings")
            .flickTopToBottom()
    }

    @Test
    @Order(40)
    fun tapWithScrollUp_id() {

        classicScope {
            scenario {
                case(1) {
                    condition {
                        it.macro("[iOS Settings Top Screen]")
                            .flickBottomToTop()
                    }.action {
                        it.tapWithScrollUp("#General")
                    }.expectation {
                        it.screenIs("[General Screen]")
                    }
                }
            }

            it.tap("[<Settings]")
                .flickTopToBottom()
        }
    }

//    @Test
//    @Order(3)
//    fun tapWithScrollUp_access() {
//
//    }
//
//    @Test
//    @Order(3)
//    fun tapWithScrollUp_accessStartsWith() {
//
//    }

    @Test
    @Order(50)
    fun tapWithScrollUp_xpath() {

        classicScope {
            scenario {
                case(1) {
                    condition {
                        it.macro("[iOS Settings Top Screen]")
                            .flickBottomToTop()
                    }.action {
                        it.tapWithScrollUp("xpath=//*[@label='General' and @visible='true']")
                    }.expectation {
                        it.screenIs("[General Screen]")
                    }
                }
            }

            it.pressBack()
        }
    }

}