package shirates.core.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.*
import shirates.core.driver.TestMode.isAndroid
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Measure
import shirates.core.logging.Message.message
import shirates.core.logging.ScanRecord
import shirates.core.logging.TestLog
import shirates.core.utility.image.isSame
import kotlin.math.max
import kotlin.math.min


internal fun TestDrive.getScrollableElementsInDescendants(): List<TestElement> {

    val testElement = getThisOrRootElement()

    if (isAndroid) {
        return testElement.descendants.filter { it.isScrollable }
    } else {
        return testElement.descendants.filter { it.isScrollable && it.isVisibleCalculated }
    }
}

internal fun TestDrive.getScrollableElementsInAncestors(): List<TestElement> {

    val testElement = getThisOrRootElement()

    return if (isAndroid) {
        testElement.ancestors.filter { it.isScrollable }
    } else {
        testElement.ancestors.filter { it.isScrollable && it.isVisible }
    }
}

internal fun TestDrive.getScrollableElement(): TestElement {

    val testElement = getThisOrRootElement()
    if (testElement.isScrollable) {
        return testElement
    }

    val innerLargestScrollableElement = this.getScrollableElementsInDescendants().sortedByDescending { it.bounds.area }
        .firstOrNull()
    if (innerLargestScrollableElement != null) {
        return innerLargestScrollableElement
    }

    val largestScrollableElement =
        rootElement.getScrollableElementsInDescendants().sortedByDescending { it.bounds.area }
            .firstOrNull()
    if (largestScrollableElement != null) {
        return largestScrollableElement
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
    command: String,
    direction: ScrollDirection,
    startMarginRatio: Double,
    swipeAction: (ScrollingInfo) -> Unit
) {
    val testElement = getThisOrRootElement()

    val message = message(id = command)
    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        val r = getScrollingInfo(direction = direction, marginRatio = startMarginRatio)
        swipeAction(r)
        TestDriver.autoScreenshot()
    }
}

/**
 * scrollDown
 */
fun TestDrive.scrollDown(
    durationSeconds: Double = testContext.swipeDurationSeconds,
    startMarginRatio: Double = testContext.scrollVerticalMarginRatio
): TestElement {

    scrollCommand(
        command = "scrollDown",
        direction = ScrollDirection.Down,
        startMarginRatio = startMarginRatio
    ) { s ->
        swipePointToPointCore(
            SwipeContext(
                swipeFrame = s.adjustedScrollableBounds,
                viewPort = viewport,
                startX = s.startX,
                startY = s.startY,
                endX = s.endX,
                endY = s.endY,
                durationSeconds = durationSeconds,
            )
        )
    }

    return lastElement
}

/**
 * scrollUp
 */
fun TestDrive.scrollUp(
    durationSeconds: Double = testContext.swipeDurationSeconds,
    startMarginRatio: Double = testContext.scrollVerticalMarginRatio
): TestElement {

    scrollCommand(
        command = "scrollUp",
        direction = ScrollDirection.Up,
        startMarginRatio = startMarginRatio
    ) { s ->
        swipePointToPointCore(
            SwipeContext(
                swipeFrame = s.adjustedScrollableBounds,
                viewPort = viewport,
                startX = s.startX,
                startY = s.startY,
                endX = s.endX,
                endY = s.endY,
                durationSeconds = durationSeconds
            )
        )
    }

    return lastElement
}

/**
 * scrollRight
 */
fun TestDrive.scrollRight(
    durationSeconds: Double = testContext.swipeDurationSeconds,
    startMarginRatio: Double = testContext.scrollHorizontalMarginRatio
): TestElement {

    scrollCommand(
        command = "scrollRight",
        direction = ScrollDirection.Right,
        startMarginRatio = startMarginRatio
    ) { s ->
        swipePointToPointCore(
            SwipeContext(
                swipeFrame = s.adjustedScrollableBounds,
                viewPort = viewport,
                startX = s.startX,
                startY = s.startY,
                endX = s.endX,
                endY = s.endY,
                durationSeconds = durationSeconds
            )
        )
    }

    return lastElement
}

