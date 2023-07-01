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
class TestDriveSelectExtensionTest3 : UITest() {

    @Unstable("Direct mode")
    @Test
    @Order(10)
    fun canSelectWithScrollDown_canSelectWithScrollUp2() {

        scenario {
            case(1) {
                expectation {
                    describe("textStartsWith")
                    it.canSelectWithScrollDown("Dev*").thisIsTrue("Dev*")
                    it.canSelectWithScrollDown("Gene*").thisIsFalse("Gene*")
                    it.canSelectWithScrollUp("Gene*").thisIsTrue("Gene*")
                    it.canSelectWithScrollUp("Dev*").thisIsFalse("Dev*")
                }
            }
            case(2) {
                expectation {
                    describe("textContains")
                    it.canSelectWithScrollDown("*evelope*").thisIsTrue("*evelope*")
                    it.canSelectWithScrollDown("*enera*").thisIsFalse("*enera*")
                    it.canSelectWithScrollUp("*enera*").thisIsTrue("*enera*")
                    it.canSelectWithScrollUp("*evelope*").thisIsFalse("*evelope*")
                }
            }
        }
    }

}