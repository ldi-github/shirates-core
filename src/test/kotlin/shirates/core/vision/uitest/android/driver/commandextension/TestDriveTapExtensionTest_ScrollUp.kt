package shirates.core.vision.uitest.android.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.flickBottomToTop
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.tapWithScrollUp
import shirates.core.vision.testDriveScope
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveTapExtensionTest_ScrollUp : VisionTest() {

    @Test
    fun tapWithScrollUp_accessStartsWith() {

        testDriveScope {
            scenario {
                case(1) {
                    condition {
                        it.macro("[Connected devices Screen]")
                    }.action {
                        it.tapWithScrollUp("@Navigate*")
                    }.expectation {
                        it.screenIs("[Android Settings Top Screen]")
                    }
                }
            }
        }
    }

    @Test
    fun tapWithScrollUp_xpath() {

        testDriveScope {
            scenario {
                case(1) {
                    condition {
                        it.macro("[Android Settings Top Screen]")
                            .flickBottomToTop()
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