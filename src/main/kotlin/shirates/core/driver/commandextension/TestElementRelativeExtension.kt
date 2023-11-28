package shirates.core.driver.commandextension

import org.openqa.selenium.By
import shirates.core.configuration.Selector
import shirates.core.configuration.removeRedundantExpression
import shirates.core.driver.*
import shirates.core.logging.Measure
import shirates.core.logging.Message

/**
 * relative
 */
fun TestElement.relative(
    command: String,
    scopeElements: List<TestElement> = rootElement.elements,
    frame: Bounds? = null
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
            scopeElements = scopeElements,
            frame = frame
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

private val commandsAllowedInDirectAccessMode = listOf(
    ":child",
    ":sibling",
    ":parent",
    ":ancestor",
    ":descendant",
    ":next",
    ":previous",
    ":nextInput",
    ":preInput",
    ":nextLabel",
    ":preLabel",
    ":nextImage",
    ":preImage",
    ":nextButton",
    ":preButton",
    ":nextSwitch",
    ":preSwitch",
    ":not"
)

internal fun TestElement.relative(
    relativeSelector: Selector,
    newSelector: Selector = this.getChainedSelector(relativeSelector = relativeSelector),
    scopeElements: List<TestElement>? = null,
    frame: Bounds? = null
): TestElement {

    if (testContext.useCache.not()) {
        if (commandsAllowedInDirectAccessMode.contains(relativeSelector.command).not()) {
            Message.message(
                id = "relativeSelectorNotSupportedInDirectAccessMode",
                arg1 = relativeSelector.toString(),
                arg2 = relativeSelector.expression
            )
        }
    }

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
                    frame = frame
                )
            }

            ":rightInput" -> {
                e = this.rightInput(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: inputWidgets,
                    frame = frame
                )
            }

            ":rightLabel" -> {
                e = this.rightLabel(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: labelWidgets,
                    frame = frame
                )
            }

            ":rightImage" -> {
                e = this.rightImage(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: imageWidgets,
                    frame = frame
                )
            }

            ":rightButton" -> {
                e = this.rightButton(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: buttonWidgets,
                    frame = frame
                )
            }

            ":rightSwitch" -> {
                e = this.rightSwitch(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: switchWidgets,
                    frame = frame
                )
            }

            ":below" -> {
                e = this.below(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: widgets,
                    frame = frame
                )
            }

            ":belowInput" -> {
                e = this.belowInput(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: inputWidgets,
                    frame = frame
                )
            }

            ":belowLabel" -> {
                e = this.belowLabel(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: labelWidgets,
                    frame = frame
                )
            }

            ":belowImage" -> {
                e = this.belowImage(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: imageWidgets,
                    frame = frame
                )
            }

            ":belowButton" -> {
                e = this.belowButton(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: buttonWidgets,
                    frame = frame
                )
            }

            ":belowSwitch" -> {
                e = this.belowSwitch(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: switchWidgets,
                    frame = frame
                )
            }

            ":left" -> {
                e = this.left(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: widgets,
                    frame = frame
                )
            }

            ":leftInput" -> {
                e = this.leftInput(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: inputWidgets,
                    frame = frame
                )
            }

            ":leftLabel" -> {
                e = this.leftLabel(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: labelWidgets,
                    frame = frame
                )
            }

            ":leftImage" -> {
                e = this.leftImage(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: imageWidgets,
                    frame = frame
                )
            }

            ":leftButton" -> {
                e = this.leftButton(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: buttonWidgets,
                    frame = frame
                )
            }

            ":leftSwitch" -> {
                e = this.leftSwitch(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: switchWidgets,
                    frame = frame
                )
            }

            ":above" -> {
                e = this.above(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: widgets,
                    frame = frame
                )
            }

            ":aboveInput" -> {
                e = this.aboveInput(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: inputWidgets,
                    frame = frame
                )
            }

            ":aboveLabel" -> {
                e = this.aboveLabel(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: labelWidgets,
                    frame = frame
                )
            }

            ":aboveImage" -> {
                e = this.aboveImage(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: imageWidgets,
                    frame = frame
                )
            }

            ":aboveButton" -> {
                e = this.aboveButton(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: buttonWidgets,
                    frame = frame
                )
            }

            ":aboveSwitch" -> {
                e = this.aboveSwitch(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: switchWidgets,
                    frame = frame
                )
            }

            /**
             * Widget flow based
             */
            ":flow" -> {
                e = this.flow(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: widgets,
                    frame = frame
                )
            }

            ":vflow" -> {
                e = this.vflow(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: widgets,
                    frame = frame
                )
            }

            ":flowLabel", ":label" -> {
                e = this.flowLabel(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: labelWidgets,
                    frame = frame
                )
            }

            ":flowInput", ":input" -> {
                e = this.flowInput(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: inputWidgets,
                    frame = frame
                )
            }

            ":flowImage", ":image" -> {
                e = this.flowImage(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: imageWidgets,
                    frame = frame
                )
            }

            ":flowButton", ":button" -> {
                e = this.flowButton(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: buttonWidgets,
                    frame = frame
                )
            }

            ":flowSwitch", ":switch" -> {
                e = this.flowSwitch(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: switchWidgets,
                    frame = frame
                )
            }

            ":innerWidget", ":inner" -> {
                val sel = relativeSelector.copy()
                sel.command = null
                e = this.innerWidget(selector = sel, frame = frame)
            }

            ":innerLabel" -> {
                val sel = relativeSelector.copy()
                sel.command = null
                e = this.innerLabel(selector = sel, frame = frame)
            }

            ":innerInput" -> {
                val sel = relativeSelector.copy()
                sel.command = null
                e = this.innerInput(selector = sel, frame = frame)
            }

            ":innerImage" -> {
                val sel = relativeSelector.copy()
                sel.command = null
                e = this.innerImage(selector = sel, frame = frame)
            }

            ":innerButton" -> {
                val sel = relativeSelector.copy()
                sel.command = null
                e = this.innerButton(selector = sel, frame = frame)
            }

            ":innerSwitch" -> {
                val sel = relativeSelector.copy()
                sel.command = null
                e = this.innerSwitch(selector = sel, frame = frame)
            }

            ":innerVWidget", ":innerV" -> {
                val sel = relativeSelector.copy()
                sel.command = null
                e = this.innerVWidget(selector = sel, frame = frame)
            }

            ":innerVlabel" -> {
                val sel = relativeSelector.copy()
                sel.command = null
                e = this.innerVlabel(selector = sel, frame = frame)
            }

            ":innerVinput" -> {
                val sel = relativeSelector.copy()
                sel.command = null
                e = this.innerVinput(selector = sel, frame = frame)
            }

            ":innerVimage" -> {
                val sel = relativeSelector.copy()
                sel.command = null
                e = this.innerVimage(selector = sel, frame = frame)
            }

            ":innerVbutton" -> {
                val sel = relativeSelector.copy()
                sel.command = null
                e = this.innerVbutton(selector = sel, frame = frame)
            }

            ":innerVswitch" -> {
                val sel = relativeSelector.copy()
                sel.command = null
                e = this.innerVswitch(selector = sel, frame = frame)
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
                    targetElements = scopeElements ?: widgets,
                    frame = frame
                )
            }

            ":previous" -> {
                e = this.previous(
                    selector = relativeSelector,
                    targetElements = scopeElements ?: widgets,
                    frame = frame
                )
            }

            ":nextInput" -> {
                e = this.nextInput(
                    relativeSelector,
                    targetElements = scopeElements ?: innerWidgets,
                    frame = frame
                )
            }

            ":preInput" -> {
                e = this.preInput(
                    relativeSelector,
                    targetElements = scopeElements ?: inputWidgets,
                    frame = frame
                )
            }

            ":nextLabel" -> {
                e = this.nextLabel(
                    relativeSelector,
                    targetElements = scopeElements ?: labelWidgets,
                    frame = frame
                )
            }

            ":preLabel" -> {
                e = this.preLabel(
                    relativeSelector,
                    targetElements = scopeElements ?: labelWidgets,
                    frame = frame
                )
            }

            ":nextImage" -> {
                e = this.nextImage(
                    relativeSelector,
                    targetElements = scopeElements ?: imageWidgets,
                    frame = frame
                )
            }

            ":preImage" -> {
                e = this.preImage(
                    relativeSelector,
                    targetElements = scopeElements ?: imageWidgets,
                    frame = frame
                )
            }

            ":nextButton" -> {
                e = this.nextButton(
                    relativeSelector,
                    targetElements = scopeElements ?: buttonWidgets,
                    frame = frame
                )
            }

            ":preButton" -> {
                e = this.preButton(
                    relativeSelector,
                    targetElements = scopeElements ?: buttonWidgets,
                    frame = frame
                )
            }

            ":nextSwitch" -> {
                e = this.nextSwitch(
                    relativeSelector,
                    targetElements = scopeElements ?: switchWidgets,
                    frame = frame
                )
            }

            ":preSwitch" -> {
                e = this.preSwitch(
                    relativeSelector,
                    targetElements = scopeElements ?: switchWidgets,
                    frame = frame
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
    scopeElements: List<TestElement> = rootElement.elements,
    frame: Bounds? = null
): TestElement {

    var e = this
    for (selector in relativeSelectors) {
        e = e.relative(
            relativeSelector = selector,
            scopeElements = scopeElements,
            frame = frame
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
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val e = relative(
        command = ":child($expression)",
        scopeElements = children
    )

    lastElement = e

    if (func != null) {
        func(e)
    }

    return lastElement
}

/**
 * child
 */
fun TestElement.child(
    pos: Int = 1,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val e = relative(
        command = ":child($pos)",
        scopeElements = children
    )

    lastElement = e

    if (func != null) {
        func(e)
    }

    return lastElement
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
    pos: Int = 1,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val e = relative(
        command = ":sibling($pos)",
        scopeElements = parent().children
    )

    lastElement = e

    if (func != null) {
        func(e)
    }

    return lastElement
}

/**
 * parent
 */
fun TestElement.parent(
    func: (TestElement.() -> Unit)? = null
): TestElement {

    var e = TestElement()
    val context = TestDriverCommandContext(this)
    context.execRelativeCommand(subject = subject) {

        if (testContext.useCache) {
            e = parentElement
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

    if (func != null) {
        func(e)
    }

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
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val e = relative(":ancestor($expression)")

    lastElement = e

    if (func != null) {
        func(e)
    }

    return lastElement
}

/**
 * ancestor
 */
fun TestElement.ancestor(
    pos: Int = 1,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val e = relative(
        command = ":ancestor($pos)",
        scopeElements = this.ancestors
    )

    lastElement = e

    if (func != null) {
        func(e)
    }

    return lastElement
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
    expression: String,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val e = relative(
        command = ":descendant($expression)",
        scopeElements = this.descendants
    )

    lastElement = e

    if (func != null) {
        func(e)
    }

    return lastElement
}

/**
 * descendant
 */
fun TestElement.descendant(
    pos: Int = 1,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val e = relative(
        command = ":descendant($pos)",
        scopeElements = descendants
    )

    lastElement = e

    if (func != null) {
        func(e)
    }

    return lastElement
}

/**
 * not
 */
fun TestElement.not(): TestElement {

    return if (this.isEmpty) TestElement.dummyElement
    else TestElement.emptyElement
}

