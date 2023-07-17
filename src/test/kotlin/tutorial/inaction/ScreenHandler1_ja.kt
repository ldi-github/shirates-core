package tutorial.inaction

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.eventextension.onScreen
import shirates.core.logging.printWarn
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class ScreenHandler1_ja : UITest() {

    @Test
    @Order(10)
    fun onScreen1() {

        onScreen("[ネットワークとインターネット]") { c ->
            printWarn("${c.screenName} が表示されました。")
        }
        onScreen("[System Screen]") { c ->
            printWarn("${c.screenName} が表示されました。")
        }

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android設定トップ画面]")
                }.action {
                    it.tap("[ネットワークとインターネット]")
                }.expectation {
                    it.screenIs("[ネットワークとインターネット画面]")
                }
            }

            case(2) {
                condition {
                    it.pressBack()
                    it.screenIs("[Android設定トップ画面]")
                }.action {
                    it.tapWithScrollDown("[システム]")
                }.expectation {
                    it.screenIs("[システム画面]")
                }
            }
        }
    }

}