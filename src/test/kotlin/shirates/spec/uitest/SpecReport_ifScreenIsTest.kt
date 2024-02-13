package shirates.spec.uitest

import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.android
import shirates.core.driver.branchextension.emulator
import shirates.core.driver.branchextension.ifScreenIs
import shirates.core.driver.commandextension.describe
import shirates.core.driver.commandextension.launchApp
import shirates.core.driver.commandextension.screenIs
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

@SheetName("SheetName1")
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class SpecReport_ifScreenIsTest : UITest() {

    @NoLoadRun
    @Test
    @Order(10)
    @DisplayName("s10@NoLoadRun")
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
        }

    }

    private fun branches() {

        ifScreenIs("[Android Settings Top Screen]") {
            describe("in ifScreenIs")
        }.ifScreenIs("[Network & internet Screen]") {
            describe("in ifScreenIs")
        }.ifScreenIsNot("[Android Settings Top Screen]") {
            describe("in ifScreenIsNot")
        }.ifScreenIsNot("[Network & internet Screen]") {
            describe("in ifScreenIsNot")
        }.ifElse {
            describe("in ifElse")
        }

        android {
            ifScreenIs("[Android Settings Top Screen]") {
                describe("in ifScreenIs")
            }.ifScreenIs("[Network & internet Screen]") {
                describe("in ifScreenIs")
            }.ifScreenIsNot("[Android Settings Top Screen]") {
                describe("in ifScreenIsNot")
            }.ifScreenIsNot("[Network & internet Screen]") {
                describe("in ifScreenIsNot")
            }.ifElse {
                describe("in ifElse")
            }

            emulator {
                ifScreenIs("[Android Settings Top Screen]") {
                    describe("in ifScreenIs")
                }.ifScreenIs("[Network & internet Screen]") {
                    describe("in ifScreenIs")
                }.ifScreenIsNot("[Android Settings Top Screen]") {
                    describe("in ifScreenIsNot")
                }.ifScreenIsNot("[Network & internet Screen]") {
                    describe("in ifScreenIsNot")
                }.ifElse {
                    describe("in ifElse")
                }
            }
        }
    }

    override fun finally() {

        var filePath =
            TestLog.directoryForLog.resolve("SpecReport_ifScreenIsTest/SpecReport_ifScreenIsTest.xlsx")
        if (Files.exists(filePath).not()) {
            filePath =
                TestLog.directoryForLog.resolve("SpecReport_ifScreenIsTest/SpecReport_ifScreenIsTest@a.xlsx")
        }
        val ws = ExcelUtility.getWorkbook(filePath = filePath).worksheets("SheetName1")

        fun XSSFCell.textIs(expected: String) {

            assertThat(this.text).isEqualTo(expected)
        }

        /**
         * Header
         */
        val deviceModel = if (ws.cells("D4").text.isNotBlank()) "sdk_gphone64_arm64" else ""
        val platformVersion = if (deviceModel.isNotBlank()) "12" else ""
        ws.assertHeader(
            testConfigName = "Settings",
            sheetName = "SheetName1",
            testClassName = "SpecReport_ifScreenIsTest",
            profileName = "Pixel 3a(Android 12)",
            deviceModel = deviceModel,
            platformVersion = platformVersion,
            notImpl = 1,
            total = 1
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
                condition = "s10@NoLoadRun",
            )
            assertRow(
                rowNum = 11,
                id = 2,
                step = "1",
                condition = """
- Launch app <Settings>
- [Android Settings Top Screen] is displayed
if screen is [Android Settings Top Screen] {
  - in ifScreenIs
}
if screen is [Network & internet Screen] {
  - in ifScreenIs
}
if screen is not [Android Settings Top Screen] {
  - in ifScreenIsNot
}
if screen is not [Network & internet Screen] {
  - in ifScreenIsNot
}
if else {
  - in ifElse
}
android {
  if screen is [Android Settings Top Screen] {
    - in ifScreenIs
  }
  if screen is [Network & internet Screen] {
    - in ifScreenIs
  }
  if screen is not [Android Settings Top Screen] {
    - in ifScreenIsNot
  }
  if screen is not [Network & internet Screen] {
    - in ifScreenIsNot
  }
  if else {
    - in ifElse
  }
  emulator {
    if screen is [Android Settings Top Screen] {
      - in ifScreenIs
    }
    if screen is [Network & internet Screen] {
      - in ifScreenIs
    }
    if screen is not [Android Settings Top Screen] {
      - in ifScreenIsNot
    }
    if screen is not [Network & internet Screen] {
      - in ifScreenIsNot
    }
    if else {
      - in ifElse
    }
  }
}
""".trimIndent(),
                action = """
if screen is [Android Settings Top Screen] {
  - in ifScreenIs
}
if screen is [Network & internet Screen] {
  - in ifScreenIs
}
if screen is not [Android Settings Top Screen] {
  - in ifScreenIsNot
}
if screen is not [Network & internet Screen] {
  - in ifScreenIsNot
}
if else {
  - in ifElse
}
android {
  if screen is [Android Settings Top Screen] {
    - in ifScreenIs
  }
  if screen is [Network & internet Screen] {
    - in ifScreenIs
  }
  if screen is not [Android Settings Top Screen] {
    - in ifScreenIsNot
  }
  if screen is not [Network & internet Screen] {
    - in ifScreenIsNot
  }
  if else {
    - in ifElse
  }
  emulator {
    if screen is [Android Settings Top Screen] {
      - in ifScreenIs
    }
    if screen is [Network & internet Screen] {
      - in ifScreenIs
    }
    if screen is not [Android Settings Top Screen] {
      - in ifScreenIsNot
    }
    if screen is not [Network & internet Screen] {
      - in ifScreenIsNot
    }
    if else {
      - in ifElse
    }
  }
}
""".trimIndent(),
                expectation = """
if screen is [Android Settings Top Screen] {
- in ifScreenIs
}
if screen is [Network & internet Screen] {
- in ifScreenIs
}
if screen is not [Android Settings Top Screen] {
- in ifScreenIsNot
}
if screen is not [Network & internet Screen] {
- in ifScreenIsNot
}
if else {
- in ifElse
}
""".trimIndent(),
            )
            assertRow(
                rowNum = 12,
                id = 3,
                expectation = """
if screen is [Android Settings Top Screen] {
- in ifScreenIs
}
if screen is [Network & internet Screen] {
- in ifScreenIs
}
if screen is not [Android Settings Top Screen] {
- in ifScreenIsNot
}
if screen is not [Network & internet Screen] {
- in ifScreenIsNot
}
if else {
- in ifElse
}
""".trimIndent(),
                os = "Android",
            )
            assertRow(
                rowNum = 13,
                id = 4,
                expectation = """
if screen is [Android Settings Top Screen] {
- in ifScreenIs
}
if screen is [Network & internet Screen] {
- in ifScreenIs
}
if screen is not [Android Settings Top Screen] {
- in ifScreenIsNot
}
if screen is not [Network & internet Screen] {
- in ifScreenIsNot
}
if else {
- in ifElse
}
""".trimIndent(),
                os = "Android",
                special = "emulator"
            )
        }
    }

    private fun assertOnLoadRunningMode(ws: XSSFSheet) {

        with(ws) {
            assertRow(
                rowNum = 14,
                id = 5,
                step = "s11",
                condition = "s11",
            )
            assertRow(
                rowNum = 15,
                id = 6,
                step = "1",
                condition = """
- Launch app <Settings>
- [Android Settings Top Screen] is displayed
if screen is [Android Settings Top Screen] {
  - in ifScreenIs
}
if screen is not [Network & internet Screen] {
  - in ifScreenIsNot
}
android {
  if screen is [Android Settings Top Screen] {
    - in ifScreenIs
  }
  if screen is not [Network & internet Screen] {
    - in ifScreenIsNot
  }
  emulator {
    if screen is [Android Settings Top Screen] {
      - in ifScreenIs
    }
    if screen is not [Network & internet Screen] {
      - in ifScreenIsNot
    }
  }
}
""".trimIndent(),
                action = """
if screen is [Android Settings Top Screen] {
  - in ifScreenIs
}
if screen is not [Network & internet Screen] {
  - in ifScreenIsNot
}
android {
  if screen is [Android Settings Top Screen] {
    - in ifScreenIs
  }
  if screen is not [Network & internet Screen] {
    - in ifScreenIsNot
  }
  emulator {
    if screen is [Android Settings Top Screen] {
      - in ifScreenIs
    }
    if screen is not [Network & internet Screen] {
      - in ifScreenIsNot
    }
  }
}
""".trimIndent(),
                expectation = """
if screen is [Android Settings Top Screen] {
- in ifScreenIs
}
if screen is not [Network & internet Screen] {
- in ifScreenIsNot
}
""".trimIndent(),
                os = "",
            )
            assertRow(
                rowNum = 16,
                id = 7,
                expectation = """
if screen is [Android Settings Top Screen] {
- in ifScreenIs
}
if screen is not [Network & internet Screen] {
- in ifScreenIsNot
}
""".trimIndent(),
                os = "Android",
            )
            assertRow(
                rowNum = 17,
                id = 8,
                expectation = """
if screen is [Android Settings Top Screen] {
- in ifScreenIs
}
if screen is not [Network & internet Screen] {
- in ifScreenIsNot
}
""".trimIndent(),
                os = "Android",
                special = "emulator",
                auto = "A",
                result = "NOTIMPL",
                testDate = Date().format("yyyy/MM/dd"),
                tester = "auto"
            )

        }
    }

}