package shirates.core.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestElementCellExtensionTest : UITest() {

    @Test
    fun targetCell_existInCell_cellLabel() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .tap("General")
                        .tap("Game Controller")
                }.expectation {
                    it.cellOf("Gamepad") {
                        existInCell("Game Controller")
                        existInCell("Gamepad")
                        existInCell("Connected")
                    }
                    it.cellOf("Identify Controller") {
                        existInCell("Identify Controller")
                    }
                    it.cellOf("Default") {
                        existInCell("Game Controller")
                        existInCell("Default")
                        existInCell("1 controllers")
                    }
                }
            }
            case(2) {
                expectation {
                    it.cellOf("Gamepad") {
                        cellWidget(1).textIs("Gamepad, Connected")
                        cellWidget(2).textIs("Game Controller")
                        cellWidget(3).textIs("Gamepad")
                        cellWidget(4).textIs("Connected")
                        cellWidget(5).type.thisIs("XCUIElementTypeImage")
                    }
                    it.cellOf("Identify Controller") {
                        cellWidget(1).textIs("Identify Controller")
                    }
                    it.cellOf("Default") {
                        cellWidget(1).textIs("Default, 1 controllers")
                        cellWidget(2).textIs("Game Controller")
                        cellWidget(3).textIs("Default")
                        cellWidget(4).textIs("1 controllers")
                    }
                }
            }
        }
    }

}