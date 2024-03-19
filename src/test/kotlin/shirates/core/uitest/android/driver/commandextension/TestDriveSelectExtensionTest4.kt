package shirates.core.uitest.android.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveSelectExtensionTest4 : UITest() {

    @Test
    fun canSelectWithScrollDown_canSelectWithScrollUp() {

        scenario {
            case(1) {
                condition {
                    it.restartApp()
                        .macro("[Android Settings Top Screen]")
                }.expectation {
                    it.canSelectWithScrollDown(expression = "[Battery]", log = true).thisIsTrue()
                    it.canSelectWithScrollDown(expression = "[Tips & support]", log = true).thisIsTrue()
                    it.canSelectWithScrollDown(expression = "no exist", log = true).thisIsFalse()

                    it.canSelectWithScrollUp(expression = "[System]", log = true).thisIsTrue()
                    it.canSelectWithScrollUp(expression = "[Display]", log = true).thisIsTrue()
                    it.canSelectWithScrollUp(expression = "[Apps]", log = true).thisIsTrue()
                    it.canSelectWithScrollUp(expression = "no exist", log = true).thisIsFalse()
                }
            }
        }

    }


    @Test
    fun canSelectAllWithScrollDown_canSelectAllWithScrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.canSelectAllWithScrollDown("[Network & internet]", "[Storage]", "[System]", log = true)
                        .thisIsTrue()
                    it.flickAndGoUp()
                        .canSelectAllWithScrollDown("[Accessibility]", "[System]", "[Network & internet]", log = true)
                        .thisIsFalse()

                    it.canSelectAllWithScrollUp("[System]", "[Accessibility]", "[Network & internet]", log = true)
                        .thisIsTrue()
                    it.flickAndGoDown()
                        .canSelectAllWithScrollUp("[Network & internet]", "[Storage]", "[System]", log = true)
                        .thisIsFalse()

                    it.flickAndGoUpTurbo()
                    it.flickAndGoDownTurbo()
                }
            }
        }
    }

    @Test
    fun flickAndGoDownTurbo_flickAndGoUpTurbo() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.flickAndGoDownTurbo()
                }.expectation {
                    it.exist("Tips & support")
                }
            }
            case(2) {
                action {
                    it.flickAndGoUpTurbo()
                }.expectation {
                    it.exist("Settings")
                }
            }
        }
    }

}