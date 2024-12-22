package shirates.core.vision

import org.json.JSONObject
import shirates.core.exception.TestDriverException
import shirates.core.logging.CodeExecutionContext
import shirates.core.utility.image.BufferedImageUtility
import shirates.core.utility.image.Rectangle
import java.awt.image.BufferedImage

class RecognizeTextParser(
    val language: String?,
    val jsonString: String,

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
            JSONObject(jsonString)
        } catch (t: Throwable) {
            throw TestDriverException("The content could not be parsed as JSONObject. \n$jsonString")
        }
        val candidatesArray = try {
            jsonObject.getJSONArray("candidates")
        } catch (t: Throwable) {
            throw TestDriverException("\"candidates\" not found. \n$jsonString")
        }

        val screenshotImage = screenshotImage ?: BufferedImageUtility.getBufferedImage(filePath = screenshotFile!!)
        val localRegionImage = localRegionImage ?: screenshotImage

        val list = mutableListOf<RecognizeTextObservation>()
        for (json in candidatesArray) {
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
                jsonString = jsonString,
                language = language,

                screenshotFile = screenshotFile,
                screenshotImage = screenshotImage,

                localRegionFile = localRegionFile,
                localRegionImage = localRegionImage,

                localRegionX = localRegionX,
                localRegionY = localRegionY,
                rectOnLocalRegion = rect,
            )
            list.add(observation)
        }

        return list
    }
}