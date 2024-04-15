package tutorial.advanced

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.*
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class CreatingCommandFunction1 : UITest() {

    @Test
    @Order(10)
    fun scrollToTopAndTapWithScrollDown() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.scrollToTop()
                        .tapWithScrollDown("[Accessibility]")
                }.expectation {
                    it.screenIs("[Accessibility Screen]")
                }
            }
        }
    }

    private fun TestDrive.tapWithScrollDownFromTop(
        expression: String,
        scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
        scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
        scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
        scrollMaxCount: Int = testContext.scrollMaxCount,
        holdSeconds: Double = testContext.tapHoldSeconds,
        tapMethod: TapMethod = TapMethod.auto
    ): TestElement {

        val testElement = getThisOrIt()

        val command = "tapWithScrollDownFromTop"
        val sel = getSelector(expression = expression)
        val message = "Scroll to top and tap $sel with scrolling down"
        val context = TestDriverCommandContext(testElement)
        context.execOperateCommand(command = command, message = message) {
            scrollToTop()
            tapWithScrollDown(
                expression = expression,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
                holdSeconds = holdSeconds,
                tapMethod = tapMethod
            )
        }

        return lastElement
    }

    @Test
    @Order(20)
    fun tapWithScrollDownFromTop() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .flickAndGoDown()
                }.action {
                    it.tapWithScrollDownFromTop("[Accessibility]")
                }.expectation {
                    it.screenIs("[Accessibility Screen]")
                }
            }
        }
    }

}