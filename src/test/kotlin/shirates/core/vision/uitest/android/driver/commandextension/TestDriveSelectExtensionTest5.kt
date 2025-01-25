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
class TestDriveSelectExtensionTest5 : VisionTest() {

    @Test
    fun canDetectWithoutScroll() {

        scenario {
            case(1) {
                condition {
                    it.restartApp()
                        .macro("[Android Settings Top Screen]")
                }.action {
                    withScrollDown {
                        b1 = canDetectWithoutScroll("Accessibility")
                    }
                }.expectation {
                    b1.thisIsFalse("<Accessibility> not found.")
                }
            }
            case(2) {
                action {
                    withScrollDown {
                        b1 = canDetect("Accessibility")
                    }
                }.expectation {
                    b1.thisIsTrue("<Accessibility> found.")
                }
            }
        }

    }

    @Test
    fun canDetectAll() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.canDetectAll("Network & internet", "Apps", "Battery")
                        .thisIsTrue()
                    it.canDetectAll("Network & internet", "Apps", "Battery", "Accessibility")
                        .thisIsFalse()
                }
            }
            case(2) {
                expectation {
                    withScrollDown {
                        it.canDetectAll("Network & internet", "Apps", "Battery", "Accessibility")
                            .thisIsTrue()
                    }
                }
            }
            case(3) {
                expectation {
                    withScrollUp {
                        it.canDetectAll("Network & internet", "Apps", "Battery", "Accessibility")
                            .thisIsFalse()
                    }
                }
            }
            case(4) {
                condition {
                    it.flickAndGoDown(repeat = 2)
                }.expectation {
                    withScrollUp {
                        it.canDetectAll("Accessibility", "Battery", "Apps", "Network & internet")
                            .thisIsTrue()
                    }
                }
            }
        }
    }

}