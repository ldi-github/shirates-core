package shirates.core.driver.commandextension.helper

import shirates.core.driver.TestElement

interface IFlowContainer {
    /**
     * addElement
     */
    fun addElement(element: TestElement, margin: Int = 0, force: Boolean = false): Any?

    /**
     * addAll
     */
    fun addAll(elements: List<TestElement>, margin: Int = 0, force: Boolean = false)

    /**
     * getElements
     */
    fun getElements(): MutableList<TestElement>

    /**
     * element
     */
    fun element(pos: Int): TestElement
}