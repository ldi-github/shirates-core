package shirates.core.vision.uitest.ios.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.utility.string.forVisionComparison
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class VisionElementElementsExtensionTest : VisionTest() {

    @Test
    fun cell() {

        it.screenIs("[iOS Settings Top Screen]")

        run {
            // Arrange
            v1 = it.detect("General")
            // Act
            val a1 = v1.cell(index = 0)
            // Assert
            assertThat(a1.textForComparison).contains("General Accessibility".forVisionComparison())

            // Act
            val a2 = v1.cell(index = 1)
            // Assert
            assertThat(a2.isEmpty).isTrue
        }
    }

    @Test
    fun ancestors() {

        it.screenIs("[iOS Settings Top Screen]")

        run {
            // Arrange
            v1 = it.detect("General")
            // Act
            val list1 = v1.ancestors()
            // Assert
            assertThat(list1.count()).isEqualTo(1)
        }
    }

    @Test
    fun ancestorsContains_rect() {

        it.screenIs("[iOS Settings Top Screen]")

        run {
            // Arrange
            v1 = it.detect("General")
            v2 = it.detect("Accessibility")
            // Act
            val list1 = v1.ancestorsContains(v2.rect)
            // Assert
            assertThat(list1.count()).isEqualTo(1)
        }
        run {
            // Arrange
            v1 = it.detect("General")
            v2 = it.detect("Screen Time")
            // Act
            val list1 = v1.ancestorsContains(v2.rect)
            // Assert
            assertThat(list1.count()).isEqualTo(0)
        }
    }

    @Test
    fun ancestorsContains_visionElement() {

        it.screenIs("[iOS Settings Top Screen]")

        run {
            // Arrange
            v1 = it.detect("General")
            v2 = it.detect("Accessibility")
            // Act
            val list1 = v1.ancestorsContains(v2)
            // Assert
            assertThat(list1.count()).isEqualTo(1)
        }
        run {
            // Arrange
            v1 = it.detect("General")
            v2 = it.detect("Screen Time")
            // Act
            val list1 = v1.ancestorsContains(v2)
            // Assert
            assertThat(list1.count()).isEqualTo(0)
        }
    }

}