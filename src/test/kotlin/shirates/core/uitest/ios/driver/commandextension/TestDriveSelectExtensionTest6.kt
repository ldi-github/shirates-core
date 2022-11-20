package shirates.core.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveSelectExtensionTest6 : UITest() {

    @Test
    @Order(10)
    fun canSelectWithScrollDown_canSelectWithScrollUp5() {

        scenario {
            case(1) {
                expectation {
                    describe("accessStartsWith")
                    it.canSelectWithScrollDown("@DEVELOPER_SET*").thisIsTrue()
                    it.canSelectWithScrollDown("@Gener*").thisIsFalse()
                    it.canSelectWithScrollUp("@Genera*").thisIsTrue()
                    it.canSelectWithScrollUp("@DEVELOPER*").thisIsFalse()
                }
            }
            case(2) {
                expectation {
                    describe("className")
                    it.canSelectWithScrollDown(".XCUIElementTypeCell").thisIsTrue()
                    it.canSelectWithScrollDown(".no exist").thisIsFalse()
                    it.canSelectWithScrollUp(".XCUIElementTypeNavigationBar").thisIsTrue()
                    it.canSelectWithScrollUp(".no exist").thisIsFalse()
                }
            }

        }
    }

}