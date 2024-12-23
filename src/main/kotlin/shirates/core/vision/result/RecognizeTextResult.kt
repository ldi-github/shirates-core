package shirates.core.vision.result

import org.json.JSONObject
import shirates.core.exception.TestDriverException
import shirates.core.utility.image.Rectangle

class RecognizeTextResult(
    val jsonString: String
) {
    var input: String? = null
    var candidates: List<Candidate>

    init {
        val jsonObject = try {
            JSONObject(jsonString)
        } catch (t: Throwable) {
            throw TestDriverException("Couldn't parse json string.\n$jsonString", t)
        }

        this.input = jsonObject.getString("input")
        this.candidates = jsonObject.getJSONArray("candidates").map {
            val jso = it as JSONObject
            val rectObj = jso.getJSONObject("rect")
            val rect = Rectangle(
                x = rectObj.getInt("x"),
                y = rectObj.getInt("y"),
                width = rectObj.getInt("width"),
                height = rectObj.getInt("height")
            )
            Candidate(
                confidence = jso.getFloat("confidence"),
                text = jso.getString("text"),
                rect = rect
            )
        }
    }

    class Candidate(
        val confidence: Float,
        val text: String,
        val rect: Rectangle,
    ) {
        override fun toString(): String {
            return "$confidence, $text, $rect"
        }
    }
}