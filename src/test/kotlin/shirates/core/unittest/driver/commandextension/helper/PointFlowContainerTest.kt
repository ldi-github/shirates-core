package shirates.core.unittest.driver.commandextension.helper

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.helper.PointFlowContainer
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataAndroid

class PointFlowContainerTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setAndroid()
        TestElementCache.loadXml(XmlDataAndroid.FlowTest)
        TestElementCache.synced = true
    }

    @Test
    fun addAll() {

        // Arrange
        val pfc = PointFlowContainer()
        // Act
        for (element in TestElementCache.allElements.filter { it.isWidget }) {
            pfc.addElement(element)
        }
        val elements = pfc.getElements()
        elements.forEach { println("$it") }
        // Assert
        Assertions.assertThat(elements.count()).isEqualTo(14)
        Assertions.assertThat(elements[0]).isEqualTo(TestElementCache.select("#close"))
        Assertions.assertThat(elements[1]).isEqualTo(TestElementCache.select("#menu"))
        Assertions.assertThat(elements[2]).isEqualTo(TestElementCache.select("Settings"))
        Assertions.assertThat(elements[3]).isEqualTo(TestElementCache.select("Last Name"))
        Assertions.assertThat(elements[4]).isEqualTo(TestElementCache.select("Test"))
        Assertions.assertThat(elements[5]).isEqualTo(TestElementCache.select("#image1"))
        Assertions.assertThat(elements[6]).isEqualTo(TestElementCache.select("First Name"))
        Assertions.assertThat(elements[7]).isEqualTo(TestElementCache.select("Taro"))
        Assertions.assertThat(elements[8]).isEqualTo(TestElementCache.select("General"))
        Assertions.assertThat(elements[9]).isEqualTo(TestElementCache.select("#general-switch"))
        Assertions.assertThat(elements[10]).isEqualTo(TestElementCache.select("Administrator"))
        Assertions.assertThat(elements[11]).isEqualTo(TestElementCache.select("#administrator-switch"))
        Assertions.assertThat(elements[12]).isEqualTo(TestElementCache.select("#notification"))
        Assertions.assertThat(elements[13]).isEqualTo(TestElementCache.select("#save"))
    }
}