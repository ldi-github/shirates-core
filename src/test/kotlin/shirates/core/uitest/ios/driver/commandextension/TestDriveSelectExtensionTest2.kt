package shirates.core.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveSelectExtensionTest2 : UITest() {

    @Test
    @Order(10)
    fun canSelectWithScrollDown_canSelectWithScrollUp1() {

        scenario {
            case(1) {
                condition {
                    it.restartApp()
                        .macro("[iOS Settings Top Screen]")
                }.expectation {
                    describe("[nickname]")
                    it.canSelectWithScrollDown(expression = "[Developer]").thisIsTrue()
                    it.canSelectWithScrollDown(expression = "[General]").thisIsFalse()
                    it.canSelectWithScrollUp(expression = "[General]").thisIsTrue()
                    it.canSelectWithScrollUp(expression = "[Developer]").thisIsFalse()
                }
            }
            case(2) {
                expectation {
                    describe("text")
                    it.canSelectWithScrollDown("Developer").thisIsTrue()
                    it.canSelectWithScrollDown("General").thisIsFalse()
                    it.canSelectWithScrollUp("General").thisIsTrue()
                    it.canSelectWithScrollUp("Developer").thisIsFalse()
                }
            }
        }

    }

}