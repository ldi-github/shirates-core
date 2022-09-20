package shirates.core.driver.commandextension.helper

import shirates.core.driver.TestElement

class VerticalBand(internal var left: Int, internal var right: Int) {

    internal val members = mutableListOf<TestElement>()

    constructor(baseElement: TestElement) : this(baseElement.bounds.left, baseElement.bounds.right) {

        members.add(baseElement)
    }

    /**
     * canMerge
     */
    fun canMerge(element: TestElement): Boolean {

        if (element.bounds.right < this.left) {
            return false
        }
        if (this.right < element.bounds.left) {
            return false
        }
        return true
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
            members.sortWith(compareBy<TestElement> { it.bounds.top }
                .thenBy { it.bounds.left }
                .thenBy { -it.bounds.area })
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