package shirates.core.driver.commandextension

import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.driver.commandextension.helper.HorizontalBand
import shirates.core.driver.commandextension.helper.VerticalBand
import shirates.core.utility.element.ElementCategoryExpressionUtility

private fun TestElement.filterCandidates(
    selector: Selector,
    targetElements: List<TestElement>,
    widgetOnly: Boolean,
    frame: Bounds? = null
): MutableList<TestElement> {

    var filtered = targetElements
    if (widgetOnly) {
        filtered = filtered.filter { it.isWidget }
    }
    filtered = filtered.filterBySelector(selector = selector, frame = frame)
        .filter { it != this }
        .toMutableList()

    return filtered
}

private fun Selector.copyAndRemovePos(): Selector {

    val sel = this.copy()
    if (sel.filterMap.containsKey("pos")) {
        sel.filterMap.remove("pos")
    }
    return sel
}

internal fun TestElement.rightLeftCore(
    relative: RelativeDirection,
    selector: Selector,
    targetElements: List<TestElement>,
    widgetOnly: Boolean,
    frame: Bounds?
): TestElement {

    val sel = selector.copyAndRemovePos()
    sel.command = null
    val widgets = targetElements
        .filter { it.isWidget }
        .filterBySelector(selector = sel)

    val horizontalBand = HorizontalBand(this)
    for (widget in widgets) {
        horizontalBand.merge(widget)
    }
    val elms = horizontalBand.getElements()
    val candidates = filterCandidates(
        selector = sel,
        targetElements = elms,
        widgetOnly = widgetOnly,
        frame = frame
    )
    val sortedElements =
        if (relative.isRight)
            candidates.filter { this.bounds.left <= it.bounds.left }
                .sortedWith(compareBy<TestElement> { it.bounds.left }.thenBy { it.bounds.right })
                .toMutableList()
        else
            candidates.filter { it.bounds.right <= this.bounds.right }
                .sortedWith(compareByDescending<TestElement> { it.bounds.right }.thenBy { it.bounds.left })
                .toMutableList()
    val pos = selector.pos ?: 1
    val e =
        if (sortedElements.isEmpty() || sortedElements.count() < pos) TestElement()
        else sortedElements[pos - 1]

    TestDriver.lastElement = e

    return e
}

internal fun getWidgetOnly(selector: Selector): Boolean {

    if (selector.isXmlBased) {
        return false
    }
    if (selector.filterMap.containsKey("className")) {
        return false
    }
    if (selector.command == ":belowScrollable" || selector.command == ":aboveScrollable") {
        return false
    }
    return true
}

internal fun TestElement.belowAboveCore(
    relative: RelativeDirection,
    selector: Selector,
    targetElements: List<TestElement>,
    widgetOnly: Boolean,
    frame: Bounds?
): TestElement {

    val sel = selector.copyAndRemovePos()
    sel.command = null
    var elements = targetElements
    if (widgetOnly) {
        elements = elements.filter { it.isWidget }
    }
    elements = elements.filterBySelector(selector = sel)

    val verticalBand = VerticalBand(this)
    for (element in elements) {
        verticalBand.merge(element)
    }
    val elms = verticalBand.getElements()
    val candidates = filterCandidates(
        selector = sel,
        targetElements = elms,
        widgetOnly = widgetOnly,
        frame = frame
    )
    val sortedElements =
        if (relative.isBelow)
            candidates.filter { this.bounds.top < it.bounds.top }
                .sortedWith(compareBy<TestElement> { it.bounds.top }.thenBy { it.bounds.bottom })
                .toMutableList()
        else
            candidates.filter { it.bounds.bottom < this.bounds.bottom }
                .sortedWith(compareByDescending<TestElement> { it.bounds.bottom }.thenBy { it.bounds.top })
                .toMutableList()
    val pos = selector.pos ?: 1
    val e =
        if (sortedElements.isEmpty() || sortedElements.count() < pos) TestElement()
        else sortedElements[pos - 1]

    TestDriver.lastElement = e

    return e
}

internal fun TestElement.right(
    selector: Selector,
    targetElements: List<TestElement>,
    widgetOnly: Boolean,
    frame: Bounds?
): TestElement {

    val e = execRelativeCommand(
        relativeSelector = selector,
        getElement = {
            rightLeftCore(
                relative = RelativeDirection.right,
                selector = selector,
                targetElements = targetElements,
                widgetOnly = widgetOnly,
                frame = frame
            )
        }
    )
    return e
}

/**
 * right
 */
