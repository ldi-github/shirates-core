package tutorial.inaction

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.tap
import shirates.core.driver.commandextension.tapWithScrollDown
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Debugging1 : UITest() {

    @Test
    @Order(10)
    fun missingSelector_ERROR() {

        scenario {
            case(1) {
                action {
                    it.tap("Setting")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun watchingSourceXml() {

        scenario {
            case(1) {
                action {
                    it.tapWithScrollDown("Accessibility")
                        .tapWithScrollDown("Accessibility shortcuts")
                        .tap("Accessibility button")
                }
            }
        }
    }
}