package shirates.core.driver.commandextension

import shirates.core.configuration.Filter
import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.driver.commandextension.helper.HorizontalBand
import shirates.core.driver.commandextension.helper.VerticalBand
import shirates.core.utility.element.ElementCategoryExpressionUtility

private fun TestElement.filterCandidates(
    selector: Selector,
    targetElements: List<TestElement>
): MutableList<TestElement> {

    val sel = selector.copy()
    if (sel.className == null) {
        val widgetTypes = ElementCategoryExpressionUtility.widgetTypesExpression
        sel.filterMap["className"] = Filter("className=$widgetTypes")
    }

    val filtered = targetElements
        .filterBySelector(selector = sel)
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
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copyAndRemovePos()
    sel.command = null
    val widgets = targetElements
        .filter { it.isWidget }
        .filterBySelector(sel)

    val horizontalBand = HorizontalBand(this)
    for (widget in widgets) {
        horizontalBand.merge(widget)
    }
    val elms = horizontalBand.getElements()
    val candidates = filterCandidates(
        selector = sel,
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
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copyAndRemovePos()
    val list = targetElements
        .filterBySelector(sel)

    val verticalBand = VerticalBand(this)
    for (e in list) {
        verticalBand.merge(e)
    }
    val elms = verticalBand.getElements()
    val candidates = filterCandidates(
        selector = sel,
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
    targetElements: List<TestElement>
): TestElement {

    val e = execRelativeCommand(
        relativeSelector = selector,
        getElement = {
            rightLeftCore(
                relative = RelativeDirection.right,
                selector = selector,
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
): TestElement {

    return relative(":right($pos)")
}

/**
 * right
 */
fun TestElement.right(
    expression: String,
): TestElement {

    return relative(":right($expression)")
}

internal fun TestElement.rightInput(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":rightInput",
        selector = selector,
        className = ElementCategoryExpressionUtility.inputTypesExpression,
        targetElements = targetElements
    )

    lastElement = e
    return e
}

private fun TestElement.getRelativeWidget(
    relativeCommand: String,
    selector: Selector,
    className: String,
    targetElements: List<TestElement>
): TestElement {

    val sel = selector.copy()
    sel.className = className

    val e = if (relativeCommand.startsWith(":right")) {
        right(selector = sel, targetElements = targetElements)
    } else if (relativeCommand.startsWith(":left")) {
        left(selector = sel, targetElements = targetElements)
    } else if (relativeCommand.startsWith(":above")) {
        above(selector = sel, targetElements = targetElements)
    } else if (relativeCommand.startsWith(":below")) {
        below(selector = sel, targetElements = targetElements)
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
): TestElement {

    return relative(":rightInput($pos)")
}

/**
 * rightInput
 */
fun TestElement.rightInput(
    expression: String,
): TestElement {

    return relative(":rightInput($expression)")
}

internal fun TestElement.rightLabel(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":rightLabel",
        selector = selector,
        className = ElementCategoryExpressionUtility.labelTypesExpression,
        targetElements = targetElements
    )
    return e
}

/**
 * rightLabel
 */
fun TestElement.rightLabel(
    pos: Int = 1,
): TestElement {

    return relative(":rightLabel($pos)")
}

/**
 * rightLabel
 */
fun TestElement.rightLabel(
    expression: String,
): TestElement {

    return relative(":rightLabel($expression)")
}

internal fun TestElement.rightImage(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":rightImage",
        selector = selector,
        className = ElementCategoryExpressionUtility.imageTypesExpression,
        targetElements = targetElements
    )
    return e
}

/**
 * rightImage
 */
fun TestElement.rightImage(
    pos: Int = 1,
): TestElement {

    return relative(":rightImage($pos)")
}

/**
 * rightImage
 */
fun TestElement.rightImage(
    expression: String
): TestElement {

    return relative(":rightImage($expression)")
}

internal fun TestElement.rightButton(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":rightButton",
        selector = selector,
        className = ElementCategoryExpressionUtility.buttonTypesExpression,
        targetElements = targetElements
    )
    return e
}

/**
 * rightButton
 */
fun TestElement.rightButton(
    pos: Int = 1,
): TestElement {

    return relative(":rightButton($pos)")
}

/**
 * rightButton
 */
fun TestElement.rightButton(
    expression: String,
): TestElement {

    return relative(":rightButton($expression)")
}

internal fun TestElement.rightSwitch(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":rightSwitch",
        selector = selector,
        className = ElementCategoryExpressionUtility.switchTypesExpression,
        targetElements = targetElements
    )
    return e
}

/**
 * rightSwitch
 */
fun TestElement.rightSwitch(
    pos: Int = 1,
): TestElement {

    return relative(":rightSwitch($pos)")
}

/**
 * rightSwitch
 */
fun TestElement.rightSwitch(
    expression: String,
): TestElement {

    return relative(":rightSwitch($expression)")
}

internal fun TestElement.below(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    return execRelativeCommand(
        relativeSelector = selector,
        getElement = {
            belowAboveCore(
                relative = RelativeDirection.below,
                selector = selector,
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
): TestElement {

    return relative(":below($pos)")
}

/**
 * below
 */
fun TestElement.below(
    expression: String,
): TestElement {

    return relative(":below($expression)")
}

internal fun TestElement.belowInput(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":belowInput",
        selector = selector,
        className = ElementCategoryExpressionUtility.inputTypesExpression,
        targetElements = targetElements
    )
    return e
}

/**
 * belowInput
 */
fun TestElement.belowInput(
    pos: Int = 1,
): TestElement {

    return relative(":belowInput($pos)")
}

/**
 * belowInput
 */
fun TestElement.belowInput(
    expression: String,
): TestElement {

    return relative(":belowInput($expression)")
}

internal fun TestElement.belowLabel(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":belowLabel",
        selector = selector,
        className = ElementCategoryExpressionUtility.labelTypesExpression,
        targetElements = targetElements
    )
    return e
}

/**
 * belowLabel
 */
fun TestElement.belowLabel(
    pos: Int = 1,
): TestElement {

    return relative(":belowLabel($pos)")
}

/**
 * belowLabel
 */
fun TestElement.belowLabel(
    expression: String,
): TestElement {

    return relative(":belowLabel($expression)")
}

internal fun TestElement.belowImage(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":belowImage",
        selector = selector,
        className = ElementCategoryExpressionUtility.imageTypesExpression,
        targetElements = targetElements
    )
    return e
}

/**
 * belowImage
 */
fun TestElement.belowImage(
    pos: Int = 1,
): TestElement {

    return relative(":belowImage($pos)")
}

/**
 * belowImage
 */
fun TestElement.belowImage(
    expression: String,
): TestElement {

    return relative(":belowImage($expression)")
}

internal fun TestElement.belowButton(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":belowButton",
        selector = selector,
        className = ElementCategoryExpressionUtility.buttonTypesExpression,
        targetElements = targetElements
    )
    return e
}