fun TestElement.right(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    val scopedElements = widgets.toMutableList()
    if (scopedElements.contains(this).not()) {
        scopedElements.add(this)
    }
    return relative(
        command = ":right($pos)",
        scopeElements = scopedElements,
        frame = frame
    )
}

/**
 * right
 */
fun TestElement.right(
    expression: String,
    frame: Bounds? = null
): TestElement {

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":right($exp)",
        scopeElements = widgets,
        frame = frame
    )
}

internal fun TestElement.rightInput(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":rightInput",
        selector = selector,
        className = ElementCategoryExpressionUtility.inputTypesExpression,
        targetElements = targetElements,
        frame = frame
    )

    lastElement = e
    return e
}

private fun TestElement.getRelativeWidget(
    relativeCommand: String,
    selector: Selector,
    className: String,
    targetElements: List<TestElement>,
    frame: Bounds? = null
): TestElement {

    return getRelativeElement(
        relativeCommand = relativeCommand,
        selector = selector,
        className = className,
        targetElements = targetElements,
        widgetOnly = true,
        frame = frame
    )
}

private fun TestElement.getRelativeElement(
    relativeCommand: String,
    selector: Selector,
    className: String,
    targetElements: List<TestElement>,
    widgetOnly: Boolean,
    frame: Bounds? = null
): TestElement {

    val sel = selector.copy()
    sel.className = className

    val e = if (relativeCommand.startsWith(":right")) {
        right(selector = sel, targetElements = targetElements, widgetOnly = widgetOnly, frame = frame)
    } else if (relativeCommand.startsWith(":left")) {
        left(selector = sel, targetElements = targetElements, widgetOnly = widgetOnly, frame = frame)
    } else if (relativeCommand.startsWith(":above")) {
        above(selector = sel, targetElements = targetElements, widgetOnly = widgetOnly, frame = frame)
    } else if (relativeCommand.startsWith(":below")) {
        below(selector = sel, targetElements = targetElements, widgetOnly = widgetOnly, frame = frame)
    } else {
        throw NotImplementedError(relativeCommand)
    }

    sel.className = null

    return e
}

/**
 * rightInput
 */
fun TestElement.rightInput(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":rightInput($pos)",
        scopeElements = inputWidgets,
        frame = frame
    )
}

/**
 * rightInput
 */
fun TestElement.rightInput(
    expression: String,
    frame: Bounds? = null
): TestElement {

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":rightInput($exp)",
        scopeElements = inputWidgets,
        frame = frame
    )
}

internal fun TestElement.rightLabel(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":rightLabel",
        selector = selector,
        className = ElementCategoryExpressionUtility.labelTypesExpression,
        targetElements = targetElements,
        frame = frame
    )
    return e
}

/**
 * rightLabel
 */
fun TestElement.rightLabel(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":rightLabel($pos)",
        scopeElements = widgets,
        frame = frame
    )
}

/**
 * rightLabel
 */
fun TestElement.rightLabel(
    expression: String,
    frame: Bounds? = null
): TestElement {

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":rightLabel($exp)",
        scopeElements = labelWidgets,
        frame = frame
    )
}

internal fun TestElement.rightImage(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":rightImage",
        selector = selector,
        className = ElementCategoryExpressionUtility.imageTypesExpression,
        targetElements = targetElements,
        frame = frame
    )
    return e
}

/**
 * rightImage
 */
fun TestElement.rightImage(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":rightImage($pos)",
        scopeElements = imageWidgets,
        frame = frame
    )
}

/**
 * rightImage
 */
fun TestElement.rightImage(
    expression: String,
    frame: Bounds? = null
): TestElement {

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":rightImage($exp)",
        scopeElements = imageWidgets,
        frame = frame
    )
}

internal fun TestElement.rightButton(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":rightButton",
        selector = selector,
        className = ElementCategoryExpressionUtility.buttonTypesExpression,
        targetElements = targetElements,
        frame = frame
    )
    return e
}

/**
 * rightButton
 */
fun TestElement.rightButton(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":rightButton($pos)",
        scopeElements = buttonWidgets,
        frame = frame
    )
}

/**
 * rightButton
 */
fun TestElement.rightButton(
    expression: String,
    frame: Bounds? = null
): TestElement {

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":rightButton($exp)",
        scopeElements = buttonWidgets,
        frame = frame
    )
}

internal fun TestElement.rightSwitch(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":rightSwitch",
        selector = selector,
        className = ElementCategoryExpressionUtility.switchTypesExpression,
        targetElements = targetElements,
        frame = frame
    )
    return e
}

/**
 * rightSwitch
 */
fun TestElement.rightSwitch(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":rightSwitch($pos)",
        scopeElements = switchWidgets,
        frame = frame
    )
}

