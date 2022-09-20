package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.sendKeys
import shirates.core.driver.commandextension.tap
import shirates.core.driver.commandextension.textIs
import shirates.core.storage.data
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Data1 : UITest() {

    @Test
    @Order(10)
    fun data1() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Search Screen]")
                        .tap("[Search Box]")
                }.action {
                    it.sendKeys(data("[product1].product_name"))
                }.expectation {
                    it.textIs("Super High Tension")
                }
            }
        }

    }
}