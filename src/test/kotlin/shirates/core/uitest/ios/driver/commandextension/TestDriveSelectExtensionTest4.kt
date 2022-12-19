package shirates.core.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Unstable
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveSelectExtensionTest4 : UITest() {

    @Unstable("false is true (actual=false)")
    @Test
    @Order(10)
    fun canSelectWithScrollDown_canSelectWithScrollUp3() {

        scenario {
            case(1) {
                expectation {
                    describe("textEndsWith")
                    it.canSelectWithScrollDown("*eveloper").thisIsTrue()
                    it.canSelectWithScrollDown("*eneral").thisIsFalse()
                    it.canSelectWithScrollUp("*eneral").thisIsTrue()
                    it.canSelectWithScrollUp("*eveloper").thisIsFalse()
                }
            }

            case(2) {
                expectation {
                    describe("textMatches")
                    it.canSelectWithScrollDown("textMatches=^Dev.*per$").thisIsTrue()
                    it.canSelectWithScrollDown("textMatches=^Gen.*al$").thisIsFalse()
                    it.canSelectWithScrollUp("textMatches=^Gen.*al$").thisIsTrue()
                    it.canSelectWithScrollUp("textMatches=^Dev.*per$").thisIsFalse()
                }
            }
        }

    }

}