package shirates.core.driver.commandextension

import org.openqa.selenium.By
import shirates.core.configuration.Selector
import shirates.core.configuration.removeRedundantExpression
import shirates.core.driver.*
import shirates.core.logging.Measure

/**
 * relative
 */
fun TestElement.relative(
    command: String,
    scopeElements: List<TestElement> = rootElement.elements,
): TestElement {

    val ms = Measure("relative command=$command")
    try {
        val selectors = TestDriver.screenInfo.selectors
        val exp =
            if (selectors.containsKey(command)) selectors[command]?.expression
            else null
        val expression = exp ?: command.removeRedundantExpression()
        val relativeSelector = Selector(expression)

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
    } finally {
        ms.end()
    }
}

internal fun TestElement.relative(
    relativeSelector: Selector,
    newSelector: Selector = this.getChainedSelector(relativeSelector = relativeSelector),
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
                    targetElements = scopeElements ?: widgets
                )
            }

            ":rightInput" -> {
                e = this.rightInput(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: inputWidgets
                )
            }

            ":rightLabel" -> {
                e = this.rightLabel(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: labelWidgets
                )
            }

            ":rightImage" -> {
                e = this.rightImage(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: imageWidgets
                )
            }

            ":rightButton" -> {
                e = this.rightButton(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: buttonWidgets
                )
            }

            ":rightSwitch" -> {
                e = this.rightSwitch(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: switchWidgets
                )
            }

            ":below" -> {
                e = this.below(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: widgets
                )
            }

            ":belowInput" -> {
                e = this.belowInput(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: inputWidgets
                )
            }

            ":belowLabel" -> {
                e = this.belowLabel(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: labelWidgets
                )
            }

            ":belowImage" -> {
                e = this.belowImage(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: imageWidgets
                )
            }

            ":belowButton" -> {
                e = this.belowButton(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: buttonWidgets
                )
            }

            ":belowSwitch" -> {
                e = this.belowSwitch(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: switchWidgets
                )
            }

            ":left" -> {
                e = this.left(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: widgets
                )
            }

            ":leftInput" -> {
                e = this.leftInput(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: inputWidgets
                )
            }

            ":leftLabel" -> {
                e = this.leftLabel(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: labelWidgets
                )
            }

            ":leftImage" -> {
                e = this.leftImage(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: imageWidgets
                )
            }

            ":leftButton" -> {
                e = this.leftButton(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: buttonWidgets
                )
            }

            ":leftSwitch" -> {
                e = this.leftSwitch(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: switchWidgets
                )
            }

            ":above" -> {
                e = this.above(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: widgets
                )
            }

            ":aboveInput" -> {
                e = this.aboveInput(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: inputWidgets
                )
            }

            ":aboveLabel" -> {
                e = this.aboveLabel(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: labelWidgets
                )
            }

            ":aboveImage" -> {
                e = this.aboveImage(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: imageWidgets
                )
            }

            ":aboveButton" -> {
                e = this.aboveButton(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: buttonWidgets
                )
            }

            ":aboveSwitch" -> {
                e = this.aboveSwitch(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: switchWidgets
                )
            }

            /**
             * Widget flow based
             */
            ":flow" -> {
                e = this.flow(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: widgets
                )
            }

            ":vflow" -> {
                e = this.vflow(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: widgets
                )
            }

            ":flowLabel", ":label" -> {
                e = this.flowLabel(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: labelWidgets
                )
            }

            ":flowInput", ":input" -> {
                e = this.flowInput(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: inputWidgets
                )
            }

            ":flowImage", ":image" -> {
                e = this.flowImage(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: imageWidgets
                )
            }

            ":flowButton", ":button" -> {
                e = this.flowButton(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: buttonWidgets
                )
            }

            ":flowSwitch", ":switch" -> {
                e = this.flowSwitch(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: switchWidgets
                )
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
                e = this.next(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: widgets
                )
            }

            ":previous" -> {
                e = this.previous(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: widgets
                )
            }

            ":nextInput" -> {
                e = this.nextInput(
                    relativeSelector,
                    targetElements = scopeElements ?: innerWidgets
                )
            }

            ":preInput" -> {
                e = this.preInput(
                    relativeSelector,
                    targetElements = scopeElements ?: inputWidgets
                )
            }

            ":nextLabel" -> {
                e = this.nextLabel(
                    relativeSelector,
                    targetElements = scopeElements ?: labelWidgets
                )
            }

            ":preLabel" -> {
                e = this.preLabel(
                    relativeSelector,
                    targetElements = scopeElements ?: labelWidgets
                )
            }

            ":nextImage" -> {
                e = this.nextImage(
                    relativeSelector,
                    targetElements = scopeElements ?: imageWidgets
                )
            }

            ":preImage" -> {
                e = this.preImage(
                    relativeSelector,
                    targetElements = scopeElements ?: imageWidgets
                )
            }

            ":nextButton" -> {
                e = this.nextButton(
                    relativeSelector,
                    targetElements = scopeElements ?: buttonWidgets
                )
            }

            ":preButton" -> {
                e = this.preButton(
                    relativeSelector,
                    targetElements = scopeElements ?: buttonWidgets
                )
            }

            ":nextSwitch" -> {
                e = this.nextSwitch(
                    relativeSelector,
                    targetElements = scopeElements ?: switchWidgets
                )
            }

            ":preSwitch" -> {
                e = this.preSwitch(
                    relativeSelector,
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
    scopeElements: List<TestElement> = rootElement.elements
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
    expression: String
): TestElement {

    return relative(
        command = ":child($expression)",
        scopeElements = children
    )
}

/**
 * child
 */
fun TestElement.child(
    pos: Int = 1
): TestElement {

    return relative(
        command = ":child($pos)",
        scopeElements = children
    )
}

internal fun TestElement.sibling(
    selector: Selector
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

    return relative(
        command = ":sibling($expression)",
        scopeElements = parent().children
    )
}

/**
 * sibling
 */
fun TestElement.sibling(
    pos: Int = 1
): TestElement {

    return relative(
        command = ":sibling($pos)",
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
    selector: Selector
): TestElement {

    var e = TestElement()
    val context = TestDriverCommandContext(this)
    context.execRelativeCommand(subject = subject, arg1 = selector.nickname) {
        val filtered = this.ancestors.reversed()
            .filterBySelector(selector = selector, throwsException = false)
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
    pos: Int = 1
): TestElement {

    return relative(
        command = ":ancestor($pos)",
        scopeElements = this.ancestors
    )
}

internal fun TestElement.descendant(
    selector: Selector
): TestElement {

    var e = TestElement()
    val context = TestDriverCommandContext(this)
    context.execRelativeCommand(subject = subject, arg1 = selector.nickname) {
        val filtered = this.descendants.filterBySelector(
            selector = selector,
            throwsException = false
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
    expression: String
): TestElement {

    return relative(
        command = ":descendant($expression)",
        scopeElements = this.descendants
    )
}

/**
 * descendant
 */
fun TestElement.descendant(
    pos: Int = 1
): TestElement {

    return relative(
        command = ":descendant($pos)",
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

