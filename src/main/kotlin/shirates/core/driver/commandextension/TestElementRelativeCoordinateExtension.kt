package shirates.core.driver.commandextension

import shirates.core.configuration.Filter
import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.driver.commandextension.helper.HorizontalBand
import shirates.core.driver.commandextension.helper.VerticalBand
import shirates.core.utility.element.ElementCategoryExpressionUtility

private fun TestElement.filterCandidates(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): MutableList<TestElement> {

    val sel = selector.copy()
    if (sel.className == null) {
        val widgetTypes = ElementCategoryExpressionUtility.widgetTypesExpression
        sel.filterMap["className"] = Filter("className=$widgetTypes")
    }

    val filtered = targetElements
        .filterBySelector(selector = sel, inViewOnly = inViewOnly)
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
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copyAndRemovePos()
    sel.command = null
    val widgets = targetElements
        .filter { it.isWidget }
        .filterBySelector(selector = sel, inViewOnly = inViewOnly)

    val horizontalBand = HorizontalBand(this)
    for (widget in widgets) {
        horizontalBand.merge(widget)
    }
    val elms = horizontalBand.getElements()
    val candidates = filterCandidates(
        selector = sel,
        inViewOnly = inViewOnly,
        targetElements = elms
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

internal fun TestElement.belowAboveCore(
    relative: RelativeDirection,
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copyAndRemovePos()
    sel.command = null
    val widgets = targetElements
        .filter { it.isWidget }
        .filterBySelector(selector = sel, inViewOnly = inViewOnly)

    val verticalBand = VerticalBand(this)
    for (widget in widgets) {
        verticalBand.merge(widget)
    }
    val elms = verticalBand.getElements()
    val candidates = filterCandidates(
        selector = sel,
        inViewOnly = inViewOnly,
        targetElements = elms
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
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = execRelativeCommand(
        relativeSelector = selector,
        getElement = {
            rightLeftCore(
                relative = RelativeDirection.right,
                selector = selector,
                inViewOnly = inViewOnly,
                targetElements = targetElements
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
    inViewOnly: Boolean = true
): TestElement {

    val scopedElements = widgets.toMutableList()
    if (scopedElements.contains(this).not()) {
        scopedElements.add(this)
    }
    return relative(
        command = ":right($pos)",
        inViewOnly = inViewOnly,
        scopeElements = scopedElements
    )
}

/**
 * right
 */
fun TestElement.right(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":right($expression)",
        inViewOnly = inViewOnly,
        scopeElements = widgets
    )
}

internal fun TestElement.rightInput(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":rightInput",
        selector = selector,
        className = ElementCategoryExpressionUtility.inputTypesExpression,
        inViewOnly = inViewOnly,
        targetElements = targetElements
    )

    lastElement = e
    return e
}

private fun TestElement.getRelativeWidget(
    relativeCommand: String,
    selector: Selector,
    className: String,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = className

    val e = if (relativeCommand.startsWith(":right")) {
        right(selector = sel, inViewOnly = inViewOnly, targetElements = targetElements)
    } else if (relativeCommand.startsWith(":left")) {
        left(selector = sel, inViewOnly = inViewOnly, targetElements = targetElements)
    } else if (relativeCommand.startsWith(":above")) {
        above(selector = sel, inViewOnly = inViewOnly, targetElements = targetElements)
    } else if (relativeCommand.startsWith(":below")) {
        below(selector = sel, inViewOnly = inViewOnly, targetElements = targetElements)
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
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":rightInput($pos)",
        inViewOnly = inViewOnly,
        scopeElements = inputWidgets
    )
}

/**
 * rightInput
 */
fun TestElement.rightInput(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":rightInput($expression)",
        inViewOnly = inViewOnly,
        scopeElements = inputWidgets
    )
}

internal fun TestElement.rightLabel(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":rightLabel",
        selector = selector,
        className = ElementCategoryExpressionUtility.labelTypesExpression,
        inViewOnly = inViewOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * rightLabel
 */
fun TestElement.rightLabel(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":rightLabel($pos)",
        inViewOnly = inViewOnly,
        scopeElements = widgets
    )
}

/**
 * rightLabel
 */
fun TestElement.rightLabel(
    expression: String,
    inViewOnly: Boolean = true,
): TestElement {

    return relative(
        command = ":rightLabel($expression)",
        inViewOnly = inViewOnly,
        scopeElements = labelWidgets
    )
}

internal fun TestElement.rightImage(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":rightImage",
        selector = selector,
        className = ElementCategoryExpressionUtility.imageTypesExpression,
        inViewOnly = inViewOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * rightImage
 */
fun TestElement.rightImage(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":rightImage($pos)",
        inViewOnly = inViewOnly,
        imageWidgets
    )
}

/**
 * rightImage
 */
fun TestElement.rightImage(
    expression: String,
    inViewOnly: Boolean = true,
): TestElement {

    return relative(
        command = ":rightImage($expression)",
        inViewOnly = inViewOnly,
        scopeElements = imageWidgets
    )
}

internal fun TestElement.rightButton(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":rightButton",
        selector = selector,
        className = ElementCategoryExpressionUtility.buttonTypesExpression,
        inViewOnly = inViewOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * rightButton
 */
fun TestElement.rightButton(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":rightButton($pos)",
        inViewOnly = inViewOnly,
        scopeElements = buttonWidgets
    )
}

/**
 * rightButton
 */
fun TestElement.rightButton(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":rightButton($expression)",
        inViewOnly = inViewOnly,
        scopeElements = buttonWidgets
    )
}

internal fun TestElement.rightSwitch(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":rightSwitch",
        selector = selector,
        className = ElementCategoryExpressionUtility.switchTypesExpression,
        inViewOnly = inViewOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * rightSwitch
 */
fun TestElement.rightSwitch(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":rightSwitch($pos)",
        inViewOnly = inViewOnly,
        scopeElements = switchWidgets
    )
}

/**
 * rightSwitch
 */
fun TestElement.rightSwitch(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":rightSwitch($expression)",
        inViewOnly = inViewOnly,
        scopeElements = switchWidgets
    )
}

internal fun TestElement.below(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    return execRelativeCommand(
        relativeSelector = selector,
        getElement = {
            belowAboveCore(
                relative = RelativeDirection.below,
                selector = selector,
                inViewOnly = inViewOnly,
                targetElements = targetElements
            )
        }
    )
}

/**
 * below
 */
fun TestElement.below(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":below($pos)",
        inViewOnly = inViewOnly,
        scopeElements = widgets
    )
}

