package shirates.core.vision

import org.json.JSONObject
import shirates.core.exception.TestDriverException

object ScreenRecognizer {

    /**
     * recognizeScreen
     */
    fun recognizeScreen(
        screenImageFile: String,
        withTextMatching: Boolean = false,
        maxDistance: Double = 0.5,
    ): String {

        val jsonString = SrvisionProxy.classifyWithImageFeaturePrintOrText(
            inputFile = screenImageFile,
            withTextMatching = withTextMatching,
            log = true,
        )
        val resultObject = try {
            JSONObject(jsonString)
        } catch (t: Throwable) {
            throw TestDriverException("Could not parse json.\n$jsonString", cause = t)
        }
        if (resultObject.has("entries").not()) {
            return "?"
        }
        val entriesArray = resultObject.getJSONArray("entries")
        if (entriesArray.isEmpty) {
            return "?"
        }
        val firstEntryObject = entriesArray.getJSONObject(0)
        if (firstEntryObject.has("distance").not() || firstEntryObject.has("name").not()) {
            return "?"
        }
        val distance = firstEntryObject.getDouble("distance")
        val screenName = firstEntryObject.getString("name")
        if (distance > maxDistance) {
            return "?"
        }
        return screenName
    }

}