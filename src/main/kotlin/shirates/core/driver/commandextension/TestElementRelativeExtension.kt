package shirates.core.driver.commandextension

import org.openqa.selenium.By
import shirates.core.configuration.Selector
import shirates.core.configuration.removeRedundantExpression
import shirates.core.driver.*

/**
 * relative
 */
fun TestElement.relative(
    command: String,
    inViewOnly: Boolean = true,
    scopeElements: List<TestElement> = rootElement.elements,
): TestElement {

    val selectors = TestDriver.screenInfo.selectors
    val exp =
        if (selectors.containsKey(command)) selectors[command]?.expression
        else null
    val expression = exp ?: command.removeRedundantExpression()
    val relativeSelector = Selector(expression)

    val oldSelector = this.selector
    val newSelector = this.getChainedSelector(relativeSelector = relativeSelector)

    val scoped =
        if (inViewOnly)
            scopeElements.filter { it.isInView }
        else scopeElements

    val e = relative(
        relativeSelector = relativeSelector,
        inViewOnly = inViewOnly,
        scopeElements = scoped
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
    inViewOnly: Boolean,
    scopeElements: List<TestElement>? = null
): TestElement {

    val oldSelector = this.selector

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
                    targetElements = scopeElements ?: widgets,
                    inViewOnly = inViewOnly
                )
            }

            ":rightInput" -> {
                e = this.rightInput(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: inputWidgets
                )
            }

            ":rightLabel" -> {
                e = this.rightLabel(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: labelWidgets
                )
            }

            ":rightImage" -> {
                e = this.rightImage(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: imageWidgets
                )
            }

            ":rightButton" -> {
                e = this.rightButton(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: buttonWidgets
                )
            }

            ":rightSwitch" -> {
                e = this.rightSwitch(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: switchWidgets
                )
            }

            ":below" -> {
                e = this.below(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: widgets
                )
            }

            ":belowInput" -> {
                e = this.belowInput(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: inputWidgets
                )
            }

            ":belowLabel" -> {
                e = this.belowLabel(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: labelWidgets
                )
            }

            ":belowImage" -> {
                e = this.belowImage(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: imageWidgets
                )
            }

            ":belowButton" -> {
                e = this.belowButton(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: buttonWidgets
                )
            }

            ":belowSwitch" -> {
                e = this.belowSwitch(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: switchWidgets
                )
            }

            ":left" -> {
                e = this.left(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: widgets
                )
            }

            ":leftInput" -> {
                e = this.leftInput(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: inputWidgets
                )
            }

            ":leftLabel" -> {
                e = this.leftLabel(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: labelWidgets
                )
            }

            ":leftImage" -> {
                e = this.leftImage(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: imageWidgets
                )
            }

            ":leftButton" -> {
                e = this.leftButton(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: buttonWidgets
                )
            }

            ":leftSwitch" -> {
                e = this.leftSwitch(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: switchWidgets
                )
            }

            ":above" -> {
                e = this.above(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: widgets
                )
            }

            ":aboveInput" -> {
                e = this.aboveInput(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: inputWidgets
                )
            }

            ":aboveLabel" -> {
                e = this.aboveLabel(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: labelWidgets
                )
            }

            ":aboveImage" -> {
                e = this.aboveImage(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: imageWidgets
                )
            }

            ":aboveButton" -> {
                e = this.aboveButton(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: buttonWidgets
                )
            }

            ":aboveSwitch" -> {
                e = this.aboveSwitch(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: switchWidgets
                )
            }

            /**
             * Widget flow based
             */
            ":flow" -> {
                e = this.flow(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: widgets
                )
            }

            ":vflow" -> {
                e = this.vflow(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: widgets
                )
            }

            ":flowLabel", ":label" -> {
                e = this.flowLabel(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: labelWidgets
                )
            }

            ":flowInput", ":input" -> {
                e = this.flowInput(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: inputWidgets
                )
            }

            ":flowImage", ":image" -> {
                e = this.flowImage(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: imageWidgets
                )
            }

            ":flowButton", ":button" -> {
                e = this.flowButton(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: buttonWidgets
                )
            }

            ":flowSwitch", ":switch" -> {
                e = this.flowSwitch(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: switchWidgets
                )
            }

            ":innerFlow", ":inner" -> {
                e = this.innerFlow(selector = relativeSelector, inViewOnly = inViewOnly)
            }

            ":innerLabel" -> {
                e = this.innerLabel(selector = relativeSelector, inViewOnly = inViewOnly)
            }

            ":innerInput" -> {
                e = this.innerInput(selector = relativeSelector, inViewOnly = inViewOnly)
            }

            ":innerImage" -> {
                e = this.innerImage(selector = relativeSelector, inViewOnly = inViewOnly)
            }

            ":innerButton" -> {
                e = this.innerButton(selector = relativeSelector, inViewOnly = inViewOnly)
            }

            ":innerSwitch" -> {
                e = this.innerSwitch(selector = relativeSelector, inViewOnly = inViewOnly)
            }

            ":innerVflow", ":innerV" -> {
                e = this.innerVflow(selector = relativeSelector, inViewOnly = inViewOnly)
            }

            ":innerVlabel" -> {
                e = this.innerVlabel(selector = relativeSelector, inViewOnly = inViewOnly)
            }

            ":innerVinput" -> {
                e = this.innerVinput(selector = relativeSelector, inViewOnly = inViewOnly)
            }

            ":innerVimage" -> {
                e = this.innerVimage(selector = relativeSelector, inViewOnly = inViewOnly)
            }

            ":innerVbutton" -> {
                e = this.innerVbutton(selector = relativeSelector, inViewOnly = inViewOnly)
            }

            ":innerVswitch" -> {
                e = this.innerVswitch(selector = relativeSelector, inViewOnly = inViewOnly)
            }

            /**
             * XML based
             */
            ":child" -> {
                e = this.child(selector = relativeSelector, inViewOnly = inViewOnly)
            }

            ":sibling" -> {
                e = this.sibling(selector = relativeSelector, inViewOnly = inViewOnly)
            }

            ":parent" -> {
                e = this.parent()
            }

            ":ancestor" -> {
                e = this.ancestor(selector = relativeSelector, inViewOnly = inViewOnly)
            }

            ":descendant" -> {
                e = this.descendant(selector = relativeSelector, inViewOnly = inViewOnly)
            }

            ":next" -> {
                e = this.next(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: widgets
                )
            }

            ":previous" -> {
                e = this.previous(
                    selector = relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: widgets
                )
            }

            ":nextInput" -> {
                e = this.nextInput(
                    relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: innerWidgets
                )
            }

            ":preInput" -> {
                e = this.preInput(
                    relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: inputWidgets
                )
            }

            ":nextLabel" -> {
                e = this.nextLabel(
                    relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: labelWidgets
                )
            }

            ":preLabel" -> {
                e = this.preLabel(
                    relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: labelWidgets
                )
            }

            ":nextImage" -> {
                e = this.nextImage(
                    relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: imageWidgets
                )
            }

            ":preImage" -> {
                e = this.preImage(
                    relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: imageWidgets
                )
            }

            ":nextButton" -> {
                e = this.nextButton(
                    relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: buttonWidgets
                )
            }

            ":preButton" -> {
                e = this.preButton(
                    relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: buttonWidgets
                )
            }

            ":nextSwitch" -> {
                e = this.nextSwitch(
                    relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: switchWidgets
                )
            }

            ":preSwitch" -> {
                e = this.preSwitch(
                    relativeSelector,
                    inViewOnly = inViewOnly,
                    targetElements = scopeElements ?: switchWidgets
                )
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
    inViewOnly: Boolean = true,
    scopeElements: List<TestElement> = rootElement.elements
): TestElement {

    var e = this
    for (selector in relativeSelectors) {
        e = e.relative(
            relativeSelector = selector,
            inViewOnly = inViewOnly,
            scopeElements = scopeElements
        )
    }

    return e
}

