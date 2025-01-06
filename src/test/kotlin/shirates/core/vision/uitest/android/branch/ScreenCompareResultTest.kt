package shirates.core.vision.uitest.android.branch

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.result.ScreenCompareResult
import shirates.core.logging.TestLog
import shirates.core.testcode.Want
import shirates.core.vision.driver.branchextension.android
import shirates.core.vision.driver.commandextension.describe
import shirates.core.vision.driver.commandextension.ifScreenIsNot
import shirates.core.vision.driver.commandextension.macro
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/vision/android/androidSettings/testrun.properties")
class ScreenCompareResultTest : VisionTest() {

    @Order(10)
    @Test
    fun ifScreenIs_ifElse() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    val result = ScreenCompareResult()
                    result
                        .ifScreenIs("[Network & internet Screen]") {
                            describe("never called")
                        }
                        .ifScreenIs("[Android Settings Top Screen]") {
                            describe("called")
                        }
                        .ifScreenIs("[Android Settings Top Screen]", "[Network & internet Screen]") {
                            describe("called")
                        }
                        .ifElse {
                            describe("never called")
                        }
                    assertThat(result.anyMatched).isTrue()
                    assertThat(result.history.count()).isEqualTo(2)
                    assertThat(result.history[0].condition).isEqualTo("if screen is [Android Settings Top Screen]")
                    assertThat(result.history[0].matched).isTrue()
                    assertThat(result.history[1].condition).isEqualTo("if screen is ([Android Settings Top Screen] or [Network & internet Screen])")
                    assertThat(result.history[1].matched).isTrue()
                }.expectation {
                    android {
                        OK("called")
                    }

                    val result = ScreenCompareResult()
                    result
                        .ifScreenIs("[Network & internet Screen]") {
                            NG("never called")
                        }
                        .ifScreenIs("[Android Settings Top Screen]") {
                            OK("called")
                        }
                        .ifScreenIs("[Android Settings Top Screen]", "[Network & internet Screen]") {
                            OK("called")
                        }
                        .ifElse {
                            NG("never called")
                        }
                    assertThat(result.anyMatched).isTrue()
                    assertThat(result.history.count()).isEqualTo(2)
                    assertThat(result.history[0].condition).isEqualTo("if screen is [Android Settings Top Screen]")
                    assertThat(result.history[0].matched).isTrue()
                    assertThat(result.history[1].condition).isEqualTo("if screen is ([Android Settings Top Screen] or [Network & internet Screen])")
                    assertThat(result.history[1].matched).isTrue()
                }
            }
            case(2) {
                condition {
                    it.macro("[Network & internet Screen]")
                }.expectation {
                    val result = ScreenCompareResult()
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
                    val result = ScreenCompareResult()
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
                    val result = ScreenCompareResult()
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
                    val result = ScreenCompareResult()
                    assertThatThrownBy {
                        result
                            .ifScreenIs {
                                NG("never called")
                            }
                    }.isInstanceOf(IllegalArgumentException::class.java)
                        .hasMessage("screenNames is required.")
                }
            }
            case(6) {
                expectation {
                    val result = ScreenCompareResult()
                    result.ifScreenIs("[not exist screen]") {
                        NG("never called")
                    }
                    assertThat(TestLog.lastTestLog?.message).startsWith("screenName '[not exist screen]' is not registered in ")
                }
            }
        }
    }

    @Order(20)
    @Test
    fun ifScreenIsNot_ifElse() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    val result = ScreenCompareResult()
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
                    val result = ScreenCompareResult()
                    result
                        .ifScreenIsNot("[Network & internet Screen]", "[Connected devices Screen]") {
                            OK("called")
                        }
                        .ifElse {
                            NG("never called")
                        }
                    assertThat(result.anyMatched).isTrue()
                    assertThat(result.history.count()).isEqualTo(1)
                    assertThat(result.history[0].condition).isEqualTo("if screen is not ([Network & internet Screen] or [Connected devices Screen])")
                    assertThat(result.history[0].matched).isTrue()
                }
            }
            case(3) {
                expectation {
                    assertThatThrownBy {
                        ifScreenIsNot {
                            NG("Never called")
                        }
                    }.isInstanceOf(IllegalArgumentException::class.java)
                        .hasMessage("screenNames is required.")
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
                    val result = ScreenCompareResult()
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
                    val result = ScreenCompareResult()
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
                    val result = ScreenCompareResult()
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