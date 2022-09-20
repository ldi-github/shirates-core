package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.capture
import shirates.core.driver.commandextension.putSelector
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class CaptureTest : UITest() {

    @Test
    fun capture() {

        scenario {
            case(1) {
                condition {
                    it.putSelector("[Network & internet]")
                        .capture("[Network & internet]")
                        .capture("[Network & internet]:left")
                        .capture("[Network & internet]:leftImage")
                        .capture("[Network & internet]:label")
                }
            }

        }
    }
}