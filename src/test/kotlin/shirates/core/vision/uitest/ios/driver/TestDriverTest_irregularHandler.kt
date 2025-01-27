package shirates.core.vision.uitest.ios.driver

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriverEventContext
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.logging.TestLog
import shirates.core.vision.driver.commandextension.ifScreenIs
import shirates.core.vision.driver.commandextension.screenIs
import shirates.core.vision.driver.commandextension.tap
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class TestDriverTest_irregularHandler : VisionTest() {

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
                     * irregularHandler DOES NOT fire on tap in vision mode
                     */
                }.expectation {
                    val hasMessage = TestLog.lines.takeLast(5)
                        .any { it.message == "irregularHandler is not supported on vision mode." }
                    hasMessage.thisIsTrue()
                }
            }
        }
    }
}