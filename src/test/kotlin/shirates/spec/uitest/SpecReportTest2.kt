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

@SheetName("clock test")
@Testrun("testConfig/android/clock/testrun.properties")
class SpecReportTest2 : UITest() {

    @NoLoadRun
    @Test
    @Order(10)
    @DisplayName("Alarm Screen")
    fun s10() {

        scenarioCore()
    }

    @Test
    @Order(20)
    @DisplayName("Alarm Screen")
    fun s20() {

        scenarioCore()
    }

    private fun scenarioCore() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Alarm Screen]")
                }.expectation {
                    it.cell("[Cell of 8:30 AM]") {
                        existInCell("[8:30 AM]")
                        existInCell("[8:30 AM Expand alarm]")
                        existInCell("[8:30 AM Days of week]")
                        existInCell("[8:30 AM ON/OFF]")
                    }
                    it.select("<9:00 AM>:parent").cell {
                        existInCell("9:00 AM")
                        existInCell("#arrow")
                        existInCell("Sun, Sat")
                        existInCell("#onoff")
                    }
                    it.cellOf("8:30 AM") {
                        existInCell("8:30 AM")
                        existInCell("#arrow")
                        existInCell("Mon, Tue, Wed, Thu, Fri")
                        existInCell("#onoff")
                    }
                    it.cellOf("9:00 AM") {
                        existInCell("9:00 AM")
                        existInCell("#arrow")
                        existInCell("Sun, Sat")
                        existInCell("#onoff")
                    }
                    it.cell("#tab_menu_alarm") {
                        existInCell("#navigation_bar_item_icon_container")
                        existInCell("#navigation_bar_item_large_label_view")
                    }
                    it.cell("#tab_menu_clock") {
                        existInCell("#navigation_bar_item_icon_container")
                        existInCell("#navigation_bar_item_labels_group")
                    }
                }.expectation {
                    it.cellOf("[8:30 AM]") {
                        existInCell("[8:30 AM]")
                        existInCell("[8:30 AM Expand alarm]")
                        existInCell("[8:30 AM Days of week]")
                        existInCell("[8:30 AM ON/OFF]")
                    }
                    it.cellOf("[9:00 AM]") {
                        existInCell("[9:00 AM]")
                        existInCell("[9:00 AM Expand alarm]")
                        existInCell("[9:00 AM Days of week]")
                        existInCell("[9:00 AM ON/OFF]")
                    }
                }.expectation {
                    /**
                     * cellWidget
                     */
                    it.cellOf("[8:30 AM]") {
                        cellWidget(1).textIs("8:30 AM")
                        cellWidget(2).idIs("arrow")
                        cellWidget(3).textIs("Mon, Tue, Wed, Thu, Fri")
                        cellWidget(4).idIs("onoff")
                    }
                    it.cellOf("[9:00 AM]") {
                        cellWidget(1).textIs("9:00 AM")
                        cellWidget(2).idIs("arrow")
                        cellWidget(3).textIs("Sun, Sat")
                        cellWidget(4).idIs("onoff")
                    }
                }
            }
        }
    }

    override fun finally() {

        var filePath = TestLog.directoryForLog.resolve("SpecReportTest2/SpecReportTest2.xlsx")
        if (Files.exists(filePath).not()) {
            filePath = TestLog.directoryForLog.resolve("SpecReportTest2/SpecReportTest2@a.xlsx")
        }
        val ws = ExcelUtility.getWorkbook(filePath = filePath).worksheets("clock test")

        fun XSSFCell.textIs(expected: String) {

            assertThat(this.text).isEqualTo(expected)
        }

        /**
         * Header
         */
        val deviceModel = if (ws.cells("D4").text.isNotBlank()) "sdk_gphone64_arm64" else ""
        val platformVersion = if (deviceModel.isNotBlank()) "12" else ""
        ws.assertHeader(
            testConfigName = "Clock",
            sheetName = "clock test",
            testClassName = "SpecReportTest2",
            profileName = "Pixel 3a(Android 12)",
            deviceModel = deviceModel,
            platformVersion = platformVersion,
            ok = 10,
            notImpl = 0,
            total = 10
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
                condition = "Alarm Screen",
            )
            assertRow(
                rowNum = 11,
                id = 2,
                step = "1",
                condition = "- [Alarm Screen]",
                action = "",
                target = "[Cell of 8:30 AM]",
                expectation = "- [8:30 AM]\n" +
                        "- [8:30 AM Expand alarm]\n" +
                        "- [8:30 AM Days of week]\n" +
                        "- [8:30 AM ON/OFF]",
                auto = "M",
                result = "N/A"
            )
            assertRow(
                rowNum = 12,
                id = 3,
                target = "<9:00 AM>:parent",
                expectation = "- <9:00 AM>\n" +
                        "- <#arrow>\n" +
                        "- <Sun, Sat>\n" +
                        "- <#onoff>",
                auto = "M",
                result = "N/A"
            )
            assertRow(
                rowNum = 13,
                id = 4,
                target = "Cell of <8:30 AM>",
                expectation = "- <8:30 AM>\n" +
                        "- <#arrow>\n" +
                        "- <Mon, Tue, Wed, Thu, Fri>\n" +
                        "- <#onoff>",
                auto = "M",
                result = "N/A"
            )
            assertRow(
                rowNum = 14,
                id = 5,
                target = "Cell of <9:00 AM>",
                expectation = "- <9:00 AM>\n" +
                        "- <#arrow>\n" +
                        "- <Sun, Sat>\n" +
                        "- <#onoff>",
                auto = "M",
                result = "N/A"
            )
            assertRow(
                rowNum = 15,
                id = 6,
                target = "<#tab_menu_alarm>",
                expectation = "- <#navigation_bar_item_icon_container>\n" +
                        "- <#navigation_bar_item_large_label_view>",
                auto = "M",
                result = "N/A"
            )
            assertRow(
                rowNum = 16,
                id = 7,
                target = "<#tab_menu_clock>",
                expectation = "- <#navigation_bar_item_icon_container>\n" +
                        "- <#navigation_bar_item_labels_group>",
                auto = "M",
                result = "N/A"
            )
            assertRow(
                rowNum = 17,
                id = 8,
                target = "Cell of [8:30 AM]",
                expectation = "- [8:30 AM]\n" +
                        "- [8:30 AM Expand alarm]\n" +
                        "- [8:30 AM Days of week]\n" +
                        "- [8:30 AM ON/OFF]",
                auto = "M",
                result = "N/A"
            )
            assertRow(
                rowNum = 18,
                id = 9,
                target = "Cell of [9:00 AM]",
                expectation = "- [9:00 AM]\n" +
                        "- [9:00 AM Expand alarm]\n" +
                        "- [9:00 AM Days of week]\n" +
                        "- [9:00 AM ON/OFF]",
                auto = "M",
                result = "N/A"
            )
            assertRow(
                rowNum = 19,
                id = 10,
                target = "Cell of [8:30 AM]",
                expectation = "- cellWidget is \"8:30 AM\"\n" +
                        "- cellWidget(2).idOrName is \"arrow\"\n" +
                        "- cellWidget(3) is \"Mon, Tue, Wed, Thu, Fri\"\n" +
                        "- cellWidget(4).idOrName is \"onoff\"",
                auto = "M",
                result = "N/A"
            )
            assertRow(
                rowNum = 20,
                id = 11,
                target = "Cell of [9:00 AM]",
                expectation = "- cellWidget is \"9:00 AM\"\n" +
                        "- cellWidget(2).idOrName is \"arrow\"\n" +
                        "- cellWidget(3) is \"Sun, Sat\"\n" +
                        "- cellWidget(4).idOrName is \"onoff\"",
                auto = "M",
                result = "N/A"
            )


            assertRow(
                rowNum = 21,
                id = 12,
                step = "s20",
                condition = "Alarm Screen",
            )
            assertRow(
                rowNum = 22,
                id = 13,
                step = "1",
                condition = "- [Alarm Screen]",
                action = "",
                target = "[Cell of 8:30 AM]",
                expectation = "- [8:30 AM]\n" +
                        "- [8:30 AM Expand alarm]\n" +
                        "- [8:30 AM Days of week]\n" +
                        "- [8:30 AM ON/OFF]",
                auto = "A",
                result = "OK",
                testDate = date,
                tester = "auto"
            )
            assertRow(
                rowNum = 23,
                id = 14,
                target = "<9:00 AM>:parent",
                expectation = "- <9:00 AM>\n" +
                        "- <#arrow>\n" +
                        "- <Sun, Sat>\n" +
                        "- <#onoff>",
                auto = "A",
                result = "OK",
                testDate = date,
                tester = "auto"
            )
            assertRow(
                rowNum = 24,
                id = 15,
                target = "Cell of <8:30 AM>",
                expectation = "- <8:30 AM>\n" +
                        "- <#arrow>\n" +
                        "- <Mon, Tue, Wed, Thu, Fri>\n" +
                        "- <#onoff>",
                auto = "A",
                result = "OK",
                testDate = date,
                tester = "auto"
            )
            assertRow(
                rowNum = 25,
                id = 16,
                target = "Cell of <9:00 AM>",
                expectation = "- <9:00 AM>\n" +
                        "- <#arrow>\n" +
                        "- <Sun, Sat>\n" +
                        "- <#onoff>",
                auto = "A",
                result = "OK",
                testDate = date,
                tester = "auto"
            )
            assertRow(
                rowNum = 26,
                id = 17,
                target = "<#tab_menu_alarm>",
                expectation = "- <#navigation_bar_item_icon_container>\n" +
                        "- <#navigation_bar_item_large_label_view>",
                auto = "A",
                result = "OK",
                testDate = date,
                tester = "auto"
            )
            assertRow(
                rowNum = 27,
                id = 18,
                target = "<#tab_menu_clock>",
                expectation = "- <#navigation_bar_item_icon_container>\n" +
                        "- <#navigation_bar_item_labels_group>",
                auto = "A",
                result = "OK",
                testDate = date,
                tester = "auto"
            )
            assertRow(
                rowNum = 28,
                id = 19,
                target = "Cell of [8:30 AM]",
                expectation = "- [8:30 AM]\n" +
                        "- [8:30 AM Expand alarm]\n" +
                        "- [8:30 AM Days of week]\n" +
                        "- [8:30 AM ON/OFF]",
                auto = "A",
                result = "OK",
                testDate = date,
                tester = "auto"
            )
            assertRow(
                rowNum = 29,
                id = 20,
                target = "Cell of [9:00 AM]",
                expectation = "- [9:00 AM]\n" +
                        "- [9:00 AM Expand alarm]\n" +
                        "- [9:00 AM Days of week]\n" +
                        "- [9:00 AM ON/OFF]",
                auto = "A",
                result = "OK",
                testDate = date,
                tester = "auto"
            )
            assertRow(
                rowNum = 30,
                id = 21,
                target = "Cell of [8:30 AM]",
                expectation = "- cellWidget is \"8:30 AM\"\n" +
                        "- cellWidget(2).idOrName is \"arrow\"\n" +
                        "- cellWidget(3) is \"Mon, Tue, Wed, Thu, Fri\"\n" +
                        "- cellWidget(4).idOrName is \"onoff\"",
                auto = "A",
                result = "OK",
                testDate = date,
                tester = "auto"
            )
            assertRow(
                rowNum = 31,
                id = 22,
                target = "Cell of [9:00 AM]",
                expectation = "- cellWidget is \"9:00 AM\"\n" +
                        "- cellWidget(2).idOrName is \"arrow\"\n" +
                        "- cellWidget(3) is \"Sun, Sat\"\n" +
                        "- cellWidget(4).idOrName is \"onoff\"",
                auto = "A",
                result = "OK",
                testDate = date,
                tester = "auto"
            )


        }
    }

}