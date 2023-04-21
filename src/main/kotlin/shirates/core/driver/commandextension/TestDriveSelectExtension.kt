package shirates.core.driver.commandextension

import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.exception.TestDriverException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Message.message

/**
 * filterElements
 */
fun TestDrive.filterElements(
    expression: String,
    safeElementOnly: Boolean = true,
    useCache: Boolean = testContext.useCache
): List<TestElement> {

    if (useCache) {
        syncCache(force = true)
    }
    return TestElementCache.filterElements(expression = expression, safeElementOnly = safeElementOnly)
}

/**
 * filterElements
 */
fun TestDrive.filterElements(
    selector: Selector,
    safeElementOnly: Boolean = true,
    selectContext: TestElement = rootElement
): List<TestElement> {

    return TestElementCache.filterElements(
        selector = selector,
        safeElementOnly = safeElementOnly,
        selectContext = selectContext
    )
}

/**
 * select
 */
fun TestDrive.select(
    expression: String,
    throwsException: Boolean = true,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    useCache: Boolean = testContext.useCache,
    safeElementOnly: Boolean = true,
    log: Boolean = false,
    func: (TestElement.() -> Unit)? = null
): TestElement {
    val testElement = getTestElement()

    val sel = getSelector(expression = expression)
    var e = TestElement(selector = sel)
    val context = TestDriverCommandContext(testElement)
    context.execSelectCommand(selector = sel, subject = sel.toString(), log = log) {
        val scroll = CodeExecutionContext.withScrollDirection != null
        val direction = CodeExecutionContext.withScrollDirection ?: ScrollDirection.Down

        e = TestDriver.select(
            selector = sel,
            scroll = scroll,
            direction = direction,
            waitSeconds = waitSeconds,
            throwsException = throwsException,
            useCache = useCache,
            safeElementOnly = safeElementOnly
        )
    }
    if (func != null) {
        e.func()
    }
    if (TestMode.isNoLoadRun) {
        e.selector = sel
    }

    lastElement = e
    return lastElement
}

/**
 * selectWithScrollDown
 */
fun TestDrive.selectWithScrollDown(
    expression: String,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
    log: Boolean = false,
    func: (TestElement.() -> Unit)? = null
): TestElement {
    val testElement = getTestElement()

    val selector = getSelector(expression = expression)
    val context = TestDriverCommandContext(testElement)
    var e = TestElement(selector = selector)
    context.execSelectCommand(selector = selector, subject = selector.toString(), log = log) {
        e = TestDriver.selectWithScroll(
            selector = selector,
            direction = ScrollDirection.Down,
            durationSeconds = scrollDurationSeconds,
            startMarginRatio = scrollStartMarginRatio,
            scrollMaxCount = scrollMaxCount,
            throwsException = throwsException
        )
    }
    if (func != null) {
        e.func()
    }
    lastElement = e
    return lastElement
}

/**
 * selectWithScrollUp
 */
fun TestDrive.selectWithScrollUp(
    expression: String,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
    log: Boolean = false,
    func: (TestElement.() -> Unit)? = null
): TestElement {
    val testElement = getTestElement()

    val selector = getSelector(expression = expression)
    var e = TestElement(selector = selector)
    val context = TestDriverCommandContext(testElement)
    context.execSelectCommand(selector = selector, subject = selector.toString(), log = log) {
        e = TestDriver.selectWithScroll(
            selector = selector,
            direction = ScrollDirection.Up,
            durationSeconds = scrollDurationSeconds,
            startMarginRatio = scrollStartMarginRatio,
            scrollMaxCount = scrollMaxCount,
            throwsException = throwsException
        )
    }
    if (func != null) {
        e.func()
    }
    lastElement = e
    return lastElement
}

/**
 * selectWithScrollRight
 */
