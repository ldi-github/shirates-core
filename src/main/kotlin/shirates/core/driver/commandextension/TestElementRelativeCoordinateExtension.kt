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

    return relative(":right($pos)", safeElementOnly = safeElementOnly)
}

/**
 * right
 */
fun TestElement.right(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":right($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":rightInput($pos)", safeElementOnly = safeElementOnly)
}

/**
 * rightInput
 */
fun TestElement.rightInput(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":rightInput($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":rightLabel($pos)", safeElementOnly = safeElementOnly)
}

/**
 * rightLabel
 */
fun TestElement.rightLabel(
    expression: String,
    safeElementOnly: Boolean = true,
): TestElement {

    return relative(":rightLabel($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":rightImage($pos)", safeElementOnly = safeElementOnly)
}

/**
 * rightImage
 */
fun TestElement.rightImage(
    expression: String,
    safeElementOnly: Boolean = true,
): TestElement {

    return relative(":rightImage($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":rightButton($pos)", safeElementOnly = safeElementOnly)
}

/**
 * rightButton
 */
fun TestElement.rightButton(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":rightButton($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":rightSwitch($pos)", safeElementOnly = safeElementOnly)
}

/**
 * rightSwitch
 */
fun TestElement.rightSwitch(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":rightSwitch($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":below($pos)", safeElementOnly = safeElementOnly)
}

/**
 * below
 */
fun TestElement.below(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":below($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":belowInput($pos)", safeElementOnly = safeElementOnly)
}

/**
 * belowInput
 */
fun TestElement.belowInput(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":belowInput($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":belowLabel($pos)", safeElementOnly = safeElementOnly)
}

/**
 * belowLabel
 */
fun TestElement.belowLabel(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":belowLabel($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":belowImage($pos)", safeElementOnly = safeElementOnly)
}

/**
 * belowImage
 */
fun TestElement.belowImage(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":belowImage($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":belowButton($pos)", safeElementOnly = safeElementOnly)
}

/**
 * belowButton
 */
fun TestElement.belowButton(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":belowButton($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":belowSwitch($pos)", safeElementOnly = safeElementOnly)
}

/**
 * belowSwitch
 */
fun TestElement.belowSwitch(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":belowSwitch($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":left($pos)", safeElementOnly = safeElementOnly)
}

/**
 * left
 */
fun TestElement.left(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":left($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":leftInput($pos)", safeElementOnly = safeElementOnly)
}

/**
 * leftInput
 */
fun TestElement.leftInput(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":leftInput($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":leftLabel($pos)", safeElementOnly = safeElementOnly)
}

/**
 * leftLabel
 */
fun TestElement.leftLabel(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":leftLabel($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":leftImage($pos)", safeElementOnly = safeElementOnly)
}

/**
 * leftImage
 */
fun TestElement.leftImage(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":leftImage($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":leftButton($pos)", safeElementOnly = safeElementOnly)
}

/**
 * leftButton
 */
fun TestElement.leftButton(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":leftButton($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":leftSwitch($pos)", safeElementOnly = safeElementOnly)
}

/**
 * leftSwitch
 */
fun TestElement.leftSwitch(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":leftSwitch($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":above($pos)", safeElementOnly = safeElementOnly)
}

/**
 * above
 */
fun TestElement.above(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":above($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":aboveInput($pos)", safeElementOnly = safeElementOnly)
}

/**
 * aboveInput
 */
fun TestElement.aboveInput(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":aboveInput($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":aboveLabel($pos)", safeElementOnly = safeElementOnly)
}

/**
 * aboveLabel
 */
fun TestElement.aboveLabel(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":aboveLabel($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":aboveImage($pos)", safeElementOnly = safeElementOnly)
}

/**
 * aboveImage
 */
fun TestElement.aboveImage(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":aboveImage($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":aboveButton($pos)", safeElementOnly = safeElementOnly)
}

/**
 * aboveButton
 */
fun TestElement.aboveButton(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":aboveButton($expression)", safeElementOnly = safeElementOnly)
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

    return relative(":aboveSwitch($pos)", safeElementOnly = safeElementOnly)
}

/**
 * aboveSwitch
 */
fun TestElement.aboveSwitch(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":aboveSwitch($expression)", safeElementOnly = safeElementOnly)
}
