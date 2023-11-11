package shirates.core.driver.commandextension

import shirates.core.configuration.Selector
import shirates.core.driver.Bounds
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
    selector: Selector,
    frame: Bounds?
): TestElement {

    return this.flow(
        selector = selector,
        targetElements = innerWidgets,
        frame = frame
    )
}

/**
 * innerWidget
 */
fun TestElement.innerWidget(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":innerWidget($pos)",
        scopeElements = innerWidgets,
        frame = frame
    )
}

/**
 * innerWidget
 */
fun TestElement.innerWidget(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":innerWidget($expression)",
        scopeElements = innerWidgets,
        frame = frame
    )
}

internal fun TestElement.innerLabel(
    selector: Selector,
    frame: Bounds? = null
): TestElement {

    return this.flow(
        selector = selector,
        targetElements = innerLabelWidgets,
        frame = frame
    )
}

/**
 * innerLabel
 */
fun TestElement.innerLabel(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":innerLabel($pos)",
        scopeElements = innerLabelWidgets,
        frame = frame
    )
}

/**
 * innerLabel
 */
fun TestElement.innerLabel(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":innerLabel($expression)",
        scopeElements = innerLabelWidgets,
        frame = frame
    )
}

internal fun TestElement.innerInput(
    selector: Selector,
    frame: Bounds?
): TestElement {

    return this.flow(
        selector = selector,
        targetElements = innerInputWidgets,
        frame = frame
    )
}

/**
 * innerInput
 */
fun TestElement.innerInput(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":innerInput($pos)",
        scopeElements = innerInputWidgets,
        frame = frame
    )
}

/**
 * innerInput
 */
fun TestElement.innerInput(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":innerInput($expression)",
        scopeElements = innerInputWidgets,
        frame = frame
    )
}

internal fun TestElement.innerImage(
    selector: Selector,
    frame: Bounds?
): TestElement {

    return this.flow(
        selector = selector,
        targetElements = innerImageWidgets,
        frame = frame
    )
}

/**
 * innerImage
 */
fun TestElement.innerImage(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":innerImage($pos)",
        scopeElements = innerImageWidgets,
        frame = frame
    )
}

/**
 * innerImage
 */
fun TestElement.innerImage(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":innerImage($expression)",
        scopeElements = innerImageWidgets,
        frame = frame
    )
}

internal fun TestElement.innerButton(
    selector: Selector,
    frame: Bounds?
): TestElement {

    return this.flow(
        selector = selector,
        targetElements = innerButtonWidgets,
        frame = frame
    )
}

/**
 * innerButton
 */
fun TestElement.innerButton(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":innerButton($pos)",
        scopeElements = innerButtonWidgets,
        frame = frame
    )
}

/**
 * innerButton
 */
fun TestElement.innerButton(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":innerButton($expression)",
        scopeElements = innerButtonWidgets,
        frame = frame
    )
}

internal fun TestElement.innerSwitch(
    selector: Selector,
    frame: Bounds?
): TestElement {

    return this.flow(
        selector = selector,
        targetElements = innerSwitchWidgets,
        frame = frame
    )
}

/**
 * innerSwitch
 */
fun TestElement.innerSwitch(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":innerSwitch($pos)",
        scopeElements = innerSwitchWidgets,
        frame = frame
    )
}

/**
 * innerSwitch
 */
fun TestElement.innerSwitch(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":innerSwitch($expression)",
        scopeElements = innerSwitchWidgets,
        frame = frame
    )
}

internal fun TestElement.innerVWidget(
    selector: Selector,
    frame: Bounds?
): TestElement {

    return this.vflow(
        selector = selector,
        targetElements = innerWidgets,
        frame = frame
    )
}

/**
 * innerVWidget
 */
fun TestElement.innerVWidget(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":innerVWidget($pos)",
        scopeElements = innerWidgets,
        frame = frame
    )
}

/**
 * innerVWidget
 */
fun TestElement.innerVWidget(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":innerVWidget($expression)",
        scopeElements = innerWidgets,
        frame = frame
    )
}

internal fun TestElement.innerVlabel(
    selector: Selector,
    frame: Bounds?
): TestElement {

    return this.vflow(
        selector = selector,
        targetElements = innerLabelWidgets,
        frame = frame
    )
}

/**
 * innerVlabel
 */
fun TestElement.innerVlabel(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":innerVlabel($pos)",
        scopeElements = innerLabelWidgets,
        frame = frame
    )
}

/**
 * innerVlabel
 */
fun TestElement.innerVlabel(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":innerVlabel($expression)",
        scopeElements = labelWidgets,
        frame = frame
    )
}

internal fun TestElement.innerVinput(
    selector: Selector,
    frame: Bounds?
): TestElement {

    return this.vflow(
        selector = selector,
        targetElements = innerInputWidgets,
        frame = frame
    )
}

/**
 * innerVinput
 */
fun TestElement.innerVinput(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":innerVinput($pos)",
        scopeElements = innerInputWidgets,
        frame = frame
    )
}

/**
 * innerVinput
 */
