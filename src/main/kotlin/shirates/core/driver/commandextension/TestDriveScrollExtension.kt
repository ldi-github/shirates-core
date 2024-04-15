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


internal fun TestElement.getScrollableElementsInDescendantsAndSelf(
    includeCellHost: Boolean = false
): List<TestElement> {

    if (isAndroid) {
        return this.descendantsAndSelf.filter { it.isScrollable || (includeCellHost && it.isCellHost) }
    } else {
        return this.descendantsAndSelf.filter { (it.isScrollable || (includeCellHost && it.isCellHost)) && it.isVisibleCalculated }
    }
}

internal fun TestElement.getScrollableElementsInAncestorsAndSelf(
    includeCellHost: Boolean = false
): List<TestElement> {

    return if (isAndroid) {
        this.ancestorsAndSelf.filter { it.isScrollable || (includeCellHost && it.isCellHost) }
    } else {
        this.ancestorsAndSelf.filter { (it.isScrollable || (includeCellHost && it.isCellHost)) && it.isVisible }
    }
}

internal fun TestDrive.getScrollableElement(
    scrollable: String = ""
): TestElement {

    if (scrollable.isNotBlank()) {
        val s = select(scrollable)
        if (s.isFound) {
            return s
        }
    }

    val testElement = getThisOrIt()
    if (testElement.isScrollable) {
        return testElement
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

    val rootDescendants = rootElement.getScrollableElementsInDescendantsAndSelf(includeCellHost = true)
        .sortedByDescending { it.bounds.area }
    if (rootDescendants.any()) {
        return rootDescendants.first()
    }

    return rootElement
}

/**
 * hasScrollable
 */
val TestDrive.hasScrollable: Boolean
    get() {
        return this.scrollFrame.isScrollable
    }

private fun TestDrive.scrollCommand(
    scrollableElement: TestElement,
    command: String,
    direction: ScrollDirection,
    startMarginRatio: Double,
    endMarginRatio: Double,
    swipeAction: (ScrollingInfo) -> Unit
) {
    val message = message(id = command)
    val context = TestDriverCommandContext(scrollableElement)
    context.execOperateCommand(command = command, message = message) {

        val r = getScrollingInfo(
            scrollableElement = scrollableElement,
            direction = direction,
            startMarginRatio = startMarginRatio,
            endMarginRatio = endMarginRatio
        )
        swipeAction(r)
        TestDriver.autoScreenshot()
    }
}

/**
 * scrollDown
 */
fun TestDrive.scrollDown(
    scrollable: String = "",
    durationSeconds: Double = testContext.swipeDurationSeconds,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = Const.SWIPE_INTERVAL_SECONDS
): TestElement {

    val scrollableElement = getScrollableElement(scrollable)
    scrollCommand(
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
    scrollable: String = "",
    durationSeconds: Double = testContext.swipeDurationSeconds,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = Const.SWIPE_INTERVAL_SECONDS
): TestElement {

    val scrollableElement = getScrollableElement(scrollable)
    scrollCommand(
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
                repeat = repeat,
                intervalSeconds = intervalSeconds
            )
        )
    }

    return lastElement
}

/**
 * scrollRight
 */
fun TestDrive.scrollRight(
    scrollable: String = "",
    durationSeconds: Double = testContext.swipeDurationSeconds,
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = Const.SWIPE_INTERVAL_SECONDS
): TestElement {

    val scrollableElement = getScrollableElement(scrollable)
    scrollCommand(
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
                repeat = repeat,
                intervalSeconds = intervalSeconds
            )
        )
    }

    return lastElement
}

/**
 * scrollLeft
 */
fun TestDrive.scrollLeft(
    scrollable: String = "",
    durationSeconds: Double = testContext.swipeDurationSeconds,
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = Const.SWIPE_INTERVAL_SECONDS
): TestElement {

    val scrollableElement = getScrollableElement(scrollable)
    scrollCommand(
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
                repeat = repeat,
                intervalSeconds = intervalSeconds
            )
        )
    }

    return lastElement
}

