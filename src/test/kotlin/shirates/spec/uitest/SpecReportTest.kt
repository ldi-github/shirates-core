package shirates.spec.uitest

import org.apache.poi.xssf.usermodel.XSSFCell
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.logging.TestLog
import shirates.core.testcode.NoLoadRun
import shirates.core.testcode.SheetName
import shirates.core.testcode.UITest
import shirates.core.utility.format
import shirates.spec.utilily.ExcelUtility
import shirates.spec.utilily.cells
import shirates.spec.utilily.text
import shirates.spec.utilily.worksheets
import java.nio.file.Files
import java.util.*

@SheetName("calculator test")
@Testrun("testConfig/android/calculator/testrun.properties")
class SpecReportTest : UITest() {

    /**
     * Install Calculator app (Google LLC) before running this test.
     */

    @NoLoadRun
    @Test
    @Order(10)
    @DisplayName("calculate 123+456")
    fun s10() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Restart Calculator]")
                    it.screenIs("[Calculator Main Screen]")
                }.action {
                    it
                        .tap("[1]")
                        .tap("[2]")
                        .tap("[3]")
                }.expectation {
                    it.select("[formula]")
                        .textIs("123")
                }
            }

            case(2) {
                action {
                    it.tap("[+]")
                }.expectation {
                    it.select("[formula]")
                        .textIs("123+")
                }
            }

            case(3) {
                action {
                    it
                        .tap("[4]")
                        .tap("[5]")
                        .tap("[6]")
                }.expectation {
                    it.select("[formula]")
                        .textIs("123+456")
                    it.select("[result preview]")
                        .textIs("579")
                }
            }

            case(4) {
                action {
                    it.tap("[=]")
                }.expectation {
                    it.select("[result final]")
                        .textIs("579")
                }
            }
        }
    }

    //    @NoLoadRun
    @Test
    @Order(20)
    @DisplayName("calculate 123+456")
    fun s20() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Restart Calculator]")
                    it.screenIs("[Calculator Main Screen]")
                }.action {
                    it
                        .tap("[1]")
                        .tap("[2]")
                        .tap("[3]")
                }.expectation {
                    it.select("[formula]")
                        .textIs("123")
                }
            }

            case(2) {
                action {
                    it.tap("[+]")
                }.expectation {
                    it.select("[formula]")
                        .textIs("123+")
                }
            }

            case(3) {
                action {
                    it
                        .tap("[4]")
                        .tap("[5]")
                        .tap("[6]")
                }.expectation {
                    it.select("[formula]")
                        .textIs("123+456")
                    it.select("[result preview]")
                        .textIs("579")
                }
            }

            case(4) {
                action {
                    it.tap("[=]")
                }.expectation {
                    it.select("[result final]")
                        .textIs("579")
                }
            }
        }
    }

    override fun finally() {

        var filePath = TestLog.directoryForLog.resolve("SpecReportTest/SpecReportTest.xlsx")
        if (Files.exists(filePath).not()) {
            filePath = TestLog.directoryForLog.resolve("SpecReportTest/SpecReportTest@a.xlsx")
        }
        val ws = ExcelUtility.getWorkbook(filePath = filePath).worksheets("calculator test")

        fun XSSFCell.textIs(expected: String) {

            assertThat(this.text).isEqualTo(expected)
        }

        /**
         * Header
         */
        val deviceModel = if (ws.cells("D4").text.isNotBlank()) "sdk_gphone64_arm64" else ""
        val platformVersion = if (deviceModel.isNotBlank()) "12" else ""
        ws.assertHeader(
            testConfigName = "Calculator",
            sheetName = "calculator test",
            testClassName = "SpecReportTest",
            profileName = "Pixel 3a(Android 12)",
            deviceModel = deviceModel,
            platformVersion = platformVersion,
            ok = 4,
            notImpl = 1,
            total = 5
        )

        val date = Date().format("yyyy/MM/dd")

        /**
         * Row Header
         */
        ws.assertRowHeader()

        /**
         * Rows
         */
        with(ws) {
            assertRow(
                rowNum = 10,
                id = 1,
                step = "s10",
                condition = "calculate 123+456",
            )
            assertRow(
                rowNum = 11,
                id = 2,
                step = "1",
                condition = "- [Restart Calculator]\n" +
                        "- [Calculator Main Screen] is displayed",
                action = "- Tap [1]\n" +
                        "- Tap [2]\n" +
                        "- Tap [3]",
                expectation = "- [formula] is \"123\"",
                auto = "M",
                result = "N/A"
            )
            assertRow(
                rowNum = 12,
                id = 3,
                step = "2",
                action = "- Tap [+]",
                expectation = "- [formula] is \"123+\"",
                auto = "M",
                result = "N/A"
            )
            assertRow(
                rowNum = 13,
                id = 4,
                step = "3",
                action = "- Tap [4]\n" +
                        "- Tap [5]\n" +
                        "- Tap [6]",
                expectation = "- [formula] is \"123+456\"\n" +
                        "- [result preview] is \"579\"",
                auto = "M",
                result = "N/A"
            )
            assertRow(
                rowNum = 14,
                id = 5,
                step = "4",
                action = "- Tap [=]",
                expectation = "- [result final] is \"579\"",
                auto = "A",
                result = "NOTIMPL",
                testDate = date,
                tester = "auto"
            )

            assertRow(
                rowNum = 15,
                id = 6,
                step = "s20",
                condition = "calculate 123+456",
            )
            assertRow(
                rowNum = 16,
                id = 7,
                step = "1",
                condition = "- [Restart Calculator]\n" +
                        "- [Calculator Main Screen] is displayed",
                action = "- Tap [1]\n" +
                        "- Tap [2]\n" +
                        "- Tap [3]",
                expectation = "- [formula] is \"123\"",
                auto = "A",
                result = "OK",
                testDate = date,
                tester = "auto"
            )
            assertRow(
                rowNum = 17,
                id = 8,
                step = "2",
                action = "- Tap [+]",
                expectation = "- [formula] is \"123+\"",
                auto = "A",
                result = "OK",
                testDate = date,
                tester = "auto"
            )
            assertRow(
                rowNum = 18,
                id = 9,
                step = "3",
                action = "- Tap [4]\n" +
                        "- Tap [5]\n" +
                        "- Tap [6]",
                expectation = "- [formula] is \"123+456\"\n" +
                        "- [result preview] is \"579\"",
                auto = "A",
                result = "OK",
                testDate = date,
                tester = "auto"
            )
            assertRow(
                rowNum = 19,
                id = 10,
                step = "4",
                action = "- Tap [=]",
                expectation = "- [result final] is \"579\"",
                auto = "A",
                result = "OK",
                testDate = date,
                tester = "auto"
            )

        }
    }

}