package shirates.core.uitest.android.driver.commandextension.work04

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestDriveTapExtensionTest2_DownUpTest1 : UITest() {

    @Order(10)
    @Test
    fun tapWithScrollDown_tapWithScrollUp_nickname() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.tapWithScrollDown("[System]")
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                }.action {
                    it.tapWithScrollUp(expression = "[Connected devices]")
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
        }
    }

    @Order(20)
    @Test
    fun tapWithScrollUp_tapWithScrollUp_text() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.tapWithScrollDown("System")
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                }.action {
                    it.tapWithScrollUp("Connected devices")
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
        }
    }

    @Order(30)
    @Test
    fun tapWithScrollDown_tapWithScrollUp_textStartsWith() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.tapWithScrollDown("Syste*")
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                }.action {
                    it.tapWithScrollUp("Connected*")
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
        }
    }

    @Order(40)
    @Test
    fun tapWithScrollDown_tapWithScrollUp_textContains() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.tapWithScrollDown("*yste*")
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                }.action {
                    it.tapWithScrollUp("*onnected device*")
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
        }
    }

    @Order(50)
    @Test
    fun tapWithScrollDown_tapWithScrollUp_textEndsWith() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.tapWithScrollDown("*ystem")
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                }.action {
                    it.tapWithScrollUp("*ted devices")
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
        }
    }


}