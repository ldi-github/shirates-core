package shirates.core.vision.result

import org.json.JSONObject
import shirates.core.exception.TestDriverException
import shirates.core.utility.image.Rectangle
import shirates.core.utility.string.replaceWithRegisteredWord

class RecognizeTextResult(
    var inputFile: String,
    var language: String,
    val jsonString: String,
) {
    var input: String? = null
    var candidates = listOf<Candidate>()

    var errorMessage: String? = null

    init {
        val jsonObject = try {
            JSONObject(jsonString)
        } catch (t: Throwable) {
            throw TestDriverException("Couldn't parse json string.\n$jsonString", t)
        }

        if (jsonObject.has("input")) {
            this.input = jsonObject.getString("input")
        }
        if (jsonObject.has("candidates")) {
            this.candidates = jsonObject.getJSONArray("candidates").map {
                val jso = it as JSONObject
                val rectObj = jso.getJSONObject("rect")
                val rect = Rectangle(
                    left = rectObj.getInt("x"),
                    top = rectObj.getInt("y"),
                    width = rectObj.getInt("width"),
                    height = rectObj.getInt("height")
                )
                val oldText = jso.getString("text")
                val newText = oldText.replaceWithRegisteredWord()
                Candidate(
                    confidence = jso.getFloat("confidence"),
                    text = newText,
                    rect = rect
                )
            }
        }

        if (jsonObject.has("reason")) {
            errorMessage = jsonObject.getString("reason")
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