/**
 * belowButton
 */
fun TestElement.belowButton(
    pos: Int = 1,
): TestElement {

    return relative(":belowButton($pos)")
}

/**
 * belowButton
 */
fun TestElement.belowButton(
    expression: String,
): TestElement {

    return relative(":belowButton($expression)")
}

internal fun TestElement.belowSwitch(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":belowSwitch",
        selector = selector,
        className = ElementCategoryExpressionUtility.switchTypesExpression,
        targetElements = targetElements
    )
    return e
}

/**
 * belowSwitch
 */
fun TestElement.belowSwitch(
    pos: Int = 1,
): TestElement {

    return relative(":belowSwitch($pos)")
}

/**
 * belowSwitch
 */
fun TestElement.belowSwitch(
    expression: String,
): TestElement {

    return relative(":belowSwitch($expression)")
}

internal fun TestElement.left(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val e = execRelativeCommand(
        relativeSelector = selector,
        getElement = {
            rightLeftCore(
                relative = RelativeDirection.left,
                selector = selector,
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
): TestElement {

    return relative(":left($pos)")
}

/**
 * left
 */
fun TestElement.left(
    expression: String,
): TestElement {

    return relative(":left($expression)")
}

internal fun TestElement.leftInput(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":leftInput",
        selector = selector,
        className = ElementCategoryExpressionUtility.inputTypesExpression,
        targetElements = targetElements
    )
    return e
}

/**
 * leftInput
 */
fun TestElement.leftInput(
    pos: Int = 1,
): TestElement {

    return relative(":leftInput($pos)")
}

/**
 * leftInput
 */
fun TestElement.leftInput(
    expression: String,
): TestElement {

    return relative(":leftInput($expression)")
}

internal fun TestElement.leftLabel(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":leftLabel",
        selector = selector,
        className = ElementCategoryExpressionUtility.labelTypesExpression,
        targetElements = targetElements
    )
    return e
}

/**
 * leftLabel
 */
fun TestElement.leftLabel(
    pos: Int = 1,
): TestElement {

    return relative(":leftLabel($pos)")
}

/**
 * leftLabel
 */
fun TestElement.leftLabel(
    expression: String,
): TestElement {

    return relative(":leftLabel($expression)")
}

internal fun TestElement.leftImage(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":leftImage",
        selector = selector,
        className = ElementCategoryExpressionUtility.imageTypesExpression,
        targetElements = targetElements
    )
    return e
}

