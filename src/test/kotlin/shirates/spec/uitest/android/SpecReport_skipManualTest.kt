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
class SpecReport_skipManualTest : UITest() {

    @Manual
    @Test
    @Order(10)
    @DisplayName("@Manual, SKIP_CASE, SKIP_SCENARIO")
    fun s10() {

        scenarioCore(
            func1 = {
                SKIP_CASE("Skipping case(1)")
            },
            func2 = {
                SKIP_SCENARIO("Skipping scenario")
            }
        )
    }

    @Test
    @Order(11)
    @DisplayName("SKIP_CASE, SKIP_SCENARIO")
    fun s11() {

        scenarioCore(
            func1 = {
                SKIP_CASE("Skipping case(1)")
            },
            func2 = {
                SKIP_SCENARIO("Skipping scenario")
            }
        )
    }

    @Manual
    @Test
    @Order(20)
    @DisplayName("@Manual, MANUAL_CASE, MANUAL_SCENARIO")
    fun s20() {

        scenarioCore(
            func1 = {
                MANUAL_CASE("Execute test manually after this row in this case.")
            },
            func2 = {
                MANUAL_SCENARIO("Execute test manually after this row in this scenario.")
            }
        )
    }

    @Test
    @Order(21)
    @DisplayName("MANUAL_CASE, MANUAL_SCENARIO")
    fun s21() {

        scenarioCore(
            func1 = {
                MANUAL_CASE("Execute test manually after this row in this case.")
            },
            func2 = {
                MANUAL_SCENARIO("Execute test manually after this row in this scenario.")
            }
        )
    }

    private fun scenarioCore(func1: () -> Unit, func2: () -> Unit) {

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
                condition {
                    func1()
                }.action {
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
            case(4) {
                condition {
                    func2()
                }.action {
                    it.selectWithScrollDown("Notifications")
                }.expectation {
                    it.textIs("Notifications")
                }
            }
            case(5) {
                action {
                    it.selectWithScrollDown("Tips & support")
                }.expectation {
                    it.textIs("Tips & support")
                }
            }
        }

    }


