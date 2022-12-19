package shirates.core.uitest.android.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.restartApp
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.tap
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestDriveTapExtensionTest2 : UITest() {

    @Order(50)
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

    @Order(60)
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

    @Order(70)
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

    @Order(80)
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

}