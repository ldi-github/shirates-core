package shirates.core.utility.image

import boofcv.gui.binary.VisualizeBinaryData
import boofcv.io.image.ConvertBufferedImage
import boofcv.struct.image.GrayF32
import boofcv.struct.image.GrayU8
import java.awt.image.BufferedImage

object BinarizationUtility {

    /**
     * getBinaryAsBufferedImage
     */
    fun getBinaryAsBufferedImage(
        image: BufferedImage,
        invert: Boolean = false,
        skinThickness: Int = 0
    ): BufferedImage {

        val binary = getBinaryAsGrayU8(image = image, invert = invert, skinThickness = skinThickness)
        val bufferedImage = BufferedImage(binary.width, binary.height, BufferedImage.TYPE_BYTE_GRAY)
        VisualizeBinaryData.renderBinary(binary, false, bufferedImage)
        return bufferedImage
    }

    /**
     * getBinaryAsGrayU8
     */
    fun getBinaryAsGrayU8(
        image: BufferedImage,
        invert: Boolean = false,
        skinThickness: Int = 0
    ): GrayU8 {

        val input = ConvertBufferedImage.convertFromSingle(image, null, GrayF32::class.java)
        val binary = GrayU8(input.width, input.height)

        val notSetValue = if (invert) 255 else 0
        val setValue = if (invert) 0 else 255

        var lastImageValue: Int? = null
        for (y in 0 until input.height) {
            for (x in 0 until input.width) {
                val v = image.getRGB(x, y)
                val bv = if (lastImageValue == null || v == lastImageValue)
                    notSetValue
                else
                    setValue
                binary.setWithSkinThickness(x, y, bv, skinThickness)
                lastImageValue = v
            }
        }
        for (x in 0 until input.width) {
            for (y in 0 until input.height) {
                val v = image.getRGB(x, y)
                if (v != lastImageValue) {
                    binary.setWithSkinThickness(x, y, setValue, skinThickness)
                }
                lastImageValue = v
            }
        }

        return binary
    }

    private fun GrayU8.setWithSkinThickness(x: Int, y: Int, value: Int, skinThickness: Int) {

        var left = x - skinThickness
        if (left < 0) left = 0

        var right = x + skinThickness
        if (right > this.width - 1) right = this.width - 1

        var top = y - skinThickness
        if (top < 0) top = 0

        var bottom = y + skinThickness
        if (bottom > this.height - 1) bottom = this.height - 1

        for (yy in top..bottom) {
            for (xx in left..right) {
                this.set(xx, yy, value)
            }
        }
    }

}