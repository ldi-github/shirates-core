package shirates.core.driver.commandextension

import shirates.core.Const
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.*
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.TestMode.isiOS
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Measure
import shirates.core.logging.Message.message
import shirates.core.logging.ScanRecord
import shirates.core.logging.TestLog


internal fun TestElement.getScrollableElementsInDescendantsAndSelf(): List<TestElement> {

    if (isAndroid) {
        return this.descendantsAndSelf.filter { it.isScrollableElement }
    } else {
        return this.descendantsAndSelf.filter { it.isScrollableElement && it.isVisibleCalculated }
    }
}

internal fun TestElement.getScrollableElementsInAncestorsAndSelf(): List<TestElement> {

    return if (isAndroid) {
        this.ancestorsAndSelf.filter { it.isScrollableElement }
    } else {
        this.ancestorsAndSelf.filter { it.isScrollableElement && it.isVisible }
    }
}

internal fun TestDrive.getScrollableElement(
    scrollFrame: String = ""
): TestElement {

    val frame =
        if (scrollFrame.isNotBlank()) scrollFrame
        else CodeExecutionContext.scrollFrame
    if (frame.isNotBlank()) {
        val s = findElements(expression = frame).firstOrNull()
        if (s != null) {
            return s
        }
    }

    if (CodeExecutionContext.withScroll != false && CodeExecutionContext.scrollableElement != null) {
        return CodeExecutionContext.scrollableElement!!
    }

    val sf = TestDriver.screenInfo.scrollInfo.scrollFrame
    if (sf.isNotBlank()) {
        val s = findElements(expression = sf).firstOrNull()
        if (s != null) {
            return s
        }
    }

    val testElement = getThisOrIt()
    if (testElement.isScrollableElement) {
        return testElement
    }
    if (testElement.isEmpty || testElement == rootElement) {
        return rootElement
    }

    val ancestors = testElement.getScrollableElementsInAncestorsAndSelf()
    if (ancestors.any()) {
        return ancestors.first()
    }

    val descendants = testElement.getScrollableElementsInDescendantsAndSelf()
        .sortedByDescending { it.bounds.area }
    if (descendants.any()) {
        return descendants.first()
    }

    val rootDescendants = rootElement.getScrollableElementsInDescendantsAndSelf()
        .sortedByDescending { it.bounds.area }
    if (rootDescendants.any()) {
        return rootDescendants.first()
    }

    return rootElement
}

private fun TestDrive.scrollCommand(
    command: String,
    scrollFrame: String = CodeExecutionContext.scrollFrame,
    scrollableElement: TestElement? = CodeExecutionContext.scrollableElement,
    direction: ScrollDirection = CodeExecutionContext.scrollDirection ?: ScrollDirection.Down,
    startMarginRatio: Double = CodeExecutionContext.scrollStartMarginRatio,
    endMarginRatio: Double = CodeExecutionContext.scrollEndMarginRatio,
    swipeAction: (ScrollingInfo) -> Unit
) {
    val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)

    val message = message(id = command)
    val context = TestDriverCommandContext(scrollableElement)
    context.execOperateCommand(command = command, message = message) {

        val r = getScrollingInfo(
            scrollableElement = sc,
            direction = direction,
            startMarginRatio = startMarginRatio,
            endMarginRatio = endMarginRatio
        )
        TestLog.info("scrollableElement: $sc")
        swipeAction(r)
        TestDriver.autoScreenshot()
    }
}

/**
 * scrollDown
 */
fun TestDrive.scrollDown(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    intervalSeconds: Double = Const.SWIPE_INTERVAL_SECONDS,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    repeat: Int = 1,
): TestElement {

    scrollCommand(
        scrollFrame = scrollFrame,
        scrollableElement = scrollableElement,
        command = "scrollDown",
        direction = ScrollDirection.Down,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio
    ) { s ->
        swipePointToPointCore(
            SwipeContext(
                swipeFrame = s.adjustedScrollableBounds,
                viewport = viewBounds,
                startX = s.startX,
                startY = s.startY,
                endX = s.endX,
                endY = s.endY,
                durationSeconds = durationSeconds,
                repeat = repeat,
                intervalSeconds = intervalSeconds
            )
        )
    }

    return lastElement
}

/**
 * scrollUp
 */
