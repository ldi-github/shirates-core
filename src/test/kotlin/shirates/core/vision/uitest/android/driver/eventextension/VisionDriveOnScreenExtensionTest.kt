package shirates.core.vision.uitest.android.driver.eventextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.testContext
import shirates.core.logging.printWarn
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.driver.eventextension.onScreen
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class VisionDriveOnScreenExtensionTest : VisionTest() {

    @Test
    @Order(10)
    fun onScreen1() {

        onScreen("[Network & internet Screen]") { c ->
            printWarn("${c.screenName} is displayed.")
        }
        assertThat(testContext.visionDriveScreenHandlers.count()).isEqualTo(1)

        onScreen("[System Screen]") { c ->
            printWarn("${c.screenName} is displayed.")
        }
        assertThat(testContext.visionDriveScreenHandlers.count()).isEqualTo(2)

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    assertThat(testContext.visionDriveScreenHandlers.count()).isEqualTo(2)
                    it.tap("[Network & internet]")
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                    assertThat(testContext.visionDriveScreenHandlers.count()).isEqualTo(1)
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    assertThat(testContext.visionDriveScreenHandlers.count()).isEqualTo(1)
                    it.tapWithScrollDown("[System]")
                }.expectation {
                    it.screenIs("[System Screen]")
                    assertThat(testContext.visionDriveScreenHandlers.count()).isEqualTo(0)
                }
            }
        }

        assertThat(testContext.visionDriveScreenHandlers.count()).isEqualTo(0)
    }

    @Test
    @Order(20)
    fun onScreen2() {

        onScreen("[Network & internet Screen]") { c ->
            printWarn("${c.screenName} is displayed.")
        }

        onScreen("[System Screen]") { c ->
            printWarn("${c.screenName} is displayed.")
        }

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                    assertThat(testContext.visionDriveScreenHandlers.count()).isEqualTo(2)
                    disableScreenHandler()
                }.action {
                    it.tapWithScrollUp("[Network & internet]")
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                    assertThat(testContext.visionDriveScreenHandlers.count()).isEqualTo(2)
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                    it.screenIs("[Android Settings Top Screen]")
                    enableScreenHandler()
                }.action {
                    it.tapWithScrollDown("[System]")
                }.expectation {
                    it.screenIs("[System Screen]")
                    assertThat(testContext.visionDriveScreenHandlers.count()).isEqualTo(1)
                }
            }
        }

    }

}