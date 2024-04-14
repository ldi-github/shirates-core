package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.viewBounds
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Swipe1 : UITest() {

    @Test
    @Order(10)
    fun swipeTo_swipeToAdjust() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.select("[Battery]")
                        .swipeTo("[Network & internet]")
                }.expectation {
                }
            }
            case(2) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.select("[Battery]")
                        .swipeToAdjust("[Network & internet]")
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
                    it.macro("[Android Settings Top Screen]")
                        .exist("[Notifications]")
                        .exist("[Battery]")
                }.action {
                    it.select("[Battery]")
                        .swipeToCenterOfScreen()
                        .swipeToTopOfScreen(durationSeconds = 10.0)
                }.expectation {
                    it.dontExist("[Notifications]")
                        .exist("[Storage]")
                }
            }
            case(2) {
                condition {
                    it.exist("[Security & privacy]")
                        .exist("[Location]")
                }.action {
                    it.select("[Security & privacy]")
                        .swipeToBottomOfScreen(durationSeconds = 10.0)
                }.expectation {
                    it.exist("[Security & privacy]")
                        .dontExist("[Location]")
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
                    it.macro("[Android Settings Top Screen]")
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
                    it.macro("[Android Settings Top Screen]")
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
                    it.macro("[Android Settings Top Screen]")
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
                    it.macro("[Android Settings Top Screen]")
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
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.selectWithScrollDown("[Battery]")
                        .swipeVerticalTo(300)
                }.expectation {
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
                        .screenIs("[Pixel Home Screen]")
                }.action {
                    it.select("Chrome")
                        .swipeHorizontalTo(0)
                }.expectation {
                }
            }

            case(2) {
                action {
                    it.select("Messages")
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
                    it.pressHome()
                        .pressHome()
                        .screenIs("[Pixel Home Screen]")
                }.action {
                    it.select("@Search")
                        .swipeToTop()
                }.expectation {
                }
            }

            case(2) {
                action {
                    it.select("#input")
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
                    it.pressHome()
                        .pressHome()
                        .screenIs("[Pixel Home Screen]")
                }.action {
                    it.select("@Search")
                        .flickToTop()
                }.expectation {
                }
            }

            case(2) {
                action {
                    it.select("#input")
                        .flickToBottom()
                }.expectation {
                }
            }
        }
    }
}