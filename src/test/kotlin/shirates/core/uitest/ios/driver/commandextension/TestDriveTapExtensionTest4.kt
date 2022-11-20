package shirates.core.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveTapExtensionTest4 : UITest() {

    @Test
    @Order(10)
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

    @Test
    @Order(20)
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

    @Test
    @Order(30)
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

}