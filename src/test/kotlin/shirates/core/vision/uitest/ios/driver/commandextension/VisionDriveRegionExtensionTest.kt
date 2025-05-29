package shirates.core.vision.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.vision.driver.commandextension.macro
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class VisionDriveRegionExtensionTest : VisionTest() {

    @Test
    @Order(10)
    fun cell_onCellOf_onCellOfWithScrollDown() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Developer Screen]")
                }.expectation {

                }
            }
            case(2) {
                expectation {
                }
            }
        }
    }
}