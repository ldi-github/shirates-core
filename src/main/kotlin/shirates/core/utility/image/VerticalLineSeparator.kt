package shirates.core.utility.image

import boofcv.struct.image.GrayU8
import shirates.core.configuration.PropertiesManager
import java.awt.Color
import java.awt.image.BufferedImage

class VerticalLineSeparator(
    val containerImage: BufferedImage,
    val lineThreshold: Double = PropertiesManager.visionLineThreshold,
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

        for (x in 0 until containerImage.width) {
            val verticalLine = VerticalLine(image = containerImage, binary = segmentContainer.binary!!, x = x)
            verticalLines.add(verticalLine)
        }

        val lines =
            if (inverse) verticalLines.filter { this.lineThreshold <= it.falseRate }
            else verticalLines.filter { this.lineThreshold <= it.trueRate }
        this.verticalLines.clear()
        this.verticalLines.addAll(lines)

        return this
    }

    /**
     * draw
     */
    fun draw(
        color: Color = Color.BLACK,
        stroke: Float = 1f,
    ): VerticalLineSeparator {

        for (line in verticalLines) {
            val rect = Rectangle(left = line.x + 1, top = 0, width = 1, height = line.height)
            containerImage.drawRect(rect = rect, color = color, stroke = stroke)
        }
        return this
    }

}