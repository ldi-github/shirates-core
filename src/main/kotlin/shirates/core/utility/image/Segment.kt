package shirates.core.utility.image

import shirates.core.driver.Bounds

class Segment(
) {
    val members = mutableListOf<Rectangle>()

    var left: Int = 0
        private set

    var top: Int = 0
        private set

    var right: Int = 0
        private set

    var bottom: Int = 0
        private set

    val width: Int
        get() {
            return (right + 1) - left
        }

    val height: Int
        get() {
            return (bottom + 1) - top
        }

    val area: Int
        get() {
            return width * height
        }

    val aspectRatio: Float
        get() {
            return width.toFloat() / height.toFloat()
        }

    override fun toString(): String {
        return "[$left, $top, $right, $bottom](w=$width, h=$height, ratio=$aspectRatio)"
    }

    /**
     * addMember
     */
    fun addMember(rectangle: Rectangle): Segment {

        if (members.isEmpty()) {
            left = rectangle.left
            top = rectangle.top
            right = rectangle.right
            bottom = rectangle.bottom
            members.add(rectangle)
            return this
        }

        refreshSegmentBoundary()
        return this
    }

    private fun refreshSegmentBoundary() {

        left = members.minOfOrNull { it.left } ?: 0
        top = members.minOfOrNull { it.top } ?: 0
        right = members.maxOfOrNull { it.right } ?: 0
        bottom = members.maxOfOrNull { it.bottom } ?: 0
    }

    /**
     * addAll
     */
    fun addAll(list: List<Rectangle>): Segment {

        for (rect in list) {
            if (members.contains(rect).not()) {
                members.add(rect)
            }
        }
        refreshSegmentBoundary()
        return this
    }

    /**
     * canMerge
     */
    fun canMerge(segment: Segment, margin: Int): Boolean {

        val thisBounds = this.toBounds()
        val thatBounds = segment.toBounds()

        thatBounds.left -= margin
        if (thatBounds.left <= 0) thatBounds.left = 0

        thatBounds.top -= margin
        if (thatBounds.top <= 0) thatBounds.top = 0

        thatBounds.width += margin * 2
        thatBounds.height += margin * 2

        if (thisBounds.isOverlapping(thatBounds)) {
            return true
        }

        return false
    }

    /**
     * merge
     */
    fun merge(segment: Segment, margin: Int = 0, force: Boolean = false): Segment {

        if (force || canMerge(segment = segment, margin = margin)) {
            addAll(segment.members)
        }

        return this
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