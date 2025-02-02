package shirates.core.utility.image

import shirates.core.driver.Bounds
import shirates.core.driver.testContext
import shirates.core.vision.VisionElement

class Rectangle(
    var x: Int = 0,
    var y: Int = 0,
    var width: Int = 0,
    var height: Int = 0
) {
    val left: Int
        get() {
            return x
        }

    val top: Int
        get() {
            return y
        }

    val right: Int
        get() {
            if (width == 0) {
                return x
            }
            return x + width - 1
        }

    val bottom: Int
        get() {
            if (height == 0) {
                return y
            }
            return y + height - 1
        }

    /**
     * centerX
     */
    val centerX: Int
        get() {
            return x + width / 2
        }

    /**
     * centerY
     */
    val centerY: Int
        get() {
            return y + height / 2
        }

    val isEmpty: Boolean
        get() {
            return area == 0
        }

    val area: Int
        get() {
            return width * height
        }


    constructor(leftTopRightBottom: String) : this() {

        try {
            val r = leftTopRightBottom.removePrefix("[").split("]").first().split(",").map { it.trim().toInt() }
            this.x = r[0]
            this.y = r[1]
            this.width = r[2] - x + 1
            this.height = r[3] - y + 1
        } catch (t: Throwable) {
            throw IllegalArgumentException(leftTopRightBottom, t)
        }
    }

    companion object {

        fun createFrom(left: Int, top: Int, right: Int, bottom: Int): Rectangle {
            val width = right - left + 1
            val height = bottom - top + 1
            return Rectangle(x = left, y = top, width = width, height = height)
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
            x = this.x + offsetX,
            y = this.y + offsetY,
            width = width,
            height = height
        )
    }

    /**
     * localRegionRect
     */
    fun localRegionRect(): Rectangle {

        return Rectangle(x = 0, y = 0, width = width, height = height)
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
        c.localRegionX = this.x
        c.localRegionY = this.y
        c.rectOnLocalRegion = Rectangle(x = 0, y = 0, width = this.width, this.height)
        c.localRegionImage = c.screenshotImage?.cropImage(rect = this)
        c.localRegionFile = null

//        val fileName = TestLog.getNextScreenshotFileName(suffix = "_cropRegion_$this")
//        c.localRegionFile = c.localRegionImage!!.saveImage(TestLog.directoryForLog.resolve(fileName).toString())
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
        return Rectangle.createFrom(left = minLeft, top = minTop, right = maxRight, bottom = maxBottom)
    }
}