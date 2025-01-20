package shirates.core.vision.uitest.ios.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.Want
import shirates.core.vision.testDriveScope
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveScrollExtensionTest2 : VisionTest() {

    @Test
    @Order(40)
    fun scrollDown_scrollUp() {

        testDriveScope {
            run {
                // Arrange
                it.macro("[Developer Screen]")
                val lastItem =
                    it.select(".XCUIElementTypeTable").descendants.last { it.type == "XCUIElementTypeStaticText" && it.isVisible }
                println("lastItem:$lastItem")
                val lastBounds = lastItem.bounds
                val title = lastItem.label
                // Act
                it.scrollDown(scrollDurationSeconds = 5.0)
                // Assert
                val movedItem = it.widget(title)
                println("movedItem:$movedItem")
                println("${movedItem.bounds.centerY} <= ${lastBounds.centerY}")
                assertThat(movedItem.bounds.centerY <= lastBounds.centerY).isTrue()
            }

            run {
                // Arrange
                val firstItem =
                    it.select(".XCUIElementTypeTable").descendants.first { it.type == "XCUIElementTypeStaticText" && it.isVisible }
                println("firstItem:$firstItem")
                val firstBounds = firstItem.bounds
                val title = firstItem.label

                // Act
                it.scrollUp(scrollDurationSeconds = 5.0)
                // Assert
                val movedItem = it.widget(title, throwsException = false, waitSeconds = 0.0)
                println("movedItem:$movedItem")
                println("${firstBounds.centerY} <= ${movedItem.bounds.centerY}")
                assertThat(firstBounds.centerY <= movedItem.bounds.centerY).isTrue()
            }
        }
    }

    @Test
    @Order(50)
    fun scrollToBottom_scrollToTop() {

        testDriveScope {
            // Arrange
            it.macro("[Developer Screen]")
            // Act
            it.flickAndGoDown()
            it.scrollToBottom()
            // Assert
            assertThat(it.canSelect("Sign In")).isTrue()

            // Act
            it.flickAndGoUp()
            it.scrollToTop()
            // Assert
            assertThat(it.canSelect("Dark Appearance")).isTrue()
        }
    }

}