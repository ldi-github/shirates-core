package shirates.core.utility.image

import boofcv.struct.image.GrayU8
import shirates.core.configuration.PropertiesManager
import java.awt.Color
import java.awt.image.BufferedImage

class HorizontalLineSeparator(
    val containerImage: BufferedImage,
    val lineThreshold: Double = PropertiesManager.visionLineThreshold,
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

        for (y in 0 until containerImage.height) {
            val line = HorizontalLine(image = containerImage, binary = segmentContainer.binary!!, y = y)
            horizontalLines.add(line)
        }

        val lines =
            if (inverse) horizontalLines.filter { this.lineThreshold <= it.falseRate }
            else horizontalLines.filter { this.lineThreshold <= it.trueRate }
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

        for (line in horizontalLines) {
            val rect = Rectangle(left = 0, top = line.y, width = line.width, height = 1)
            containerImage.drawRect(rect = rect, color = color, stroke = stroke)
        }
        return this
    }

}