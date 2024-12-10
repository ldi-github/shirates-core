package shirates.core.vision.driver

import shirates.core.Const
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.*
import shirates.core.driver.commandextension.suppressHandler
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Measure
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.rect
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.branchextension.lastScreenshotImage


private fun VisionDrive.scrollCommand(
    command: String,
    rect: Rectangle,
    direction: ScrollDirection = CodeExecutionContext.scrollDirection ?: ScrollDirection.Down,
    startMarginRatio: Double = CodeExecutionContext.scrollStartMarginRatio,
    endMarginRatio: Double = CodeExecutionContext.scrollEndMarginRatio,
    swipeAction: (ScrollingInfo) -> Unit
) {
    val message = message(id = command)
    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {

        val r = getScrollingInfo(
            rect = rect,
            direction = direction,
            startMarginRatio = startMarginRatio,
            endMarginRatio = endMarginRatio
        )
        TestLog.info("scrollableRect: $rect")
        swipeAction(r)
        TestDriver.autoScreenshot()
    }
}

/**
 * scrollDown
 */
fun VisionDrive.scrollDown(
    rect: Rectangle = lastScreenshotImage!!.rect,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    intervalSeconds: Double = Const.SWIPE_INTERVAL_SECONDS,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    repeat: Int = 1,
): VisionElement {

    scrollCommand(
        rect = rect,
        command = "scrollDown",
        direction = ScrollDirection.Down,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio
    ) { s ->
        swipePointToPointCore(
            SwipeContext(
                swipeFrame = s.adjustedScrollableBounds,
                viewport = s.adjustedScrollableBounds,
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
fun VisionDrive.scrollUp(
    rect: Rectangle = lastScreenshotImage!!.rect,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    intervalSeconds: Double = Const.SWIPE_INTERVAL_SECONDS,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    repeat: Int = 1,
): VisionElement {

    scrollCommand(
        rect = rect,
        command = "scrollUp",
        direction = ScrollDirection.Up,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio
    ) { s ->
        swipePointToPointCore(
            SwipeContext(
                swipeFrame = s.adjustedScrollableBounds,
                viewport = s.adjustedScrollableBounds,
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
fun VisionDrive.scrollRight(
    rect: Rectangle = lastScreenshotImage!!.rect,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    intervalSeconds: Double = Const.SWIPE_INTERVAL_SECONDS,
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    repeat: Int = 1,
): VisionElement {

    scrollCommand(
        rect = rect,
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
fun VisionDrive.scrollLeft(
    rect: Rectangle = lastScreenshotImage!!.rect,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    intervalSeconds: Double = Const.SWIPE_INTERVAL_SECONDS,
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    repeat: Int = 1,
): VisionElement {

    scrollCommand(
        rect = rect,
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

internal fun VisionDrive.getScrollingInfo(
    rect: Rectangle = lastScreenshotImage!!.rect,
    direction: ScrollDirection = ScrollDirection.None,
    startMarginRatio: Double = testContext.getScrollStartMarginRatio(direction),
    endMarginRatio: Double = testContext.getScrollEndMarginRatio(direction),
): ScrollingInfo {

    val r = ScrollingInfo(
        errorMessage = "",
        bounds = rect.toBoundsWithRatio(),
        viewport = viewBounds,
        direction = direction,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio,
//        headerBottom = headerBottom,
//        footerTop = footerTop
    )
    return r
}

/**
 * doUntilScrollStop
 */
fun VisionDrive.doUntilScrollStop(
    rect: Rectangle = lastScreenshotImage!!.rect,
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
): VisionElement {

    val ms = Measure()

    val originalIsScrolling = CodeExecutionContext.isScrolling
    try {
        CodeExecutionContext.isScrolling = true
        return doUntilScrollStopCore(
            scrollFunc = scrollFunc,
            direction = direction,
            rect = rect,
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

internal fun VisionDrive.doUntilScrollStopCore(
    scrollFunc: (() -> Unit)?,
    direction: ScrollDirection,
    rect: Rectangle,
    durationSeconds: Double,
    intervalSeconds: Double,
    startMarginRatio: Double,
    endMarginRatio: Double,
    maxLoopCount: Int,
    edgeSelector: String?,
    repeat: Int,
    actionFunc: (() -> Boolean)?
): VisionElement {
    val scroll = scrollFunc ?: {
        val ms = Measure()
        testDrive.suppressHandler {
            if (direction.isDown) {
                scrollDown(
                    rect = rect,
                    durationSeconds = durationSeconds,
                    intervalSeconds = intervalSeconds,
                    startMarginRatio = startMarginRatio,
                    endMarginRatio = endMarginRatio,
                    repeat = repeat
                )
            } else if (direction.isUp) {
                scrollUp(
                    rect = rect,
                    durationSeconds = durationSeconds,
                    intervalSeconds = intervalSeconds,
                    startMarginRatio = startMarginRatio,
                    endMarginRatio = endMarginRatio,
                    repeat = repeat
                )
            } else if (direction.isRight) {
                scrollRight(
                    rect = rect,
                    durationSeconds = durationSeconds,
                    intervalSeconds = intervalSeconds,
                    startMarginRatio = startMarginRatio,
                    endMarginRatio = endMarginRatio,
                    repeat = repeat
                )
            } else if (direction.isLeft) {
                scrollLeft(
                    rect = rect,
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
            return false

        } finally {
            ms.end()
        }
    }

    if (TestDriver.isInitialized) {
//        TestDriver.refreshCache()
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
//                TestDriver.refreshCache()

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

    return lastVisionElement
}

internal fun VisionDrive.edgeElementFound(expressions: List<String>): Boolean {

    for (expression in expressions) {
        val v =
            detect(expression = expression, allowScroll = false, throwsException = false, waitSeconds = 0.0)
        if (v.isFound) {
            TestLog.info("edge element found. ($expression)")
            return true
        } else {
            TestLog.info("Finding edge element of '$expression'")
        }
    }
    return false
}

/**
 * withScrollDown
 */
fun VisionDrive.withScrollDown(
    rect: Rectangle = lastScreenshotImage!!.rect,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    scrollToEdgeBoost: Int = testContext.scrollToEdgeBoost,
    proc: () -> Unit
): VisionElement {

    val command = "withScrollDown"
    val message = message(id = command)
    val context = TestDriverCommandContext(null)
    context.execWithScroll(
        command = command,
        scrollDirection = ScrollDirection.Down,
        bounds = rect.toBoundsWithRatio(),
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
fun VisionDrive.withScrollUp(
    rect: Rectangle = lastScreenshotImage!!.rect,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    scrollToEdgeBoost: Int = testContext.scrollToEdgeBoost,
    proc: () -> Unit
): VisionElement {

    val command = "withScrollUp"
    val message = message(id = command)
    val context = TestDriverCommandContext(null)
    context.execWithScroll(
        command = command,
        scrollDirection = ScrollDirection.Up,
        bounds = rect.toBoundsWithRatio(),
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
fun VisionDrive.withScrollRight(
    rect: Rectangle = lastScreenshotImage!!.rect,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    scrollToEdgeBoost: Int = testContext.scrollToEdgeBoost,
    proc: () -> Unit
): VisionElement {

    val command = "withScrollRight"
    val message = message(id = command)
    val context = TestDriverCommandContext(null)
    context.execWithScroll(
        command = command,
        scrollDirection = ScrollDirection.Right,
        bounds = rect.toBoundsWithRatio(),
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
fun VisionDrive.withScrollLeft(
    rect: Rectangle = lastScreenshotImage!!.rect,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    scrollToEdgeBoost: Int = testContext.scrollToEdgeBoost,
    proc: () -> Unit
): VisionElement {

    val command = "withScrollLeft"
    val message = message(id = command)
    val context = TestDriverCommandContext(null)
    context.execWithScroll(
        command = command,
        scrollDirection = ScrollDirection.Left,
        bounds = rect.toBoundsWithRatio(),
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
fun VisionDrive.withoutScroll(
    proc: () -> Unit
): VisionElement {

    val command = "withoutScroll"
    val message = message(id = command)
    val context = TestDriverCommandContext(null)
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
