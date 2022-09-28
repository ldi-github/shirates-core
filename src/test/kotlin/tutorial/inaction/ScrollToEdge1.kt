package tutorial.inaction

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.exist
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.scrollToBottom
import shirates.core.driver.commandextension.scrollToTop
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class ScrollToEdge1 : UITest() {

    @Order(10)
    @Test
    fun scrollToBottom_scrollToTop() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.scrollToBottom()
                }.expectation {
                    it.exist("{Tips & support}")
                }
            }
            case(2) {
                action {
                    it.scrollToTop()
                }.expectation {
                    it.exist("[Network & internet]")
                }
            }
        }
    }

    @Order(20)
    @Test
    fun scrollToBottom_scrollToTop_with_edge_selector() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.scrollToBottom(edgeSelector = "{Tips & support}")
                }.expectation {
                    it.exist("{Tips & support}")
                }
            }
            case(2) {
                action {
                    it.scrollToTop(edgeSelector = "[Network & internet]")
                }.expectation {
                    it.exist("[Network & internet]")
                }
            }
        }
    }
}