package shirates.core.utility.macos

import org.apache.logging.log4j.core.parser.ParseException
import org.json.JSONObject
import shirates.core.utility.getFloatOrNull
import shirates.core.utility.getIntOrNull
import shirates.core.utility.getStringOrNull
import shirates.core.utility.image.Rectangle

class DetectRectanglesResult() {

    var imagePath = ""
    val items = mutableListOf<ListItem>()
    var original = ""
    var maximumObservations: Int? = null
    var minimumSize: Float? = null
    var minimumAspectRatio: Float? = null
    var maximumAspectRatio: Float? = null

    constructor(jsonString: String) : this() {
        this.original = jsonString

        val jso = try {
            JSONObject(jsonString)
        } catch (t: Throwable) {
            throw IllegalArgumentException("Couldn't parse JSON object: \n$jsonString")
        }
        this.imagePath =
            jso.getStringOrNull("imagePath") ?: throw ParseException("DetectRectanglesResult imagePath missing")
        this.maximumObservations = jso.getIntOrNull("maximumObservations")
        this.minimumSize = jso.getFloatOrNull("minimumSize")
        this.minimumAspectRatio = jso.getFloatOrNull("minimumAspectRatio")
        this.maximumAspectRatio = jso.getFloatOrNull("maximumAspectRatio")

        if (jso.has("items").not()) {
            throw ParseException("DetectRectanglesResult items missing")
        }
        val jsonList = jso.getJSONArray("items").map { it as JSONObject }
        for (jsonItem in jsonList) {
            val item = ListItem(
                confidence = jsonItem.getFloatOrNull("confidence")
                    ?: throw ParseException("DetectRectanglesResult confidence is not able to be cast as float. ($)"),
                rect = jsonItem.getString("rect")
            )
            this.items.add(item)
        }
        this.items.sortBy { it.confidence }
    }

    class ListItem(
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
            return "$confidence\t$rectangle"
        }
    }
}