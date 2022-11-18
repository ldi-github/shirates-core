package shirates.core.uitest.android.driver.commandextension

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
    @Order(10)
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
    @Order(20)
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

    @Order(30)
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

    @Order(40)
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


}