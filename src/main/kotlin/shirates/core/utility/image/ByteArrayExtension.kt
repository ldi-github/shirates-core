package shirates.core.utility

import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

/**
 * toBufferedImage
 */
fun ByteArray.toBufferedImage(): BufferedImage {

    return ImageIO.read(ByteArrayInputStream(this))
}
