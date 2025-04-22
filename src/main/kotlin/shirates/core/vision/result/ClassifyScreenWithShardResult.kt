package shirates.core.vision.result

import org.json.JSONObject
import shirates.core.exception.TestDriverException
import shirates.core.logging.TestLog

class ClassifyScreenWithShardResult(
    val jsonString: String
) {
    val classifyScreenResults = mutableListOf<ClassifyScreenResult>()

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
            val shardID = if (jso.has("shardID")) jso.getInt("shardID") else 0
            val item = ClassifyScreenResult(jsonString = jso.toString(), shardID = shardID)
            classifyScreenResults.add(item)
        }
    }

    override fun toString(): String {
        return jsonString
    }

    /**
     * getCandidates
     */
    fun getCandidates(
        count: Int = 5
    ): List<ClassifyScreenResult.Classification> {

        val list = classifyScreenResults.flatMap { it.classifications }
            .sortedByDescending { it.confidence }.take(count).filter { it.confidence > 0.1 }
        return list
    }

}