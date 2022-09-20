package shirates.core.uitest.android.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.ifScreenIs
import shirates.core.driver.branchextension.ifScreenIsNot
import shirates.core.driver.branchextension.result.ScreenCompareResult
import shirates.core.driver.commandextension.macro
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class ScreenCompareResultTest : UITest() {

    @Order(10)
    @Test
    fun ifScreenIs_ifElse() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    val result = ScreenCompareResult()
                    result
                        .ifScreenIs("[Network & internet Screen]", onTrue = {
                            NG("never called")
                        })
                        .ifScreenIs("[Android Settings Top Screen]", onTrue = {
                            OK("called")
                        })
                        .ifElse {
                            NG("never called")
                        }
                }
            }
            case(2) {
                condition {
                    it.macro("[Network & internet Screen]")
                }.expectation {
                    val result = ScreenCompareResult()
                    result
                        .ifScreenIs("[Network & internet Screen]", onTrue = {
                            OK("called")
                        })
                        .ifScreenIs("[Android Settings Top Screen]", onTrue = {
                            NG("never called")
                        })
                        .ifElse {
                            NG("never called")
                        }
                }
            }
            case(3) {
                condition {
                    it.macro("[Connected devices Screen]")
                }.expectation {
                    val result = ScreenCompareResult()
                    result
                        .ifScreenIs("[Network & internet Screen]", onTrue = {
                            NG("never called")
                        })
                        .ifScreenIs("[Android Settings Top Screen]", onTrue = {
                            NG("never called")
                        })
                        .ifElse {
                            OK("called")
                        }
                }
            }
            case(4) {
                expectation {
                    val result = ScreenCompareResult()
                    result.ifScreenIs {
                        NG("never called")
                    }.ifElse {
                        OK("called")
                    }
                }
            }
        }
    }

    @Order(20)
    @Test
    fun ifScreenIs_not() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    ifScreenIs("[Android Settings Top Screen]") {
                        OK("called")
                    }.not {
                        NG("never called")
                    }
                }
            }
            case(2) {
                expectation {
                    ifScreenIs("[Connected devices]") {
                        NG("never called")
                    }.not {
                        OK("called")
                    }
                }
            }
            case(3) {
                expectation {
                    ifScreenIsNot {
                        NG("Never called")
                    }.ifElse {
                        OK("Called")
                    }
                }
            }
        }

    }

    @Order(30)
    @Test
    fun ifScreenIsNot_not() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    ifScreenIsNot("[Android Settings Top Screen]") {
                        NG("never called")
                    }.not {
                        OK("called")
                    }

                    ifScreenIsNot("[Connected devices]") {
                        OK("called")
                    }.not {
                        NG("never called")
                    }
                }
            }
        }

    }

    @Order(40)
    @Test
    fun multipleCallTest() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    ifScreenIs("[Android Settings Top Screen]") {
                        OK("called")
                    }.ifScreenIs("[Android Settings Top Screen]") {
                        OK("called")
                    }.not {
                        NG("never called")
                    }.not {
                        NG("never called")
                    }
                }
            }
            case(2) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    ifScreenIsNot("[Android Settings Top Screen]") {
                        NG("never called")
                    }.ifScreenIsNot("[Android Settings Top Screen]") {
                        NG("never called")
                    }.not {
                        OK("called")
                    }.not {
                        NG("never called")
                    }
                }
            }
            case(3) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    ifScreenIsNot("[Android Settings Top Screen]") {
                        NG("never called")
                    }.ifScreenIs("[Android Settings Top Screen]") {
                        OK("called")
                    }.not {
                        NG("never called")
                    }.not {
                        OK("called")
                    }
                }
            }
        }
    }

}