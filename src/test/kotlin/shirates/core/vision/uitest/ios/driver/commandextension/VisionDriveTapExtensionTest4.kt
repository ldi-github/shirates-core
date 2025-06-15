package shirates.core.vision.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.flickCenterToTop
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.tapWithScrollUp
import shirates.core.testcode.Want
import shirates.core.vision.classicScope
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class VisionDriveTapExtensionTest4 : VisionTest() {

    @Test
    @Order(10)
    fun tapWithScrollUp_nickname() {

        classicScope {
            scenario {
                case(1) {
                    condition {
                        it.macro("[iOS Settings Top Screen]")
                            .flickCenterToTop()
                    }.action {
                        it.tapWithScrollUp(expression = "[General]")
                    }.expectation {
                        it.screenIs("[General Screen]")
                    }
                }
            }
        }
    }

    @Test
    @Order(20)
    fun tapWithScrollUp_text() {

        classicScope {
            scenario {
                case(1) {
                    condition {
                        it.macro("[iOS Settings Top Screen]")
                            .flickCenterToTop()
                    }.action {
                        it.tapWithScrollUp("General")
                    }.expectation {
                        it.screenIs("[General Screen]")
                    }
                }
            }
        }
    }

    @Test
    @Order(30)
    fun tapWithScrollUp_textStartsWith() {

        classicScope {
            scenario {
                case(1) {
                    condition {
                        it.macro("[iOS Settings Top Screen]")
                            .flickCenterToTop()
                    }.action {
                        it.tapWithScrollUp("Gen*")
                    }.expectation {
                        it.screenIs("[General Screen]")
                    }
                }
            }
        }
    }

}