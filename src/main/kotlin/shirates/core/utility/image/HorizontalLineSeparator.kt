package shirates.core.utility.image

import boofcv.struct.image.GrayU8
import shirates.core.configuration.PropertiesManager
import java.awt.BasicStroke
import java.awt.Color
import java.awt.image.BufferedImage

class HorizontalLineSeparator(
    val containerImage: BufferedImage,
    val horizontalLineThreshold: Double = PropertiesManager.visionHorizontalLineThreshold,
) {
    val horizontalLines = mutableListOf<HorizontalLine>()

    class HorizontalLine(
        val image: BufferedImage,
        val binary: GrayU8,
        val y: Int,
    ) {
        val width: Int
            get() {
                return image.width
            }

        var trueCount = 0
        var falseCount = 0

        val trueRate: Double
            get() {
                if (width == 0) {
                    return 0.0
                }
                return trueCount / width.toDouble()
            }
        val falseRate: Double
            get() {
                if (width == 0) {
                    return 0.0
                }
                return falseCount / width.toDouble()
            }

        init {
            for (x in 0 until binary.width) {
                val value = binary.get(x, y)
                if (value == 0) {
                    falseCount++
                } else {
                    trueCount++
                }
            }
        }

        override fun toString(): String {
            return "y=$y, trueRate=$trueRate, falseRate=$falseRate, trueCount=$trueCount, falseCount=$falseCount, width=$width"
        }
    }

    /**
     * split
     */
    fun split(
        inverse: Boolean = false,
    ): HorizontalLineSeparator {

        val segmentContainer = SegmentContainer(
            containerImage = containerImage,
            segmentMarginHorizontal = PropertiesManager.segmentMarginHorizontal,
            segmentMarginVertical = PropertiesManager.segmentMarginVertical,
        ).split(splitUnit = 1, segmentationPng = false)

        segmentContainer.drawOriginalSegments()

        val horizontalLines = mutableListOf<HorizontalLine>()
        for (y in 0 until containerImage.height) {
            val line = HorizontalLine(image = containerImage, binary = segmentContainer.binary!!, y = y)
            horizontalLines.add(line)
        }

        val lines =
            if (inverse) horizontalLines.filter { this.horizontalLineThreshold <= it.falseRate }
            else horizontalLines.filter { this.horizontalLineThreshold <= it.trueRate }
        this.horizontalLines.clear()
        this.horizontalLines.addAll(lines)

        return this
    }

    /**
     * draw
     */
    fun draw(
        color: Color = Color.BLACK,
        stroke: Float = 1f,
    ): HorizontalLineSeparator {

        val g2d = containerImage.createGraphics()
        g2d.color = color
        g2d.stroke = BasicStroke(stroke)

        for (line in horizontalLines) {
            g2d.drawLine(0, line.y, containerImage.right, line.y)
        }
        return this
    }

}