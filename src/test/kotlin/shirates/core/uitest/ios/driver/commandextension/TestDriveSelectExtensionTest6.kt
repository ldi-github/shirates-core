package shirates.core.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Unstable
import shirates.core.testcode.Want

@Unstable("Direct mode")
@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveSelectExtensionTest6 : UITest() {

    @Unstable("Direct mode")
    @Test
    @Order(10)
    fun canSelectWithScrollDown_canSelectWithScrollUp5() {

        scenario {
            case(1) {
                expectation {
                    describe("accessStartsWith")
                    it.canSelectWithScrollDown("@DEVELOPER_SET*").thisIsTrue("@DEVELOPER_SET*")
                    it.canSelectWithScrollDown("@Gener*").thisIsFalse("@Gener*")
                    it.canSelectWithScrollUp("@Genera*").thisIsTrue("@Genera*")
                    it.canSelectWithScrollUp("@DEVELOPER*").thisIsFalse("@DEVELOPER*")
                }
            }
            case(2) {
                expectation {
                    describe("className")
                    it.canSelectWithScrollDown(".XCUIElementTypeCell").thisIsTrue(".XCUIElementTypeCell")
                    it.canSelectWithScrollDown(".no exist").thisIsFalse(".no exist")
                    it.canSelectWithScrollUp(".XCUIElementTypeNavigationBar")
                        .thisIsTrue(".XCUIElementTypeNavigationBar")
                    it.canSelectWithScrollUp(".no exist").thisIsFalse(".no exist")
                }
            }

        }
    }

}