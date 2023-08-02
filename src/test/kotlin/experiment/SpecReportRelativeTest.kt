package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class SpecReportRelativeTest : UITest() {

    @Test
    fun relative_label() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Network & internet Screen]")
                }.expectation {
                    it.exist("SIMs") {
                        aboveLabel(2).textIs("Calls & SMS")
                        aboveLabel(1).textIs("T-Mobile")
                        belowLabel(1).textIs("T-Mobile")
                        belowLabel(2).textIs("Airplane mode")
                    }
                }
            }
        }
    }
}