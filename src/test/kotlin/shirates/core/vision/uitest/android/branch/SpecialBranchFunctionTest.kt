package shirates.core.vision.uitest.android.branch

import org.apache.poi.xssf.usermodel.XSSFSheet
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.TestProfile
import shirates.core.configuration.Testrun
import shirates.core.driver.TestMode
import shirates.core.logging.TestLog
import shirates.core.testcode.Manual
import shirates.core.testcode.Want
import shirates.core.utility.format
import shirates.core.vision.driver.branchextension.specialTag
import shirates.core.vision.testcode.VisionTest
import shirates.spec.report.entity.SpecReportData
import shirates.spec.report.models.SpecReportDataAdapter
import shirates.spec.uitest.assertHeader
import shirates.spec.uitest.assertRow
import shirates.spec.uitest.assertRowHeader
import shirates.spec.utilily.ExcelUtility
import shirates.spec.utilily.worksheets
import java.nio.file.Files
import java.util.*

@Want
@Testrun("unitTestConfig/vision/android/androidSettings/testrun.properties")
class SpecialBranchFunctionTest : VisionTest() {

    lateinit var profile: TestProfile

    @Manual
    @Test
    @Order(10)
    fun s10() {

        scenarioCore()
    }

    @Test
    @Order(11)
    fun s11() {

        scenarioCore()
    }

    private fun scenarioCore() {

        profile = testProfile

        scenario {
            testProfile.specialTags = "Device1, Device2"

            case(1, "Device1 and Device2 matches") {
                expectation {
                    // Arrange
                    var device1Called = false
                    var device2Called = false
                    var unregisteredTagCalled = false
                    var ifElseCalled = false
                    // Act
                    specialTag("Device1") {
                        device1Called = true
                        OK("Device1 called")
                    }.specialTag("Device2") {
                        device2Called = true
                        OK("Device2 called")
                    }.specialTag("UnregisteredTag") {
                        unregisteredTagCalled = true
                        OK("UnregisteredTag called")
                    }.ifElse {
                        ifElseCalled = true
                        OK("ifElse called")
                    }
                    // Assert
                    if (TestMode.isNoLoadRun) {
                        assertThat(device1Called).isTrue()
                        assertThat(device2Called).isTrue()
                        assertThat(unregisteredTagCalled).isTrue()
                        assertThat(ifElseCalled).isTrue()
                    } else {
                        assertThat(device1Called).isTrue()
                        assertThat(device2Called).isTrue()
                        assertThat(unregisteredTagCalled).isFalse()
                        assertThat(ifElseCalled).isFalse()
                    }
                }
            }

            testProfile.specialTags = "Device2"
            case(2, "not matched") {
                expectation {
                    // Arrange
                    var device1Called = false
                    var ifElseCalled = false
                    // Act
                    specialTag("Device1") {
                        device1Called = true
                        OK("Device1 called")
                    }.ifElse {
                        ifElseCalled = true
                        OK("ifElse called")
                    }
                    // Assert
                    if (TestMode.isNoLoadRun) {
                        assertThat(device1Called).isTrue()
                        assertThat(ifElseCalled).isTrue()
                    } else {
                        assertThat(device1Called).isFalse()
                        assertThat(ifElseCalled).isTrue()
                    }
                }
            }
            case(3, "unregisteredTag") {
                expectation {
                    // Arrange
                    var unregisteredTagCalled = false
                    var ifElseCalled = false
                    // Act
                    specialTag("UnregisteredTag") {
                        unregisteredTagCalled = true
                        OK("UnregisteredTag called")
                    }.ifElse {
                        ifElseCalled = true
                        OK("ifElse called")
                    }
                    // Assert
                    if (TestMode.isNoLoadRun) {
                        assertThat(unregisteredTagCalled).isTrue()
                        assertThat(ifElseCalled).isTrue()
                    } else {
                        assertThat(unregisteredTagCalled).isFalse()
                        assertThat(ifElseCalled).isTrue()
                    }
                }
            }
        }
    }

