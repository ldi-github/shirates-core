package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.tap
import shirates.core.driver.commandextension.tempSelector
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TempSelector1 : UITest() {

    @Test
    @Order(10)
    fun tempSelector() {

        tempSelector("[First Item]", "Network & internet")

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.tap("[First Item]")
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
        }
    }

}