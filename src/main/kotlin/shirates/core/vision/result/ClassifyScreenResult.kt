package shirates.core.vision.result

import org.json.JSONObject
import shirates.core.exception.TestDriverException
import shirates.core.logging.TestLog

class ClassifyScreenResult(
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
        val jsonObject = try {
            JSONObject(jsonString)
        } catch (t: Throwable) {
            val msg = "Error while parsing JSON results. $jsonString"
            TestLog.warn(msg)
            throw TestDriverException(msg, t)
        }
        if (jsonObject.has("error")) {
            val reason = jsonObject.getString("reason")
            if (reason.contains("File not found") && reason.contains(".mlmodel"))
                throw TestDriverException("mlmodel file not found. $reason")
        }

        val jsonArray = jsonObject.getJSONArray("candidates")
        for (i in 0 until jsonArray.length()) {
            val jso = jsonArray.getJSONObject(i)
            val identifier = jso.getString("identifier")
            val confidence = jso.getFloat("confidence")
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