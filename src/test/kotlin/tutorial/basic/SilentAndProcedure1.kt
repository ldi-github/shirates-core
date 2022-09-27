package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class SilentAndProcedure1 : UITest() {

    @Test
    @Order(10)
    fun silent1() {

        scenario {
            case(1) {
                condition {
                    macro("[Android Settings Top Screen]")
                }.action {
                    describe("Tap [System]")
                    silent {
                        it.scrollToBottom()
                            .tap("[System]")
                    }
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun procedure1() {

        scenario {
            case(1) {
                condition {
                    macro("[Android Settings Top Screen]")
                }.action {
                    procedure("Tap [System]") {
                        it.scrollToBottom()
                            .tap("[System]")
                    }
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
        }
    }

}