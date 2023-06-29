package shirates.core.driver.commandextension

import shirates.core.configuration.Selector
import shirates.core.driver.TestElement
import shirates.core.utility.element.ElementCategoryExpressionUtility

internal fun TestElement.innerFlow(
    selector: Selector,
    inViewOnly: Boolean
): TestElement {

    val innerElements = this.descendants.filter { it.isWidget }.toMutableList()
    return this.flow(
        selector = selector,
        inViewOnly = inViewOnly,
        targetElements = innerElements
    )
}

/**
 * innerFlow
 */
fun TestElement.innerFlow(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerFlow($pos)",
        inViewOnly = inViewOnly,
        scopeElements = widgets
    )
}

/**
 * innerFlow
 */
fun TestElement.innerFlow(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerFlow($expression)",
        inViewOnly = inViewOnly,
        scopeElements = widgets
    )
}

internal fun TestElement.innerLabel(
    selector: Selector,
    inViewOnly: Boolean
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.labelTypesExpression
    return this.innerFlow(selector = sel, inViewOnly = inViewOnly)
}

/**
 * innerLabel
 */
fun TestElement.innerLabel(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerLabel($pos)",
        inViewOnly = inViewOnly,
        scopeElements = labelWidgets
    )
}

/**
 * innerLabel
 */
fun TestElement.innerLabel(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerLabel($expression)",
        inViewOnly = inViewOnly,
        scopeElements = labelWidgets
    )
}

internal fun TestElement.innerInput(
    selector: Selector,
    inViewOnly: Boolean
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.inputTypesExpression
    return this.innerFlow(selector = sel, inViewOnly = inViewOnly)
}

/**
 * innerInput
 */
fun TestElement.innerInput(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerInput($pos)",
        inViewOnly = inViewOnly,
        scopeElements = inputWidgets
    )
}

/**
 * innerInput
 */
fun TestElement.innerInput(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerInput($expression)",
        inViewOnly = inViewOnly,
        scopeElements = inputWidgets
    )
}

internal fun TestElement.innerImage(
    selector: Selector,
    inViewOnly: Boolean
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.imageTypesExpression
    return this.innerFlow(selector = sel, inViewOnly = inViewOnly)
}

/**
 * innerImage
 */
fun TestElement.innerImage(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerImage($pos)",
        inViewOnly = inViewOnly,
        scopeElements = imageWidgets
    )
}

/**
 * innerImage
 */
fun TestElement.innerImage(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerImage($expression)",
        inViewOnly = inViewOnly,
        scopeElements = imageWidgets
    )
}

internal fun TestElement.innerButton(
    selector: Selector,
    inViewOnly: Boolean
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.buttonTypesExpression
    return this.innerFlow(selector = sel, inViewOnly = inViewOnly)
}

/**
 * innerButton
 */
fun TestElement.innerButton(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerButton($pos)",
        inViewOnly = inViewOnly,
        scopeElements = buttonWidgets
    )
}

/**
 * innerButton
 */
fun TestElement.innerButton(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerButton($expression)",
        inViewOnly = inViewOnly,
        scopeElements = buttonWidgets
    )
}

internal fun TestElement.innerSwitch(
    selector: Selector,
    inViewOnly: Boolean
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.switchTypesExpression
    return this.innerFlow(selector = sel, inViewOnly = inViewOnly)
}

/**
 * innerSwitch
 */
fun TestElement.innerSwitch(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerSwitch($pos)",
        inViewOnly = inViewOnly,
        scopeElements = switchWidgets
    )
}

/**
 * innerSwitch
 */
fun TestElement.innerSwitch(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerSwitch($expression)",
        inViewOnly = inViewOnly,
        scopeElements = switchWidgets
    )
}


internal fun TestElement.innerVflow(
    selector: Selector,
    inViewOnly: Boolean
): TestElement {

    val innerElements = this.descendants.toMutableList()
    return this.vflow(
        selector = selector,
        inViewOnly = inViewOnly,
        targetElements = innerElements
    )
}

/**
 * innerVflow
 */
fun TestElement.innerVflow(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerVflow($pos)",
        inViewOnly = inViewOnly,
        scopeElements = widgets
    )
}

/**
 * innerVflow
 */
fun TestElement.innerVflow(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerVflow($expression)",
        inViewOnly = inViewOnly,
        scopeElements = widgets
    )
}

internal fun TestElement.innerVlabel(
    selector: Selector,
    inViewOnly: Boolean
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.labelTypesExpression
    return this.innerVflow(selector = sel, inViewOnly = inViewOnly)
}

/**
 * innerVlabel
 */
fun TestElement.innerVlabel(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerVlabel($pos)",
        inViewOnly = inViewOnly,
        scopeElements = labelWidgets
    )
}

/**
 * innerVlabel
 */
fun TestElement.innerVlabel(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerVlabel($expression)",
        inViewOnly = inViewOnly,
        scopeElements = labelWidgets
    )
}

internal fun TestElement.innerVinput(
    selector: Selector,
    inViewOnly: Boolean
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.inputTypesExpression
    return this.innerVflow(selector = sel, inViewOnly = inViewOnly)
}

/**
 * innerVinput
 */
fun TestElement.innerVinput(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerVinput($pos)",
        inViewOnly = inViewOnly,
        scopeElements = inputWidgets
    )
}

/**
 * innerVinput
 */
fun TestElement.innerVinput(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerVinput($expression)",
        inViewOnly = inViewOnly,
        scopeElements = inputWidgets
    )
}

internal fun TestElement.innerVimage(
    selector: Selector,
    inViewOnly: Boolean
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.imageTypesExpression
    return this.innerVflow(selector = sel, inViewOnly = inViewOnly)
}

/**
 * innerVimage
 */
fun TestElement.innerVimage(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerVimage($pos)",
        inViewOnly = inViewOnly,
        scopeElements = imageWidgets
    )
}

/**
 * innerVimage
 */
fun TestElement.innerVimage(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerVimage($expression)",
        inViewOnly = inViewOnly,
        scopeElements = imageWidgets
    )
}

internal fun TestElement.innerVbutton(
    selector: Selector,
    inViewOnly: Boolean
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.buttonTypesExpression
    return this.innerVflow(selector = sel, inViewOnly = inViewOnly)
}

/**
 * innerVbutton
 */
fun TestElement.innerVbutton(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerVbutton($pos)",
        inViewOnly = inViewOnly,
        scopeElements = buttonWidgets
    )
}

/**
 * innerVbutton
 */
fun TestElement.innerVbutton(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerVbutton($expression)",
        inViewOnly = inViewOnly,
        scopeElements = buttonWidgets
    )
}

internal fun TestElement.innerVswitch(
    selector: Selector,
    inViewOnly: Boolean
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.switchTypesExpression
    return this.innerVflow(selector = sel, inViewOnly = inViewOnly)
}

/**
 * innerVswitch
 */
fun TestElement.innerVswitch(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerVswitch($pos)",
        inViewOnly = inViewOnly,
        scopeElements = switchWidgets
    )
}

/**
 * innerVswitch
 */
fun TestElement.innerVswitch(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerVswitch($expression)",
        inViewOnly = inViewOnly,
        scopeElements = switchWidgets
    )
}
