package shirates.core.vision.driver.commandextension.helper

import shirates.core.logging.Message.message
import shirates.core.vision.VisionElement

class FlowContainer : IFlowContainer {

    class Row(initialElement: IRect) {
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
        fun canMerge(
            element: IRect,
            margin: Int = 0,
        ): Boolean {

            return horizontalBand.canMerge(element = element, margin = margin)
        }

        /**
         * merge
         */
        fun merge(
            element: IRect,
            margin: Int = 0,
        ): Boolean {

            if (canMerge(element = element, margin = margin).not()) {
                return false
            }
            horizontalBand.merge(element = element, margin = margin)
            return true
        }

        /**
         * members
         */
        val members: MutableList<IRect>
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
    override fun addElement(
        element: IRect,
        margin: Int,
    ): Any? {

        return addElementToRow(element = element, margin = margin)
    }

    /**
     * addElementToRow
     */
    fun addElementToRow(
        element: IRect,
        margin: Int = 0,
    ): Row? {

        val s1 = element.toString()
        val elements = getElements()
        val contains = elements.any() { it.toString() == s1 }
        if (contains) {
            return null
        }

        try {
            if (rows.isEmpty()) {
                val row = Row(initialElement = element)
                rows.add(row)
                return row
            }

            for (row in rows) {
                if (row.canMerge(element = element, margin = margin)) {
                    row.merge(element = element, margin = margin)
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
    override fun addAll(elements: List<IRect>, margin: Int) {

        for (e in elements) {
            addElementToRow(element = e)
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
    override fun getElements(): MutableList<IRect> {

        sortRows()

        val list = mutableListOf<IRect>()
        for (row in rows) {
            list.addAll(row.members)
        }

        return list
    }

    /**
     * element
     */
    override fun element(pos: Int): IRect {

        if (pos == 0) {
            throw IndexOutOfBoundsException(message(id = "posMustBeGreaterThanZero", arg1 = "${pos}"))
        }

        val elements = getElements()
        if (pos > elements.count()) {
            return VisionElement()
        }

        return elements[pos - 1]
    }

}