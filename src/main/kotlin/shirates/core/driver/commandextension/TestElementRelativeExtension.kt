package shirates.core.driver.commandextension

import shirates.core.configuration.Selector
import shirates.core.configuration.removeRedundantExpression
import shirates.core.driver.*

/**
 * relative
 */
fun TestElement.relative(
    command: String,
    scopeElements: List<TestElement> = TestElementCache.allElements,
): TestElement {

    val c = command.removeRedundantExpression()
    val relativeSelector = Selector(c)

    val oldSelector = this.selector
    val newSelector = this.getChainedSelector(relativeSelector = relativeSelector)

    val e = relative(
        relativeSelector = relativeSelector,
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
                e = this.right(selector = relativeSelector, targetElements = targetElements)
            }

            ":rightInput" -> {
                e = this.rightInput(selector = relativeSelector, targetElements = targetElements)
            }

            ":rightLabel" -> {
                e = this.rightLabel(selector = relativeSelector, targetElements = targetElements)
            }

            ":rightImage" -> {
                e = this.rightImage(selector = relativeSelector, targetElements = targetElements)
            }

            ":rightButton" -> {
                e = this.rightButton(selector = relativeSelector, targetElements = targetElements)
            }

            ":rightSwitch" -> {
                e = this.rightSwitch(selector = relativeSelector, targetElements = targetElements)
            }

            ":below" -> {
                e = this.below(selector = relativeSelector, targetElements = targetElements)
            }

            ":belowInput" -> {
                e = this.belowInput(selector = relativeSelector, targetElements = targetElements)
            }

            ":belowLabel" -> {
                e = this.belowLabel(selector = relativeSelector, targetElements = targetElements)
            }

            ":belowImage" -> {
                e = this.belowImage(selector = relativeSelector, targetElements = targetElements)
            }

            ":belowButton" -> {
                e = this.belowButton(selector = relativeSelector, targetElements = targetElements)
            }

            ":belowSwitch" -> {
                e = this.belowSwitch(selector = relativeSelector, targetElements = targetElements)
            }

            ":left" -> {
                e = this.left(selector = relativeSelector, targetElements = targetElements)
            }

            ":leftInput" -> {
                e = this.leftInput(selector = relativeSelector, targetElements = targetElements)
            }

            ":leftLabel" -> {
                e = this.leftLabel(selector = relativeSelector, targetElements = targetElements)
            }

            ":leftImage" -> {
                e = this.leftImage(selector = relativeSelector, targetElements = targetElements)
            }

            ":leftButton" -> {
                e = this.leftButton(selector = relativeSelector, targetElements = targetElements)
            }

            ":leftSwitch" -> {
                e = this.leftSwitch(selector = relativeSelector, targetElements = targetElements)
            }

            ":above" -> {
                e = this.above(selector = relativeSelector, targetElements = targetElements)
            }

            ":aboveInput" -> {
                e = this.aboveInput(selector = relativeSelector, targetElements = targetElements)
            }

            ":aboveLabel" -> {
                e = this.aboveLabel(selector = relativeSelector, targetElements = targetElements)
            }

            ":aboveImage" -> {
                e = this.aboveImage(selector = relativeSelector, targetElements = targetElements)
            }

            ":aboveButton" -> {
                e = this.aboveButton(selector = relativeSelector, targetElements = targetElements)
            }

            ":aboveSwitch" -> {
                e = this.aboveSwitch(selector = relativeSelector, targetElements = targetElements)
            }

            /**
             * Widget flow based
             */
            ":flow" -> {
                e = this.flow(selector = relativeSelector, targetElements = targetElements)
            }

            ":vflow" -> {
                e = this.vflow(selector = relativeSelector, targetElements = targetElements)
            }

            ":flowLabel", ":label" -> {
                e = this.flowLabel(selector = relativeSelector, targetElements = targetElements)
            }

            ":flowInput", ":input" -> {
                e = this.flowInput(selector = relativeSelector, targetElements = targetElements)
            }

            ":flowImage", ":image" -> {
                e = this.flowImage(selector = relativeSelector, targetElements = targetElements)
            }

            ":flowButton", ":button" -> {
                e = this.flowButton(selector = relativeSelector, targetElements = targetElements)
            }

            ":flowSwitch", ":switch" -> {
                e = this.flowSwitch(selector = relativeSelector, targetElements = targetElements)
            }

            ":innerFlow", ":inner" -> {
                e = this.innerFlow(selector = relativeSelector)
            }

            ":innerLabel" -> {
                e = this.innerLabel(selector = relativeSelector)
            }

            ":innerInput" -> {
                e = this.innerInput(selector = relativeSelector)
            }

            ":innerImage" -> {
                e = this.innerImage(selector = relativeSelector)
            }

            ":innerButton" -> {
                e = this.innerButton(selector = relativeSelector)
            }

            ":innerSwitch" -> {
                e = this.innerSwitch(selector = relativeSelector)
            }

            ":innerVflow", ":innerV" -> {
                e = this.innerVflow(selector = relativeSelector)
            }

            ":innerVlabel" -> {
                e = this.innerVlabel(selector = relativeSelector)
            }

            ":innerVinput" -> {
                e = this.innerVinput(selector = relativeSelector)
            }

            ":innerVimage" -> {
                e = this.innerVimage(selector = relativeSelector)
            }

            ":innerVbutton" -> {
                e = this.innerVbutton(selector = relativeSelector)
            }

            ":innerVswitch" -> {
                e = this.innerVswitch(selector = relativeSelector)
            }

            /**
             * XML based
             */
            ":child" -> {
                e = this.child(selector = relativeSelector)
            }

            ":sibling" -> {
                e = this.sibling(selector = relativeSelector)
            }

            ":parent" -> {
                e = this.parent()
            }

            ":ancestor" -> {
                e = this.ancestor(selector = relativeSelector)
            }

            ":descendant" -> {
                e = this.descendant(selector = relativeSelector)
            }

            ":next" -> {
                e = this.next(selector = relativeSelector, targetElements = targetElements)
            }

            ":previous" -> {
                e = this.previous(selector = relativeSelector, targetElements = targetElements)
            }

            ":nextInput" -> {
                e = this.nextInput(relativeSelector, targetElements = targetElements)
            }

            ":preInput" -> {
                e = this.preInput(relativeSelector, targetElements = targetElements)
            }

            ":nextLabel" -> {
                e = this.nextLabel(relativeSelector, targetElements = targetElements)
            }

            ":preLabel" -> {
                e = this.preLabel(relativeSelector, targetElements = targetElements)
            }

            ":nextImage" -> {
                e = this.nextImage(relativeSelector, targetElements = targetElements)
            }

            ":preImage" -> {
                e = this.preImage(relativeSelector, targetElements = targetElements)
            }

            ":nextButton" -> {
                e = this.nextButton(relativeSelector, targetElements = targetElements)
            }

            ":preButton" -> {
                e = this.preButton(relativeSelector, targetElements = targetElements)
            }

            ":nextSwitch" -> {
                e = this.nextSwitch(relativeSelector, targetElements = targetElements)
            }

            ":preSwitch" -> {
                e = this.preSwitch(relativeSelector, targetElements = targetElements)
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
    scopeElements: List<TestElement> = TestElementCache.allElements
): TestElement {

    var e = this
    for (selector in relativeSelectors) {
        e = e.relative(
            relativeSelector = selector,
            scopeElements = scopeElements
        )
    }

    return e
}

internal fun TestElement.child(
    selector: Selector
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
        val target = children.filterBySelector(selector = selector)
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
): TestElement {

    return relative(":child($expression)")
}

/**
 * child
 */
fun TestElement.child(
    pos: Int = 1,
): TestElement {

    return relative(":child($pos)")
}

internal fun TestElement.sibling(
    selector: Selector
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
        val targets = siblings.filterBySelector(selector = selector)
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
): TestElement {

    return relative(":sibling($expression)")
}

/**
 * sibling
 */
fun TestElement.sibling(
    pos: Int = 1,
): TestElement {

    return relative(":sibling($pos)")
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
    selector: Selector
): TestElement {

    var e = TestElement()
    val context = TestDriverCommandContext(this)
    context.execRelativeCommand(subject = subject, arg1 = selector.nickname) {
        val filtered = this.ancestors.reversed().filterBySelector(selector = selector, throwsException = false)
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
): TestElement {

    return relative(":ancestor($expression)")
}

/**
 * ancestor
 */
fun TestElement.ancestor(
    pos: Int = 1,
): TestElement {

    return relative(":ancestor($pos)")
}

internal fun TestElement.descendant(
    selector: Selector
): TestElement {

    var e = TestElement()
    val context = TestDriverCommandContext(this)
    context.execRelativeCommand(subject = subject, arg1 = selector.nickname) {
        val filtered = this.descendants.filterBySelector(selector = selector, throwsException = false)
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
): TestElement {

    return relative(":descendant($expression)")
}

/**
 * descendant
 */
fun TestElement.descendant(
    pos: Int = 1,
): TestElement {

    return relative(":descendant($pos)")
}

/**
 * not
 */
fun TestElement.not(): TestElement {

    return if (this.isEmpty) TestElement.dummyElement
    else TestElement.emptyElement
}

