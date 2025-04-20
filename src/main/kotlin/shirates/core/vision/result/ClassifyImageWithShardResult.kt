package shirates.core.vision.result

import org.json.JSONObject
import shirates.core.exception.TestDriverException
import shirates.core.logging.TestLog

class ClassifyImageWithShardResult(
    val jsonString: String
) {
    val classifyImageResults = mutableListOf<ClassifyImageResult>()

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

        val jsonArray = jsonObject.getJSONArray("items")
        for (i in 0 until jsonArray.length()) {
            val jso = jsonArray.getJSONObject(i)
            val item = ClassifyImageResult(jso.toString())
            classifyImageResults.add(item)
        }
    }

    override fun toString(): String {
        return jsonString
    }
}