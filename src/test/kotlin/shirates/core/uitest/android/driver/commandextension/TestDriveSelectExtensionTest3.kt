package shirates.core.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.ScrollDirection
import shirates.core.driver.TestElementCache
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveSelectExtensionTest3 : UITest() {

    @Test
    fun selectInScanResults_withScrollDown_withScrollUp() {

        scenario {
            case(1, "down, end-elements in screen nickname file") {
                condition {
                    it.macro("[Android Settings Top Screen]")
                    it.canSelect("[Account Avatar]").thisIsTrue()
                    it.canSelect("[Tips & support]").thisIsFalse()
                    TestElementCache.scanResults.clear()
                    it.scanElements() // Down
                }.expectation {
                    it.canSelect("[Account Avatar]").thisIsFalse()
                    it.canSelect("[Tips & support]").thisIsTrue()
                    it.selectInScanResults("[Account Avatar]").isFound.thisIsTrue()
                    it.selectInScanResults("[Tips & support]").isFound.thisIsTrue()
                }
            }
            case(2, "up, end-elements in screen nickname file") {
                condition {
                    TestElementCache.scanResults.clear()
                    it.scanElements(direction = ScrollDirection.Up)
                }.expectation {
                    it.canSelect("[Account Avatar]").thisIsTrue()
                    it.canSelect("[Tips & support]").thisIsFalse()
                    it.selectInScanResults("[Account Avatar]").isFound.thisIsTrue()
                    it.selectInScanResults("[Tips & support]").isFound.thisIsTrue()
                }
            }
            case(3, "down, endSelector") {
                condition {
                    TestElementCache.scanResults.clear()
                    it.scanElements(direction = ScrollDirection.Down, endSelector = "[Tips & support]")
                }.expectation {
                    it.canSelect("[Account Avatar]").thisIsFalse()
                    it.canSelect("[Tips & support]").thisIsTrue()
                    it.selectInScanResults("[Account Avatar]").isFound.thisIsTrue()
                    it.selectInScanResults("[Tips & support]").isFound.thisIsTrue()
                }
            }
            case(4, "up, endSelector") {
                condition {
                    TestElementCache.scanResults.clear()
                    it.scanElements(direction = ScrollDirection.Up, endSelector = "[Account Avatar]")
                }.expectation {
                    it.canSelect("[Account Avatar]").thisIsTrue()
                    it.canSelect("[Tips & support]").thisIsFalse()
                    it.selectInScanResults("[Account Avatar]").isFound.thisIsTrue()
                    it.selectInScanResults("[Tips & support]").isFound.thisIsTrue()
                }
            }

            case(99) {
                expectation {
                    assertThatThrownBy {
                        it.selectInScanResults("no exist")
                    }.isInstanceOf(TestDriverException::class.java)
                        .hasMessage(Message.message(id = "elementNotFoundInScanResults", subject = "<no exist>"))
                }
            }
        }
    }

}