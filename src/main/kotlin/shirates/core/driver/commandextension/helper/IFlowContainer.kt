package shirates.core.driver.commandextension.helper

import shirates.core.driver.TestElement

interface IFlowContainer {
    /**
     * addElement
     */
    fun addElement(element: TestElement, force: Boolean = false): Any?

    /**
     * addAll
     */
    fun addAll(elements: List<TestElement>)

    /**
     * getElements
     */
    fun getElements(): MutableList<TestElement>

    /**
     * element
     */
    fun element(pos: Int): TestElement
}