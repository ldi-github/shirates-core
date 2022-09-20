package shirates.core.utility

import org.w3c.dom.Node

/**
 * setAttribute
 */
fun Node?.setAttribute(name: String, value: String) {

    if (this == null) {
        throw IllegalAccessError("node is not set.")
    }

    for (i in 0..this.attributes.length - 1) {
        val attrNode = this.attributes.item(i)!!
        if (attrNode.nodeName == name) {
            attrNode.nodeValue = value
            return
        }
    }
    val node = this.ownerDocument.createAttribute(name)
    node.value = value
    this.attributes.setNamedItem(node)

}

/**
 * getAttribute
 */
fun Node?.getAttribute(name: String): String {

    if (this == null) {
        return ""
    }

    for (i in 0..this.attributes.length - 1) {
        val attrNode = this.attributes.item(i)!!
        if (attrNode.nodeName == name) {
            return attrNode.nodeValue
        }
    }
    return ""
}

/**
 * hasAttribute
 */
fun Node?.hasAttribute(name: String): Boolean {

    if (this == null) {
        return false
    }

    if (this.hasAttributes().not()) {
        return false
    }
    for (i in 0..this.attributes.length - 1) {
        val attrNode = this.attributes.item(i)!!
        if (attrNode.nodeName == name) {
            return true
        }
    }
    return false
}

/**
 * children
 */
fun Node?.children(): List<Node> {

    val children = mutableListOf<Node>()
    if (this == null) {
        return children
    }

    for (i in 0..this.childNodes.length - 1) {
        children.add(this.childNodes.item(i))
    }
    return children
}
