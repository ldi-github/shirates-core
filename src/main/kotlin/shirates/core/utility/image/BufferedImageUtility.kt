package shirates.core.utility.image

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

}