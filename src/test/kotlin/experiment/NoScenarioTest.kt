package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class NoScenarioTest : UITest() {

    @Test
    fun noScenario() {


    }

    @Test
    fun noCase() {

        scenario {

        }
    }

    @Test
    fun emptyCase() {

        scenario {
            case(1) {

            }
        }
    }

    @Test
    fun expectationAndAssertionImplemented() {

        scenario {
            case(1) {
                expectation {
                    OK("expectation and assertion implemented")
                }
            }
        }

    }
}