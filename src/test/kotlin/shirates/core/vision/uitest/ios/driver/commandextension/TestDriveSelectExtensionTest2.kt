package shirates.core.vision.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisIsFalse
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveSelectExtensionTest2 : VisionTest() {

    @Test
    @Order(10)
    fun canDetectWithScrollDown_canDetectWithScrollUp1() {

        scenario {
            case(1) {
                condition {
                    it.restartApp()
                        .macro("[iOS Settings Top Screen]")
                }.expectation {
                    describe("[nickname]")
                    it.canDetectWithScrollDown("[Developer]").thisIsTrue("[Developer]")
                    it.canDetectWithScrollDown("[General]").thisIsFalse("[General]")
                    it.canDetectWithScrollUp("[General]").thisIsTrue("[General]")
                    it.canDetectWithScrollUp("[Developer]").thisIsFalse("[Developer]")
                }
            }
            case(2) {
                expectation {
                    describe("text")
                    it.canDetectWithScrollDown("Developer").thisIsTrue("Developer")
                    it.canDetectWithScrollDown("General").thisIsFalse("General")
                    it.canDetectWithScrollUp("General").thisIsTrue("General")
                    it.canDetectWithScrollUp("Developer").thisIsFalse("Developer")
                }
            }
        }

    }

}