package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.select
import shirates.core.driver.commandextension.textIs
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class SelectAndAssert1 : UITest() {

    @Test
    @Order(10)
    fun selectAndAssert1_OK() {

        scenario {
            case(1) {
                expectation {
                    it.select("Settings")
                        .textIs("Settings")   // OK
                }
            }
        }
    }

    @Test
    @Order(20)
    fun selectAndAssert2_NG() {

        scenario {
            case(1) {
                expectation {
                    it.select("Settings")
                        .textIs("Network & internet")   // NG
                }
            }
        }
    }

}