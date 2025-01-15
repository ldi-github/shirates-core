package shirates.core.vision.uitest.android.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestDriveTapExtensionTest_DownUpTest2 : VisionTest() {

    @Order(30)
    @Test
    fun tapWithScrollDown_tapWithScrollUp_textStartsWith() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.tapWithScrollDown("Syste*")
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                }.action {
                    it.tapWithScrollUp("Connected*")
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
        }
    }

    @Order(40)
    @Test
    fun tapWithScrollDown_tapWithScrollUp_textContains() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.tapWithScrollDown("*yste*")
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                }.action {
                    it.tapWithScrollUp("*onnected device*")
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
        }
    }

}