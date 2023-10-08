package shirates.core.uitest.ios.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriverEventContext
import shirates.core.driver.branchextension.ifCanSelect
import shirates.core.driver.commandextension.*
import shirates.core.driver.rootElement
import shirates.core.logging.printInfo
import shirates.core.testcode.UITest
import shirates.core.testcode.Unstable
import shirates.core.testcode.Want
import shirates.core.utility.element.ElementCategoryExpressionUtility
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
                        e.isScrollable.thisIsTrue()
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
                        e.isScrollable.thisIsTrue()
                    }
                    scrollableElements.count().thisIs(3)
                    scrollableElements.count { it.type == "XCUIElementTypeMap" }.thisIs(1)
                    scrollableElements.count { it.type == "XCUIElementTypeTable" }.thisIs(1)
                    scrollableElements.count { it.type == "XCUIElementTypeCollectionView" }.thisIs(1)
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

    @Unstable("[iOS Settings Top Screen] is displayed(currentScreen=, expected identity=[Settings])")
    @Test
    @Order(30)
    fun hasScrollable() {

        // Arrange
        it.macro("[iOS Settings Top Screen]")
        // Act, Assert
        assertThat(it.hasScrollable).isTrue()

        val originalData = ElementCategoryExpressionUtility.iosScrollableTypesExpression
        try {
            // Arrange
            ElementCategoryExpressionUtility.iosScrollableTypesExpression = "DummyType"
            // Act, Assert
            assertThat(it.hasScrollable).isFalse()
        } finally {
            ElementCategoryExpressionUtility.iosScrollableTypesExpression = originalData
        }
    }

}