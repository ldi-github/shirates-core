package shirates.core.vision

import org.json.JSONArray
import org.json.JSONObject
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.Bounds
import shirates.core.driver.TestMode.isiOS
import shirates.core.exception.TestDriverException

class RecognizeTextParser(
    val content: String
) {
    fun parse(): List<RecognizeTextObservation> {

        val jsonArray = try {
            JSONArray(content)
        } catch (t: Throwable) {
            throw TestDriverException("The content could not be parsed as JSONArray. \n$content")
        }

        val list = mutableListOf<RecognizeTextObservation>()
        for (json in jsonArray) {
            val jsonObject = try {
                json as JSONObject
            } catch (t: Throwable) {
                throw TestDriverException("The content could not be parsed as JSONObject. \n$json", cause = t)
            }
            if (jsonObject.has("text").not()) {
                throw TestDriverException("text not found. \n$json")
            }
            val text = jsonObject.getString("text")

            if (jsonObject.has("rect").not()) {
                throw TestDriverException("rect not found. \n$json")
            }
            val values = jsonObject.getString("rect").split(",").map { it.trim() }
            if (values.count() != 4) {
                throw TestDriverException("Could not parse rect. `left, top, with, height` is expected.")
            }

            val scale = PropertiesManager.screenshotScale
            val ratio = if (isiOS) 3 else 1
            val rect = Bounds()
            rect.left = (values[0].toInt() / scale / ratio).toInt()
            rect.top = (values[1].toInt() / scale / ratio).toInt()
            rect.width = (values[2].toInt() / scale / ratio).toInt()
            rect.height = (values[3].toInt() / scale / ratio).toInt()

            if (jsonObject.has("confidence").not()) {
                throw TestDriverException("confidence not found. \n$json")
            }

            val confidence = try {
                jsonObject.getFloat("confidence")
            } catch (t: Throwable) {
                throw TestDriverException("Could not parse confidence. \n$json", cause = t)
            }

            list.add(RecognizeTextObservation(text = text, rect = rect, confidence = confidence))
        }

        return list
    }
}