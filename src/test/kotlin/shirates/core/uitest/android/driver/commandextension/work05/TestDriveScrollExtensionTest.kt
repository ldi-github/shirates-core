package shirates.core.uitest.android.driver.commandextension.work05

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.ScrollDirection
import shirates.core.driver.TestElementCache
import shirates.core.driver.branchextension.emulator
import shirates.core.driver.branchextension.realDevice
import shirates.core.driver.commandextension.*
import shirates.core.driver.descendants
import shirates.core.driver.rootElement
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveScrollExtensionTest : UITest() {

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

    @Order(40)
    @Test
    fun scrollDown_scrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .exist("Network & internet")
                        .dontExist("Security")
                }.action {
                    it.scrollDown()
                }.expectation {
                    it.dontExist("Network & internet")
                    it.exist("Security")
                }
            }
            case(2) {
                action {
                    it.scrollUp()
                }.expectation {
                    it.exist("Network & internet")
                        .dontExist("Security")
                }
            }
            case(3) {
                action {
                    it.scrollDown(durationSeconds = 8.0)
                }.expectation {
                    it.dontExist("Network & internet")
                    it.exist("Security")
                }
            }
            case(4) {
                action {
                    it.scrollUp(durationSeconds = 8.0)
                }.expectation {
                    it.exist("Network & internet")
                        .dontExist("Security")
                }
            }
        }
    }

    @Order(50)
    @Test
    fun scrollToBottom_scrollToTop() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .exist("[Account Avatar]")
                        .exist("[Settings]")
                }.action {
                    it.scrollToBottom()
                }.expectation {
                    it.exist("[Tips & support]")
                }
            }
            case(2) {
                action {
                    it.scrollToTop()
                }.expectation {
                    it.exist("[Account Avatar]")
                        .exist("[Settings]")
                }
            }
        }
    }

    @Order(60)
    @Test
    fun scrollToRightEdge_scrollToLeftEdge() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.action {
                    it.select("#below_search_omnibox_container")
                        .scrollToRightEdge()
                }.expectation {
                    it.exist("More")
                }
            }
            case(2) {
                action {
                    it.select("#below_search_omnibox_container")
                        .scrollToLeftEdge()
                }.expectation {
                    it.dontExist("More")
                }
            }
        }
    }

    @Order(70)
    @Test
    fun doUntilScrollStop() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.doUntilScrollStop(
                        direction = ScrollDirection.Down,
                        actionFunc = {
                            it.canSelect("System")
                        }
                    )
                    it.tap()
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                }.action {
                    it.doUntilScrollStop(
                        direction = ScrollDirection.Down,
                        actionFunc = {
                            it.canSelect("System2")
                        }
                    )
                }.expectation {
                    val lastItem = it.select("#recycler_view").descendants.last { it.id == "android:id/title" }
                    lastItem.text.thisIs("Tips & support")
                }
            }
        }

    }

    @Order(80)
    @Test
    fun scanElements() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                    TestElementCache.scanResults.clear()
                    (TestElementCache.scanResults.count() == 0).thisIsTrue()
                }.action {
                    it.scanElements()
                }.expectation {
                    (TestElementCache.scanResults.count() > 0).thisIsTrue()
                    (TestElementCache.scanResults.first().element.descendants.any() { it.text == "Search settings" }).thisIsTrue()
                    emulator {
                        (TestElementCache.scanResults.last().element.descendants.any() { it.text == "About emulated device" }).thisIsTrue()
                    }.realDevice {
                        (TestElementCache.scanResults.last().element.descendants.any() { it.text == "About phone" }).thisIsTrue()
                    }
                }
            }
        }

    }

    @Test
    @Order(90)
    fun withScroll() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    withScrollDown {
                        it
                            .select("[Notifications]").textIs("Notifications")
                            .select("[System]").textIs("System")
                    }
                    withScrollUp {
                        it
                            .select("[Google]").textIs("Google")
                            .select("[Display]").textIs("Display")
                    }
                }
            }
            case(2) {
                expectation {
                    withScrollDown {
                        it.exist("[System]")
                    }
                    withScrollUp {
                        it.exist("[Storage]")
                    }
                }
            }
            case(3) {
                action {
                    withScrollDown {
                        it.tap("[System]")
                    }
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
            case(4) {
                action {
                    it.tap("[<-]")
                    withScrollUp {
                        it.tap("[Network & internet]")
                    }
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
        }
    }
}