/**
 * scrollLeft
 */
fun TestDrive.scrollLeft(
    durationSeconds: Double = testContext.swipeDurationSeconds,
    startMarginRatio: Double = testContext.scrollHorizontalMarginRatio
): TestElement {

    scrollCommand(
        command = "scrollLeft",
        direction = ScrollDirection.Left,
        startMarginRatio = startMarginRatio
    ) { s ->
        swipePointToPointCore(
            SwipeContext(
                swipeFrame = s.adjustedScrollableBounds,
                viewPort = viewport,
                startX = s.startX,
                startY = s.startY,
                endX = s.endX,
                endY = s.endY,
                durationSeconds = durationSeconds
            )
        )
    }

    return lastElement
}

private fun TestDrive.scrollToEdgeCommand(
    command: String,
    maxLoopCount: Int,
    direction: ScrollDirection,
    flick: Boolean,
    startMarginRatio: Double,
    repeat: Int,
    intervalSeconds: Double,
    edgeSelector: String?,
    imageCompare: Boolean
) {
    val testElement = getThisOrRootElement()

    val message = message(id = command)
    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {

        doUntilScrollStop(
            maxLoopCount = maxLoopCount,
            direction = direction,
            flick = flick,
            startMarginRatio = startMarginRatio,
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
    startMarginRatio: Double = testContext.scrollVerticalMarginRatio,
    repeat: Int = testContext.scrollToEdgeBoost,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
    flick: Boolean = true,
    maxLoopCount: Int = testContext.scrollMaxCount,
    edgeSelector: String? = null,
    imageCompare: Boolean = false
): TestElement {

    scrollToEdgeCommand(
        command = "scrollToBottom",
        direction = ScrollDirection.Down,
        maxLoopCount = maxLoopCount,
        flick = flick,
        startMarginRatio = startMarginRatio,
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
    startMarginRatio: Double = testContext.scrollVerticalMarginRatio,
    repeat: Int = testContext.scrollToEdgeBoost,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
    flick: Boolean = true,
    maxLoopCount: Int = testContext.scrollMaxCount,
    edgeSelector: String? = null,
    imageCompare: Boolean = false
): TestElement {

    scrollToEdgeCommand(
        command = "scrollToTop",
        direction = ScrollDirection.Up,
        maxLoopCount = maxLoopCount,
        flick = flick,
        startMarginRatio = startMarginRatio,
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
    startMarginRatio: Double = testContext.scrollHorizontalMarginRatio,
    repeat: Int = testContext.scrollToEdgeBoost,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
    flick: Boolean = true,
    maxLoopCount: Int = testContext.scrollMaxCount,
    edgeSelector: String? = null,
    imageCompare: Boolean = false
): TestElement {

    scrollToEdgeCommand(
        command = "scrollToRightEdge",
        direction = ScrollDirection.Right,
        maxLoopCount = maxLoopCount,
        flick = flick,
        startMarginRatio = startMarginRatio,
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
    startMarginRatio: Double = testContext.scrollHorizontalMarginRatio,
    repeat: Int = testContext.scrollToEdgeBoost,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
    flick: Boolean = true,
    maxLoopCount: Int = testContext.scrollMaxCount,
    edgeSelector: String? = null,
    imageCompare: Boolean = false
): TestElement {

    scrollToEdgeCommand(
        command = "scrollToLeftEdge",
        direction = ScrollDirection.Left,
        maxLoopCount = maxLoopCount,
        flick = flick,
        startMarginRatio = startMarginRatio,
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
    maxLoopCount: Int = testContext.scrollMaxCount,
    direction: ScrollDirection,
    flick: Boolean = false,
    durationSeconds: Double =
        if (flick) testContext.flickDurationSeconds
        else testContext.swipeDurationSeconds,
    startMarginRatio: Double =
        if (direction.isDown || direction.isUp) testContext.scrollVerticalMarginRatio
        else testContext.scrollHorizontalMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
    edgeSelector: String? = null,
    imageCompare: Boolean = false,
    scrollFunc: (() -> Unit)? = null,
    actionFunc: (() -> Boolean)? = null
): TestElement {

    val ms = Measure()

    try {
        return doUntilScrollStopCore(
            scrollFunc = scrollFunc,
            direction = direction,
            durationSeconds = durationSeconds,
            startMarginRatio = startMarginRatio,
            imageCompare = imageCompare,
            edgeSelector = edgeSelector,
            actionFunc = actionFunc,
            maxLoopCount = maxLoopCount,
            repeat = repeat,
            intervalSeconds = intervalSeconds
        )
    } finally {
        ms.end()
    }
}

private fun TestDrive.doUntilScrollStopCore(
    scrollFunc: (() -> Unit)?,
    direction: ScrollDirection,
    durationSeconds: Double,
    startMarginRatio: Double,
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
                scrollDown(durationSeconds = durationSeconds, startMarginRatio = startMarginRatio)
            } else if (direction.isUp) {
                scrollUp(durationSeconds = durationSeconds, startMarginRatio = startMarginRatio)
            } else if (direction.isRight) {
                scrollRight(durationSeconds = durationSeconds, startMarginRatio = startMarginRatio)
            } else if (direction.isLeft) {
                scrollLeft(durationSeconds = durationSeconds, startMarginRatio = startMarginRatio)
            }
        }
        ms.end()
    }

    var lastSerialized = scrollFrame.descendantsInBounds.serialize()
    var lastCroppedImage =
        if (imageCompare) scrollFrame.cropImage(save = false).lastCropInfo?.croppedImage else null

    fun isEndOfScroll(): Boolean {

        val ms = Measure()
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

            return if (imageCompare || testContext.useCache.not()) {
                val croppedImage = rootElement.cropImage(save = false).lastCropInfo?.croppedImage
                val result = croppedImage.isSame(lastCroppedImage)
                lastCroppedImage = croppedImage
                result
            } else {
                val serialized = sourceXml //innerElements.serialize()
                val result = serialized == lastSerialized
                lastSerialized = serialized
                result
            }
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
        val original = CodeExecutionContext.isScrolling
        try {
            CodeExecutionContext.isScrolling = true

            for (i in 1..maxLoopCount) {
                for (j in 1..repeat) {
                    scroll()
                }
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
            CodeExecutionContext.isScrolling = original
            ms.end()
        }
    }

    return lastElement
}

internal fun TestDrive.edgeElementFound(expressions: List<String>): Boolean {

    for (expression in expressions) {
        val e = TestDriver.select(expression = expression, throwsException = false, waitSeconds = 0.0)
        if (e.isFound && e.bounds.isIncludedIn(e.getScrollableElement().bounds)) {
            TestLog.info("edge element found. ($expression)")
            return true
        } else {
            TestLog.info("Finding edge element of '$expression'")
        }
    }
    return false
}

private fun List<TestElement>.serialize(): String {

    val list = this.map { it.serializeForEndOfScroll() }.sortedBy { it }
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

    val testElement = getThisOrRootElement()
    val command = "withScrollDown"
    val message = message(id = command)
    val context = TestDriverCommandContext(testElement)
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

    val testElement = getThisOrRootElement()
    val command = "withScrollUp"
    val message = message(id = command)
    val context = TestDriverCommandContext(testElement)
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

    val testElement = getThisOrRootElement()
    val command = "withScrollRight"
    val message = message(id = command)
    val context = TestDriverCommandContext(testElement)
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

    val testElement = getThisOrRootElement()
    val command = "withScrollLeft"
    val message = message(id = command)
    val context = TestDriverCommandContext(testElement)
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
 * suppressWithScroll
 */
fun TestDrive.suppressWithScroll(
    proc: () -> Unit
): TestElement {

    val testElement = getThisOrRootElement()
    val command = "suppressWithScroll"
    val message = message(id = command)
    val context = TestDriverCommandContext(testElement)
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
    direction: ScrollDirection = ScrollDirection.Down,
    startMarginRatio: Double =
        if (direction.isDown || direction.isUp) testContext.scrollVerticalMarginRatio
        else testContext.scrollHorizontalMarginRatio,
    maxScrollTimes: Int = testContext.scrollMaxCount,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    endSelector: String? = null,
    imageCompare: Boolean = false
): TestElement {

    val testElement = getThisOrRootElement()

    val command = "scanElements"
    val message = message(id = command, arg1 = direction.toString())

    val context = TestDriverCommandContext(testElement)
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

            val r = getScrollingInfo(direction = direction, marginRatio = startMarginRatio)
            if (r.hasError) {
                TestLog.trace("no scrollable element found.")
                return@useCache
            }

            doUntilScrollStop(
                repeat = 1,
                maxLoopCount = maxScrollTimes,
                direction = direction,
                durationSeconds = durationSeconds,
                startMarginRatio = startMarginRatio,
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

private class ScrollingInfo(
    val errorMessage: String,
    val scrollableBounds: Bounds,
    val viewPort: Bounds,
    val direction: ScrollDirection,
    val marginRatio: Double,
) {
    val hasError: Boolean
        get() {
            return errorMessage.isBlank().not()
        }

    val leftEdge: Int
        get() {
            return max(scrollableBounds.left, viewPort.left)
        }

    val rightEdge: Int
        get() {
            return min(scrollableBounds.right, viewPort.right)
        }

    val topEdge: Int
        get() {
            return max(scrollableBounds.top, viewPort.top)
        }

    val bottomEdge: Int
        get() {
            return min(scrollableBounds.bottom, viewPort.bottom)
        }

    val adjustedScrollableBounds: Bounds
        get() {
            return Bounds(
                left = leftEdge,
                top = topEdge,
                width = rightEdge - leftEdge + 1,
                height = bottomEdge - topEdge + 1
            )
        }

    val leftMargin: Int
        get() {
            return if (direction.isLeft)
                (adjustedScrollableBounds.width * marginRatio).toInt()
            else 0
        }

    val rightMargin: Int
        get() {
            return if (direction.isRight)
                (adjustedScrollableBounds.width * marginRatio).toInt()
            else 0
        }

    val topMargin: Int
        get() {
            return if (direction.isUp)
                (adjustedScrollableBounds.height * marginRatio).toInt()
            else 0
        }

    val bottomMargin: Int
        get() {
            return if (direction.isDown)
                (adjustedScrollableBounds.height * marginRatio).toInt()
            else 0
        }

    val startY: Int
        get() {
            return when (direction) {
                ScrollDirection.Down -> bottomEdge - bottomMargin
                ScrollDirection.Up -> topEdge + topMargin
                else -> adjustedScrollableBounds.centerY
            }
        }

    val endY: Int
        get() {
            return when (direction) {
                ScrollDirection.Down -> topEdge
                ScrollDirection.Up -> bottomEdge
                else -> adjustedScrollableBounds.centerY
            }
        }

    val startX: Int
        get() {
            return when (direction) {
                ScrollDirection.Right -> rightEdge - rightMargin
                ScrollDirection.Left -> leftEdge + leftMargin
                else -> adjustedScrollableBounds.centerX
            }
        }

    val endX: Int
        get() {
            return when (direction) {
                ScrollDirection.Right -> leftEdge
                ScrollDirection.Left -> rightEdge
                else -> adjustedScrollableBounds.centerX
            }
        }
}

private fun TestDrive.getScrollingInfo(
    direction: ScrollDirection,
    marginRatio: Double =
        if (direction.isDown || direction.isUp) testContext.scrollVerticalMarginRatio
        else testContext.scrollHorizontalMarginRatio
): ScrollingInfo {

    val scrollableTarget = getScrollableElement()
    val r = ScrollingInfo(
        errorMessage = "",
        scrollableBounds = scrollableTarget.bounds,
        viewPort = viewport,
        direction = direction,
        marginRatio = marginRatio
    )
    return r
}
