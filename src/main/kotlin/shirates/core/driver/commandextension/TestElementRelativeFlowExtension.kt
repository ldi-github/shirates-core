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
    scopeElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.pos = null
    val filteredElements = scopeElements.filterBySelector(selector = sel)

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
        flowElements.filterBySelector(selector = selector)   // filter with pos
    val e = elements.firstOrNull() ?: TestElement()
    e.selector = this.getChainedSelector(relativeCommand = "$selector")

    TestDriver.lastElement = e
    return e
}

private fun TestElement.vflowCore(
    selector: Selector,
    scopeElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.pos = null
    val filteredElements = scopeElements.filterBySelector(selector = sel)

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
        vflowElements.filterBySelector(selector = selector)   // filter with pos
    val e = elements.firstOrNull() ?: TestElement()
    e.selector = this.getChainedSelector(relativeCommand = "$selector")

    TestDriver.lastElement = e
    return e
}

internal fun TestElement.flow(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val e = execRelativeCommand(
        relativeSelector = selector,
        getElement = {
            flowCore(
                selector = selector,
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
    pos: Int = 1
): TestElement {

    return relative(
        command = ":flow($pos)",
        scopeElements = widgets
    )
}

/**
 * flow
 */
fun TestElement.flow(
    expression: String
): TestElement {

    return relative(
        command = ":flow($expression)",
        scopeElements = widgets
    )
}

internal fun TestElement.flowLabel(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.labelTypesExpression
    return flow(selector = sel, targetElements = targetElements)
}

/**
 * flowLabel
 */
fun TestElement.flowLabel(
    pos: Int = 1
): TestElement {

    return relative(
        command = ":flowLabel($pos)",
        scopeElements = labelWidgets
    )
}

/**
 * flowLabel
 */
fun TestElement.flowLabel(
    expression: String
): TestElement {

    return relative(
        command = ":flowLabel($expression)",
        scopeElements = labelWidgets
    )
}

internal fun TestElement.flowInput(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.inputTypesExpression
    return flow(selector = sel, targetElements = targetElements)
}

/**
 * flowInput
 */
fun TestElement.flowInput(
    pos: Int = 1
): TestElement {

    return relative(
        command = ":flowInput($pos)",
        scopeElements = inputWidgets
    )
}

/**
 * flowInput
 */
fun TestElement.flowInput(
    expression: String
): TestElement {

    return relative(
        command = ":flowInput($expression)",
        scopeElements = inputWidgets
    )
}

internal fun TestElement.flowImage(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.imageTypesExpression
    return flow(selector = sel, targetElements = targetElements)
}

/**
 * flowImage
 */
fun TestElement.flowImage(
    pos: Int = 1
): TestElement {

    return relative(
        command = ":flowImage($pos)",
        scopeElements = imageWidgets
    )
}

/**
 * flowImage
 */
fun TestElement.flowImage(
    expression: String
): TestElement {

    return relative(
        command = ":flowImage($expression)",
        scopeElements = imageWidgets
    )
}

internal fun TestElement.flowButton(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.buttonTypesExpression
    return flow(selector = sel, targetElements = targetElements)
}

/**
 * flowButton
 */
fun TestElement.flowButton(
    pos: Int = 1
): TestElement {

    return relative(
        command = ":flowButton($pos)",
        scopeElements = buttonWidgets
    )
}

/**
 * flowButton
 */
fun TestElement.flowButton(
    expression: String
): TestElement {

    return relative(
        command = ":flowButton($expression)",
        scopeElements = buttonWidgets
    )
}

internal fun TestElement.flowSwitch(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.switchTypesExpression
    return flow(selector = sel, targetElements = targetElements)
}

/**
 * flowSwitch
 */
fun TestElement.flowSwitch(
    pos: Int = 1
): TestElement {

    return relative(
        command = ":flowSwitch($pos)",
        scopeElements = switchWidgets
    )
}

/**
 * flowSwitch
 */
fun TestElement.flowSwitch(
    expression: String
): TestElement {

    return relative(
        command = ":flowSwitch($expression)",
        scopeElements = switchWidgets
    )
}

internal fun TestElement.vflow(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    return execRelativeCommand(
        relativeSelector = selector,
        getElement = {
            vflowCore(
                selector = selector,
                scopeElements = targetElements
            )
        }
    )
}

/**
 * vflow
 */
fun TestElement.vflow(
    pos: Int = 1
): TestElement {

    return relative(
        command = ":vflow($pos)",
        scopeElements = widgets
    )
}

/**
 * vflow
 */
fun TestElement.vflow(
    expression: String
): TestElement {

    return relative(
        command = ":vflow($expression)",
        scopeElements = widgets
    )
}