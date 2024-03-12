package shirates.core.unittest.driver.commandextension.helper

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestElementCache.select
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.helper.VerticalFlowContainer
import shirates.core.logging.Message.message
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataAndroid

class VerticalFlowContainerTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setAndroid()
        TestElementCache.loadXml(XmlDataAndroid.FlowTest)
        TestElementCache.synced = true
    }

    @Test
    fun addCloseButton() {

        // Arrange
        val closeButton = select("#close")
        val vfc = VerticalFlowContainer()
        // Act
        vfc.addElementToColumn(closeButton)
        // Assert
        assertThat(vfc.columns.count()).isEqualTo(1)
        // Arrange
        val column1 = vfc.columns.first()
        val column1Element1 = column1.members[0]
        // Assert
        assertThat(column1.members.count()).isEqualTo(1)
        assertThat(column1.left).isEqualTo(14)
        assertThat(column1.right).isEqualTo(44)
        assertThat(column1Element1).isEqualTo(closeButton)
    }

    @Test
    fun addSettings() {

        // Arrange
        val closeButton = select("#close")
        val settings = select("Settings")
        val vfc = VerticalFlowContainer()
        // Act
        vfc.addElementToColumn(closeButton)
        vfc.addElementToColumn(settings)
        // Assert
        assertThat(vfc.columns.count()).isEqualTo(2)
    }

    @Test
    fun addMenu() {

        // Arrange
        val closeButton = select("#close")
        val settings = select("Settings")
        val menu = select("Menu")
        val vfc = VerticalFlowContainer()
        // Act
        vfc.addElementToColumn(closeButton)
        vfc.addElementToColumn(settings)
        vfc.addElementToColumn(menu)
        // Assert
        assertThat(vfc.columns.count()).isEqualTo(3)
    }

    @Test
    fun addImage1() {

        // Arrange
        val closeButton = select("#close")
        val settings = select("Settings")
        val menu = select("Menu")
        val image1 = select("#image1")
        val vfc = VerticalFlowContainer()
        // Act
        vfc.addElementToColumn(closeButton)
        vfc.addElementToColumn(settings)
        vfc.addElementToColumn(menu)
        vfc.addElementToColumn(image1)
        // Assert
        assertThat(vfc.columns.count()).isEqualTo(3)
        // Arrange
        val column1 = vfc.columns[0]
        // Assert
        assertThat(column1.members[0]).isEqualTo(closeButton)
        assertThat(column1.members[1]).isEqualTo(image1)
    }

    @Test
    fun getElements() {

        // Arrange
        val vfc = VerticalFlowContainer()
        vfc.addElementToColumn(select("#close"))
        vfc.addElementToColumn(select("Administrator"))
        vfc.addElementToColumn(select("General"))
        vfc.addElementToColumn(select("Menu"))

        run {
            // Act
            val elements = vfc.getElements()
            // Assert
            assertThat(elements.count()).isEqualTo(4)
        }
    }

    @Test
    fun element() {

        // Arrange
        val vfc = VerticalFlowContainer()
        vfc.addElementToColumn(select("#close"))
        vfc.addElementToColumn(select("Administrator"))
        vfc.addElementToColumn(select("General"))
        vfc.addElementToColumn(select("Menu"))
        // Act, Assert
        assertThat(vfc.element(1)).isEqualTo(select("#close"))
        assertThat(vfc.element(2)).isEqualTo(select("General"))
        assertThat(vfc.element(3)).isEqualTo(select("Administrator"))
        assertThat(vfc.element(4)).isEqualTo(select("Menu"))
        assertThat(vfc.element(5).isEmpty).isEqualTo(true)
        assertThatThrownBy {
            vfc.element(0)
        }.isInstanceOf(IndexOutOfBoundsException::class.java)
            .hasMessage(message(id = "posMustBeGreaterThanZero", arg1 = "0"))
    }
}