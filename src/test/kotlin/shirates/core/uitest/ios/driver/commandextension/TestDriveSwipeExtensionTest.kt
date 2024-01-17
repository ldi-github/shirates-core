package shirates.core.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.testContext
import shirates.core.driver.viewBounds
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveSwipeExtensionTest : UITest() {

    @Order(10)
    @Test
    fun swipePointToPoint() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    e1 = it.select("Passwords")
                    e2 = it.select("General")
                    output("from=${e1.selector}, target=${e2.selector}")
                    it.swipePointToPoint(
                        startX = e1.bounds.centerX,
                        startY = e1.bounds.centerY,
                        endX = e2.bounds.centerX,
                        endY = e2.bounds.centerY,
                        durationSeconds = 10
                    )
                }.expectation {
                    val current = it.select("Passwords")
                    val diff = current.bounds.centerY - e2.bounds.centerY
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
                    e1 = it.select("Passwords")
                    e2 = it.select("General")
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
                    val current = it.select("Passwords")
                    val diff = current.bounds.centerY - e2.bounds.centerY
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
                    e1 = it.select("Passwords")
                    e2 = it.select("General")
                }.action {
                    it.swipeElementToElement(startElement = e1, endElement = e2, durationSeconds = 2.0, adjust = true)
                }.expectation {
                    it.bounds.isOverlapping(e2.bounds)
                        .thisIsTrue("${it.bounds} is overlapping ${e2.bounds}")
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
                    e1 = it.select("Passwords")
                    e2 = it.select("General")
                }.action {
                    it.swipeElementToElementAdjust(startElement = e1, endElement = e2, durationSeconds = 2.0)
                }.expectation {
                    it.bounds.isOverlapping(e2.bounds)
                        .thisIsTrue("${it.bounds} is overlapping ${e2.bounds}")
                }
            }
        }
    }

    @Test
    @Order(40)
    fun swipeTo_swipeVerticalTo() {

        scenario {
            case(1) {
                action {
                    e1 = it.select("Passwords")
                    e2 = it.select("General")
                    e1.swipeVerticalTo(endY = e2.bounds.top, durationSeconds = 5.0)

                }.expectation {
                    e3 = it.select("Passwords")
                    val abs = Math.abs(e3.bounds.centerY - e2.bounds.centerY)
                    (abs < e3.bounds.height).thisIsTrue("|${e3.bounds.centerY} - ${e2.bounds.centerY}| < ${abs}")
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
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.select("[Safari]")
                        .swipeToCenterOfScreen()
                }.expectation {
                    val b = it.bounds
                    val low = b.top
                    val high = b.bottom
                    (low <= viewBounds.centerY).thisIsTrue("$low <= ${viewBounds.centerY}")
                    (viewBounds.centerY <= high).thisIsTrue("${viewBounds.centerY} <= $high")
                }
            }
            case(2) {
                action {
                    it.select("General")
                        .swipeToTopOfScreen(durationSeconds = 3.0)
                }.expectation {
                    val b = it.bounds
                    val low = select(".XCUIElementTypeNavigationBar").bounds.bottom + 1
                    val high = low + b.height - 1
                    (low <= b.centerY).thisIsTrue("$low <= ${b.centerY}")
                    (b.centerY <= high).thisIsTrue("${b.centerY} <= $high")
                }
            }
            case(3) {
                condition {
                    it.exist("[Photos]")
                }.action {
                    it.select("[Photos]")
                        .swipeToBottomOfScreen(durationSeconds = 3.0)
                }.expectation {
                }
            }
        }
    }

}