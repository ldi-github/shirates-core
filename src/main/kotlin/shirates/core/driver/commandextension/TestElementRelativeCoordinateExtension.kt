package shirates.core.driver.commandextension

import shirates.core.configuration.Filter
import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.driver.commandextension.helper.HorizontalBand
import shirates.core.driver.commandextension.helper.VerticalBand
import shirates.core.utility.element.ElementCategoryExpressionUtility

private fun TestElement.filterCandidates(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): MutableList<TestElement> {

    val sel = selector.copy()
    if (sel.className == null) {
        val widgetTypes = ElementCategoryExpressionUtility.widgetTypesExpression
        sel.filterMap["className"] = Filter("className=$widgetTypes")
    }

    val filtered = targetElements
        .filterBySelector(selector = sel, safeElementOnly = safeElementOnly)
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
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copyAndRemovePos()
    sel.command = null
    val widgets = targetElements
        .filter { it.isWidget }
        .filterBySelector(selector = sel, safeElementOnly = safeElementOnly)

    val horizontalBand = HorizontalBand(this)
    for (widget in widgets) {
        horizontalBand.merge(widget)
    }
    val elms = horizontalBand.getElements()
    val candidates = filterCandidates(
        selector = sel,
        safeElementOnly = safeElementOnly,
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
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copyAndRemovePos()
    val list = targetElements
        .filterBySelector(selector = sel, safeElementOnly = safeElementOnly)

    val verticalBand = VerticalBand(this)
    for (e in list) {
        verticalBand.merge(e)
    }
    val elms = verticalBand.getElements()
    val candidates = filterCandidates(
        selector = sel,
        safeElementOnly = safeElementOnly,
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
    val pos =
        if (selector.pos != null) selector.pos!!
        else 1
    val e =
        if (sortedElements.isEmpty() || sortedElements.count() < pos) TestElement()
        else sortedElements[pos - 1]

    TestDriver.lastElement = e

    return e
}

internal fun TestElement.right(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = execRelativeCommand(
        relativeSelector = selector,
        getElement = {
            rightLeftCore(
                relative = RelativeDirection.right,
                selector = selector,
                safeElementOnly = safeElementOnly,
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
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":right($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = widgets
    )
}

/**
 * right
 */
fun TestElement.right(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":right($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = widgets
    )
}

internal fun TestElement.rightInput(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":rightInput",
        selector = selector,
        className = ElementCategoryExpressionUtility.inputTypesExpression,
        safeElementOnly = safeElementOnly,
        targetElements = targetElements
    )

    lastElement = e
    return e
}

private fun TestElement.getRelativeWidget(
    relativeCommand: String,
    selector: Selector,
    className: String,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = className

    val e = if (relativeCommand.startsWith(":right")) {
        right(selector = sel, safeElementOnly = safeElementOnly, targetElements = targetElements)
    } else if (relativeCommand.startsWith(":left")) {
        left(selector = sel, safeElementOnly = safeElementOnly, targetElements = targetElements)
    } else if (relativeCommand.startsWith(":above")) {
        above(selector = sel, safeElementOnly = safeElementOnly, targetElements = targetElements)
    } else if (relativeCommand.startsWith(":below")) {
        below(selector = sel, safeElementOnly = safeElementOnly, targetElements = targetElements)
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
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":rightInput($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = inputWidgets
    )
}

/**
 * rightInput
 */
fun TestElement.rightInput(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":rightInput($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = inputWidgets
    )
}

internal fun TestElement.rightLabel(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":rightLabel",
        selector = selector,
        className = ElementCategoryExpressionUtility.labelTypesExpression,
        safeElementOnly = safeElementOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * rightLabel
 */
fun TestElement.rightLabel(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":rightLabel($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = widgets
    )
}

/**
 * rightLabel
 */
fun TestElement.rightLabel(
    expression: String,
    safeElementOnly: Boolean = true,
): TestElement {

    return relative(
        command = ":rightLabel($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = labelWidgets
    )
}

internal fun TestElement.rightImage(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":rightImage",
        selector = selector,
        className = ElementCategoryExpressionUtility.imageTypesExpression,
        safeElementOnly = safeElementOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * rightImage
 */
fun TestElement.rightImage(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":rightImage($pos)",
        safeElementOnly = safeElementOnly,
        imageWidgets
    )
}

/**
 * rightImage
 */
fun TestElement.rightImage(
    expression: String,
    safeElementOnly: Boolean = true,
): TestElement {

    return relative(
        command = ":rightImage($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = imageWidgets
    )
}

internal fun TestElement.rightButton(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":rightButton",
        selector = selector,
        className = ElementCategoryExpressionUtility.buttonTypesExpression,
        safeElementOnly = safeElementOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * rightButton
 */
fun TestElement.rightButton(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":rightButton($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = buttonWidgets
    )
}

/**
 * rightButton
 */
fun TestElement.rightButton(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":rightButton($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = buttonWidgets
    )
}

