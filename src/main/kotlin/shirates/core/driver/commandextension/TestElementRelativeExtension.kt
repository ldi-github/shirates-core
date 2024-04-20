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
    widgetOnly: Boolean? = null,
    frame: Bounds? = null
): TestElement {

    val ms = Measure("relative command=$command")
    try {
        val selectorMap = TestDriver.screenInfo.selectorMap
        val exp =
            if (selectorMap.containsKey(command)) selectorMap[command]?.expression
            else null
        val expression = exp ?: command.removeRedundantExpression()
        val relativeSelector = Selector(expression)

        val oldSelector = this.selector?.copy()
        val newSelector = this.selector?.getChainedSelector(relativeSelector = relativeSelector)

        val e = relative(
            relativeSelector = relativeSelector,
            scopeElements = scopeElements,
            widgetOnly = widgetOnly ?: getWidgetOnly(selector = relativeSelector),
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
    scopeElements: List<TestElement> = allElements(),
    widgetOnly: Boolean,
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

    val oldSelector = this.selector?.copy()
    val newSelector = this.selector?.getChainedSelector(relativeSelector = relativeSelector)

    var targetElements = scopeElements
    if (widgetOnly && relativeSelector.isXmlBased.not()) {
        targetElements = targetElements.filter { it.isWidget }
    }

    val context = TestDriverCommandContext(this)
    var e = TestElement(selector = this.selector)
    context.execRelativeCommand("${this.subject}:${relativeSelector}", subject = this.subject) {

        when (relativeSelector.command) {

            /**
             * direction based
             */
            ":right" -> {
                e = this.right(
                    selector = relativeSelector,
                    targetElements = targetElements,
                    widgetOnly = widgetOnly,
                    frame = frame
                )
            }

            ":rightInput" -> {
                e = this.rightInput(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isInput },
                    frame = frame
                )
            }

            ":rightLabel" -> {
                e = this.rightLabel(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isLabel },
                    frame = frame
                )
            }

            ":rightImage" -> {
                e = this.rightImage(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isImage },
                    frame = frame
                )
            }

            ":rightButton" -> {
                e = this.rightButton(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isButton },
                    frame = frame
                )
            }

            ":rightSwitch" -> {
                e = this.rightSwitch(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isSwitch },
                    frame = frame
                )
            }

            ":below" -> {
                e = this.below(
                    selector = relativeSelector,
                    targetElements = targetElements,
                    widgetOnly = widgetOnly,
                    frame = frame
                )
            }

            ":belowInput" -> {
                e = this.belowInput(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isInput },
                    frame = frame
                )
            }

            ":belowLabel" -> {
                e = this.belowLabel(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isLabel },
                    frame = frame
                )
            }

            ":belowImage" -> {
                e = this.belowImage(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isImage },
                    frame = frame
                )
            }

            ":belowButton" -> {
                e = this.belowButton(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isButton },
                    frame = frame
                )
            }

            ":belowSwitch" -> {
                e = this.belowSwitch(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isSwitch },
                    frame = frame
                )
            }

            ":belowScrollable" -> {
                e = this.belowScrollable(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isScrollable },
                    frame = frame
                )
            }

            ":left" -> {
                e = this.left(
                    selector = relativeSelector,
                    targetElements = targetElements,
                    widgetOnly = widgetOnly,
                    frame = frame
                )
            }

            ":leftInput" -> {
                e = this.leftInput(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isInput },
                    frame = frame
                )
            }

            ":leftLabel" -> {
                e = this.leftLabel(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isLabel },
                    frame = frame
                )
            }

            ":leftImage" -> {
                e = this.leftImage(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isImage },
                    frame = frame
                )
            }

            ":leftButton" -> {
                e = this.leftButton(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isButton },
                    frame = frame
                )
            }

            ":leftSwitch" -> {
                e = this.leftSwitch(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isSwitch },
                    frame = frame
                )
            }

            ":above" -> {
                e = this.above(
                    selector = relativeSelector,
                    targetElements = targetElements,
                    widgetOnly = widgetOnly,
                    frame = frame
                )
            }

            ":aboveInput" -> {
                e = this.aboveInput(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isInput },
                    frame = frame
                )
            }

            ":aboveLabel" -> {
                e = this.aboveLabel(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isLabel },
                    frame = frame
                )
            }

            ":aboveImage" -> {
                e = this.aboveImage(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isImage },
                    frame = frame
                )
            }

            ":aboveButton" -> {
                e = this.aboveButton(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isButton },
                    frame = frame
                )
            }

            ":aboveSwitch" -> {
                e = this.aboveSwitch(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isSwitch },
                    frame = frame
                )
            }

            ":aboveScrollable" -> {
                e = this.aboveScrollable(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isScrollable },
                    frame = frame
                )
            }

            /**
             * Widget flow based
             */
            ":flow" -> {
                e = this.flow(
                    selector = relativeSelector,
                    targetElements = targetElements,
                    frame = frame
                )
            }

            ":vflow" -> {
                e = this.vflow(
                    selector = relativeSelector,
                    targetElements = targetElements,
                    frame = frame
                )
            }

            ":flowLabel", ":label" -> {
                e = this.flowLabel(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isLabel },
                    frame = frame
                )
            }

            ":flowInput", ":input" -> {
                e = this.flowInput(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isInput },
                    frame = frame
                )
            }

            ":flowImage", ":image" -> {
                e = this.flowImage(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isImage },
                    frame = frame
                )
            }

            ":flowButton", ":button" -> {
                e = this.flowButton(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isButton },
                    frame = frame
                )
            }

            ":flowSwitch", ":switch" -> {
                e = this.flowSwitch(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isSwitch },
                    frame = frame
                )
            }

            ":flowScrollable", ":scrollable" -> {
                e = this.flowScrollable(
                    selector = relativeSelector,
                    targetElements = targetElements.filter { it.isScrollable },
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
                    targetElements = targetElements,
                    frame = frame
                )
            }

            ":previous" -> {
                e = this.previous(
                    selector = relativeSelector,
                    targetElements = targetElements,
                    frame = frame
                )
            }

            ":nextInput" -> {
                e = this.nextInput(
                    relativeSelector,
                    targetElements = targetElements,
                    frame = frame
                )
            }

            ":preInput" -> {
                e = this.preInput(
                    relativeSelector,
                    targetElements = targetElements,
                    frame = frame
                )
            }

            ":nextLabel" -> {
                e = this.nextLabel(
                    relativeSelector,
                    targetElements = targetElements,
                    frame = frame
                )
            }

            ":preLabel" -> {
                e = this.preLabel(
                    relativeSelector,
                    targetElements = targetElements,
                    frame = frame
                )
            }

            ":nextImage" -> {
                e = this.nextImage(
                    relativeSelector,
                    targetElements = targetElements,
                    frame = frame
                )
            }

            ":preImage" -> {
                e = this.preImage(
                    relativeSelector,
                    targetElements = targetElements,
                    frame = frame
                )
            }

            ":nextButton" -> {
                e = this.nextButton(
                    relativeSelector,
                    targetElements = targetElements,
                    frame = frame
                )
            }

            ":preButton" -> {
                e = this.preButton(
                    relativeSelector,
                    targetElements = targetElements,
                    frame = frame
                )
            }

            ":nextSwitch" -> {
                e = this.nextSwitch(
                    relativeSelector,
                    targetElements = targetElements,
                    frame = frame
                )
            }

            ":preSwitch" -> {
                e = this.preSwitch(
                    relativeSelector,
                    targetElements = targetElements,
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
    widgetOnly: Boolean = false,
    frame: Bounds? = null
): TestElement {

    var e = this
    for (selector in relativeSelectors) {
        e = e.relative(
            relativeSelector = selector,
            scopeElements = scopeElements,
            widgetOnly = widgetOnly,
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

