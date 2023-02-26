package shirates.core.hand.uitest

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.screenIs
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/android/calculator/testrun.properties")
class MacroTest1 : UITest() {

    @Test
    @Order(10)
    fun restartCalculator() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Calculator Main Screen]")
                        .screenIs("[Calculator Main Screen]")
                }.action {
                    it.macro("[Calculator Main Screen]")
                }.expectation {
                    it.screenIs("[Calculator Main Screen]")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun error() {

        scenario {
            case(1) {
                condition {
                    macro("[Null Pointer Exception]") { e ->
                        e.cancel()
                        SKIP_SCENARIO(e.errorMessage)
                    }
                }
            }
        }
    }
}