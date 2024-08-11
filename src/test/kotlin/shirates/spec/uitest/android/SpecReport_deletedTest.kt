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
import shirates.core.testcode.Deleted
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
class SpecReport_deletedTest : UITest() {

    @Deleted("s10 is deleted")
    @Manual
    @Test
    @Order(10)
    @DisplayName("@Deleted,@Manual")
    fun s10() {

        scenarioCore()
    }

    @Deleted("s11 is deleted")
    @Test
    @Order(11)
    @DisplayName("@Deleted")
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
        }

    }


    override fun finally() {

        var filePath =
            TestLog.directoryForLog.resolve("SpecReport_deletedTest/SpecReport_deletedTest.xlsx")
        if (Files.exists(filePath).not()) {
            filePath =
                TestLog.directoryForLog.resolve("SpecReport_deletedTest/SpecReport_deletedTest@a.xlsx")
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
            testClassName = "SpecReport_deletedTest",
            profileName = testProfile.profileName,
            deviceModel = data.p.getValue("appium:deviceModel").toString(),
            platformVersion = testProfile.platformVersion,
            m = 1,
            m_na = 0,
            a_ca = 1,
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
                condition = "@Deleted,@Manual",
                result = "@Manual"
            )
            assertRow(
                rowNum = 11,
                id = 2,
                condition = "s10 is deleted",
            )
            assertRow(
                rowNum = 12,
                id = 3,
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
                result = "DELETED"
            )

// s11
            assertRow(
                rowNum = 13,
                id = 4,
                step = "s11",
                condition = "@Deleted",
            )
            assertRow(
                rowNum = 14,
                id = 5,
                condition = "s11 is deleted",
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
                result = "DELETED"
            )
        }
    }

}