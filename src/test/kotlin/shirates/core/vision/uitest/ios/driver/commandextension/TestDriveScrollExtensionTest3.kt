package shirates.core.vision.uitest.ios.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.ScrollDirection
import shirates.core.driver.TestElementCache
import shirates.core.driver.commandextension.scanElements
import shirates.core.testcode.Want
import shirates.core.vision.classicScope
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveScrollExtensionTest3 : VisionTest() {

//    override fun setEventHandlers(context: TestDriverEventContext) {
//        context.irregularHandler = {
//            it.handleIrregulars()
//        }
//    }

    @Test
    @Order(60)
    fun doUntilScrollStop() {

        // Arrange
        it.macro("[Developer Screen]")
        // Act
        it.doUntilScrollStop(
            direction = ScrollDirection.Down,
            actionFunc = {
                it.canDetect("TV Provider")
            }
        )
        it.tap()
        // Assert
        it.exist("Cache Buster")

        // Arrange
        it.pressBack()
            .flickAndGoDown()
        // Act
        it.doUntilScrollStop(
            direction = ScrollDirection.Down,
            actionFunc = {
                it.canDetect("no exist")
            }
        )
        // Assert
        val lastItem = it.detect("sandbox apple account")
        assertThat(lastItem.isFound).isTrue()
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
        classicScope {
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

}