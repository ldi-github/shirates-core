package shirates.core.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestElement
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestElementSwipeExtensionTest : UITest() {

    @Test
    fun swipeTo() {

        var from = TestElement()
        var target = TestElement()

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                    target = it.select("General")
                }.action {
                    from = it.select("StandBy")

                    from.swipeTo("General")

                }.expectation {
                    val current = it.select("StandBy")
                    val diffY = target.bounds.top - from.bounds.top
                    val diffY2 = target.bounds.top - current.bounds.top
                    output("diffY2=${diffY2}, diffY=${diffY}")
                    val result = Math.abs(diffY2) < Math.abs(diffY)
                    result.thisIsTrue("abs(diffY2) < abs(diffY)")
                }
            }
            case(2) {
                action {
                    from = it.select("Camera")
                    target = it.select("StandBy")
                    it.swipeElementToElement(startElement = from, endElement = target)
                }.expectation {
                    val current = it.select("Camera")
                    val diffY = target.bounds.top - from.bounds.top
                    val diffY2 = target.bounds.top - current.bounds.top
                    output("diffY2=${diffY2}, diffY=${diffY}")
                    val result = Math.abs(diffY2) < Math.abs(diffY)
                    result.thisIsTrue("abs(diffY2) < abs(diffY)")
                }
            }

        }
    }
}