internal fun TestElement.rightSwitch(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":rightSwitch",
        selector = selector,
        className = ElementCategoryExpressionUtility.switchTypesExpression,
        safeElementOnly = safeElementOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * rightSwitch
 */
fun TestElement.rightSwitch(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":rightSwitch($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = switchWidgets
    )
}

/**
 * rightSwitch
 */
fun TestElement.rightSwitch(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":rightSwitch($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = switchWidgets
    )
}

internal fun TestElement.below(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    return execRelativeCommand(
        relativeSelector = selector,
        getElement = {
            belowAboveCore(
                relative = RelativeDirection.below,
                selector = selector,
                safeElementOnly = safeElementOnly,
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
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":below($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = widgets
    )
}

/**
 * below
 */
fun TestElement.below(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":below($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = widgets
    )
}

internal fun TestElement.belowInput(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":belowInput",
        selector = selector,
        className = ElementCategoryExpressionUtility.inputTypesExpression,
        safeElementOnly = safeElementOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * belowInput
 */
fun TestElement.belowInput(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":belowInput($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = inputWidgets
    )
}

/**
 * belowInput
 */
fun TestElement.belowInput(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":belowInput($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = inputWidgets
    )
}

internal fun TestElement.belowLabel(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":belowLabel",
        selector = selector,
        className = ElementCategoryExpressionUtility.labelTypesExpression,
        safeElementOnly = safeElementOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * belowLabel
 */
fun TestElement.belowLabel(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":belowLabel($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = labelWidgets
    )
}

/**
 * belowLabel
 */
fun TestElement.belowLabel(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":belowLabel($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = labelWidgets
    )
}

internal fun TestElement.belowImage(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":belowImage",
        selector = selector,
        className = ElementCategoryExpressionUtility.imageTypesExpression,
        safeElementOnly = safeElementOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * belowImage
 */
fun TestElement.belowImage(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":belowImage($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = imageWidgets
    )
}

/**
 * belowImage
 */
fun TestElement.belowImage(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":belowImage($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = imageWidgets
    )
}

internal fun TestElement.belowButton(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":belowButton",
        selector = selector,
        className = ElementCategoryExpressionUtility.buttonTypesExpression,
        safeElementOnly = safeElementOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * belowButton
 */
fun TestElement.belowButton(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":belowButton($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = buttonWidgets
    )
}

/**
 * belowButton
 */
fun TestElement.belowButton(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":belowButton($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = buttonWidgets
    )
}

internal fun TestElement.belowSwitch(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":belowSwitch",
        selector = selector,
        className = ElementCategoryExpressionUtility.switchTypesExpression,
        safeElementOnly = safeElementOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * belowSwitch
 */
fun TestElement.belowSwitch(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":belowSwitch($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = switchWidgets
    )
}

/**
 * belowSwitch
 */
fun TestElement.belowSwitch(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":belowSwitch($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = switchWidgets
    )
}

internal fun TestElement.left(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = execRelativeCommand(
        relativeSelector = selector,
        getElement = {
            rightLeftCore(
                relative = RelativeDirection.left,
                selector = selector,
                safeElementOnly = safeElementOnly,
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
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":left($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = widgets
    )
}

/**
 * left
 */
fun TestElement.left(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":left($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = widgets
    )
}

internal fun TestElement.leftInput(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":leftInput",
        selector = selector,
        className = ElementCategoryExpressionUtility.inputTypesExpression,
        safeElementOnly = safeElementOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * leftInput
 */
fun TestElement.leftInput(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":leftInput($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = inputWidgets
    )
}

/**
 * leftInput
 */
fun TestElement.leftInput(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":leftInput($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = inputWidgets
    )
}

