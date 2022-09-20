package shirates.core.driver.commandextension.helper

import shirates.core.driver.TestElement

class HorizontalBand(internal var top: Int, internal var bottom: Int) {

    internal val members = mutableListOf<TestElement>()

    constructor(baseElement: TestElement) : this(baseElement.bounds.top, baseElement.bounds.bottom) {

        merge(baseElement)
    }

    /**
     * canMerge
     */
    fun canMerge(element: TestElement): Boolean {

        if (members.isEmpty()) {
            return true
        }
        if (element.bounds.bottom <= this.top) {
            return false
        }
        if (this.bottom <= element.bounds.top) {
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
    fun merge(element: TestElement): Boolean {

        if (canMerge(element = element).not()) {
            return false
        }

        if (members.contains(element).not()) {
            members.add(element)
            members.sortWith(compareBy<TestElement> { it.bounds.left }
                .thenBy { it.bounds.top }
                .thenBy { -it.bounds.area })
            refreshTopAndBottom()
        }
        return true
    }

    /**
     * getElements
     */
    fun getElements(): MutableList<TestElement> {
        return members.toMutableList()
    }

}