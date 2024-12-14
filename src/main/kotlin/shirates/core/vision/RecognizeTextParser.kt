package shirates.core.vision

import org.json.JSONArray
import org.json.JSONObject
import shirates.core.exception.TestDriverException
import shirates.core.logging.CodeExecutionContext
import shirates.core.utility.image.Rectangle
import java.awt.image.BufferedImage

class RecognizeTextParser(
    val content: String,

    val screenshotFile: String? = CodeExecutionContext.lastScreenshotFile,
    val screenshotImage: BufferedImage? = CodeExecutionContext.lastScreenshotImage,

    val localRegionFile: String? = null,
    val localRegionImage: BufferedImage? = null,

    val localRegionX: Int,
    val localRegionY: Int,
) {

    /**
     * parse
     */
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

            val scale = 1.0

            val left = (values[0].toInt() / scale).toInt()
            val top = (values[1].toInt() / scale).toInt()
            val width = (values[2].toInt() / scale).toInt()
            val height = (values[3].toInt() / scale).toInt()
            val rect = Rectangle(x = left, y = top, width = width, height = height)

            if (jsonObject.has("confidence").not()) {
                throw TestDriverException("confidence not found. \n$json")
            }

            val confidence = try {
                jsonObject.getFloat("confidence")
            } catch (t: Throwable) {
                throw TestDriverException("Could not parse confidence. \n$json", cause = t)
            }


            val observation = RecognizeTextObservation(
                text = text,
                confidence = confidence,
                jsonString = content,

                screenshotFile = screenshotFile,
                screenshotImage = screenshotImage,

                localRegionFile = localRegionFile,
                localRegionImage = localRegionImage,

                localRegionX = localRegionX,
                localRegionY = localRegionY,
                rectOnLocalRegionImage = rect,
            )
            list.add(observation)
        }

        return list
    }
}