internal fun TestElement.child(
    selector: Selector,
    inViewOnly: Boolean
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
        val target = children.filterBySelector(selector = selector, inViewOnly = inViewOnly)
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
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":child($expression)",
        inViewOnly = inViewOnly,
        scopeElements = children
    )
}

/**
 * child
 */
fun TestElement.child(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":child($pos)",
        inViewOnly = inViewOnly,
        scopeElements = children
    )
}

internal fun TestElement.sibling(
    selector: Selector,
    inViewOnly: Boolean
): TestElement {

    var e = TestElement(selector = selector)
    val context = TestDriverCommandContext(this)
    context.execRelativeCommand(subject = subject, arg1 = selector.nickname) {
        val te = this
        val p = te.parentElement
        if (p.isEmpty) {
            TestDriver.lastElement = TestElement.emptyElement
            return@execRelativeCommand
        }
        val siblings = this.siblings
        val targets = siblings.filterBySelector(selector = selector, inViewOnly = inViewOnly)
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
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":sibling($expression)",
        inViewOnly = inViewOnly,
        scopeElements = parent().children
    )
}

/**
 * sibling
 */
fun TestElement.sibling(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":sibling($pos)",
        inViewOnly = inViewOnly,
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

        if (testContext.useCache) {
            e = parentElement ?: TestElement.emptyElement
        } else {
            if (parentElement.webElement != null) {
                e = parentElement
            } else {
                val xpath = getUniqueXpath() + "/parent::*"
                e = testDrive.findWebElementBy(locator = By.xpath(xpath), timeoutMilliseconds = 0)
                parentElement = e
            }
        }
    }

    e.selector = this.getChainedSelector(relativeCommand = ":parent")

    lastElement = e

    return lastElement
}

internal fun TestElement.ancestor(
    selector: Selector,
    inViewOnly: Boolean
): TestElement {

    var e = TestElement()
    val context = TestDriverCommandContext(this)
    context.execRelativeCommand(subject = subject, arg1 = selector.nickname) {
        val filtered = this.ancestors.reversed()
            .filterBySelector(selector = selector, throwsException = false, inViewOnly = inViewOnly)
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
    inViewOnly: Boolean = true
): TestElement {

    return relative(":ancestor($expression)", inViewOnly = inViewOnly)
}

/**
 * ancestor
 */
fun TestElement.ancestor(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":ancestor($pos)",
        inViewOnly = inViewOnly,
        scopeElements = this.ancestors
    )
}

internal fun TestElement.descendant(
    selector: Selector,
    inViewOnly: Boolean
): TestElement {

    var e = TestElement()
    val context = TestDriverCommandContext(this)
    context.execRelativeCommand(subject = subject, arg1 = selector.nickname) {
        val filtered = this.descendants.filterBySelector(
            selector = selector,
            throwsException = false,
            inViewOnly = inViewOnly
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
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":descendant($expression)",
        inViewOnly = inViewOnly,
        scopeElements = this.descendants
    )
}

/**
 * descendant
 */
fun TestElement.descendant(
    pos: Int = 1,
    inViewOnly: Boolean = true
): TestElement {

    return relative(
        command = ":descendant($pos)",
        inViewOnly = inViewOnly,
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