/**
 * rightSwitch
 */
fun TestElement.rightSwitch(
    expression: String,
    frame: Bounds? = null
): TestElement {

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":rightSwitch($exp)",
        scopeElements = switchWidgets,
        frame = frame
    )
}

internal fun TestElement.below(
    selector: Selector,
    targetElements: List<TestElement>,
    widgetOnly: Boolean,
    frame: Bounds?
): TestElement {

    return execRelativeCommand(
        relativeSelector = selector,
        getElement = {
            belowAboveCore(
                relative = RelativeDirection.below,
                selector = selector,
                targetElements = targetElements,
                widgetOnly = widgetOnly,
                frame = frame
            )
        }
    )
}

/**
 * below
 */
fun TestElement.below(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":below($pos)",
        scopeElements = widgets,
        frame = frame
    )
}

/**
 * below
 */
fun TestElement.below(
    expression: String,
    widgetOnly: Boolean? = null,
    frame: Bounds? = null
): TestElement {

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":below($exp)",
        scopeElements = allElements(),
        widgetOnly = widgetOnly,
        frame = frame
    )
}

internal fun TestElement.belowInput(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":belowInput",
        selector = selector,
        className = ElementCategoryExpressionUtility.inputTypesExpression,
        targetElements = targetElements,
    )
    return e
}

/**
 * belowInput
 */
fun TestElement.belowInput(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":belowInput($pos)",
        scopeElements = inputWidgets,
        frame = frame
    )
}

/**
 * belowInput
 */
fun TestElement.belowInput(
    expression: String,
    frame: Bounds? = null
): TestElement {

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":belowInput($exp)",
        scopeElements = inputWidgets,
        frame = frame
    )
}

internal fun TestElement.belowLabel(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":belowLabel",
        selector = selector,
        className = ElementCategoryExpressionUtility.labelTypesExpression,
        targetElements = targetElements,
        frame = frame
    )
    return e
}

/**
 * belowLabel
 */
fun TestElement.belowLabel(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":belowLabel($pos)",
        scopeElements = labelWidgets,
        frame = frame
    )
}

/**
 * belowLabel
 */
fun TestElement.belowLabel(
    expression: String,
    frame: Bounds? = null
): TestElement {

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":belowLabel($exp)",
        scopeElements = labelWidgets,
        frame = frame
    )
}

internal fun TestElement.belowImage(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":belowImage",
        selector = selector,
        className = ElementCategoryExpressionUtility.imageTypesExpression,
        targetElements = targetElements,
        frame = frame
    )
    return e
}

/**
 * belowImage
 */
fun TestElement.belowImage(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":belowImage($pos)",
        scopeElements = imageWidgets,
        frame = frame
    )
}

/**
 * belowImage
 */
fun TestElement.belowImage(
    expression: String,
    frame: Bounds? = null
): TestElement {

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":belowImage($exp)",
        scopeElements = imageWidgets,
        frame = frame
    )
}

internal fun TestElement.belowButton(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":belowButton",
        selector = selector,
        className = ElementCategoryExpressionUtility.buttonTypesExpression,
        targetElements = targetElements,
        frame = frame
    )
    return e
}

/**
 * belowButton
 */
fun TestElement.belowButton(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":belowButton($pos)",
        scopeElements = buttonWidgets,
        frame = frame
    )
}

/**
 * belowButton
 */
fun TestElement.belowButton(
    expression: String,
    frame: Bounds? = null
): TestElement {

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":belowButton($exp)",
        scopeElements = buttonWidgets,
        frame = frame
    )
}

internal fun TestElement.belowSwitch(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":belowSwitch",
        selector = selector,
        className = ElementCategoryExpressionUtility.switchTypesExpression,
        targetElements = targetElements,
        frame = frame
    )
    return e
}

/**
 * belowSwitch
 */
fun TestElement.belowSwitch(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":belowSwitch($pos)",
        scopeElements = switchWidgets,
        frame = frame
    )
}

/**
 * belowSwitch
 */
fun TestElement.belowSwitch(
    expression: String,
    frame: Bounds? = null
): TestElement {

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":belowSwitch($exp)",
        scopeElements = switchWidgets,
        frame = frame
    )
}

internal fun TestElement.belowScrollable(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val e = getRelativeElement(
        relativeCommand = ":belowScrollable",
        selector = selector,
        className = ElementCategoryExpressionUtility.scrollableTypesExpression,
        targetElements = targetElements,
        frame = frame,
        widgetOnly = false
    )
    return e
}

/**
 * belowScrollable
 */
fun TestElement.belowScrollable(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":belowScrollable($pos)",
        scopeElements = scrollableElements,
        widgetOnly = false,
        frame = frame
    )
}

