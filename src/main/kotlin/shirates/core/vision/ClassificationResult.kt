package shirates.core.vision

import org.json.JSONArray
import shirates.core.exception.TestDriverException

class ClassificationResult(
    val jsonString: String
) {
    var classifications = mutableListOf<Classification>()

    /**
     * primaryClassification
     */
    val primaryClassification: Classification
        get() {
            return classifications.first()
        }

    init {
        val jsonArray = try {
            JSONArray(jsonString)
        } catch (t: Throwable) {
            throw TestDriverException("Error while parsing JSON results. $jsonString", t)
        }
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val identifier = jsonObject.getString("identifier")
            val confidence = jsonObject.getFloat("confidence")
            val classification = Classification(identifier = identifier, confidence = confidence)
            classifications.add(classification)
        }
    }

    override fun toString(): String {
        return jsonString
    }

    class Classification(
        val identifier: String,
        val confidence: Float,
    ) {
        override fun toString(): String {
            return "identifier $identifier, confidence $confidence"
        }
    }
}