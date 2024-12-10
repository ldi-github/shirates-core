package shirates.core.driver.commandextension

import shirates.core.configuration.Selector
import shirates.core.driver.Bounds
import shirates.core.driver.TestElement
import shirates.core.driver.filterBySelector

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

    val elms = this.innerWidgets.filterBySelector(
        selector = selector,
        throwsException = false,
        frame = frame
    )
    val e = elms.firstOrNull() ?: TestElement.emptyElement
    e.selector = this.selector?.getChainedSelector(":innerWidget($selector)")
    return e
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
        margin = 0,
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

    val sel = getSelector(expression = expression)
    return innerWidget(selector = sel, frame = frame)
}

internal fun TestElement.innerLabel(
    selector: Selector,
    frame: Bounds? = null
): TestElement {

    return this.flow(
        selector = selector,
        targetElements = innerLabelWidgets,
        margin = 0,
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
        margin = 0,
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

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":innerLabel($exp)",
        scopeElements = innerLabelWidgets,
        margin = 0,
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
        margin = 0,
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
        margin = 0,
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

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":innerInput($exp)",
        scopeElements = innerInputWidgets,
        margin = 0,
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
        margin = 0,
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
        margin = 0,
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

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":innerImage($exp)",
        scopeElements = innerImageWidgets,
        margin = 0,
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
        margin = 0,
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
        margin = 0,
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

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":innerButton($exp)",
        scopeElements = innerButtonWidgets,
        margin = 0,
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
        margin = 0,
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
        margin = 0,
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

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":innerSwitch($exp)",
        scopeElements = innerSwitchWidgets,
        margin = 0,
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
        margin = 0,
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
        margin = 0,
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

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":innerVWidget($exp)",
        scopeElements = innerWidgets,
        margin = 0,
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
        margin = 0,
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
        margin = 0,
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

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":innerVlabel($exp)",
        scopeElements = labelWidgets,
        margin = 0,
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
        margin = 0,
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
        margin = 0,
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

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":innerVinput($exp)",
        scopeElements = innerInputWidgets,
        margin = 0,
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
        margin = 0,
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
        margin = 0,
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

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":innerVimage($exp)",
        scopeElements = innerImageWidgets,
        margin = 0,
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
        margin = 0,
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
        margin = 0,
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

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":innerVbutton($exp)",
        scopeElements = innerButtonWidgets,
        margin = 0,
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
        margin = 0,
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
        margin = 0,
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

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":innerVswitch($exp)",
        scopeElements = innerSwitchWidgets,
        margin = 0,
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

