package shirates.spec.uitest.android

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import shirates.core.configuration.TestProfile
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.android
import shirates.core.driver.branchextension.ios
import shirates.core.driver.commandextension.describe
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.tap
import shirates.core.logging.TestLog
import shirates.core.testcode.SheetName
import shirates.core.testcode.UITest
import shirates.spec.report.entity.SpecReportData
import shirates.spec.report.models.SpecReportDataAdapter
import shirates.spec.uitest.assertHeader
import shirates.spec.uitest.assertRow
import shirates.spec.uitest.assertRowHeader
import shirates.spec.utilily.ExcelUtility
import shirates.spec.utilily.cells
import shirates.spec.utilily.text
import shirates.spec.utilily.worksheets
import java.nio.file.Files

@SheetName("SheetName1")
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class SpecReportLoadRunTest : UITest() {

    lateinit var profile: TestProfile

    @Test
    @DisplayName("scenario1")
    fun S1000() {

        profile = testProfile

        scenario {
            case(1) {
                condition {
                    it.macro("[Network & internet Screen]")
                }.action {
                    android {
                        it.tap("[Internet]")
                    }
                    ios {
                        describe("never called")
                    }
                }.expectation {
                    it.screenIs("[Internet Screen]")
                }
            }
        }
    }

    override fun finally() {

        var filePath = TestLog.directoryForLog.resolve("SpecReportLoadRunTest/SpecReportLoadRunTest@a.xlsx")
        if (Files.exists(filePath).not()) {
            filePath = TestLog.directoryForLog.resolve("SpecReportLoadRunTest/SpecReportLoadRunTest.xlsx")
        }
        val ws = ExcelUtility.getWorkbook(filePath = filePath).worksheets("SheetName1")

        val data = SpecReportData()
        val adapter = SpecReportDataAdapter(data)
        adapter.loadWorkbook(filePath)

        val commandSheet = ws.workbook.worksheets("CommandList")

        val executionDateTime = commandSheet.cells("B4").text
        val date = executionDateTime.substring(0, 10)

        /**
         * Sheets
         */
        assertThat(ws.workbook.worksheets.count()).isEqualTo(3)
        assertThat(ws.workbook.worksheets[0].sheetName).isEqualTo("CommandList")
        assertThat(ws.workbook.worksheets[1].sheetName).isEqualTo("SheetName1")
        assertThat(ws.workbook.worksheets[2].sheetName).isEqualTo("List")

        /**
         * Header
         */
        ws.assertHeader(
            testConfigName = "Settings",
            sheetName = "SheetName1",
            testClassName = "SpecReportLoadRunTest",
            profileName = profile.profileName,
            deviceModel = data.p.getValue("appium:deviceModel").toString(),
            platformVersion = profile.platformVersion,
            noLoadRunMode = "",
            ok = 1,
            total = 1,
            a_ca = 1
        )

        /**
         * Header Row
         */
        ws.assertRowHeader()

        /**
         * Rows
         */
        with(ws) {
            assertRow(rowNum = 10, id = 1, step = "S1000", condition = "scenario1")
            assertRow(
                rowNum = 11,
                id = 2,
                step = "1",
                condition = "- [Network & internet Screen]",
                action = "android {\n  - Tap [Internet]\n}",
                target = "[Internet Screen]",
                expectation = "- is displayed",
                auto = "A",
                result = "OK",
                testDate = date,
                tester = "auto"
            )
            assertRow(rowNum = 12)
        }
    }
}