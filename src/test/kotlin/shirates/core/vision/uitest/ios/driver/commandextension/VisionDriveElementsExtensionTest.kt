package shirates.core.vision.uitest.ios.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.vision.driver.commandextension.descendants
import shirates.core.vision.driver.commandextension.detect
import shirates.core.vision.driver.commandextension.rootElement
import shirates.core.vision.driver.commandextension.screenIs
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class VisionDriveElementsExtensionTest : VisionTest() {

    @Test
    fun descendants() {

        it.screenIs("[iOS Settings Top Screen]")

        run {
            // Arrange
            val v = it.detect("General")
            // Act
            val list = v.descendants()
            val boxes = list.filter { it.textForComparison.contains(v1.textForComparison) }
            // Assert
            assertThat(boxes).hasSize(1)
        }
        run {
            // Arrange
            val v = it.detect("General")
            // Act
            val list = rootElement.descendants()
            val boxes = list.filter { it.textForComparison.contains(v.textForComparison) }
            // Assert
            assertThat(boxes).hasSize(2)
            assertThat(boxes[0].textForComparison.contains(v.textForComparison))
            assertThat(boxes[1].textForComparison.contains(v.textForComparison))
        }
    }

}