    override fun finally() {

        var filePath =
            TestLog.directoryForLog.resolve("SpecReport_skipManualTest/SpecReport_skipManualTest.xlsx")
        if (Files.exists(filePath).not()) {
            filePath =
                TestLog.directoryForLog.resolve("SpecReport_skipManualTest/SpecReport_skipManualTest@a.xlsx")
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
            testClassName = "SpecReport_skipManualTest",
            profileName = testProfile.profileName,
            deviceModel = data.p.getValue("appium:deviceModel").toString(),
            platformVersion = testProfile.platformVersion,
            ok = 4,
            na = 13,
            skip = 3,
            total = 20,
            m = 13,
            m_na = 13,
            a_ca = 7,
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
                condition = "@Manual, SKIP_CASE, SKIP_SCENARIO",
                result = "@Manual"
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
                result = "N/A"
            )
            assertRow(
                rowNum = 12,
                id = 3,
                step = "2",
                condition = """
- SKIP_CASE(Skipping case(1))
""".trimIndent(),
                action = """
- Select <Connected devices> (scroll down)
""".trimIndent(),
                expectation = """
- <Connected devices> is "Connected devices"
""".trimIndent(),
                auto = "M",
                result = "N/A"
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
                result = "N/A"
            )
            assertRow(
                rowNum = 14,
                id = 5,
                step = "4",
                condition = """
- SKIP_SCENARIO(Skipping scenario)
""".trimIndent(),
                action = """
- Select <Notifications> (scroll down)
""".trimIndent(),
                expectation = """
- <Notifications> is "Notifications"
""".trimIndent(),
                auto = "M",
                result = "N/A"
            )
            assertRow(
                rowNum = 15,
                id = 6,
                step = "5",
                action = """
- Select <Tips & support> (scroll down)
""".trimIndent(),
                expectation = """
- <Tips & support> is "Tips & support"
""".trimIndent(),
                auto = "M",
                result = "N/A"
            )

// s11
            assertRow(
                rowNum = 16,
                id = 7,
                step = "s11",
                condition = "SKIP_CASE, SKIP_SCENARIO",
            )
            assertRow(
                rowNum = 17,
                id = 8,
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
                tester = "auto"
            )
            assertRow(
                rowNum = 18,
                id = 9,
                step = "2",
                condition = """
- SKIP_CASE(Skipping case(1))
""".trimIndent(),
                action = """
- Select <Connected devices> (scroll down)
""".trimIndent(),
                expectation = """
- <Connected devices> is "Connected devices"
""".trimIndent(),
                auto = "A",
                result = "SKIP",
                testDate = date,
                tester = "auto"
            )
            assertRow(
                rowNum = 19,
                id = 10,
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
                tester = "auto"
            )
            assertRow(
                rowNum = 20,
                id = 11,
                step = "4",
                condition = """
- SKIP_SCENARIO(Skipping scenario)
""".trimIndent(),
                action = """
- Select <Notifications> (scroll down)
""".trimIndent(),
                expectation = """
- <Notifications> is "Notifications"
""".trimIndent(),
                auto = "A",
                result = "SKIP",
                testDate = date,
                tester = "auto"
            )
            assertRow(
                rowNum = 21,
                id = 12,
                step = "5",
                action = """
- Select <Tips & support> (scroll down)
""".trimIndent(),
                expectation = """
- <Tips & support> is "Tips & support"
""".trimIndent(),
                auto = "A",
                result = "SKIP",
                testDate = date,
                tester = "auto"
            )

// s20
            assertRow(
                rowNum = 22,
                id = 13,
                step = "s20",
                condition = "@Manual, MANUAL_CASE, MANUAL_SCENARIO",
                result = "@Manual"
            )
            assertRow(
                rowNum = 23,
                id = 14,
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
                result = "N/A"
            )
            assertRow(
                rowNum = 24,
                id = 15,
                step = "2",
                condition = """
- MANUAL_CASE(Execute test manually after this row in this case.)
""".trimIndent(),
                action = """
- Select <Connected devices> (scroll down)
""".trimIndent(),
                expectation = """
- <Connected devices> is "Connected devices"
""".trimIndent(),
                auto = "M",
                result = "N/A"
            )
            assertRow(
                rowNum = 25,
                id = 16,
                step = "3",
                action = """
- Select <Apps> (scroll down)
""".trimIndent(),
                expectation = """
- <Apps> is "Apps"
""".trimIndent(),
                auto = "M",
                result = "N/A"
            )
            assertRow(
                rowNum = 26,
                id = 17,
                step = "4",
                condition = """
- MANUAL_SCENARIO(Execute test manually after this row in this scenario.)
""".trimIndent(),
                action = """
- Select <Notifications> (scroll down)
""".trimIndent(),
                expectation = """
- <Notifications> is "Notifications"
""".trimIndent(),
                auto = "M",
                result = "N/A"
            )
            assertRow(
                rowNum = 27,
                id = 18,
                step = "5",
                action = """
- Select <Tips & support> (scroll down)
""".trimIndent(),
                expectation = """
- <Tips & support> is "Tips & support"
""".trimIndent(),
                auto = "M",
                result = "N/A"
            )

// s21
            assertRow(
                rowNum = 28,
                id = 19,
                step = "s21",
                condition = "MANUAL_CASE, MANUAL_SCENARIO"
            )
            assertRow(
                rowNum = 29,
                id = 20,
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
                tester = "auto"
            )
            assertRow(
                rowNum = 30,
                id = 21,
                step = "2",
                condition = """
- MANUAL_CASE(Execute test manually after this row in this case.)
""".trimIndent(),
                action = """
- Select <Connected devices> (scroll down)
""".trimIndent(),
                expectation = """
- <Connected devices> is "Connected devices"
""".trimIndent(),
                auto = "M",
                result = "N/A",
            )
            assertRow(
                rowNum = 31,
                id = 22,
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
                tester = "auto"
            )
            assertRow(
                rowNum = 32,
                id = 23,
                step = "4",
                condition = """
- MANUAL_SCENARIO(Execute test manually after this row in this scenario.)
""".trimIndent(),
                action = """
- Select <Notifications> (scroll down)
""".trimIndent(),
                expectation = """
- <Notifications> is "Notifications"
""".trimIndent(),
                auto = "M",
                result = "N/A",
            )
            assertRow(
                rowNum = 33,
                id = 24,
                step = "5",
                action = """
- Select <Tips & support> (scroll down)
""".trimIndent(),
                expectation = """
- <Tips & support> is "Tips & support"
""".trimIndent(),
                auto = "M",
                result = "N/A",
            )

        }
    }

}