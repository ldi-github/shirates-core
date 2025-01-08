package shirates.core.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriverEventContext
import shirates.core.driver.branchextension.ifCanSelect
import shirates.core.driver.commandextension.*
import shirates.core.driver.wait
import shirates.core.testcode.UITest
import shirates.core.testcode.Want
import utility.handleIrregulars

@Want
@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class TestDriveKeyboardExtensionTest1 : UITest() {

    override fun setEventHandlers(context: TestDriverEventContext) {
        context.irregularHandler = {
            it.handleIrregulars()
        }
    }

    @Test
    @Order(10)
    fun pressBack() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Apple Maps Top Screen]")
                    it.appIs("Maps")
                    it.tap("[Search Maps]")
                        .sendKeys("tokyo tower")
                        .tapSoftwareKey("search||検索")
                        .wait()
                        .ifCanSelect("#xmark") {
                            it.tap()
                        }
                        .tap("Website")
                        .tap("Open in Safari")
                        .wait(10.0)
                        .appIs("Safari")
                }.action {
                    it.pressBack()
                }.expectation {
                    it.appIs("Maps")
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
                    it.exist("#Home screen icons")
                }
            }
        }
    }

}