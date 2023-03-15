package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.describe
import shirates.core.driver.wait
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Wait1 : UITest() {

    @Test
    @Order(10)
    fun wait1() {

        scenario {
            case(1) {
                action {
                    describe("Wait for a short time.")
                        .wait()
                }
            }

            case(2) {
                action {
                    describe("Wait for 3.0 seconds.")
                        .wait(3.0)
                }
            }
        }
    }

}