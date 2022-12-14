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
                    val scrollableElements = rootElement.getScrollableElementsInDescendants()
                    (scrollableElements.count() > 0).thisIsTrue()
                    for (e in scrollableElements) {
                        e.isScrollable.thisIsTrue()
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
                    val scrollableElements = rootElement.getScrollableElementsInDescendants()
                    val scrollableElement = scrollableElements.first()
                    val target1 = scrollableElement.getScrollableTarget()
                    (target1 == scrollableElement).thisIsTrue()
                }
            }
            case(2) {
                expectation {
                    val scrollableElements = rootElement.getScrollableElementsInDescendants()
                    val largestScrollableTarget = scrollableElements.maxByOrNull { it.bounds.area }
                    val nonScrollableElement = rootElement
                    val target2 = nonScrollableElement.getScrollableTarget()
                    (target2 != nonScrollableElement).thisIsTrue()
                    (target2 == largestScrollableTarget).thisIsTrue()
                }
            }
        }

    }

    @Order(30)
    @Test
    fun hasScrollable() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Calculator Main Screen]")
                }.expectation {
                    rootElement.hasScrollable.thisIsFalse()
                }
            }
            case(2) {
                condition {
                    it.macro("[Play Store Screen]")
                }.expectation {
                    rootElement.hasScrollable.thisIsTrue()
                }
            }
        }

    }


}
