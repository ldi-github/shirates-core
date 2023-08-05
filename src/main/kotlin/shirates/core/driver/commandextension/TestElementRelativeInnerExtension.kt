package shirates.core.driver.commandextension

import shirates.core.configuration.Selector
import shirates.core.driver.TestElement

internal val TestElement.innerElements: List<TestElement>
    get() {
        return this.descendants.filter { it.bounds.area > 0 }.toMutableList()
    }

internal val TestElement.innerWidgets: List<TestElement>
    get() {
        return this.descendants.filter { it.isWidget && it.bounds.area > 0 }.toMutableList()
    }

internal val TestElement.innerLabelWidgets: List<TestElement>
    get() {
        return this.descendants.filter { it.isLabel && it.bounds.area > 0 }.toMutableList()
    }

internal val TestElement.innerInputWidgets: List<TestElement>
    get() {
        return this.descendants.filter { it.isInput && it.bounds.area > 0 }.toMutableList()
    }

internal val TestElement.innerImageWidgets: List<TestElement>
    get() {
        return this.descendants.filter { it.isImage && it.bounds.area > 0 }.toMutableList()
    }

internal val TestElement.innerButtonWidgets: List<TestElement>
    get() {
        return this.descendants.filter { it.isButton && it.bounds.area > 0 }.toMutableList()
    }

internal val TestElement.innerSwitchWidgets: List<TestElement>
    get() {
        return this.descendants.filter { it.isSwitch && it.bounds.area > 0 }.toMutableList()
    }


internal fun TestElement.innerWidget(
    selector: Selector
): TestElement {

    return this.flow(
        selector = selector,
        targetElements = innerWidgets
    )
}

/**
 * innerWidget
 */
fun TestElement.innerWidget(
    pos: Int = 1
): TestElement {

    return relative(
        command = ":innerWidget($pos)",
        scopeElements = innerWidgets
    )
}

/**
 * innerWidget
 */
fun TestElement.innerWidget(
    expression: String
): TestElement {

    return relative(
        command = ":innerWidget($expression)",
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

internal fun TestElement.innerVWidget(
    selector: Selector
): TestElement {

    return this.vflow(
        selector = selector,
        targetElements = innerWidgets
    )
}

/**
 * innerVWidget
 */
fun TestElement.innerVWidget(
    pos: Int = 1
): TestElement {

    return relative(
        command = ":innerVWidget($pos)",
        scopeElements = innerWidgets
    )
}

/**
 * innerVWidget
 */
fun TestElement.innerVWidget(
    expression: String
): TestElement {

    return relative(
        command = ":innerVWidget($expression)",
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


internal fun TestElement.getAncestorAt(level: Int): TestElement {

    var e = this
    for (i in 1..level) {
        e = e.parentElement
    }
    return e
}

internal fun TestElement.cellWidget(
    selector: Selector
): TestElement {

    return this.flow(
        selector = selector,
        targetElements = this.innerWidgets
    )
}

private fun TestElement.cellCore(
    command: String
): TestElement {

    val oldSelector = this.selector
    val e = relative(command = command)
    if (e == this) {
        e.selector = oldSelector
    }
    return e
}

/**
 * cellWidget
 */
fun TestElement.cellWidget(
    pos: Int = 1
): TestElement {

    return cellCore(command = ":cellWidget($pos)")
}

/**
 * cellWidget
 */
fun TestElement.cellWidget(
    expression: String
): TestElement {

    return cellCore(command = ":cellWidget($expression)")
}


internal fun TestElement.cellLabel(
    selector: Selector
): TestElement {

    return this.flow(
        selector = selector,
        targetElements = this.innerLabelWidgets
    )
}

/**
 * cellLabel
 */
fun TestElement.cellLabel(
    pos: Int = 1
): TestElement {

    return cellCore(command = ":cellLabel($pos)")
}

/**
 * cellLabel
 */
fun TestElement.cellLabel(
    expression: String
): TestElement {

    return cellCore(command = ":cellLabel($expression)")
}


internal fun TestElement.cellInput(
    selector: Selector
): TestElement {

    return flow(
        selector = selector,
        targetElements = this.innerInputWidgets
    )
}

/**
 * cellInput
 */
fun TestElement.cellInput(
    pos: Int = 1
): TestElement {

    return cellCore(command = ":cellInput($pos)")
}

/**
 * cellInput
 */
fun TestElement.cellInput(
    expression: String
): TestElement {

    return cellCore(command = ":cellInput($expression)")
}


internal fun TestElement.cellImage(
    selector: Selector
): TestElement {

    return flow(
        selector = selector,
        targetElements = this.innerImageWidgets
    )
}

/**
 * cellImage
 */
fun TestElement.cellImage(
    pos: Int = 1
): TestElement {

    return cellCore(command = ":cellImage($pos)")
}

/**
 * cellImage
 */
fun TestElement.cellImage(
    expression: String
): TestElement {

    return cellCore(command = ":cellImage($expression)")
}


internal fun TestElement.cellButton(
    selector: Selector
): TestElement {

    return flow(
        selector = selector,
        targetElements = this.innerButtonWidgets
    )
}

/**
 * cellButton
 */
fun TestElement.cellButton(
    pos: Int = 1
): TestElement {

    return cellCore(command = ":cellButton($pos)")
}

/**
 * cellButton
 */
fun TestElement.cellButton(
    expression: String
): TestElement {

    return cellCore(command = ":cellButton($expression)")
}


internal fun TestElement.cellSwitch(
    selector: Selector
): TestElement {

    return flow(
        selector = selector,
        targetElements = this.innerSwitchWidgets
    )
}

/**
 * cellSwitch
 */
fun TestElement.cellSwitch(
    pos: Int = 1
): TestElement {

    return cellCore(command = ":cellSwitch($pos)")
}

/**
 * cellSwitch
 */
fun TestElement.cellSwitch(
    expression: String
): TestElement {

    return cellCore(command = ":cellSwitch($expression)")
}
