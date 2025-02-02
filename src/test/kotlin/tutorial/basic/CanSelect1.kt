package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class CanSelect1 : UITest() {

    @Test
    @Order(10)
    fun canSelect() {

        scenario {
            case(1) {
                action {
                    it.canSelect("Settings")
                }
            }
            case(2) {
                action {
                    it.canSelectWithScrollDown("System")
                }
            }
            case(3) {
                action {
                    it.canSelectWithScrollUp("Settings")
                }
            }
            case(4) {
                action {
                    it.canSelectAllWithScrollDown("Settings", "System")
                }
            }
            case(5) {
                action {
                    it.canSelectAllWithScrollUp("Settings", "System")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun canSelectInScanElements() {

        scenario {
            case(1) {
                condition {
                    it.scanElements()
                }.action {
                    it.canSelectInScanResults("Settings")
                    it.canSelectInScanResults("Accessibility")
                    it.canSelectInScanResults("System")
                    it.canSelectInScanResults("Foo")
                }
            }
            case(2) {
                action {
                    it.canSelectAllInScanResults("Settings", "Accessibility", "System")
                    it.canSelectAllInScanResults("Settings", "Accessibility", "Foo")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun canSelect_true() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.canSelect("Network & internet")
                        .thisIsTrue(message = "canSelect(\"Network & internet\") is true")
                }
            }
        }
    }

    @Test
    @Order(40)
    fun canSelect_false() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.canSelect("Network business")
                        .thisIsFalse("canSelect(\"Network business\") is false")
                }
            }
        }
    }

    @Test
    @Order(50)
    fun canSelectWithScrollDown_true() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.canSelectWithScrollDown("System")
                        .thisIsTrue("canSelectWithScrollDown(\"System\") is true")
                }
            }
        }
    }

    @Test
    @Order(60)
    fun canSelectWithScrollDown_false() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.canSelectWithScrollDown("Network business")
                        .thisIsFalse("canSelectWithScrollDown(\"Network business\") is false")
                }
            }
        }
    }

    @Test
    @Order(70)
    fun canSelectWithScrollUp_true() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .flickBottomToTop()
                }.expectation {
                    it.canSelectWithScrollUp("Network & internet")
                        .thisIsTrue("canSelectWithScrollUp(\"Netowork & internet\") is true")
                }
            }
        }
    }

    @Test
    @Order(80)
    fun canSelectWithScrollUp_false() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .flickBottomToTop()
                }.expectation {
                    it.canSelectWithScrollUp("Network business")
                        .thisIsFalse("canSelectWithScrollUp(\"Network business\") is false")
                }
            }
        }
    }

    @Test
    @Order(90)
    fun canSelectInScanResults_true() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .scanElements()
                }.expectation {
                    it.canSelectInScanResults("Network & internet")
                        .thisIsTrue("canSelectInScanResults(\"Network & internet\") is true")
                }
            }
        }
    }

    @Test
    @Order(100)
    fun canSelectInScanResults_false() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .scanElements()
                }.expectation {
                    it.canSelectInScanResults("Network business")
                        .thisIsFalse("canSelectInScanResults(\"Network business\") is false")
                }
            }
        }
    }


}