package shirates.core.utility.image

import boofcv.struct.image.GrayU8
import shirates.core.configuration.PropertiesManager
import java.awt.BasicStroke
import java.awt.Color
import java.awt.image.BufferedImage

class VerticalLineSeparator(
    val containerImage: BufferedImage,
    val verticalLineThreshold: Double = PropertiesManager.visionVerticalLineThreshold,
) {
    val verticalLines = mutableListOf<VerticalLine>()

    class VerticalLine(
        val image: BufferedImage,
        val binary: GrayU8,
        val x: Int,
    ) {
        val height: Int
            get() {
                return image.height
            }

        var trueCount = 0
        var falseCount = 0

        val trueRate: Double
            get() {
                if (height == 0) {
                    return 0.0
                }
                return trueCount / height.toDouble()
            }
        val falseRate: Double
            get() {
                if (height == 0) {
                    return 0.0
                }
                return falseCount / height.toDouble()
            }

        init {
            for (y in 0 until binary.height) {
                val value = binary.get(x, y)
                if (value == 0) {
                    falseCount++
                } else {
                    trueCount++
                }
            }
        }

        override fun toString(): String {
            return "x=$x, trueRate=$trueRate, falseRate=$falseRate, trueCount=$trueCount, falseCount=$falseCount, height=$height"
        }
    }

    /**
     * split
     */
    fun split(
        inverse: Boolean = false,
    ): VerticalLineSeparator {

        val segmentContainer = SegmentContainer(
            containerImage = containerImage,
            segmentMarginHorizontal = PropertiesManager.segmentMarginHorizontal,
            segmentMarginVertical = PropertiesManager.segmentMarginVertical,
        ).split(splitUnit = 1, segmentationPng = false)

        segmentContainer.drawOriginalSegments()

        val verticalLines = mutableListOf<VerticalLine>()
        for (x in 0 until containerImage.width) {
            val verticalLine = VerticalLine(image = containerImage, binary = segmentContainer.binary!!, x = x)
            verticalLines.add(verticalLine)
        }

        val lines =
            if (inverse) verticalLines.filter { this.verticalLineThreshold <= it.falseRate }
            else verticalLines.filter { this.verticalLineThreshold <= it.trueRate }
        this.verticalLines.clear()
        this.verticalLines.addAll(lines)

        return this
    }

    /**
     * draw
     */
    fun draw(
        color: Color = Color.BLACK,
        stroke: Float = 3f,
    ): VerticalLineSeparator {

        val g2d = containerImage.createGraphics()
        g2d.color = color
        g2d.stroke = BasicStroke(stroke)

        for (line in verticalLines) {
            val offset = 1
            g2d.drawLine(line.x + offset, 0, line.x + offset, containerImage.bottom)
        }
        return this
    }

}