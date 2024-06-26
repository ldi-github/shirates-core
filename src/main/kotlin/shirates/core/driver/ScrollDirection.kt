package shirates.core.driver

enum class ScrollDirection {
    Down,
    Up,
    Right,
    Left,
    None
}

val ScrollDirection.isDown: Boolean
    get() {
        return this == ScrollDirection.Down
    }

val ScrollDirection.isUp: Boolean
    get() {
        return this == ScrollDirection.Up
    }

val ScrollDirection.isRight: Boolean
    get() {
        return this == ScrollDirection.Right
    }

val ScrollDirection.isLeft: Boolean
    get() {
        return this == ScrollDirection.Left
    }

val ScrollDirection.isNone: Boolean
    get() {
        return this == ScrollDirection.None
    }

val ScrollDirection.isVertical: Boolean
    get() {
        return isDown || isUp
    }

val ScrollDirection.isHorizontal: Boolean
    get() {
        return isLeft || isRight
    }

fun ScrollDirection.toAxis(): Axis {

    if (isVertical) {
        return Axis.Vertical
    } else if (isHorizontal) {
        return Axis.Horizontal
    } else {
        return Axis.None
    }
}