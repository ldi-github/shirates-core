package shirates.core.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveKeyboardExtensionTest2 : UITest() {

    @Test
    @Order(30)
    fun pressEnter_search() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Search Screen]")
                        .tap("#SpotlightSearchField")
                        .sendKeys("appium")
                        .keyboardIsShown()
                }.action {
                    it.pressEnter()
                }.expectation {
                    it.keyboardIsNotShown()
                }
            }
        }

    }

    @Test
    @Order(40)
    fun pressSearch() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Search Screen]")
                        .tap("#SpotlightSearchField")
                        .sendKeys("appium")
                        .keyboardIsShown()
                }.action {
                    it.pressSearch()
                }.expectation {
                    it.keyboardIsNotShown()
                }
            }
        }
    }

    @Test
    @Order(50)
    fun tapSoftwareKey() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Search Screen]")
                        .tap("#SpotlightSearchField")
                        .sendKeys("appium")
                        .keyboardIsShown()
                }.action {
                    it.tapSoftwareKey("#Search")
                }.expectation {
                    it.keyboardIsNotShown()
                }
            }
            case(2) {
                expectation {
                    try {
                        it.tapSoftwareKey("#Search")
                        NG()
                    } catch (t: Throwable) {
                        OK(t.message!!)
                    }
                }
            }
        }

    }

}