internal fun TestElement.leftLabel(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":leftLabel",
        selector = selector,
        className = ElementCategoryExpressionUtility.labelTypesExpression,
        safeElementOnly = safeElementOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * leftLabel
 */
fun TestElement.leftLabel(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":leftLabel($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = labelWidgets
    )
}

/**
 * leftLabel
 */
fun TestElement.leftLabel(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":leftLabel($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = labelWidgets
    )
}

internal fun TestElement.leftImage(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":leftImage",
        selector = selector,
        className = ElementCategoryExpressionUtility.imageTypesExpression,
        safeElementOnly = safeElementOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * leftImage
 */
fun TestElement.leftImage(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":leftImage($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = imageWidgets
    )
}

/**
 * leftImage
 */
fun TestElement.leftImage(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":leftImage($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = imageWidgets
    )
}

internal fun TestElement.leftButton(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":leftButton",
        selector = selector,
        className = ElementCategoryExpressionUtility.buttonTypesExpression,
        safeElementOnly = safeElementOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * leftButton
 */
fun TestElement.leftButton(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":leftButton($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = buttonWidgets
    )
}

/**
 * leftButton
 */
fun TestElement.leftButton(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":leftButton($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = buttonWidgets
    )
}

internal fun TestElement.leftSwitch(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":leftSwitch",
        selector = selector,
        className = ElementCategoryExpressionUtility.switchTypesExpression,
        safeElementOnly = safeElementOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * leftSwitch
 */
fun TestElement.leftSwitch(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":leftSwitch($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = switchWidgets
    )
}

/**
 * leftSwitch
 */
fun TestElement.leftSwitch(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":leftSwitch($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = switchWidgets
    )
}

internal fun TestElement.above(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    return execRelativeCommand(
        relativeSelector = selector,
        getElement = {
            belowAboveCore(
                relative = RelativeDirection.above,
                selector = selector,
                safeElementOnly = safeElementOnly,
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
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":above($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = widgets
    )
}

/**
 * above
 */
fun TestElement.above(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":above($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = widgets
    )
}

internal fun TestElement.aboveInput(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":aboveInput",
        selector = selector,
        className = ElementCategoryExpressionUtility.inputTypesExpression,
        safeElementOnly = safeElementOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * aboveInput
 */
fun TestElement.aboveInput(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":aboveInput($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = inputWidgets
    )
}

/**
 * aboveInput
 */
fun TestElement.aboveInput(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":aboveInput($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = inputWidgets
    )
}

internal fun TestElement.aboveLabel(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":aboveLabel",
        selector = selector,
        className = ElementCategoryExpressionUtility.labelTypesExpression,
        safeElementOnly = safeElementOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * aboveLabel
 */
fun TestElement.aboveLabel(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":aboveLabel($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = labelWidgets
    )
}

/**
 * aboveLabel
 */
fun TestElement.aboveLabel(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":aboveLabel($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = labelWidgets
    )
}

internal fun TestElement.aboveImage(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":aboveImage",
        selector = selector,
        className = ElementCategoryExpressionUtility.imageTypesExpression,
        safeElementOnly = safeElementOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * aboveImage
 */
fun TestElement.aboveImage(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":aboveImage($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = imageWidgets
    )
}

/**
 * aboveImage
 */
fun TestElement.aboveImage(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":aboveImage($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = imageWidgets
    )
}

internal fun TestElement.aboveButton(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":aboveButton",
        selector = selector,
        className = ElementCategoryExpressionUtility.buttonTypesExpression,
        safeElementOnly = safeElementOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * aboveButton
 */
fun TestElement.aboveButton(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":aboveButton($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = buttonWidgets
    )
}

/**
 * aboveButton
 */
fun TestElement.aboveButton(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":aboveButton($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = buttonWidgets
    )
}

internal fun TestElement.aboveSwitch(
    selector: Selector,
    safeElementOnly: Boolean,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":aboveSwitch",
        selector = selector,
        className = ElementCategoryExpressionUtility.switchTypesExpression,
        safeElementOnly = safeElementOnly,
        targetElements = targetElements
    )
    return e
}

/**
 * aboveSwitch
 */
fun TestElement.aboveSwitch(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":aboveSwitch($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = switchWidgets
    )
}

/**
 * aboveSwitch
 */
fun TestElement.aboveSwitch(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":aboveSwitch($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = switchWidgets
    )
}
