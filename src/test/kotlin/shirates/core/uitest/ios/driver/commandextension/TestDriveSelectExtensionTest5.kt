package shirates.core.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveSelectExtensionTest5 : UITest() {

    @Test
    @Order(10)
    fun canSelectWithScrollDown_canSelectWithScrollUp4() {

        scenario {
            case(1) {
                expectation {
                    describe("id")
                    it.canSelectWithScrollDown("#com.apple.settings.developer").thisIsTrue()
                    it.canSelectWithScrollDown("#no exist").thisIsFalse()
                    it.canSelectWithScrollUp("#com.apple.settings.general").thisIsTrue()
                    it.canSelectWithScrollUp("#no exist").thisIsFalse()
                }
            }

            case(2) {
                expectation {
                    describe("access")
                    it.canSelectWithScrollDown("@com.apple.settings.developer").thisIsTrue()
                    it.canSelectWithScrollDown("@com.apple.settings.general").thisIsFalse()
                    it.canSelectWithScrollUp("@com.apple.settings.general").thisIsTrue()
                    it.canSelectWithScrollUp("@com.apple.settings.developer").thisIsFalse()
                }
            }
        }
    }

}