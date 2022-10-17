package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.ifCanSelect
import shirates.core.driver.branchextension.ifCanSelectNot
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.scrollToBottom
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class IfCanSelect1 : UITest() {

    @Test
    @Order(10)
    fun ifCanSelectTest() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    ifCanSelect("[Network & internet]") {
                        OK("ifCanSelect called")
                    }.ifElse {
                        NG()
                    }

                    ifCanSelectNot("[System]") {
                        OK("ifCanSelectNot called")
                    }.ifElse {
                        NG()
                    }
                }
            }
            case(2) {
                action {
                    it.scrollToBottom()
                }.expectation {
                    ifCanSelect("[Network & internet]") {
                        NG()
                    }.ifElse {
                        OK("ifElse called")
                    }

                    ifCanSelectNot("[System]") {
                        NG()
                    }.ifElse {
                        OK("ifElse called")
                    }
                }
            }
        }
    }

}