package shirates.core.driver

import shirates.core.configuration.PropertiesManager
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.TrimObject

/**
 * Bounds
 */
data class Bounds(
    var left: Int = 0,
    var top: Int = 0,
    var width: Int = 0,
    var height: Int = 0
) {

    var viewPortOrBoundString = ""

    constructor(viewPortOrBoundString: String) : this() {

        this.viewPortOrBoundString = viewPortOrBoundString

        if (viewPortOrBoundString.startsWith("{")) {
            parseAsViewportRect(viewPortOrBoundString)
        } else {
            parseAsBoundsString(viewPortOrBoundString)
        }
    }

    /**
     * viewportRect:{left=0, top=66, width=1080, height=2022}
     */
    private fun parseAsViewportRect(viewPort: String) {

        val rect = viewPort.replace("{", "").replace("}", "").replace(" ", "")
        val list = rect.split(",")
        try {
            x1 = list[0].replace("left=", "").toInt()
            y1 = list[1].replace("top=", "").toInt()
            width = list[2].replace("width=", "").toInt()
            height = list[3].replace("height=", "").toInt()
        } catch (t: Throwable) {
            throw IllegalArgumentException(
                "Following viewPort format is allowed: {left=0, top=66, width=1080, height=2022}",
                t
            )
        }
    }

    /**
     * boundsString:[0,0][100,200]
     */
    private fun parseAsBoundsString(boundsString: String) {

        if (TestMode.isNoLoadRun) {
            x1 = dummy.x1
            y1 = dummy.y1
            width = dummy.width
            height = dummy.height
            return
        }
        val list = boundsString.replace(" ", "").trimStart('[').trimEnd(']').split(",", "][")
        try {
            x1 = list[0].toInt()
            y1 = list[1].toInt()
            val x2 = list[2].toInt()
            val y2 = list[3].toInt()
            width = x2 - x1 + 1
            height = y2 - y1 + 1
        } catch (t: Throwable) {
            throw IllegalArgumentException("Following bounds format is allowed: [x1,y1][x2,y2]", t)
        }
    }

    /**
     * x2
     */
    val x2: Int
        get() {
            return x1 + width - 1
        }

    /**
     * y2
     */
    val y2: Int
        get() {
            return y1 + height - 1
        }

    /**
     * centerX
     */
    val centerX: Int
        get() {
            return x1 + width / 2
        }

    /**
     * centerY
     */
    val centerY: Int
        get() {
            return y1 + height / 2
        }

    /**
     * x1
     */
    var x1: Int
        get() {
            return left;
        }
        set(value) {
            left = value
        }

    /**
     * y1
     */
    var y1: Int
        get() {
            return top;
        }
        set(value) {
            top = value
        }

    /**
     * right
     */
    val right: Int
        get() {
            return x1 + width - 1;
        }

    /**
     * bottom
     */
    val bottom: Int
        get() {
            return y1 + height - 1;
        }

    /**
     * area
     */
    val area: Int
        get() {
            return width * height
        }

    /**
     * isEmpty
     */
    val isEmpty: Boolean
        get() {
            return (this == empty)
        }

    /**
     * isCenterXIncludedIn
     */
    fun isCenterXIncludedIn(bounds: Bounds): Boolean {

        if (this.centerX < bounds.x1) {
            return false
        }
        if (this.centerX > bounds.x2) {
            return false
        }

        return true
    }

    /**
     * isCenterYIncludedIn
     */
    fun isCenterYIncludedIn(bounds: Bounds): Boolean {

        if (this.centerY < bounds.y1) {
            return false
        }
        if (this.centerY > bounds.y2) {
            return false
        }

        return true
    }

    /**
     * isCenterIncludedIn
     */
    fun isCenterIncludedIn(bounds: Bounds): Boolean {

        return isCenterXIncludedIn(bounds) && isCenterYIncludedIn(bounds)
    }

    /**
     * includesPoint
     */
    fun includesPoint(x: Int, y: Int): Boolean {

        return (this.x1 <= x && x <= this.x2 && this.y1 <= y && y <= this.y2)
    }

    /**
     * isLeftIncludedIn
     */
    fun isLeftIncludedIn(bounds: Bounds): Boolean {

        return (bounds.left <= this.left && this.left <= bounds.right)
    }

    /**
     * isRightIncludedIn
     */
    fun isRightIncludedIn(bounds: Bounds): Boolean {

        return (bounds.left <= this.right && this.right <= bounds.right)
    }

    /**
     * isTopIncludedIn
     */
    fun isTopIncludedIn(bounds: Bounds): Boolean {

        return (bounds.top <= this.top && this.top <= bounds.bottom)
    }

    /**
     * isBottomIncludedIn
     */
    fun isBottomIncludedIn(bounds: Bounds): Boolean {

        return (bounds.top <= this.bottom && this.bottom <= bounds.bottom)
    }

    /**
     * isIncludedIn
     */
    fun isIncludedIn(bounds: Bounds): Boolean {

        return isLeftIncludedIn(bounds) &&
                isTopIncludedIn(bounds) &&
                isRightIncludedIn(bounds) &&
                isBottomIncludedIn(bounds)
    }

    /**
     * isAlmostIncludedIn
     */
    fun isAlmostIncludedIn(bounds: Bounds, margin: Int = 5): Boolean {

        val relaxedBounds = Bounds(
            left = bounds.left - margin,
            top = bounds.top - margin,
            width = bounds.width + margin * 2,
            height = bounds.height + margin * 2
        )
        return isLeftIncludedIn(relaxedBounds) &&
                isTopIncludedIn(relaxedBounds) &&
                isRightIncludedIn(relaxedBounds) &&
                isBottomIncludedIn(relaxedBounds)
    }

    /**
     * isSeparatedFrom
     */
    fun isSeparatedFrom(bounds: Bounds): Boolean {

        if (bounds.right < this.left) {
            return true
        }
        if (bounds.bottom < this.top) {
            return true
        }
        if (this.right < bounds.left) {
            return true
        }
        if (this.bottom < bounds.top) {
            return true
        }
        return false
    }

    /**
     * isOverlapping
     */
    fun isOverlapping(bounds: Bounds): Boolean {

        return isSeparatedFrom(bounds).not()
    }

    /**
     * toRectWithRatio
     */
    fun toRectWithRatio(
        ratio: Int = testContext.boundsToRectRatio,
        trimObject: TrimObject? = null
    ): Rectangle {

        var r = Rectangle(
            x1 * ratio,
            y1 * ratio,
            width * ratio,
            height * ratio
        )
        if (trimObject != null) {
            r = trimObject.trim(r)
        }

        return r
    }

    /**
     * toScaledRect
     */
    fun toScaledRect(): Rectangle {
        val scale = PropertiesManager.screenshotScale
        val x = this.x1 * scale
        val y = this.y1 * scale
        val width = this.width * scale
        val height = this.height * scale
        val r = Rectangle(x.toInt(), y.toInt(), width.toInt(), height.toInt())
        return r
    }

    /**
     * offsetBounds
     */
    fun offsetBounds(offsetX: Int = 0, offsetY: Int = 0): Bounds {

        val newBounds = Bounds(
            left = left + offsetX,
            top = top + offsetY,
            width = width,
            height = height
        )
        return newBounds
    }

    /**
     * companion object
     */
    companion object {

        /**
         * empty
         */
        val empty: Bounds = Bounds(0, 0, 0, 0)

        /**
         * dummy
         */
        val dummy: Bounds = Bounds(left = -9999, top = -9999, width = -9999, height = -9999)
    }

    /**
     * boundString
     */
    val boundString: String
        get() {
            return "[$x1,$y1][$x2,$y2]"
        }

    /**
     * toString
     */
    override fun toString(): String {
        return "$boundString width=$width, height=$height, centerX=$centerX, centerY=$centerY"
    }
}