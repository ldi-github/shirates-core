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
class TestDriveSelectExtensionTest5 : VisionTest() {

    @Test
    @Order(10)
    fun canSelectWithScrollDown_canSelectWithScrollUp4() {

        classicScope {
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

}