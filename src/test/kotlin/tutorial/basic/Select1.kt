package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Select1 : UITest() {

    @Test
    @Order(10)
    fun select() {

        scenario {
            case(1) {
                action {
                    it.select("Settings")
                    output(it)
                }
            }
            case(2) {
                action {
                    it.selectWithScrollDown("System")
                    output(it)
                }
            }
            case(3) {
                action {
                    it.selectWithScrollUp("Settings")
                    output(it)
                }
            }
        }
    }

    @Test
    @Order(20)
    fun selectInScanElements() {

        scenario {
            case(1) {
                action {
                    it.scanElements()
                        .selectInScanResults("Settings")
                        .selectInScanResults("Accessibility")
                        .selectInScanResults("System")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun selectWithScrollAndSwipeToCenter() {

        scenario {
            case(1) {
                action {
                    it.selectWithScrollDown("Security & privacy", log = false)
                    output(it)
                }
            }
            case(2) {
                action {
                    it.selectWithScrollUp("Notifications")
                    output(it)
                }
            }
        }
    }

}