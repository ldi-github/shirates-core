package shirates.core.uitest.ios.driver.commandextension.work01

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveTapExtensionTest2 : UITest() {

    @Order(10)
    @Test
    fun tapWithScrollUp_nickname() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .flickBottomToTop()
                }.action {
                    it.tapWithScrollUp(expression = "[General]")
                }.expectation {
                    it.screenIs("[General Screen]")
                }
            }
        }

        it.tap("[<Settings]")
            .flickTopToBottom()
    }

    @Order(20)
    @Test
    fun tapWithScrollUp_text() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .flickBottomToTop()
                }.action {
                    it.tapWithScrollUp("General")
                }.expectation {
                    it.screenIs("[General Screen]")
                }
            }
        }

        it.tap("[<Settings]")
            .flickTopToBottom()
    }

    @Order(30)
    @Test
    fun tapWithScrollUp_textStartsWith() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .flickBottomToTop()
                }.action {
                    it.tapWithScrollUp("Gen*")
                }.expectation {
                    it.screenIs("[General Screen]")
                }
            }
        }

        it.tap("[<Settings]")
            .flickTopToBottom()
    }

    @Order(40)
    @Test
    fun tapWithScrollUp_textContains() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .flickBottomToTop()
                }.action {
                    it.tapWithScrollUp("*enera*")
                }.expectation {
                    it.screenIs("[General Screen]")
                }
            }
        }

        it.tap("[<Settings]")
            .flickTopToBottom()
    }

    @Order(50)
    @Test
    fun tapWithScrollUp_textEndsWith() {

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

    @Order(60)
    @Test
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

        it.tap("[<Settings]")
            .flickTopToBottom()
    }

    @Order(70)
    @Test
    fun tapWithScrollUp_id() {

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

//    @Order(3)
//    @Test
//    fun tapWithScrollUp_access() {
//
//    }
//
//    @Order(3)
//    @Test
//    fun tapWithScrollUp_accessStartsWith() {
//
//    }

    @Order(80)
    @Test
    fun tapWithScrollUp_xpath() {

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