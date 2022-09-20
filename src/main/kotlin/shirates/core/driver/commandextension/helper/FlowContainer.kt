package shirates.core.driver.commandextension.helper

import shirates.core.driver.TestElement
import shirates.core.logging.Message.message

class FlowContainer {

    class Row(initialElement: TestElement) {
        internal val horizontalBand: HorizontalBand

        init {
            horizontalBand = HorizontalBand(initialElement)
        }

        /**
         * top
         */
        val top: Int
            get() {
                return horizontalBand.top
            }

        /**
         * bottom
         */
        val bottom: Int
            get() {
                return horizontalBand.bottom
            }

        /**
         * canMerge
         */
        fun canMerge(element: TestElement, force: Boolean = false): Boolean {

            if (!force && element.isWidget.not()) {
                return false
            }
            return horizontalBand.canMerge(element)
        }

        /**
         * merge
         */
        fun merge(element: TestElement, force: Boolean = false): Boolean {

            if (canMerge(element = element, force = force).not()) {
                return false
            }
            horizontalBand.merge(element)
            return true
        }

        /**
         * members
         */
        val members: MutableList<TestElement>
            get() {
                return horizontalBand.members.toMutableList()
            }
    }

    /**
     * rows
     */
    val rows = mutableListOf<Row>()

    private fun addRow(row: Row) {
        if (rows.contains(row)) {
            return
        }
        rows.add(row)
        rows.sortWith(compareBy { it.top })
    }

    /**
     * addElement
     */
    fun addElement(element: TestElement, force: Boolean = false): Row? {

        if (!force && element.isWidget.not()) {
            return null
        }

        try {
            if (rows.isEmpty()) {
                val row = Row(initialElement = element)
                rows.add(row)
                return row
            }

            for (row in rows) {
                if (row.canMerge(element = element, force = force)) {
                    row.merge(element = element, force = force)
                    return row
                }
            }

            val row = Row(initialElement = element)
            addRow(row)
            return row

        } finally {
            sortRows()
        }
    }

    /**
     * addAll
     */
    fun addAll(elements: List<TestElement>) {

        for (e in elements) {
            addElement(e)
        }
    }

    /**
     * sortRows
     */
    fun sortRows() {

        rows.sortWith(compareBy { it.top })
    }

    /**
     * getElements
     */
    fun getElements(): MutableList<TestElement> {

        sortRows()

        val list = mutableListOf<TestElement>()
        for (row in rows) {
            list.addAll(row.members)
        }

        return list
    }

    /**
     * element
     */
    fun element(pos: Int): TestElement {

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