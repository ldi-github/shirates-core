package shirates.core.driver.commandextension.helper

import shirates.core.driver.TestElement
import shirates.core.driver.commandextension.cellOf
import shirates.core.utility.element.ElementCategory

class CellFlowContainer {

    class ContainerItem(
        val x: Int,
        val y: Int,
        val item: TestElement
    )

    private val members = mutableListOf<ContainerItem>()

    /**
     * addElement
     */
    fun addElement(
        element: TestElement
    ): TestElement? {

        var e = element.cellOf(throwsException = false)
        if (e.isEmpty) {
            e = element.parentElement
            if (e.category == ElementCategory.BUTTON) {
                e = e.parentElement
            }
            return null
        }
        println(e)

        return addElement(
            element = element,
            x = e.bounds.left,
            y = e.bounds.top
        )
    }

    /**
     * addElement
     */
    fun addElement(
        element: TestElement,
        x: Int = element.parentElement.bounds.left,
        y: Int = element.parentElement.bounds.top,
        force: Boolean = false
    ): TestElement? {

        if (!force && element.isWidget.not()) {
            return null
        }
        val registered = members.map { it.item }.firstOrNull() { it == element }
        if (registered != null) {
            return registered
        }

        val item = ContainerItem(x = x, y = y, item = element)
        members.add(item)

        return element
    }

    /**
     * addAll
     */
    fun addAll(
        elements: List<TestElement>
    ) {
        for (e in elements) {
            addElement(e)
        }
    }

    /**
     * getElements
     */
    fun getElements(): List<TestElement> {

        members.sortWith(compareBy<ContainerItem> { it.y }
            .thenBy { it.x }
            .thenBy { it.item.bounds.left }
            .thenBy { it.item.bounds.top }
            .thenBy { -it.item.bounds.area })

        return members.map { it.item }.toList()
    }
}