fun TestDrive.scrollUp(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    intervalSeconds: Double = Const.SWIPE_INTERVAL_SECONDS,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    repeat: Int = 1,
): TestElement {

    scrollCommand(
        scrollFrame = scrollFrame,
        scrollableElement = scrollableElement,
        command = "scrollUp",
        direction = ScrollDirection.Up,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio
    ) { s ->
        swipePointToPointCore(
            SwipeContext(
                swipeFrame = s.adjustedScrollableBounds,
                viewport = viewBounds,
                startX = s.startX,
                startY = s.startY,
                endX = s.endX,
                endY = s.endY,
                durationSeconds = durationSeconds,
                intervalSeconds = intervalSeconds,
                repeat = repeat,
            )
        )
    }

    return lastElement
}

/**
 * scrollRight
 */
fun TestDrive.scrollRight(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = Const.SWIPE_INTERVAL_SECONDS
): TestElement {

    scrollCommand(
        scrollFrame = scrollFrame,
        scrollableElement = scrollableElement,
        command = "scrollRight",
        direction = ScrollDirection.Right,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio
    ) { s ->
        swipePointToPointCore(
            SwipeContext(
                swipeFrame = s.adjustedScrollableBounds,
                viewport = viewBounds,
                startX = s.startX,
                startY = s.startY,
                endX = s.endX,
                endY = s.endY,
                durationSeconds = durationSeconds,
                intervalSeconds = intervalSeconds,
                repeat = repeat,
            )
        )
    }

    return lastElement
}

/**
 * scrollLeft
 */
fun TestDrive.scrollLeft(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = Const.SWIPE_INTERVAL_SECONDS
): TestElement {

    scrollCommand(
        scrollFrame = scrollFrame,
        scrollableElement = scrollableElement,
        command = "scrollLeft",
        direction = ScrollDirection.Left,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio
    ) { s ->
        swipePointToPointCore(
            SwipeContext(
                swipeFrame = s.adjustedScrollableBounds,
                viewport = viewBounds,
                startX = s.startX,
                startY = s.startY,
                endX = s.endX,
                endY = s.endY,
                durationSeconds = durationSeconds,
                intervalSeconds = intervalSeconds,
                repeat = repeat,
            )
        )
    }

    return lastElement
}

private fun TestDrive.scrollToEdgeCommand(
    scrollFrame: String = "",
    scrollableElement: TestElement?,
    command: String,
    maxLoopCount: Int,
    direction: ScrollDirection,
    flick: Boolean,
    startMarginRatio: Double = testContext.getScrollStartMarginRatio(direction = direction),
    endMarginRatio: Double = testContext.getScrollEndMarginRatio(direction = direction),
    repeat: Int,
    intervalSeconds: Double = testContext.getIntervalSeconds(flick = flick),
    edgeSelector: String?,
) {
    val message = message(id = command)
    val context = TestDriverCommandContext(lastElement)
    context.execOperateCommand(command = command, message = message) {

        doUntilScrollStop(
            scrollFrame = scrollFrame,
            scrollableElement = scrollableElement,
            maxLoopCount = maxLoopCount,
            direction = direction,
            startMarginRatio = startMarginRatio,
            endMarginRatio = endMarginRatio,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
            edgeSelector = edgeSelector,
        )

        TestDriver.invalidateCache()
    }
}

/**
 * scrollToBottom
 */
fun TestDrive.scrollToBottom(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    repeat: Int = testContext.scrollToEdgeBoost,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
    flick: Boolean = true,
    maxLoopCount: Int = testContext.scrollMaxCount,
    edgeSelector: String? = null,
    imageCompare: Boolean = false
): TestElement {

    scrollToEdgeCommand(
        scrollFrame = scrollFrame,
        scrollableElement = scrollableElement,
        command = "scrollToBottom",
        direction = ScrollDirection.Down,
        maxLoopCount = maxLoopCount,
        flick = flick,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio,
        repeat = repeat,
        intervalSeconds = intervalSeconds,
        edgeSelector = edgeSelector,
    )

    return lastElement
}

/**
 * scrollToTop
 */
fun TestDrive.scrollToTop(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    repeat: Int = testContext.scrollToEdgeBoost,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
    flick: Boolean = true,
    maxLoopCount: Int = testContext.scrollMaxCount,
    edgeSelector: String? = null,
    imageCompare: Boolean = false
): TestElement {

    scrollToEdgeCommand(
        scrollFrame = scrollFrame,
        scrollableElement = scrollableElement,
        command = "scrollToTop",
        direction = ScrollDirection.Up,
        maxLoopCount = maxLoopCount,
        flick = flick,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio,
        repeat = repeat,
        intervalSeconds = intervalSeconds,
        edgeSelector = edgeSelector,
    )

    return lastElement
}

/**
 * scrollToRightEdge
 */
