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
    safeElementOnly: Boolean,
    scopeElements: List<TestElement>
): TestElement {


    val sel = selector.copy()
    sel.pos = null
    val filteredElements = scopeElements.filterBySelector(selector = sel, safeElementOnly = safeElementOnly)

    val fc = FlowContainer()
    val isThisContainingOthers = scopeElements.any { it != this && it.bounds.isIncludedIn(this.bounds) }
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
        flowElements.filterBySelector(selector = selector, safeElementOnly = safeElementOnly)   // filter with pos
    val e = elements.firstOrNull() ?: TestElement()
    e.selector = this.getChainedSelector(relativeCommand = "$selector")

    TestDriver.lastElement = e
    return e
}

private fun TestElement.vflowCore(
    selector: Selector,
    safeElementOnly: Boolean,
    scopeElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.pos = null
    val filteredElements = scopeElements.filterBySelector(selector = sel, safeElementOnly = safeElementOnly)

    val vfc = VerticalFlowContainer()
    val isThisContainingOthers = scopeElements.any { it != this && it.bounds.isIncludedIn(this.bounds) }
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
        vflowElements.filterBySelector(selector = selector, safeElementOnly = safeElementOnly)   // filter with pos
    val e = elements.firstOrNull() ?: TestElement()
    e.selector = this.getChainedSelector(relativeCommand = "$selector")

    TestDriver.lastElement = e
    return e
}

internal fun TestElement.flow(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = execRelativeCommand(
        relativeSelector = selector,
        getElement = {
            flowCore(
                selector = selector,
                safeElementOnly = safeElementOnly,
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
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":flow($pos)", safeElementOnly = safeElementOnly)
}

/**
 * flow
 */
fun TestElement.flow(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":flow($expression)", safeElementOnly = safeElementOnly)
}

internal fun TestElement.flowLabel(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.labelTypesExpression
    return flow(selector = sel, safeElementOnly = safeElementOnly, targetElements = targetElements)
}

/**
 * flowLabel
 */
fun TestElement.flowLabel(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":flowLabel($pos)", safeElementOnly = safeElementOnly)
}

/**
 * flowLabel
 */
fun TestElement.flowLabel(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":flowLabel($expression)", safeElementOnly = safeElementOnly)
}

internal fun TestElement.flowInput(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.inputTypesExpression
    return flow(selector = sel, safeElementOnly = safeElementOnly, targetElements = targetElements)
}

/**
 * flowInput
 */
fun TestElement.flowInput(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":flowInput($pos)", safeElementOnly = safeElementOnly)
}

/**
 * flowInput
 */
fun TestElement.flowInput(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":flowInput($expression)", safeElementOnly = safeElementOnly)
}

internal fun TestElement.flowImage(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.imageTypesExpression
    return flow(selector = sel, safeElementOnly = safeElementOnly, targetElements = targetElements)
}

/**
 * flowImage
 */
fun TestElement.flowImage(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":flowImage($pos)", safeElementOnly = safeElementOnly)
}

/**
 * flowImage
 */
fun TestElement.flowImage(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":flowImage($expression)", safeElementOnly = safeElementOnly)
}

internal fun TestElement.flowButton(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.buttonTypesExpression
    return flow(selector = sel, safeElementOnly = safeElementOnly, targetElements = targetElements)
}

/**
 * flowButton
 */
fun TestElement.flowButton(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":flowButton($pos)", safeElementOnly = safeElementOnly)
}

/**
 * flowButton
 */
fun TestElement.flowButton(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":flowButton($expression)", safeElementOnly = safeElementOnly)
}

internal fun TestElement.flowSwitch(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.switchTypesExpression
    return flow(selector = sel, safeElementOnly = safeElementOnly, targetElements = targetElements)
}

/**
 * flowSwitch
 */
fun TestElement.flowSwitch(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":flowSwitch($pos)", safeElementOnly = safeElementOnly)
}

/**
 * flowSwitch
 */
fun TestElement.flowSwitch(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":flowSwitch($expression)", safeElementOnly = safeElementOnly)
}

internal fun TestElement.vflow(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    return execRelativeCommand(
        relativeSelector = selector,
        getElement = {
            vflowCore(
                selector = selector,
                safeElementOnly = safeElementOnly,
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
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":vflow($pos)", safeElementOnly = safeElementOnly)
}

/**
 * vflow
 */
fun TestElement.vflow(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":vflow($expression)", safeElementOnly = safeElementOnly)
}