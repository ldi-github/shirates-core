package shirates.core.utility.image

class TrimObject(
    expression: String? = ""
) {
    var expression: String
        private set

    internal var trimMethods = listOf<TrimMethod>()
        private set

    var top = TrimMethod()
    var right = TrimMethod()
    var bottom = TrimMethod()
    var left = TrimMethod()

    val isEmpty: Boolean
        get() {
            return top.isZero && right.isZero && bottom.isZero && left.isZero
        }

    init {

        this.expression = expression?.removeSuffix("px") ?: ""

        if (this.expression != "") {
            initialize()
        }
    }

    private fun initialize() {

        trimMethods = expression.split(",", " ").map { TrimMethod(it) }

        when (trimMethods.count()) {
            0 -> {
                // NOP
            }
            1 -> {
                top = trimMethods[0]
                right = trimMethods[0]
                bottom = trimMethods[0]
                left = trimMethods[0]
            }
            2 -> {
                top = trimMethods[0]
                right = trimMethods[1]
                bottom = trimMethods[0]
                left = trimMethods[1]
            }
            3 -> {
                top = trimMethods[0]
                right = trimMethods[1]
                bottom = trimMethods[2]
                left = trimMethods[1]
            }
            4 -> {
                top = trimMethods[0]
                right = trimMethods[1]
                bottom = trimMethods[2]
                left = trimMethods[3]
            }
            else -> {
                throw IllegalArgumentException("TrimObject format error. (expression=$expression)")
            }
        }

    }

    fun trim(rect: Rectangle): Rectangle {

        val width = rect.width
        val height = rect.height

        val trimmedRect = Rectangle(
            rect.x + left.getPixel(width),
            rect.y + top.getPixel(height),
            width - (left.getPixel(width) + right.getPixel(width)),
            height - (top.getPixel(height) + bottom.getPixel(height))
        )

        return trimmedRect
    }

    override fun toString(): String {

        if (expression == "") {
            return ""
        }

        val trims = trimMethods
        when (trims.count()) {
            0 -> return ""
            1 -> return "trim=${trims[0]}"
            2 -> return "trim=${trims[0]},${trims[1]}"
            3 -> return "trim=${trims[0]},${trims[1]},${trims[2]}"
            4 -> return "trim=${trims[0]},${trims[1]},${trims[2]},${trims[3]}"
            else -> throw UnknownError("trim=")
        }
    }

}