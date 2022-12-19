package shirates.core.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveSelectExtensionTest3 : UITest() {

    @Test
    @Order(20)
    fun canSelectWithScrollDown_canSelectWithScrollUp2() {

        scenario {
            case(1) {
                expectation {
                    describe("textStartsWith")
                    it.canSelectWithScrollDown("Dev*").thisIsTrue()
                    it.canSelectWithScrollDown("Gene*").thisIsFalse()
                    it.canSelectWithScrollUp("Gene*").thisIsTrue()
                    it.canSelectWithScrollUp("Dev*").thisIsFalse()
                }
            }
            case(2) {
                expectation {
                    describe("textContains")
                    it.canSelectWithScrollDown("*evelope*").thisIsTrue()
                    it.canSelectWithScrollDown("*enera*").thisIsFalse()
                    it.canSelectWithScrollUp("*enera*").thisIsTrue()
                    it.canSelectWithScrollUp("*evelope*").thisIsFalse()
                }
            }
        }
    }

}