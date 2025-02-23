package shirates.core.vision.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.canSelectWithScrollDown
import shirates.core.driver.commandextension.canSelectWithScrollUp
import shirates.core.driver.commandextension.thisIsFalse
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.testcode.Unstable
import shirates.core.testcode.Want
import shirates.core.vision.classicScope
import shirates.core.vision.driver.commandextension.describe
import shirates.core.vision.testcode.VisionTest

@Unstable("Direct mode")
@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class VisionDriveDetectExtensionTest6 : VisionTest() {

    @Unstable("Direct mode")
    @Test
    @Order(10)
    fun canSelectWithScrollDown_canSelectWithScrollUp5() {

        classicScope {
            scenario {
                case(1) {
                    expectation {
                        describe("accessStartsWith")
                        it.canSelectWithScrollDown("@com.apple.settings.dev*").thisIsTrue("@com.apple.settings.dev*")
                        it.canSelectWithScrollDown("@com.apple.settings.gener*")
                            .thisIsFalse("@com.apple.settings.gener*")
                        it.canSelectWithScrollUp("@com.apple.settings.genera*")
                            .thisIsTrue("@com.apple.settings.genera*")
                        it.canSelectWithScrollUp("@com.apple.settings.developer*")
                            .thisIsFalse("@com.apple.settings.developer*")
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

}