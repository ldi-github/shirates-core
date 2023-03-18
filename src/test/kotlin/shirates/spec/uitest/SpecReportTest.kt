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
        with(ws) {
            cells("A1").textIs("Calculator")
            cells("D1").textIs("calculator test")
            cells("D2").textIs("SpecReportTest")
            cells("D3").textIs("Pixel 3a(Android 12)")
            if (cells("D4").text.isNotBlank()) {
                cells("D4").textIs("sdk_gphone64_arm64")
                cells("D5").textIs("12")
            }
        }

        val date = Date().format("yyyy/MM/dd")

        /**
         * s10
         */
        with(ws) {
            cells("B10").textIs("s10")
            cells("C10").textIs("calculate 123+456")
            cells("D10").textIs("")
            cells("E10").textIs("")
            cells("F10").textIs("")
            cells("G10").textIs("")
            cells("H10").textIs("")
            cells("I10").textIs("")
            cells("J10").textIs("")
            cells("K10").textIs("")
            cells("L10").textIs("")
            cells("M10").textIs("")
            cells("N10").textIs("")
            cells("O10").textIs("")
            cells("P10").textIs("")
            cells("Q10").textIs("")
            cells("R10").textIs("")

            cells("B11").textIs("1")
            cells("C11").textIs("- [Restart Calculator]\n- [Calculator Main Screen] is displayed")
            cells("D11").textIs("- Tap [1]\n- Tap [2]\n- Tap [3]")
            cells("E11").textIs("")
            cells("F11").textIs("- [formula] is \"123\"")
            cells("G11").textIs("")
            cells("H11").textIs("")
            cells("I11").textIs("M")
            cells("J11").textIs("N/A")
            cells("K11").textIs("")
            cells("L11").textIs("")

            cells("B12").textIs("2")
            cells("C12").textIs("")
            cells("D12").textIs("- Tap [+]")
            cells("E12").textIs("")
            cells("F12").textIs("- [formula] is \"123+\"")
            cells("G12").textIs("")
            cells("H12").textIs("")
            cells("I12").textIs("M")
            cells("J12").textIs("N/A")
            cells("K12").textIs("")
            cells("L12").textIs("")

            cells("B13").textIs("3")
            cells("C13").textIs("")
            cells("D13").textIs("- Tap [4]\n- Tap [5]\n- Tap [6]")
            cells("E13").textIs("")
            cells("F13").textIs("- [formula] is \"123+456\"\n- [result preview] is \"579\"")
            cells("G13").textIs("")
            cells("H13").textIs("")
            cells("I13").textIs("M")
            cells("J13").textIs("N/A")
            cells("K13").textIs("")
            cells("L1").textIs("")

            cells("B14").textIs("4")
            cells("C14").textIs("")
            cells("D14").textIs("- Tap [=]")
            cells("E14").textIs("")
            cells("F14").textIs("- [result final] is \"579\"")
            cells("G14").textIs("")
            cells("H14").textIs("")
            cells("I14").textIs("A")
            cells("J14").textIs("NOTIMPL")
            cells("K14").textIs(date)
            cells("L14").textIs("auto")
            cells("O14").textIs("No test result found. Use assertion function in expectation block.")
        }

        /**
         * s20
         */
        with(ws) {
            cells("B15").textIs("s20")
            cells("C15").textIs("calculate 123+456")
            cells("D15").textIs("")
            cells("E15").textIs("")
            cells("F15").textIs("")
            cells("G15").textIs("")
            cells("H15").textIs("")
            cells("I15").textIs("")
            cells("J15").textIs("")
            cells("K15").textIs("")
            cells("L15").textIs("")
            cells("M15").textIs("")
            cells("N15").textIs("")
            cells("O15").textIs("")
            cells("P15").textIs("")
            cells("Q15").textIs("")
            cells("R15").textIs("")

            cells("B16").textIs("1")
            cells("C16").textIs("- [Restart Calculator]\n- [Calculator Main Screen] is displayed")
            cells("D16").textIs("- Tap [1]\n- Tap [2]\n- Tap [3]")
            cells("E16").textIs("")
            cells("F16").textIs("- [formula] is \"123\"")
            cells("G16").textIs("")
            cells("H16").textIs("")
            cells("I16").textIs("A")
            cells("J16").textIs("OK")
            cells("K16").textIs(date)
            cells("L16").textIs("auto")

            cells("B17").textIs("2")
            cells("C17").textIs("")
            cells("D17").textIs("- Tap [+]")
            cells("E17").textIs("")
            cells("F17").textIs("- [formula] is \"123+\"")
            cells("G17").textIs("")
            cells("H17").textIs("")
            cells("I17").textIs("A")
            cells("J17").textIs("OK")
            cells("K17").textIs(date)
            cells("L17").textIs("auto")

            cells("B18").textIs("3")
            cells("C18").textIs("")
            cells("D18").textIs("- Tap [4]\n- Tap [5]\n- Tap [6]")
            cells("E18").textIs("")
            cells("F18").textIs("- [formula] is \"123+456\"\n- [result preview] is \"579\"")
            cells("G18").textIs("")
            cells("H18").textIs("")
            cells("I18").textIs("A")
            cells("J18").textIs("OK")
            cells("K18").textIs(date)
            cells("L18").textIs("auto")

            cells("B19").textIs("4")
            cells("C19").textIs("")
            cells("D19").textIs("- Tap [=]")
            cells("E19").textIs("")
            cells("F19").textIs("- [result final] is \"579\"")
            cells("G19").textIs("")
            cells("H19").textIs("")
            cells("I19").textIs("A")
            cells("J19").textIs("OK")
            cells("K19").textIs(date)
            cells("L19").textIs("auto")
        }

    }

}