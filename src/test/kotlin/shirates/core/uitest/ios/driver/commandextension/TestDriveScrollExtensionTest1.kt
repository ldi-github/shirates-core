package shirates.core.uitest.ios.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.*
import shirates.core.driver.branchextension.ifCanSelect
import shirates.core.driver.commandextension.*
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
                    it.macro("[Apple Maps Top Screen]")
                        .ifCanSelect("Not Now") {
                            it.tap()
                        }
                        .screenIs("[Apple Maps Top Screen]")
                        .tap(".XCUIElementTypeSearchField")
                }.expectation {
                    // Act
                    val scrollableElements = rootElement.getScrollableElementsInDescendants()
                    // Assert
                    assertThat(scrollableElements.count()).isGreaterThanOrEqualTo(2)
                    for (e in scrollableElements) {
                        e.isScrollable.thisIsTrue()
                    }
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
        val scrollableElements = rootElement.getScrollableElementsInDescendants()
        val largestScrollableElement = scrollableElements.filter { it.isVisible }.maxByOrNull { it.bounds.area }!!
        // Act
        val target1 = largestScrollableElement.getScrollableTarget()
        // Assert
        assertThat(target1).isEqualTo(largestScrollableElement)


        // Arrange
        val largestScrollableTarget = scrollableElements.maxByOrNull { it.bounds.area }
        val nonScrollableElement = rootElement
        val target2 = nonScrollableElement.getScrollableTarget()
        // Act, Assert
        assertThat(target2).isNotEqualTo(nonScrollableElement)
        assertThat(target2).isEqualTo(largestScrollableTarget)
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