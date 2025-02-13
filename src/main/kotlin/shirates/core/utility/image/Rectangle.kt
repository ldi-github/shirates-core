package shirates.core.utility.image

import shirates.core.driver.Bounds
import shirates.core.driver.testContext
import shirates.core.vision.VisionElement

class Rectangle(
    override var left: Int = 0,
    override var top: Int = 0,
    override var width: Int = 0,
    override var height: Int = 0,
) : RectangleInterface {

    val isEmpty: Boolean
        get() {
            return area == 0
        }

    constructor(leftTopRightBottom: String) : this() {

        try {
            val r = leftTopRightBottom.removePrefix("[").split("]").first().split(",").map { it.trim().toInt() }
            this.left = r[0]
            this.top = r[1]
            this.width = r[2] - left + 1
            this.height = r[3] - top + 1
        } catch (t: Throwable) {
            throw IllegalArgumentException(leftTopRightBottom, t)
        }
    }

    companion object {

        fun createFrom(left: Int, top: Int, right: Int, bottom: Int): Rectangle {
            val width = right - left + 1
            val height = bottom - top + 1
            return Rectangle(left = left, top = top, width = width, height = height)
        }
    }

    fun trimBy(trimObject: TrimObject): Rectangle {

        return trimObject.trim(this)
    }

    /**
     * isSame
     */
    fun isSame(other: Rectangle): Boolean {

        return this.toString() == other.toString()
    }

    /**
     * offsetRect
     */
    fun offsetRect(
        offsetX: Int = 0,
        offsetY: Int = 0,
        width: Int = this.width,
        height: Int = this.height
    ): Rectangle {

        return Rectangle(
            left = this.left + offsetX,
            top = this.top + offsetY,
            width = width,
            height = height
        )
    }

    /**
     * localRegionRect
     */
    fun localRegionRect(): Rectangle {

        return Rectangle(left = 0, top = 0, width = width, height = height)
    }

    /**
     * toBoundsWithRatio
     */
    fun toBoundsWithRatio(
        ratio: Int = testContext.boundsToRectRatio,
    ): Bounds {

        return Bounds(left = left / ratio, top = top / ratio, width = width / ratio, height = height / ratio)
    }

    override fun toString(): String {
        return "[$left, $top, $right, $bottom](w=$width, h=$height)"
    }

    /**
     * toVisionElement
     */
    fun toVisionElement(): VisionElement {

        val v = VisionElement(capture = true)
        val c = v.visionContext
        c.localRegionX = this.left
        c.localRegionY = this.top
        c.rectOnLocalRegion = Rectangle(left = 0, top = 0, width = this.width, this.height)
        c.localRegionImage = c.screenshotImage?.cropImage(rect = this)
        c.localRegionFile = null

        return v
    }

    /**
     * mergeWith
     */
    fun mergeWith(other: Rectangle): Rectangle {

        val minLeft = Math.min(this.left, other.left)
        val minTop = Math.min(this.top, other.top)
        val maxRight = Math.max(this.right, other.right)
        val maxBottom = Math.max(this.bottom, other.bottom)
        return createFrom(left = minLeft, top = minTop, right = maxRight, bottom = maxBottom)
    }
}