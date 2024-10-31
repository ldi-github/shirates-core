package shirates.core.utility.image

import shirates.core.driver.Bounds

class Rectangle(
    var x: Int = 0,
    var y: Int = 0,
    var width: Int = 0,
    var height: Int = 0
) {
    constructor(rectString: String) : this() {
        parse(rectString)
    }

    val bounds: Bounds
        get() {
            if (_bounds == null) {
                _bounds = Bounds(this.x, this.y, this.width, this.height)
            }
            return _bounds!!
        }
    private var _bounds: Bounds? = null

    private fun getUsage(input: String): String {

        return "Format [x, y, width, height] is accepted. ($input)"
    }

    private fun parse(rectString: String) {

        val errorMessage = getUsage(rectString)

        val text = rectString.trim()

        if (text.startsWith("[").not() || text.endsWith("]").not()) {
            throw IllegalArgumentException(errorMessage)
        }

        val tokens = text.trim('[', ']').split(",").map { it.trim() }
        if (tokens.size != 4) {
            throw IllegalArgumentException(errorMessage)
        }

        val values = try {
            tokens.map { it.toDouble().toInt() }
        } catch (t: Exception) {
            throw IllegalArgumentException(errorMessage, t)
        }

        this.x = values[0]
        this.y = values[1]
        this.width = values[2]
        this.height = values[3]
    }

    fun area(): Int {
        return width * height
    }

    fun trimBy(trimObject: TrimObject): Rectangle {

        return trimObject.trim(this)
    }

    override fun toString(): String {
        return "[$x, $y, $width, $height]"
    }
}