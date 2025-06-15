package shirates.core.uitest.ios.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.ScrollDirection
import shirates.core.driver.TestDriverEventContext
import shirates.core.driver.TestElementCache
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Want
import utility.handleIrregulars

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveScrollExtensionTest3 : UITest() {

    override fun setEventHandlers(context: TestDriverEventContext) {
        context.irregularHandler = {
            it.handleIrregulars()
        }
    }

    @Test
    @Order(60)
    fun doUntilScrollStop() {

        // Arrange
        it.macro("[Developer Screen]")
        // Act
        it.doUntilScrollStop(
            direction = ScrollDirection.Down,
            actionFunc = {
                it.canSelect("TV Provider")
            }
        )
        it.tap()
        // Assert
        it.exist("Cache Buster")


        // Arrange
        it.tap(".XCUIElementTypeButton&&Developer")
            .flickAndGoDown()
        // Act
        it.doUntilScrollStop(
            direction = ScrollDirection.Down,
            actionFunc = {
                it.canSelect("no exist")
            }
        )
        // Assert
        val descendants = it.select(".XCUIElementTypeTable").descendants
        assertThat(descendants.any() { it.label == "This account will only be used for testing your in-app purchases in sandbox." })
            .isTrue
    }

    @Test
    @Order(70)
    fun scanElements() {

        // Arrange
        it.macro("[iOS Settings Top Screen]")
        it.flickAndGoDown()
            .tap("Privacy & Security")
        TestElementCache.scanResults.clear()
        assertThat(TestElementCache.scanResults.count() == 0).isTrue()
        // Act
        it.scanElements()
        // Assert
        assertThat(TestElementCache.scanResults.count() > 0).isTrue()
        assertThat(TestElementCache.scanResults.first().element.descendants.any() { it.label == "Location Services" })
            .isTrue()
        assertThat(TestElementCache.scanResults.last().element.descendants.any() { it.label == "Apple Advertising" })
            .isTrue()
    }

}