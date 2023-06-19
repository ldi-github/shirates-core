package shirates.core.driver.commandextension

import shirates.core.configuration.Selector
import shirates.core.configuration.removeRedundantExpression
import shirates.core.driver.*

/**
 * relative
 */
fun TestElement.relative(
    command: String,
    safeElementOnly: Boolean = true,
    scopeElements: List<TestElement> = elements,
): TestElement {

    val c = command.removeRedundantExpression()
    val relativeSelector = Selector(c)

    val oldSelector = this.selector
    val newSelector = this.getChainedSelector(relativeSelector = relativeSelector)

    val e = relative(
        relativeSelector = relativeSelector,
        safeElementOnly = safeElementOnly,
        scopeElements = scopeElements
    )
    if (e == this) {
        e.selector = oldSelector
    } else {
        e.selector = newSelector
    }

    return e
}

internal fun TestElement.relative(
    relativeSelector: Selector,
    newSelector: Selector = this.getChainedSelector(relativeSelector = relativeSelector),
    safeElementOnly: Boolean,
    scopeElements: List<TestElement>
): TestElement {

    val oldSelector = this.selector
    val targetElements = scopeElements

    val context = TestDriverCommandContext(this)
    var e = TestElement(selector = newSelector)
    context.execRelativeCommand("${this.subject}:${relativeSelector}", subject = this.subject) {

        when (relativeSelector.command) {

            /**
             * direction based
             */
            ":right" -> {
                e = this.right(
                    selector = relativeSelector,
                    targetElements = targetElements,
                    safeElementOnly = safeElementOnly
                )
            }

            ":rightInput" -> {
                e = this.rightInput(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":rightLabel" -> {
                e = this.rightLabel(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":rightImage" -> {
                e = this.rightImage(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":rightButton" -> {
                e = this.rightButton(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":rightSwitch" -> {
                e = this.rightSwitch(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":below" -> {
                e = this.below(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":belowInput" -> {
                e = this.belowInput(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":belowLabel" -> {
                e = this.belowLabel(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":belowImage" -> {
                e = this.belowImage(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":belowButton" -> {
                e = this.belowButton(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":belowSwitch" -> {
                e = this.belowSwitch(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":left" -> {
                e = this.left(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":leftInput" -> {
                e = this.leftInput(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":leftLabel" -> {
                e = this.leftLabel(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":leftImage" -> {
                e = this.leftImage(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":leftButton" -> {
                e = this.leftButton(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":leftSwitch" -> {
                e = this.leftSwitch(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":above" -> {
                e = this.above(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":aboveInput" -> {
                e = this.aboveInput(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":aboveLabel" -> {
                e = this.aboveLabel(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":aboveImage" -> {
                e = this.aboveImage(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":aboveButton" -> {
                e = this.aboveButton(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":aboveSwitch" -> {
                e = this.aboveSwitch(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            /**
             * Widget flow based
             */
            ":flow" -> {
                e = this.flow(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":vflow" -> {
                e = this.vflow(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":flowLabel", ":label" -> {
                e = this.flowLabel(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":flowInput", ":input" -> {
                e = this.flowInput(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":flowImage", ":image" -> {
                e = this.flowImage(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":flowButton", ":button" -> {
                e = this.flowButton(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":flowSwitch", ":switch" -> {
                e = this.flowSwitch(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":innerFlow", ":inner" -> {
                e = this.innerFlow(selector = relativeSelector, safeElementOnly = safeElementOnly)
            }

            ":innerLabel" -> {
                e = this.innerLabel(selector = relativeSelector, safeElementOnly = safeElementOnly)
            }

            ":innerInput" -> {
                e = this.innerInput(selector = relativeSelector, safeElementOnly = safeElementOnly)
            }

            ":innerImage" -> {
                e = this.innerImage(selector = relativeSelector, safeElementOnly = safeElementOnly)
            }

            ":innerButton" -> {
                e = this.innerButton(selector = relativeSelector, safeElementOnly = safeElementOnly)
            }

            ":innerSwitch" -> {
                e = this.innerSwitch(selector = relativeSelector, safeElementOnly = safeElementOnly)
            }

            ":innerVflow", ":innerV" -> {
                e = this.innerVflow(selector = relativeSelector, safeElementOnly = safeElementOnly)
            }

            ":innerVlabel" -> {
                e = this.innerVlabel(selector = relativeSelector, safeElementOnly = safeElementOnly)
            }

            ":innerVinput" -> {
                e = this.innerVinput(selector = relativeSelector, safeElementOnly = safeElementOnly)
            }

            ":innerVimage" -> {
                e = this.innerVimage(selector = relativeSelector, safeElementOnly = safeElementOnly)
            }

            ":innerVbutton" -> {
                e = this.innerVbutton(selector = relativeSelector, safeElementOnly = safeElementOnly)
            }

            ":innerVswitch" -> {
                e = this.innerVswitch(selector = relativeSelector, safeElementOnly = safeElementOnly)
            }

            /**
             * XML based
             */
            ":child" -> {
                e = this.child(selector = relativeSelector, safeElementOnly = safeElementOnly)
            }

            ":sibling" -> {
                e = this.sibling(selector = relativeSelector, safeElementOnly = safeElementOnly)
            }

            ":parent" -> {
                e = this.parent()
            }

            ":ancestor" -> {
                e = this.ancestor(selector = relativeSelector, safeElementOnly = safeElementOnly)
            }

            ":descendant" -> {
                e = this.descendant(selector = relativeSelector, safeElementOnly = safeElementOnly)
            }

            ":next" -> {
                e = this.next(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":previous" -> {
                e = this.previous(
                    selector = relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":nextInput" -> {
                e = this.nextInput(relativeSelector, safeElementOnly = safeElementOnly, targetElements = targetElements)
            }

            ":preInput" -> {
                e = this.preInput(relativeSelector, safeElementOnly = safeElementOnly, targetElements = targetElements)
            }

            ":nextLabel" -> {
                e = this.nextLabel(relativeSelector, safeElementOnly = safeElementOnly, targetElements = targetElements)
            }

            ":preLabel" -> {
                e = this.preLabel(relativeSelector, safeElementOnly = safeElementOnly, targetElements = targetElements)
            }

            ":nextImage" -> {
                e = this.nextImage(relativeSelector, safeElementOnly = safeElementOnly, targetElements = targetElements)
            }

            ":preImage" -> {
                e = this.preImage(relativeSelector, safeElementOnly = safeElementOnly, targetElements = targetElements)
            }

            ":nextButton" -> {
                e = this.nextButton(
                    relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":preButton" -> {
                e = this.preButton(relativeSelector, safeElementOnly = safeElementOnly, targetElements = targetElements)
            }

            ":nextSwitch" -> {
                e = this.nextSwitch(
                    relativeSelector,
                    safeElementOnly = safeElementOnly,
                    targetElements = targetElements
                )
            }

            ":preSwitch" -> {
                e = this.preSwitch(relativeSelector, safeElementOnly = safeElementOnly, targetElements = targetElements)
            }

            // negation
            ":not" -> {
                e = this.not()
            }

            else -> {
                if (relativeSelector.command.isNullOrBlank().not()) {
                    throw NotImplementedError("command.funcName='${relativeSelector.command}' is not supported")
                }
            }
        }
    }

    if (e.isFound) {
        if (e == this) {
            e.selector = oldSelector
        } else {
            e.selector = newSelector
        }
    }

    return e
}

/**
 * getRelative
 */
fun TestElement.relative(
    relativeSelectors: List<Selector>,
    safeElementOnly: Boolean = true,
    scopeElements: List<TestElement> = elements
): TestElement {

    var e = this
    for (selector in relativeSelectors) {
        e = e.relative(
            relativeSelector = selector,
            safeElementOnly = safeElementOnly,
            scopeElements = scopeElements
        )
    }

    return e
}

internal fun TestElement.child(
    selector: Selector,
    safeElementOnly: Boolean
): TestElement {

    var e = TestElement(selector = selector)
    val context = TestDriverCommandContext(this)
    context.execRelativeCommand(subject = subject, arg1 = selector.nickname) {
        val te = this
        val children = te.children
        if (children.count() == 0) {
            TestDriver.lastElement = TestElement.emptyElement
            return@execRelativeCommand
        }
        val target = children.filterBySelector(selector = selector, safeElementOnly = safeElementOnly)
        e = target.firstOrNull() ?: TestElement.emptyElement
    }

    e.selector = this.getChainedSelector(relativeCommand = "$selector")

    lastElement = e
    return lastElement
}

/**
 * child
 */
fun TestElement.child(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":child($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = children
    )
}

/**
 * child
 */
fun TestElement.child(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":child($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = children
    )
}

internal fun TestElement.sibling(
    selector: Selector,
    safeElementOnly: Boolean
): TestElement {

    var e = TestElement(selector = selector)
    val context = TestDriverCommandContext(this)
    context.execRelativeCommand(subject = subject, arg1 = selector.nickname) {
        val te = this
        val p = te.parentElement
        if (p == null) {
            TestDriver.lastElement = TestElement.emptyElement
            return@execRelativeCommand
        }
        val siblings = this.siblings
        val targets = siblings.filterBySelector(selector = selector, safeElementOnly = safeElementOnly)
        e = targets.firstOrNull() ?: TestElement.emptyElement
    }

    e.selector = this.getChainedSelector(relativeCommand = "$selector")
    lastElement = e

    return lastElement
}

/**
 * sibling
 */
fun TestElement.sibling(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":sibling($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = parent().children
    )
}

/**
 * sibling
 */
fun TestElement.sibling(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":sibling($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = parent().children
    )
}

/**
 * parent
 */
fun TestElement.parent(): TestElement {

    var e = TestElement()
    val context = TestDriverCommandContext(this)
    context.execRelativeCommand(subject = subject) {
        val filtered = this.ancestors.reversed()
        e = filtered.firstOrNull() ?: TestElement.emptyElement
    }

    e.selector = this.getChainedSelector(relativeCommand = ":parent")

    lastElement = e

    return lastElement
}

internal fun TestElement.ancestor(
    selector: Selector,
    safeElementOnly: Boolean
): TestElement {

    var e = TestElement()
    val context = TestDriverCommandContext(this)
    context.execRelativeCommand(subject = subject, arg1 = selector.nickname) {
        val filtered = this.ancestors.reversed()
            .filterBySelector(selector = selector, throwsException = false, safeElementOnly = safeElementOnly)
        e = filtered.firstOrNull() ?: TestElement.emptyElement
    }

    e.selector = this.getChainedSelector(relativeCommand = "$selector")
    TestDriver.lastElement = e

    return TestDriver.lastElement
}

/**
 * ancestor
 */
fun TestElement.ancestor(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(":ancestor($expression)", safeElementOnly = safeElementOnly)
}

/**
 * ancestor
 */
fun TestElement.ancestor(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":ancestor($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = this.ancestors
    )
}

internal fun TestElement.descendant(
    selector: Selector,
    safeElementOnly: Boolean
): TestElement {

    var e = TestElement()
    val context = TestDriverCommandContext(this)
    context.execRelativeCommand(subject = subject, arg1 = selector.nickname) {
        val filtered = this.descendants.filterBySelector(
            selector = selector,
            throwsException = false,
            safeElementOnly = safeElementOnly
        )
        e = filtered.firstOrNull() ?: TestElement.emptyElement
    }

    e.selector = this.getChainedSelector(relativeCommand = "$selector")
    TestDriver.lastElement = e

    return TestDriver.lastElement
}

/**
 * descendant
 */
fun TestElement.descendant(
    expression: String,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":descendant($expression)",
        safeElementOnly = safeElementOnly,
        scopeElements = this.descendants
    )
}

/**
 * descendant
 */
fun TestElement.descendant(
    pos: Int = 1,
    safeElementOnly: Boolean = true
): TestElement {

    return relative(
        command = ":descendant($pos)",
        safeElementOnly = safeElementOnly,
        scopeElements = descendants
    )
}

/**
 * not
 */
fun TestElement.not(): TestElement {

    return if (this.isEmpty) TestElement.dummyElement
    else TestElement.emptyElement
}

