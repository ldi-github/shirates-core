package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriverEventContext
import shirates.core.driver.branchextension.ifCanSelect
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.tap
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class EventHandlerTest : UITest() {

    override fun setEventHandlers(context: TestDriverEventContext) {

        context.irregularHandler = {

            println("onBeforeSelect")

            ifCanSelect("Airplane mode") {
                println("Airplane mode found")
            }.ifElse {
                println("Airplane mode not found")
            }
        }
    }

    @Test
    fun onBeforeSelect() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    it.tap("Network & internet")
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
        }
    }
}