fun TestDrive.scrollToRightEdge(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    repeat: Int = testContext.scrollToEdgeBoost,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
    flick: Boolean = true,
    maxLoopCount: Int = testContext.scrollMaxCount,
    edgeSelector: String? = null,
    imageCompare: Boolean = false
): TestElement {

    scrollToEdgeCommand(
        scrollFrame = scrollFrame,
        scrollableElement = scrollableElement,
        command = "scrollToRightEdge",
        direction = ScrollDirection.Right,
        maxLoopCount = maxLoopCount,
        flick = flick,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio,
        repeat = repeat,
        intervalSeconds = intervalSeconds,
        edgeSelector = edgeSelector,
    )

    return lastElement
}

/**
 * scrollToLeftEdge
 */
fun TestDrive.scrollToLeftEdge(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    repeat: Int = testContext.scrollToEdgeBoost,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
    flick: Boolean = true,
    maxLoopCount: Int = testContext.scrollMaxCount,
    edgeSelector: String? = null,
    imageCompare: Boolean = false
): TestElement {

    scrollToEdgeCommand(
        scrollFrame = scrollFrame,
        scrollableElement = scrollableElement,
        command = "scrollToLeftEdge",
        direction = ScrollDirection.Left,
        maxLoopCount = maxLoopCount,
        flick = flick,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio,
        repeat = repeat,
        intervalSeconds = intervalSeconds,
        edgeSelector = edgeSelector,
    )

    return lastElement
}

/**
 * doUntilScrollStop
 */
fun TestDrive.doUntilScrollStop(
    scrollFrame: String = CodeExecutionContext.scrollFrame,
    scrollableElement: TestElement? = CodeExecutionContext.scrollableElement,
    maxLoopCount: Int = CodeExecutionContext.scrollMaxCount,
    direction: ScrollDirection = CodeExecutionContext.scrollDirection ?: ScrollDirection.Down,
    durationSeconds: Double = CodeExecutionContext.scrollDurationSeconds,
    startMarginRatio: Double = CodeExecutionContext.scrollStartMarginRatio,
    endMarginRatio: Double = CodeExecutionContext.scrollEndMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
    edgeSelector: String? = null,
    scrollFunc: (() -> Unit)? = null,
    actionFunc: (() -> Boolean)? = null
): TestElement {

    val ms = Measure()

    val originalIsScrolling = CodeExecutionContext.isScrolling
    try {
        CodeExecutionContext.isScrolling = true
        return doUntilScrollStopCore(
            scrollFunc = scrollFunc,
            direction = direction,
            scrollFrame = scrollFrame,
            scrollableElement = scrollableElement,
            durationSeconds = durationSeconds,
            startMarginRatio = startMarginRatio,
            endMarginRatio = endMarginRatio,
            edgeSelector = edgeSelector,
            actionFunc = actionFunc,
            maxLoopCount = maxLoopCount,
            repeat = repeat,
            intervalSeconds = intervalSeconds
        )
    } finally {
        CodeExecutionContext.isScrolling = originalIsScrolling
        ms.end()
    }
}

