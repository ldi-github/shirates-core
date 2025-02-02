package shirates.core.uitest.ios.driver.misc

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriverEventContext
import shirates.core.driver.branchextension.ifScreenIs
import shirates.core.driver.commandextension.exist
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.tap
import shirates.core.testcode.UITest

@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class TestDriverTest_irregularHandler : UITest() {

    override fun setEventHandlers(context: TestDriverEventContext) {

        context.irregularHandler = {

            ifScreenIs("[Keyboards Screen]") {
                it.tap("One-Handed Keyboard")
            }
            ifScreenIs("[General Screen]") {
                it.tap("Keyboard")
            }
        }
    }

    @Test
    fun multipleHandlerCalls() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[iOS Settings Top Screen]")
                }.action {
                    it.tap("General")
                    /**
                     * irregularHandler fires on [General Screen] and [Keyboards Screen]
                     */
                }.expectation {
                    it.exist("Right")
                }
            }
        }
    }
}