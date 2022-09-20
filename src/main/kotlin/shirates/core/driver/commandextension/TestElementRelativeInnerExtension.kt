package shirates.core.driver.commandextension

import shirates.core.configuration.Selector
import shirates.core.driver.TestElement
import shirates.core.driver.descendants
import shirates.core.utility.element.ElementCategoryExpressionUtility

internal fun TestElement.innerFlow(
    selector: Selector
): TestElement {

    val innerElements = this.descendants.toMutableList()
    return this.flow(
        selector = selector,
        targetElements = innerElements
    )
}

/**
 * innerFlow
 */
fun TestElement.innerFlow(
    pos: Int = 1,
): TestElement {

    return relative(":innerFlow($pos)")
}

/**
 * innerFlow
 */
fun TestElement.innerFlow(
    expression: String,
): TestElement {

    return relative(":innerFlow($expression)")
}

internal fun TestElement.innerLabel(
    selector: Selector
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.labelTypesExpression
    return this.innerFlow(selector = sel)
}

/**
 * innerLabel
 */
fun TestElement.innerLabel(
    pos: Int = 1,
): TestElement {

    return relative(":innerLabel($pos)")
}

/**
 * innerLabel
 */
fun TestElement.innerLabel(
    expression: String,
): TestElement {

    return relative(":innerLabel($expression)")
}

internal fun TestElement.innerInput(
    selector: Selector
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.inputTypesExpression
    return this.innerFlow(selector = sel)
}

/**
 * innerInput
 */
fun TestElement.innerInput(
    pos: Int = 1,
): TestElement {

    return relative(":innerInput($pos)")
}

/**
 * innerInput
 */
fun TestElement.innerInput(
    expression: String,
): TestElement {

    return relative(":innerInput($expression)")
}

internal fun TestElement.innerImage(
    selector: Selector
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.imageTypesExpression
    return this.innerFlow(selector = sel)
}

/**
 * innerImage
 */
fun TestElement.innerImage(
    pos: Int = 1,
): TestElement {

    return relative(":innerImage($pos)")
}

/**
 * innerImage
 */
fun TestElement.innerImage(
    expression: String,
): TestElement {

    return relative(":innerImage($expression)")
}

internal fun TestElement.innerButton(
    selector: Selector
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.buttonTypesExpression
    return this.innerFlow(selector = sel)
}

/**
 * innerButton
 */
fun TestElement.innerButton(
    pos: Int = 1,
): TestElement {

    return relative(":innerButton($pos)")
}

/**
 * innerButton
 */
fun TestElement.innerButton(
    expression: String,
): TestElement {

    return relative(":innerButton($expression)")
}

internal fun TestElement.innerSwitch(
    selector: Selector
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.switchTypesExpression
    return this.innerFlow(selector = sel)
}

/**
 * innerSwitch
 */
fun TestElement.innerSwitch(
    pos: Int = 1,
): TestElement {

    return relative(":innerSwitch($pos)")
}

/**
 * innerSwitch
 */
fun TestElement.innerSwitch(
    expression: String,
): TestElement {

    return relative(":innerSwitch($expression)")
}


internal fun TestElement.innerVflow(
    selector: Selector
): TestElement {

    val innerElements = this.descendants.toMutableList()
    return this.vflow(
        selector = selector,
        targetElements = innerElements
    )
}

/**
 * innerVflow
 */
fun TestElement.innerVflow(
    pos: Int = 1,
): TestElement {

    return relative(":innerVflow($pos)")
}

/**
 * innerVflow
 */
fun TestElement.innerVflow(
    expression: String,
): TestElement {

    return relative(":innerVflow($expression)")
}

internal fun TestElement.innerVlabel(
    selector: Selector
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.labelTypesExpression
    return this.innerVflow(selector = sel)
}

/**
 * innerVlabel
 */
fun TestElement.innerVlabel(
    pos: Int = 1,
): TestElement {

    return relative(":innerVlabel($pos)")
}

/**
 * innerVlabel
 */
fun TestElement.innerVlabel(
    expression: String,
): TestElement {

    return relative(":innerVlabel($expression)")
}

internal fun TestElement.innerVinput(
    selector: Selector
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.inputTypesExpression
    return this.innerVflow(selector = sel)
}

/**
 * innerVinput
 */
fun TestElement.innerVinput(
    pos: Int = 1,
): TestElement {

    return relative(":innerVinput($pos)")
}

/**
 * innerVinput
 */
fun TestElement.innerVinput(
    expression: String,
): TestElement {

    return relative(":innerVinput($expression)")
}

internal fun TestElement.innerVimage(
    selector: Selector
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.imageTypesExpression
    return this.innerVflow(selector = sel)
}

/**
 * innerVimage
 */
fun TestElement.innerVimage(
    pos: Int = 1,
): TestElement {

    return relative(":innerVimage($pos)")
}

/**
 * innerVimage
 */
fun TestElement.innerVimage(
    expression: String,
): TestElement {

    return relative(":innerVimage($expression)")
}

internal fun TestElement.innerVbutton(
    selector: Selector
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.buttonTypesExpression
    return this.innerVflow(selector = sel)
}

/**
 * innerVbutton
 */
fun TestElement.innerVbutton(
    pos: Int = 1,
): TestElement {

    return relative(":innerVbutton($pos)")
}

/**
 * innerVbutton
 */
fun TestElement.innerVbutton(
    expression: String,
): TestElement {

    return relative(":innerVbutton($expression)")
}

internal fun TestElement.innerVswitch(
    selector: Selector
): TestElement {

    val sel = selector.copy()
    sel.className = ElementCategoryExpressionUtility.switchTypesExpression
    return this.innerVflow(selector = sel)
}

/**
 * innerVswitch
 */
fun TestElement.innerVswitch(
    pos: Int = 1,
): TestElement {

    return relative(":innerVswitch($pos)")
}

/**
 * innerVswitch
 */
fun TestElement.innerVswitch(
    expression: String,
): TestElement {

    return relative(":innerVswitch($expression)")
}
