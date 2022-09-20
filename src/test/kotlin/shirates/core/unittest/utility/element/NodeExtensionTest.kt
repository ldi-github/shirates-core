package shirates.core.unittest.utility.element

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.TestConfig
import shirates.core.driver.TestElement
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.driver.rootElement
import shirates.core.testcode.UnitTest
import shirates.core.utility.*

class NodeExtensionTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setAndroid()

        val configPath = "unitTestData/testConfig/androidSettings/testConfigTestData1.json".toPath()
        TestConfig(configPath.toString())
    }

    override fun afterAll(context: ExtensionContext?) {

        TestMode.clear()
    }

    val xmlData = """
<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>
<hierarchy>
  <android.widget.FrameLayout>
    <item />
    <item />
    <item />
  </android.widget.FrameLayout>
</hierarchy>
""".trimIndent()

    @Test
    fun setAttribute_getAttributes() {

        // Act, Assert
        assertThatThrownBy {
            val e = TestElement()
            e.node.setAttribute("content-desc", "value1")
        }.isInstanceOf(IllegalAccessError::class.java)
            .hasMessage("node is not set.")


        // Arrange
        TestElementCache.loadXml(xmlData)
        val e = rootElement
        // Act
        e.node.setAttribute("content-desc", "a1")
        // Assert
        assertThat(e.node.getAttribute("content-desc")).isEqualTo("a1")


        // Act
        e.node.setAttribute("content-desc", "a2")
        // Assert
        assertThat(e.node.getAttribute("content-desc")).isEqualTo("a2")
    }

    @Test
    fun hasAttribute() {

        // Arrange
        TestElementCache.loadXml(xmlData)
        val e = rootElement
        // Act, Assert
        assertThat(e.node.hasAttribute("content-desc")).isFalse()


        // Arrange
        e.node.setAttribute("content-desc", "a1")
        // Act, Assert
        assertThat(e.node.getAttribute("content-desc")).isEqualTo("a1")
    }

    @Test
    fun children() {

        // Arrange
        TestElementCache.loadXml(xmlData)
        val e = rootElement
        // Act
        val children = e.node!!.children()
        // Assert
        assertThat(children.count()).isEqualTo(7)
        assertThat(children.filter { it.nodeName == "item" }.count()).isEqualTo(3)
    }

}
