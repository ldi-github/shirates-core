package shirates.core.vision.uitest.ios.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class VisionDriveElementsExtensionTest : VisionTest() {

    @Test
    fun ancestors_descendants() {

        it.screenIs("[iOS Settings Top Screen]")

        run {
            // Arrange
            val v = it.detect("Apple Account")
            // Act
            val list = v.ancestors()
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