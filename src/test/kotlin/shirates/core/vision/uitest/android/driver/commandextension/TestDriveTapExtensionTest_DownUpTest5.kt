package shirates.core.vision.uitest.android.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.vision.testDriveScope
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestDriveTapExtensionTest_DownUpTest5 : VisionTest() {

    @Order(80)
    @Test
    fun tapWithScrollDown_tapWithScrollUp_id() {

        testDriveScope {
            scenario {
                case(1) {
                    condition {
                        it.macro("[Android Settings Top Screen]")
                    }.action {
                        it.tapWithScrollDown("#android:id/title&&Security & privacy")
                    }.expectation {
                        it.exist("[<-]")
                    }
                }
                case(2) {
                    condition {
                        it.pressBack()
                    }.action {
                        it.tapWithScrollUp("#search_action_bar_title")
                    }.expectation {
                        it.exist("[<-]")
                    }
                }
            }
        }
    }

    @Order(90)
    @Test
    fun tapWithScrollDown_tapWithScrollUp_xpath() {

        testDriveScope {
            scenario {
                case(1) {
                    condition {
                        it.macro("[Android Settings Top Screen]")
                    }.action {
                        it.tapWithScrollDown("xpath=//*[@text='Accessibility']")
                    }.expectation {
                        it.screenIs("[Accessibility Screen]")
                    }
                }
                case(2) {
                    condition {
                        it.pressBack()
                    }.action {
                        it.tapWithScrollUp("xpath=//*[@text='Accessibility']")
                    }.expectation {
                        it.screenIs("[Accessibility Screen]")
                    }
                }
            }
        }
    }

}