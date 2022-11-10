package shirates.core.uitest.ios.driver.commandextension.work08

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.*
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Want
import shirates.core.utility.element.ElementCategoryExpressionUtility
import utility.handleIrregulars

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveScrollExtensionTest : UITest() {

    override fun setEventHandlers(context: TestDriverEventContext) {
        context.irregularHandler = {
            it.handleIrregulars()
        }
    }

    @Order(1)
    @Test
    fun getScrollableElementsInDescendants() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Apple Maps Top Screen]")
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

    @Order(1)
    @Test
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

    @Order(2)
    @Test
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

    @Order(3)
    @Test
    fun scrollDown_scrollUp() {

        run {
            // Arrange
            it.macro("[Developer Screen]")
            val lastItem =
                it.select(".XCUIElementTypeTable").descendants.last { it.type == "XCUIElementTypeStaticText" && it.isVisible }
            val title = lastItem.label
            // Act
            it.scrollDown(durationSeconds = 5.0)
            // Assert
            var movedItem = it.select(title)
            assertThat(movedItem.bounds.centerY < lastItem.bounds.centerY).isTrue()


            // Act
            it.scrollDown()
            // Assert
            movedItem = it.select(title, throwsException = false, waitSeconds = 0.0)
            assertThat(movedItem.isEmpty).isTrue()
        }

        run {
            // Arrange
            val firstItem =
                it.select(".XCUIElementTypeTable").descendants.first { it.type == "XCUIElementTypeStaticText" && it.isVisible }
            val title = firstItem.label

            // Act
            it.scrollUp(durationSeconds = 5.0)
            // Assert
            var movedItem = it.select(title, throwsException = false, waitSeconds = 0.0)
            assertThat(firstItem.bounds.centerY < movedItem.bounds.centerY).isTrue()


            // Act
            it.scrollUp()
            // Assert
            movedItem = it.select(title, throwsException = false, waitSeconds = 0.0)
            assertThat(movedItem.isEmpty).isTrue()
        }
    }

    @Order(3)
    @Test
    fun scrollToBottom_scrollToTop() {

        // Arrange
        it.macro("[Developer Screen]")
        // Act
        it.scrollToBottom()
        // Assert
        val lastItem =
            it.select(".XCUIElementTypeTable").descendants.last { it.type == "XCUIElementTypeStaticText" && it.isVisible }
        assertThat(lastItem.label).isEqualTo("The graphics performance HUD shows framerate, GPU time, memory usage, and can log performance data for later analysis.")


        // Act
        it.scrollToTop()
        // Assert
        val firstItem =
            it.select(".XCUIElementTypeTable").descendants.first { it.type == "XCUIElementTypeStaticText" && it.isVisible }
        assertThat(firstItem.label).isEqualTo("Dark Appearance")
    }

    @Order(3)
    @Test
    fun doUntilScrollStop() {

        // Arrange
        it.macro("[Developer Screen]")
        // Act
        it.doUntilScrollStop(
            direction = ScrollDirection.Down,
            actionFunc = {
                it.canSelect("AirPlay Suggestions")
            }
        )
        it.tap()
        // Assert
        it.exist("Always Prompt User with Suggested TV")


        // Arrange
        it.tap(".XCUIElementTypeButton&&Developer")
        // Act
        it.doUntilScrollStop(
            direction = ScrollDirection.Down,
            actionFunc = {
                it.canSelect("no exist")
            }
        )
        // Assert
        val lastItem =
            it.select(".XCUIElementTypeTable").descendants.last { it.type == "XCUIElementTypeStaticText" && it.isVisible }
        assertThat(lastItem.label).isEqualTo("The graphics performance HUD shows framerate, GPU time, memory usage, and can log performance data for later analysis.")
    }

    @Order(3)
    @Test
    fun scanElements() {

        // Arrange
        it.macro("[Developer Screen]")
        TestElementCache.scanResults.clear()
        assertThat(TestElementCache.scanResults.count() == 0).isTrue()
        // Act
        it.scanElements()
        // Assert
        assertThat(TestElementCache.scanResults.count() > 0).isTrue()
        assertThat(TestElementCache.scanResults.first().element.descendants.any() { it.label == "Dark Appearance" }).isTrue()
        assertThat(TestElementCache.scanResults.last().element.descendants.any() { it.label == "Enable MIDI-CI" }).isTrue()
    }
}