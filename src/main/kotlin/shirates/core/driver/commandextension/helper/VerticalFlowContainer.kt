package shirates.core.driver.commandextension.helper

import shirates.core.driver.TestElement
import shirates.core.driver.commandextension.removeIncludingElements
import shirates.core.logging.Message.message

class VerticalFlowContainer : IFlowContainer {

    class Column(initialElement: TestElement) {
        internal val verticalBand: VerticalBand

        init {
            verticalBand = VerticalBand(initialElement)
        }

        /**
         * left
         */
        val left: Int
            get() {
                return verticalBand.left
            }

        /**
         * right
         */
        val right: Int
            get() {
                return verticalBand.right
            }

        /**
         * canMerge
         */
        fun canMerge(
            element: TestElement,
            margin: Int = 0,
            force: Boolean = false
        ): Boolean {

            if (!force && element.isWidget.not()) {
                return false
            }
            return verticalBand.canMerge(element = element, margin = margin)
        }

        /**
         * merge
         */
        fun merge(
            element: TestElement,
            margin: Int = 0,
            force: Boolean = false
        ): Boolean {

            if (canMerge(element = element, margin = margin, force = force).not()) {
                return false
            }
            verticalBand.merge(element = element, margin = margin)
            return true
        }

        /**
         * members
         */
        val members: MutableList<TestElement>
            get() {
                return verticalBand.members.toMutableList()
            }
    }

    /**
     * columns
     */
    val columns = mutableListOf<Column>()

    private fun addColumn(column: Column) {
        if (columns.contains(column)) {
            return
        }
        columns.add(column)
        columns.sortWith(compareBy { it.left })
    }

    /**
     * addElement
     */
    override fun addElement(
        element: TestElement,
        margin: Int,
        force: Boolean
    ): Any? {

        return addElementToColumn(element = element, margin = margin, force = force)
    }

    /**
     * addElementToColumn
     */
    fun addElementToColumn(
        element: TestElement,
        margin: Int = 0,
        force: Boolean = false
    ): Column? {

        if (!force && element.isWidget.not()) {
            return null
        }

        val s1 = element.toString()
        val elements = getElements()
        val contains = elements.any() { it.toString() == s1 }
        if (contains) {
            return null
        }

        try {
            if (columns.isEmpty()) {
                val column = Column(initialElement = element)
                columns.add(column)
                return column
            }

            for (column in columns) {
                if (column.canMerge(element = element, margin = margin, force = force)) {
                    column.merge(element = element, margin = margin, force = force)
                    return column
                }
            }

            val column = Column(initialElement = element)
            addColumn(column)
            return column

        } finally {
            sortColumns()
        }
    }

    /**
     * addAll
     */
    override fun addAll(
        elements: List<TestElement>,
        margin: Int,
        force: Boolean
    ) {
        val elms = elements.removeIncludingElements()
        for (e in elms) {
            addElementToColumn(element = e, margin = margin, force = force)
        }
    }

    /**
     * sortColumns
     */
    fun sortColumns() {

        columns.sortWith(compareBy { it.left })
    }

    /**
     * getElements
     */
    override fun getElements(): MutableList<TestElement> {

        sortColumns()

        val list = mutableListOf<TestElement>()
        for (column in columns) {
            list.addAll(column.members)
        }

        return list
    }

    /**
     * element
     */
    override fun element(pos: Int): TestElement {

        if (pos == 0) {
            throw IndexOutOfBoundsException(message(id = "posMustBeGreaterThanZero", arg1 = "${pos}"))
        }

        val elements = getElements()
        if (pos > elements.count()) {
            return TestElement()
        }

        return elements[pos - 1]
    }

}