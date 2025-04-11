package shirates.core.vision.uitest.android.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.driver.tempSelector
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class VisionDriveTapExtensionTest_DownUpTest1 : VisionTest() {

    @Order(10)
    @Test
    fun tapWithScrollDown_tapWithScrollUp_nickname() {

        scenario {
            tempSelector("[System]", "System")
            tempSelector("[Connected devices]", "Connected devices")

            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.tapWithScrollDown("[System]")
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                        .screenIs("[Android Settings Top Screen]")
                }.action {
                    it.tapWithScrollUp(expression = "[Connected devices]")
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
        }
    }

    @Order(20)
    @Test
    fun tapWithScrollUp_tapWithScrollUp_text() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.tapWithScrollDown("System")
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                }.action {
                    it.tapWithScrollUp("Connected devices")
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
        }
    }

}