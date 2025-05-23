package shirates.spec.uitest.android

import org.apache.poi.xssf.usermodel.XSSFSheet
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.*
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
class SpecReport_platformBranch_Test : UITest() {

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

        scenario {
            case(1) {
                condition {
                    realDevice {
                        NOTIMPL("This test must be run on emulator.")
                    }
                    intel {
                        NOTIMPL("This test must be run on amd64 platform.")
                    }
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
        android {
            describe("in android")
            emulator {
                describe("in emulator")
            }
        }
        ios {
            describe("in ios")
            simulator {
                describe("in simulator")
            }
        }
        virtualDevice {
            describe("in virtualDevice")
            arm64 {
                describe("in arm64")
            }
            intel {
                describe("in intel")
            }
        }
        realDevice {
            describe("in realDevice")
            osaifuKeitai {
                describe("in osaifuKeitai")
            }.osaifuKeitaiNot {
                describe("in osaifuKeitaiNot")
            }
        }
        stub {
            describe("in stub")
        }.stubNot {
            describe("in stubNot")
        }
    }

    override fun finally() {

        var filePath =
            TestLog.directoryForLog.resolve("SpecReport_platformBranch_Test/SpecReport_platformBranch_Test.xlsx")
        if (Files.exists(filePath).not()) {
            filePath =
                TestLog.directoryForLog.resolve("SpecReport_platformBranch_Test/SpecReport_platformBranch_Test@a.xlsx")
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
            testClassName = "SpecReport_platformBranch_Test",
            profileName = testProfile.profileName,
            deviceModel = data.p.getValue("appium:deviceModel") ?: "",
            platformVersion = if (data.noLoadRun) "" else testProfile.platformVersion,
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
        assertRowsOnNoLoadRunningMode(ws)
        assertRowsOnLoadRunningMode(ws)
    }

    private fun assertRowsOnNoLoadRunningMode(ws: XSSFSheet) {

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
- Launch <Settings>
- [Android Settings Top Screen] is displayed
android {
  - in android
  emulator {
    - in emulator
  }
}
ios {
  - in ios
  simulator {
    - in simulator
  }
}
virtualDevice {
  - in virtualDevice
  arm64 {
    - in arm64
  }
  intel {
    - in intel
  }
}
realDevice {
  - in realDevice
  osaifuKeitai {
    - in osaifuKeitai
  }
  osaifuKeitaiNot {
    - in osaifuKeitaiNot
  }
}
stub {
  - in stub
}
stubNot {
  - in stubNot
}
""".trimIndent(),
                action = """
android {
  - in android
  emulator {
    - in emulator
  }
}
ios {
  - in ios
  simulator {
    - in simulator
  }
}
virtualDevice {
  - in virtualDevice
  arm64 {
    - in arm64
  }
  intel {
    - in intel
  }
}
realDevice {
  - in realDevice
  osaifuKeitai {
    - in osaifuKeitai
  }
  osaifuKeitaiNot {
    - in osaifuKeitaiNot
  }
}
stub {
  - in stub
}
stubNot {
  - in stubNot
}
""".trimIndent(),
                expectation = "- in android",
                os = "Android",
            )
            assertRow(
                rowNum = 12,
                id = 3,
                expectation = "- in emulator",
                os = "Android",
                special = "emulator",
            )
            assertRow(
                rowNum = 13,
                id = 4,
                expectation = "- in ios",
                os = "iOS",
            )
            assertRow(
                rowNum = 14,
                id = 5,
                expectation = "- in simulator",
                os = "iOS",
                special = "simulator",
            )
            assertRow(
                rowNum = 15,
                id = 6,
                expectation = "- in virtualDevice",
                special = "virtualDevice",
            )
            assertRow(
                rowNum = 16,
                id = 7,
                expectation = "- in arm64",
                special = "virtualDevice/arm64",
            )
            assertRow(
                rowNum = 17,
                id = 8,
                expectation = "- in intel",
                special = "virtualDevice/intel",
            )
            assertRow(
                rowNum = 18,
                id = 9,
                expectation = "- in realDevice",
                special = "realDevice",
            )
            assertRow(
                rowNum = 19,
                id = 10,
                expectation = "- in osaifuKeitai",
                special = "realDevice/osaifuKeitai",
            )
            assertRow(
                rowNum = 20,
                id = 11,
                expectation = "- in osaifuKeitaiNot",
                special = "realDevice/osaifuKeitaiNot",
            )
            assertRow(
                rowNum = 21,
                id = 12,
                expectation = "- in stub",
                special = "stub",
            )
            assertRow(
                rowNum = 22,
                id = 13,
                expectation = "- in stubNot",
                special = "stubNot",
            )

        }
    }

    private fun assertRowsOnLoadRunningMode(ws: XSSFSheet) {

        with(ws) {
            assertRow(
                rowNum = 23,
                id = 14,
                step = "s11",
                condition = "s11",
            )
            assertRow(
                rowNum = 24,
                id = 15,
                step = "1",
                condition = """
- Launch <Settings>
- [Android Settings Top Screen] is displayed
android {
  - in android
  emulator {
    - in emulator
  }
}
virtualDevice {
  - in virtualDevice
  arm64 {
    - in arm64
  }
}
stubNot {
  - in stubNot
}
""".trimIndent(),
                action = """
android {
  - in android
  emulator {
    - in emulator
  }
}
virtualDevice {
  - in virtualDevice
  arm64 {
    - in arm64
  }
}
stubNot {
  - in stubNot
}
""".trimIndent(),
                expectation = "- in android",
                os = "Android",
            )
            assertRow(
                rowNum = 25,
                id = 16,
                expectation = "- in emulator",
                os = "Android",
                special = "emulator"
            )
            assertRow(
                rowNum = 26,
                id = 17,
                expectation = "- in virtualDevice",
                special = "virtualDevice"
            )
            assertRow(
                rowNum = 27,
                id = 18,
                expectation = "- in arm64",
                special = "virtualDevice/arm64"
            )
            val date = Date().format("yyyy/MM/dd")
            assertRow(
                rowNum = 28,
                id = 19,
                expectation = "- in stubNot",
                special = "stubNot",
            )
            assertRow(
                rowNum = 29,
                id = 20,
                auto = "A",
                result = "NOTIMPL",
                testDate = date,
                tester = "auto"
            )

        }
    }

}