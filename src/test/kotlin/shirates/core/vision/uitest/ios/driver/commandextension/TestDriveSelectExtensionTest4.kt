package shirates.core.vision.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisIsFalse
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.testcode.Unstable
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.canDetectWithScrollDown
import shirates.core.vision.driver.commandextension.canDetectWithScrollUp
import shirates.core.vision.driver.commandextension.describe
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveSelectExtensionTest4 : VisionTest() {

    @Unstable("false is true (actual=false)")
    @Test
    @Order(10)
    fun canSelectWithScrollDown_canSelectWithScrollUp3() {

        scenario {
            case(1) {
                expectation {
                    describe("textEndsWith")
                    it.canDetectWithScrollDown("eveloper").thisIsTrue()
                    it.canDetectWithScrollDown("eneral").thisIsFalse()
                    it.canDetectWithScrollUp("eneral").thisIsTrue()
                    it.canDetectWithScrollUp("eveloper").thisIsFalse()
                }
            }

            case(2) {
                expectation {
                    describe("textMatches")
                    it.canDetectWithScrollDown("textMatches=^Dev.*per$").thisIsTrue()
                    it.canDetectWithScrollDown("textMatches=^Gen.*al$").thisIsFalse()
                    it.canDetectWithScrollUp("textMatches=^Gen.*al$").thisIsTrue()
                    it.canDetectWithScrollUp("textMatches=^Dev.*per$").thisIsFalse()
                }
            }
        }

    }

}