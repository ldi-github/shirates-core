package shirates.core.vision.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class VisionElementSwipeExtensionTest : VisionTest() {

    @Test
    fun swipeTo() {

        var from = VisionElement.emptyElement
        var target = VisionElement.emptyElement

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                    target = it.detect("General")
                }.action {
                    from = it.detect("StandBy")

                    from.swipeTo("General")

                }.expectation {
                    val current = it.detect("StandBy")
                    val diffY = target.bounds.top - from.bounds.top
                    val diffY2 = target.bounds.top - current.bounds.top
                    output("diffY2=${diffY2}, diffY=${diffY}")
                    val result = Math.abs(diffY2) < Math.abs(diffY)
                    result.thisIsTrue("abs(diffY2) < abs(diffY)")
                }
            }
            case(2) {
                action {
                    from = it.detect("Camera")
                    target = it.detect("StandBy")
                    it.swipeElementToElement(startElement = from, endElement = target)
                }.expectation {
                    val current = it.detect("Camera")
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