fun TestDrive.selectWithScrollRight(
    expression: String,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollHorizontalMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
    log: Boolean = false,
    func: (TestElement.() -> Unit)? = null
): TestElement {
    val testElement = getTestElement()

    val selector = getSelector(expression = expression)
    var e = TestElement(selector = selector)
    val context = TestDriverCommandContext(testElement)
    context.execSelectCommand(selector = selector, subject = selector.toString(), log = log) {
        e = TestDriver.selectWithScroll(
            selector = selector,
            direction = ScrollDirection.Right,
            durationSeconds = scrollDurationSeconds,
            startMarginRatio = scrollStartMarginRatio,
            scrollMaxCount = scrollMaxCount,
            throwsException = throwsException
        )
    }
    if (func != null) {
        e.func()
    }
    lastElement = e
    return lastElement
}

/**
 * selectWithScrollLeft
 */
fun TestDrive.selectWithScrollLeft(
    expression: String,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollHorizontalMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
    log: Boolean = false,
    func: (TestElement.() -> Unit)? = null
): TestElement {
    val testElement = getTestElement()

    val selector = getSelector(expression = expression)
    var e = TestElement(selector = selector)
    val context = TestDriverCommandContext(testElement)
    context.execSelectCommand(selector = selector, subject = selector.toString(), log = log) {
        e = TestDriver.selectWithScroll(
            selector = selector,
            direction = ScrollDirection.Left,
            durationSeconds = scrollDurationSeconds,
            startMarginRatio = scrollStartMarginRatio,
            scrollMaxCount = scrollMaxCount,
            throwsException = throwsException
        )
    }
    if (func != null) {
        e.func()
    }
    lastElement = e
    return lastElement
}

/**
 * selectInScanResults
 */
fun TestDrive.selectInScanResults(
    expression: String,
    throwsException: Boolean = true,
    log: Boolean = false,
    func: (TestElement.() -> Unit)? = null
): TestElement {
    val testElement = getTestElement()

    val sel = getSelector(expression = expression)
    var e = TestElement(selector = sel)
    val context = TestDriverCommandContext(testElement)
    context.execSelectCommand(selector = sel, subject = sel.toString(), log = log) {

        for (scanRoot in TestElementCache.scanResults) {
            e = TestElementCache.select(
                expression = expression,
                throwsException = false,
                selectContext = scanRoot.element,
                safeElementOnly = true
            )
            if (e.isEmpty.not()) {
                return@execSelectCommand
            }
        }

        val error = TestDriverException(message(id = "elementNotFoundInScanResults", subject = "$sel"))
        e.lastError = error
        if (throwsException) {
            throw error
        }
    }
    if (func != null) {
        e.func()
    }
    lastElement = e
    return lastElement
}

internal fun TestDrive.canSelect(
    selector: Selector,
    scroll: Boolean = false,
    direction: ScrollDirection = ScrollDirection.Down,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    waitSeconds: Double = 0.0,
    safeElementOnly: Boolean = true
): Boolean {

    val e = TestDriver.select(
        selector = selector,
        scroll = scroll,
        direction = direction,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollMaxCount = scrollMaxCount,
        waitSeconds = waitSeconds,
        throwsException = false,
        safeElementOnly = safeElementOnly
    )

    return e.isEmpty.not()
}

/**
 * canSelect
 */
fun TestDrive.canSelect(
    expression: String,
    scroll: Boolean = false,
    direction: ScrollDirection = ScrollDirection.Down,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    screenName: String = TestDriver.currentScreen,
    waitSeconds: Double = 0.0,
    safeElementOnly: Boolean = true,
    log: Boolean = false
): Boolean {
    val testElement = this.getTestElement()

    val sel = TestDriver.expandExpression(expression = expression, screenName = screenName)
    var found = false
    val context = TestDriverCommandContext(testElement)
    val logLine = context.execBooleanCommand(subject = sel.toString(), log = log) {
        found = canSelect(
            selector = sel,
            scroll = scroll,
            direction = direction,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollMaxCount = scrollMaxCount,
            waitSeconds = waitSeconds,
            safeElementOnly = safeElementOnly
        )
    }
    if (logLine != null) {
        logLine.message += " (result=$found)"
    }
    return found
}

/**
 * canSelectWithScrollDown
 */
