package shirates.core.driver.commandextension.helper

import shirates.core.driver.TestElement

class HorizontalBand(
    internal var top: Int,
    internal var bottom: Int,
) {

    internal val members = mutableListOf<TestElement>()

    constructor(
        baseElement: TestElement,
    ) : this(
        top = baseElement.bounds.top,
        bottom = baseElement.bounds.bottom,
    ) {
        merge(element = baseElement)
    }

    /**
     * canMerge
     */
    fun canMerge(
        element: TestElement,
        margin: Int = 0
    ): Boolean {

        if (members.isEmpty()) {
            return true
        }
        if (element.bounds.bottom <= this.top - margin) {
            return false
        }
        if (this.bottom + margin <= element.bounds.top) {
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
        top = members.map { it.bounds.top }.minOrNull() ?: 0
        bottom = members.map { it.bounds.bottom }.maxOrNull() ?: 0
    }

    /**
     * merge
     */
    fun merge(
        element: TestElement,
        margin: Int = 0
    ): Boolean {

        if (canMerge(element = element, margin = margin).not()) {
            return false
        }

        members.add(element)
        members.sortWith(compareBy<TestElement> { it.bounds.left }
            .thenBy { it.bounds.top }
            .thenBy { -it.bounds.area })
        refreshTopAndBottom()
        return true
    }

    /**
     * getElements
     */
    fun getElements(): MutableList<TestElement> {
        return members.toMutableList()
    }

}