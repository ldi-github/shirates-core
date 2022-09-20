package shirates.core.driver

enum class RelativeDirection {

    above,
    right,
    below,
    left
}

val RelativeDirection.isAbove: Boolean
    get() {
        return this == RelativeDirection.above
    }

val RelativeDirection.isRight: Boolean
    get() {
        return this == RelativeDirection.right
    }

val RelativeDirection.isBelow: Boolean
    get() {
        return this == RelativeDirection.below
    }

val RelativeDirection.isLeft: Boolean
    get() {
        return this == RelativeDirection.left
    }
