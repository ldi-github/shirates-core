package shirates.core.uitest.android.driver.commandextension.work01

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestDriveTapExtensionTest2_DownUpTest2 : UITest() {

    @Order(60)
    @Test
    fun tapWithScrollDown_tapWithScrollUp_textMatches() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.tapWithScrollDown("textMatches=^System$")
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                }.action {
                    it.tapWithScrollUp("textMatches=^Connected devices$")
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
        }
    }

    @Order(70)
    @Test
    fun tapWithScrollDown_tapWithScrollUp_id() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .flickBottomToTop()
                }.action {
                    it.tapWithScrollDown("#android:id/title")
                }.expectation {
                    it.exist("[<-]")
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                }.action {
                    it.tapWithScrollUp("#search_action_bar_title")
                }.expectation {
                    it.exist("[<-]")
                }
            }
        }
    }

    @Order(80)
    @Test
    fun tapWithScrollDown_tapWithScrollUp_access() {

        scenario {
            /**
             * access
             */
            case(1) {
                condition {
                    it.macro("[Developer options Screen]")
                }.action {
                    it.tapWithScrollDown("@Wireless debugging")
                }.expectation {
                    it.exist("Allow wireless debugging on this network?")
                }
            }
            case(2) {
                condition {
                    it.tap("Cancel")
                        .select("@Wireless debugging")
                        .scrollDown()
                        .scrollDown()
                }.action {
                    it.tapWithScrollUp("@Wireless debugging")
                }.expectation {
                    it.exist("Allow wireless debugging on this network?")
                }
            }
            /**
             * accessStartsWith
             */
            case(3) {
                condition {
                    it.tap("Cancel")
                        .select("@Wireless debugging")
                        .scrollUp()
                        .scrollUp()
                }.action {
                    it.tapWithScrollDown("@Wireless debug*")
                }.expectation {
                    it.exist("Allow wireless debugging on this network?")
                }
            }
            case(4) {
                condition {
                    it.tap("Cancel")
                        .select("@Wireless debugging")
                        .scrollDown()
                        .scrollDown()
                }.action {
                    it.tapWithScrollUp("@Wireless debug*")
                }.expectation {
                    it.exist("Allow wireless debugging on this network?")
                }
            }
        }
    }

    @Order(90)
    @Test
    fun tapWithScrollDown_tapWithScrollUp_xpath() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.tapWithScrollDown("xpath=//*[@text='Accessibility']")
                }.expectation {
                    it.screenIs("[Accessibility Screen]")
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                }.action {
                    it.tapWithScrollUp("xpath=//*[@text='Accessibility']")
                }.expectation {
                    it.screenIs("[Accessibility Screen]")
                }
            }
        }
    }

}