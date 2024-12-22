package shirates.core.vision

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

        val jsonObject = try {
            JSONObject(content)
        } catch (t: Throwable) {
            throw TestDriverException("The content could not be parsed as JSONObject. \n$content")
        }
        val jsonArray = try {
            jsonObject.getJSONArray("candidates")
        } catch (t: Throwable) {
            throw TestDriverException("\"candidates\" not found. \n$content")
        }

        val list = mutableListOf<RecognizeTextObservation>()
        for (json in jsonArray) {
            val jso = try {
                json as JSONObject
            } catch (t: Throwable) {
                throw TestDriverException("The content could not be parsed as JSONObject. \n$json", cause = t)
            }
            if (jso.has("text").not()) {
                throw TestDriverException("text not found. \n$json")
            }
            val text = jso.getString("text")

            if (jso.has("rect").not()) {
                throw TestDriverException("rect not found. \n$json")
            }
            val rectObj = jso.getJSONObject("rect")

            val scale = 1.0

            val left = (rectObj.getInt("x") / scale).toInt()
            val top = (rectObj.getInt("y") / scale).toInt()
            val width = (rectObj.getInt("width") / scale).toInt()
            val height = (rectObj.getInt("height") / scale).toInt()
            val rect = Rectangle(x = left, y = top, width = width, height = height)

            if (jso.has("confidence").not()) {
                throw TestDriverException("confidence not found. \n$json")
            }

            val confidence = try {
                jso.getFloat("confidence")
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