package experiment

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.ifCheckOFF
import shirates.core.driver.commandextension.checkIsOFF
import shirates.core.driver.commandextension.checkIsON
import shirates.core.driver.commandextension.select
import shirates.core.driver.commandextension.tap
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/android/androidSettings_ja/testrun.properties")
class AirplaneModeTestJa : UITest() {

    @Test
    @Order(10)
    @DisplayName("機内モードスイッチがONの場合にタップするとOFFになること")
    fun airplaneModeSwitch() {

        scenario {
            case(1) {
                condition {
                    it.tap("ネットワークとインターネット")
                    it.select("<機内モード>:rightSwitch")
                        .ifCheckOFF {
                            it.tap()
                        }
                    it.checkIsON()
                }.action {
                    it.tap("<機内モード>:rightSwitch")
                }.expectation {
                    it.checkIsOFF()
                }
            }
        }
    }

}