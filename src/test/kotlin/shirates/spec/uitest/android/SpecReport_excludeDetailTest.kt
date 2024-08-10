package shirates.spec.uitest.android

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
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
@Testrun("unitTestConfig/android/androidSettings/no-load-exclude.testrun.properties")
class SpecReport_excludeDetailTest : UITest() {

    var profileName = ""

    override fun beforeAllAfterSetup(context: ExtensionContext?) {
        profileName = testProfile.profileName
    }

    @Test
    @DisplayName("exclude detail items on scenario mode")
    fun S100() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                        .exist("Settings")
                }.action {
                    it.tap("Network & internet")
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                        .exist("Internet")
                        .exist("SIMs")
                    it.target("Airplane mode")
                        .manual("check is OFF")
                }
            }
        }
    }

    override fun finally() {

        val filePath = TestLog.directoryForLog.resolve("SpecReport_excludeDetailTest/SpecReport_excludeDetailTest.xlsx")
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
            testClassName = "SpecReport_excludeDetailTest",
            profileName = "",
            deviceModel = "",
            platformVersion = "",
            noLoadRunMode = "No-Load-Run Mode",
            na = 2,
            excluded = 1,
            total = 3,
            m = 1,
            a_ca = 2,
            a_ca_na = 2
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
                condition = "exclude detail items on scenario mode"
            )
            assertRow(
                rowNum = 11,
                id = 2,
                step = "1",
                condition = "- [Android Settings Top Screen] is displayed\n" +
                        "- <Settings> exists",
                action = "- Tap <Network & internet>",
                target = "[Network & internet Screen]",
                expectation = "- is displayed",
                auto = "A",
                result = "N/A"
            )
            assertRow(
                rowNum = 12,
                id = 3,
                expectation = "- <Internet>\n" +
                        "- <SIMs>",
                auto = "A",
                result = "N/A",
            )
            assertRow(
                rowNum = 13,
                id = 4,
                target = "Airplane mode",
                expectation = "- check is OFF",
                auto = "M",
                result = "EXCLUDED",
                supplement = "Excluded on checking screen transition"
            )
        }
    }
}