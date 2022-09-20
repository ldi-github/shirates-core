package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.onScreen
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.screenIs
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class OnScreen1 : UITest() {

    @Test
    @Order(10)
    fun onScreen1() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    onScreen("[Android Settings Top Screen]") {
                        it.screenIs("[Android Settings Top Screen]")
                    }
                    onScreen("[System Screen]") {
                        it.screenIs("[System Screen]")
                    }
                }.expectation {
                    onScreen("[Android Settings Top Screen]") {
                        it.screenIs("[Android Settings Top Screen]")
                    }
                    onScreen("[System Screen]") {
                        it.screenIs("[System Screen]")
                    }
                }
            }
        }
    }

}