package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.viewBounds
import shirates.core.logging.printInfo
import shirates.core.testcode.UITest

@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class SwipeIos1 : UITest() {

    @Test
    @Order(10)
    fun swipeTo_swipeToAdjust() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.select("[Passwords]")
                        .swipeTo("[General]")
                }.expectation {
                }
            }
            case(2) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.select("[Passwords]")
                        .swipeToAdjust("[General]")
                }.expectation {
                }
            }
        }
    }

    @Test
    @Order(20)
    fun swipeToCenter_swipeToTop_swipeToBottom() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.select("[Safari]")
                        .swipeToCenterOfScreen()
                }.expectation {
                }
            }
            case(2) {
                action {
                    it.select("General")
                        .swipeToTopOfScreen(durationSeconds = 3.0)
                }.expectation {
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

    @Test
    @Order(30)
    fun swipePointToPoint() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.swipePointToPoint(
                        startX = viewBounds.centerX,
                        startY = viewBounds.centerY,
                        endX = viewBounds.centerX,
                        endY = viewBounds.top
                    )
                }.expectation {

                }
            }

            case(2) {
                action {
                    it.swipePointToPoint(
                        startX = viewBounds.centerX,
                        startY = viewBounds.centerY,
                        endX = viewBounds.centerX,
                        endY = viewBounds.bottom,
                        durationSeconds = 0.2
                    )
                }.expectation {

                }
            }
        }
    }

    @Test
    @Order(40)
    fun swipeCenterToTop_swipeCenterToBottom() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.swipeCenterToTop()
                }.expectation {

                }
            }

            case(2) {
                action {
                    it.swipeCenterToBottom()
                }.expectation {

                }
            }
        }

    }

    @Test
    @Order(50)
    fun swipeLeftToRight_swipeRightToLeft() {

        scenario {
            case(1) {
                condition {
                    it
                        .pressHome()
                        .pressHome()
                }.action {
                    it.swipeLeftToRight()
                }.expectation {

                }
            }

            case(2) {
                action {
                    it.swipeRightToLeft()
                }.expectation {

                }
            }
        }
    }

    @Test
    @Order(60)
    fun flickLeftToRight_flickRightToLeft() {

        scenario {
            case(1) {
                condition {
                    it
                        .pressHome()
                        .pressHome()
                }.action {
                    it.flickLeftToRight()
                }.expectation {

                }
            }

            case(2) {
                action {
                    it.flickRightToLeft()
                }.expectation {

                }
            }

        }

    }

    @Test
    @Order(70)
    fun swipeBottomToTop_swipeTopToBottom() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.swipeBottomToTop()
                }.expectation {

                }
            }

            case(2) {
                action {
                    it.swipeTopToBottom()
                }.expectation {

                }
            }
        }

    }

    @Test
    @Order(80)
    fun flickBottomToTop_flickTopToBottom() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.flickBottomToTop()
                }.expectation {

                }
            }

            case(2) {
                action {
                    it.flickTopToBottom()
                }.expectation {

                }
            }
        }

    }

    @Test
    @Order(90)
    fun swipeVerticalTo() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.selectWithScrollDown("[Safari]")
                        .swipeVerticalTo(150)
                }.expectation {
                    it.bounds.printInfo()
                }
            }

            case(2) {
                action {
                    it.swipeVerticalTo(2000)
                }.expectation {
                }
            }
        }
    }

    @Test
    @Order(100)
    fun swipeHorizontalTo() {

        scenario {
            case(1) {
                condition {
                    it.pressHome()
                        .pressHome()
                        .screenIs("[iOS Home Screen]")
                }.action {
                    it.select("Settings||News")
                        .swipeHorizontalTo(0)
                }.expectation {
                }
            }

            case(2) {
                action {
                    it.select("Watch")
                        .swipeHorizontalTo(viewBounds.right)
                }.expectation {
                }
            }

        }
    }

    @Test
    @Order(110)
    fun swipeToTop_swipeToBottom() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.select("[Safari]")
                        .swipeToTop()
                }.expectation {
                }
            }

            case(2) {
                action {
                    it.select("[Photos]")
                        .swipeToBottom()
                }.expectation {
                }
            }
        }
    }

    @Test
    @Order(120)
    fun flickToTop_flickToBottom() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.select("[Safari]")
                        .flickToTop()
                }.expectation {
                }
            }

            case(2) {
                action {
                    it.select("[Photos]")
                        .flickToBottom()
                }.expectation {
                }
            }
        }
    }
}