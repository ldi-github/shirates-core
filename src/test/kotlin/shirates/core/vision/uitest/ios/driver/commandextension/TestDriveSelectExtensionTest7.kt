package shirates.core.vision.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.canSelectWithScrollDown
import shirates.core.driver.commandextension.canSelectWithScrollUp
import shirates.core.driver.commandextension.thisIsFalse
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.testcode.Want
import shirates.core.vision.classicScope
import shirates.core.vision.driver.commandextension.describe
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveSelectExtensionTest7 : VisionTest() {

    @Test
    @Order(10)
    fun canSelectWithScrollDown_canSelectWithScrollUp6() {

        classicScope {
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

}