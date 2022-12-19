package shirates.core.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveTapExtensionTest1 : UITest() {

    @Test
    @Order(10)
    fun tap_x_y() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    val e = it.select("General")
                    it.tap(x = e.bounds.centerX, y = e.bounds.centerY)
                }.expectation {
                    it.screenIs("[General Screen]")
                }
            }
        }

    }

    @Test
    @Order(20)
    fun tap_nickname() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.tap(expression = "[General]")
                }.expectation {
                    it.screenIs("[General Screen]")
                }
            }
        }

    }

    @Test
    @Order(30)
    fun tap_text() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.tap("General")
                }.expectation {
                    it.screenIs("[General Screen]")
                }
            }
        }

    }

    @Test
    @Order(40)
    fun tap_textStartsWith() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.tap("textStartsWith=Gene")
                }.expectation {
                    it.screenIs("[General Screen]")
                }
            }
        }

    }

    @Test
    @Order(50)
    fun tap_textContains() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.tap("*enera*")
                }.expectation {
                    it.screenIs("[General Screen]")
                }
            }
        }

    }

    @Test
    @Order(60)
    fun tap_textMatches() {

        scenario {
            case(1) {
                condition {
                    it.restartApp()
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.tap("textMatches=^Gen.*al$")
                }.expectation {
                    it.screenIs("[General Screen]")
                }
            }
        }

    }

}