internal fun TestDrive.doUntilScrollStopCore(
    scrollFunc: (() -> Unit)?,
    direction: ScrollDirection,
    scrollFrame: String,
    scrollableElement: TestElement?,
    durationSeconds: Double,
    intervalSeconds: Double,
    startMarginRatio: Double,
    endMarginRatio: Double,
    maxLoopCount: Int,
    edgeSelector: String?,
    repeat: Int,
    actionFunc: (() -> Boolean)?
): TestElement {
    val scroll = scrollFunc ?: {
        val ms = Measure()
        suppressHandler {
            if (direction.isDown) {
                scrollDown(
                    scrollFrame = scrollFrame,
                    scrollableElement = scrollableElement,
                    durationSeconds = durationSeconds,
                    intervalSeconds = intervalSeconds,
                    startMarginRatio = startMarginRatio,
                    endMarginRatio = endMarginRatio,
                    repeat = repeat
                )
            } else if (direction.isUp) {
                scrollUp(
                    scrollFrame = scrollFrame,
                    scrollableElement = scrollableElement,
                    durationSeconds = durationSeconds,
                    intervalSeconds = intervalSeconds,
                    startMarginRatio = startMarginRatio,
                    endMarginRatio = endMarginRatio,
                    repeat = repeat
                )
            } else if (direction.isRight) {
                scrollRight(
                    scrollFrame = scrollFrame,
                    scrollableElement = scrollableElement,
                    durationSeconds = durationSeconds,
                    intervalSeconds = intervalSeconds,
                    startMarginRatio = startMarginRatio,
                    endMarginRatio = endMarginRatio,
                    repeat = repeat
                )
            } else if (direction.isLeft) {
                scrollLeft(
                    scrollFrame = scrollFrame,
                    scrollableElement = scrollableElement,
                    durationSeconds = durationSeconds,
                    intervalSeconds = intervalSeconds,
                    startMarginRatio = startMarginRatio,
                    endMarginRatio = endMarginRatio,
                    repeat = repeat
                )
            }
        }
        ms.end()
    }

    val msLastSerialized = Measure("lastSerialized")
    var lastSerialized = testDrive.widgets.lastOrNull()?.toString() ?: ""
    msLastSerialized.end()

    fun isEndOfScroll(): Boolean {

        val ms = Measure("isEndOfScroll")
        try {
            val scrollInfo = TestDriver.screenInfo.scrollInfo
            val expressions = if (edgeSelector != null) {
                mutableListOf(edgeSelector)
            } else {
                when (direction) {
                    ScrollDirection.Down -> scrollInfo.endElements
                    ScrollDirection.Up -> scrollInfo.startElements
                    else -> mutableListOf()
                }
            }
            if (edgeElementFound(expressions = expressions)) {
                return true
            }

            val msSerialized = Measure("serialized")
            val serialized =
                if (testContext.useCache) rootElement.descendants.serialize()
                else testDrive.widgets.lastOrNull()?.toString() ?: ""
            msSerialized.end()
            val result = serialized == lastSerialized
            lastSerialized = serialized
            return result
        } finally {
            ms.end()
        }
    }

    if (TestDriver.isInitialized) {
        TestDriver.refreshCache()
        if (actionFunc != null) {
            val result = actionFunc()
            if (result) {
                return lastElement
            }
        }

        val ms = Measure("doUntilScrollStop-loop")
        try {
            for (i in 1..maxLoopCount) {
                scroll()
                TestDriver.refreshCache()

                if (actionFunc != null) {
                    val result = actionFunc()
                    if (result) {
                        return lastElement
                    }
                }

                val endOfScroll = isEndOfScroll()
                TestLog.info("endOfScroll=$endOfScroll", log = PropertiesManager.enableSyncLog)
                if (endOfScroll) {
                    break
                }
                if (i < maxLoopCount && intervalSeconds > 0.0) {
                    Thread.sleep((intervalSeconds * 1000).toLong())
                }
            }
        } finally {
            ms.end()
        }
    }

    return lastElement
}

internal fun TestDrive.edgeElementFound(expressions: List<String>): Boolean {

    for (expression in expressions) {
        val e =
            TestDriver.select(expression = expression, allowScroll = false, throwsException = false, waitSeconds = 0.0)
        if (e.isFound && e.isVisibleCalculated) {
            TestLog.info("edge element found. ($expression)")
            return true
        } else {
            TestLog.info("Finding edge element of '$expression'")
        }
    }
    return false
}

private fun List<TestElement>.serialize(): String {

    val list = this.map { it.toString() }
    return list.joinToString("\n")
}

/**
 * withScrollDown
 */
fun TestDrive.withScrollDown(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    scrollToEdgeBoost: Int = testContext.scrollToEdgeBoost,
    proc: () -> Unit
): TestElement {

    val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)

    val command = "withScrollDown"
    val message = message(id = command)
    val context = TestDriverCommandContext(lastElement)
    context.execWithScroll(
        command = command,
        scrollDirection = ScrollDirection.Down,
        scrollableElement = sc,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        scrollToEdgeBoost = scrollToEdgeBoost,
        message = message
    ) {
        proc()
    }

    return lastElement
}

/**
 * withScrollUp
 */
fun TestDrive.withScrollUp(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    scrollToEdgeBoost: Int = testContext.scrollToEdgeBoost,
    proc: () -> Unit
): TestElement {

    val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)

    val command = "withScrollUp"
    val message = message(id = command)
    val context = TestDriverCommandContext(lastElement)
    context.execWithScroll(
        command = command,
        scrollDirection = ScrollDirection.Up,
        scrollableElement = sc,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        scrollToEdgeBoost = scrollToEdgeBoost,
        message = message
    ) {
        proc()
    }

    return lastElement
}

/**
 * withScrollRight
 */
