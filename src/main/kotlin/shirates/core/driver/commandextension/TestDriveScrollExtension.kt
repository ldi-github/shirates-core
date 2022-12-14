package shirates.core.driver.commandextension

import shirates.core.Const
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.*
import shirates.core.driver.TestDriver.lastElement
import shirates.core.driver.TestMode.isAndroid
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Message.message
import shirates.core.logging.ScanRecord
import shirates.core.logging.TestLog
import shirates.core.utility.image.isSame


internal fun TestDrive?.getScrollableElementsInDescendants(): List<TestElement> {

    val testElement = getTestElement()

    if (isAndroid) {
        return testElement.descendants.filter { it.isScrollable }
    } else {
        return testElement.descendants.filter { it.isScrollable && it.isVisible }
    }
}

internal fun TestDrive?.getScrollableElementsInAncestors(): List<TestElement> {

    val testElement = getTestElement()

    if (isAndroid) {
        return testElement.ancestors.filter { it.isScrollable }
    } else {
        return testElement.ancestors.filter { it.isScrollable && it.isVisible }
    }
}

internal fun TestDrive?.getScrollableTarget(): TestElement {

    val testElement = getTestElement()
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
val TestDrive?.hasScrollable: Boolean
    get() {
        return this.scrollFrame.isScrollable
    }

private fun TestDrive?.scrollCommand(
    command: String,
    direction: ScrollDirection,
    startMarginRatio: Double,
    swipeAction: (ScrollingInfo) -> Unit
) {
    val testElement = getTestElement()

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
fun TestDrive?.scrollDown(
    durationSeconds: Double = testContext.swipeDurationSeconds,
    startMarginRatio: Double = testContext.scrollVerticalMarginRatio
): TestElement {

    scrollCommand(
        command = "scrollDown",
        direction = ScrollDirection.Down,
        startMarginRatio = startMarginRatio
    ) { scrollingInfo ->
        val b = scrollingInfo.scrollableFrame.bounds
        swipePointToPointCore(
            SwipeContext(
                swipeFrame = b,
                startX = b.centerX,
                startY = b.bottom - scrollingInfo.margin,
                endX = b.centerX,
                endY = b.top,
                durationSeconds = durationSeconds,
            )
        )
    }

    return lastElement
}

/**
 * scrollUp
 */
fun TestDrive?.scrollUp(
    durationSeconds: Double = testContext.swipeDurationSeconds,
    startMarginRatio: Double = testContext.scrollVerticalMarginRatio
): TestElement {

    scrollCommand(
        command = "scrollUp",
        direction = ScrollDirection.Up,
        startMarginRatio = startMarginRatio
    ) { scrollingInfo ->
        val b = scrollingInfo.scrollableFrame.bounds
        swipePointToPointCore(
            SwipeContext(
                swipeFrame = b,
                startX = b.centerX,
                startY = b.top + scrollingInfo.margin,
                endX = b.centerX,
                endY = b.bottom,
                durationSeconds = durationSeconds
            )
        )
    }

    return lastElement
}

/**
 * scrollRight
 */
fun TestDrive?.scrollRight(
    durationSeconds: Double = testContext.swipeDurationSeconds,
    startMarginRatio: Double = testContext.scrollHorizontalMarginRatio
): TestElement {

    scrollCommand(
        command = "scrollRight",
        direction = ScrollDirection.Right,
        startMarginRatio = startMarginRatio
    ) { scrollingInfo ->
        val b = scrollingInfo.scrollableFrame.bounds
        swipePointToPointCore(
            SwipeContext(
                swipeFrame = b,
                startX = b.right - scrollingInfo.margin,
                startY = b.centerY,
                endX = b.left,
                endY = b.centerY,
                swipeOffsetY = 0,
                durationSeconds = durationSeconds
            )
        )
    }

    return lastElement
}

/**
 * scrollLeft
 */
fun TestDrive?.scrollLeft(
    durationSeconds: Double = testContext.swipeDurationSeconds,
    startMarginRatio: Double = testContext.scrollHorizontalMarginRatio
): TestElement {

    scrollCommand(
        command = "scrollLeft",
        direction = ScrollDirection.Left,
        startMarginRatio = startMarginRatio
    ) { scrollingInfo ->
        val b = scrollingInfo.scrollableFrame.bounds
        swipePointToPointCore(
            SwipeContext(
                swipeFrame = b,
                startX = b.left + scrollingInfo.margin,
                startY = b.centerY,
                endX = b.right,
                endY = b.centerY,
                swipeOffsetY = 0,
                durationSeconds = durationSeconds
            )
        )
    }

    return lastElement
}

private fun TestDrive?.scrollToDirectionCommand(
    command: String,
    maxLoopCount: Int,
    direction: ScrollDirection,
    flick: Boolean,
    startMarginRatio: Double,
    repeat: Int,
    edgeSelector: String?,
    imageCompare: Boolean
) {
    val testElement = getTestElement()

    val message = message(id = command)
    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {

        doUntilScrollStop(
            maxLoopCount = maxLoopCount,
            direction = direction,
            flick = flick,
            startMarginRatio = startMarginRatio,
            repeat = repeat,
            edgeSelector = edgeSelector,
            imageCompare = imageCompare
        )

        TestDriver.invalidateCache()
    }
}

/**
 * scrollToBottom
 */
fun TestDrive?.scrollToBottom(
    startMarginRatio: Double = testContext.scrollVerticalMarginRatio,
    repeat: Int = 2,
    flick: Boolean = true,
    maxLoopCount: Int = shirates.core.Const.SCROLL_MAX_COUNT,
    edgeSelector: String? = null,
    imageCompare: Boolean = false
): TestElement {

    scrollToDirectionCommand(
        command = "scrollToBottom",
        direction = ScrollDirection.Down,
        maxLoopCount = maxLoopCount,
        flick = flick,
        startMarginRatio = startMarginRatio,
        repeat = repeat,
        edgeSelector = edgeSelector,
        imageCompare = imageCompare
    )

    return lastElement
}

/**
 * scrollToTop
 */
fun TestDrive?.scrollToTop(
    startMarginRatio: Double = testContext.scrollVerticalMarginRatio,
    repeat: Int = 2,
    flick: Boolean = true,
    maxLoopCount: Int = shirates.core.Const.SCROLL_MAX_COUNT,
    edgeSelector: String? = null,
    imageCompare: Boolean = false
): TestElement {

    scrollToDirectionCommand(
        command = "scrollToTop",
        direction = ScrollDirection.Up,
        maxLoopCount = maxLoopCount,
        flick = flick,
        startMarginRatio = startMarginRatio,
        repeat = repeat,
        edgeSelector = edgeSelector,
        imageCompare = imageCompare
    )

    return lastElement
}

/**
 * scrollToRightEdge
 */
fun TestDrive?.scrollToRightEdge(
    startMarginRatio: Double = testContext.scrollHorizontalMarginRatio,
    repeat: Int = 2,
    flick: Boolean = true,
    maxLoopCount: Int = shirates.core.Const.SCROLL_MAX_COUNT,
    edgeSelector: String? = null,
    imageCompare: Boolean = false
): TestElement {

    scrollToDirectionCommand(
        command = "scrollToRightEdge",
        direction = ScrollDirection.Right,
        maxLoopCount = maxLoopCount,
        flick = flick,
        startMarginRatio = startMarginRatio,
        repeat = repeat,
        edgeSelector = edgeSelector,
        imageCompare = imageCompare
    )

    return lastElement
}

/**
 * scrollToLeftEdge
 */
fun TestDrive?.scrollToLeftEdge(
    startMarginRatio: Double = testContext.scrollHorizontalMarginRatio,
    repeat: Int = 2,
    flick: Boolean = true,
    maxLoopCount: Int = shirates.core.Const.SCROLL_MAX_COUNT,
    edgeSelector: String? = null,
    imageCompare: Boolean = false
): TestElement {

    scrollToDirectionCommand(
        command = "scrollToLeftEdge",
        direction = ScrollDirection.Left,
        maxLoopCount = maxLoopCount,
        flick = flick,
        startMarginRatio = startMarginRatio,
        repeat = repeat,
        edgeSelector = edgeSelector,
        imageCompare = imageCompare
    )

    return lastElement
}

/**
 * doUntilScrollStop
 */
fun TestDrive?.doUntilScrollStop(
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
    edgeSelector: String? = null,
    imageCompare: Boolean = false,
    scrollFunc: (() -> Unit)? = null,
    actionFunc: (() -> Boolean)? = null
): TestElement {

    val scroll = scrollFunc ?: {
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
    }

    var lastSerialized = scrollFrame.descendantsInBounds.serialize()
    var lastCroppedImage =
        if (imageCompare) scrollFrame.cropImage(save = false).lastCropInfo?.croppedImage else null

    fun isEndOfScroll(): Boolean {

        if (imageCompare) {
            val croppedImage = rootElement.cropImage(save = false).lastCropInfo?.croppedImage
            val result = croppedImage.isSame(lastCroppedImage)
            lastCroppedImage = croppedImage
            return result
        }

        if (edgeSelector != null) {
            val result = edgeElementFound(mutableListOf(edgeSelector))
            return result
        }

        val endElements = TestDriver.screenInfo.scrollInfo.endElements.toMutableList()
        if (edgeElementFound(endElements)) {
            return true
        }

        val serialized = sourceXml //innerElements.serialize()
        val result = serialized == lastSerialized
        lastSerialized = serialized
        return result
    }

    if (TestDriver.isInitialized) {
        val original = CodeExecutionContext.isScrolling
        try {
            CodeExecutionContext.isScrolling = true

            for (i in 1..maxLoopCount) {
                for (j in 1..repeat) {
                    scroll()
                }
                TestDriver.refreshCache()

                if (actionFunc != null) {
                    val found = actionFunc()
                    if (found) {
                        return lastElement
                    }
                }

                val endOfScroll = isEndOfScroll()
                TestLog.info("endOfScroll=$endOfScroll", log = PropertiesManager.enableSyncLog)
                if (endOfScroll) {
                    break
                }
            }
        } finally {
            CodeExecutionContext.isScrolling = original
        }
    }

    return lastElement
}

internal fun TestDrive?.edgeElementFound(expressions: List<String>): Boolean {

    for (expression in expressions) {
        val e = TestElementCache.select(expression = expression, throwsException = false)
        if (e.isFound && e.bounds.isIncludedIn(e.getScrollableTarget().bounds)) {
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
fun TestDrive?.withScrollDown(
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    proc: () -> Unit
): TestElement {

    val testElement = getTestElement()
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
fun TestDrive?.withScrollUp(
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    proc: () -> Unit
): TestElement {

    val testElement = getTestElement()
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
fun TestDrive?.withScrollRight(
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    proc: () -> Unit
): TestElement {

    val testElement = getTestElement()
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
fun TestDrive?.withScrollLeft(
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    proc: () -> Unit
): TestElement {

    val testElement = getTestElement()
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
fun TestDrive?.suppressWithScroll(
    proc: () -> Unit
): TestElement {

    val testElement = getTestElement()
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
fun TestDrive?.scanElements(
    direction: ScrollDirection = ScrollDirection.Down,
    startMarginRatio: Double =
        if (direction.isDown || direction.isUp) testContext.scrollVerticalMarginRatio
        else testContext.scrollHorizontalMarginRatio,
    maxScrollTimes: Int = Const.SCROLL_MAX_COUNT,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    endSelector: String? = null,
    imageCompare: Boolean = false
): TestElement {

    val testElement = getTestElement()

    val command = "scanElements"
    val message = message(id = command, arg1 = direction.toString())

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
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
            return@execOperateCommand
        }

        doUntilScrollStop(
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
    TestLog.trace("scanElements completed.(pageCount=${TestElementCache.scanResults.count()})")

    return lastElement
}

private data class ScrollingInfo(
    val errorMessage: String,
    val scrollableFrame: TestElement,
    val direction: ScrollDirection,
    val marginRatio: Double,
) {
    val hasError: Boolean
        get() {
            return errorMessage.isBlank().not()
        }

    val margin: Int
        get() {
            val length = if (direction.isDown || direction.isUp) scrollableFrame.bounds.height
            else scrollableFrame.bounds.width
            return (length * marginRatio).toInt()
        }
}

private fun TestDrive?.getScrollingInfo(
    direction: ScrollDirection,
    marginRatio: Double =
        if (direction.isDown || direction.isUp) testContext.scrollVerticalMarginRatio
        else testContext.scrollHorizontalMarginRatio
): ScrollingInfo {

    val testElement = getTestElement()

    val scrollableTarget = testElement.getScrollableTarget()
    val r = ScrollingInfo(
        errorMessage = "",
        scrollableFrame = scrollableTarget,
        direction = direction,
        marginRatio = marginRatio
    )
    return r
}
