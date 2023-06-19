package shirates.core.driver.commandextension

import shirates.core.configuration.Selector
import shirates.core.driver.TestDriver
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestElement
import shirates.core.driver.filterBySelector
import shirates.core.utility.element.ElementCategoryExpressionUtility

internal fun TestElement.next(
    selector: Selector,
    safeElementOnly: Boolean,
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
                safeElementOnly = safeElementOnly,
                selectorForNextPrevious = sel
            )
        } else {
            e = nextPreviousCore(
                next = true,
                targetElements = targetElements,
                safeElementOnly = safeElementOnly,
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
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":next($expression)",
        safeElementOnly = safeElementOnly,
    )
}

/**
 * next
 */
fun TestElement.next(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":next($pos)",
        safeElementOnly = safeElementOnly,
    )
}

internal fun TestElement.previous(
    selector: Selector,
    safeElementOnly: Boolean,
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
                safeElementOnly = safeElementOnly,
                targetElements = targetElements,
                selectorForNextPrevious = sel
            )
        } else {
            e = nextPreviousCore(
                next = false,
                safeElementOnly = safeElementOnly,
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
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":previous($expression)",
        safeElementOnly = safeElementOnly,
    )
}

/**
 * previous
 */
fun TestElement.previous(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":previous($pos)",
        safeElementOnly = safeElementOnly,
    )
}

private fun TestElement.nextPreviousCore(
    next: Boolean,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>,
    selectorForNextPrevious: Selector
): TestElement {

    val currentIndex = targetElements.indexOf(this)

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
    filtered = filtered.filterBySelector(selector = sel, safeElementOnly = safeElementOnly)

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
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.labelTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { next(selector = sel, safeElementOnly = safeElementOnly, targetElements = targetElements) }
    )
}

/**
 * nextLabel
 */
fun TestElement.nextLabel(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":nextLabel($pos)",
        safeElementOnly = safeElementOnly,
    )
}

/**
 * nextLabel
 */
fun TestElement.nextLabel(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":nextLabel($expression)",
        safeElementOnly = safeElementOnly,
    )
}

internal fun TestElement.preLabel(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>,
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.labelTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { previous(selector = sel, safeElementOnly = safeElementOnly, targetElements = targetElements) }
    )
}

/**
 * preLabel
 */
fun TestElement.preLabel(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":preLabel($pos)",
        safeElementOnly = safeElementOnly,
    )
}

/**
 * preLabel
 */
fun TestElement.preLabel(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":preLabel($expression)",
        safeElementOnly = safeElementOnly,
    )
}

internal fun TestElement.nextInput(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>,
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.inputTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { next(selector = sel, safeElementOnly = safeElementOnly, targetElements = targetElements) }
    )
}

/**
 * nextInput
 */
fun TestElement.nextInput(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":nextInput($pos)",
        safeElementOnly = safeElementOnly,
    )
}

/**
 * nextInput
 */
fun TestElement.nextInput(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":nextInput($expression)",
        safeElementOnly = safeElementOnly,
    )
}

internal fun TestElement.preInput(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.inputTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { previous(selector = sel, safeElementOnly = safeElementOnly, targetElements = targetElements) }
    )
}

/**
 * preInput
 */
fun TestElement.preInput(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":preInput($pos)",
        safeElementOnly = safeElementOnly,
    )
}

/**
 * preInput
 */
fun TestElement.preInput(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":preInput($expression)",
        safeElementOnly = safeElementOnly,
    )
}

internal fun TestElement.nextImage(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.imageTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { next(selector = sel, safeElementOnly = safeElementOnly, targetElements = targetElements) }
    )
}

/**
 * nextImage
 */
fun TestElement.nextImage(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":nextImage($pos)",
        safeElementOnly = safeElementOnly,
    )
}

/**
 * nextImage
 */
fun TestElement.nextImage(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":nextImage($expression)",
        safeElementOnly = safeElementOnly,
    )
}

internal fun TestElement.preImage(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.imageTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { previous(selector = sel, safeElementOnly = safeElementOnly, targetElements = targetElements) }
    )
}

/**
 * preImage
 */
fun TestElement.preImage(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":preImage($pos)",
        safeElementOnly = safeElementOnly,
    )
}

/**
 * preImage
 */
fun TestElement.preImage(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":preImage($expression)",
        safeElementOnly = safeElementOnly,
    )
}

internal fun TestElement.nextButton(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.buttonTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { next(selector = sel, safeElementOnly = safeElementOnly, targetElements = targetElements) }
    )
}

/**
 * nextButton
 */
fun TestElement.nextButton(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":nextButton($pos)",
        safeElementOnly = safeElementOnly,
    )
}

/**
 * nextButton
 */
fun TestElement.nextButton(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":nextButton($expression)",
        safeElementOnly = safeElementOnly,
    )
}

internal fun TestElement.preButton(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.buttonTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { previous(selector = sel, safeElementOnly = safeElementOnly, targetElements = targetElements) }
    )
}

/**
 * preButton
 */
fun TestElement.preButton(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":preButton($pos)",
        safeElementOnly = safeElementOnly,
    )
}

/**
 * preButton
 */
fun TestElement.preButton(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":preButton($expression)",
        safeElementOnly = safeElementOnly,
    )
}

internal fun TestElement.nextSwitch(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.switchTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { next(selector = sel, safeElementOnly = safeElementOnly, targetElements = targetElements) }
    )
}

/**
 * nextSwitch
 */
fun TestElement.nextSwitch(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":nextSwitch($pos)",
        safeElementOnly = safeElementOnly,
    )
}

/**
 * nextSwitch
 */
fun TestElement.nextSwitch(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":nextSwitch($expression)",
        safeElementOnly = safeElementOnly,
    )
}

internal fun TestElement.preSwitch(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.switchTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { previous(selector = sel, safeElementOnly = safeElementOnly, targetElements = targetElements) }
    )
}

/**
 * preSwitch
 */
fun TestElement.preSwitch(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":preSwitch($pos)",
        safeElementOnly = safeElementOnly,
    )
}

/**
 * preSwitch
 */
fun TestElement.preSwitch(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":preSwitch($expression)",
        safeElementOnly = safeElementOnly,
    )
}

