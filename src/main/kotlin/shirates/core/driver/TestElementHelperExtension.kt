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
 * findInDescendantsAndSelf
 */
fun TestElement.findInDescendantsAndSelf(
    expression: String,
    inViewOnly: Boolean = false
): TestElement {

    TestLog.trace()

    val sel: Selector
    if (TestDriver.isInitialized.not()) {
        sel = Selector(expression = expression)
    } else {
        sel = TestDriver.expandExpression(expression = expression)
    }

    TestDriver.lastElement = findInDescendantsAndSelf(selector = sel, inViewOnly = inViewOnly)

    return TestDriver.lastElement
}

/**
 * findInDescendantsAndSelf
 */
fun TestElement.findInDescendantsAndSelf(
    selector: Selector,
    inViewOnly: Boolean = false
): TestElement {

    TestLog.trace()

    val elms = descendantsAndSelf.filterBySelector(selector = selector, inViewOnly = inViewOnly)
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


