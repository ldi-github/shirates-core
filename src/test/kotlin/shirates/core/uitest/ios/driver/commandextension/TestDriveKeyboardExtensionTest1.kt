package shirates.core.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriverEventContext
import shirates.core.driver.commandextension.*
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
                    it.screenIs("[iOS Settings Top Screen]")
                        .launchApp("Maps")
                        .appIs("[Maps]")
                }.action {
                    it.pressBack()
                }.expectation {
                    it.appIs("[Settings]")
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