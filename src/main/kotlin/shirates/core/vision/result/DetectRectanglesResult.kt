package shirates.core.vision.result

import okio.FileNotFoundException
import org.json.JSONObject
import shirates.core.exception.TestDriverException
import shirates.core.logging.TestLog
import shirates.core.utility.file.exists
import shirates.core.utility.image.BufferedImageUtility
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.drawGrid
import shirates.core.utility.image.drawRect
import java.awt.Color
import java.awt.image.BufferedImage

class DetectRectanglesResult(
    val inputFile: String,
    val jsonString: String
) {
    /**
     * rectangles
     */
    val rectangles: List<Rectangle>

    /**
     * inputImage
     */
    val inputImage: BufferedImage?
        get() {
            if (inputFile.exists().not()) {
                return null
            }
            return BufferedImageUtility.getBufferedImage(inputFile)
        }

    /**
     * outputImage
     */
    val outputImage: BufferedImage?
        get() {
            val image = draw()
            return image
        }

    init {
        val jsonObject = try {
            JSONObject(jsonString)
        } catch (t: Throwable) {
            val msg = "Error while parsing JSON results. $jsonString"
            TestLog.warn(msg)
            throw TestDriverException(msg, t)
        }

        var list = listOf<Rectangle>()
        if (jsonObject.has("rectangles")) {
            list = jsonObject.getJSONArray("rectangles").map {
                val o = (it as JSONObject)
                Rectangle(
                    left = o.getInt("x"),
                    top = o.getInt("y"),
                    width = o.getInt("width"),
                    height = o.getInt("height")
                )
            }
        }
        rectangles = list
    }

    /**
     * draw
     */
    fun draw(
        gridWidth: Int = 10,
        gridColor: Color = Color.GRAY,
        rectColor: Color = Color.RED,
    ): BufferedImage {
        if (inputFile.exists().not()) {
            throw FileNotFoundException("File not found. (inputFile=$inputFile)")
        }
        val image = inputImage!!
        image.drawGrid(
            gridWidth = gridWidth,
            gridColor = gridColor,
        )

        for (rect in rectangles) {
            image.drawRect(rect = rect, color = rectColor, stroke = 1f)
        }
        return image
    }
}