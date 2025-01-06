package shirates.core.vision.unittest.driver.commandextension.helper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.toVisionElement
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataAndroid
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.commandextension.helper.HorizontalBand

class HorizontalBandTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setAndroid()
        TestElementCache.loadXml(XmlDataAndroid.FlowTest)
        TestElementCache.synced = true
    }

    fun select(expression: String): VisionElement {
        return TestElementCache.select(expression).toVisionElement()
    }

    @Test
    fun canMerge() {

        run {
            // Arrange
            val hband = HorizontalBand(select("#close"))
            // Act, Assert
            assertThat(hband.canMerge(select("#close"))).isEqualTo(false)
            assertThat(hband.canMerge(select("Settings"))).isEqualTo(true)
            assertThat(hband.canMerge(select("#menu"))).isEqualTo(true)
            assertThat(hband.canMerge(select("#image1"))).isEqualTo(false)
        }

        run {
            // Arrange
            val hband = HorizontalBand(select("General"))
            // Act, Assert
            assertThat(hband.canMerge(select("General"))).isEqualTo(false)
            assertThat(hband.canMerge(select("#general-switch"))).isEqualTo(true)
            assertThat(hband.canMerge(select("Administrator"))).isEqualTo(false)
        }
    }

    @Test
    fun merge() {

        run {
            // Arrange
            val hband = HorizontalBand(select("#close"))
            // Act
            hband.merge(select("Settings"))
            hband.merge(select("#menu"))
            hband.merge(select("#image1"))
            val elements = hband.getElements()
            // Assert
            assertThat(elements.count()).isEqualTo(3)
        }

        run {
            // Arrange
            val hband = HorizontalBand(select("General"))
            // Act
            hband.merge(select("General"))
            hband.merge(select("#general-switch"))
            hband.merge(select("Administrator"))
            val elements = hband.getElements()
            // Assert
            assertThat(elements.count()).isEqualTo(2)
        }
    }

    @Test
    fun elements() {

        run {
            // Arrange
            val hband = HorizontalBand(select("#close"))
            // Act
            for (e in TestElementCache.allElements.filter { it.isWidget }.sortedByDescending { it.bounds.left }) {
                hband.merge(e.toVisionElement())
            }
            val elements = hband.getElements()
            // Assert
            assertThat(elements.count()).isEqualTo(3)
        }
    }

}