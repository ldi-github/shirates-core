package shirates.core.vision.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriverEventContext
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.driver.wait
import shirates.core.vision.testDriveScope
import shirates.core.vision.testcode.VisionTest
import utility.handleIrregulars

@Want
@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class TestDriveKeyboardExtensionTest1 : VisionTest() {

    override fun setEventHandlers(context: TestDriverEventContext) {
        context.irregularHandler = {
            testDriveScope {
                it.handleIrregulars()
            }
        }
    }

    @Test
    @Order(10)
    fun pressBack() {

        scenario {
            case(1) {
                condition {
                    it.terminateApp("[Maps]")
                    it.macro("[Apple Maps Top Screen]")
                    it.appIs("Maps")
                    it.tap("Search Maps")
                        .sendKeys("google san francisco")
                        .tap("search")
                        .wait()
                        .tap("Website")
                        .tap("Open in Safari", directAccess = true)
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
                    it.exist("Reminders")
                }
            }
        }
    }

}