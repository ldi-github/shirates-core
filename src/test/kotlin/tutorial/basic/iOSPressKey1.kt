package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.wait
import shirates.core.testcode.UITest

@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class iOSPressKey1 : UITest() {

    @Test
    @Order(10)
    fun pressBack() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Search Screen]")
                    it.select("[SpotlightSearchField]")
                        .clearInput()
                        .sendKeys("safa")
                        .tap("Safari")
                        .wait()
                        .appIs("Safari")
                }.action {
                    it.pressBack()
                }.expectation {
                    it.screenIs("[iOS Search Screen]")
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
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.pressHome()
                }.expectation {
                    it.exist(".XCUIElementTypePageIndicator")
                        .exist("#Safari")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun pressEnter() {

        scenario {
            case(1) {
                condition {
                    it.pressHome()
                        .swipeCenterToBottom()
                        .tap("#SpotlightSearchField")
                        .clearInput()
                        .sendKeys("safari")
                }.action {
                    it.pressEnter()
                }.expectation {
                    it.appIs("Safari")
                }
            }
        }
    }

}