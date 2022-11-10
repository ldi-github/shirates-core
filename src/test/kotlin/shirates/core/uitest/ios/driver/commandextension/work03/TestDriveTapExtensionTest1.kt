package shirates.core.uitest.ios.driver.commandextension.work03

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveTapExtensionTest1 : UITest() {

    @Order(10)
    @Test
    fun tap_x_y() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    val e = it.select("General")
                    it.tap(x = e.bounds.centerX, y = e.bounds.centerY)
                }.expectation {
                    it.screenIs("[General Screen]")
                }
            }
        }

    }

    @Order(20)
    @Test
    fun tap_nickname() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.tap(expression = "[General]")
                }.expectation {
                    it.screenIs("[General Screen]")
                }
            }
        }

    }

    @Order(30)
    @Test
    fun tap_text() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.tap("General")
                }.expectation {
                    it.screenIs("[General Screen]")
                }
            }
        }

    }

    @Order(40)
    @Test
    fun tap_textStartsWith() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.tap("textStartsWith=Gene")
                }.expectation {
                    it.screenIs("[General Screen]")
                }
            }
        }

    }

    @Order(50)
    @Test
    fun tap_textContains() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.tap("*enera*")
                }.expectation {
                    it.screenIs("[General Screen]")
                }
            }
        }

    }

    @Order(60)
    @Test
    fun tap_textMatches() {

        scenario {
            case(1) {
                condition {
                    it.restartApp()
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.tap("textMatches=^Gen.*al$")
                }.expectation {
                    it.screenIs("[General Screen]")
                }
            }
        }

    }

    @Order(70)
    @Test
    fun tap_id() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.tap("#General")
                }.expectation {
                    it.screenIs("[General Screen]")
                }
            }
        }

    }

//    @Order(1)
//    @Test
//    fun tap_access() {
//
//    }
//
//    @Order(1)
//    @Test
//    fun tap_accessStartsWith() {
//
//    }

    @Order(80)
    @Test
    fun tap_xpath() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.tap("xpath=//*[@label='General']")
                }.expectation {
                    it.screenIs("[General Screen]")
                }
            }
        }

    }

    @Order(90)
    @Test
    fun tapWithScrollDown_nickname() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.tapWithScrollDown("[Developer]")
                }.expectation {
                    it.screenIs("[Developer Screen]")
                }
            }
        }

    }

    @Order(100)
    @Test
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

    @Order(110)
    @Test
    fun tapWithScrollDown_textStartsWith() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.tapWithScrollDown("Dev*")
                }.expectation {
                    it.screenIs("[Developer Screen]")
                }
            }
        }

    }

    @Order(120)
    @Test
    fun tapWithScrollDown_textContains() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.tapWithScrollDown("*evelop*")
                }.expectation {
                    it.screenIs("[Developer Screen]")
                }
            }
        }

    }

    @Order(130)
    @Test
    fun tapWithScrollDown_textEndsWith() {

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

    @Order(140)
    @Test
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

    @Order(150)
    @Test
    fun tapWithScrollDown_id() {

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

//    @Order(2)
//    @Test
//    fun tapWithScrollDown_access() {
//
//    }
//
//    @Order(2)
//    @Test
//    fun tapWithScrollDown_accessStartsWith() {
//
//    }

    @Order(160)
    @Test
    fun tapWithScrollDown_xpath() {

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