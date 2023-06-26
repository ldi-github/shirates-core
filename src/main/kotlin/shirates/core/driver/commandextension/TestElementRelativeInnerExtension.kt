package shirates.core.driver.commandextension

import shirates.core.configuration.Selector
import shirates.core.driver.TestElement
import shirates.core.utility.element.ElementCategoryExpressionUtility

internal fun TestElement.innerFlow(
    selector: Selector,
    safeElementOnly: Boolean
): TestElement {

    val innerElements = this.descendants.filter { it.isWidget }.toMutableList()
    return this.flow(
        selector = selector,
        safeElementOnly = safeElementOnly,
        targetElements = innerElements
    )
}

/**
 * innerFlow
 */
fun TestElement.innerFlow(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerFlow($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = widgets
    )
}

/**
 * innerFlow
 */
fun TestElement.innerFlow(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerFlow($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = widgets
    )
}

internal fun TestElement.innerLabel(
    selector: Selector,
    safeElementOnly: Boolean
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.labelTypesExpression
    return this.innerFlow(selector = sel, safeElementOnly = safeElementOnly)
}

/**
 * innerLabel
 */
fun TestElement.innerLabel(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerLabel($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = labelWidgets
    )
}

/**
 * innerLabel
 */
fun TestElement.innerLabel(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerLabel($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = labelWidgets
    )
}

internal fun TestElement.innerInput(
    selector: Selector,
    safeElementOnly: Boolean
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.inputTypesExpression
    return this.innerFlow(selector = sel, safeElementOnly = safeElementOnly)
}

/**
 * innerInput
 */
fun TestElement.innerInput(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerInput($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = inputWidgets
    )
}

/**
 * innerInput
 */
fun TestElement.innerInput(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerInput($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = inputWidgets
    )
}

internal fun TestElement.innerImage(
    selector: Selector,
    safeElementOnly: Boolean
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.imageTypesExpression
    return this.innerFlow(selector = sel, safeElementOnly = safeElementOnly)
}

/**
 * innerImage
 */
fun TestElement.innerImage(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerImage($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = imageWidgets
    )
}

/**
 * innerImage
 */
fun TestElement.innerImage(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerImage($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = imageWidgets
    )
}

internal fun TestElement.innerButton(
    selector: Selector,
    safeElementOnly: Boolean
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.buttonTypesExpression
    return this.innerFlow(selector = sel, safeElementOnly = safeElementOnly)
}

/**
 * innerButton
 */
fun TestElement.innerButton(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerButton($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = buttonWidgets
    )
}

/**
 * innerButton
 */
fun TestElement.innerButton(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerButton($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = buttonWidgets
    )
}

internal fun TestElement.innerSwitch(
    selector: Selector,
    safeElementOnly: Boolean
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.switchTypesExpression
    return this.innerFlow(selector = sel, safeElementOnly = safeElementOnly)
}

/**
 * innerSwitch
 */
fun TestElement.innerSwitch(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerSwitch($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = switchWidgets
    )
}

/**
 * innerSwitch
 */
fun TestElement.innerSwitch(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerSwitch($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = switchWidgets
    )
}


internal fun TestElement.innerVflow(
    selector: Selector,
    safeElementOnly: Boolean
): TestElement {

    val innerElements = this.descendants.toMutableList()
    return this.vflow(
        selector = selector,
        safeElementOnly = safeElementOnly,
        targetElements = innerElements
    )
}

/**
 * innerVflow
 */
fun TestElement.innerVflow(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerVflow($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = widgets
    )
}

/**
 * innerVflow
 */
fun TestElement.innerVflow(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerVflow($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = widgets
    )
}

internal fun TestElement.innerVlabel(
    selector: Selector,
    safeElementOnly: Boolean
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.labelTypesExpression
    return this.innerVflow(selector = sel, safeElementOnly = safeElementOnly)
}

/**
 * innerVlabel
 */
fun TestElement.innerVlabel(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerVlabel($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = labelWidgets
    )
}

/**
 * innerVlabel
 */
fun TestElement.innerVlabel(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerVlabel($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = labelWidgets
    )
}

internal fun TestElement.innerVinput(
    selector: Selector,
    safeElementOnly: Boolean
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.inputTypesExpression
    return this.innerVflow(selector = sel, safeElementOnly = safeElementOnly)
}

/**
 * innerVinput
 */
fun TestElement.innerVinput(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerVinput($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = inputWidgets
    )
}

/**
 * innerVinput
 */
fun TestElement.innerVinput(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerVinput($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = inputWidgets
    )
}

internal fun TestElement.innerVimage(
    selector: Selector,
    safeElementOnly: Boolean
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.imageTypesExpression
    return this.innerVflow(selector = sel, safeElementOnly = safeElementOnly)
}

/**
 * innerVimage
 */
fun TestElement.innerVimage(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerVimage($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = imageWidgets
    )
}

/**
 * innerVimage
 */
fun TestElement.innerVimage(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerVimage($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = imageWidgets
    )
}

internal fun TestElement.innerVbutton(
    selector: Selector,
    safeElementOnly: Boolean
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.buttonTypesExpression
    return this.innerVflow(selector = sel, safeElementOnly = safeElementOnly)
}

/**
 * innerVbutton
 */
fun TestElement.innerVbutton(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerVbutton($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = buttonWidgets
    )
}

/**
 * innerVbutton
 */
fun TestElement.innerVbutton(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerVbutton($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = buttonWidgets
    )
}

internal fun TestElement.innerVswitch(
    selector: Selector,
    safeElementOnly: Boolean
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.switchTypesExpression
    return this.innerVflow(selector = sel, safeElementOnly = safeElementOnly)
}

/**
 * innerVswitch
 */
fun TestElement.innerVswitch(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerVswitch($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = switchWidgets
    )
}

/**
 * innerVswitch
 */
fun TestElement.innerVswitch(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":innerVswitch($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = switchWidgets
    )
}
