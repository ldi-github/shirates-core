package shirates.core.vision.driver.commandextension.helper

import shirates.core.vision.VisionElement

class VerticalBand(internal var left: Int, internal var right: Int) {

    internal val members = mutableListOf<VisionElement>()

    constructor(baseElement: VisionElement) : this(baseElement.rect.left, baseElement.rect.right) {

        members.add(baseElement)
    }

    /**
     * canMerge
     */
    fun canMerge(element: VisionElement, margin: Int = 0): Boolean {

        if (members.isEmpty()) {
            return true
        }
        if (element.rect.right < this.left + margin) {
            return false
        }
        if (this.right + margin < element.rect.left) {
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
    fun merge(element: VisionElement, margin: Int = 0): Boolean {

        if (canMerge(element = element, margin = margin).not()) {
            return false
        }

        if (members.contains(element).not()) {
            members.add(element)
            members.sortWith(compareBy<VisionElement> { it.rect.top }
                .thenBy { it.rect.left }
                .thenBy { -it.rect.area })
        }
        return true
    }

    /**
     * getElements
     */
    fun getElements(): MutableList<VisionElement> {
        return members.toMutableList()
    }

}