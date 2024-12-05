package shirates.core.utility.image

import shirates.core.driver.Bounds
import java.awt.image.BufferedImage

class Segment(
    var left: Int = 0,
    var top: Int = 0,
    var width: Int,
    var height: Int,

    ) {

    val right: Int
        get() {
            if (width == 0) {
                return left
            }
            return left + width - 1
        }

    val bottom: Int
        get() {
            if (height == 0) {
                return top
            }
            return top + height - 1
        }

    val area: Int
        get() {
            return width * height
        }

    val aspectRatio: Float
        get() {
            return width.toFloat() / height.toFloat()
        }

    var savedSegmentImageFile: String? = null

    var segmentImage: BufferedImage? = null

    override fun toString(): String {
        return "[$left, $top, $right, $bottom](w=$width, h=$height, ratio=$aspectRatio)"
    }

    /**
     * canMerge
     */
    fun canMerge(segment: Segment, margin: Int): Boolean {

        val thisBounds = this.toBounds()
        val thatBounds = segment.toBounds()

        thatBounds.left = thatBounds.left - margin - 1
        if (thatBounds.left <= 0) thatBounds.left = 0

        thatBounds.top = thatBounds.top - margin - 1
        if (thatBounds.top <= 0) thatBounds.top = 0

        thatBounds.width += (margin + 1) * 2
        thatBounds.height += (margin + 1) * 2

        if (thisBounds.isOverlapping(thatBounds)) {
            return true
        }

        return false
    }

    /**
     * toRectangle
     */
    fun toRectangle(): Rectangle {

        return Rectangle(x = left, y = top, width = width, height = height)
    }

    /**
     * toBounds
     */
    fun toBounds(): Bounds {

        return Bounds(left = left, top = top, width = width, height = height)
    }
}