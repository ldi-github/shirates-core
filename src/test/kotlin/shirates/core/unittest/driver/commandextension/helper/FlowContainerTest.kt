package shirates.core.unittest.driver.commandextension.helper

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestElementCache.select
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.helper.FlowContainer
import shirates.core.logging.Message.message
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataAndroid

class FlowContainerTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setAndroid()
        TestElementCache.loadXml(XmlDataAndroid.FlowTest)
        TestElementCache.synced = true
    }

    @Test
    fun addCloseButton() {

        // Arrange
        val closeButton = select("#close")
        val fc = FlowContainer()
        // Act
        fc.addElementToRow(closeButton)
        // Assert
        assertThat(fc.rows.count()).isEqualTo(1)
        // Arrange
        val row1 = fc.rows.first()
        val row1Element1 = row1.members[0]
        // Assert
        assertThat(row1.top).isEqualTo(14)
        assertThat(row1.bottom).isEqualTo(39)
        assertThat(row1Element1).isEqualTo(closeButton)
    }

    @Test
    fun addImage1() {

        // Arrange
        val closeButton = select("#close")
        val image1 = select("#image1")
        val fc = FlowContainer()
        // Act
        fc.addElementToRow(closeButton)
        fc.addElementToRow(image1)
        // Assert
        assertThat(fc.rows.count()).isEqualTo(2)
        // Arrange
        val row2 = fc.rows[1]
        val row2Element1 = row2.members[0]
        // Assert
        assertThat(row2.top).isEqualTo(67)
        assertThat(row2.bottom).isEqualTo(166)
        assertThat(row2Element1).isEqualTo(image1)
    }

    @Test
    fun addSettings() {

        // Arrange
        val closeButton = select("#close")
        val image1 = select("#image1")
        val settings = select("Settings")
        val fc = FlowContainer()
        // Act
        fc.addElementToRow(closeButton)
        fc.addElementToRow(image1)
        fc.addElementToRow(settings)
        // Assert
        assertThat(fc.rows.count()).isEqualTo(2)
        // Arrange
        val row1 = fc.rows[0]
        val row1Element1 = row1.members[0]
        val row1Element2 = row1.members[1]
        // Assert
        assertThat(row1.top).isEqualTo(14)
        assertThat(row1.bottom).isEqualTo(39)
        assertThat(row1Element1).isEqualTo(closeButton)
        assertThat(row1Element2).isEqualTo(settings)
    }

    @Test
    fun addMenu() {

        // Arrange
        val closeButton = select("#close")
        val image1 = select("#image1")
        val settings = select("Settings")
        val menuButton = select("#menu")
        val fc = FlowContainer()
        // Act
        fc.addElementToRow(closeButton)
        fc.addElementToRow(image1)
        fc.addElementToRow(settings)
        fc.addElementToRow(menuButton)
        // Assert
        assertThat(fc.rows.count()).isEqualTo(2)
        // Arrange
        val row1 = fc.rows[0]
        val row1Element1 = row1.members[0]
        val row1Element2 = row1.members[1]
        val row1Element3 = row1.members[2]
        // Assert
        assertThat(row1.top).isEqualTo(14)
        assertThat(row1.bottom).isEqualTo(39)
        assertThat(row1Element1).isEqualTo(closeButton)
        assertThat(row1Element2).isEqualTo(settings)
        assertThat(row1Element3).isEqualTo(menuButton)
    }

    @Test
    fun addGeneral() {

        // Arrange
        val closeButton = select("#close")
        val image1 = select("#image1")
        val settings = select("Settings")
        val menuButton = select("#menu")
        val general = select("General")
        val fc = FlowContainer()
        // Act
        fc.addElementToRow(closeButton)
        fc.addElementToRow(image1)
        fc.addElementToRow(settings)
        fc.addElementToRow(menuButton)
        fc.addElementToRow(general)
        // Assert
        assertThat(fc.rows.count()).isEqualTo(3)
        // Arrange
        val row3 = fc.rows[2]
        val row3Element1 = row3.members[0]
        // Assert
        assertThat(row3.top).isEqualTo(204)
        assertThat(row3.bottom).isEqualTo(219)
        assertThat(row3Element1).isEqualTo(general)
    }

    @Test
    fun addAll() {

        // Arrange
        val fc = FlowContainer()
        // Act
        for (element in TestElementCache.allElements.filter { it.isWidget }) {
            fc.addElementToRow(element)
        }
        // Assert
        assertThat(fc.rows.count()).isEqualTo(6)
        val row1 = fc.rows[0]
        val row2 = fc.rows[1]
        val row3 = fc.rows[2]
        val row4 = fc.rows[3]
        val row5 = fc.rows[4]
        val row6 = fc.rows[5]
        assertThat(row1.members.count()).isEqualTo(3)
        assertThat(row2.members.count()).isEqualTo(5)
        assertThat(row3.members.count()).isEqualTo(2)
        assertThat(row4.members.count()).isEqualTo(2)
        assertThat(row5.members.count()).isEqualTo(1)
        assertThat(row6.members.count()).isEqualTo(1)
        // Assert(row1)
        assertThat(row1.members[0]).isEqualTo(select("#close"))
        assertThat(row1.members[1]).isEqualTo(select("Settings"))
        assertThat(row1.members[2]).isEqualTo(select("#menu"))
        // Assert(row2)
        assertThat(row2.members[0]).isEqualTo(select("#image1"))
        assertThat(row2.members[1]).isEqualTo(select("Last Name"))
        assertThat(row2.members[2]).isEqualTo(select("Test"))
        assertThat(row2.members[3]).isEqualTo(select("First Name"))
        assertThat(row2.members[4]).isEqualTo(select("Taro"))
        // Assert(row3)
        assertThat(row3.members[0]).isEqualTo(select("General"))
        assertThat(row3.members[1]).isEqualTo(select("#general-switch"))
        // Assert(row4)
        assertThat(row4.members[0]).isEqualTo(select("Administrator"))
        assertThat(row4.members[1]).isEqualTo(select("#administrator-switch"))
        // Assert(row5)
        assertThat(row5.members[0]).isEqualTo(select("#notification"))
        // Assert(row6)
        assertThat(row6.members[0]).isEqualTo(select("#save"))

        // Assert
        assertThatThrownBy {
            fc.element(0)
        }.isInstanceOf(IndexOutOfBoundsException::class.java)
            .hasMessage(message(id = "posMustBeGreaterThanZero", arg1 = "0"))
    }

    @Test
    fun getElements() {

        // Arrange
        val fc = FlowContainer()
        fc.addElementToRow(select("#close"))
        run {
            // Act
            val elements = fc.getElements()
            // Assert
            assertThat(elements.count()).isEqualTo(1)
        }

        // Arrange
        fc.addElementToRow(select("#image1"))
        run {
            // Act
            val elements = fc.getElements()
            // Assert
            assertThat(elements.count()).isEqualTo(2)
            assertThat(elements[0]).isEqualTo(select("#close"))
            assertThat(elements[1]).isEqualTo(select("#image1"))
        }

        // Arrange
        fc.addElementToRow(select("#menu"))
        run {
            // Act
            val elements = fc.getElements()
            // Assert
            assertThat(elements.count()).isEqualTo(3)
            assertThat(elements[0]).isEqualTo(select("#close"))
            assertThat(elements[1]).isEqualTo(select("#menu"))
            assertThat(elements[2]).isEqualTo(select("#image1"))
        }

        // Arrange
        fc.addElementToRow(select("Settings"))
        run {
            // Act
            val elements = fc.getElements()
            // Assert
            assertThat(elements.count()).isEqualTo(4)
            assertThat(elements[0]).isEqualTo(select("#close"))
            assertThat(elements[1]).isEqualTo(select("Settings"))
            assertThat(elements[2]).isEqualTo(select("#menu"))
            assertThat(elements[3]).isEqualTo(select("#image1"))
        }

        // Arrange
        fc.addElementToRow(select("#general-switch"))
        run {
            // Act
            val elements = fc.getElements()
            // Assert
            assertThat(elements.count()).isEqualTo(5)
            assertThat(elements[0]).isEqualTo(select("#close"))
            assertThat(elements[1]).isEqualTo(select("Settings"))
            assertThat(elements[2]).isEqualTo(select("#menu"))
            assertThat(elements[3]).isEqualTo(select("#image1"))
            assertThat(elements[4]).isEqualTo(select("#general-switch"))
        }

        // Arrange
        fc.addElementToRow(select("Last Name"))
        run {
            // Act
            val elements = fc.getElements()
            // Assert
            assertThat(elements.count()).isEqualTo(6)
            assertThat(elements[0]).isEqualTo(select("#close"))
            assertThat(elements[1]).isEqualTo(select("Settings"))
            assertThat(elements[2]).isEqualTo(select("#menu"))
            assertThat(elements[3]).isEqualTo(select("#image1"))
            assertThat(elements[4]).isEqualTo(select("Last Name"))
            assertThat(elements[5]).isEqualTo(select("#general-switch"))
        }
    }

    @Test
    fun element() {

        // Arrange
        val fc = FlowContainer()
        fc.addElementToRow(select("#close"))
        fc.addElementToRow(select("#image1"))
        fc.addElementToRow(select("#menu"))
        fc.addElementToRow(select("Settings"))
        fc.addElementToRow(select("#general-switch"))
        fc.addElementToRow(select("Last Name"))
        // Act, Assert
        assertThat(fc.element(1)).isEqualTo(select("#close"))
        assertThat(fc.element(2)).isEqualTo(select("Settings"))
        assertThat(fc.element(3)).isEqualTo(select("#menu"))
        assertThat(fc.element(4)).isEqualTo(select("#image1"))
        assertThat(fc.element(5)).isEqualTo(select("Last Name"))
        assertThat(fc.element(6)).isEqualTo(select("#general-switch"))
        assertThat(fc.element(7).isEmpty).isEqualTo(true)
        assertThatThrownBy {
            fc.element(0)
        }.isInstanceOf(IndexOutOfBoundsException::class.java)
            .hasMessage(message(id = "posMustBeGreaterThanZero", arg1 = "0"))
    }
}