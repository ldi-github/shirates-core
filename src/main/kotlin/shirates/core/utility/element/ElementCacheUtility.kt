package shirates.core.utility.element

import org.w3c.dom.Node
import shirates.core.driver.TestElement
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.testContext
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.element.XPathUtility.children
import javax.xml.parsers.DocumentBuilderFactory

/**
 * ElementCacheUtility
 */
object ElementCacheUtility {

    /**
     * createTestElementFromXml
     */
    fun createTestElementFromXml(source: String): TestElement {

        TestLog.trace()

        val xmlRemoved = source.replace("\r\n", "")
        val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val document = documentBuilder.parse(xmlRemoved.byteInputStream())
        val rootNode: Node?
        val mode = testContext.platformName
        if (isAndroid) {
            val list = XPathUtility.getNodesByXpath(node = document, expression = "//*")
            rootNode = if (list.count() >= 2) list[1] else null
            if (rootNode == null) {
                throw TestDriverException(message(id = "nodeForRootElementNotFound", arg1 = mode, arg2 = source))
            }
        } else {
            rootNode =
                XPathUtility.getNodesByXpath(node = document, expression = "//XCUIElementTypeApplication[1]")
                    .firstOrNull()
            if (rootNode == null) {
                throw TestDriverException(message(id = "nodeForRootElementNotFound", arg1 = mode, arg2 = source))
            }
        }

        val rootElement = TestElement(node = rootNode)
        TestElementCache.sourceXml = source
        addChildren(element = rootElement)

        return rootElement
    }

    private fun addChildren(element: TestElement) {

        for (node in element.node!!.children()) {
            if (node.nodeType == Node.ELEMENT_NODE) {
                val e = TestElement(node = node)
                e.parentElement = element
                element.children.add(e)
                addChildren(e)
            }
        }
    }

}