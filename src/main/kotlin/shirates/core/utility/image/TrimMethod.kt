package shirates.core.utility.image

class TrimMethod(expression: String = "0") {

    var value: Double
        private set

    var type: String
        private set

    val isZero: Boolean
        get() {
            return value == 0.0
        }

    init {

        value = expression.toDoubleOrNull()
            ?: throw IllegalArgumentException("TrimMethod format error. (expression=${expression})")

        if (value < 0.0) {
            throw IllegalArgumentException("TrimMethod range error. Value must not be negative. (expression=$expression)")
        }

        if (value < 1.0) {
            type = "ratio"
        } else {
            type = "pixel"
            value = value.toInt().toDouble()
        }
    }

    fun getPixel(source: Int): Int {

        if (type == "pixel") {
            return value.toInt()
        }

        return (source * value).toInt()
    }

    override fun toString(): String {

        if (type == "pixel") {
            return "${value.toInt()}"
        }

        return "$value"
    }
}