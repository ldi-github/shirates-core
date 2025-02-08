package shirates.core.vision.uitest.android.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.driver.syncScreen
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class VisionDriveSwipeExtensionTest : VisionTest() {

    @Order(10)
    @Test
    fun swipePointToPoint() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    v1 = it.detect("Notifications")
                    v2 = it.detect("Network & internet")
                    output("from=${v1.selector}, target=${v2.selector}")
                    it.swipePointToPoint(
                        startX = v1.bounds.centerX,
                        startY = v1.bounds.centerY,
                        endX = v2.bounds.centerX,
                        endY = v2.bounds.centerY,
                        durationSeconds = 10.0
                    )
                }.expectation {
                    val current = it.detect("Notifications")
                    val diff = current.bounds.centerY - v2.bounds.centerY
                    output("diff=$diff")
                    val result = Math.abs(diff) != 0
                    result.thisIsTrue("Math.abs(diff) != 0. diff=$diff")
                }
            }
            case(2) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    v1 = it.detect("Notifications")
                    v2 = it.detect("Network & internet")
                    output("from=${v1.selector}, target=${v2.selector}")
                    it.swipePointToPoint(
                        startX = v1.bounds.centerX,
                        startY = v1.bounds.centerY,
                        endX = v2.bounds.centerX,
                        endY = v2.bounds.centerY,
                        withOffset = true,
                        durationSeconds = 10.0
                    )
                }.expectation {
                    syncScreen()
                    val current = it.detect("Notifications")
                    val diff = current.bounds.centerY - v2.bounds.centerY
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
                    v1 = it.detect("Notifications")
                    v2 = it.detect("Network & internet")
                }.action {
                    it.swipeElementToElement(
                        startElement = v1,
                        endElement = v2,
                        durationSeconds = 2.0,
                        adjust = true,
                    )
                }.expectation {
                    val current = it.detect("Notifications")
                    val diff = current.bounds.centerY - v2.bounds.centerY
                    output("diff=$diff")
                    current.bounds.isOverlapping(v2.bounds)
                        .thisIsTrue("${current.bounds} is overlapping ${v2.bounds}")
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
                    v1 = it.detect("Notifications")
                    v2 = it.detect("Network & internet")
                }.action {
                    it.swipeElementToElementAdjust(startElement = v1, endElement = v2, durationSeconds = 2.0)
                }.expectation {
                    val current = it.detect("Notifications")
                    val diff = current.bounds.centerY - v2.bounds.centerY
                    output("diff=$diff")
                    current.bounds.isOverlapping(v2.bounds)
                        .thisIsTrue("${current.bounds} is overlapping ${v2.bounds}")
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
                    v1 = it.detect("Notifications")
                    v2 = v1
                }.action {
                    it.swipeElementToElement(startElement = v1, endElement = v2, durationSeconds = 2.0, adjust = true)
                }.expectation {
                    v3 = it.detect("Notifications")
                    (v3.bounds.centerX == v1.bounds.centerX && v3.bounds.centerY == v1.bounds.centerY)
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
                        .detect("Storage")
                        .swipeToCenterOfScreen()
                    v1 = it.detect("Storage")
                    v2 = it.detect("Display")
                }.action {
                    v1.swipeVerticalTo(endY = v2.bounds.centerY, durationSeconds = 5.0)
                }.expectation {
                    v3 = it.detect("Storage")
                    val abs = Math.abs(v3.bounds.centerY - v2.bounds.centerY)
                    (abs < v3.bounds.height).thisIsTrue("${abs} < ${v3.bounds.height}")
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
                    it.detect("Battery")
                        .swipeToCenterOfScreen()
                }.expectation {
                    val v = it.detect("Battery")
                    val b = v.bounds
                    val low = b.top
                    val high = b.bottom
                    val rootBounds = rootElement.bounds
                    (low <= rootBounds.centerY).thisIsTrue("$low <= ${rootBounds.centerY}")
                    (rootBounds.centerY <= high).thisIsTrue("viewBounds.centerY:${rootBounds.centerY} <= [Battery].bottom:$high")
                }
            }
            case(2) {
                condition {
                    it.detectWithScrollDown("Display")
                        .swipeToCenterOfScreen()
                }.action {
                    val v = it.detect("Display")
                    v.swipeToTopOfScreen(durationSeconds = 3.0)
                }.expectation {
                    val v = detect("Display", throwsException = false)
                    v.isFound.thisIsTrue("Display is not shown. (Under `Search settings`)")
                }
            }
            case(3) {
                action {
                    it.detect("Passwords & accounts")
                        .swipeToBottomOfScreen(durationSeconds = 5.0)
                }.expectation {
                    val v = detect("Passwords & accounts")
                    val b = v.bounds
                    val rootBounds = rootElement.bounds
                    val low = rootBounds.bottom * 0.9
                    val high = rootBounds.bottom
                    (low <= b.centerY).thisIsTrue("low:$low <= centerY:${b.centerY}")
                    (b.centerY <= high).thisIsTrue("centerY:${b.centerY} <= high:$high")
                }
            }
        }
    }

}