fun TestElement.innerVinput(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":innerVinput($expression)",
        scopeElements = innerInputWidgets,
        frame = frame
    )
}

internal fun TestElement.innerVimage(
    selector: Selector,
    frame: Bounds?
): TestElement {

    return this.vflow(
        selector = selector,
        targetElements = innerImageWidgets,
        frame = frame
    )
}

/**
 * innerVimage
 */
fun TestElement.innerVimage(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":innerVimage($pos)",
        scopeElements = innerImageWidgets,
        frame = frame
    )
}

/**
 * innerVimage
 */
fun TestElement.innerVimage(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":innerVimage($expression)",
        scopeElements = innerImageWidgets,
        frame = frame
    )
}

internal fun TestElement.innerVbutton(
    selector: Selector,
    frame: Bounds?
): TestElement {

    return this.vflow(
        selector = selector,
        targetElements = innerButtonWidgets,
        frame = frame
    )
}

/**
 * innerVbutton
 */
fun TestElement.innerVbutton(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":innerVbutton($pos)",
        scopeElements = innerButtonWidgets,
        frame = frame
    )
}

/**
 * innerVbutton
 */
fun TestElement.innerVbutton(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":innerVbutton($expression)",
        scopeElements = innerButtonWidgets,
        frame = frame
    )
}

internal fun TestElement.innerVswitch(
    selector: Selector,
    frame: Bounds?
): TestElement {

    return this.vflow(
        selector = selector,
        targetElements = innerSwitchWidgets,
        frame = frame
    )
}

/**
 * innerVswitch
 */
fun TestElement.innerVswitch(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":innerVswitch($pos)",
        scopeElements = innerSwitchWidgets,
        frame = frame
    )
}

/**
 * innerVswitch
 */
fun TestElement.innerVswitch(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":innerVswitch($expression)",
        scopeElements = innerSwitchWidgets,
        frame = frame
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
    selector: Selector,
    frame: Bounds? = null
): TestElement {

    return this.flow(
        selector = selector,
        targetElements = this.innerWidgets,
        frame = frame
    )
}

private fun TestElement.cellCore(
    command: String,
    frame: Bounds?
): TestElement {

    val oldSelector = this.selector
    val e = relative(command = command, frame = frame)
    if (e == this) {
        e.selector = oldSelector
    }
    return e
}

/**
 * cellWidget
 */
fun TestElement.cellWidget(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return cellCore(command = ":cellWidget($pos)", frame = frame)
}

/**
 * cellWidget
 */
fun TestElement.cellWidget(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return cellCore(command = ":cellWidget($expression)", frame = frame)
}


internal fun TestElement.cellLabel(
    selector: Selector,
    frame: Bounds?
): TestElement {

    return this.flow(
        selector = selector,
        targetElements = this.innerLabelWidgets,
        frame = frame
    )
}

/**
 * cellLabel
 */
fun TestElement.cellLabel(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return cellCore(command = ":cellLabel($pos)", frame = frame)
}

/**
 * cellLabel
 */
fun TestElement.cellLabel(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return cellCore(command = ":cellLabel($expression)", frame = frame)
}


internal fun TestElement.cellInput(
    selector: Selector,
    frame: Bounds?
): TestElement {

    return flow(
        selector = selector,
        targetElements = this.innerInputWidgets,
        frame = frame
    )
}

/**
 * cellInput
 */
fun TestElement.cellInput(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return cellCore(command = ":cellInput($pos)", frame = frame)
}

/**
 * cellInput
 */
fun TestElement.cellInput(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return cellCore(command = ":cellInput($expression)", frame = frame)
}


internal fun TestElement.cellImage(
    selector: Selector,
    frame: Bounds?
): TestElement {

    return flow(
        selector = selector,
        targetElements = this.innerImageWidgets,
        frame = frame
    )
}

/**
 * cellImage
 */
fun TestElement.cellImage(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return cellCore(command = ":cellImage($pos)", frame = frame)
}

/**
 * cellImage
 */
fun TestElement.cellImage(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return cellCore(command = ":cellImage($expression)", frame = frame)
}


internal fun TestElement.cellButton(
    selector: Selector,
    frame: Bounds? = null
): TestElement {

    return flow(
        selector = selector,
        targetElements = this.innerButtonWidgets,
        frame = frame
    )
}

/**
 * cellButton
 */
fun TestElement.cellButton(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return cellCore(command = ":cellButton($pos)", frame = frame)
}

/**
 * cellButton
 */
fun TestElement.cellButton(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return cellCore(command = ":cellButton($expression)", frame = frame)
}


internal fun TestElement.cellSwitch(
    selector: Selector,
    frame: Bounds?
): TestElement {

    return flow(
        selector = selector,
        targetElements = this.innerSwitchWidgets,
        frame = frame
    )
}

/**
 * cellSwitch
 */
fun TestElement.cellSwitch(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return cellCore(command = ":cellSwitch($pos)", frame = frame)
}

/**
 * cellSwitch
 */
fun TestElement.cellSwitch(
    expression: String,
    frame: Bounds? = null
): TestElement {

    return cellCore(command = ":cellSwitch($expression)", frame = frame)
}
