package shirates.core.uitest.android.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveSwipeExtensionTest1 : UITest() {

    @Order(10)
    @Test
    fun swipePointToPoint() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    e1 = it.select("Notifications")
                    e2 = it.select("Network & internet")
                    output("from=${e1.selector}, target=${e2.selector}")
                    it.swipePointToPoint(
                        startX = e1.bounds.centerX,
                        startY = e1.bounds.centerY,
                        endX = e2.bounds.centerX,
                        endY = e2.bounds.centerY,
                        durationSeconds = 10
                    )
                }.expectation {
                    val current = it.select("Notifications")
                    val diff = current.bounds.centerY - e2.bounds.centerY
                    output("diff=$diff")
                    val result = Math.abs(diff) < 10
                    result.thisIsTrue("Math.abs(diff) < 10")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun swipeElementToElement() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                    e1 = it.select("Notifications")
                    e2 = it.select("Network & internet")
                }.action {
                    it.swipeElementToElement(startElement = e1, endElement = e2, durationSeconds = 2.0)
                }.expectation {
                    it.bounds.isOverlapping(e2.bounds)
                        .thisIsTrue("${it.bounds} is overlapping ${e2.bounds}")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun swipeTo_swipeVerticalTo() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    e1 = it.select("Notifications")
                    e2 = it.select("Network & internet")
                    e3 = e1.swipeTo("Network & internet", durationSeconds = 2.0)
                }.expectation {
                    println(e1.bounds)
                    println(e2.bounds)
                    println(e3.bounds)
                    e3.bounds.isOverlapping(e2.bounds)
                        .thisIsTrue("${e3.bounds} is overlapping ${e2.bounds}")
                }
            }
            case(2) {
                action {
                    e1 = it.select("Storage")
                    e2 = it.select("Display")
                    e1.swipeVerticalTo(endY = e2.bounds.top, durationSeconds = 5.0)

                }.expectation {
                    e3 = it.select("Storage")
                    val abs = Math.abs(e3.bounds.centerY - e2.bounds.centerY)
                    (abs < e3.bounds.height).thisIsTrue("|${e3.bounds.centerY} - ${e2.bounds.centerY}| < ${abs}")
                }
            }
        }
    }


}