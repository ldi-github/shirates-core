package shirates.core.vision.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.tapWithScrollDown
import shirates.core.testcode.Want
import shirates.core.vision.classicScope
import shirates.core.vision.driver.commandextension.macro
import shirates.core.vision.driver.commandextension.screenIs
import shirates.core.vision.driver.commandextension.tapWithScrollDown
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveTapExtensionTest3 : VisionTest() {

    @Test
    @Order(10)
    fun tapWithScrollDown_textContains() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.tapWithScrollDown("evelop")  // Developer
                }.expectation {
                    it.screenIs("[Developer Screen]")
                }
            }
        }

    }

    @Test
    @Order(20)
    fun tapWithScrollDown_textEndsWith() {

        classicScope {
            scenario {
                case(1) {
                    condition {
                        it.macro("[iOS Settings Top Screen]")
                    }.action {
                        it.tapWithScrollDown("*eveloper")
                    }.expectation {
                        it.screenIs("[Developer Screen]")
                    }
                }
            }
        }
    }

    @Test
    @Order(30)
    fun tapWithScrollDown_textMatches() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.tapWithScrollDown("textMatches=^Dev.*per$")
                }.expectation {
                    it.screenIs("[Developer Screen]")
                }
            }
        }

    }

    @Test
    @Order(40)
    fun tapWithScrollDown_id() {

        classicScope {
            scenario {
                case(1) {
                    condition {
                        it.macro("[iOS Settings Top Screen]")
                    }.action {
                        it.tapWithScrollDown("Developer")
                    }.expectation {
                        it.screenIs("[Developer Screen]")
                    }
                }
            }
        }
    }

//    @Test
//    @Order(2)
//    fun tapWithScrollDown_access() {
//
//    }
//
//    @Test
//    @Order(2)
//    fun tapWithScrollDown_accessStartsWith() {
//
//    }

    @Test
    @Order(50)
    fun tapWithScrollDown_xpath() {

        classicScope {
            scenario {
                case(1) {
                    condition {
                        it.macro("[Developer Screen]")
                    }.action {
                        it.tapWithScrollDown("xpath=//*[@label='Developer']")
                    }.expectation {
                        it.screenIs("[Developer Screen]")
                    }
                }
            }
        }
    }

}