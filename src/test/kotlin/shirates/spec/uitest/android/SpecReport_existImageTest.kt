package shirates.spec.uitest.android

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.dontExistImage
import shirates.core.driver.commandextension.existImage
import shirates.core.driver.commandextension.macro
import shirates.core.logging.TestLog
import shirates.core.testcode.UITest
import shirates.helper.ImageSetupHelper
import shirates.spec.uitest.assertHeader
import shirates.spec.uitest.assertRow
import shirates.spec.uitest.assertRowHeader
import shirates.spec.utilily.ExcelUtility
import shirates.spec.utilily.cells
import shirates.spec.utilily.text
import shirates.spec.utilily.worksheets

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class SpecReport_existImageTest : UITest() {

    @Test
    @Order(0)
    fun setupImage() {

        ImageSetupHelper.setupImagesMapsTopScreen()
    }

    @Test
    @Order(10)
    fun existImageTest() {

        try {
            scenario {
                case(1, "OK") {
                    condition {
                        it.macro("[Maps Top Screen]")
                    }.expectation {
                        it.existImage("[Explore Tab(selected)]")
                            .existImage("[Contribute Tab]")
                    }
                }
                case(2, "WARN, COND_AUTO") {
                    expectation {
                        it.existImage("[Explore Tab]")   // WARN & COND_AUTO
                    }
                }
                case(3) {
                    expectation {
                        it.dontExistImage("[Contribute Tab(selected)]") // OK
                    }
                }
                case(4) {
                    expectation {
                        it.dontExistImage("[Contribute Tab]") // NG
                    }
                }
            }
        } catch (t: Throwable) {
            assertThat(t.message).startsWith("Image of [Contribute Tab] does not exist (result=true, scale=1.0, threshold=1.0, x=0, y=0, score=0.0, templateImageFile=")
        }
    }

    override fun finally() {

        val filePath = TestLog.directoryForLog.resolve("SpecReport_existImageTest/SpecReport_existImageTest@a.xlsx")
        val ws = ExcelUtility.getWorkbook(filePath = filePath).worksheets("SpecReport_existImageTest")
        val commandSheet = ws.workbook.worksheets("CommandList")

        val executionDateTime = commandSheet.cells("B4").text
        val date = executionDateTime.substring(0, 10)

        /**
         * Header
         */
        ws.assertHeader(
            testConfigName = "Settings",
            sheetName = "SpecReport_existImageTest",
            testClassName = "SpecReport_existImageTest",
            profileName = testProfile.profileName,
            deviceModel = "sdk_gphone64_arm64",
            platformVersion = testProfile.platformVersion,
            ok = 2,
            ng = 1,
            condAuto = 1,
            total = 4,
            a_ca = 4,
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
                step = "existImageTest",
                condition = "existImageTest()"
            )
            assertRow(
                rowNum = 11,
                id = 2,
                step = "1",
                condition = "- [Maps Top Screen]",
                expectation = "- Image of [Explore Tab(selected)] exists\n" +
                        "- Image of [Contribute Tab] exists",
                auto = "CA",
                result = "OK",
                testDate = date,
                tester = "auto"
            )
            assertRow(
                rowNum = 12,
                id = 3,
                expectation = "- Image of [Explore Tab] exists",
                auto = "CA",
                result = "COND_AUTO",
                testDate = date,
                tester = "auto"
            )
            assertRow(
                rowNum = 13,
                id = 4,
                expectation = "- Image of [Contribute Tab(selected)] does not exist",
                auto = "CA",
                result = "OK",
                testDate = date,
                tester = "auto"
            )
            assertRow(
                rowNum = 14,
                id = 5,
                auto = "A",
                result = "NG",
                testDate = date,
                tester = "auto"
            )
        }
    }
}