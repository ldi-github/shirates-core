package shirates.spec.uitest.android

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.*
import shirates.core.driver.platformMajorVersion
import shirates.core.logging.TestLog
import shirates.core.testcode.Manual
import shirates.core.testcode.SheetName
import shirates.core.testcode.UITest
import shirates.core.utility.format
import shirates.spec.report.entity.SpecReportData
import shirates.spec.report.models.SpecReportDataAdapter
import shirates.spec.uitest.assertHeader
import shirates.spec.uitest.assertRow
import shirates.spec.uitest.assertRowHeader
import shirates.spec.utilily.ExcelUtility
import shirates.spec.utilily.worksheets
import java.nio.file.Files
import java.util.*

@SheetName("clock test")
@Testrun("testConfig/android/clock/testrun.properties")
class SpecReportTest2 : UITest() {

    @Manual
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
                    if (TestMode.isNoLoadRun.not() && platformMajorVersion < 14) {
                        NOTIMPL("This test requires Android 14 or later.")
                    }
                    it.macro("[Alarms Screen]")
                }.expectation {
                    it.cell("[Cell of 8:30 AM]") {
                        exist("[8:30 AM]")
                        exist("[8:30 AM Expand alarm]")
                        exist("[8:30 AM Days of week]")
                        exist("[8:30 AM ON/OFF]")
                    }
                    it.select("<9:00 AM>:parent").cell {
                        exist("9:00 AM")
                        exist("#arrow")
                        exist("Sun, Sat")
                        exist("#onoff")
                    }
                    it.cellOf("8:30 AM") {
                        exist("8:30 AM")
                        exist("#arrow")
                        exist("Mon-Fri")
                        exist("#onoff")
                    }
                    it.cellOf("9:00 AM") {
                        exist("9:00 AM")
                        exist("#arrow")
                        exist("Sun, Sat")
                        exist("#onoff")
                    }
                    it.cell("#tab_menu_alarm") {
                        exist("#navigation_bar_item_icon_container")
                        exist("#navigation_bar_item_large_label_view")
                    }
                    it.cell("#tab_menu_clock") {
                        exist("#navigation_bar_item_icon_container")
                        exist("#navigation_bar_item_labels_group")
                    }
                }.expectation {
                    it.cellOf("[8:30 AM]") {
                        exist("[8:30 AM]")
                        exist("[8:30 AM Expand alarm]")
                        exist("[8:30 AM Days of week]")
                        exist("[8:30 AM ON/OFF]")
                    }
                    it.cellOf("[9:00 AM]") {
                        exist("[9:00 AM]")
                        exist("[9:00 AM Expand alarm]")
                        exist("[9:00 AM Days of week]")
                        exist("[9:00 AM ON/OFF]")
                    }
                }.expectation {
                    it.cellOf("[8:30 AM]") {
                        innerWidget(1).textIs("8:30 AM")
                        innerWidget(2).idIs("arrow")
                        innerWidget(3).textIs("Mon-Fri")
                        innerWidget(4).idIs("onoff")
                    }
                    it.cellOf("[9:00 AM]") {
                        innerWidget(1).textIs("9:00 AM")
                        innerWidget(2).idIs("arrow")
                        innerWidget(3).textIs("Sun, Sat")
                        innerWidget(4).idIs("onoff")
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

        val data = SpecReportData()
        val adapter = SpecReportDataAdapter(data)
        adapter.loadWorkbook(filePath)

        val r = data.commandItems.firstOrNull() { it.result == "NOTIMPL" }
        if (r != null) {
            NOTIMPL(r.exception)
        }

        /**
         * Header
         */
        ws.assertHeader(
            testConfigName = "Clock",
            sheetName = "clock test",
            testClassName = "SpecReportTest2",
            profileName = testProfile.profileName,
            deviceModel = data.p.getValue("appium:deviceModel").toString(),
            platformVersion = testProfile.platformVersion,
            ok = 10,
            na = 10,
            total = 20,
            m = 10,
            m_na = 10,
            a_ca = 10
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
                result = "@Manual"
            )
            assertRow(
                rowNum = 11,
                id = 2,
                step = "1",
                condition = "- [Alarm Screen]",
                action = "",
                target = "Cell [Cell of 8:30 AM]",
                expectation = "- [8:30 AM]\n" +
                        "- [8:30 AM Expand alarm]\n" +
                        "- [8:30 AM Days of week]\n" +
                        "- [8:30 AM ON/OFF]",
                auto = "M",
                result = "N/A",
            )
            assertRow(
                rowNum = 12,
                id = 3,
                target = "Cell <9:00 AM>:parent",
                expectation = "- <9:00 AM>\n" +
                        "- <#arrow>\n" +
                        "- <Sun, Sat>\n" +
                        "- <#onoff>",
                auto = "M",
                result = "N/A",
            )
            assertRow(
                rowNum = 13,
                id = 4,
                target = "Cell of <8:30 AM>",
                expectation = "- <8:30 AM>\n" +
                        "- <#arrow>\n" +
                        "- <Mon-Fri>\n" +
                        "- <#onoff>",
                auto = "M",
                result = "N/A",
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
                result = "N/A",
            )
            assertRow(
                rowNum = 15,
                id = 6,
                target = "Cell <#tab_menu_alarm>",
                expectation = "- <#navigation_bar_item_icon_container>\n" +
                        "- <#navigation_bar_item_large_label_view>",
                auto = "M",
                result = "N/A",
            )
            assertRow(
                rowNum = 16,
                id = 7,
                target = "Cell <#tab_menu_clock>",
                expectation = "- <#navigation_bar_item_icon_container>\n" +
                        "- <#navigation_bar_item_labels_group>",
                auto = "M",
                result = "N/A",
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
                result = "N/A",
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
                result = "N/A",
            )
            assertRow(
                rowNum = 19,
                id = 10,
                target = "Cell of [8:30 AM]",
                expectation = "- innerWidget is \"8:30 AM\"\n" +
                        "- innerWidget(2).idOrName is \"arrow\"\n" +
                        "- innerWidget(3) is \"Mon-Fri\"\n" +
                        "- innerWidget(4).idOrName is \"onoff\"",
                auto = "M",
                result = "N/A",
            )
            assertRow(
                rowNum = 20,
                id = 11,
                target = "Cell of [9:00 AM]",
                expectation = "- innerWidget is \"9:00 AM\"\n" +
                        "- innerWidget(2).idOrName is \"arrow\"\n" +
                        "- innerWidget(3) is \"Sun, Sat\"\n" +
                        "- innerWidget(4).idOrName is \"onoff\"",
                auto = "M",
                result = "N/A",
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
                target = "Cell [Cell of 8:30 AM]",
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
                target = "Cell <9:00 AM>:parent",
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
                        "- <Mon-Fri>\n" +
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
                target = "Cell <#tab_menu_alarm>",
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
                target = "Cell <#tab_menu_clock>",
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
                expectation = "- innerWidget is \"8:30 AM\"\n" +
                        "- innerWidget(2).idOrName is \"arrow\"\n" +
                        "- innerWidget(3) is \"Mon-Fri\"\n" +
                        "- innerWidget(4).idOrName is \"onoff\"",
                auto = "A",
                result = "OK",
                testDate = date,
                tester = "auto"
            )
            assertRow(
                rowNum = 31,
                id = 22,
                target = "Cell of [9:00 AM]",
                expectation = "- innerWidget is \"9:00 AM\"\n" +
                        "- innerWidget(2).idOrName is \"arrow\"\n" +
                        "- innerWidget(3) is \"Sun, Sat\"\n" +
                        "- innerWidget(4).idOrName is \"onoff\"",
                auto = "A",
                result = "OK",
                testDate = date,
                tester = "auto"
            )


        }
    }

}