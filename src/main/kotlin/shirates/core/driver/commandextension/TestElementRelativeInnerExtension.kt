package shirates.core.driver.commandextension

import shirates.core.configuration.Selector
import shirates.core.driver.TestElement

internal val TestElement.innerWidgets: List<TestElement>
    get() {
        return this.descendants.filter { it.isWidget }.toMutableList()
    }

internal val TestElement.innerLabelWidgets: List<TestElement>
    get() {
        return this.descendants.filter { it.isLabel }.toMutableList()
    }

internal val TestElement.innerInputWidgets: List<TestElement>
    get() {
        return this.descendants.filter { it.isInput }.toMutableList()
    }

internal val TestElement.innerImageWidgets: List<TestElement>
    get() {
        return this.descendants.filter { it.isImage }.toMutableList()
    }

internal val TestElement.innerButtonWidgets: List<TestElement>
    get() {
        return this.descendants.filter { it.isButton }.toMutableList()
    }

internal val TestElement.innerSwitchWidgets: List<TestElement>
    get() {
        return this.descendants.filter { it.isSwitch }.toMutableList()
    }


internal fun TestElement.innerFlow(
    selector: Selector,
    inViewOnly: Boolean
): TestElement {

    return this.flow(
        selector = selector,
        inViewOnly = inViewOnly,
        targetElements = innerWidgets
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
        scopeElements = innerWidgets
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
        scopeElements = innerWidgets
    )
}

internal fun TestElement.innerLabel(
    selector: Selector,
    inViewOnly: Boolean
): TestElement {

    return this.flow(
        selector = selector,
        inViewOnly = inViewOnly,
        targetElements = innerLabelWidgets
    )
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
        scopeElements = innerLabelWidgets
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
        scopeElements = innerLabelWidgets
    )
}

internal fun TestElement.innerInput(
    selector: Selector,
    inViewOnly: Boolean
): TestElement {

    return this.flow(
        selector = selector,
        inViewOnly = inViewOnly,
        targetElements = innerInputWidgets
    )
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
        scopeElements = innerInputWidgets
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
        scopeElements = innerInputWidgets
    )
}

internal fun TestElement.innerImage(
    selector: Selector,
    inViewOnly: Boolean
): TestElement {

    return this.flow(
        selector = selector,
        inViewOnly = inViewOnly,
        targetElements = innerImageWidgets
    )
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
        scopeElements = innerImageWidgets
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
        scopeElements = innerImageWidgets
    )
}

internal fun TestElement.innerButton(
    selector: Selector,
    inViewOnly: Boolean
): TestElement {

    return this.flow(
        selector = selector,
        inViewOnly = inViewOnly,
        targetElements = innerButtonWidgets
    )
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
        scopeElements = innerButtonWidgets
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
        scopeElements = innerButtonWidgets
    )
}

internal fun TestElement.innerSwitch(
    selector: Selector,
    inViewOnly: Boolean
): TestElement {

    return this.flow(
        selector = selector,
        inViewOnly = inViewOnly,
        targetElements = innerSwitchWidgets
    )
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
        scopeElements = innerSwitchWidgets
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
        scopeElements = innerSwitchWidgets
    )
}

internal fun TestElement.innerVflow(
    selector: Selector,
    inViewOnly: Boolean
): TestElement {

    return this.vflow(
        selector = selector,
        inViewOnly = inViewOnly,
        targetElements = innerWidgets
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
        scopeElements = innerWidgets
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
        scopeElements = innerWidgets
    )
}

internal fun TestElement.innerVlabel(
    selector: Selector,
    inViewOnly: Boolean
): TestElement {

    return this.vflow(
        selector = selector,
        inViewOnly = inViewOnly,
        targetElements = innerLabelWidgets
    )
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
        scopeElements = innerLabelWidgets
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

    return this.vflow(
        selector = selector,
        inViewOnly = inViewOnly,
        targetElements = innerInputWidgets
    )
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
        scopeElements = innerInputWidgets
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
        scopeElements = innerInputWidgets
    )
}

internal fun TestElement.innerVimage(
    selector: Selector,
    inViewOnly: Boolean
): TestElement {

    return this.vflow(
        selector = selector,
        inViewOnly = inViewOnly,
        targetElements = innerImageWidgets
    )
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
        scopeElements = innerImageWidgets
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
        scopeElements = innerImageWidgets
    )
}

internal fun TestElement.innerVbutton(
    selector: Selector,
    inViewOnly: Boolean
): TestElement {

    return this.vflow(
        selector = selector,
        inViewOnly = inViewOnly,
        targetElements = innerButtonWidgets
    )
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
        scopeElements = innerButtonWidgets
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
        scopeElements = innerButtonWidgets
    )
}

internal fun TestElement.innerVswitch(
    selector: Selector,
    inViewOnly: Boolean
): TestElement {

    return this.vflow(
        selector = selector,
        inViewOnly = inViewOnly,
        targetElements = innerSwitchWidgets
    )
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
        scopeElements = innerSwitchWidgets
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
        scopeElements = innerSwitchWidgets
    )
}
