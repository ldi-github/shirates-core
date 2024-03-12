package shirates.core.driver.commandextension.helper

import shirates.core.driver.TestElement

class PointFlowContainer {

    class VertexItem(
        val x: Int,
        val y: Int,
        val item: TestElement
    )

    private val members = mutableListOf<VertexItem>()

    /**
     * addElement
     */
    fun addElement(
        element: TestElement,
        x: Int = element.bounds.centerX,
        y: Int = element.bounds.centerY,
        force: Boolean = false
    ): TestElement? {

        if (!force && element.isWidget.not()) {
            return null
        }
        val registered = members.map { it.item }.firstOrNull() { it == element }
        if (registered != null) {
            return registered
        }

        val item = VertexItem(x = x, y = y, item = element)
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

        members.sortWith(compareBy<VertexItem> { it.y }
            .thenBy { it.x }
            .thenBy { -it.item.bounds.area })

        return members.map { it.item }.toList()
    }
}