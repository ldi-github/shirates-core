package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.exception.RerunScenarioException
import shirates.core.logging.printInfo
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class ScenarioRerunTest : UITest() {

    @Test
    fun rerunScenario() {

        scenario {
            case(1) {
                action {
                    throw RerunScenarioException("Rerun Scenario requested")
                }
            }
        }
    }

    @Test
    fun rerunScenario_multipleScenario() {

        scenario {
            case(1) {
                action {
                    printInfo("case(1)-action")
                }
            }
        }

        scenario {
            case(1) {
                action {
//                    throw RerunScenarioException("Rerun Scenario requested")
                }
            }
        }
    }

}