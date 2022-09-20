package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class iOSKeyboard1 : UITest() {

    @Test
    @Order(10)
    fun hideKeyboard() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Home Screen]")
                        .isKeyboardShown.thisIsFalse("Keyboard is not shown")
                }.action {
                    it.swipeCenterToBottom()
                }.expectation {
                    it.keyboardIsShown()
                }
            }
            case(2) {
                action {
                    it.hideKeyboard()
                }.expectation {
                    it.keyboardIsNotShown()
                }
            }
        }
    }

}