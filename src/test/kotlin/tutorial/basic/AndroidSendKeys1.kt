package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.sendKeys
import shirates.core.driver.commandextension.textIs
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class AndroidSendKeys1 : UITest() {

    @Test
    @Order(10)
    fun sendKeys() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Search Screen]")
                }.action {
                    it.sendKeys("clock")
                }.expectation {
                    it.textIs("clock")
                }
            }
        }
    }

}