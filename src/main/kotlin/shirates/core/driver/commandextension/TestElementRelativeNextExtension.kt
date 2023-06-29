package shirates.core.driver.commandextension

import shirates.core.configuration.Selector
import shirates.core.driver.TestDriver
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestElement
import shirates.core.driver.filterBySelector
import shirates.core.utility.element.ElementCategoryExpressionUtility

internal fun TestElement.next(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    var e = TestElement()
    val context = TestDriverCommandContext(this)
    context.execRelativeCommand(subject = subject, arg1 = selector.nickname) {

        if (selector.pos != null && selector.pos!! < 0) {
            /**
             * next(-1) is converted to previous(1)
             */
            val sel = selector.copy()
            sel.pos = Math.abs(sel.pos!!)
            e = nextPreviousCore(
                next = false,
                targetElements = targetElements,
                inViewOnly = inViewOnly,
                selectorForNextPrevious = sel
            )
        } else {
            e = nextPreviousCore(
                next = true,
                targetElements = targetElements,
                inViewOnly = inViewOnly,
                selectorForNextPrevious = selector
            )
        }
    }

    e.selector = this.getChainedSelector(relativeSelector = selector)
    lastElement = e

    return TestDriver.lastElement
}

/**
 * next
 */
fun TestElement.next(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":next($expression)",
        inViewOnly = inViewOnly,
    )
}

/**
 * next
 */
fun TestElement.next(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":next($pos)",
        inViewOnly = inViewOnly,
    )
}

internal fun TestElement.previous(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    var e = TestElement()
    val context = TestDriverCommandContext(this)
    context.execRelativeCommand(subject = subject, arg1 = selector.nickname) {
        if (selector.pos != null && selector.pos!! < 0) {
            /**
             * previous(-1) is converted to next(1)
             */
            val sel = selector.copy()
            sel.pos = Math.abs(sel.pos!!)
            e = nextPreviousCore(
                next = true,
                inViewOnly = inViewOnly,
                targetElements = targetElements,
                selectorForNextPrevious = sel
            )
        } else {
            e = nextPreviousCore(
                next = false,
                inViewOnly = inViewOnly,
                targetElements = targetElements,
                selectorForNextPrevious = selector
            )
        }
    }

    e.selector = this.getChainedSelector(relativeSelector = selector)
    lastElement = e

    return TestDriver.lastElement
}

/**
 * previous
 */
fun TestElement.previous(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":previous($expression)",
        inViewOnly = inViewOnly,
    )
}

/**
 * previous
 */
fun TestElement.previous(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":previous($pos)",
        inViewOnly = inViewOnly,
    )
}

private fun TestElement.nextPreviousCore(
    next: Boolean,
    inViewOnly: Boolean,
    targetElements: List<TestElement>,
    selectorForNextPrevious: Selector
): TestElement {

    val current = targetElements.firstOrNull() { it.toString() == this.toString() }
    if (current == null) {
        return TestElement.emptyElement
    }
    val currentIndex = targetElements.indexOf(current)

    val filteredByIndex: List<TestElement>
    if (next) {
        filteredByIndex = targetElements.filterIndexed { i, _ -> i > currentIndex }
    } else {
        filteredByIndex = targetElements.filterIndexed { i, _ -> i < currentIndex }
    }

    val sel = selectorForNextPrevious.copy()
    if (!next && sel.pos != null) {
        sel.pos = -1 * sel.pos!!
    }
    var filtered = filteredByIndex
    filtered = filtered.filterBySelector(selector = sel, inViewOnly = inViewOnly)

    var e = if (next) filtered.firstOrNull()
    else filtered.lastOrNull()

    if (e == null) {
        e = TestElement()
    }
    e.selector = this.getChainedSelector(selectorForNextPrevious)

    TestDriver.lastElement = e
    return e
}

internal fun TestElement.execRelativeCommand(
    relativeSelector: Selector,
    getElement: () -> TestElement
): TestElement {

    val oldSelector = this.selector
    val newSelector = this.getChainedSelector(relativeSelector = relativeSelector)

    val context = TestDriverCommandContext(this)
    var e = TestElement(selector = newSelector)
    context.execRelativeCommand(subject = subject, arg1 = relativeSelector.nickname) {

        e = getElement()
    }

    if (e.isFound) {
        if (e == this) {
            e.selector = oldSelector
        } else {
            e.selector = newSelector
        }
    }
    TestDriver.lastElement = e

    return TestDriver.lastElement
}

