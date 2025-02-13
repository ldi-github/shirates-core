package shirates.core.driver

import shirates.core.configuration.PropertiesManager
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.RectangleInterface
import shirates.core.utility.image.TrimObject

/**
 * Bounds
 */
data class Bounds(
    override var left: Int = 0,
    override var top: Int = 0,
    override var width: Int = 0,
    override var height: Int = 0
) : RectangleInterface {

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
     * isEmpty
     */
    val isEmpty: Boolean
        get() {
            return (this == empty)
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