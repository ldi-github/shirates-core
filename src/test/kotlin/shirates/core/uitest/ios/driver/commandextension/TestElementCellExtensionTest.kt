package shirates.core.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestElementCellExtensionTest : UITest() {

    @Test
    fun cellOfTest() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .tap("General")
                        .tap("Game Controller")
                }.expectation {
                    it.cellOf("Gamepad") {
                        exist("Game Controller")
                        exist("Gamepad")
                        exist("Connected")
                    }
                    it.cellOf("Identify Controller") {
                        exist("Identify Controller")
                    }
                    it.cellOf("Default") {
                        exist("Game Controller")
                        exist("Default")
                        exist("1 controllers")
                    }
                }
            }
            case(2) {
                expectation {
                    it.cellOf("Gamepad") {
                        innerWidget(1).textIs("Gamepad, Connected")
                        innerWidget(2).textIs("Game Controller")
                        innerWidget(3).textIs("Gamepad")
                        innerWidget(4).textIs("Connected")
                        innerWidget(5).type.thisIs("XCUIElementTypeImage")
                    }
                    it.cellOf("Identify Controller") {
                        innerWidget(1).textIs("Identify Controller")
                    }
                    it.cellOf("Default") {
                        innerWidget(1).textIs("Default, 1 controllers")
                        innerWidget(2).textIs("Game Controller")
                        innerWidget(3).textIs("Default")
                        innerWidget(4).textIs("1 controllers")
                    }
                }
            }
        }
    }

}