package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.output
import shirates.core.driver.commandextension.thisIs
import shirates.core.exception.RerunScenarioException
import shirates.core.testcode.UITest

@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class RerunScenario2 : UITest() {

    @Test
    @Order(10)
    fun rerunScenario() {

        var count = 0

        scenario {
            case(1) {
                action {
                    count++
                    output("count=$count")
                    if (count == 1) {
                        throw RerunScenarioException("Request to rerun scenario")
                    }
                }.expectation {
                    count.thisIs(2, "count is 2")
                }
            }
        }
    }
}