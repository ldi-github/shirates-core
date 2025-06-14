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
class VisionDriveTapExtensionTest5 : VisionTest() {

    @Test
    @Order(10)
    fun tapWithScrollUp_textContains() {

        classicScope {
            scenario {
                case(1) {
                    condition {
                        it.macro("[iOS Settings Top Screen]")
                            .flickCenterToTop()
                    }.action {
                        it.tapWithScrollUp("*enera*")
                    }.expectation {
                        it.screenIs("[General Screen]")
                    }
                }
            }
        }
    }

    @Test
    @Order(20)
    fun tapWithScrollUp_textEndsWith() {

        classicScope {
            scenario {
                case(1) {
                    condition {
                        it.macro("[iOS Settings Top Screen]")
                            .flickCenterToTop()
                    }.action {
                        it.tapWithScrollUp("*eneral")
                    }.expectation {
                        it.screenIs("[General Screen]")
                    }
                }
            }
        }
    }

    @Test
    @Order(30)
    fun tapWithScrollUp_textMatches() {

        classicScope {
            scenario {
                case(1) {
                    condition {
                        it.macro("[iOS Settings Top Screen]")
                            .flickCenterToTop()
                    }.action {
                        it.tapWithScrollUp("textMatches=^Gen.*al$")
                    }.expectation {
                        it.screenIs("[General Screen]")
                    }
                }
            }
        }
    }

    @Test
    @Order(40)
    fun tapWithScrollUp_id() {

        classicScope {
            scenario {
                case(1) {
                    condition {
                        it.macro("[iOS Settings Top Screen]")
                            .flickCenterToTop()
                    }.action {
                        it.tapWithScrollUp("#General")
                    }.expectation {
                        it.screenIs("[General Screen]")
                    }
                }
            }
        }
    }

//    @Test
//    @Order(3)
//    fun tapWithScrollUp_access() {
//
//    }
//
//    @Test
//    @Order(3)
//    fun tapWithScrollUp_accessStartsWith() {
//
//    }

    @Test
    @Order(50)
    fun tapWithScrollUp_xpath() {

        classicScope {
            scenario {
                case(1) {
                    condition {
                        it.macro("[iOS Settings Top Screen]")
                            .flickCenterToTop()
                    }.action {
                        it.tapWithScrollUp("xpath=//*[@label='General' and @visible='true']")
                    }.expectation {
                        it.screenIs("[General Screen]")
                    }
                }
            }
        }
    }

}