package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.sendKeys
import shirates.core.driver.commandextension.tap
import shirates.core.driver.commandextension.textIs
import shirates.core.storage.account
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Account1 : UITest() {

    @Test
    @Order(10)
    fun account() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                        .tap("Search settings")
                        .screenIs("[Android Settings Search Screen]")
                        .tap("[Search Box]")
                }.action {
                    it.sendKeys(account("[account1].id"))
                }.expectation {
                    it.textIs(account("[account1].id"))
                }
            }
        }

    }
}