/**
 * leftImage
 */
fun TestElement.leftImage(
    pos: Int = 1,
): TestElement {

    return relative(":leftImage($pos)")
}

/**
 * leftImage
 */
fun TestElement.leftImage(
    expression: String,
): TestElement {

    return relative(":leftImage($expression)")
}

internal fun TestElement.leftButton(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":leftButton",
        selector = selector,
        className = ElementCategoryExpressionUtility.buttonTypesExpression,
        targetElements = targetElements
    )
    return e
}

/**
 * leftButton
 */
fun TestElement.leftButton(
    pos: Int = 1,
): TestElement {

    return relative(":leftButton($pos)")
}

/**
 * leftButton
 */
fun TestElement.leftButton(
    expression: String,
): TestElement {

    return relative(":leftButton($expression)")
}

internal fun TestElement.leftSwitch(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":leftSwitch",
        selector = selector,
        className = ElementCategoryExpressionUtility.switchTypesExpression,
        targetElements = targetElements
    )
    return e
}

/**
 * leftSwitch
 */
fun TestElement.leftSwitch(
    pos: Int = 1,
): TestElement {

    return relative(":leftSwitch($pos)")
}

/**
 * leftSwitch
 */
fun TestElement.leftSwitch(
    expression: String,
): TestElement {

    return relative(":leftSwitch($expression)")
}

internal fun TestElement.above(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    return execRelativeCommand(
        relativeSelector = selector,
        getElement = {
            belowAboveCore(
                relative = RelativeDirection.above,
                selector = selector,
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
): TestElement {

    return relative(":above($pos)")
}

/**
 * above
 */
fun TestElement.above(
    expression: String,
): TestElement {

    return relative(":above($expression)")
}

internal fun TestElement.aboveInput(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":aboveInput",
        selector = selector,
        className = ElementCategoryExpressionUtility.inputTypesExpression,
        targetElements = targetElements
    )
    return e
}

/**
 * aboveInput
 */
fun TestElement.aboveInput(
    pos: Int = 1,
): TestElement {

    return relative(":aboveInput($pos)")
}

/**
 * aboveInput
 */
fun TestElement.aboveInput(
    expression: String,
): TestElement {

    return relative(":aboveInput($expression)")
}

internal fun TestElement.aboveLabel(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":aboveLabel",
        selector = selector,
        className = ElementCategoryExpressionUtility.labelTypesExpression,
        targetElements = targetElements
    )
    return e
}

/**
 * aboveLabel
 */
fun TestElement.aboveLabel(
    pos: Int = 1,
): TestElement {

    return relative(":aboveLabel($pos)")
}

/**
 * aboveLabel
 */
fun TestElement.aboveLabel(
    expression: String,
): TestElement {

    return relative(":aboveLabel($expression)")
}

internal fun TestElement.aboveImage(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":aboveImage",
        selector = selector,
        className = ElementCategoryExpressionUtility.imageTypesExpression,
        targetElements = targetElements
    )
    return e
}

/**
 * aboveImage
 */
fun TestElement.aboveImage(
    pos: Int = 1,
): TestElement {

    return relative(":aboveImage($pos)")
}

/**
 * aboveImage
 */
fun TestElement.aboveImage(
    expression: String,
): TestElement {

    return relative(":aboveImage($expression)")
}

internal fun TestElement.aboveButton(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":aboveButton",
        selector = selector,
        className = ElementCategoryExpressionUtility.buttonTypesExpression,
        targetElements = targetElements
    )
    return e
}

/**
 * aboveButton
 */
fun TestElement.aboveButton(
    pos: Int = 1,
): TestElement {

    return relative(":aboveButton($pos)")
}

/**
 * aboveButton
 */
fun TestElement.aboveButton(
    expression: String,
): TestElement {

    return relative(":aboveButton($expression)")
}

internal fun TestElement.aboveSwitch(
    selector: Selector,
    targetElements: List<TestElement>
): TestElement {

    val e = getRelativeWidget(
        relativeCommand = ":aboveSwitch",
        selector = selector,
        className = ElementCategoryExpressionUtility.switchTypesExpression,
        targetElements = targetElements
    )
    return e
}

/**
 * aboveSwitch
 */
fun TestElement.aboveSwitch(
    pos: Int = 1,
): TestElement {

    return relative(":aboveSwitch($pos)")
}

/**
 * aboveSwitch
 */
fun TestElement.aboveSwitch(
    expression: String,
): TestElement {

    return relative(":aboveSwitch($expression)")
}
