package shirates.core.vision.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class VisionDriveKeyboardExtensionTest2 : VisionTest() {

    @Test
    fun tap_search() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Search Screen]")
                        .sendKeys("appium")
                        .keyboardIsShown()
                }.action {
                    it.tap("search")
                }.expectation {
                    it.keyboardIsNotShown()
                }
            }
        }

    }


//    @Test
//    @Order(30)
//    fun pressEnter_search() {
//
//        scenario {
//            case(1) {
//                condition {
//                    it.macro("[iOS Search Screen]")
//                        .sendKeys("appium")
//                        .keyboardIsShown()
//                }.action {
//                    it.tap("search")
//                }.expectation {
//                    it.keyboardIsNotShown()
//                }
//            }
//        }
//
//    }

//    @Test
//    @Order(40)
//    fun pressSearch() {
//
//        scenario {
//            case(1) {
//                condition {
//                    it.macro("[iOS Search Screen]")
//                        .sendKeys("appium")
//                        .keyboardIsShown()
//                }.action {
//                    it.tap("search")
//                }.expectation {
//                    it.keyboardIsNotShown()
//                }
//            }
//        }
//    }

//    @Test
//    @Order(50)
//    fun tapSoftwareKey() {
//
//        scenario {
//            case(1) {
//                condition {
//                    it.macro("[iOS Search Screen]")
//                        .tap("#SpotlightSearchField")
//                        .sendKeys("appium")
//                        .keyboardIsShown()
//                }.action {
//                    it.tap("search")
//                }.expectation {
//                    it.keyboardIsNotShown()
//                }
//            }
//            case(2) {
//                expectation {
//                    try {
//                        it.tap("search")
//                        NG()
//                    } catch (t: Throwable) {
//                        OK(t.message!!)
//                    }
//                }
//            }
//        }
//
//    }

}