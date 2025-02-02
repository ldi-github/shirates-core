package shirates.core.uitest.android.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriver
import shirates.core.driver.commandextension.*
import shirates.core.driver.viewBounds
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveSwipeExtensionTest : UITest() {

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
                    val result = Math.abs(diff) != 0
                    result.thisIsTrue("Math.abs(diff) != 0. diff=$diff")
                }
            }
            case(2) {
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
                        withOffset = true,
                        durationSeconds = 10
                    )
                }.expectation {
                    syncCache(force = true)
                    val current = it.select("Notifications")
                    val diff = current.bounds.centerY - e2.bounds.centerY
                    val result = Math.abs(diff) < 10
                    result.thisIsTrue("Math.abs(diff) < 10. diff=$diff")
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
                    it.swipeElementToElement(
                        startElement = e1,
                        endElement = e2,
                        durationSeconds = 2.0,
                        adjust = true,
                    )
                }.expectation {
                    val current = it.select("Notifications")
                    val diff = current.bounds.centerY - e2.bounds.centerY
                    output("diff=$diff")
                    current.bounds.isOverlapping(e2.bounds)
                        .thisIsTrue("${current.bounds} is overlapping ${e2.bounds}")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun swipeElementToElementAdjust() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                    e1 = it.select("Notifications")
                    e2 = it.select("Network & internet")
                }.action {
                    it.swipeElementToElementAdjust(startElement = e1, endElement = e2, durationSeconds = 2.0)
                }.expectation {
                    val current = it.select("Notifications")
                    val diff = current.bounds.centerY - e2.bounds.centerY
                    output("diff=$diff")
                    current.bounds.isOverlapping(e2.bounds)
                        .thisIsTrue("${current.bounds} is overlapping ${e2.bounds}")
                }
            }
        }
    }

    @Test
    @Order(35)
    fun swipeElementToSameElement() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                    e1 = it.select("Notifications")
                    e2 = e1
                }.action {
                    it.swipeElementToElement(startElement = e1, endElement = e2, durationSeconds = 2.0, adjust = true)
                }.expectation {
                    e3 = it.select("Notifications")
                    (e3.bounds.centerX == e1.bounds.centerX && e3.bounds.centerY == e1.bounds.centerY)
                        .thisIsTrue("Swipe on the same coordinates skipped")
                }
            }
        }
    }

    @Test
    @Order(40)
    fun swipeTo_swipeVerticalTo() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .select("Storage")
                        .swipeToCenterOfScreen()
                    e1 = it.select("Storage")
                    e2 = it.select("Display")
                }.action {
                    e1.swipeVerticalTo(endY = e2.bounds.top, durationSeconds = 5.0)
                }.expectation {
                    e3 = it.select("Storage")
                    val abs = Math.abs(e3.bounds.centerY - e2.bounds.centerY)
                    (abs < e3.bounds.height).thisIsTrue("${abs} < ${e3.bounds.height}")
                }
            }
        }
    }

    @Test
    @Order(50)
    fun swipeToCenter_swipeToTop_swipeToBottom() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.select("[Battery]")
                        .swipeToCenterOfScreen()
                }.expectation {
                    val b = it.bounds
                    val low = b.top
                    val high = b.bottom
                    (low <= viewBounds.centerY).thisIsTrue("$low <= ${viewBounds.centerY}")
                    (viewBounds.centerY <= high).thisIsTrue("viewBounds.centerY:${viewBounds.centerY} <= [Battery].bottom:$high")
                }
            }
            case(2) {
                condition {
                    it.selectWithScrollDown("[Display]")
                        .swipeToCenterOfScreen()
                }.action {
                    it.swipeToTopOfScreen(durationSeconds = 3.0)
                }.expectation {
                    val b = it.bounds
                    val headerBottom = TestDriver.screenInfo.scrollInfo.getHeaderBottom()
                    val low = headerBottom + 1
                    val high = low + b.height - 1
                    (low <= b.centerY).thisIsTrue("$low <= ${b.centerY}")
                    (b.centerY <= high).thisIsTrue("${b.centerY} <= $high")
                }
            }
            case(3) {
                action {
                    it.select("[Passwords & accounts]")
                        .swipeToBottomOfScreen(durationSeconds = 5.0)
                }.expectation {
                    val b = it.bounds
                    val low = viewBounds.bottom - b.height + 1
                    val high = viewBounds.bottom
                    (low <= b.centerY).thisIsTrue("low:$low <= centerY:${b.centerY}")
                    (b.centerY <= high).thisIsTrue("centerY:${b.centerY} <= high:$high")
                }
            }
        }
    }

}