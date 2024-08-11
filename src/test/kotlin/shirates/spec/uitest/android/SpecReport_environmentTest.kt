package shirates.spec.uitest.android

import org.apache.poi.xssf.usermodel.XSSFSheet
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.selectWithScrollDown
import shirates.core.driver.commandextension.textIs
import shirates.core.logging.TestLog
import shirates.core.testcode.Environment
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

@SheetName("SheetName1")
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class SpecReport_environmentTest : UITest() {

    @Environment("stg")
    @Manual(supplement = "This test should be done manually.")
    @Test
    @Order(10)
    @DisplayName("@Environment, @Manual")
    fun s10() {

        scenarioCore()
    }

    @Environment("stg")
    @Test
    @Order(11)
    @DisplayName("@Environment")
    fun s11() {

        scenarioCore()
    }

    private fun scenarioCore() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.selectWithScrollDown("Network & internet")
                }.expectation {
                    it.textIs("Network & internet")
                }
            }
            case(2) {
                action {
                    it.selectWithScrollDown("Connected devices")
                }.expectation {
                    it.textIs("Connected devices")
                }
            }
            case(3) {
                action {
                    it.selectWithScrollDown("Apps")
                }.expectation {
                    it.textIs("Apps")
                }
            }
        }

    }


    override fun finally() {

        var filePath =
            TestLog.directoryForLog.resolve("SpecReport_environmentTest/SpecReport_environmentTest.xlsx")
        if (Files.exists(filePath).not()) {
            filePath =
                TestLog.directoryForLog.resolve("SpecReport_environmentTest/SpecReport_environmentTest@a.xlsx")
        }
        val ws = ExcelUtility.getWorkbook(filePath = filePath).worksheets("SheetName1")

        val data = SpecReportData()
        val adapter = SpecReportDataAdapter(data)
        adapter.loadWorkbook(filePath)

        /**
         * Header
         */
        ws.assertHeader(
            testConfigName = "Settings",
            sheetName = "SheetName1",
            testClassName = "SpecReport_environmentTest",
            profileName = testProfile.profileName,
            deviceModel = data.p.getValue("appium:deviceModel").toString(),
            platformVersion = testProfile.platformVersion,
            ok = 3,
            na = 3,
            total = 6,
            m = 3,
            m_na = 3,
            a_ca = 3,
            a_ca_na = 0
        )

        /**
         * Row Header
         */
        ws.assertRowHeader()

        /**
         * Rows
         */
        assertRows(ws)
    }

    private fun assertRows(ws: XSSFSheet) {

        val date = Date().format("yyyy/MM/dd")

        with(ws) {
// s10
            assertRow(
                rowNum = 10,
                id = 1,
                step = "s10",
                condition = "@Environment, @Manual",
                result = "@Manual",
                environment = "stg",
                supplement = "This test should be done manually."
            )
            assertRow(
                rowNum = 11,
                id = 2,
                step = "1",
                condition = """
- [Android Settings Top Screen]
""".trimIndent(),
                action = """
- Select <Network & internet> (scroll down)
""".trimIndent(),
                expectation = """
- <Network & internet> is "Network & internet"
""".trimIndent(),
                auto = "M",
                result = "N/A",
                environment = "stg",
            )
            assertRow(
                rowNum = 12,
                id = 3,
                step = "2",
                action = """
- Select <Connected devices> (scroll down)
""".trimIndent(),
                expectation = """
- <Connected devices> is "Connected devices"
""".trimIndent(),
                auto = "M",
                result = "N/A",
                environment = "stg",
            )
            assertRow(
                rowNum = 13,
                id = 4,
                step = "3",
                action = """
- Select <Apps> (scroll down)
""".trimIndent(),
                expectation = """
- <Apps> is "Apps"
""".trimIndent(),
                auto = "M",
                result = "N/A",
                environment = "stg",
            )
// s10
            assertRow(
                rowNum = 14,
                id = 5,
                step = "s11",
                condition = """
@Environment
""".trimIndent(),
                environment = "stg",
            )
            assertRow(
                rowNum = 15,
                id = 6,
                step = "1",
                condition = """
- [Android Settings Top Screen]
""".trimIndent(),
                action = """
- Select <Network & internet> (scroll down)
""".trimIndent(),
                expectation = """
- <Network & internet> is "Network & internet"
""".trimIndent(),
                auto = "A",
                result = "OK",
                testDate = date,
                tester = "auto",
                environment = "stg",
            )
            assertRow(
                rowNum = 16,
                id = 7,
                step = "2",
                action = """
- Select <Connected devices> (scroll down)
""".trimIndent(),
                expectation = """
- <Connected devices> is "Connected devices"
""".trimIndent(),
                auto = "A",
                result = "OK",
                testDate = date,
                tester = "auto",
                environment = "stg",
            )
            assertRow(
                rowNum = 17,
                id = 8,
                step = "3",
                action = """
- Select <Apps> (scroll down)
""".trimIndent(),
                expectation = """
- <Apps> is "Apps"
""".trimIndent(),
                auto = "A",
                result = "OK",
                testDate = date,
                tester = "auto",
                environment = "stg",
            )

        }
    }

}