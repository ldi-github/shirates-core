package shirates.core.utility.image

import shirates.core.configuration.PropertiesManager
import shirates.core.utility.toPath
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Files
import javax.imageio.ImageIO

object BufferedImageUtility {

    class ColorShare(
        val color: Int,
        var colorCount: Int,
        val imageWidth: Int,
        val imageHeight: Int,
    ) {
        val pixelCount: Int
            get() {
                return imageWidth * imageHeight
            }
        val colorShare: Double
            get() {
                return colorCount.toDouble() / pixelCount
            }
    }

    /**
     * getBufferedImage
     */
    fun getBufferedImage(filePath: String): BufferedImage {

        if (Files.exists(filePath.toPath()).not()) {
            throw FileNotFoundException(filePath)
        }

        val file = File(filePath)
        val image = ImageIO.read(ByteArrayInputStream(file.readBytes()))
        return image
    }

    /**
     * getColorShareMap
     */
    fun getColorShareMap(image: BufferedImage): Map<Int, ColorShare> {

        val map = mutableMapOf<Int, ColorShare>()
        for (y in 0 until image.height) {
            for (x in 0 until image.width) {
                val color = image.getRGB(x, y)
                if (map.containsKey(color)) {
                    val colorShare = map[color]!!
                    colorShare.colorCount += 1
                } else {
                    map[color] =
                        ColorShare(color = color, colorCount = 1, imageWidth = image.width, imageHeight = image.height)
                }
            }
        }

        return map
    }

    /**
     * getLargestColorShare
     */
    fun getLargestColorShare(image: BufferedImage): ColorShare {

        val map = getColorShareMap(image)
        val max = map.maxBy { it.value.colorShare }
        return max.value
    }

    /**
     * isBlackout
     */
    fun isBlackout(image: BufferedImage, threshold: Double = PropertiesManager.screenshotBlackoutThreshold): Boolean {

        val largestColorShare = getLargestColorShare(image = image)
        val result = largestColorShare.colorShare > threshold && largestColorShare.color == -16777216
        return result
    }

}