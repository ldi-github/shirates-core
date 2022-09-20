package shirates.core.unittest.utility.element

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.w3c.dom.Node
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataAndroid
import shirates.core.utility.element.XPathUtility
import javax.xml.parsers.DocumentBuilderFactory

class XpathUtilityTest : UnitTest() {

    @Test
    fun getNodesByXpath() {

        val sourceText = XmlDataAndroid.Android1
        val xmlRemoved = sourceText.replace("\r\n", "")
        val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val document = documentBuilder.parse(xmlRemoved.byteInputStream())

        run {
            val xpath = "//android.widget.FrameLayout[@resource-id='jp.co.app.android:id/navHostFragment']"
            val list = XPathUtility.getNodesByXpath(null, expression = xpath)
            assertThat(list.count()).isEqualTo(0)
        }
        run {
            val xpath = "//android.widget.FrameLayout[@resource-id='jp.co.app.android:id/navHostFragment']"
            val list = XPathUtility.getNodesByXpath(document, expression = xpath)
            assertThat(list.count()).isEqualTo(1)
        }
        run {
            val xpath = "//android.widget.TextView"
            val list = XPathUtility.getNodesByXpath(document, expression = xpath)
            for (n in list) {
                println("${n.nodeName}, nodeType=${n.nodeType}")
                assertThat(n.nodeName).isEqualTo("android.widget.TextView")
            }
        }
        run {
            val xpath = "//android.widget.TextView"
            val list = XPathUtility.getNodesByXpath(document, expression = xpath, nodeType = Node.ELEMENT_NODE)
            assertThat(list.count()).isEqualTo(9)
            for (n in list) {
                println("${n.nodeName}, nodeType=${n.nodeType}")
                assertThat(n.nodeName).isEqualTo("android.widget.TextView")
            }
        }
        run {
            val xpath = "//*"
            val list = XPathUtility.getNodesByXpath(document, expression = xpath, Node.COMMENT_NODE)
            assertThat(list.count()).isEqualTo(0)
        }
    }

    @Test
    fun getQuotedText() {

        run {
            // Arrange, Act
            val actual = XPathUtility.getQuotedText("a")
            // Assert
            assertThat(actual).isEqualTo("'a'")
        }

        run {
            // Arrange, Act
            val actual = XPathUtility.getQuotedText("It's mine.")
            // Assert
            assertThat(actual).isEqualTo("\"It's mine.\"")
        }

        run {
            // Arrange, Act
            val actual = XPathUtility.getQuotedText("That is \"hoge\".")
            // Assert
            assertThat(actual).isEqualTo("'That is \"hoge\".'")
        }

        run {
            // Arrange, Act
            val actual = XPathUtility.getQuotedText("That's \"fuga\".")
            // Assert
            assertThat(actual).isEqualTo("concat('That',\"'\",'s ','\"','fuga','\"','.')")
        }
    }
}