/**
 * belowScrollable
 */
fun TestElement.belowScrollable(
    expression: String,
    frame: Bounds? = null
): TestElement {

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":belowScrollable($exp)",
        scopeElements = scrollableElements,
        frame = frame
    )
}

internal fun TestElement.left(
    selector: Selector,
    targetElements: List<TestElement>,
    widgetOnly: Boolean,
    frame: Bounds?
): TestElement {

    val e = execRelativeCommand(
        relativeSelector = selector,
        getElement = {
            rightLeftCore(
                relative = RelativeDirection.left,
                selector = selector,
                targetElements = targetElements,
                widgetOnly = widgetOnly,
                frame = frame
            )
        }
    )

    return e
}

/**
 * left
 */
fun TestElement.left(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":left($pos)",
        scopeElements = widgets,
        frame = frame
    )
}

/**
 * left
 */
fun TestElement.left(
    expression: String,
    frame: Bounds? = null
): TestElement {

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":left($exp)",
        scopeElements = widgets,
        frame = frame
    )
}

internal fun TestElement.leftInput(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":leftInput",
        selector = selector,
        className = ElementCategoryExpressionUtility.inputTypesExpression,
        targetElements = targetElements,
        frame = frame
    )
    return e
}

/**
 * leftInput
 */
fun TestElement.leftInput(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":leftInput($pos)",
        scopeElements = inputWidgets,
        frame = frame
    )
}

/**
 * leftInput
 */
fun TestElement.leftInput(
    expression: String,
    frame: Bounds? = null
): TestElement {

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":leftInput($exp)",
        scopeElements = inputWidgets,
        frame = frame
    )
}

internal fun TestElement.leftLabel(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":leftLabel",
        selector = selector,
        className = ElementCategoryExpressionUtility.labelTypesExpression,
        targetElements = targetElements,
        frame = frame
    )
    return e
}

/**
 * leftLabel
 */
fun TestElement.leftLabel(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":leftLabel($pos)",
        scopeElements = labelWidgets,
        frame = frame
    )
}

/**
 * leftLabel
 */
fun TestElement.leftLabel(
    expression: String,
    frame: Bounds? = null
): TestElement {

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":leftLabel($exp)",
        scopeElements = labelWidgets,
        frame = frame
    )
}

internal fun TestElement.leftImage(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":leftImage",
        selector = selector,
        className = ElementCategoryExpressionUtility.imageTypesExpression,
        targetElements = targetElements,
        frame = frame
    )
    return e
}

/**
 * leftImage
 */
fun TestElement.leftImage(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":leftImage($pos)",
        scopeElements = imageWidgets,
        frame = frame
    )
}

/**
 * leftImage
 */
fun TestElement.leftImage(
    expression: String,
    frame: Bounds? = null
): TestElement {

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":leftImage($exp)",
        scopeElements = imageWidgets,
        frame = frame
    )
}

internal fun TestElement.leftButton(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":leftButton",
        selector = selector,
        className = ElementCategoryExpressionUtility.buttonTypesExpression,
        targetElements = targetElements,
        frame = frame
    )
    return e
}

/**
 * leftButton
 */
fun TestElement.leftButton(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":leftButton($pos)",
        scopeElements = buttonWidgets,
        frame = frame
    )
}

/**
 * leftButton
 */
fun TestElement.leftButton(
    expression: String,
    frame: Bounds? = null
): TestElement {

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":leftButton($exp)",
        scopeElements = buttonWidgets,
        frame = frame
    )
}

internal fun TestElement.leftSwitch(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":leftSwitch",
        selector = selector,
        className = ElementCategoryExpressionUtility.switchTypesExpression,
        targetElements = targetElements,
        frame = frame
    )
    return e
}

/**
 * leftSwitch
 */
fun TestElement.leftSwitch(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":leftSwitch($pos)",
        scopeElements = switchWidgets,
        frame = frame
    )
}

/**
 * leftSwitch
 */
fun TestElement.leftSwitch(
    expression: String,
    frame: Bounds? = null
): TestElement {

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":leftSwitch($exp)",
        scopeElements = switchWidgets,
        frame = frame
    )
}

internal fun TestElement.above(
    selector: Selector,
    targetElements: List<TestElement>,
    widgetOnly: Boolean,
    frame: Bounds?
): TestElement {

    return execRelativeCommand(
        relativeSelector = selector,
        getElement = {
            belowAboveCore(
                relative = RelativeDirection.above,
                selector = selector,
                targetElements = targetElements,
                widgetOnly = widgetOnly,
                frame = frame
            )
        }
    )
}

