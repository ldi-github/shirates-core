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
     * getRGBCountMap
     */
    fun getRGBCountMap(image: BufferedImage): Map<Int, Int> {

        val map = mutableMapOf<Int, Int>()
        for (y in 0 until image.height) {
            for (x in 0 until image.width) {
                val value = image.getRGB(x, y)
                if (map.containsKey(value)) {
                    val v = map[value]!!
                    map[value] = v + 1
                } else {
                    map[value] = 1
                }
            }
        }

        return map
    }

    /**
     * getLargestShare
     */
    fun getLargestShare(image: BufferedImage): Double {
        val map = getRGBCountMap(image)

        val max = map.maxBy { it.value }
        val maxCount = max.value
        val pixelCount = image.width * image.height
        val share = maxCount.toDouble() / pixelCount
        return share
    }

    /**
     * isBlackout
     */
    fun isBlackout(image: BufferedImage, threshold: Double = PropertiesManager.screenshotBlackoutThreshold): Boolean {

        val share = getLargestShare(image)
        val result = share > threshold
        return result
    }

}