package shirates.core.driver

enum class ScrollDirection {
    Down,
    Up,
    Right,
    Left
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

val ScrollDirection.isVertical: Boolean
    get() {
        return isDown || isUp
    }

val ScrollDirection.isHorizontal: Boolean
    get() {
        return isLeft || isRight
    }