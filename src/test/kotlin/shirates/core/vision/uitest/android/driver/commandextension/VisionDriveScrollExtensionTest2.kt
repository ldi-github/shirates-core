package shirates.core.vision.uitest.android.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class VisionDriveScrollExtensionTest2 : VisionTest() {

    @Order(10)
    @Test
    fun scrollDown_scrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .scrollToTop()
                        .dontExist("Security & privacy")
                }.action {
                    it.scrollDown()
                }.expectation {
                    it.dontExist("Network & internet")
                    it.exist("Security & privacy")
                }
            }
            case(2) {
                action {
                    it.scrollUp()
                }.expectation {
                    it.exist("Network & internet")
                        .dontExist("Security & privacy")
                }
            }
            case(3) {
                action {
                    it.scrollDown(scrollDurationSeconds = 4.0)
                }.expectation {
                    it.dontExist("Network & internet")
                    it.exist("Security & privacy")
                }
            }
            case(4) {
                action {
                    it.scrollUp(scrollDurationSeconds = 4.0)
                }.expectation {
                    it.exist("Network & internet")
                        .dontExist("Security & privacy")
                }
            }
        }
    }

    @Order(20)
    @Test
    fun scrollToBottom_scrollToTop() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .exist("Settings")
                }.action {
                    it.scrollToBottom()
                }.expectation {
                    it.exist("Tips & support")
                }
            }
            case(2) {
                action {
                    it.scrollToTop()
                }.expectation {
                    it.exist("Settings")
                }
            }
        }
    }

    @Order(30)
    @Test
    fun scrollToRightEdge_scrollToLeftEdge() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.action {
                    it.onLineOf("Restaurants") {
                        it.scrollToRightEdge()
                    }
                }.expectation {
                    it.exist("More")
                }
            }
            case(2) {
                action {
                    it.onLineOf("More") {
                        it.scrollToLeftEdge()
                    }
                }.expectation {
                    it.dontExist("More")
                }
            }
        }
    }

}