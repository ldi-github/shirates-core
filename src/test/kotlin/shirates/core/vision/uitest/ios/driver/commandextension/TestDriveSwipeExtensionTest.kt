package shirates.core.vision.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.select
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.driver.rootBounds
import shirates.core.driver.testContext
import shirates.core.driver.testDrive
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveSwipeExtensionTest : VisionTest() {

    @Order(10)
    @Test
    fun swipePointToPoint() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    v1 = it.detect("Camera")
                    v2 = it.detect("General")
                    output("from=${v1.selector}, target=${v2.selector}")
                    it.swipePointToPoint(
                        startX = v1.bounds.centerX,
                        startY = v1.bounds.centerY,
                        endX = v2.bounds.centerX,
                        endY = v2.bounds.centerY,
                        durationSeconds = 10.0
                    )
                }.expectation {
                    val current = it.detect("Camera")
                    val diff = current.bounds.centerY - v2.bounds.centerY
                    output("diff=$diff")
                    val result = Math.abs(diff) != 0
                    result.thisIsTrue("Math.abs(diff) != 0. diff=$diff")
                }
            }
            case(2) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                    testContext.appiumProxyReadTimeoutSeconds = 30.0
                }.action {
                    v1 = it.detect("Camera")
                    v2 = it.detect("General")
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
                    val current = it.detect("Camera")
                    val diff = current.bounds.centerY - v2.bounds.centerY
                    output("diff=$diff")
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
                    it.macro("[iOS Settings Top Screen]")
                    v1 = it.detect("Camera")
                    v2 = it.detect("General")
                }.action {
                    it.swipeElementToElement(
                        startElement = v1,
                        endElement = v2,
                        durationSeconds = 2.0,
                        adjust = true
                    )
                }.expectation {
                    val current = it.detect("Camera")
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
                    it.macro("[iOS Settings Top Screen]")
                    v1 = it.detect("Camera")
                    v2 = it.detect("General")
                }.action {
                    it.swipeElementToElementAdjust(startElement = v1, endElement = v2, durationSeconds = 2.0)
                }.expectation {
                    val current = it.detect("Camera")
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
                    it.macro("[iOS Settings Top Screen]")
                    v1 = it.detect("Camera")
                    v2 = v1
                }.action {
                    it.swipeElementToElement(startElement = v1, endElement = v2, durationSeconds = 2.0, adjust = true)
                }.expectation {
                    v3 = it.detect("Camera")
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
                    it.screenIs("[iOS Settings Top Screen]")
                }.action {
                    v1 = it.detect("Camera")
                    v2 = it.detect("General")
                    v1.swipeVerticalTo(endY = v2.bounds.centerY, durationSeconds = 5.0)

                }.expectation {
                    v3 = it.detect("Camera")
                    val abs = Math.abs(v3.bounds.centerY - v2.bounds.centerY)
                    (abs < v3.bounds.height).thisIsTrue("|${v3.bounds.centerY} - ${v2.bounds.centerY}| < ${v3.bounds.height}")
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
                    it.screenIs("[iOS Settings Top Screen]")
                }.action {
                    it.detect("Screen Time")
                        .swipeToCenterOfScreen()
                }.expectation {
                    val b = it.bounds
                    val low = b.centerY - b.height
                    val high = b.centerY + b.height
                    (low <= rootBounds.centerY).thisIsTrue("$low <= ${rootBounds.centerY}")
                    (rootBounds.centerY <= high).thisIsTrue("${rootBounds.centerY} <= $high")
                }
            }
            case(2) {
                condition {
                    it.flickAndGoUp()
                }
                action {
                    it.detect("Action Button")
                        .swipeToTopOfScreen()
                        .swipeToTopOfScreen(durationSeconds = 2.0)
                }.expectation {
                    val v = it.detect("Action Button")
                    val actionBar = testDrive.select(".XCUIElementTypeNavigationBar", useCache = false)
                    val b = v.bounds
                    val low = actionBar.bounds.bottom + 1
                    val high = low + b.height + 10
                    (low <= b.centerY).thisIsTrue("$low <= ${b.centerY}")
                    (b.centerY <= high).thisIsTrue("${b.centerY} <= $high")
                }
            }
            case(3) {
                condition {
                    it.exist("Game Center")
                }.action {
                    it.detect("Game Center")
                        .swipeToBottomOfScreen(durationSeconds = 3.0)
                }.expectation {
                }
            }
        }
    }

}