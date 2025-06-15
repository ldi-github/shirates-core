package shirates.core.vision.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class VisionDriveKeyboardExtensionTest1 : VisionTest() {

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
                    it.exist("Reminders")
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
                    it.macro("[iOS Search Screen]")
                }.action {
                    it.sendKeys("fitness")
                        .pressEnter()
                }
            }
        }
    }

}