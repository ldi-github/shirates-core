package shirates.core.utility.image

import boofcv.io.image.ConvertBufferedImage
import boofcv.struct.image.GrayF32
import java.awt.image.BufferedImage

/**
 * toBufferedImage
 */
fun GrayF32?.toBufferedImage(): BufferedImage? {

    return ConvertBufferedImage.convertTo(this, null as BufferedImage?)
}