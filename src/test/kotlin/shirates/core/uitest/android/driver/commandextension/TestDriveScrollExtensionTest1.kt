package shirates.core.uitest.android.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.rootElement
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveScrollExtensionTest1 : UITest() {

    @Order(10)
    @Test
    fun getScrollableElementsInDescendants() {

        scenario {
            case(1) {
                condition {
                    it.terminateApp("[Play Store]")
                        .macro("[Play Store Screen]")
                }.expectation {
                    val scrollableElements = rootElement.getScrollableElementsInDescendantsAndSelf()
                    (scrollableElements.isNotEmpty()).thisIsTrue()
                    for (e in scrollableElements) {
                        e.isScrollableElement.thisIsTrue()
                    }
                }
            }
        }
    }

    @Order(20)
    @Test
    fun getScrollableTarget() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Play Store Screen]")
                }.expectation {
                    val scrollableElements = rootElement.getScrollableElementsInDescendantsAndSelf()
                    val scrollableElement = scrollableElements.first()
                    val target1 = scrollableElement.getScrollableElement()
                    (target1 == scrollableElement).thisIsTrue()
                }
            }
            case(2) {
                expectation {
                    val scrollableElements = rootElement.getScrollableElementsInDescendantsAndSelf()
                    val largestScrollableTarget = scrollableElements.maxByOrNull { it.bounds.area }
                    val nonScrollableElement = rootElement
                    val target2 = nonScrollableElement.getScrollableElement()
                    (target2.toString() != nonScrollableElement.toString()).thisIsTrue()
                    (target2.toString() == largestScrollableTarget.toString()).thisIsTrue()
                }
            }
        }

    }

}