/**
 * above
 */
fun TestElement.above(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":above($pos)",
        scopeElements = widgets,
        frame = frame
    )
}

/**
 * above
 */
fun TestElement.above(
    expression: String,
    frame: Bounds? = null
): TestElement {

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":above($exp)",
        scopeElements = allElements(),
        frame = frame
    )
}

internal fun TestElement.aboveInput(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":aboveInput",
        selector = selector,
        className = ElementCategoryExpressionUtility.inputTypesExpression,
        targetElements = targetElements,
        frame = frame
    )
    return e
}

/**
 * aboveInput
 */
fun TestElement.aboveInput(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":aboveInput($pos)",
        scopeElements = inputWidgets,
        frame = frame
    )
}

/**
 * aboveInput
 */
fun TestElement.aboveInput(
    expression: String,
    frame: Bounds? = null
): TestElement {

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":aboveInput($exp)",
        scopeElements = inputWidgets,
        frame = frame
    )
}

internal fun TestElement.aboveLabel(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":aboveLabel",
        selector = selector,
        className = ElementCategoryExpressionUtility.labelTypesExpression,
        targetElements = targetElements,
        frame = frame
    )
    return e
}

/**
 * aboveLabel
 */
fun TestElement.aboveLabel(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":aboveLabel($pos)",
        scopeElements = labelWidgets,
        frame = frame
    )
}

/**
 * aboveLabel
 */
fun TestElement.aboveLabel(
    expression: String,
    frame: Bounds? = null
): TestElement {

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":aboveLabel($exp)",
        scopeElements = labelWidgets,
        frame = frame
    )
}

internal fun TestElement.aboveImage(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":aboveImage",
        selector = selector,
        className = ElementCategoryExpressionUtility.imageTypesExpression,
        targetElements = targetElements,
    )
    return e
}

/**
 * aboveImage
 */
fun TestElement.aboveImage(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":aboveImage($pos)",
        scopeElements = imageWidgets,
        frame = frame
    )
}

/**
 * aboveImage
 */
fun TestElement.aboveImage(
    expression: String,
    frame: Bounds? = null
): TestElement {

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":aboveImage($exp)",
        scopeElements = imageWidgets,
        frame = frame
    )
}

internal fun TestElement.aboveButton(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":aboveButton",
        selector = selector,
        className = ElementCategoryExpressionUtility.buttonTypesExpression,
        targetElements = targetElements,
        frame = frame,
    )
    return e
}

/**
 * aboveButton
 */
fun TestElement.aboveButton(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":aboveButton($pos)",
        scopeElements = buttonWidgets,
        frame = frame
    )
}

/**
 * aboveButton
 */
fun TestElement.aboveButton(
    expression: String,
    frame: Bounds? = null
): TestElement {

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":aboveButton($exp)",
        scopeElements = buttonWidgets,
        frame = frame
    )
}

internal fun TestElement.aboveSwitch(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":aboveSwitch",
        selector = selector,
        className = ElementCategoryExpressionUtility.switchTypesExpression,
        targetElements = targetElements,
        frame = frame
    )
    return e
}

/**
 * aboveSwitch
 */
fun TestElement.aboveSwitch(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":aboveSwitch($pos)",
        scopeElements = switchWidgets,
        frame = frame
    )
}

/**
 * aboveSwitch
 */
fun TestElement.aboveSwitch(
    expression: String,
    frame: Bounds? = null
): TestElement {

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":aboveSwitch($exp)",
        scopeElements = switchWidgets,
        frame = frame
    )
}

internal fun TestElement.aboveScrollable(
    selector: Selector,
    targetElements: List<TestElement>,
    frame: Bounds?
): TestElement {

    val e = getRelativeElement(
        relativeCommand = ":aboveScrollable",
        selector = selector,
        className = ElementCategoryExpressionUtility.scrollableTypesExpression,
        targetElements = targetElements,
        frame = frame,
        widgetOnly = false
    )
    return e
}

/**
 * aboveScrollable
 */
fun TestElement.aboveScrollable(
    pos: Int = 1,
    frame: Bounds? = null
): TestElement {

    return relative(
        command = ":aboveScrollable($pos)",
        scopeElements = scrollableElements,
        widgetOnly = false,
        frame = frame
    )
}

/**
 * aboveScrollable
 */
fun TestElement.aboveScrollable(
    expression: String,
    frame: Bounds? = null
): TestElement {

    val sel = getSelector(expression = expression)
    val exp = sel.getElementExpression()
    return relative(
        command = ":aboveScrollable($exp)",
        scopeElements = scrollableElements,
        frame = frame
    )
}

