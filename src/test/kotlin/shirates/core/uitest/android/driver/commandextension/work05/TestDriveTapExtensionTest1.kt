package shirates.core.uitest.android.driver.commandextension.work05

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestElement
import shirates.core.driver.commandextension.*
import shirates.core.testcode.Must
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestDriveTapExtensionTest1 : UITest() {

    @Must
    @Order(1)
    @Test
    fun tap_x_y() {

        var e = TestElement()

        scenario {
            case(1) {
                condition {
                    it.restartApp()
                    it.macro("[Android Settings Top Screen]")
                    e = it.select("Connected devices")
                }.action {
                    it.tap(x = e.bounds.centerX, y = e.bounds.centerY)
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
        }
    }

    @Must
    @Order(1)
    @Test
    fun tap_nickname() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.tap(expression = "[Connected devices]")
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
        }
    }

    @Order(1)
    @Test
    fun tap_text() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.tap("Connected devices")
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
        }
    }

    @Order(1)
    @Test
    fun tap_textStartsWith() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.tap("textStartsWith=Connected")
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
        }
    }

    @Order(1)
    @Test
    fun tap_textContains() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.tap("*onnected*")
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
        }
    }

    @Order(1)
    @Test
    fun tap_textMatches() {

        scenario {
            case(1) {
                condition {
                    it.restartApp()
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.tap("textMatches=^Connected devices$")
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
        }
    }

    @Order(1)
    @Test
    fun tap_id() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.tap("#search_action_bar_title")
                }.expectation {
                    it.screenIs("[Android Settings Search Screen]")
                }
            }
        }
    }

    @Order(1)
    @Test
    fun tap_access() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Network & internet Screen]")
                }.action {
                    it.tap("@Navigate up")
                }.expectation {
                    it.screenIs("[Android Settings Top Screen]")
                }
            }
        }
    }

    @Order(1)
    @Test
    fun tap_accessStartsWith() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Network & internet Screen]")
                }.action {
                    it.tap("@Navi*")
                }.expectation {
                    it.screenIs("[Android Settings Top Screen]")
                }
            }
        }
    }

    @Order(1)
    @Test
    fun tap_xpath() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.tap("xpath=//*[@text='Connected devices']")
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
        }
    }

}