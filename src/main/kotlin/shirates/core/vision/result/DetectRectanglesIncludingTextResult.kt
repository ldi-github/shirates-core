package shirates.core.vision.result

import org.json.JSONObject
import shirates.core.exception.TestDriverException
import shirates.core.utility.image.Rectangle

class DetectRectanglesIncludingTextResult(
    val jsonString: String,
) {
    val text: String
    val textRectangle: Rectangle
    val rectangles: List<Rectangle>

    init {
        val jso = try {
            JSONObject(jsonString)
        } catch (t: Throwable) {
            throw TestDriverException("Failed to parse JSON object: \n$jsonString", t)
        }
        text = jso.getString("text")
        val tr = jso.getJSONObject("textRectangle")
        textRectangle = Rectangle(
            tr.getInt("x"),
            tr.getInt("y"),
            tr.getInt("width"),
            tr.getInt("height")
        )
        rectangles = jso.getJSONArray("rectangles")
            .map { it as JSONObject }.map {
                Rectangle(
                    x = it.getInt("x"),
                    y = it.getInt("y"),
                    width = it.getInt("width"),
                    height = it.getInt("height")
                )
            }
    }

    /**
     * firstCandidate
     */
    fun firstCandidate(): Rectangle {

        return rectangles.firstOrNull() ?: Rectangle()
    }
}