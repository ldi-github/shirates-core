package shirates.core.uitest.ios.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriverEventContext
import shirates.core.driver.TestElement
import shirates.core.driver.branchextension.ifCanSelect
import shirates.core.driver.commandextension.*
import shirates.core.driver.rootElement
import shirates.core.logging.printInfo
import shirates.core.testcode.UITest
import shirates.core.testcode.Want
import utility.handleIrregulars

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveScrollExtensionTest1 : UITest() {

    override fun setEventHandlers(context: TestDriverEventContext) {
        context.irregularHandler = {
            it.handleIrregulars()
        }
    }

    @Test
    @Order(10)
    fun getScrollableElementsInDescendants() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.expectation {
                    val scrollableElements = rootElement.getScrollableElementsInDescendantsAndSelf()
                    for (e in scrollableElements) {
                        e.printInfo()
                        e.isScrollableElement.thisIsTrue()
                    }
                    scrollableElements.count().thisIs(1)
                    scrollableElements.filter { it.type == "XCUIElementTypeTable" }.count().thisIs(1)
                }
            }
            case(2) {
                condition {
                    it.macro("[Apple Maps Top Screen]")
                    ifCanSelect("Cancel") {
                        it.tap()
                    }
                }.expectation {
                    val scrollableElements = rootElement.getScrollableElementsInDescendantsAndSelf()
                    for (e in scrollableElements) {
                        e.printInfo()
                        e.isScrollableElement.thisIsTrue()
                    }
                    scrollableElements.count().thisIs(3)
                    fun TestElement.thisIsScrollable() {
                        val r =
                            this.type == "XCUIElementTypeMap" || this.type == "XCUIElementTypeTable" || this.type == "XCUIElementTypeCollectionView"
                        r.thisIsTrue("This is XCUIElementTypeMap or XCUIElementTypeTable or XCUIElementTypeCollectionView")
                    }
                    scrollableElements[0].thisIsScrollable()
                    scrollableElements[1].thisIsScrollable()
                    scrollableElements[2].thisIsScrollable()
                }
            }

        }
    }

    @Test
    @Order(20)
    fun getScrollableTarget() {

        // Arrange
        it.macro("[Apple Maps Top Screen]")
            .screenIs("[Apple Maps Top Screen]")
        val scrollableElements = rootElement.getScrollableElementsInDescendantsAndSelf()
        val largestScrollableElement = scrollableElements.filter { it.isVisible }.maxByOrNull { it.bounds.area }!!
        // Act
        val target1 = largestScrollableElement.getScrollableElement()
        // Assert
        assertThat(target1.toString()).isEqualTo(largestScrollableElement.toString())


        // Arrange
        val largestScrollableTarget = scrollableElements.maxByOrNull { it.bounds.area }
        val nonScrollableElement = rootElement
        val target2 = nonScrollableElement.getScrollableElement()
        // Act, Assert
        assertThat(target2.toString()).isNotEqualTo(nonScrollableElement.toString())
        assertThat(target2.toString()).isEqualTo(largestScrollableTarget.toString())
    }

}