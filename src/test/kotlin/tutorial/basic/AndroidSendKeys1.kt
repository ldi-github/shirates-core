package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
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

    @Test
    @Order(20)
    fun clearInput() {

        scenario {
            case(1) {
                condition {
                    it.restartApp()
                        .macro("[Android Settings Search Screen]")
                        .select("[Search Box]")
                        .textIs("Search settings")
                        .sendKeys("clock")
                        .textIs("clock")
                }.action {
                    it.clearInput()
                }.expectation {
                    it.select("[Search Box]")
                        .textIs("Search settings")
                }
            }
        }
    }
}