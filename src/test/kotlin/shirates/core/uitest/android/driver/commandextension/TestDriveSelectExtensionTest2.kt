package shirates.core.uitest.android.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.ScrollDirection
import shirates.core.driver.TestElementCache
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveSelectExtensionTest2 : UITest() {

    @Test
    fun selectInScanResults_withScrollRight_withScrollLeft() {

        scenario {
            case(1, "right, endSelector") {
                condition {
                    TestElementCache.scanResults.clear()
                    writeMemo("Last button", "More")
                    it.macro("[Maps Top Screen]")
                        .select("#below_search_omnibox_container:descendant(.android.widget.Button&&[1])")
                        .text.memoTextAs("1st button")
                    it.canSelect(readMemo("1st button")).thisIsTrue()
                }.action {
                    it.select("#below_search_omnibox_container")
                        .scanElements(direction = ScrollDirection.Right, endSelector = readMemo("Last button"))
                }.expectation {
                    it.canSelect(readMemo("1st button")).thisIsFalse()
                    it.canSelect("More").thisIsTrue()
                    it.selectInScanResults(readMemo("1st button")).isFound.thisIsTrue()
                    it.selectInScanResults(readMemo("Last button")).isFound.thisIsTrue()
                }
            }
            case(2, "left, endSelector") {
                condition {
                    TestElementCache.scanResults.clear()
                    it.select("#below_search_omnibox_container:descendant(.android.widget.Button&&[-1])")
                        .text.memoTextAs("Last button")
                    it.canSelect(readMemo("Last button")).thisIsTrue()
                }.action {
                    it.select("#below_search_omnibox_container")
                        .scanElements(direction = ScrollDirection.Left, endSelector = readMemo("1st button"))
                }.expectation {
                    it.canSelect(readMemo("1st button")).thisIsTrue()
                    it.canSelect(readMemo("Last button")).thisIsFalse()
                    it.selectInScanResults(readMemo("1st button")).isFound.thisIsTrue()
                    it.selectInScanResults(readMemo("Last button")).isFound.thisIsTrue()
                }
            }
            case(3, "right, imageCompare") {
                condition {
                    TestElementCache.scanResults.clear()
                }.action {
                    it.select("#below_search_omnibox_container")
                        .scanElements(direction = ScrollDirection.Right)
                }.expectation {
                    it.canSelect(readMemo("1st button")).thisIsFalse()
                    it.canSelect(readMemo("Last button")).thisIsTrue()
                    it.selectInScanResults(readMemo("1st button")).isFound.thisIsTrue()
                    it.selectInScanResults(readMemo("Last button")).isFound.thisIsTrue()
                }
            }
            case(4, "left, imageCompare") {
                condition {
                    TestElementCache.scanResults.clear()
                }.action {
                    it.select("#below_search_omnibox_container")
                        .scanElements(direction = ScrollDirection.Left)
                }.expectation {
                    it.canSelect(readMemo("1st button")).thisIsTrue()
                    it.canSelect(readMemo("Last button")).thisIsFalse()
                    it.selectInScanResults(readMemo("1st button")).isFound.thisIsTrue()
                    it.selectInScanResults(readMemo("Last button")).isFound.thisIsTrue()
                }
            }
            case(5, "right, default") {
                condition {
                    TestElementCache.scanResults.clear()
                }.action {
                    it.select("#below_search_omnibox_container")
                        .scanElements(direction = ScrollDirection.Right)
                }.expectation {
                    it.canSelect(readMemo("1st button")).thisIsFalse()
                    it.canSelect(readMemo("Last button")).thisIsTrue()
                    it.selectInScanResults(readMemo("1st button")).isFound.thisIsTrue()
                    it.selectInScanResults(readMemo("Last button")).isFound.thisIsTrue()
                }
            }
            case(6, "left, default") {
                condition {
                    TestElementCache.scanResults.clear()
                }.action {
                    it.select("#below_search_omnibox_container")
                        .scanElements(direction = ScrollDirection.Left)
                }.expectation {
                    it.canSelect(readMemo("1st button")).thisIsTrue()
                    it.canSelect(readMemo("Last button")).thisIsFalse()
                    it.selectInScanResults(readMemo("1st button")).isFound.thisIsTrue()
                    it.selectInScanResults(readMemo("Last button")).isFound.thisIsTrue()
                }
            }
        }
    }

}