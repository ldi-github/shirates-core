package shirates.core.uitest.ios.driver.commandextension

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriverEventContext
import shirates.core.driver.commandextension.*
import shirates.core.driver.descendants
import shirates.core.testcode.UITest
import shirates.core.testcode.Want
import utility.handleIrregulars

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveScrollExtensionTest2 : UITest() {

    override fun setEventHandlers(context: TestDriverEventContext) {
        context.irregularHandler = {
            it.handleIrregulars()
        }
    }

    @Test
    @Order(40)
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
            Assertions.assertThat(movedItem.bounds.centerY < lastItem.bounds.centerY).isTrue()


            // Act
            it.scrollDown()
            // Assert
            movedItem = it.select(title, throwsException = false, waitSeconds = 0.0)
            Assertions.assertThat(movedItem.isEmpty).isTrue()
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
            Assertions.assertThat(firstItem.bounds.centerY < movedItem.bounds.centerY).isTrue()


            // Act
            it.scrollUp()
            // Assert
            movedItem = it.select(title, throwsException = false, waitSeconds = 0.0)
            Assertions.assertThat(movedItem.isEmpty).isTrue()
        }
    }

    @Test
    @Order(50)
    fun scrollToBottom_scrollToTop() {

        // Arrange
        it.macro("[Developer Screen]")
        // Act
        it.scrollToBottom()
        // Assert
        val lastItem =
            it.select(".XCUIElementTypeTable").descendants.last { it.type == "XCUIElementTypeStaticText" && it.isVisible }
        Assertions.assertThat(lastItem.label)
            .isEqualTo("The graphics performance HUD shows framerate, GPU time, memory usage, and can log performance data for later analysis.")


        // Act
        it.scrollToTop()
        // Assert
        val firstItem =
            it.select(".XCUIElementTypeTable").descendants.first { it.type == "XCUIElementTypeStaticText" && it.isVisible }
        Assertions.assertThat(firstItem.label).isEqualTo("Dark Appearance")
    }


}