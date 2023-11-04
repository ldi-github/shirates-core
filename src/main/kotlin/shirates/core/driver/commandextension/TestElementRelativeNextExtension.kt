package shirates.core.driver.commandextension

import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.utility.element.ElementCategoryExpressionUtility

internal fun TestElement.next(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds? = null
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
                selectorForNextPrevious = sel,
                frame = frame
            )
        } else {
            e = nextPreviousCore(
                next = true,
                targetElements = targetElements,
                selectorForNextPrevious = selector,
                frame = frame
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
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":next($expression)",
        frame = frame
    )
}

/**
 * next
 */
fun TestElement.next(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":next($pos)",
        frame = frame
    )
}

internal fun TestElement.previous(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
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
                targetElements = targetElements,
                selectorForNextPrevious = sel,
                frame = frame
            )
        } else {
            e = nextPreviousCore(
                next = false,
                targetElements = targetElements,
                selectorForNextPrevious = selector,
                frame = frame
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
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":previous($expression)",
        frame = frame
    )
}

/**
 * previous
 */
fun TestElement.previous(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":previous($pos)",
        frame = frame
    )
}

private fun TestElement.nextPreviousCore(
    next: Boolean,
    targetElements: List<TestElement>,
    selectorForNextPrevious: Selector,
    frame: Bounds?
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
    filtered = filtered.filterBySelector(selector = sel, frame = frame)

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
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.labelTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { next(selector = sel, targetElements = targetElements, frame = frame) }
    )
}

/**
 * nextLabel
 */
fun TestElement.nextLabel(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":nextLabel($pos)",
        frame = frame
    )
}

/**
 * nextLabel
 */
fun TestElement.nextLabel(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":nextLabel($expression)",
        frame = frame
    )
}

internal fun TestElement.preLabel(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.labelTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { previous(selector = sel, targetElements = targetElements, frame = frame) }
    )
}

/**
 * preLabel
 */
fun TestElement.preLabel(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":preLabel($pos)",
        frame = frame
    )
}

/**
 * preLabel
 */
fun TestElement.preLabel(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":preLabel($expression)",
        frame = frame
    )
}

internal fun TestElement.nextInput(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.inputTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { next(selector = sel, targetElements = targetElements, frame = frame) }
    )
}

/**
 * nextInput
 */
fun TestElement.nextInput(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":nextInput($pos)",
        frame = frame
    )
}

/**
 * nextInput
 */
fun TestElement.nextInput(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":nextInput($expression)",
        frame = frame
    )
}

internal fun TestElement.preInput(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.inputTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { previous(selector = sel, targetElements = targetElements, frame = frame) }
    )
}

/**
 * preInput
 */
fun TestElement.preInput(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":preInput($pos)",
        frame = frame
    )
}

/**
 * preInput
 */
fun TestElement.preInput(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":preInput($expression)",
        frame = frame
    )
}

internal fun TestElement.nextImage(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.imageTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { next(selector = sel, targetElements = targetElements, frame = frame) }
    )
}

/**
 * nextImage
 */
fun TestElement.nextImage(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":nextImage($pos)",
        frame = frame
    )
}

/**
 * nextImage
 */
fun TestElement.nextImage(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":nextImage($expression)",
        frame = frame
    )
}

internal fun TestElement.preImage(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.imageTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { previous(selector = sel, targetElements = targetElements, frame = frame) }
    )
}

/**
 * preImage
 */
fun TestElement.preImage(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":preImage($pos)",
        frame = frame
    )
}

/**
 * preImage
 */
fun TestElement.preImage(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":preImage($expression)",
        frame = frame
    )
}

internal fun TestElement.nextButton(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.buttonTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { next(selector = sel, targetElements = targetElements, frame = frame) }
    )
}

/**
 * nextButton
 */
fun TestElement.nextButton(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":nextButton($pos)",
        frame = frame
    )
}

/**
 * nextButton
 */
fun TestElement.nextButton(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":nextButton($expression)",
        frame = frame
    )
}

internal fun TestElement.preButton(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.buttonTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { previous(selector = sel, targetElements = targetElements, frame = frame) }
    )
}

/**
 * preButton
 */
fun TestElement.preButton(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":preButton($pos)",
        frame = frame
    )
}

/**
 * preButton
 */
fun TestElement.preButton(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":preButton($expression)",
        frame = frame
    )
}

internal fun TestElement.nextSwitch(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.switchTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { next(selector = sel, targetElements = targetElements, frame = frame) }
    )
}

/**
 * nextSwitch
 */
fun TestElement.nextSwitch(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":nextSwitch($pos)",
        frame = frame
    )
}

/**
 * nextSwitch
 */
fun TestElement.nextSwitch(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":nextSwitch($expression)",
        frame = frame
    )
}

internal fun TestElement.preSwitch(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.switchTypesExpression
    return execRelativeCommand(
        relativeSelector = sel,
        getElement = { previous(selector = sel, targetElements = targetElements, frame = frame) }
    )
}

/**
 * preSwitch
 */
fun TestElement.preSwitch(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":preSwitch($pos)",
        frame = frame
    )
}

/**
 * preSwitch
 */
fun TestElement.preSwitch(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":preSwitch($expression)",
        frame = frame
    )
}

