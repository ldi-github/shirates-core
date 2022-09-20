package shirates.core.uitest.android.driver.commandextension.work02

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.ScrollDirection
import shirates.core.driver.TestElementCache
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.testcode.Must
import shirates.core.testcode.UITest
import shirates.core.testcode.Want
import shirates.core.utility.sync.StopWatch

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveSelectExtensionTest : UITest() {

    @Test
    fun select_waitSeconds() {

        // Arrange
        it.macro("[Android Settings Top Screen]")
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
                    it.macro("[Android Settings Top Screen]")
                        .dontExist("System")
                }.action {
                    it.selectWithScrollDown("System", log = true)
                }.expectation {
                    it.text.thisIs("System")
                    it.selector!!.text.thisIs("System")

                    assertThatThrownBy {
                        it.selectWithScrollDown("no exist", log = true)
                    }.isInstanceOf(TestDriverException::class.java)
                    it.isEmpty.thisIsTrue()
                }
            }
            case(2) {
                condition {
                    it.flickBottomToTop()
                }.action {
                    it.selectWithScrollUp("Connected devices", log = true)
                }.expectation {
                    // Assert
                    assertThat(it.text).isEqualTo("Connected devices")
                    assertThat(it.selector!!.text).isEqualTo("Connected devices")

                    // Act, Assert
                    assertThatThrownBy {
                        it.selectWithScrollUp("no exist", log = true)
                    }.isInstanceOf(TestDriverException::class.java)
                    it.isEmpty.thisIsTrue()
                }
            }
        }

    }

    @Test
    fun selectInScanResults_withScrollDown_withScrollUp() {

        scenario {
            case(1, "down, end-elements in screen nickname file") {
                condition {
                    it.macro("[Android Settings Top Screen]")
                    it.canSelect("[Account Avatar]", log = true).thisIsTrue()
                    it.canSelect("[Tips & support]", log = true).thisIsFalse()
                    TestElementCache.scanResults.clear()
                    it.scanElements() // Down
                }.expectation {
                    it.canSelect("[Account Avatar]", log = true).thisIsFalse()
                    it.canSelect("[Tips & support]", log = true).thisIsTrue()
                    it.selectInScanResults("[Account Avatar]", log = true).isFound.thisIsTrue()
                    it.selectInScanResults("[Tips & support]", log = true).isFound.thisIsTrue()
                }
            }
            case(2, "up, end-elements in screen nickname file") {
                condition {
                    TestElementCache.scanResults.clear()
                    it.scanElements(direction = ScrollDirection.Up)
                }.expectation {
                    it.canSelect("[Account Avatar]", log = true).thisIsTrue()
                    it.canSelect("[Tips & support]", log = true).thisIsFalse()
                    it.selectInScanResults("[Account Avatar]", log = true).isFound.thisIsTrue()
                    it.selectInScanResults("[Tips & support]", log = true).isFound.thisIsTrue()
                }
            }
            case(3, "down, endSelector") {
                condition {
                    TestElementCache.scanResults.clear()
                    it.scanElements(direction = ScrollDirection.Down, endSelector = "[Tips & support]")
                }.expectation {
                    it.canSelect("[Account Avatar]", log = true).thisIsFalse()
                    it.canSelect("[Tips & support]", log = true).thisIsTrue()
                    it.selectInScanResults("[Account Avatar]", log = true).isFound.thisIsTrue()
                    it.selectInScanResults("[Tips & support]", log = true).isFound.thisIsTrue()
                }
            }
            case(4, "up, endSelector") {
                condition {
                    TestElementCache.scanResults.clear()
                    it.scanElements(direction = ScrollDirection.Up, endSelector = "[Account Avatar]")
                }.expectation {
                    it.canSelect("[Account Avatar]", log = true).thisIsTrue()
                    it.canSelect("[Tips & support]", log = true).thisIsFalse()
                    it.selectInScanResults("[Account Avatar]", log = true).isFound.thisIsTrue()
                    it.selectInScanResults("[Tips & support]", log = true).isFound.thisIsTrue()
                }
            }

            case(99) {
                expectation {
                    assertThatThrownBy {
                        it.selectInScanResults("no exist", log = true)
                    }.isInstanceOf(TestDriverException::class.java)
                        .hasMessage(message(id = "elementNotFoundInScanResults", subject = "<no exist>"))
                }
            }
        }
    }

    @Test
    fun selectInScanResults_withScrollRight_withScrollLeft() {

        scenario {
            case(1, "right, endSelector") {
                condition {
                    TestElementCache.scanResults.clear()
                    writeMemo("Last button", "More")
                    it.macro("[Maps Top Screen]")
                        .select("#below_search_omnibox_container:descendant(.android.widget.Button&&[1])", log = true)
                        .text.memoTextAs("1st button")
                    it.canSelect(readMemo("1st button"), log = true).thisIsTrue()
                }.action {
                    it.select("#below_search_omnibox_container", log = true)
                        .scanElements(direction = ScrollDirection.Right, endSelector = readMemo("Last button"))
                }.expectation {
                    it.canSelect(readMemo("1st button"), log = true).thisIsFalse()
                    it.canSelect("More", log = true).thisIsTrue()
                    it.selectInScanResults(readMemo("1st button"), log = true).isFound.thisIsTrue()
                    it.selectInScanResults(readMemo("Last button"), log = true).isFound.thisIsTrue()
                }
            }
            case(2, "left, endSelector") {
                condition {
                    TestElementCache.scanResults.clear()
                    it.select("#below_search_omnibox_container:descendant(.android.widget.Button&&[-1])", log = true)
                        .text.memoTextAs("Last button")
                    it.canSelect(readMemo("Last button"), log = true).thisIsTrue()
                }.action {
                    it.select("#below_search_omnibox_container")
                        .scanElements(direction = ScrollDirection.Left, endSelector = readMemo("1st button"))
                }.expectation {
                    it.canSelect(readMemo("1st button"), log = true).thisIsTrue()
                    it.canSelect(readMemo("Last button"), log = true).thisIsFalse()
                    it.selectInScanResults(readMemo("1st button"), log = true).isFound.thisIsTrue()
                    it.selectInScanResults(readMemo("Last button"), log = true).isFound.thisIsTrue()
                }
            }
            case(3, "right, imageCompare") {
                condition {
                    TestElementCache.scanResults.clear()
                }.action {
                    it.select("#below_search_omnibox_container", log = true)
                        .scanElements(direction = ScrollDirection.Right, imageCompare = true)
                }.expectation {
                    it.canSelect(readMemo("1st button"), log = true).thisIsFalse()
                    it.canSelect(readMemo("Last button"), log = true).thisIsTrue()
                    it.selectInScanResults(readMemo("1st button"), log = true).isFound.thisIsTrue()
                    it.selectInScanResults(readMemo("Last button"), log = true).isFound.thisIsTrue()
                }
            }
            case(4, "left, imageCompare") {
                condition {
                    TestElementCache.scanResults.clear()
                }.action {
                    it.select("#below_search_omnibox_container", log = true)
                        .scanElements(direction = ScrollDirection.Left, imageCompare = true)
                }.expectation {
                    it.canSelect(readMemo("1st button"), log = true).thisIsTrue()
                    it.canSelect(readMemo("Last button"), log = true).thisIsFalse()
                    it.selectInScanResults(readMemo("1st button"), log = true).isFound.thisIsTrue()
                    it.selectInScanResults(readMemo("Last button"), log = true).isFound.thisIsTrue()
                }
            }
            case(5, "right, default") {
                condition {
                    TestElementCache.scanResults.clear()
                }.action {
                    it.select("#below_search_omnibox_container", log = true)
                        .scanElements(direction = ScrollDirection.Right)
                }.expectation {
                    it.canSelect(readMemo("1st button"), log = true).thisIsFalse()
                    it.canSelect(readMemo("Last button"), log = true).thisIsTrue()
                    it.selectInScanResults(readMemo("1st button"), log = true).isFound.thisIsTrue()
                    it.selectInScanResults(readMemo("Last button"), log = true).isFound.thisIsTrue()
                }
            }
            case(6, "left, default") {
                condition {
                    TestElementCache.scanResults.clear()
                }.action {
                    it.select("#below_search_omnibox_container", log = true)
                        .scanElements(direction = ScrollDirection.Left)
                }.expectation {
                    it.canSelect(readMemo("1st button"), log = true).thisIsTrue()
                    it.canSelect(readMemo("Last button"), log = true).thisIsFalse()
                    it.selectInScanResults(readMemo("1st button"), log = true).isFound.thisIsTrue()
                    it.selectInScanResults(readMemo("Last button"), log = true).isFound.thisIsTrue()
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
                        .macro("[Android Settings Top Screen]")
                }.expectation {
                    it.canSelectWithScrollDown(expression = "[Battery]", log = true).thisIsTrue()
                    it.canSelectWithScrollDown(expression = "[Tips & support]", log = true).thisIsTrue()
                    it.canSelectWithScrollDown(expression = "no exist", log = true).thisIsFalse()

                    it.canSelectWithScrollUp(expression = "[System]", log = true).thisIsTrue()
                    it.canSelectWithScrollUp(expression = "[Display]", log = true).thisIsTrue()
                    it.canSelectWithScrollUp(expression = "[Apps]", log = true).thisIsTrue()
                    it.canSelectWithScrollUp(expression = "no exist", log = true).thisIsFalse()
                }
            }
        }

    }

    @Test
    fun canSelectInScanResults() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Accessibility Screen]")
                        .scanElements()
                }.expectation {
                    it.canSelectInScanResults("[Accessibility]", log = true).thisIsTrue()
                    it.canSelectInScanResults("[Display]", log = true).thisIsTrue()
                    it.canSelectInScanResults("[Text-to-speech output]", log = true).thisIsTrue()

                    it.canSelectInScanResults("General", log = true).thisIsTrue()
                    it.canSelectInScanResults("System con*", log = true).thisIsTrue()
                    it.canSelectInScanResults("*shortcuts", log = true).thisIsTrue()
                    it.canSelectInScanResults("*adjust*", log = true).thisIsTrue()

                    it.canSelectInScanResults("no exist", log = true).thisIsFalse()
                    it.canSelectInScanResults("no exi*", log = true).thisIsFalse()
                    it.canSelectInScanResults("*exist", log = true).thisIsFalse()
                    it.canSelectInScanResults("*o exi*", log = true).thisIsFalse()

                    it.canSelectInScanResults("textMatches=^Caption.*ces$", log = true).thisIsTrue()
                    it.canSelectInScanResults("textMatches=^no.*exist$", log = true).thisIsFalse()

                    it.canSelectInScanResults("#switchWidget", log = true).thisIsTrue()
                    it.canSelectInScanResults("#no exist", log = true).thisIsFalse()

                    it.canSelectInScanResults("@Accessibility", log = true).thisIsTrue()
                    it.canSelectInScanResults("@Accessi*", log = true).thisIsTrue()
                    it.canSelectInScanResults("@*bility", log = true).thisIsTrue()
                    it.canSelectInScanResults("@*cessibili*", log = true).thisIsTrue()

                    it.canSelectInScanResults("@no exist", log = true).thisIsFalse()
                    it.canSelectInScanResults("@no exi*", log = true).thisIsFalse()
                    it.canSelectInScanResults("@*exist", log = true).thisIsFalse()
                    it.canSelectInScanResults("@*exi*", log = true).thisIsFalse()

                    it.canSelectInScanResults(".android.widget.FrameLayout", log = true).thisIsTrue()
                    it.canSelectInScanResults(".android.widget.RelativeLayout", log = true).thisIsTrue()
                    it.canSelectInScanResults(".no exist", log = true).thisIsFalse()

                    it.canSelectInScanResults("scrollable=true", log = true).thisIsTrue()
                    it.canSelectInScanResults("scrollable=false", log = true).thisIsTrue()
                    it.canSelectInScanResults("scrollable=hoge", log = true).thisIsFalse()
                }
            }
        }

    }

    @Test
    fun canSelectAllWithScrollDown_canSelectAllWithScrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.canSelectAllWithScrollDown("[Network & internet]", "[Storage]", "[System]", log = true)
                        .thisIsTrue()
                    it.flickAndGoUp()
                        .canSelectAllWithScrollDown("[Accessibility]", "[System]", "[Network & internet]", log = true)
                        .thisIsFalse()

                    it.canSelectAllWithScrollUp("[System]", "[Accessibility]", "[Network & internet]", log = true)
                        .thisIsTrue()
                    it.flickAndGoDown()
                        .canSelectAllWithScrollUp("[Network & internet]", "[Storage]", "[System]", log = true)
                        .thisIsFalse()
                }
            }
        }
    }

}