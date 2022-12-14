package shirates.core.utility.image

class Rectangle(
    var x: Int = 0,
    var y: Int = 0,
    var width: Int = 0,
    var height: Int = 0
) {
    fun area(): Int {
        return width * height
    }

    fun trimBy(trimObject: TrimObject): Rectangle {

        return trimObject.trim(this)
    }
}