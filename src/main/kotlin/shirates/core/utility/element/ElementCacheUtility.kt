package shirates.core.utility.element

import org.w3c.dom.Node
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.Bounds
import shirates.core.driver.TestElement
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.testContext
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.element.XPathUtility.children
import shirates.core.utility.getAttribute
import javax.xml.parsers.DocumentBuilderFactory

/**
 * ElementCacheUtility
 */
object ElementCacheUtility {

    /**
     * createTestElementFromXml
     */
    fun createTestElementFromXml(sourceXml: String): TestElement {

        TestLog.trace()

        var source = sourceXml
        source = source.replace("\u000b", "")    // vertical tab is not valid in XML
        if (PropertiesManager.xmlSourceRemovePattern.isNotBlank()) {
            val regex = PropertiesManager.xmlSourceRemovePattern.toRegex()
            source = source.replace(regex, "")
        }

        val xmlRemoved = source.replace("\r\n", "")
        val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val document = documentBuilder.parse(xmlRemoved.byteInputStream())
        val mode = testContext.platformName

        val list = XPathUtility.getNodesByXpath(node = document, expression = "//*")
        if (list.count() < 2) throw TestDriverException(
            message(id = "nodeForRootElementNotFound", arg1 = mode, arg2 = source)
        )
        val rootUINode = list[1]    // Skip <hierarchy> on Android/<AppiumAUT> on iOS

        val rootUIElement = TestElement(node = rootUINode)
        rootUIElement.rootUIElement = rootUIElement
        TestElementCache.sourceXml = source
        TestElementCache.hierarchyBounds = if (isAndroid) {
            val hierarchyNode = list[0]
            val width = hierarchyNode.getAttribute("width")
            val height = hierarchyNode.getAttribute("height")
            Bounds(width = width.toInt(), height = height.toInt())
        } else {
            rootUIElement.bounds
        }
        addChildren(element = rootUIElement, rootUIElement = rootUIElement)

        return rootUIElement
    }

    private fun addChildren(element: TestElement, rootUIElement: TestElement) {

        for (node in element.node!!.children()) {
            if (node.nodeType == Node.ELEMENT_NODE) {
                val e = TestElement(node = node, rootUIElement = rootUIElement)
                e.parentElement = element
                element.children.add(e)
                addChildren(element = e, rootUIElement = rootUIElement)
            }
        }
    }

}