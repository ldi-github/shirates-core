package shirates.core.vision.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisIsFalse
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.canDetectWithScrollDown
import shirates.core.vision.driver.commandextension.canDetectWithScrollUp
import shirates.core.vision.driver.commandextension.describe
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class VisionDriveDetectExtensionTest3 : VisionTest() {

    @Test
    @Order(10)
    fun canSelectWithScrollDown_canSelectWithScrollUp2() {

        scenario {
            case(1) {
                expectation {
                    describe("textContains")
                    it.canDetectWithScrollDown("*evelope*").thisIsTrue("*evelope*")
                    it.canDetectWithScrollDown("*enera*").thisIsFalse("*enera*")
                    it.canDetectWithScrollUp("*enera*").thisIsTrue("*enera*")
                    it.canDetectWithScrollUp("*evelope*").thisIsFalse("*evelope*")
                }
            }
        }
    }

}