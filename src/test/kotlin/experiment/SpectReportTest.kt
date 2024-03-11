package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.conditionalAuto
import shirates.core.driver.commandextension.manual
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class SpectReportTest : UITest() {

    @Test
    fun test1() {

        scenario {
            case(1) {
                condition {

                }.expectation {
                    it.manual("manual1")
                    it.conditionalAuto("[image1]")
                }
            }
        }
    }
}