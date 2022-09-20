package shirates.core.uitest.ios.driver.commandextension.work02

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.testcode.Must
import shirates.core.testcode.UITest
import shirates.core.testcode.Want
import shirates.core.utility.sync.StopWatch

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveSelectExtensionTest : UITest() {

    @Test
    fun select_waitSeconds() {

        // Arrange
        it.macro("[iOS Settings Top Screen]")
        // Act
        run {
            val sw = StopWatch()
            sw.start()
            val e = it.select("no exist", waitSeconds = 0.0, throwsException = false)
            val t = sw.elapsedMillis
            println(t)
            // Assert
            assertThat(e.isEmpty).isTrue()
            assertThat(t >= 0)
            assertThat(t < 1 * 1000)
        }

        run {
            val sw = StopWatch()
            sw.start()
            val e = it.select("no exist", waitSeconds = 1.2, throwsException = false)
            val t = sw.elapsedMillis
            println(t)
            // Assert
            assertThat(e.isEmpty).isTrue()
            assertThat(t >= 1.2 * 1000)
            assertThat(t < 2 * 1000)
        }

        run {
            val sw = StopWatch()
            sw.start()
            val e = it.select("no exist", waitSeconds = 2.3, throwsException = false)
            val t = sw.elapsedMillis
            println(t)
            // Assert
            assertThat(e.isEmpty).isTrue()
            assertThat(t >= 2.3 * 1000)
            assertThat(t < 3 * 1000)
        }
    }

    @Must
    @Test
    fun select_selectWithScrollDown_selectWithScrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .dontExist("no exist")
                }.action {
                    it.selectWithScrollDown("Developer")
                }.expectation {
                    it.label.thisIs("Developer")
                    it.selector!!.text.thisIs("Developer")

                    assertThatThrownBy {
                        it.selectWithScrollDown("no exist")
                    }.isInstanceOf(TestDriverException::class.java)
                    it.isEmpty.thisIsTrue()
                }
            }
            case(2) {
                condition {
                    it.selectWithScrollUp("General")
                }.expectation {
                    it.label.thisIs("General")
                    it.selector!!.text.thisIs("General")

                    assertThatThrownBy {
                        it.selectWithScrollUp("no exist")
                    }.isInstanceOf(TestDriverException::class.java)
                    it.isEmpty.thisIsTrue()
                }
            }
        }

    }

    @Test
    fun selectInScanResults() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .scanElements()
                }.action {
                    it.selectInScanResults("Developer")
                }.expectation {
                    it.label.thisIs("Developer")

                    assertThatThrownBy {
                        it.selectInScanResults("no exist")
                    }.isInstanceOf(TestDriverException::class.java)
                        .hasMessage(message(id = "elementNotFoundInScanResults", subject = "<no exist>"))
                }
            }
        }
    }

    @Test
    fun canSelectWithScrollDown_canSelectWithScrollUp() {

        scenario {
            case(1) {
                condition {
                    it.restartApp()
                        .macro("[iOS Settings Top Screen]")
                }.expectation {
                    describe("[nickname]")
                    it.canSelectWithScrollDown(expression = "[Developer]").thisIsTrue()
                    it.canSelectWithScrollDown(expression = "[General]").thisIsFalse()
                    it.canSelectWithScrollUp(expression = "[General]").thisIsTrue()
                    it.canSelectWithScrollUp(expression = "[Developer]").thisIsFalse()
                }
            }
            case(2) {
                expectation {
                    describe("text")
                    it.canSelectWithScrollDown("Developer").thisIsTrue()
                    it.canSelectWithScrollDown("General").thisIsFalse()
                    it.canSelectWithScrollUp("General").thisIsTrue()
                    it.canSelectWithScrollUp("Developer").thisIsFalse()
                }
            }
            case(3) {
                expectation {
                    describe("textStartsWith")
                    it.canSelectWithScrollDown("Dev*").thisIsTrue()
                    it.canSelectWithScrollDown("Gene*").thisIsFalse()
                    it.canSelectWithScrollUp("Gene*").thisIsTrue()
                    it.canSelectWithScrollUp("Dev*").thisIsFalse()
                }
            }
            case(4) {
                expectation {
                    describe("textContains")
                    it.canSelectWithScrollDown("*evelope*").thisIsTrue()
                    it.canSelectWithScrollDown("*enera*").thisIsFalse()
                    it.canSelectWithScrollUp("*enera*").thisIsTrue()
                    it.canSelectWithScrollUp("*evelope*").thisIsFalse()
                }
            }

            case(5) {
                expectation {
                    describe("textEndsWith")
                    it.canSelectWithScrollDown("*eveloper").thisIsTrue()
                    it.canSelectWithScrollDown("*eneral").thisIsFalse()
                    it.canSelectWithScrollUp("*eneral").thisIsTrue()
                    it.canSelectWithScrollUp("*eveloper").thisIsFalse()
                }
            }

            case(6) {
                expectation {
                    describe("textMatches")
                    it.canSelectWithScrollDown("textMatches=^Dev.*per$").thisIsTrue()
                    it.canSelectWithScrollDown("textMatches=^Gen.*al$").thisIsFalse()
                    it.canSelectWithScrollUp("textMatches=^Gen.*al$").thisIsTrue()
                    it.canSelectWithScrollUp("textMatches=^Dev.*per$").thisIsFalse()
                }
            }

            case(7) {
                expectation {
                    describe("id")
                    it.canSelectWithScrollDown("#DEVELOPER_SETTINGS").thisIsTrue()
                    it.canSelectWithScrollDown("#no exist").thisIsFalse()
                    it.canSelectWithScrollUp("#General").thisIsTrue()
                    it.canSelectWithScrollUp("#no exist").thisIsFalse()
                }
            }

            case(8) {
                expectation {
                    describe("access")
                    it.canSelectWithScrollDown("@DEVELOPER_SETTINGS").thisIsTrue()
                    it.canSelectWithScrollDown("@General").thisIsFalse()
                    it.canSelectWithScrollUp("@General").thisIsTrue()
                    it.canSelectWithScrollUp("@DEVELOPER_SETTINGS").thisIsFalse()
                }
            }

            case(9) {
                expectation {
                    describe("accessStartsWith")
                    it.canSelectWithScrollDown("@DEVELOPER_SET*").thisIsTrue()
                    it.canSelectWithScrollDown("@Gener*").thisIsFalse()
                    it.canSelectWithScrollUp("@Genera*").thisIsTrue()
                    it.canSelectWithScrollUp("@DEVELOPER*").thisIsFalse()
                }
            }

            case(10) {
                expectation {
                    describe("className")
                    it.canSelectWithScrollDown(".XCUIElementTypeCell").thisIsTrue()
                    it.canSelectWithScrollDown(".no exist").thisIsFalse()
                    it.canSelectWithScrollUp(".XCUIElementTypeNavigationBar").thisIsTrue()
                    it.canSelectWithScrollUp(".no exist").thisIsFalse()
                }
            }

            case(11) {
                expectation {
                    describe("xpath")
                    it.canSelectWithScrollDown("xpath=//*[@label='Developer' and @visible='true']").thisIsTrue()
                    it.canSelectWithScrollDown("xpath=//*[@label='General' and @visible='true']").thisIsFalse()
                    it.canSelectWithScrollUp("xpath=//*[@label='General' and @visible='true']").thisIsTrue()
                    it.canSelectWithScrollUp("xpath=//*[@label='Developer' and @visible='true']").thisIsFalse()
                }
            }
        }

    }

    @Test
    fun canSelectInScanResults() {

        scenario {
            case(1) {
                condition {
                    it.restartApp()
                    it.macro("[iOS Settings Top Screen]")
                        .scanElements()
                }.expectation {
                    describe("")
                    it.canSelectInScanResults(expression = "[General]").thisIsTrue()
                    it.canSelectInScanResults(expression = "[Developer]").thisIsTrue()
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
                    it.canSelectInScanResults("#ACCESSIBILITY").thisIsTrue()
                    it.canSelectInScanResults("#DEVELOPER_SETTINGS").thisIsTrue()
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

    @Test
    fun canSelectAllWithScrollDown_canSelectAllWithScrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.expectation {
                    it.canSelectAllWithScrollDown("[General]", "[Privacy]", "[Developer]").thisIsTrue()
                }
            }
            case(2) {
                condition {
                    it.flickTopToBottom()
                }.expectation {
                    it.canSelectAllWithScrollDown("[Developer]", "[Privacy]", "[General]").thisIsFalse()
                    it.canSelectAllWithScrollUp("[Developer]", "[Privacy]", "[General]").thisIsTrue()
                }
            }
            case(3) {
                condition {
                    it.flickBottomToTop()
                }.expectation {
                    it.canSelectAllWithScrollUp("[General]", "[Privacy]", "[Developer]").thisIsFalse()
                }
            }
        }
    }
}