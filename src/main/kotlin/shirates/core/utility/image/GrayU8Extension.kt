package shirates.core.utility.image

import boofcv.io.image.ConvertBufferedImage
import boofcv.struct.image.GrayU8
import java.awt.image.BufferedImage

/**
 * toBufferedImage
 */
fun GrayU8?.toBufferedImage(): BufferedImage? {

    return ConvertBufferedImage.convertTo(this, null as BufferedImage?)
}