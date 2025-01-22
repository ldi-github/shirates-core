package shirates.core.vision.result

import org.json.JSONObject
import shirates.core.exception.TestDriverException

class SetupImageFeaturePrintConfigResult(
    val jsonString: String
) {
    val message: String

    init {
        val jsonObject = try {
            JSONObject(jsonString)
        } catch (t: Throwable) {
            throw TestDriverException("Couldn't parse json string.\n$jsonString", t)
        }

        this.message = jsonObject.getString("message")
    }
}