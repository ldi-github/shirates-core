package tutorial.basic

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.tap
import shirates.core.driver.commandextension.tapWithScrollDown
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Tap1 : UITest() {

    @Test
    fun tap() {

        scenario {
            case(1) {
                action {
                    it.tap("Network & internet")
                        .tap("Internet")
                    it.tap("@Navigate up")
                        .tap("@Navigate up")
                }
            }
            case(2) {
                action {
                    it.tapWithScrollDown("Display")
                        .tapWithScrollDown("Colors")
                    it.tap("@Navigate up")
                        .tap("@Navigate up")
                }
            }
        }
    }

}