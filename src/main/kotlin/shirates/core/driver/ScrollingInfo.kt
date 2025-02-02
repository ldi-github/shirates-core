package shirates.core.driver

import shirates.core.configuration.PropertiesManager
import kotlin.math.max
import kotlin.math.min

class ScrollingInfo(
    val errorMessage: String,
    val bounds: Bounds,
    val viewport: Bounds,
    val direction: ScrollDirection,
    val startMarginRatio: Double,
    val endMarginRatio: Double,
    val headerBottom: Int? = null,
    val footerTop: Int? = null
) {
    val hasError: Boolean
        get() {
            return errorMessage.isBlank().not()
        }

    val leftEdge: Int
        get() {
            return max(bounds.left, viewport.left)
        }

    val rightEdge: Int
        get() {
            return min(bounds.right, viewport.right)
        }

    val topEdge: Int
        get() {
            val headerBottom = headerBottom ?: PropertiesManager.statBarHeight
            val m1 = max(bounds.top, viewport.top)
            val m2 = max(m1, headerBottom)
            return m2
        }

    val bottomEdge: Int
        get() {
            val footerTop = footerTop ?: viewBounds.bottom
            val m1 = min(bounds.bottom, viewport.bottom)
            val m2 = min(m1, footerTop)
            return m2
        }

    val adjustedScrollableBounds: Bounds
        get() {
            return Bounds(
                left = leftEdge,
                top = topEdge,
                width = rightEdge - leftEdge + 1,
                height = bottomEdge - topEdge + 1
            )
        }

    val leftMargin: Int
        get() {
            if (direction.isNone) return 0
            if (direction.isVertical) return 0
            val marginRatio = if (direction.isLeft) startMarginRatio else endMarginRatio
            return (adjustedScrollableBounds.width * marginRatio).toInt()
        }

    val rightMargin: Int
        get() {
            if (direction.isNone) return 0
            if (direction.isVertical) return 0
            val marginRatio = if (direction.isRight) startMarginRatio else endMarginRatio
            return (adjustedScrollableBounds.width * marginRatio).toInt()
        }

    val topMargin: Int
        get() {
            if (direction.isNone) return 0
            if (direction.isHorizontal) return 0
            val marginRatio = if (direction.isUp) startMarginRatio else endMarginRatio
            return (adjustedScrollableBounds.height * marginRatio).toInt()
        }

    val bottomMargin: Int
        get() {
            if (direction.isNone) return 0
            if (direction.isHorizontal) return 0
            val marginRatio = if (direction.isDown) startMarginRatio else endMarginRatio
            return (adjustedScrollableBounds.height * marginRatio).toInt()
        }

    val startY: Int
        get() {
            return when (direction) {
                ScrollDirection.Down -> bottomEdge - bottomMargin
                ScrollDirection.Up -> topEdge + topMargin
                else -> adjustedScrollableBounds.centerY
            }
        }

    val endY: Int
        get() {
            return when (direction) {
                ScrollDirection.Down -> topEdge + topMargin
                ScrollDirection.Up -> bottomEdge - bottomMargin
                else -> adjustedScrollableBounds.centerY
            }
        }

    val startX: Int
        get() {
            return when (direction) {
                ScrollDirection.Right -> rightEdge - rightMargin
                ScrollDirection.Left -> leftEdge + leftMargin
                else -> adjustedScrollableBounds.centerX
            }
        }

    val endX: Int
        get() {
            return when (direction) {
                ScrollDirection.Right -> leftEdge + leftMargin
                ScrollDirection.Left -> rightEdge - rightMargin
                else -> adjustedScrollableBounds.centerX
            }
        }

    val safeBounds: Bounds
        get() {
            return Bounds(
                left = leftEdge,
                top = topEdge,
                width = rightEdge - leftEdge + 1,
                height = bottomEdge - topEdge + 1
            )
        }

    override fun toString(): String {
        return "safeBounds=$safeBounds, scrollableBounds=$bounds, viewport=$viewport, direction=$direction, startMarginRatio=$startMarginRatio, endMarginRatio=$endMarginRatio, headerBottom=$headerBottom, footerTop=$footerTop, "
    }
}