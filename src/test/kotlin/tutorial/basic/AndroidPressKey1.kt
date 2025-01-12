package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class AndroidPressKey1 : UITest() {

    @Test
    @Order(10)
    fun pressBack() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Network & internet Screen]")
                }.action {
                    it.pressBack()
                }.expectation {
                    it.screenIs("[Android Settings Top Screen]")
                }
            }

        }
    }

    @Test
    @Order(20)
    fun pressHome() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Network & internet Screen]")
                }.action {
                    it.pressHome()
                }.expectation {
                    it.screenIs("[Android Home Screen]")
                }
            }

        }
    }

    @Test
    @Order(30)
    fun pressSearch() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Search Screen]")
                        .sendKeys("clock")
                }.action {
                    it.pressSearch()
                }.expectation {
                    it.exist("Open Clock app")
                }
            }
        }

    }

}