    override fun finally() {

        var filePath =
            TestLog.directoryForLog.resolve("SpecialBranchFunctionTest/SpecialBranchFunctionTest.xlsx")
        if (Files.exists(filePath).not()) {
            filePath =
                TestLog.directoryForLog.resolve("SpecialBranchFunctionTest/SpecialBranchFunctionTest@a.xlsx")
        }
        val ws = ExcelUtility.getWorkbook(filePath = filePath).worksheets("SpecialBranchFunctionTest")

        val data = SpecReportData()
        val adapter = SpecReportDataAdapter(data)
        adapter.loadWorkbook(filePath)

        /**
         * Header
         */
        ws.assertHeader(
            testConfigName = "Settings",
            sheetName = "SpecialBranchFunctionTest",
            testClassName = "SpecialBranchFunctionTest",
            profileName = profile.profileName,
            deviceModel = if (TestMode.isNoLoadRun) "" else data.p.getValue("appium:deviceModel").toString(),
            platformVersion = if (TestMode.isNoLoadRun) "" else profile.platformVersion,
            ok = 4,
            total = 4,
            a_ca = 4
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
                condition = "s10()",
                result = "@Manual"
            )
            assertRow(
                rowNum = 11,
                id = 2,
                step = "",
                expectation = "- Device1 called",
                os = "",
                special = "Device1",
            )
            assertRow(
                rowNum = 12,
                id = 3,
                step = "",
                expectation = "- Device2 called",
                os = "",
                special = "Device2",
            )
            assertRow(
                rowNum = 13,
                id = 4,
                step = "",
                expectation = "- UnregisteredTag called",
                os = "",
                special = "UnregisteredTag",
            )
            assertRow(
                rowNum = 14,
                id = 5,
                step = "",
                expectation = """
if else {
- ifElse called
}
""".trimIndent(),
                os = "",
                special = "",
            )
            assertRow(
                rowNum = 15,
                id = 6,
                step = "",
                expectation = "- Device1 called",
                os = "",
                special = "Device1",
            )
            assertRow(
                rowNum = 16,
                id = 7,
                step = "",
                expectation = """
if else {
- ifElse called
}
""".trimIndent(),
                os = "",
                special = "",
            )
            assertRow(
                rowNum = 17,
                id = 8,
                step = "",
                expectation = "- UnregisteredTag called",
                os = "",
                special = "UnregisteredTag",
            )
            assertRow(
                rowNum = 18,
                id = 9,
                step = "",
                expectation = """
if else {
- ifElse called
}
""".trimIndent(),
                os = "",
                special = "",
            )

        }
    }

    private fun assertOnLoadRunningMode(ws: XSSFSheet) {

        val date = Date().format("yyyy/MM/dd")

        with(ws) {
            assertRow(
                rowNum = 19,
                id = 10,
                step = "s11",
                condition = "s11()",
            )
            assertRow(
                rowNum = 20,
                id = 11,
                step = "",
                expectation = "- Device1 called",
                os = "",
                special = "Device1",
                auto = "A",
                result = "OK",
                testDate = date,
                tester = "auto"
            )
            assertRow(
                rowNum = 21,
                id = 12,
                step = "",
                expectation = "- Device2 called",
                os = "",
                special = "Device2",
                auto = "A",
                result = "OK",
                testDate = date,
                tester = "auto"
            )
            assertRow(
                rowNum = 22,
                id = 13,
                step = "",
                expectation = """
Device1
if else {
- ifElse called
}
""".trimIndent(),
                os = "",
                special = "",
                auto = "A",
                result = "OK",
                testDate = date,
                tester = "auto"
            )
            assertRow(
                rowNum = 23,
                id = 14,
                step = "",
                expectation = """
UnregisteredTag
if else {
- ifElse called
}
""".trimIndent(),
                os = "",
                special = "",
                auto = "A",
                result = "OK",
                testDate = date,
                tester = "auto"
            )

        }
    }

}