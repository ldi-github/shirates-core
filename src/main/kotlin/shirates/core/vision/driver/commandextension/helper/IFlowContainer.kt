package shirates.core.vision.driver.commandextension.helper

interface IFlowContainer {
    /**
     * addElement
     */
    fun addElement(element: IRect, margin: Int = 0): Any?

    /**
     * addAll
     */
    fun addAll(elements: List<IRect>, margin: Int = 0)

    /**
     * getElements
     */
    fun getElements(): MutableList<IRect>

    /**
     * element
     */
    fun element(pos: Int): IRect
}