package shirates.core.utility.element

import shirates.core.driver.TestElement
import shirates.core.driver.TestMode.isiOS

/**
 * ElementVisibilityUtility
 */
object ElementVisibilityUtility {

    /**
     * isInTable
     */
    fun isInTable(element: TestElement): Boolean {

        if (isiOS.not()) {
            throw NotImplementedError("iOS is supported.")
        }
        val result =
            element.ancestors.any() { ElementCategoryExpressionUtility.scrollableTypesExpression.contains(it.type) }
        return result
    }

    /**
     * isVisibleInTable
     */
    fun isVisibleInTable(element: TestElement): Boolean {

        if (isiOS.not()) {
            throw NotImplementedError("iOS is supported.")
        }
        if (isInTable(element = element).not()) {
            return false
        }
        for (e in element.ancestorsAndSelf.reversed()) {
            if (e.isVisible.not()) {
                return false
            }
        }

        return true
    }
}