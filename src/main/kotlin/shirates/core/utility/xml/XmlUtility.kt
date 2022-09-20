package shirates.core.utility.xml

import org.w3c.dom.Document
import java.io.StringWriter
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

/**
 * XmlUtility
 */
object XmlUtility {

    /**
     * getXmlString
     */
    fun getXmlString(document: Document): String {

        val transformer = TransformerFactory.newInstance().newTransformer()
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
        val result = StreamResult(StringWriter())
        val source = DOMSource(document)
        transformer.transform(source, result)
        val xmlString = result.writer.toString()
        val header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        return """
$header
$xmlString
            """.trimIndent()
    }

}
