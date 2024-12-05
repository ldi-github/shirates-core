package shirates.core.utility.image

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

    fun area(): Int {
        return width * height
    }

    fun trimBy(trimObject: TrimObject): Rectangle {

        return trimObject.trim(this)
    }

    override fun toString(): String {
        return "[$left, $top, $right, $bottom](w=$width, h=$height)"
    }
}