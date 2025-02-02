package shirates.core.vision.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testDriveScope
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveSelectExtensionTest8 : VisionTest() {

    @Test
    @Order(10)
    fun canSelectInScanResults() {

        testDriveScope {
            scenario {
                case(1) {
                    condition {
                        it.restartApp()
                        it.macro("[iOS Settings Top Screen]")
                            .scanElements()
                    }.expectation {
                        it.canSelectInScanResults("[General]").thisIsTrue()
                        it.canSelectInScanResults("[Developer]").thisIsTrue()
                        it.canSelectInScanResults("General").thisIsTrue()
                        it.canSelectInScanResults("Developer").thisIsTrue()
                        it.canSelectInScanResults("no exist").thisIsFalse()

                        it.canSelectInScanResults("Gene*").thisIsTrue()
                        it.canSelectInScanResults("Dev*").thisIsTrue()
                        it.canSelectInScanResults("no exist*").thisIsFalse()
                        it.canSelectInScanResults("*enera*").thisIsTrue()
                        it.canSelectInScanResults("*evelope*").thisIsTrue()
                        it.canSelectInScanResults("*no exist*").thisIsFalse()

                        it.canSelectInScanResults("*eneral").thisIsTrue()
                        it.canSelectInScanResults("*eveloper").thisIsTrue()
                        it.canSelectInScanResults("*no exist").thisIsFalse()

                        it.canSelectInScanResults("textMatches=^Ge.*ral$").thisIsTrue()
                        it.canSelectInScanResults("textMatches=^Dev.*per$").thisIsTrue()
                        it.canSelectInScanResults("textMatches=^no.*exist$").thisIsFalse()
                        it.canSelectInScanResults("#com.apple.settings.accessibility").thisIsTrue()
                        it.canSelectInScanResults("#com.apple.settings.developer").thisIsTrue()
                        it.canSelectInScanResults("#no exist").thisIsFalse()

//        it.canSelectInScanResults("@").thisIsTrue()
//        it.canSelectInScanResults("@").thisIsTrue()
//        it.canSelectInScanResults("@no exist").thisIsFalse()

//        it.canSelectInScanResults("@*").thisIsTrue()
//        it.canSelectInScanResults("@*").thisIsTrue()
//        it.canSelectInScanResults("@no exist*").thisIsFalse()

                        it.canSelectInScanResults("value=Accessibility").thisIsTrue()
                        it.canSelectInScanResults("value=Developer").thisIsTrue()
                        it.canSelectInScanResults("value=no exist").thisIsFalse()

                        it.canSelectInScanResults("valueStartsWith=Accessi").thisIsTrue()
                        it.canSelectInScanResults("valueStartsWith=Deve").thisIsTrue()
                        it.canSelectInScanResults("valueStartsWith=no exist").thisIsFalse()

                        it.canSelectInScanResults("valueContains=ssibili").thisIsTrue()
                        it.canSelectInScanResults("valueContains=velop").thisIsTrue()
                        it.canSelectInScanResults("valueContains=no exist").thisIsFalse()

                        it.canSelectInScanResults("valueEndsWith=bility").thisIsTrue()
                        it.canSelectInScanResults("valueEndsWith=per").thisIsTrue()
                        it.canSelectInScanResults("valueEndsWith=no exist").thisIsFalse()

                        it.canSelectInScanResults("valueMatches=^Accessibility$").thisIsTrue()
                        it.canSelectInScanResults("valueMatches=^Developer$").thisIsTrue()
                        it.canSelectInScanResults("valueMatches=no exist").thisIsFalse()

                        it.canSelectInScanResults(".XCUIElementTypeNavigationBar").thisIsTrue()
                        it.canSelectInScanResults(".XCUIElementTypeCell").thisIsTrue()
                        it.canSelectInScanResults(".no exist").thisIsFalse()

                        // visible is for iOS
                        it.canSelectInScanResults("visible=true").thisIsTrue()
                        it.canSelectInScanResults("visible=false").thisIsTrue()
                    }
                }
            }
        }
    }

    @Test
    @Order(20)
    fun canSelectAllWithScrollDown_canSelectAllWithScrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.expectation {
                    withScrollDown {
                        it.canDetectAll("General", "Privacy & Security", "Developer")
                            .thisIsTrue("General,Privacy & Security,Developer")
                    }
                }
            }
            case(2) {
                condition {
                    it.flickTopToBottom()
                }.expectation {
                    withScrollDown {
                        it.canDetectAll("Developer", "Privacy & Security", "General")
                            .thisIsFalse("Developer,Privacy & Security,General")
                    }
                    withScrollUp {
                        it.canDetectAll("Developer", "Privacy & Security", "General")
                            .thisIsTrue("Developer,Privacy & Security,General")
                    }
                }
            }
            case(3) {
                condition {
                    it.flickBottomToTop()
                }.expectation {
                    withScrollUp {
                        it.canDetectAll("General", "Privacy & Security", "Developer")
                            .thisIsFalse("General,Privacy & Security,Developer")
                    }
                }
            }
        }
    }

}