private fun TestDrive.scrollToEdgeCommand(
    scrollable: String = "",
    command: String,
    maxLoopCount: Int,
    direction: ScrollDirection,
    flick: Boolean,
    startMarginRatio: Double,
    endMarginRatio: Double,
    repeat: Int,
    intervalSeconds: Double,
    edgeSelector: String?,
    imageCompare: Boolean
) {
    val message = message(id = command)
    val context = TestDriverCommandContext(lastElement)
    context.execOperateCommand(command = command, message = message) {

        doUntilScrollStop(
            scrollable = scrollable,
            maxLoopCount = maxLoopCount,
            direction = direction,
            flick = flick,
            startMarginRatio = startMarginRatio,
            endMarginRatio = endMarginRatio,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
            edgeSelector = edgeSelector,
            imageCompare = imageCompare
        )

        TestDriver.invalidateCache()
    }
}

/**
 * scrollToBottom
 */
fun TestDrive.scrollToBottom(
    scrollable: String = "",
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
        scrollable = scrollable,
        command = "scrollToBottom",
        direction = ScrollDirection.Down,
        maxLoopCount = maxLoopCount,
        flick = flick,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio,
        repeat = repeat,
        intervalSeconds = intervalSeconds,
        edgeSelector = edgeSelector,
        imageCompare = imageCompare
    )

    return lastElement
}

/**
 * scrollToTop
 */
fun TestDrive.scrollToTop(
    scrollable: String = "",
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
        scrollable = scrollable,
        command = "scrollToTop",
        direction = ScrollDirection.Up,
        maxLoopCount = maxLoopCount,
        flick = flick,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio,
        repeat = repeat,
        intervalSeconds = intervalSeconds,
        edgeSelector = edgeSelector,
        imageCompare = imageCompare
    )

    return lastElement
}

/**
 * scrollToRightEdge
 */
fun TestDrive.scrollToRightEdge(
    scrollable: String = "",
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
        scrollable = scrollable,
        command = "scrollToRightEdge",
        direction = ScrollDirection.Right,
        maxLoopCount = maxLoopCount,
        flick = flick,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio,
        repeat = repeat,
        intervalSeconds = intervalSeconds,
        edgeSelector = edgeSelector,
        imageCompare = imageCompare
    )

    return lastElement
}

/**
 * scrollToLeftEdge
 */
fun TestDrive.scrollToLeftEdge(
    scrollable: String = "",
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
        scrollable = scrollable,
        command = "scrollToLeftEdge",
        direction = ScrollDirection.Left,
        maxLoopCount = maxLoopCount,
        flick = flick,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio,
        repeat = repeat,
        intervalSeconds = intervalSeconds,
        edgeSelector = edgeSelector,
        imageCompare = imageCompare
    )

    return lastElement
}

/**
 * doUntilScrollStop
 */
fun TestDrive.doUntilScrollStop(
    scrollable: String = "",
    maxLoopCount: Int = testContext.scrollMaxCount,
    direction: ScrollDirection,
    flick: Boolean = false,
    durationSeconds: Double =
        if (flick) testContext.flickDurationSeconds
        else testContext.swipeDurationSeconds,
    startMarginRatio: Double = testContext.scrollStartMarginRatio(direction),
    endMarginRatio: Double = testContext.scrollEndMarginRatio(direction),
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
    edgeSelector: String? = null,
    imageCompare: Boolean = false,
    scrollFunc: (() -> Unit)? = null,
    actionFunc: (() -> Boolean)? = null
): TestElement {

    val ms = Measure()

    val original = CodeExecutionContext.isScrolling
    try {
        CodeExecutionContext.isScrolling = true
        return doUntilScrollStopCore(
            scrollable = scrollable,
            scrollFunc = scrollFunc,
            direction = direction,
            durationSeconds = durationSeconds,
            startMarginRatio = startMarginRatio,
            endMarginRatio = endMarginRatio,
            imageCompare = imageCompare,
            edgeSelector = edgeSelector,
            actionFunc = actionFunc,
            maxLoopCount = maxLoopCount,
            repeat = repeat,
            intervalSeconds = intervalSeconds
        )
    } finally {
        CodeExecutionContext.isScrolling = original
        ms.end()
    }
}

