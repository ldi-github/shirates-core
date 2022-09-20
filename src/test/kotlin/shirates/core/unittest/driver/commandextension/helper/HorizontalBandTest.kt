package shirates.core.unittest.driver.commandextension.helper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestElementCache.select
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.helper.HorizontalBand
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataAndroid

class HorizontalBandTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setAndroid()
        TestElementCache.loadXml(XmlDataAndroid.FlowTest)
        TestElementCache.synced = true
    }

    @Test
    fun canMerge() {

        run {
            // Arrange
            val hband = HorizontalBand(select("#close"))
            // Act, Assert
            assertThat(hband.canMerge(select("#close"))).isEqualTo(true)
            assertThat(hband.canMerge(select("Settings"))).isEqualTo(true)
            assertThat(hband.canMerge(select("#menu"))).isEqualTo(true)
            assertThat(hband.canMerge(select("#image1"))).isEqualTo(false)
        }

        run {
            // Arrange
            val hband = HorizontalBand(select("General"))
            // Act, Assert
            assertThat(hband.canMerge(select("General"))).isEqualTo(true)
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
            assertThat(elements[0].id).isEqualTo("close")
            assertThat(elements[1].text).isEqualTo("Settings")
            assertThat(elements[2].id).isEqualTo("menu")
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
            assertThat(elements[0].text).isEqualTo("General")
            assertThat(elements[1].id).isEqualTo("general-switch")
        }
    }

    @Test
    fun elements() {

        run {
            // Arrange
            val hband = HorizontalBand(select("#close"))
            // Act
            for (e in TestElementCache.allElements.filter { it.isWidget }.sortedByDescending { it.bounds.left }) {
                hband.merge(e)
            }
            val elements = hband.getElements()
            // Assert
            assertThat(elements.count()).isEqualTo(3)
            assertThat(elements[0].id).isEqualTo("close")
            assertThat(elements[1].text).isEqualTo("Settings")
            assertThat(elements[2].text).isEqualTo("Menu")
        }
    }

}