fun TestDrive.withScrollRight(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    scrollToEdgeBoost: Int = testContext.scrollToEdgeBoost,
    proc: () -> Unit
): TestElement {

    val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)

    val command = "withScrollRight"
    val message = message(id = command)
    val context = TestDriverCommandContext(lastElement)
    context.execWithScroll(
        command = command,
        scrollDirection = ScrollDirection.Right,
        scrollableElement = sc,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        scrollToEdgeBoost = scrollToEdgeBoost,
        message = message
    ) {
        proc()
    }

    return lastElement
}

/**
 * withScrollLeft
 */
fun TestDrive.withScrollLeft(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    scrollToEdgeBoost: Int = testContext.scrollToEdgeBoost,
    proc: () -> Unit
): TestElement {

    val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)

    val command = "withScrollLeft"
    val message = message(id = command)
    val context = TestDriverCommandContext(lastElement)
    context.execWithScroll(
        command = command,
        scrollDirection = ScrollDirection.Left,
        scrollableElement = sc,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        scrollToEdgeBoost = scrollToEdgeBoost,
        message = message
    ) {
        proc()
    }

    return lastElement
}

/**
 * withoutScroll
 */
fun TestDrive.withoutScroll(
    proc: () -> Unit
): TestElement {

    val command = "withoutScroll"
    val message = message(id = command)
    val context = TestDriverCommandContext(lastElement)
    context.execWithScroll(
        command = command,
        withScroll = false,
        scrollDirection = null,
        message = message
    ) {
        proc()
    }

    return lastElement
}

/**
 * scanElements
 */
fun TestDrive.scanElements(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    direction: ScrollDirection = ScrollDirection.Down,
    startMarginRatio: Double = testContext.getScrollStartMarginRatio(direction),
    endMarginRatio: Double = testContext.getScrollEndMarginRatio(direction),
    maxScrollTimes: Int = testContext.scrollMaxCount,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    endSelector: String? = null,
    imageCompare: Boolean = false
): TestElement {

    val command = "scanElements"
    val message = message(id = command, arg1 = direction.toString())

    val context = TestDriverCommandContext(lastElement)
    context.execOperateCommand(command = command, message = message) {
        useCache {
            val lineNo = TestLog.nextLineNo

            TestElementCache.scanResults.clear()
            TestDriver.refreshCache()
            TestElementCache.scanResults.add(
                ScanRecord(
                    lineNo = lineNo,
                    sourceXml = TestElementCache.sourceXml,
                    element = TestElementCache.rootElement
                )
            )
            TestDriver.autoScreenshot()

            doUntilScrollStop(
                scrollFrame = scrollFrame,
                scrollableElement = scrollableElement,
                repeat = 1,
                maxLoopCount = maxScrollTimes,
                direction = direction,
                durationSeconds = durationSeconds,
                startMarginRatio = startMarginRatio,
                endMarginRatio = endMarginRatio,
                edgeSelector = endSelector,
            ) {
                val lastXml = TestElementCache.scanResults.last().sourceXml
                val thisXml = TestElementCache.sourceXml
                if (thisXml != lastXml) {
                    TestElementCache.scanResults.add(
                        ScanRecord(
                            lineNo = lineNo,
                            sourceXml = thisXml,
                            element = rootElement
                        )
                    )
                }

                false
            }
        }
    }
    TestLog.trace("scanElements completed.(pageCount=${TestElementCache.scanResults.count()})")

    return lastElement
}

internal fun TestDrive.getScrollingInfo(
    scrollableElement: TestElement,
    direction: ScrollDirection = ScrollDirection.None,
    startMarginRatio: Double = testContext.getScrollStartMarginRatio(direction),
    endMarginRatio: Double = testContext.getScrollEndMarginRatio(direction),
): ScrollingInfo {

    val s = TestDriver.screenInfo.scrollInfo
    val headerBottom = s.getHeaderBottom()
    val footerTop = s.getFooterTop()

    if (isAndroid || (isiOS && isKeyboardShown.not())) {
        val r = ScrollingInfo(
            errorMessage = "",
            bounds = scrollableElement.bounds,
            viewport = viewBounds,
            direction = direction,
            startMarginRatio = startMarginRatio,
            endMarginRatio = endMarginRatio,
            headerBottom = headerBottom,
            footerTop = footerTop
        )
        return r
    }

    val keyboardElement = testDrive.getKeyboardInIos()
    val b = scrollableElement.bounds
    val height = keyboardElement.bounds.top - b.top
    val scrollBounds = Bounds(left = b.left, top = b.top, width = b.width, height = height)
    val r = ScrollingInfo(
        errorMessage = "",
        bounds = scrollBounds,
        viewport = viewBounds,
        direction = direction,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio,
        headerBottom = headerBottom,
        footerTop = footerTop
    )
    return r
}
