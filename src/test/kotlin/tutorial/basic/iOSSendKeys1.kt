package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class iOSSendKeys1 : UITest() {

    @Test
    @Order(10)
    fun sendKeys() {

        scenario {
            case(1) {
                condition {
                    it.pressHome()
                        .swipeCenterToBottom()
                        .tap("[SpotlightSearchField]")
                        .clearInput()
                }.action {
                    it.sendKeys("safari")
                }.expectation {
                    it.textIs("safari")
                }
            }
        }
    }

}