/**
 * below
 */
fun TestElement.below(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":below($expression)",
        inViewOnly = inViewOnly,
        scopeElements = widgets
    )
}

internal fun TestElement.belowInput(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":belowInput",
        selector = selector,
        className = ElementCategoryExpressionUtility.inputTypesExpression,
        inViewOnly = inViewOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * belowInput
 */
fun TestElement.belowInput(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":belowInput($pos)",
        inViewOnly = inViewOnly,
        scopeElements = inputWidgets
    )
}

/**
 * belowInput
 */
fun TestElement.belowInput(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":belowInput($expression)",
        inViewOnly = inViewOnly,
        scopeElements = inputWidgets
    )
}

internal fun TestElement.belowLabel(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":belowLabel",
        selector = selector,
        className = ElementCategoryExpressionUtility.labelTypesExpression,
        inViewOnly = inViewOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * belowLabel
 */
fun TestElement.belowLabel(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":belowLabel($pos)",
        inViewOnly = inViewOnly,
        scopeElements = labelWidgets
    )
}

/**
 * belowLabel
 */
fun TestElement.belowLabel(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":belowLabel($expression)",
        inViewOnly = inViewOnly,
        scopeElements = labelWidgets
    )
}

internal fun TestElement.belowImage(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":belowImage",
        selector = selector,
        className = ElementCategoryExpressionUtility.imageTypesExpression,
        inViewOnly = inViewOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * belowImage
 */
fun TestElement.belowImage(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":belowImage($pos)",
        inViewOnly = inViewOnly,
        scopeElements = imageWidgets
    )
}

