package shirates.core.vision.uitest.android.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisIsFalse
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class VisionDriveDetectExtensionTest4 : VisionTest() {

    @Test
    fun canDetectWithScrollDown_canDetectWithScrollUp() {

        scenario {
            case(1) {
                condition {
                    it.restartApp()
                        .macro("[Android Settings Top Screen]")
                }.expectation {
                    it.canDetectWithScrollDown(expression = "Battery").thisIsTrue()
                    it.canDetectWithScrollDown(expression = "Tips & support").thisIsTrue()
                    it.canDetectWithScrollDown(expression = "no exist").thisIsFalse()

                    it.canDetectWithScrollUp(expression = "System").thisIsTrue()
                    it.canDetectWithScrollUp(expression = "Display").thisIsTrue()
                    it.canDetectWithScrollUp(expression = "Apps").thisIsTrue()
                    it.canDetectWithScrollUp(expression = "no exist").thisIsFalse()
                }
            }
        }

    }


    @Test
    fun withScrollDownCanDetectAll_withScrollUpCanDetectAll() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    withScrollDown {
                        it.canDetectAll("Network & internet", "Storage", "System")
                            .thisIsTrue()
                    }

                    it.flickAndGoUp()

                    withScrollDown {
                        it.canDetectAll("Accessibility", "System", "Network & internet")
                            .thisIsFalse()
                    }

                    withScrollUp {
                        it.canDetectAll("System", "Accessibility", "Network & internet")
                            .thisIsTrue()
                    }

                    withScrollUp {
                        it.flickAndGoDown()
                            .canDetectAll("Network & internet", "Storage", "System")
                            .thisIsFalse()
                    }

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
                    it.flickAndGoDownTurbo(repeat = 2)
                }.expectation {
                    it.exist("Tips & support")
                }
            }
            case(2) {
                action {
                    it.flickAndGoUpTurbo(repeat = 2)
                }.expectation {
                    it.exist("Settings")
                }
            }
        }
    }

}