fun TestDrive.canSelectWithScrollDown(
    expression: String,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {
    val testElement = this.getTestElement()

    val sel = getSelector(expression = expression)
    var found = false
    val context = TestDriverCommandContext(testElement)
    val logLine = context.execBooleanCommand(subject = sel.toString(), log = log) {
        found = canSelect(
            selector = sel,
            scroll = true,
            direction = ScrollDirection.Down,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollMaxCount = scrollMaxCount,
            safeElementOnly = true
        )
    }
    if (logLine != null) {
        logLine.message += " (result=$found)"
    }
    return found
}

/**
 * canSelectWithScrollUp
 */
fun TestDrive.canSelectWithScrollUp(
    expression: String,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {
    val testElement = this.getTestElement()

    val sel = getSelector(expression = expression)
    var found = false
    val context = TestDriverCommandContext(testElement)
    val logLine = context.execBooleanCommand(subject = sel.toString(), log = log) {
        found = canSelect(
            selector = sel,
            scroll = true,
            direction = ScrollDirection.Up,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollMaxCount = scrollMaxCount,
            safeElementOnly = true
        )
    }
    if (logLine != null) {
        logLine.message += " (result=$found)"
    }
    return found
}

/**
 * canSelectWithScrollRight
 */
fun TestDrive.canSelectWithScrollRight(
    expression: String,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollHorizontalMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {
    val testElement = this.getTestElement()

    val sel = getSelector(expression = expression)
    var found = false
    val context = TestDriverCommandContext(testElement)
    val logLine = context.execBooleanCommand(subject = sel.toString(), log = log) {
        found = canSelect(
            selector = sel,
            scroll = true,
            direction = ScrollDirection.Right,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollMaxCount = scrollMaxCount,
            safeElementOnly = true
        )
    }
    if (logLine != null) {
        logLine.message += " (result=$found)"
    }
    return found
}

/**
 * canSelectWithScrollLeft
 */
fun TestDrive.canSelectWithScrollLeft(
    expression: String,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollHorizontalMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {
    val testElement = this.getTestElement()

    val sel = getSelector(expression = expression)
    var found = false
    val context = TestDriverCommandContext(testElement)
    val logLine = context.execBooleanCommand(subject = sel.toString(), log = log) {
        found = canSelect(
            selector = sel,
            scroll = true,
            direction = ScrollDirection.Left,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollMaxCount = scrollMaxCount,
            safeElementOnly = true
        )
    }
    if (logLine != null) {
        logLine.message += " (result=$found)"
    }
    return found
}

/**
 * canSelectInScanResults
 */
fun TestDrive.canSelectInScanResults(
    expression: String,
    log: Boolean = false
): Boolean {
    val testElement = this.getTestElement()

    val subject = TestDriver.screenInfo.getSelector(expression = expression).toString()
    var found = false
    val context = TestDriverCommandContext(testElement)
    val logLine = context.execBooleanCommand(subject = subject, log = log) {
        val e = selectInScanResults(
            expression = expression,
            throwsException = false,
            log = log
        )
        found = e.isEmpty.not()
    }
    if (logLine != null) {
        logLine.message += " (result=$found)"
    }
    return found
}

/**
 * canSelectAllInScanResults
 */
fun TestDrive.canSelectAllInScanResults(
    vararg expressions: String,
    log: Boolean = false
): Boolean {
    val testElement = this.getTestElement()

    val subject = expressions.map { TestDriver.screenInfo.getSelector(expression = it) }.joinToString()
    var found = true
    val context = TestDriverCommandContext(testElement)
    val logLine = context.execBooleanCommand(subject = subject, log = log) {

        for (expression in expressions) {
            val r = canSelectInScanResults(expression = expression)
            if (r.not()) {
                found = false
                return@execBooleanCommand
            }
        }
    }
    if (logLine != null) {
        logLine.message += " (result=$found)"
    }
    return found
}


/**
 * canSelectAll
 */
internal fun TestDrive.canSelectAll(
    selectors: Iterable<Selector>,
    safeElementOnly: Boolean = true,
    log: Boolean = false
): Boolean {
    val testElement = this.getTestElement()

    val subject = selectors.joinToString()
    var foundAll = false
    val context = TestDriverCommandContext(testElement)
    val logLine = context.execBooleanCommand(subject = subject, log = log) {
        if (testContext.useCache) {
            foundAll = TestElementCache.canSelectAll(selectors = selectors, safeElementOnly = safeElementOnly)
        } else {
            foundAll = testDrive.canFindAllWebElement(selectors = selectors.toList().toTypedArray())
        }
    }
    if (logLine != null) {
        logLine.message += " (result=$foundAll)"
    }
    return foundAll
}

/**
 * canSelectAll
 */
fun TestDrive.canSelectAll(
    vararg expressions: String,
    log: Boolean = false
): Boolean {
    val testElement = this.getTestElement()

    val subject = expressions.map { TestDriver.screenInfo.getSelector(expression = it) }.joinToString()
    var foundAll = false
    val context = TestDriverCommandContext(testElement)
    val logLine = context.execBooleanCommand(subject = subject, log = log) {
        val selectors = mutableListOf<Selector>()
        for (expression in expressions) {
            val selector = TestDriver.screenInfo.getSelector(expression = expression)
            selectors.add(selector)
        }
        foundAll = canSelectAll(selectors = selectors, log = log)
    }
    if (logLine != null) {
        logLine.message += " (result=$foundAll)"
    }
    return foundAll
}

/**
 * canSelectAllWithScrollDown
 */
fun TestDrive.canSelectAllWithScrollDown(
    vararg expressions: String,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {
    val testElement = this.getTestElement()

    val subject = expressions.map { TestDriver.screenInfo.getSelector(expression = it) }.joinToString()
    var foundAll = false
    val context = TestDriverCommandContext(testElement)
    val logLine = context.execBooleanCommand(subject = subject, log = log) {
        for (expression in expressions) {
            val result = canSelectWithScrollDown(
                expression = expression,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollMaxCount = scrollMaxCount,
                log = log
            )
            if (result.not()) {
                foundAll = false
                return@execBooleanCommand
            }
        }

        foundAll = true
    }
    if (logLine != null) {
        logLine.message += " (result=$foundAll)"
    }
    return foundAll
}

/**
 * canSelectAllWithScrollUp
 */
fun TestDrive.canSelectAllWithScrollUp(
    vararg expressions: String,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {
    val testElement = getTestElement()

    val subject = expressions.map { TestDriver.screenInfo.getSelector(expression = it) }.joinToString()
    var found = false
    val context = TestDriverCommandContext(testElement)
    val logLine = context.execBooleanCommand(subject = subject, log = log) {
        for (expression in expressions) {
            val result = canSelectWithScrollUp(
                expression = expression,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollMaxCount = scrollMaxCount
            )
            if (result.not()) {
                found = false
                return@execBooleanCommand
            }
        }

        found = true
    }
    if (logLine != null) {
        logLine.message += " (result=$found)"
    }
    return found
}

/**
 * canSelectAllWithScrollRight
 */
fun TestDrive.canSelectAllWithScrollRight(
    vararg expressions: String,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollHorizontalMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {
    val testElement = getTestElement()

    val subject = expressions.map { TestDriver.screenInfo.getSelector(expression = it) }.joinToString()
    var found = false
    val context = TestDriverCommandContext(testElement)
    val logLine = context.execBooleanCommand(subject = subject, log = log) {
        for (expression in expressions) {
            val result = canSelectWithScrollRight(
                expression = expression,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollMaxCount = scrollMaxCount
            )
            if (result.not()) {
                found = false
                return@execBooleanCommand
            }
        }

        found = true
    }
    if (logLine != null) {
        logLine.message += " (result=$found)"
    }
    return found
}

/**
 * canSelectAllWithScrollLeft
 */
fun TestDrive.canSelectAllWithScrollLeft(
    vararg expressions: String,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollHorizontalMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {
    val testElement = getTestElement()

    val subject = expressions.map { TestDriver.screenInfo.getSelector(expression = it) }.joinToString()
    var found = false
    val context = TestDriverCommandContext(testElement)
    val logLine = context.execBooleanCommand(subject = subject, log = log) {
        for (expression in expressions) {
            val result = canSelectWithScrollLeft(
                expression = expression,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollMaxCount = scrollMaxCount
            )
            if (result.not()) {
                found = false
                return@execBooleanCommand
            }
        }

        found = true
    }
    if (logLine != null) {
        logLine.message += " (result=$found)"
    }
    return found
}

