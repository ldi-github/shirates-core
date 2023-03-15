package experiment

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.tap
import shirates.core.driver.waitForDisplay
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class WaitTest : UITest() {

    @Test
    @Order(10)
    fun waitForDisplay() {
        scenario {
            case(1) {
                action {
                    it.waitForDisplay(expression = "A")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun tap() {
        scenario {
            case(1) {
                action {
                    it.tap(expression = "A")
                }
            }
        }
    }

}