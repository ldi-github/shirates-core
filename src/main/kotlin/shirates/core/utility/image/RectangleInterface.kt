package shirates.core.utility.image

interface RectangleInterface {
    var left: Int
    var top: Int
    var width: Int
    var height: Int

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
     * right
     */
    val right: Int
        get() {
            if (width == 0) {
                return left
            }
            return left + width - 1;
        }

    /**
     * bottom
     */
    val bottom: Int
        get() {
            if (height == 0) {
                return top
            }
            return top + height - 1;
        }

    /**
     * centerX
     */
    val centerX: Int
        get() {
            return left + width / 2
        }

    /**
     * centerY
     */
    val centerY: Int
        get() {
            return top + height / 2
        }

    /**
     * area
     */
    val area: Int
        get() {
            return width * height
        }

    /**
     * isCenterXIncludedIn
     */
    fun isCenterXIncludedIn(other: RectangleInterface): Boolean {

        if (this.centerX < other.x1) {
            return false
        }
        if (this.centerX > other.x2) {
            return false
        }

        return true
    }

    /**
     * isCenterYIncludedIn
     */
    fun isCenterYIncludedIn(other: RectangleInterface): Boolean {

        if (this.centerY < other.y1) {
            return false
        }
        if (this.centerY > other.y2) {
            return false
        }

        return true
    }

    /**
     * isCenterIncludedIn
     */
    fun isCenterIncludedIn(other: RectangleInterface): Boolean {

        return isCenterXIncludedIn(other) && isCenterYIncludedIn(other)
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
    fun isLeftIncludedIn(other: RectangleInterface): Boolean {

        return (other.left <= this.left && this.left <= other.right)
    }

    /**
     * isRightIncludedIn
     */
    fun isRightIncludedIn(other: RectangleInterface): Boolean {

        return (other.left <= this.right && this.right <= other.right)
    }

    /**
     * isTopIncludedIn
     */
    fun isTopIncludedIn(other: RectangleInterface): Boolean {

        return (other.top <= this.top && this.top <= other.bottom)
    }

    /**
     * isBottomIncludedIn
     */
    fun isBottomIncludedIn(other: RectangleInterface): Boolean {

        return (other.top <= this.bottom && this.bottom <= other.bottom)
    }

    /**
     * isIncludedIn
     */
    fun isIncludedIn(other: RectangleInterface): Boolean {

        return isLeftIncludedIn(other) &&
                isTopIncludedIn(other) &&
                isRightIncludedIn(other) &&
                isBottomIncludedIn(other)
    }

    /**
     * isSeparatedFrom
     */
    fun isSeparatedFrom(other: RectangleInterface): Boolean {

        if (other.right < this.left) {
            return true
        }
        if (other.bottom < this.top) {
            return true
        }
        if (this.right < other.left) {
            return true
        }
        if (this.bottom < other.top) {
            return true
        }
        return false
    }

    /**
     * isOverlapping
     */
    fun isOverlapping(other: RectangleInterface): Boolean {

        return isSeparatedFrom(other).not()
    }

}