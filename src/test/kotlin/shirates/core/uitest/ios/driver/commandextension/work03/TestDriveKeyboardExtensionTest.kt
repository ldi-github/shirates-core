package shirates.core.uitest.ios.driver.commandextension.work03

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriverEventContext
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Want
import utility.handleIrregulars

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveKeyboardExtensionTest : UITest() {

    override fun setEventHandlers(context: TestDriverEventContext) {
        context.irregularHandler = {
            it.handleIrregulars()
        }
    }

    @Test
    fun pressBack() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Search Screen]")
                    it.tap("[SpotlightSearchField]")
                        .clearInput()
                        .sendKeys("safa")
                        .tap(".XCUIElementTypeButton&&Safari")
                        .wait()
                        .appIs("[Safari]")
                }.action {
                    it.pressBack()
                }.expectation {
                    it.screenIs("[iOS Search Screen]")
                }
            }
        }
    }

    @Test
    fun pressHome() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.pressHome()
                }.expectation {
                    it.exist("#Home screen icons")
                }
            }
        }
    }

    @Test
    fun pressEnter_search() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Search Screen]")
                        .tap("#SpotlightSearchField")
                        .clearInput()
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
    fun pressSearch() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Search Screen]")
                        .tap("#SpotlightSearchField")
                        .clearInput()
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
}