package shirates.core.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveSelectExtensionTest7 : UITest() {

    @Test
    @Order(10)
    fun canSelectWithScrollDown_canSelectWithScrollUp6() {

        scenario {
            case(1) {
                expectation {
                    describe("xpath")
                    it.canSelectWithScrollDown("xpath=//*[@label='Developer' and @visible='true']").thisIsTrue()
                    it.canSelectWithScrollDown("xpath=//*[@label='General' and @visible='true']").thisIsFalse()
                    it.canSelectWithScrollUp("xpath=//*[@label='General' and @visible='true']").thisIsTrue()
                    it.canSelectWithScrollUp("xpath=//*[@label='Developer' and @visible='true']").thisIsFalse()
                }
            }
        }
    }

}