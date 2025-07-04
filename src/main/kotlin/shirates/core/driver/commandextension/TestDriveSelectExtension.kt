package shirates.core.driver.commandextension

import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.testcode.CodeExecutionContext


/**
 * select
 */
fun TestDrive.select(
    expression: String,
    allowScroll: Boolean? = CodeExecutionContext.withScroll,
    swipeToCenter: Boolean = false,
    throwsException: Boolean = true,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    selectContext: TestElement = rootElement,
    frame: Bounds? = null,
    useCache: Boolean = testContext.useCache,
    updateLastElement: Boolean = true,
    safeElementOnly: Boolean = CodeExecutionContext.isScrolling,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val sel = getSelector(expression = expression)

    if (CodeExecutionContext.isInCell) {
        val e = CodeExecutionContext.lastCell.innerWidget(selector = sel, frame = frame)
        e.selector = sel
        return e
    }

    var e = TestElement(selector = sel)
    val testElement = if (useCache) getThisOrIt() else null
    val context = TestDriverCommandContext(testElement)
    context.execSelectCommand(selector = sel, subject = sel.toString()) {

        e = TestDriver.findImageOrSelectCore(
            selector = sel,
            allowScroll = allowScroll,
            swipeToCenter = swipeToCenter,
            waitSeconds = waitSeconds,
            throwsException = throwsException,
            selectContext = selectContext,
            frame = frame,
            useCache = useCache,
            safeElementOnly = safeElementOnly,
        )
    }
    if (func != null) {
        func(e)
    }
    if (TestMode.isNoLoadRun) {
        e.selector = sel
    }

    if (updateLastElement) {
        lastElement = e
    }
    return e
}

/**
 * selectWithoutScroll
 */
fun TestDrive.selectWithoutScroll(
    expression: String,
    swipeToCenter: Boolean = false,
    throwsException: Boolean = true,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    frame: Bounds? = null,
    useCache: Boolean = testContext.useCache,
    updateLastElement: Boolean = true,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    return select(
        expression = expression,
        allowScroll = false,
        swipeToCenter = swipeToCenter,
        throwsException = throwsException,
        waitSeconds = waitSeconds,
        frame = frame,
        useCache = useCache,
        updateLastElement = updateLastElement,
        func = func
    )
}

/**
 * widget
 */
fun TestDrive.widget(
    expression: String,
    throwsException: Boolean = true,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    frame: Bounds? = null,
    useCache: Boolean = testContext.useCache,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    var sel = getSelector(expression = expression)
    if (sel.className.isNullOrBlank()) {
        sel = getSelector(expression = "${sel.expression}&&.widget")
    }
    return select(
        expression = sel.expression!!,
        throwsException = throwsException,
        waitSeconds = waitSeconds,
        frame = frame,
        useCache = useCache,
        func = func
    )
}

/**
 * selectWithScrollDown
 */
fun TestDrive.selectWithScrollDown(
    expression: String,
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    swipeToCenter: Boolean = false,
    throwsException: Boolean = true,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val command = "selectWithScrollDown"

    val selector = getSelector(expression = expression)
    val message = message(id = command, subject = selector.toString())
    val context = TestDriverCommandContext(scrollableElement)
    var e = TestElement(selector = selector)
    context.execOperateCommand(command = command, message = message, subject = selector.toString()) {

        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)

        withScrollDown(
            scrollableElement = sc,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            e = TestDriver.findImageOrSelectCore(
                selector = selector,
                allowScroll = true,
                swipeToCenter = swipeToCenter,
                safeElementOnly = true,
                throwsException = throwsException
            )
        }
    }
    if (func != null) {
        func(e)
    }
    lastElement = e
    return lastElement
}

/**
 * selectWithScrollUp
 */
fun TestDrive.selectWithScrollUp(
    expression: String,
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    swipeToCenter: Boolean = false,
    throwsException: Boolean = true,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val command = "selectWithScrollUp"

    val selector = getSelector(expression = expression)
    val message = message(id = command, subject = selector.toString())
    var e = TestElement(selector = selector)
    val context = TestDriverCommandContext(scrollableElement)
    context.execOperateCommand(command = command, message = message, subject = selector.toString()) {

        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)

        withScrollUp(
            scrollableElement = sc,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            e = TestDriver.findImageOrSelectCore(
                selector = selector,
                allowScroll = true,
                swipeToCenter = swipeToCenter,
                safeElementOnly = true,
                throwsException = throwsException
            )
        }
    }
    if (func != null) {
        func(e)
    }
    lastElement = e
    return lastElement
}

