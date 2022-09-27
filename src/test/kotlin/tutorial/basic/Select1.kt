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
                    it.select("Settings", log = true)
                    output(it)
                }
            }
            case(2) {
                action {
                    it.selectWithScrollDown("System", log = true)
                    output(it)
                }
            }
            case(3) {
                action {
                    it.selectWithScrollUp("Settings", log = true)
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
                        .selectInScanResults("Settings", log = true)
                        .selectInScanResults("Accessibility", log = true)
                        .selectInScanResults("System", log = true)
                }
            }
        }
    }

}