package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.select
import shirates.core.driver.commandextension.tap
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TapTest : UITest() {

    @Test
    fun tapByCoordinates() {

        scenario {
            case(1) {
                action {
                    val e = select("Network & internet")
                    it.tap(x = e.bounds.centerX, y = e.bounds.centerY)
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
        }
    }

}