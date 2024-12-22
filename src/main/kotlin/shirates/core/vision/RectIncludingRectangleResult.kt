package shirates.core.vision

import org.json.JSONObject
import shirates.core.exception.TestDriverException
import shirates.core.utility.image.Rectangle

class RectIncludingRectangleResult(
    val jsonString: String,
) {
    val baseRectangle: Rectangle
    val rectangles: List<Rectangle>

    init {
        val jso = try {
            JSONObject(jsonString)
        } catch (t: Throwable) {
            throw TestDriverException("Failed to parse JSON object: \n$jsonString", t)
        }
        val tr = jso.getJSONObject("baseRectangle")
        baseRectangle = Rectangle(
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