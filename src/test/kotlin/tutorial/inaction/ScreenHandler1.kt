package tutorial.inaction

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.eventextension.onScreen
import shirates.core.logging.printWarn
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class ScreenHandler1 : UITest() {

    @Test
    @Order(10)
    fun onScreen1() {

        onScreen("[Network & internet Screen]") { c ->
            printWarn("${c.screenName} is displayed.")
        }
        onScreen("[System Screen]") { c ->
            printWarn("${c.screenName} is displayed.")
        }

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    it.tap("[Network & internet]")
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }

            case(2) {
                condition {
                    it.pressBack()
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    it.tapWithScrollDown("[System]")
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
        }
    }

}