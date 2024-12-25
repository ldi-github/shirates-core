package shirates.core.unittest.vision.driver.commandextension.helper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.toVisionElement
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataAndroid
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.commandextension.helper.VerticalBand

class VerticalBandTest : UnitTest() {

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
            val vband = VerticalBand(select("Settings"))
            // Act, Assert
            assertThat(vband.canMerge(select("Settings"))).isEqualTo(false)
            assertThat(vband.canMerge(select("#close"))).isEqualTo(false)
            assertThat(vband.canMerge(select("#menu"))).isEqualTo(false)
            assertThat(vband.canMerge(select("#image1"))).isEqualTo(true)
            assertThat(vband.canMerge(select("Last Name"))).isEqualTo(true)
            assertThat(vband.canMerge(select("Test"))).isEqualTo(true)
            assertThat(vband.canMerge(select("First Name"))).isEqualTo(true)
            assertThat(vband.canMerge(select("Taro"))).isEqualTo(true)
            assertThat(vband.canMerge(select("General"))).isEqualTo(false)
            assertThat(vband.canMerge(select("#general-switch"))).isEqualTo(false)
            assertThat(vband.canMerge(select("Administrator"))).isEqualTo(true)
            assertThat(vband.canMerge(select("#administrator-switch"))).isEqualTo(false)
            assertThat(vband.canMerge(select("Notification"))).isEqualTo(true)
            assertThat(vband.canMerge(select("Save"))).isEqualTo(true)
        }
    }

    @Test
    fun merge() {

        run {
            // Arrange
            val vband = VerticalBand(select("Settings"))
            // Act
            vband.merge(select("#close"))
            vband.merge(select("#menu"))
            vband.merge(select("#image1"))
            vband.merge(select("Last Name"))
            vband.merge(select("Test"))
            vband.merge(select("First Name"))
            vband.merge(select("Taro"))
            vband.merge(select("General"))
            vband.merge(select("#general-switch"))
            vband.merge(select("Administrator"))
            vband.merge(select("#administrator-switch"))
            vband.merge(select("Notification"))
            vband.merge(select("Save"))
            val elements = vband.getElements()
            // Assert
            assertThat(elements.count()).isEqualTo(9)
        }
    }

    @Test
    fun elements() {

        run {
            // Arrange
            val vband = VerticalBand(select("#close"))
            // Act
            for (e in TestElementCache.allElements.filter { it.isWidget }.sortedByDescending { it.bounds.top }) {
                vband.merge(e.toVisionElement())
            }
            val elements = vband.getElements()
            // Assert
            assertThat(elements.count()).isEqualTo(5)

            for (e in vband.getElements()) {
                println(e)
            }
        }
    }

}