package shirates.core.uitest.ios.driver.commandextension.work02

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.screenshot
import shirates.core.driver.commandextension.select
import shirates.core.driver.commandextension.swipePointToPoint
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveSwipeExtensionTest : UITest() {

    @Order(1)
    @Test
    fun swipePointToPoint() {

        // Arrange
        it.macro("[iOS Settings Top Screen]")
        val itemBefore = it.select("Passwords").bounds
        val targetPlace = it.select("General").bounds
        println("targetPlace=$targetPlace")
        // Act
        it.screenshot(filename = "before")
        it.swipePointToPoint(
            startX = itemBefore.centerX,
            startY = itemBefore.centerY,
            endX = targetPlace.centerX,
            endY = targetPlace.centerY,
            durationSeconds = 9
        )
        val itemAfter = it.select("Passwords").bounds
        it.screenshot(filename = "after")
        println("itemAfter=$itemAfter")
        // Assert
        val diff = Math.abs(itemAfter.centerY - targetPlace.centerY)
        println("diff=$diff")
        assertThat(diff < 30).isTrue()
    }

}