private fun TestDrive.doUntilScrollStopCore(
    scrollable: String,
    scrollFunc: (() -> Unit)?,
    direction: ScrollDirection,
    durationSeconds: Double,
    startMarginRatio: Double,
    endMarginRatio: Double,
    imageCompare: Boolean,
    edgeSelector: String?,
    actionFunc: (() -> Boolean)?,
    maxLoopCount: Int,
    repeat: Int,
    intervalSeconds: Double
): TestElement {
    val scroll = scrollFunc ?: {
        val ms = Measure()
        suppressHandler {
            if (direction.isDown) {
                scrollDown(
                    scrollable = scrollable,
                    durationSeconds = durationSeconds,
                    startMarginRatio = startMarginRatio,
                    endMarginRatio = endMarginRatio,
                    repeat = repeat
                )
            } else if (direction.isUp) {
                scrollUp(
                    scrollable = scrollable,
                    durationSeconds = durationSeconds,
                    startMarginRatio = startMarginRatio,
                    endMarginRatio = endMarginRatio,
                    repeat = repeat
                )
            } else if (direction.isRight) {
                scrollRight(
                    scrollable = scrollable,
                    durationSeconds = durationSeconds,
                    startMarginRatio = startMarginRatio,
                    endMarginRatio = endMarginRatio,
                    repeat = repeat
                )
            } else if (direction.isLeft) {
                scrollLeft(
                    scrollable = scrollable,
                    durationSeconds = durationSeconds,
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
        val e = TestDriver.select(expression = expression, throwsException = false, waitSeconds = 0.0)
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
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    proc: () -> Unit
): TestElement {

    val command = "withScrollDown"
    val message = message(id = command)
    val context = TestDriverCommandContext(lastElement)
    context.execWithScroll(
        command = command,
        scrollDirection = ScrollDirection.Down,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollMaxCount = scrollMaxCount,
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
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    proc: () -> Unit
): TestElement {

    val command = "withScrollUp"
    val message = message(id = command)
    val context = TestDriverCommandContext(lastElement)
    context.execWithScroll(
        command = command,
        scrollDirection = ScrollDirection.Up,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollMaxCount = scrollMaxCount,
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
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    proc: () -> Unit
): TestElement {

    val command = "withScrollRight"
    val message = message(id = command)
    val context = TestDriverCommandContext(lastElement)
    context.execWithScroll(
        command = command,
        scrollDirection = ScrollDirection.Right,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollMaxCount = scrollMaxCount,
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
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    proc: () -> Unit
): TestElement {

    val command = "withScrollLeft"
    val message = message(id = command)
    val context = TestDriverCommandContext(lastElement)
    context.execWithScroll(
        command = command,
        scrollDirection = ScrollDirection.Left,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollMaxCount = scrollMaxCount,
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
    scrollable: String = "",
    direction: ScrollDirection = ScrollDirection.Down,
    startMarginRatio: Double = testContext.scrollStartMarginRatio(direction),
    endMarginRatio: Double = testContext.scrollEndMarginRatio(direction),
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
            val lineNo = TestLog.lines.count() + 1

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

            val scrollableElement = getScrollableElement(scrollable)
            val r = getScrollingInfo(
                scrollableElement = scrollableElement,
                direction = direction,
                startMarginRatio = startMarginRatio,
                endMarginRatio = endMarginRatio
            )
            if (r.hasError) {
                TestLog.trace("no scrollable element found.")
                return@useCache
            }

            doUntilScrollStop(
                scrollable = scrollable,
                repeat = 1,
                maxLoopCount = maxScrollTimes,
                direction = direction,
                durationSeconds = durationSeconds,
                startMarginRatio = startMarginRatio,
                endMarginRatio = endMarginRatio,
                edgeSelector = endSelector,
                imageCompare = imageCompare
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
    startMarginRatio: Double = testContext.scrollStartMarginRatio(direction),
    endMarginRatio: Double = testContext.scrollEndMarginRatio(direction),
): ScrollingInfo {

    val s = TestDriver.screenInfo.scrollInfo
    val headerBottom = s.getHeaderBottom()
    val footerTop = s.getFooterTop()

    if (isAndroid || (isiOS && isKeyboardShown.not())) {
        val r = ScrollingInfo(
            errorMessage = "",
            scrollableBounds = scrollableElement.bounds,
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
        scrollableBounds = scrollBounds,
        viewport = viewBounds,
        direction = direction,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio,
        headerBottom = headerBottom,
        footerTop = footerTop
    )
    return r
}
