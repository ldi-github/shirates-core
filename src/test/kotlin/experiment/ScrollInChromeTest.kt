package experiment

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriver
import shirates.core.driver.branchextension.ifTrue
import shirates.core.driver.commandextension.*
import shirates.core.driver.descendants
import shirates.core.driver.wait
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/android/chrome/testrun.properties")
class ScrollInChromeTest : UITest() {

    @Test
    @Order(10)
    fun scrollInChrome() {

        scenario {
            case(1) {
                it.tap("id=com.android.chrome:id/send_report_checkbox")
                    .checkIsOFF()
                    .tap("id=com.android.chrome:id/terms_accept")

                it.canSelect("No Thanks")
                    .ifTrue {
                        it.tap()
                    }

                it.tap("id=com.android.chrome:id/search_box_text")
                    .sendKeys("https://ja.wikipedia.org/wiki/PlayStation_5")
                    .pressEnter()

                it.wait(2)

                for (aaa in TestDriver.androidDriver.contextHandles) {
                    println(aaa)
                }

                var scrollableTarget = it.getScrollableTarget()
                if (scrollableTarget.scrollable == "false") {
                    scrollableTarget = it.getScrollableTarget()
                }
                val descendants = scrollableTarget.descendants
                val firstTextElement = descendants.first { it.text.isBlank().not() }
                val lastTextElement = descendants.last { it.text.isBlank().not() }

                it.select(lastTextElement.text)
                it.select(firstTextElement.text)


                it
                    .scrollDown()
                    .scrollDown()
            }

            case(2) {
                it
                    .scrollUp()
                    .scrollUp()
            }

            case(3) {
                it
                    .scrollToBottom()
            }

        }
    }
}