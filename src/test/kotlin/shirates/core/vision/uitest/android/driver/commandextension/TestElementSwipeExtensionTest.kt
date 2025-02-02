package shirates.core.vision.uitest.android.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestElementSwipeExtensionTest : VisionTest() {

    @Test
    fun swipeTo() {

        var from = VisionElement.emptyElement
        var target = VisionElement.emptyElement

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                    target = it.detect("Network & internet")
                }.action {
                    from = it.detect("Battery")
                    from.swipeTo("Network & internet")
                }.expectation {
                    val current = it.detect("Battery")
                    val diffY = target.bounds.top - from.bounds.top
                    val diffY2 = target.bounds.top - current.bounds.top
                    output("diffY2=${diffY2}, diffY=${diffY}")
                    val result = Math.abs(diffY2) < Math.abs(diffY)
                    result.thisIsTrue("abs(diffY2) < abs(diffY)")
                }
            }
            case(2) {
                action {
                    from = it.detect("Battery")
                    target = it.detect("Apps")
                    it.swipeElementToElement(startElement = from, endElement = target)
                }.expectation {
                    val current = it.detect("Battery")
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