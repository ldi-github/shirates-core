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
    selector: Selector
): TestElement {

    return this.flow(
        selector = selector,
        targetElements = innerWidgets
    )
}

/**
 * innerFlow
 */
fun TestElement.innerFlow(
    pos: Int = 1
): TestElement {

    return relative(
        command = ":innerFlow($pos)",
        scopeElements = innerWidgets
    )
}

/**
 * innerFlow
 */
fun TestElement.innerFlow(
    expression: String
): TestElement {

    return relative(
        command = ":innerFlow($expression)",
        scopeElements = innerWidgets
    )
}

internal fun TestElement.innerLabel(
    selector: Selector
): TestElement {

    return this.flow(
        selector = selector,
        targetElements = innerLabelWidgets
    )
}

/**
 * innerLabel
 */
fun TestElement.innerLabel(
    pos: Int = 1
): TestElement {

    return relative(
        command = ":innerLabel($pos)",
        scopeElements = innerLabelWidgets
    )
}

/**
 * innerLabel
 */
fun TestElement.innerLabel(
    expression: String
): TestElement {

    return relative(
        command = ":innerLabel($expression)",
        scopeElements = innerLabelWidgets
    )
}

internal fun TestElement.innerInput(
    selector: Selector
): TestElement {

    return this.flow(
        selector = selector,
        targetElements = innerInputWidgets
    )
}

/**
 * innerInput
 */
fun TestElement.innerInput(
    pos: Int = 1
): TestElement {

    return relative(
        command = ":innerInput($pos)",
        scopeElements = innerInputWidgets
    )
}

/**
 * innerInput
 */
fun TestElement.innerInput(
    expression: String
): TestElement {

    return relative(
        command = ":innerInput($expression)",
        scopeElements = innerInputWidgets
    )
}

internal fun TestElement.innerImage(
    selector: Selector
): TestElement {

    return this.flow(
        selector = selector,
        targetElements = innerImageWidgets
    )
}

/**
 * innerImage
 */
fun TestElement.innerImage(
    pos: Int = 1
): TestElement {

    return relative(
        command = ":innerImage($pos)",
        scopeElements = innerImageWidgets
    )
}

/**
 * innerImage
 */
fun TestElement.innerImage(
    expression: String
): TestElement {

    return relative(
        command = ":innerImage($expression)",
        scopeElements = innerImageWidgets
    )
}

internal fun TestElement.innerButton(
    selector: Selector
): TestElement {

    return this.flow(
        selector = selector,
        targetElements = innerButtonWidgets
    )
}

/**
 * innerButton
 */
fun TestElement.innerButton(
    pos: Int = 1
): TestElement {

    return relative(
        command = ":innerButton($pos)",
        scopeElements = innerButtonWidgets
    )
}

/**
 * innerButton
 */
fun TestElement.innerButton(
    expression: String
): TestElement {

    return relative(
        command = ":innerButton($expression)",
        scopeElements = innerButtonWidgets
    )
}

internal fun TestElement.innerSwitch(
    selector: Selector
): TestElement {

    return this.flow(
        selector = selector,
        targetElements = innerSwitchWidgets
    )
}

/**
 * innerSwitch
 */
fun TestElement.innerSwitch(
    pos: Int = 1
): TestElement {

    return relative(
        command = ":innerSwitch($pos)",
        scopeElements = innerSwitchWidgets
    )
}

/**
 * innerSwitch
 */
fun TestElement.innerSwitch(
    expression: String
): TestElement {

    return relative(
        command = ":innerSwitch($expression)",
        scopeElements = innerSwitchWidgets
    )
}

internal fun TestElement.innerVflow(
    selector: Selector
): TestElement {

    return this.vflow(
        selector = selector,
        targetElements = innerWidgets
    )
}

/**
 * innerVflow
 */
fun TestElement.innerVflow(
    pos: Int = 1
): TestElement {

    return relative(
        command = ":innerVflow($pos)",
        scopeElements = innerWidgets
    )
}

/**
 * innerVflow
 */
fun TestElement.innerVflow(
    expression: String
): TestElement {

    return relative(
        command = ":innerVflow($expression)",
        scopeElements = innerWidgets
    )
}

internal fun TestElement.innerVlabel(
    selector: Selector
): TestElement {

    return this.vflow(
        selector = selector,
        targetElements = innerLabelWidgets
    )
}

/**
 * innerVlabel
 */
fun TestElement.innerVlabel(
    pos: Int = 1
): TestElement {

    return relative(
        command = ":innerVlabel($pos)",
        scopeElements = innerLabelWidgets
    )
}

/**
 * innerVlabel
 */
fun TestElement.innerVlabel(
    expression: String
): TestElement {

    return relative(
        command = ":innerVlabel($expression)",
        scopeElements = labelWidgets
    )
}

internal fun TestElement.innerVinput(
    selector: Selector
): TestElement {

    return this.vflow(
        selector = selector,
        targetElements = innerInputWidgets
    )
}

/**
 * innerVinput
 */
fun TestElement.innerVinput(
    pos: Int = 1
): TestElement {

    return relative(
        command = ":innerVinput($pos)",
        scopeElements = innerInputWidgets
    )
}

/**
 * innerVinput
 */
fun TestElement.innerVinput(
    expression: String
): TestElement {

    return relative(
        command = ":innerVinput($expression)",
        scopeElements = innerInputWidgets
    )
}

internal fun TestElement.innerVimage(
    selector: Selector
): TestElement {

    return this.vflow(
        selector = selector,
        targetElements = innerImageWidgets
    )
}

/**
 * innerVimage
 */
fun TestElement.innerVimage(
    pos: Int = 1
): TestElement {

    return relative(
        command = ":innerVimage($pos)",
        scopeElements = innerImageWidgets
    )
}

/**
 * innerVimage
 */
fun TestElement.innerVimage(
    expression: String
): TestElement {

    return relative(
        command = ":innerVimage($expression)",
        scopeElements = innerImageWidgets
    )
}

internal fun TestElement.innerVbutton(
    selector: Selector
): TestElement {

    return this.vflow(
        selector = selector,
        targetElements = innerButtonWidgets
    )
}

/**
 * innerVbutton
 */
fun TestElement.innerVbutton(
    pos: Int = 1
): TestElement {

    return relative(
        command = ":innerVbutton($pos)",
        scopeElements = innerButtonWidgets
    )
}

/**
 * innerVbutton
 */
fun TestElement.innerVbutton(
    expression: String
): TestElement {

    return relative(
        command = ":innerVbutton($expression)",
        scopeElements = innerButtonWidgets
    )
}

internal fun TestElement.innerVswitch(
    selector: Selector
): TestElement {

    return this.vflow(
        selector = selector,
        targetElements = innerSwitchWidgets
    )
}

/**
 * innerVswitch
 */
fun TestElement.innerVswitch(
    pos: Int = 1
): TestElement {

    return relative(
        command = ":innerVswitch($pos)",
        scopeElements = innerSwitchWidgets
    )
}

/**
 * innerVswitch
 */
fun TestElement.innerVswitch(
    expression: String
): TestElement {

    return relative(
        command = ":innerVswitch($expression)",
        scopeElements = innerSwitchWidgets
    )
}
