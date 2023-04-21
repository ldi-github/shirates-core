package shirates.core.driver.commandextension

import shirates.core.configuration.Selector
import shirates.core.driver.TestElement
import shirates.core.driver.descendants
import shirates.core.utility.element.ElementCategoryExpressionUtility

internal fun TestElement.innerFlow(
    selector: Selector,
    safeElementOnly: Boolean
): TestElement {

    val innerElements = this.descendants.toMutableList()
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

    return relative(":innerFlow($pos)", safeElementOnly = safeElementOnly)
}

/**
 * innerFlow
 */
fun TestElement.innerFlow(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":innerFlow($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":innerLabel($pos)", safeElementOnly = safeElementOnly)
}

/**
 * innerLabel
 */
fun TestElement.innerLabel(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":innerLabel($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":innerInput($pos)", safeElementOnly = safeElementOnly)
}

/**
 * innerInput
 */
fun TestElement.innerInput(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":innerInput($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":innerImage($pos)", safeElementOnly = safeElementOnly)
}

/**
 * innerImage
 */
fun TestElement.innerImage(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":innerImage($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":innerButton($pos)", safeElementOnly = safeElementOnly)
}

/**
 * innerButton
 */
fun TestElement.innerButton(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":innerButton($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":innerSwitch($pos)", safeElementOnly = safeElementOnly)
}

/**
 * innerSwitch
 */
fun TestElement.innerSwitch(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":innerSwitch($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":innerVflow($pos)", safeElementOnly = safeElementOnly)
}

/**
 * innerVflow
 */
fun TestElement.innerVflow(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":innerVflow($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":innerVlabel($pos)", safeElementOnly = safeElementOnly)
}

/**
 * innerVlabel
 */
fun TestElement.innerVlabel(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":innerVlabel($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":innerVinput($pos)", safeElementOnly = safeElementOnly)
}

/**
 * innerVinput
 */
fun TestElement.innerVinput(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":innerVinput($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":innerVimage($pos)", safeElementOnly = safeElementOnly)
}

/**
 * innerVimage
 */
fun TestElement.innerVimage(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":innerVimage($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":innerVbutton($pos)", safeElementOnly = safeElementOnly)
}

/**
 * innerVbutton
 */
fun TestElement.innerVbutton(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":innerVbutton($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":innerVswitch($pos)", safeElementOnly = safeElementOnly)
}

/**
 * innerVswitch
 */
fun TestElement.innerVswitch(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":innerVswitch($expression)", safeElementOnly = safeElementOnly)
}
