package shirates.core.vision.uitest.android.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestDriveTapExtensionTest_DownUpTest3 : VisionTest() {


    @Order(50)
    @Test
    fun tapWithScrollDown_tapWithScrollUp_textEndsWith() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.tapWithScrollDown("*ystem")
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                }.action {
                    it.tapWithScrollUp("*ted devices")
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
        }
    }

    @Order(60)
    @Test
    fun tapWithScrollDown_tapWithScrollUp_textMatches() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.tapWithScrollDown("textMatches=^System$")
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                }.action {
                    it.tapWithScrollUp("textMatches=^Connected devices$")
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
        }
    }

}