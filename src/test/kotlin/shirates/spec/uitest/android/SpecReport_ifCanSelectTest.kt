package shirates.spec.uitest.android

import org.apache.poi.xssf.usermodel.XSSFSheet
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.TestProfile
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.ifCanSelect
import shirates.core.driver.branchextension.ifCanSelectNot
import shirates.core.driver.commandextension.describe
import shirates.core.driver.commandextension.launchApp
import shirates.core.driver.commandextension.screenIs
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

@SheetName("SheetName1")
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class SpecReport_ifCanSelectTest : UITest() {

    lateinit var profile: TestProfile

    @Manual
    @Test
    @Order(10)
    @DisplayName("s10@Manual")
    fun s10() {

        scenarioCore()
    }

    @Test
    @Order(11)
    @DisplayName("s11")
    fun s11() {

        scenarioCore()
    }

    private fun scenarioCore() {

        profile = testProfile

        scenario {
            case(1) {
                condition {
                    it.launchApp("Settings")
                        .screenIs("[Android Settings Top Screen]")
                    branches()
                }.action {
                    branches()
                }.expectation {
                    branches()
                }
            }
            case(2) {
                condition {
                    branches2()
                }.action {
                    branches2()
                }.expectation {
                    branches2()
                }
            }
        }

    }

    private fun branches() {

        ifCanSelect("[Network & internet]") {
            describe("in ifCanSelect")
        }.ifCanSelect("[Connected devices]") {
            describe("in ifCanSelect")
        }.ifElse {
            describe("in ifElse")
        }

        ifCanSelect("[System]") {
            describe("in ifCanSelect")
        }.ifCanSelect("[Tips & support]") {
            describe("in ifCanSelect")
        }.ifElse {
            describe("in ifElse")
        }
    }

    private fun branches2() {

        ifCanSelectNot("[Network & internet]") {
            describe("in ifCanSelectNot")
        }.ifCanSelectNot("[Connected devices]") {
            describe("in ifCanSelectNot")
        }.ifElse {
            describe("in ifElse")
        }

        ifCanSelectNot("[System]") {
            describe("in ifCanSelectNot")
        }.ifCanSelectNot("[Tips & support]") {
            describe("in ifCanSelectNot")
        }.ifElse {
            describe("in ifElse")
        }
    }

    override fun finally() {

        var filePath =
            TestLog.directoryForLog.resolve("SpecReport_ifCanSelectTest/SpecReport_ifCanSelectTest.xlsx")
        if (Files.exists(filePath).not()) {
            filePath =
                TestLog.directoryForLog.resolve("SpecReport_ifCanSelectTest/SpecReport_ifCanSelectTest@a.xlsx")
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
            testClassName = "SpecReport_ifCanSelectTest",
            profileName = profile.profileName,
            deviceModel = data.p.getValue("appium:deviceModel").toString(),
            platformVersion = profile.platformVersion,
            notImpl = 1,
            total = 1,
            a_ca = 1
        )

        /**
         * Row Header
         */
        ws.assertRowHeader()

        /**
         * Rows
         */
        assertOnNoLoadRunningMode(ws)
        assertOnLoadRunningMode(ws)
    }

    private fun assertOnNoLoadRunningMode(ws: XSSFSheet) {

        with(ws) {
            assertRow(
                rowNum = 10,
                id = 1,
                step = "s10",
                condition = "s10@Manual",
                result = "@Manual"
            )
            assertRow(
                rowNum = 11,
                id = 2,
                step = "1",
                condition = """
- Launch app <Settings>
- [Android Settings Top Screen] is displayed
if can select [Network & internet] {
  - in ifCanSelect
}
if can select [Connected devices] {
  - in ifCanSelect
}
if else {
  - in ifElse
}
if can select [System] {
  - in ifCanSelect
}
if can select [Tips & support] {
  - in ifCanSelect
}
if else {
  - in ifElse
}
""".trimIndent(),
                action = """
if can select [Network & internet] {
  - in ifCanSelect
}
if can select [Connected devices] {
  - in ifCanSelect
}
if else {
  - in ifElse
}
if can select [System] {
  - in ifCanSelect
}
if can select [Tips & support] {
  - in ifCanSelect
}
if else {
  - in ifElse
}
""".trimIndent(),
                expectation = """
if can select [Network & internet] {
- in ifCanSelect
}
if can select [Connected devices] {
- in ifCanSelect
}
if else {
- in ifElse
}
if can select [System] {
- in ifCanSelect
}
if can select [Tips & support] {
- in ifCanSelect
}
if else {
- in ifElse
}
""".trimIndent(),
            )
            assertRow(
                rowNum = 12,
                id = 3,
                step = "2",
                condition = """
if can not select [Network & internet] {
  - in ifCanSelectNot
}
if can not select [Connected devices] {
  - in ifCanSelectNot
}
if else {
  - in ifElse
}
if can not select [System] {
  - in ifCanSelectNot
}
if can not select [Tips & support] {
  - in ifCanSelectNot
}
if else {
  - in ifElse
}
""".trimIndent(),
                action = """
if can not select [Network & internet] {
  - in ifCanSelectNot
}
if can not select [Connected devices] {
  - in ifCanSelectNot
}
if else {
  - in ifElse
}
if can not select [System] {
  - in ifCanSelectNot
}
if can not select [Tips & support] {
  - in ifCanSelectNot
}
if else {
  - in ifElse
}""".trimIndent(),
                expectation = """
if can not select [Network & internet] {
- in ifCanSelectNot
}
if can not select [Connected devices] {
- in ifCanSelectNot
}
if else {
- in ifElse
}
if can not select [System] {
- in ifCanSelectNot
}
if can not select [Tips & support] {
- in ifCanSelectNot
}
if else {
- in ifElse
}
""".trimIndent(),
            )
        }
    }

    private fun assertOnLoadRunningMode(ws: XSSFSheet) {

        with(ws) {
            assertRow(
                rowNum = 13,
                id = 4,
                step = "s11",
                condition = "s11",
            )
            assertRow(
                rowNum = 14,
                id = 5,
                step = "1",
                condition = """
- Launch app <Settings>
- [Android Settings Top Screen] is displayed
if can select [Network & internet] {
  - in ifCanSelect
}
if can select [Connected devices] {
  - in ifCanSelect
}
if can select [System]
if can select [Tips & support]
if else {
  - in ifElse
}
""".trimIndent(),
                action = """
if can select [Network & internet] {
  - in ifCanSelect
}
if can select [Connected devices] {
  - in ifCanSelect
}
if can select [System]
if can select [Tips & support]
if else {
  - in ifElse
}
""".trimIndent(),
                expectation = """
if can select [Network & internet] {
- in ifCanSelect
}
if can select [Connected devices] {
- in ifCanSelect
}
if can select [System]
if can select [Tips & support]
if else {
- in ifElse
}
""".trimIndent(),
                os = "",
            )
            assertRow(
                rowNum = 15,
                id = 6,
                step = "2",
                condition = """
if can not select [Network & internet]
if can not select [Connected devices]
if else {
  - in ifElse
}
if can not select [System] {
  - in ifCanSelectNot
}
if can not select [Tips & support] {
  - in ifCanSelectNot
}
""".trimIndent(),
                action = """
if can not select [Network & internet]
if can not select [Connected devices]
if else {
  - in ifElse
}
if can not select [System] {
  - in ifCanSelectNot
}
if can not select [Tips & support] {
  - in ifCanSelectNot
}
""".trimIndent(),
                expectation = """
if can not select [Network & internet]
if can not select [Connected devices]
if else {
- in ifElse
}
if can not select [System] {
- in ifCanSelectNot
}
if can not select [Tips & support] {
- in ifCanSelectNot
}
""".trimIndent(),
                auto = "A",
                result = "NOTIMPL",
                testDate = Date().format("yyyy/MM/dd"),
                tester = "auto"
            )

        }
    }

}