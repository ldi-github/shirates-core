package shirates.core.vision.uitest.android.driver.branchextension

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.exception.TestConfigException
import shirates.core.testcode.Want
import shirates.core.vision.driver.branchextension.android
import shirates.core.vision.driver.branchextension.result.VisionDriveScreenCompareResult
import shirates.core.vision.driver.commandextension.describe
import shirates.core.vision.driver.commandextension.macro
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/vision/android/androidSettings/testrun.properties")
class VisionDriveScreenCompareResultTest : VisionTest() {

    @Order(10)
    @Test
    fun ifScreenIs_ifScreenIsOf_ifElse() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    val result = VisionDriveScreenCompareResult()
                    result
                        .ifScreenIs("[Network & internet Screen]") {
                            describe("never called")
                        }
                        .ifScreenIs("[Android Settings Top Screen]") {
                            describe("called")
                        }
                        .ifScreenIsOf("[Android Settings Top Screen]", "[Network & internet Screen]") {
                            describe("called")
                        }
                        .ifElse {
                            describe("never called")
                        }
                    assertThat(result.anyMatched).isTrue()
                    assertThat(result.history.count()).isEqualTo(2)
                    assertThat(result.history[0].condition).isEqualTo("if screen is [Android Settings Top Screen]")
                    assertThat(result.history[0].matched).isTrue()
                    assertThat(result.history[1].condition).isEqualTo("if screen is of ([Android Settings Top Screen] or [Network & internet Screen])")
                    assertThat(result.history[1].matched).isTrue()
                }.expectation {
                    android {
                        OK("called")
                    }

                    val result = VisionDriveScreenCompareResult()
                    result
                        .ifScreenIs("[Network & internet Screen]") {
                            NG("never called")
                        }
                        .ifScreenIs("[Android Settings Top Screen]") {
                            OK("called")
                        }
                        .ifScreenIsOf("[Android Settings Top Screen]", "[Network & internet Screen]") {
                            OK("called")
                        }
                        .ifElse {
                            NG("never called")
                        }
                    assertThat(result.anyMatched).isTrue()
                    assertThat(result.history.count()).isEqualTo(2)
                    assertThat(result.history[0].condition).isEqualTo("if screen is [Android Settings Top Screen]")
                    assertThat(result.history[0].matched).isTrue()
                    assertThat(result.history[1].condition).isEqualTo("if screen is of ([Android Settings Top Screen] or [Network & internet Screen])")
                    assertThat(result.history[1].matched).isTrue()
                }
            }
            case(2) {
                condition {
                    it.macro("[Network & internet Screen]")
                }.expectation {
                    val result = VisionDriveScreenCompareResult()
                    result
                        .ifScreenIs("[Network & internet Screen]") {
                            OK("called")
                        }
                        .ifScreenIs("[Android Settings Top Screen]") {
                            NG("never called")
                        }
                        .ifElse {
                            NG("never called")
                        }
                    assertThat(result.anyMatched).isTrue()
                    assertThat(result.history.count()).isEqualTo(1)
                    assertThat(result.history[0].condition).isEqualTo("if screen is [Network & internet Screen]")
                    assertThat(result.history[0].matched).isTrue()
                }
            }
            case(3) {
                expectation {
                    val result = VisionDriveScreenCompareResult()
                    result
                        .ifScreenIs("[About phone Screen]") {
                            OK("called")
                        }
                    assertThat(result.anyMatched).isFalse()
                    assertThat(result.history.count()).isEqualTo(0)
                }
            }
            case(4) {
                condition {
                    it.macro("[Connected devices Screen]")
                }.expectation {
                    val result = VisionDriveScreenCompareResult()
                    result
                        .ifScreenIs("[Network & internet Screen]") {
                            NG("never called")
                        }
                        .ifScreenIs("[Android Settings Top Screen]") {
                            NG("never called")
                        }
                        .ifElse {
                            OK("called")
                        }
                    assertThat(result.anyMatched).isTrue()
                    assertThat(result.history.count()).isEqualTo(1)
                    assertThat(result.history[0].condition).isEqualTo("if else")
                    assertThat(result.history[0].matched).isTrue()
                }
            }
            case(5) {
                expectation {
                    val result = VisionDriveScreenCompareResult()
                    assertThatThrownBy {
                        result.ifScreenIs("[not exist screen]") {
                            NG("never called")
                        }
                    }.isInstanceOf(TestConfigException::class.java)
                        .hasMessage("screenName is not registered in ScreenClassifier. (screenName=[not exist screen])")
                }
            }
        }
    }

    @Order(20)
    @Test
    fun ifScreenIsNot_ifScreenIsNotOf_ifElse() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    val result = VisionDriveScreenCompareResult()
                    result
                        .ifScreenIs("[Android Settings Top Screen]") {
                            OK("called")
                        }
                        .ifScreenIsNot("[Android Settings Top Screen]") {
                            NG("never called")
                        }
                        .ifScreenIsNot("[Network & internet Screen]") {
                            OK("called")
                        }
                    assertThat(result.anyMatched).isTrue()
                    assertThat(result.history.count()).isEqualTo(2)
                    assertThat(result.history[0].condition).isEqualTo("if screen is [Android Settings Top Screen]")
                    assertThat(result.history[0].matched).isTrue()
                    assertThat(result.history[1].condition).isEqualTo("if screen is not [Network & internet Screen]")
                    assertThat(result.history[1].matched).isTrue()
                }
            }
            case(2) {
                expectation {
                    val result = VisionDriveScreenCompareResult()
                    result
                        .ifScreenIsNotOf("[Network & internet Screen]", "[Connected devices Screen]") {
                            OK("called")
                        }
                        .ifElse {
                            NG("never called")
                        }
                    assertThat(result.anyMatched).isTrue()
                    assertThat(result.history.count()).isEqualTo(1)
                    assertThat(result.history[0].condition).isEqualTo("if screen is not of ([Network & internet Screen] or [Connected devices Screen])")
                    assertThat(result.history[0].matched).isTrue()
                }
            }
        }

    }

    @Order(30)
    @Test
    fun multipleCallTest() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    val result = VisionDriveScreenCompareResult()
                    result
                        .ifScreenIs("[Android Settings Top Screen]") {
                            OK("called")
                        }.ifScreenIs("[Android Settings Top Screen]") {
                            OK("called")
                        }.ifElse {
                            NG("never called")
                        }.ifElse {
                            NG("never called")
                        }
                    assertThat(result.anyMatched).isTrue()
                    assertThat(result.history.count()).isEqualTo(2)
                    assertThat(result.history[0].condition).isEqualTo("if screen is [Android Settings Top Screen]")
                    assertThat(result.history[0].matched).isTrue()
                    assertThat(result.history[1].condition).isEqualTo("if screen is [Android Settings Top Screen]")
                    assertThat(result.history[1].matched).isTrue()
                }
            }
            case(2) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    val result = VisionDriveScreenCompareResult()
                    result
                        .ifScreenIsNot("[Android Settings Top Screen]") {
                            NG("never called")
                        }.ifScreenIsNot("[Android Settings Top Screen]") {
                            NG("never called")
                        }.ifElse {
                            OK("called")
                        }.ifElse {
                            NG("never called")
                        }
                    assertThat(result.anyMatched).isTrue()
                    assertThat(result.history.count()).isEqualTo(1)
                    assertThat(result.history[0].condition).isEqualTo("if else")
                    assertThat(result.history[0].matched).isTrue()
                }
            }
            case(3) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    val result = VisionDriveScreenCompareResult()
                    result
                        .ifScreenIsNot("[Android Settings Top Screen]") {
                            NG("never called")
                        }.ifScreenIs("[Android Settings Top Screen]") {
                            OK("called")
                        }.ifElse {
                            NG("never called")
                        }.ifElse {
                            NG("never called")
                        }
                    assertThat(result.anyMatched).isTrue()
                    assertThat(result.history.count()).isEqualTo(1)
                    assertThat(result.history[0].condition).isEqualTo("if screen is [Android Settings Top Screen]")
                    assertThat(result.history[0].matched).isTrue()
                }
            }
        }
    }

}