package shirates.core.utility.macos

import org.apache.logging.log4j.core.parser.ParseException
import org.json.JSONObject
import shirates.core.utility.getFloatOrNull
import shirates.core.utility.getStringOrNull
import shirates.core.utility.image.Rectangle

class RecognizeTextResult() {

    var imagePath = ""
    val items = mutableListOf<ListItem>()
    var original = ""

    constructor(jsonString: String) : this() {

        this.original = jsonString

        val jso = try {
            JSONObject(jsonString)
        } catch (t: Throwable) {
            throw IllegalArgumentException("Couldn't parse JSON object: \n$jsonString")
        }
        this.imagePath =
            jso.getStringOrNull("imagePath") ?: throw ParseException("RecognizeTextResult imagePath missing")
        if (jso.has("items").not()) {
            throw ParseException("RecognizeTextResult items missing")
        }
        val jsonList = jso.getJSONArray("items").map { it as JSONObject }
        for (jsonItem in jsonList) {
            val item = ListItem(
                text = jsonItem.getString("text"),
                confidence = jsonItem.getFloatOrNull("confidence")
                    ?: throw ParseException("RecognizeTextResult confidence missing"),
                rect = jsonItem.getString("rect")
            )
            this.items.add(item)
        }
        this.items.sortWith(compareByDescending<ListItem> { it.confidence }.thenBy { it.text })
    }

    class ListItem(
        val text: String,
        val confidence: Float,
        val rect: String
    ) {
        val rectangle: Rectangle
            get() {
                if (_Rectangle == null) {
                    _Rectangle = Rectangle(this.rect)
                }
                return _Rectangle!!
            }

        private var _Rectangle: Rectangle? = null

        override fun toString(): String {
            return "$confidence\t$text\t$rectangle"
        }
    }
}