internal fun TestElement.nextLabel(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.labelTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { next(selector = sel, inViewOnly = inViewOnly, targetElements = targetElements) }
    )
}

/**
 * nextLabel
 */
fun TestElement.nextLabel(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":nextLabel($pos)",
        inViewOnly = inViewOnly,
    )
}

/**
 * nextLabel
 */
fun TestElement.nextLabel(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":nextLabel($expression)",
        inViewOnly = inViewOnly,
    )
}

internal fun TestElement.preLabel(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>,
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.labelTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { previous(selector = sel, inViewOnly = inViewOnly, targetElements = targetElements) }
    )
}

/**
 * preLabel
 */
fun TestElement.preLabel(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":preLabel($pos)",
        inViewOnly = inViewOnly,
    )
}

/**
 * preLabel
 */
fun TestElement.preLabel(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":preLabel($expression)",
        inViewOnly = inViewOnly,
    )
}

internal fun TestElement.nextInput(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>,
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.inputTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { next(selector = sel, inViewOnly = inViewOnly, targetElements = targetElements) }
    )
}

/**
 * nextInput
 */
fun TestElement.nextInput(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":nextInput($pos)",
        inViewOnly = inViewOnly,
    )
}

/**
 * nextInput
 */
fun TestElement.nextInput(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":nextInput($expression)",
        inViewOnly = inViewOnly,
    )
}

internal fun TestElement.preInput(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.inputTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { previous(selector = sel, inViewOnly = inViewOnly, targetElements = targetElements) }
    )
}

/**
 * preInput
 */
fun TestElement.preInput(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":preInput($pos)",
        inViewOnly = inViewOnly,
    )
}

/**
 * preInput
 */
fun TestElement.preInput(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":preInput($expression)",
        inViewOnly = inViewOnly,
    )
}

internal fun TestElement.nextImage(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.imageTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { next(selector = sel, inViewOnly = inViewOnly, targetElements = targetElements) }
    )
}

/**
 * nextImage
 */
fun TestElement.nextImage(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":nextImage($pos)",
        inViewOnly = inViewOnly,
    )
}

/**
 * nextImage
 */
fun TestElement.nextImage(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":nextImage($expression)",
        inViewOnly = inViewOnly,
    )
}

internal fun TestElement.preImage(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.imageTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { previous(selector = sel, inViewOnly = inViewOnly, targetElements = targetElements) }
    )
}

/**
 * preImage
 */
fun TestElement.preImage(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":preImage($pos)",
        inViewOnly = inViewOnly,
    )
}

/**
 * preImage
 */
fun TestElement.preImage(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":preImage($expression)",
        inViewOnly = inViewOnly,
    )
}

internal fun TestElement.nextButton(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.buttonTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { next(selector = sel, inViewOnly = inViewOnly, targetElements = targetElements) }
    )
}

/**
 * nextButton
 */
fun TestElement.nextButton(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":nextButton($pos)",
        inViewOnly = inViewOnly,
    )
}

/**
 * nextButton
 */
fun TestElement.nextButton(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":nextButton($expression)",
        inViewOnly = inViewOnly,
    )
}

internal fun TestElement.preButton(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.buttonTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { previous(selector = sel, inViewOnly = inViewOnly, targetElements = targetElements) }
    )
}

/**
 * preButton
 */
fun TestElement.preButton(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":preButton($pos)",
        inViewOnly = inViewOnly,
    )
}

/**
 * preButton
 */
fun TestElement.preButton(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":preButton($expression)",
        inViewOnly = inViewOnly,
    )
}

internal fun TestElement.nextSwitch(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.switchTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { next(selector = sel, inViewOnly = inViewOnly, targetElements = targetElements) }
    )
}

/**
 * nextSwitch
 */
fun TestElement.nextSwitch(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":nextSwitch($pos)",
        inViewOnly = inViewOnly,
    )
}

/**
 * nextSwitch
 */
fun TestElement.nextSwitch(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":nextSwitch($expression)",
        inViewOnly = inViewOnly,
    )
}

internal fun TestElement.preSwitch(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.switchTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { previous(selector = sel, inViewOnly = inViewOnly, targetElements = targetElements) }
    )
}

/**
 * preSwitch
 */
fun TestElement.preSwitch(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":preSwitch($pos)",
        inViewOnly = inViewOnly,
    )
}

/**
 * preSwitch
 */
fun TestElement.preSwitch(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":preSwitch($expression)",
        inViewOnly = inViewOnly,
    )
}

