package shirates.spec.uitest

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.android
import shirates.core.driver.branchextension.ios
import shirates.core.driver.branchextension.specialTag
import shirates.core.driver.commandextension.*
import shirates.core.testcode.SheetName
import shirates.core.testcode.UITest

@SheetName("SheetName1")
@Testrun("unitTestConfig/android/androidSettings/no-load.testrun.properties")
class SpecReportNoLoadRunTest : UITest() {

    @Test
    @DisplayName("condition_only")
    fun S100() {

        scenario {
            case(1) {
                condition {
                    it.caption("caption1")
                        .describe("describe1")
                    it.caption("caption2")
                        .manual("manual1")
                }
            }
        }
    }

    @Test
    @DisplayName("action_only")
    fun S200() {

        scenario {
            case(1) {
                action {
                    it.caption("caption1")
                        .describe("describe1")
                    it.caption("caption2")
                        .manual("manual1")
                }
            }
        }
    }

    @Test
    @DisplayName("expectation_only")
    fun S300() {

        scenario {
            case(1) {
                expectation {
                    it.caption("caption1")
                        .describe("describe1")
                    it.caption("caption2")
                        .manual("manual1")
                }
            }
        }
    }

    @Test
    @DisplayName("scenario1")
    fun S1000() {

        scenario {
            case(1) {
                condition {
                    it.caption("caption C1")
                        .describe("describe C1-1")
                        .describe("describe C1-2")
                    it.caption("caption C2")
                        .describe("describe C2-1")
                        .describe("describe C2-2")
                }.action {
                    it.caption("caption A1")
                        .describe("describe A1-1")
                        .describe("describe A1-2")
                    it.caption("caption A2")
                        .describe("describe A2-1")
                        .describe("describe A2-2")
                }.expectation {
                    it.screenIs("[Android Settings Top Screen]")
                    it.caption("E1")
                        .describe("describe E1-1")
                        .describe("describe E1-2")
                    it.caption("E2")
                        .describe("describe E2-1")
                        .describe("describe E2-2")

                    it.target("target T1")
                        .caption("caption T1")
                        .describe("expectation has no test result")
                        .describe("if neither assertion nor manua specified")
                        .android {
                            it.caption("os branch")
                            specialTag("S1") {
                                it.caption("specialTag S1")
                                    .describe("describe S1-1")
                                    .screenIs("[AAA Screen]")
                                    .describe("describe S1-2")
                                    .manual("manual S1-2")
                            }
                        }
                        .ios {
                            it.caption("os branch")
                            specialTag("S2") {
                                it.caption("S2")
                                    .describe("describe S2-1")
                                    .manual("manual S2-1")
                                    .describe("describe S2-2")
                                    .manual("manual S2-2")
                            }
                        }

                    it.target("target T2")
                        .caption("T2")
                        .describe("describe T2-1")
                        .describe("describe T2-2")
                        .android {
                            describe("android")
                            specialTag("S1") {
                                it.caption("S1")
                                    .describe("describe S1-1")
                                    .manual("manual S1-1")
                                    .describe("describe S1-2")
                                    .manual("manual S1-2")
                            }
                        }
                        .ios {
                            describe("ios")
                            specialTag("S2") {
                                it.caption("S2")
                                    .describe("describe S2-1")
                                    .manual("manual S2-1")
                                    .describe("describe S2-2")
                                    .manual("manual S2-2")
                            }
                        }
                }
            }
        }
    }
}