/**
 * belowImage
 */
fun TestElement.belowImage(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":belowImage($expression)",
        inViewOnly = inViewOnly,
        scopeElements = imageWidgets
    )
}

internal fun TestElement.belowButton(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":belowButton",
        selector = selector,
        className = ElementCategoryExpressionUtility.buttonTypesExpression,
        inViewOnly = inViewOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * belowButton
 */
fun TestElement.belowButton(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":belowButton($pos)",
        inViewOnly = inViewOnly,
        scopeElements = buttonWidgets
    )
}

/**
 * belowButton
 */
fun TestElement.belowButton(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":belowButton($expression)",
        inViewOnly = inViewOnly,
        scopeElements = buttonWidgets
    )
}

internal fun TestElement.belowSwitch(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":belowSwitch",
        selector = selector,
        className = ElementCategoryExpressionUtility.switchTypesExpression,
        inViewOnly = inViewOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * belowSwitch
 */
fun TestElement.belowSwitch(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":belowSwitch($pos)",
        inViewOnly = inViewOnly,
        scopeElements = switchWidgets
    )
}

/**
 * belowSwitch
 */
fun TestElement.belowSwitch(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":belowSwitch($expression)",
        inViewOnly = inViewOnly,
        scopeElements = switchWidgets
    )
}

internal fun TestElement.left(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = execRelativeCommand(
        relativeSelector = selector,
        getElement = {
            rightLeftCore(
                relative = RelativeDirection.left,
                selector = selector,
                inViewOnly = inViewOnly,
                targetElements = targetElements
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
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":left($pos)",
        inViewOnly = inViewOnly,
        scopeElements = widgets
    )
}

/**
 * left
 */
fun TestElement.left(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":left($expression)",
        inViewOnly = inViewOnly,
        scopeElements = widgets
    )
}

internal fun TestElement.leftInput(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":leftInput",
        selector = selector,
        className = ElementCategoryExpressionUtility.inputTypesExpression,
        inViewOnly = inViewOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * leftInput
 */
fun TestElement.leftInput(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":leftInput($pos)",
        inViewOnly = inViewOnly,
        scopeElements = inputWidgets
    )
}

/**
 * leftInput
 */
fun TestElement.leftInput(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":leftInput($expression)",
        inViewOnly = inViewOnly,
        scopeElements = inputWidgets
    )
}

internal fun TestElement.leftLabel(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":leftLabel",
        selector = selector,
        className = ElementCategoryExpressionUtility.labelTypesExpression,
        inViewOnly = inViewOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * leftLabel
 */
fun TestElement.leftLabel(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":leftLabel($pos)",
        inViewOnly = inViewOnly,
        scopeElements = labelWidgets
    )
}

/**
 * leftLabel
 */
fun TestElement.leftLabel(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":leftLabel($expression)",
        inViewOnly = inViewOnly,
        scopeElements = labelWidgets
    )
}

internal fun TestElement.leftImage(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":leftImage",
        selector = selector,
        className = ElementCategoryExpressionUtility.imageTypesExpression,
        inViewOnly = inViewOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * leftImage
 */
fun TestElement.leftImage(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":leftImage($pos)",
        inViewOnly = inViewOnly,
        scopeElements = imageWidgets
    )
}

/**
 * leftImage
 */
fun TestElement.leftImage(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":leftImage($expression)",
        inViewOnly = inViewOnly,
        scopeElements = imageWidgets
    )
}

internal fun TestElement.leftButton(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":leftButton",
        selector = selector,
        className = ElementCategoryExpressionUtility.buttonTypesExpression,
        inViewOnly = inViewOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * leftButton
 */
fun TestElement.leftButton(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":leftButton($pos)",
        inViewOnly = inViewOnly,
        scopeElements = buttonWidgets
    )
}

/**
 * leftButton
 */
fun TestElement.leftButton(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":leftButton($expression)",
        inViewOnly = inViewOnly,
        scopeElements = buttonWidgets
    )
}

