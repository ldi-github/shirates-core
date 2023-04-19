package shirates.core.driver

import shirates.core.configuration.Selector
import shirates.core.driver.TestElement.Companion.emptyElement
import shirates.core.exception.TestDriverException
import shirates.core.logging.TestLog


/**
 * descendantsInBounds
 */
val TestElement.descendantsInBounds: List<TestElement>
    get() {
        return this.descendants.filter { it.bounds.isIncludedIn(this.bounds) }
    }

/**
 * descendants
 */
val TestElement.descendants: List<TestElement>
    get() {
        if (this.descendantsCache != null) {
            return descendantsCache!!
        }
        val list = mutableListOf<TestElement>()
        addChild(this, list)
        descendantsCache = list
        return descendantsCache!!
    }

/**
 * descendantsAndSelf
 */
val TestElement.descendantsAndSelf: List<TestElement>
    get() {
        val list = descendants.toMutableList()
        list.add(0, this)
        return list
    }

private fun addChild(element: TestElement, list: MutableList<TestElement>) {

    for (c in element.children) {
        list.add(c)
        addChild(c, list)
    }
}

/**
 * ancestors
 */
val TestElement.ancestors: List<TestElement>
    get() {
        val list = mutableListOf<TestElement>()
        getAncestors(this, list)
        return list
    }

/**
 * ancestorsAndSelf
 */
val TestElement.ancestorsAndSelf: List<TestElement>
    get() {
        val list = ancestors.toMutableList()
        list.add(this)
        return list
    }

private fun getAncestors(element: TestElement, list: MutableList<TestElement>) {

    if (element.parentElement == null) {
        return
    }

    list.add(0, element.parentElement!!)

    getAncestors(element.parentElement!!, list)
}

/**
 * siblings
 */
val TestElement.siblings: List<TestElement>
    get() {
        if (parentElement == null) {
            return mutableListOf()
        }

        return parentElement!!.children.toList()
    }

/**
 * findInDescendantsAndSelf
 */
fun TestElement.findInDescendantsAndSelf(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    TestLog.trace()

    val sel: Selector
    if (TestDriver.isInitialized.not()) {
        sel = Selector(expression = expression)
    } else {
        sel = TestDriver.expandExpression(expression = expression)
    }

    TestDriver.lastElement = findInDescendantsAndSelf(selector = sel, safeElementOnly = safeElementOnly)

    return TestDriver.lastElement
}

/**
 * findInDescendantsAndSelf
 */
fun TestElement.findInDescendantsAndSelf(
    selector: Selector,
    safeElementOnly: Boolean = true
): TestElement {

    TestLog.trace()

    val elms =
        TestElementCache.filterElements(selector = selector, safeElementOnly = safeElementOnly, selectContext = this)
    var a = elms.firstOrNull()

    if (a == null) {
        a = emptyElement
        a.lastError = TestDriverException("element not found in cache. (selector=$selector)")
    } else {
        a.lastError = null
    }
    a.selector = selector
    TestDriver.lastElement = a
    return a
}


