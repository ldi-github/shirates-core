package shirates.core.uitest.android.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveScrollExtensionTest2 : UITest() {

    @Order(40)
    @Test
    fun scrollDown_scrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .exist("Network & internet")
                        .dontExist("Security")
                }.action {
                    it.scrollDown()
                    it.scrollDown()
                }.expectation {
                    it.dontExist("Network & internet")
                    it.exist("Security")
                }
            }
            case(2) {
                action {
                    it.scrollUp()
                }.expectation {
                    it.exist("Network & internet")
                        .dontExist("Security")
                }
            }
            case(3) {
                action {
                    it.scrollDown(durationSeconds = 8.0)
                }.expectation {
                    it.dontExist("Network & internet")
                    it.exist("Security")
                }
            }
            case(4) {
                action {
                    it.scrollUp(durationSeconds = 8.0)
                }.expectation {
                    it.exist("Network & internet")
                        .dontExist("Security")
                }
            }
        }
    }

    @Order(50)
    @Test
    fun scrollToBottom_scrollToTop() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .exist("[Account Avatar]")
                        .exist("[Settings]")
                }.action {
                    it.scrollToBottom()
                }.expectation {
                    it.exist("[Tips & support]")
                }
            }
            case(2) {
                action {
                    it.scrollToTop()
                }.expectation {
                    it.exist("[Account Avatar]")
                        .exist("[Settings]")
                }
            }
        }
    }

    @Order(60)
    @Test
    fun scrollToRightEdge_scrollToLeftEdge() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.action {
                    it.select("#below_search_omnibox_container")
                        .scrollToRightEdge()
                }.expectation {
                    it.exist("More")
                }
            }
            case(2) {
                action {
                    it.select("#below_search_omnibox_container")
                        .scrollToLeftEdge()
                }.expectation {
                    it.dontExist("More")
                }
            }
        }
    }

}