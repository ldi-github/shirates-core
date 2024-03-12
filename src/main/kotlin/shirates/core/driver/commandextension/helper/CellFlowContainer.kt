package shirates.core.driver.commandextension.helper

import shirates.core.driver.TestElement
import shirates.core.driver.commandextension.cellOf
import shirates.core.utility.element.ElementCategory

class CellFlowContainer {

    class ContainerItem(
        val cell: TestElement,
        val testElement: TestElement,
        val container: IFlowContainer
    )

    private val containerItems = mutableListOf<ContainerItem>()

    /**
     * addElement
     */
    fun addElement(
        element: TestElement
    ) {

        var cell = element.cellOf(throwsException = false)
        if (cell.isEmpty) {
            cell = element.parentElement
            if (cell.category == ElementCategory.BUTTON) {
                cell = cell.parentElement
            }
        }
        var containerItem = containerItems.filter { it.cell == cell }.firstOrNull()
        if (containerItem == null) {
            val container = FlowContainer()
            containerItem = ContainerItem(cell = cell, testElement = element, container = container)
            containerItems.add(containerItem)
        }
        containerItem.container.addElement(element)
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

        containerItems.sortWith(compareBy<ContainerItem> { it.cell.bounds.top }
            .thenBy { it.cell.bounds.left })
        val elements = mutableListOf<TestElement>()
        for (containerItem in containerItems) {
            elements.addAll(containerItem.container.getElements())
        }
        return elements
    }
}