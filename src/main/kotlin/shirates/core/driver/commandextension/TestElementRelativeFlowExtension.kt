package shirates.core.driver.commandextension

import shirates.core.configuration.Selector
import shirates.core.driver.TestDriver
import shirates.core.driver.TestElement
import shirates.core.driver.commandextension.helper.FlowContainer
import shirates.core.driver.commandextension.helper.VerticalFlowContainer
import shirates.core.driver.filterBySelector
import shirates.core.utility.element.ElementCategoryExpressionUtility

private fun TestElement.flowCore(
    selector: Selector,
    inViewOnly: Boolean,
    scopeElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.pos = null
    val filteredElements = scopeElements.filterBySelector(selector = sel, inViewOnly = inViewOnly)

    val fc = FlowContainer()
    val isThisContainingOthers =
        filteredElements.any { it.toString() != this.toString() && it.bounds.isIncludedIn(this.bounds) }
    if (isThisContainingOthers.not()) {
        fc.addElement(element = this, force = true)
    }
    fc.addAll(filteredElements)

    val flowElements = fc.getElements()
    val indexOfThis = flowElements.indexOf(this)
    for (i in 0..indexOfThis) {
        flowElements.removeAt(0)
    }

    val elements =
        flowElements.filterBySelector(selector = selector, inViewOnly = inViewOnly)   // filter with pos
    val e = elements.firstOrNull() ?: TestElement()
    e.selector = this.getChainedSelector(relativeCommand = "$selector")

    TestDriver.lastElement = e
    return e
}

private fun TestElement.vflowCore(
    selector: Selector,
    inViewOnly: Boolean,
    scopeElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.pos = null
    val filteredElements = scopeElements.filterBySelector(selector = sel, inViewOnly = inViewOnly)

    val vfc = VerticalFlowContainer()
    val isThisContainingOthers =
        filteredElements.any { it.toString() != this.toString() && it.bounds.isIncludedIn(this.bounds) }
    if (isThisContainingOthers.not()) {
        vfc.addElement(element = this, force = true)
    }
    vfc.addAll(filteredElements)

    val vflowElements = vfc.getElements()
    val indexOfThis = vflowElements.indexOf(this)
    for (i in 0..indexOfThis) {
        vflowElements.removeAt(0)
    }

    val elements =
        vflowElements.filterBySelector(selector = selector, inViewOnly = inViewOnly)   // filter with pos
    val e = elements.firstOrNull() ?: TestElement()
    e.selector = this.getChainedSelector(relativeCommand = "$selector")

    TestDriver.lastElement = e
    return e
}

internal fun TestElement.flow(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = execRelativeCommand(
        relativeSelector = selector,
        getElement = {
            flowCore(
                selector = selector,
                inViewOnly = inViewOnly,
                scopeElements = targetElements
            )
        }
    )

    return e
}

/**
 * flow
 */
fun TestElement.flow(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":flow($pos)",
        inViewOnly = inViewOnly,
        scopeElements = widgets
    )
}

/**
 * flow
 */
fun TestElement.flow(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":flow($expression)",
        inViewOnly = inViewOnly,
        scopeElements = widgets
    )
}

internal fun TestElement.flowLabel(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.labelTypesExpression
    return flow(selector = sel, inViewOnly = inViewOnly, targetElements = targetElements)
}

/**
 * flowLabel
 */
fun TestElement.flowLabel(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":flowLabel($pos)",
        inViewOnly = inViewOnly,
        scopeElements = labelWidgets
    )
}

/**
 * flowLabel
 */
fun TestElement.flowLabel(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":flowLabel($expression)",
        inViewOnly = inViewOnly,
        scopeElements = labelWidgets
    )
}

internal fun TestElement.flowInput(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.inputTypesExpression
    return flow(selector = sel, inViewOnly = inViewOnly, targetElements = targetElements)
}

/**
 * flowInput
 */
fun TestElement.flowInput(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":flowInput($pos)",
        inViewOnly = inViewOnly,
        scopeElements = inputWidgets
    )
}

/**
 * flowInput
 */
fun TestElement.flowInput(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":flowInput($expression)",
        inViewOnly = inViewOnly,
        scopeElements = inputWidgets
    )
}

internal fun TestElement.flowImage(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.imageTypesExpression
    return flow(selector = sel, inViewOnly = inViewOnly, targetElements = targetElements)
}

/**
 * flowImage
 */
fun TestElement.flowImage(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":flowImage($pos)",
        inViewOnly = inViewOnly,
        scopeElements = imageWidgets
    )
}

/**
 * flowImage
 */
fun TestElement.flowImage(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":flowImage($expression)",
        inViewOnly = inViewOnly,
        scopeElements = imageWidgets
    )
}

internal fun TestElement.flowButton(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.buttonTypesExpression
    return flow(selector = sel, inViewOnly = inViewOnly, targetElements = targetElements)
}

/**
 * flowButton
 */
fun TestElement.flowButton(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":flowButton($pos)",
        inViewOnly = inViewOnly,
        scopeElements = buttonWidgets
    )
}

/**
 * flowButton
 */
fun TestElement.flowButton(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":flowButton($expression)",
        inViewOnly = inViewOnly,
        scopeElements = buttonWidgets
    )
}

internal fun TestElement.flowSwitch(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.switchTypesExpression
    return flow(selector = sel, inViewOnly = inViewOnly, targetElements = targetElements)
}

/**
 * flowSwitch
 */
fun TestElement.flowSwitch(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":flowSwitch($pos)",
        inViewOnly = inViewOnly,
        scopeElements = switchWidgets
    )
}

/**
 * flowSwitch
 */
fun TestElement.flowSwitch(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":flowSwitch($expression)",
        inViewOnly = inViewOnly,
        scopeElements = switchWidgets
    )
}

internal fun TestElement.vflow(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    return execRelativeCommand(
        relativeSelector = selector,
        getElement = {
            vflowCore(
                selector = selector,
                inViewOnly = inViewOnly,
                scopeElements = targetElements
            )
        }
    )
}

/**
 * vflow
 */
fun TestElement.vflow(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":vflow($pos)",
        inViewOnly = inViewOnly,
        scopeElements = widgets
    )
}

/**
 * vflow
 */
fun TestElement.vflow(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":vflow($expression)",
        inViewOnly = inViewOnly,
        scopeElements = widgets
    )
}