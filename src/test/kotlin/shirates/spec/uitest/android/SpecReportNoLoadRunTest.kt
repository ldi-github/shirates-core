package shirates.spec.uitest.android

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.android
import shirates.core.driver.branchextension.ios
import shirates.core.driver.branchextension.specialTag
import shirates.core.driver.commandextension.*
import shirates.core.driver.testProfile
import shirates.core.logging.TestLog
import shirates.core.testcode.SheetName
import shirates.core.testcode.UITest
import shirates.spec.uitest.assertHeader
import shirates.spec.uitest.assertRow
import shirates.spec.uitest.assertRowHeader
import shirates.spec.utilily.ExcelUtility
import shirates.spec.utilily.cells
import shirates.spec.utilily.text
import shirates.spec.utilily.worksheets

@SheetName("SheetName1")
@Testrun("unitTestConfig/android/androidSettings/no-load.testrun.properties")
class SpecReportNoLoadRunTest : UITest() {

    var profileName = ""

    override fun beforeAllAfterSetup(context: ExtensionContext?) {
        profileName = testProfile.profileName
    }

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

                    android {
                        it.screenIs("[AAA Screen]")
                        target("T1")
                            .exist("exist T1")
                        specialTag("S1") {
                            it.caption("specialTag S1")
                                .describe("describe S1-1")
                                .describe("describe S1-2")
                                .manual("manual S1-2")
                        }
                    }
                    ios {
                        it.screenIs("[AAA Screen]")
                        target("T1")
                            .exist("exist T1")
                        specialTag("S2") {
                            it.caption("S2")
                                .describe("describe S2-1")
                                .describe("describe S2-2")
                                .manual("manual S2-2")
                        }
                    }

                    target("T2")
                    android {
                        specialTag("S1") {
                            it.caption("S1")
                                .describe("describe S1-1")
                                .manual("manual S1-1")
                                .describe("describe S1-2")
                                .manual("manual S1-2")
                        }
                    }
                    ios {
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

    override fun finally() {

        val filePath = TestLog.directoryForLog.resolve("SpecReportNoLoadRunTest/SpecReportNoLoadRunTest.xlsx")
        val ws = ExcelUtility.getWorkbook(filePath = filePath).worksheets("SheetName1")
        val commandSheet = ws.workbook.worksheets("CommandList")

        val executionDateTime = commandSheet.cells("B4").text
        val date = executionDateTime.substring(0, 10)

        /**
         * Header
         */
        ws.assertHeader(
            testConfigName = "Settings",
            sheetName = "SheetName1",
            testClassName = "SpecReportNoLoadRunTest",
            profileName = profileName,
            deviceModel = "",
            platformVersion = "",
            noLoadRunMode = "No-Load-Run Mode",
            none = 5,
            manual = 5,
            total = 10
        )

        /**
         * Header Row
         */
        ws.assertRowHeader()

        /**
         * Rows
         */
        with(ws) {
            assertRow(
                rowNum = 10,
                id = 1,
                step = "S100",
                condition = "condition_only"
            )
            assertRow(
                rowNum = 11,
                id = 2,
                step = "1",
                condition = "(caption1)\n- describe1\n(caption2)\n- manual1",
            )
            assertRow(
                rowNum = 12,
                id = 3,
                step = "S200",
                condition = "action_only"
            )
            assertRow(
                rowNum = 13,
                id = 4,
                step = "1",
                action = "(caption1)\n" +
                        "- describe1\n" +
                        "(caption2)\n" +
                        "- manual1"
            )
            assertRow(
                rowNum = 14,
                id = 5,
                step = "S300",
                condition = "expectation_only"
            )
            assertRow(
                rowNum = 15,
                id = 6,
                expectation = "(caption1)\n" +
                        "- describe1\n" +
                        "(caption2)\n" +
                        "- manual1",
                auto = "M",
                result = "MANUAL"
            )
            assertRow(
                rowNum = 16,
                id = 7,
                step = "S1000",
                condition = "scenario1"
            )
            assertRow(
                rowNum = 17,
                id = 8,
                step = "1",
                condition = "(caption C1)\n" +
                        "- describe C1-1\n" +
                        "- describe C1-2\n" +
                        "(caption C2)\n" +
                        "- describe C2-1\n" +
                        "- describe C2-2",
                action = "(caption A1)\n" +
                        "- describe A1-1\n" +
                        "- describe A1-2\n" +
                        "(caption A2)\n" +
                        "- describe A2-1\n" +
                        "- describe A2-2",
                target = "[Android Settings Top Screen]",
                expectation = "- is displayed\n" +
                        "(E1)\n" +
                        "- describe E1-1\n" +
                        "- describe E1-2\n" +
                        "(E2)\n" +
                        "- describe E2-1\n" +
                        "- describe E2-2",
                auto = "A",
                result = "N/A",
            )
            assertRow(
                rowNum = 18,
                id = 9,
                target = "[AAA Screen]",
                expectation = "- is displayed",
                os = "Android",
                auto = "A",
                result = "N/A"
            )
            assertRow(
                rowNum = 19,
                id = 10,
                target = "T1",
                expectation = "- <exist T1>",
                os = "Android",
                auto = "A",
                result = "N/A"
            )
            assertRow(
                rowNum = 20,
                id = 11,
                expectation = "(specialTag S1)\n" +
                        "- describe S1-1\n" +
                        "- describe S1-2\n" +
                        "- manual S1-2",
                os = "Android",
                special = "S1",
                auto = "M",
                result = "MANUAL"
            )
            assertRow(
                rowNum = 21,
                id = 12,
                target = "[AAA Screen]",
                expectation = "- is displayed",
                os = "iOS",
                auto = "A",
                result = "N/A"
            )
            assertRow(
                rowNum = 22,
                id = 13,
                target = "T1",
                expectation = "- <exist T1>",
                os = "iOS",
                auto = "A",
                result = "N/A"
            )
            assertRow(
                rowNum = 23,
                id = 14,
                expectation = "(S2)\n" +
                        "- describe S2-1\n" +
                        "- describe S2-2\n" +
                        "- manual S2-2",
                os = "iOS",
                special = "S2",
                auto = "M",
                result = "MANUAL"
            )
            assertRow(
                rowNum = 24,
                id = 15,
                target = "T2",
                expectation = "(S1)\n" +
                        "- describe S1-1\n" +
                        "- manual S1-1\n" +
                        "- describe S1-2\n" +
                        "- manual S1-2",
                os = "Android",
                special = "S1",
                auto = "M",
                result = "MANUAL"
            )
            assertRow(
                rowNum = 25,
                id = 16,
                expectation = "(S2)\n" +
                        "- describe S2-1\n" +
                        "- manual S2-1\n" +
                        "- describe S2-2\n" +
                        "- manual S2-2",
                os = "iOS",
                special = "S2",
                auto = "M",
                result = "MANUAL"
            )
        }
    }
}