package shirates.core.vision.driver.commandextension.helper

import shirates.core.vision.VisionElement

class HorizontalBand(
    internal var top: Int,
    internal var bottom: Int,
) {

    internal val members = mutableListOf<VisionElement>()

    constructor(
        baseElement: VisionElement,
    ) : this(
        top = baseElement.rect.top,
        bottom = baseElement.rect.bottom,
    ) {
        merge(element = baseElement)
    }

    /**
     * canMerge
     */
    fun canMerge(
        element: VisionElement,
        margin: Int = 0
    ): Boolean {

        if (members.isEmpty()) {
            return true
        }
        if (element.rect.bottom <= this.top - margin) {
            return false
        }
        if (this.bottom + margin <= element.rect.top) {
            return false
        }
        val s1 = element.toString()
        val contains = members.any() { it.toString() == s1 }
        if (contains) {
            return false
        }
        return true
    }

    private fun refreshTopAndBottom() {
        top = members.map { it.rect.top }.minOrNull() ?: 0
        bottom = members.map { it.rect.bottom }.maxOrNull() ?: 0
    }

    /**
     * merge
     */
    fun merge(
        element: VisionElement,
        margin: Int = 0
    ): Boolean {

        if (canMerge(element = element, margin = margin).not()) {
            return false
        }

        members.add(element)
        members.sortWith(compareBy<VisionElement> { it.rect.left }
            .thenBy { it.rect.top }
            .thenBy { -it.rect.area })
        refreshTopAndBottom()
        return true
    }

    /**
     * getElements
     */
    fun getElements(): MutableList<VisionElement> {
        return members.toMutableList()
    }

}