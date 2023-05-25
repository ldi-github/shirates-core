package shirates.core.utility.element

import org.w3c.dom.Node
import org.w3c.dom.NodeList
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

/**
 * XPathUtility
 */
object XPathUtility {

    /**
     * xpath
     */
    val xpath = XPathFactory.newInstance().newXPath()

    /**
     * getNodesByXpath
     */
    fun getNodesByXpath(node: Node?, expression: String, nodeType: Short? = null): List<Node> {

        if (node == null) {
            return mutableListOf()
        }
        val nodeList = try {
            xpath.compile(expression).evaluate(node, XPathConstants.NODESET) as NodeList
        } catch (t: Throwable) {
            throw Exception("$t expression=$expression")
        }
        val nodes = mutableListOf<Node>()
        for (i in 0 until nodeList.length) {
            val item = nodeList.item(i)
            nodes.add(item)
        }

        return if (nodeType == null)
            nodes
        else
            nodes.filter { it.nodeType == nodeType }.toMutableList()
    }

    /**
     * children
     */
    fun Node.children(): List<Node> {

        val children = mutableListOf<Node>()
        for (i in 0 until this.childNodes.length) {
            children.add(this.childNodes.item(i))
        }
        return children
    }

    /**
     * getQuotedText
     */
    fun getQuotedText(text: String): String {

        if (text.contains("'").not()) {
            return "'$text'"
        }
        if (text.contains("\"").not()) {
            return "\"$text\""
        }
        val temp = text.replace("'", "__'__").replace("\"", "__\"__")
        val tokens = temp.split("__")
        val list = mutableListOf<String>()
        for (token in tokens) {
            if (token.contains("'")) {
                list.add("\"$token\"")
            } else if (token.contains("\"")) {
                list.add("'$token'")
            } else {
                list.add("'$token'")
            }
        }
        return "concat(${list.joinToString(",")})"
    }
}