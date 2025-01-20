package shirates.core.vision.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.tap
import shirates.core.driver.commandextension.tapWithScrollDown
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.macro
import shirates.core.vision.driver.commandextension.screenIs
import shirates.core.vision.driver.commandextension.tapWithScrollDown
import shirates.core.vision.testDriveScope
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveTapExtensionTest2 : VisionTest() {

    @Test
    @Order(10)
    fun tap_id() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    testDriveScope {
                        it.tap("#General")
                    }
                }.expectation {
                    it.screenIs("[General Screen]")
                }
            }
        }

    }

//    @Test
//    @Order(1)
//    fun tap_access() {
//
//    }
//
//    @Test
//    @Order(1)
//    fun tap_accessStartsWith() {
//
//    }

    @Test
    @Order(20)
    fun tap_xpath() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    testDriveScope {
                        it.tap("xpath=//*[@label='General']")
                    }
                }.expectation {
                    it.screenIs("[General Screen]")
                }
            }
        }

    }

    @Test
    @Order(30)
    fun tapWithScrollDown_nickname() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    testDriveScope {
                        it.tapWithScrollDown("[Developer]")
                    }
                }.expectation {
                    it.screenIs("[Developer Screen]")
                }
            }
        }

    }

    @Test
    @Order(40)
    fun tapWithScrollDown_text() {

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

    @Test
    @Order(50)
    fun tapWithScrollDown_textStartsWith() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    testDriveScope {
                        it.tapWithScrollDown("Dev*")
                    }
                }.expectation {
                    it.screenIs("[Developer Screen]")
                }
            }
        }

    }

}