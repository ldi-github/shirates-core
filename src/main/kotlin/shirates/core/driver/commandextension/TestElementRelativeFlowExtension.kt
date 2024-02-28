package shirates.core.driver.commandextension

import shirates.core.configuration.Selector
import shirates.core.driver.Bounds
import shirates.core.driver.TestDriver
import shirates.core.driver.TestElement
import shirates.core.driver.commandextension.helper.FlowContainer
import shirates.core.driver.commandextension.helper.VerticalFlowContainer
import shirates.core.driver.filterBySelector
import shirates.core.utility.element.ElementCategoryExpressionUtility

private fun TestElement.flowCore(
    selector: Selector,
    scopeElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val sel = selector.copy()
    sel.pos = null
    val filteredElements = scopeElements.filterBySelector(selector = sel, frame = frame)

    val fc = FlowContainer()
    val isThisContainingOthers =
        filteredElements.any { it.toString() != this.toString() && it.bounds.isIncludedIn(this.bounds) }
    if (isThisContainingOthers.not()) {
        fc.addElementToRow(element = this, force = true)
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
    scopeElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val sel = selector.copy()
    sel.pos = null
    val filteredElements = scopeElements.filterBySelector(selector = sel, frame = frame)

    val vfc = VerticalFlowContainer()
    val isThisContainingOthers =
        filteredElements.any { it.toString() != this.toString() && it.bounds.isIncludedIn(this.bounds) }
    if (isThisContainingOthers.not()) {
        vfc.addElementToColumn(element = this, force = true)
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
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val e = execRelativeCommand(
        relativeSelector = selector,
        getElement = {
            flowCore(
                selector = selector,
                scopeElements = targetElements,
                frame = frame
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
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":flow($pos)",
        scopeElements = widgets,
        frame = frame
    )
}

/**
 * flow
 */
fun TestElement.flow(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":flow($expression)",
        scopeElements = widgets,
        frame = frame
    )
}

internal fun TestElement.flowLabel(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.labelTypesExpression
    return flow(selector = sel, targetElements = targetElements, frame = frame)
}

/**
 * flowLabel
 */
fun TestElement.flowLabel(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":flowLabel($pos)",
        scopeElements = labelWidgets,
        frame = frame
    )
}

/**
 * flowLabel
 */
fun TestElement.flowLabel(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":flowLabel($expression)",
        scopeElements = labelWidgets,
        frame = frame
    )
}

internal fun TestElement.flowInput(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.inputTypesExpression
    return flow(selector = sel, targetElements = targetElements, frame = frame)
}

/**
 * flowInput
 */
fun TestElement.flowInput(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":flowInput($pos)",
        scopeElements = inputWidgets,
        frame = frame
    )
}

/**
 * flowInput
 */
fun TestElement.flowInput(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":flowInput($expression)",
        scopeElements = inputWidgets,
        frame = frame
    )
}

internal fun TestElement.flowImage(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.imageTypesExpression
    return flow(selector = sel, targetElements = targetElements, frame = frame)
}

/**
 * flowImage
 */
fun TestElement.flowImage(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":flowImage($pos)",
        scopeElements = imageWidgets,
        frame = frame
    )
}

/**
 * flowImage
 */
fun TestElement.flowImage(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":flowImage($expression)",
        scopeElements = imageWidgets,
        frame = frame
    )
}

internal fun TestElement.flowButton(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.buttonTypesExpression
    return flow(selector = sel, targetElements = targetElements, frame = frame)
}

/**
 * flowButton
 */
fun TestElement.flowButton(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":flowButton($pos)",
        scopeElements = buttonWidgets,
        frame = frame
    )
}

/**
 * flowButton
 */
fun TestElement.flowButton(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":flowButton($expression)",
        scopeElements = buttonWidgets,
        frame = frame
    )
}

internal fun TestElement.flowSwitch(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.switchTypesExpression
    return flow(selector = sel, targetElements = targetElements, frame = frame)
}

/**
 * flowSwitch
 */
fun TestElement.flowSwitch(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":flowSwitch($pos)",
        scopeElements = switchWidgets,
        frame = frame
    )
}

/**
 * flowSwitch
 */
fun TestElement.flowSwitch(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":flowSwitch($expression)",
        scopeElements = switchWidgets,
        frame = frame
    )
}

internal fun TestElement.vflow(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    return execRelativeCommand(
        relativeSelector = selector,
        getElement = {
            vflowCore(
                selector = selector,
                scopeElements = targetElements,
                frame = frame
            )
        }
    )
}

/**
 * vflow
 */
fun TestElement.vflow(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":vflow($pos)",
        scopeElements = widgets,
        frame = frame
    )
}

/**
 * vflow
 */
fun TestElement.vflow(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":vflow($expression)",
        scopeElements = widgets,
        frame = frame
    )
}