/**
 * selectWithScrollRight
 */
fun TestDrive.selectWithScrollRight(
    expression: String,
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    swipeToCenter: Boolean = false,
    throwsException: Boolean = true,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val command = "selectWithScrollRight"

    val selector = getSelector(expression = expression)
    val message = message(id = command, subject = selector.toString())
    var e = TestElement(selector = selector)
    val context = TestDriverCommandContext(scrollableElement)
    context.execOperateCommand(command = command, message = message, subject = selector.toString()) {

        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)

        withScrollRight(
            scrollableElement = sc,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            e = TestDriver.findImageOrSelectCore(
                selector = selector,
                allowScroll = true,
                swipeToCenter = swipeToCenter,
                safeElementOnly = true,
                throwsException = throwsException
            )
        }
    }
    if (func != null) {
        func(e)
    }
    lastElement = e
    return lastElement
}

/**
 * selectWithScrollLeft
 */
fun TestDrive.selectWithScrollLeft(
    expression: String,
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    swipeToCenter: Boolean = false,
    throwsException: Boolean = true,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val command = "selectWithScrollLeft"

    val selector = getSelector(expression = expression)
    val message = message(id = command, subject = selector.toString())
    var e = TestElement(selector = selector)
    val context = TestDriverCommandContext(scrollableElement)
    context.execOperateCommand(command = command, message = message, subject = selector.toString()) {

        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)

        withScrollLeft(
            scrollableElement = sc,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            e = TestDriver.findImageOrSelectCore(
                selector = selector,
                allowScroll = true,
                swipeToCenter = swipeToCenter,
                safeElementOnly = true,
                throwsException = throwsException
            )
        }
    }
    if (func != null) {
        func(e)
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
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val sel = getSelector(expression = expression)
    var e = TestElement(selector = sel)
    val context = TestDriverCommandContext(this.toTestElement)
    context.execSelectCommand(selector = sel, subject = sel.toString()) {

        for (scanRoot in TestElementCache.scanResults) {
            e = TestElementCache.select(
                expression = expression,
                throwsException = false,
                selectContext = scanRoot.element,
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
        func(e)
    }
    lastElement = e
    return lastElement
}

internal fun TestDrive.canSelectCore(
    selector: Selector,
    allowScroll: Boolean?,
    waitSeconds: Double,
    safeElementOnly: Boolean,
    frame: Bounds? = null
): Boolean {

    val e = TestDriver.findImageOrSelectCore(
        selector = selector,
        allowScroll = allowScroll,
        swipeToCenter = false,
        waitSeconds = waitSeconds,
        throwsException = false,
        frame = frame,
        safeElementOnly = safeElementOnly
    )

    return e.isEmpty.not()
}

/**
 * canSelect
 */
fun TestDrive.canSelect(
    expression: String,
    screenName: String = TestDriver.currentScreen,
    allowScroll: Boolean? = CodeExecutionContext.withScroll,
    waitSeconds: Double = 0.0,
    safeElementOnly: Boolean = false,
): Boolean {
    if (CodeExecutionContext.isInCell && this is TestElement) {
        return this.innerWidget(expression = expression).isFound
    }

    val testElement = getThisOrIt()

    val sel = TestDriver.expandExpression(expression = expression, screenName = screenName)
    var found = false
    val context = TestDriverCommandContext(testElement)
    val logLine = context.execBooleanCommand(subject = sel.toString()) {

        found = canSelectCore(
            selector = sel,
            allowScroll = allowScroll,
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
 * canSelectWithoutScroll
 */
fun TestDrive.canSelectWithoutScroll(
    expression: String,
    screenName: String = TestDriver.currentScreen,
    waitSeconds: Double = 0.0,
): Boolean {
    return canSelect(
        expression = expression,
        screenName = screenName,
        allowScroll = false,
        waitSeconds = waitSeconds,
    )
}


/**
 * canSelectWithScrollDown
 */
fun TestDrive.canSelectWithScrollDown(
    expression: String,
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {

    val sel = getSelector(expression = expression)
    var found = false
    val context = TestDriverCommandContext(this.toTestElement)
    val logLine = context.execBooleanCommand(subject = sel.toString(), log = log) {

        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)

        withScrollDown(
            scrollableElement = sc,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            found = canSelectCore(
                selector = sel,
                allowScroll = true,
                waitSeconds = 0.0,
                safeElementOnly = true
            )
        }
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
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {

    val sel = getSelector(expression = expression)
    var found = false
    val context = TestDriverCommandContext(this.toTestElement)
    val logLine = context.execBooleanCommand(subject = sel.toString(), log = log) {

        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)

        withScrollUp(
            scrollableElement = sc,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            found = canSelectCore(
                selector = sel,
                allowScroll = true,
                waitSeconds = 0.0,
                safeElementOnly = true
            )
        }
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
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {

    val sel = getSelector(expression = expression)
    var found = false
    val context = TestDriverCommandContext(this.toTestElement)
    val logLine = context.execBooleanCommand(subject = sel.toString(), log = log) {

        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)

        withScrollRight(
            scrollableElement = sc,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            found = canSelectCore(
                selector = sel,
                allowScroll = true,
                waitSeconds = 0.0,
                safeElementOnly = true
            )
        }
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
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {

    val sel = getSelector(expression = expression)
    var found = false
    val context = TestDriverCommandContext(this.toTestElement)
    val logLine = context.execBooleanCommand(subject = sel.toString(), log = log) {

        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)

        withScrollLeft(
            scrollableElement = sc,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            found = canSelectCore(
                selector = sel,
                allowScroll = true,
                waitSeconds = 0.0,
                safeElementOnly = true
            )
        }
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
): Boolean {

    val subject = TestDriver.screenInfo.getSelector(expression = expression).toString()
    var found = false
    val context = TestDriverCommandContext(this.toTestElement)
    val logLine = context.execBooleanCommand(subject = subject) {
        val e = selectInScanResults(
            expression = expression,
            throwsException = false,
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
    val testElement = getThisOrIt()

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
    allowScroll: Boolean?,
    frame: Bounds?,
    safeElementOnly: Boolean,
    log: Boolean = false
): Boolean {
    val testElement = refreshLastElement()

    val subject = selectors.joinToString()
    var foundAll = false
    val context = TestDriverCommandContext(testElement)
    val logLine = context.execBooleanCommand(subject = subject, log = log) {
        for (selector in selectors) {
            foundAll = canSelectCore(
                selector = selector,
                allowScroll = allowScroll,
                waitSeconds = 0.0,
                frame = frame,
                safeElementOnly = safeElementOnly
            )
            if (foundAll.not()) {
                break
            }
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
    allowScroll: Boolean? = CodeExecutionContext.withScroll,
    frame: Bounds? = null,
    safeElementOnly: Boolean = true,
    log: Boolean = false
): Boolean {
    val testElement = getThisOrIt()

    val subject = expressions.map { TestDriver.screenInfo.getSelector(expression = it) }.joinToString()
    var foundAll = false
    val context = TestDriverCommandContext(testElement)
    val logLine = context.execBooleanCommand(subject = subject, log = log) {
        val screenInfo = TestDriver.screenInfo
        val selectors = expressions.map { screenInfo.getSelector(expression = it) }
        foundAll = canSelectAll(
            selectors = selectors,
            allowScroll = allowScroll,
            frame = frame,
            safeElementOnly = safeElementOnly
        )
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
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {

    val subject = expressions.map { TestDriver.screenInfo.getSelector(expression = it) }.joinToString()
    var foundAll = false
    val context = TestDriverCommandContext(this.toTestElement)
    val logLine = context.execBooleanCommand(subject = subject, log = log) {
        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)
        for (expression in expressions) {
            val result = canSelectWithScrollDown(
                expression = expression,
                scrollableElement = sc,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
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
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {

    val subject = expressions.map { TestDriver.screenInfo.getSelector(expression = it) }.joinToString()
    var found = false
    val context = TestDriverCommandContext(this.toTestElement)
    val logLine = context.execBooleanCommand(subject = subject, log = log) {
        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)
        for (expression in expressions) {
            val result = canSelectWithScrollUp(
                expression = expression,
                scrollableElement = sc,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
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
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {

    val subject = expressions.map { TestDriver.screenInfo.getSelector(expression = it) }.joinToString()
    var found = false
    val context = TestDriverCommandContext(this.toTestElement)
    val logLine = context.execBooleanCommand(subject = subject, log = log) {
        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)
        for (expression in expressions) {
            val result = canSelectWithScrollRight(
                expression = expression,
                scrollableElement = sc,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
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
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {

    val subject = expressions.map { TestDriver.screenInfo.getSelector(expression = it) }.joinToString()
    var found = false
    val context = TestDriverCommandContext(this.toTestElement)
    val logLine = context.execBooleanCommand(subject = subject, log = log) {
        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)
        for (expression in expressions) {
            val result = canSelectWithScrollLeft(
                expression = expression,
                scrollableElement = sc,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
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

