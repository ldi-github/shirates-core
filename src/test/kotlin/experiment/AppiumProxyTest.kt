package experiment

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Selector
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.refreshCache
import shirates.core.proxy.AppiumProxy
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class AppiumProxyTest : UITest() {

    @Test
    @Order(10)
    fun find() {

        scenario {
            case(10) {
                action {
                    Selector("[Network & internet]")
                    val result =
                        AppiumProxy.findElement(using = "id", value = "com.android.settings:id/search_action_bar_title")
                    println(result)
                }
            }
        }
    }

    @Test
    @Order(20)
    fun getSource() {

        scenario {
            case(10) {
                action {
                    it.refreshCache()
                    it.refreshCache()
                    it.refreshCache()
                    it.refreshCache()
                    it.refreshCache()
                    it.refreshCache()
                    it.refreshCache()
                    it.refreshCache()
                    it.refreshCache()
                    it.refreshCache()
                }
            }
        }
    }

}