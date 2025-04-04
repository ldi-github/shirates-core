package shirates.core.vision.result

import org.json.JSONObject
import shirates.core.exception.TestDriverException
import shirates.core.logging.TestLog

class DistanceResult(
    val jsonString: String
) {
    val distance: Double
    val imageFile1: String
    val imageFile2: String

    init {
        val jsonObject = try {
            JSONObject(jsonString)
        } catch (t: Throwable) {
            val msg = "Error while parsing JSON results. $jsonString"
            TestLog.warn(msg)
            throw TestDriverException(msg, t)
        }

        distance = jsonObject.getDouble("distance")
        imageFile1 = jsonObject.getString("imageFile1")
        imageFile2 = jsonObject.getString("imageFile2")
    }

    override fun toString(): String {
        return jsonString
    }
}