internal fun TestElement.leftSwitch(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":leftSwitch",
        selector = selector,
        className = ElementCategoryExpressionUtility.switchTypesExpression,
        inViewOnly = inViewOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * leftSwitch
 */
fun TestElement.leftSwitch(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":leftSwitch($pos)",
        inViewOnly = inViewOnly,
        scopeElements = switchWidgets
    )
}

/**
 * leftSwitch
 */
fun TestElement.leftSwitch(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":leftSwitch($expression)",
        inViewOnly = inViewOnly,
        scopeElements = switchWidgets
    )
}

internal fun TestElement.above(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    return execRelativeCommand(
        relativeSelector = selector,
        getElement = {
            belowAboveCore(
                relative = RelativeDirection.above,
                selector = selector,
                inViewOnly = inViewOnly,
                targetElements = targetElements
            )
        }
    )
}

/**
 * above
 */
fun TestElement.above(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":above($pos)",
        inViewOnly = inViewOnly,
        scopeElements = widgets
    )
}

/**
 * above
 */
fun TestElement.above(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":above($expression)",
        inViewOnly = inViewOnly,
        scopeElements = widgets
    )
}

internal fun TestElement.aboveInput(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":aboveInput",
        selector = selector,
        className = ElementCategoryExpressionUtility.inputTypesExpression,
        inViewOnly = inViewOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * aboveInput
 */
fun TestElement.aboveInput(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":aboveInput($pos)",
        inViewOnly = inViewOnly,
        scopeElements = inputWidgets
    )
}

/**
 * aboveInput
 */
fun TestElement.aboveInput(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":aboveInput($expression)",
        inViewOnly = inViewOnly,
        scopeElements = inputWidgets
    )
}

internal fun TestElement.aboveLabel(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":aboveLabel",
        selector = selector,
        className = ElementCategoryExpressionUtility.labelTypesExpression,
        inViewOnly = inViewOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * aboveLabel
 */
fun TestElement.aboveLabel(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":aboveLabel($pos)",
        inViewOnly = inViewOnly,
        scopeElements = labelWidgets
    )
}

/**
 * aboveLabel
 */
fun TestElement.aboveLabel(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":aboveLabel($expression)",
        inViewOnly = inViewOnly,
        scopeElements = labelWidgets
    )
}

internal fun TestElement.aboveImage(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":aboveImage",
        selector = selector,
        className = ElementCategoryExpressionUtility.imageTypesExpression,
        inViewOnly = inViewOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * aboveImage
 */
fun TestElement.aboveImage(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":aboveImage($pos)",
        inViewOnly = inViewOnly,
        scopeElements = imageWidgets
    )
}

/**
 * aboveImage
 */
fun TestElement.aboveImage(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":aboveImage($expression)",
        inViewOnly = inViewOnly,
        scopeElements = imageWidgets
    )
}

internal fun TestElement.aboveButton(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":aboveButton",
        selector = selector,
        className = ElementCategoryExpressionUtility.buttonTypesExpression,
        inViewOnly = inViewOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * aboveButton
 */
fun TestElement.aboveButton(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":aboveButton($pos)",
        inViewOnly = inViewOnly,
        scopeElements = buttonWidgets
    )
}

/**
 * aboveButton
 */
fun TestElement.aboveButton(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":aboveButton($expression)",
        inViewOnly = inViewOnly,
        scopeElements = buttonWidgets
    )
}

internal fun TestElement.aboveSwitch(
    selector: Selector,
    inViewOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":aboveSwitch",
        selector = selector,
        className = ElementCategoryExpressionUtility.switchTypesExpression,
        inViewOnly = inViewOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * aboveSwitch
 */
fun TestElement.aboveSwitch(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":aboveSwitch($pos)",
        inViewOnly = inViewOnly,
        scopeElements = switchWidgets
    )
}

/**
 * aboveSwitch
 */
fun TestElement.aboveSwitch(
    expression: String,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":aboveSwitch($expression)",
        inViewOnly = inViewOnly,
        scopeElements = switchWidgets
    )
}
