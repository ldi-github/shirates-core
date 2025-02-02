package shirates.core.vision.driver.commandextension.helper

class VerticalBand(internal var left: Int, internal var right: Int) {

    internal val members = mutableListOf<IRect>()

    constructor(baseElement: IRect) : this(baseElement.getRectInfo().left, baseElement.getRectInfo().right) {

        members.add(baseElement)
    }

    /**
     * canMerge
     */
    fun canMerge(element: IRect, margin: Int = 0): Boolean {

        if (members.isEmpty()) {
            return true
        }
        if (element.getRectInfo().right < this.left + margin) {
            return false
        }
        if (this.right + margin < element.getRectInfo().left) {
            return false
        }
        val s1 = element.toString()
        val contains = members.any() { it.toString() == s1 }
        if (contains) {
            return false
        }
        return true
    }

    /**
     * merge
     */
    fun merge(element: IRect, margin: Int = 0): Boolean {

        if (canMerge(element = element, margin = margin).not()) {
            return false
        }

        if (members.contains(element).not()) {
            members.add(element)
            members.sortWith(compareBy<IRect> { it.getRectInfo().top }
                .thenBy { it.getRectInfo().left }
                .thenBy { -it.getRectInfo().area })
        }
        return true
    }

    /**
     * getElements
     */
    fun getElements(): MutableList<IRect> {
        return members.toMutableList()
    }

}