package shirates.core.vision.driver.commandextension

import shirates.core.Const
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.*
import shirates.core.exception.TestDriverException
import shirates.core.logging.Measure
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.image.saveImage
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement
import shirates.core.vision.driver.silent


private fun VisionDrive.scrollCommand(
    command: String,
    direction: ScrollDirection = CodeExecutionContext.scrollDirection ?: ScrollDirection.Down,
    startMarginRatio: Double = CodeExecutionContext.scrollStartMarginRatio,
    endMarginRatio: Double = CodeExecutionContext.scrollEndMarginRatio,
    swipeAction: (ScrollingInfo) -> Unit
) {
    val message = message(id = command)
    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {

        val r = getScrollingInfo(
            direction = direction,
            startMarginRatio = startMarginRatio,
            endMarginRatio = endMarginRatio
        )
        if (CodeExecutionContext.shouldOutputLog) {
            TestLog.info("scrollableRect: ${r.bounds}")
        }
        swipeAction(r)
    }
}

/**
 * scrollDown
 */
fun VisionDrive.scrollDown(
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = Const.SWIPE_INTERVAL_SECONDS,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    repeat: Int = 1,
): VisionElement {

    scrollCommand(
        command = "scrollDown",
        direction = ScrollDirection.Down,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio
    ) { s ->
        val scrollElement = CodeExecutionContext.scrollVisionElement ?: rootElement
        swipePointToPointCore(
            SwipeContext(
                swipeFrame = scrollElement.bounds,
                viewport = viewBounds,
                startX = s.startX,
                startY = s.startY,
                endX = s.endX,
                endY = s.endY,
                scrollDurationSeconds = scrollDurationSeconds,
                repeat = repeat,
                scrollIntervalSeconds = scrollIntervalSeconds
            )
        )
    }

    return lastElement
}

/**
 * scrollUp
 */
fun VisionDrive.scrollUp(
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = Const.SWIPE_INTERVAL_SECONDS,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    repeat: Int = 1,
): VisionElement {

    scrollCommand(
        command = "scrollUp",
        direction = ScrollDirection.Up,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio
    ) { s ->
        val scrollElement = CodeExecutionContext.scrollVisionElement ?: rootElement
        swipePointToPointCore(
            SwipeContext(
                swipeFrame = scrollElement.bounds,
                viewport = viewBounds,
                startX = s.startX,
                startY = s.startY,
                endX = s.endX,
                endY = s.endY,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
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
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = Const.SWIPE_INTERVAL_SECONDS,
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    repeat: Int = 1,
): VisionElement {

    scrollCommand(
        command = "scrollRight",
        direction = ScrollDirection.Right,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio
    ) { s ->
        val scrollElement = CodeExecutionContext.scrollVisionElement ?: rootElement
        swipePointToPointCore(
            SwipeContext(
                swipeFrame = scrollElement.bounds,
                viewport = viewBounds,
                startX = s.startX,
                startY = s.startY,
                endX = s.endX,
                endY = s.endY,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
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
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = Const.SWIPE_INTERVAL_SECONDS,
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    repeat: Int = 1,
): VisionElement {

    scrollCommand(
        command = "scrollLeft",
        direction = ScrollDirection.Left,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio
    ) { s ->
        val scrollElement = CodeExecutionContext.scrollVisionElement ?: rootElement
        swipePointToPointCore(
            SwipeContext(
                swipeFrame = scrollElement.bounds,
                viewport = viewBounds,
                startX = s.startX,
                startY = s.startY,
                endX = s.endX,
                endY = s.endY,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
                repeat = repeat,
            )
        )
    }

    return lastElement
}

internal fun VisionDrive.getScrollingInfo(
    direction: ScrollDirection = ScrollDirection.None,
    startMarginRatio: Double = testContext.getScrollStartMarginRatio(direction),
    endMarginRatio: Double = testContext.getScrollEndMarginRatio(direction),
): ScrollingInfo {

    val scrollElement = CodeExecutionContext.scrollVisionElement ?: rootElement
    val r = ScrollingInfo(
        errorMessage = "",
        bounds = scrollElement.bounds,
        viewport = viewBounds,
        direction = direction,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio,
//        headerBottom = headerBottom,
//        footerTop = footerTop
    )
    return r
}

private fun VisionDrive.scrollToEdgeCommand(
    command: String,
    scrollVisionElement: VisionElement = rootElement,
    maxLoopCount: Int,
    direction: ScrollDirection,
    flick: Boolean,
    startMarginRatio: Double = testContext.getScrollStartMarginRatio(direction = direction),
    endMarginRatio: Double = testContext.getScrollEndMarginRatio(direction = direction),
    repeat: Int,
    scrollIntervalSeconds: Double = testContext.getIntervalSeconds(flick = flick),
//    edgeSelector: String?,
) {
    val message = message(id = command)
    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {

        val originalScrollVisionElement = CodeExecutionContext.scrollVisionElement
        try {
            CodeExecutionContext.scrollVisionElement = scrollVisionElement
            doUntilScrollStop(
                maxLoopCount = maxLoopCount,
                direction = direction,
                startMarginRatio = startMarginRatio,
                endMarginRatio = endMarginRatio,
                repeat = repeat,
                scrollIntervalSeconds = scrollIntervalSeconds,
//            edgeSelector = edgeSelector,
            )
        } finally {
            CodeExecutionContext.scrollVisionElement = originalScrollVisionElement
        }

        invalidateScreen()
    }
}

/**
 * scrollToBottom
 */
fun VisionDrive.scrollToBottom(
    expression: String? = null,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    repeat: Int = testContext.scrollToEdgeBoost,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
    flick: Boolean = true,
    maxLoopCount: Int = testContext.scrollMaxCount,
//    edgeSelector: String? = null,
): VisionElement {

    val scrollVisionElement = getScrollColumnElement(
        expression = expression,
    )
    scrollToEdgeCommand(
        command = "scrollToBottom",
        scrollVisionElement = scrollVisionElement,
        direction = ScrollDirection.Down,
        maxLoopCount = maxLoopCount,
        flick = flick,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio,
        repeat = repeat,
        scrollIntervalSeconds = intervalSeconds,
//        edgeSelector = edgeSelector,
    )

    return lastElement
}

/**
 * scrollToTop
 */
fun VisionDrive.scrollToTop(
    expression: String? = null,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    repeat: Int = testContext.scrollToEdgeBoost,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
    flick: Boolean = true,
    maxLoopCount: Int = testContext.scrollMaxCount,
//    edgeSelector: String? = null,
): VisionElement {

    val scrollVisionElement = getScrollColumnElement(
        expression = expression,
    )
    scrollToEdgeCommand(
        command = "scrollToTop",
        scrollVisionElement = scrollVisionElement,
        direction = ScrollDirection.Up,
        maxLoopCount = maxLoopCount,
        flick = flick,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio,
        repeat = repeat,
        scrollIntervalSeconds = intervalSeconds,
//        edgeSelector = edgeSelector,
    )

    return lastElement
}

internal fun VisionDrive.getScrollColumnElement(
    expression: String?,
): VisionElement {
    if (expression != null) {
        return detect(
            expression = expression,
            throwsException = true,
        ).columnRegionElement()
    }
    val thisElement = getThisOrIt()
    if (thisElement.isEmpty.not()) {
        return thisElement.columnRegionElement()
    }
    return rootElement
}

internal fun VisionDrive.getScrollLineElement(
    expression: String?,
): VisionElement {
    if (expression != null) {
        return detect(
            expression = expression,
            throwsException = true,
        ).lineRegionElement()
    }
    val thisElement = getThisOrIt()
    if (thisElement.isEmpty.not()) {
        return thisElement.lineRegionElement()
    }
    return rootElement
}

/**
 * scrollToRightEdge
 */
fun VisionDrive.scrollToRightEdge(
    expression: String? = null,
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    repeat: Int = testContext.scrollToEdgeBoost,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    flick: Boolean = true,
    maxLoopCount: Int = testContext.scrollMaxCount,
//    edgeSelector: String? = null,
): VisionElement {

    val scrollVisionElement = getScrollLineElement(
        expression = expression,
    )
    scrollToEdgeCommand(
        command = "scrollToRightEdge",
        scrollVisionElement = scrollVisionElement,
        direction = ScrollDirection.Right,
        maxLoopCount = maxLoopCount,
        flick = flick,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio,
        repeat = repeat,
        scrollIntervalSeconds = scrollIntervalSeconds,
//        edgeSelector = edgeSelector,
    )

    return lastElement
}

/**
 * scrollToLeftEdge
 */
fun VisionDrive.scrollToLeftEdge(
    expression: String? = null,
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    repeat: Int = testContext.scrollToEdgeBoost,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    flick: Boolean = true,
    maxLoopCount: Int = testContext.scrollMaxCount,
//    edgeSelector: String? = null,
): VisionElement {

    val scrollVisionElement = getScrollLineElement(
        expression = expression,
    )
    scrollToEdgeCommand(
        command = "scrollToLeftEdge",
        scrollVisionElement = scrollVisionElement,
        direction = ScrollDirection.Left,
        maxLoopCount = maxLoopCount,
        flick = flick,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio,
        repeat = repeat,
        scrollIntervalSeconds = scrollIntervalSeconds,
//        edgeSelector = edgeSelector,
    )

    return lastElement
}

/**
 * doUntilScrollStop
 */
fun VisionDrive.doUntilScrollStop(
    maxLoopCount: Int = CodeExecutionContext.scrollMaxCount,
    direction: ScrollDirection = CodeExecutionContext.scrollDirection ?: ScrollDirection.Down,
    scrollDurationSeconds: Double = CodeExecutionContext.scrollDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    startMarginRatio: Double = CodeExecutionContext.scrollStartMarginRatio,
    endMarginRatio: Double = CodeExecutionContext.scrollEndMarginRatio,
    repeat: Int = 1,
//    edgeSelector: String? = null,
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
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            startMarginRatio = startMarginRatio,
            endMarginRatio = endMarginRatio,
//            edgeSelector = edgeSelector,
            actionFunc = actionFunc,
            maxLoopCount = maxLoopCount,
            repeat = repeat,
        )
    } finally {
        CodeExecutionContext.isScrolling = originalIsScrolling
        ms.end()
    }
}

internal fun VisionDrive.doUntilScrollStopCore(
    scrollFunc: (() -> Unit)?,
    direction: ScrollDirection,
    scrollDurationSeconds: Double,
    scrollIntervalSeconds: Double,
    startMarginRatio: Double,
    endMarginRatio: Double,
    maxLoopCount: Int,
//    edgeSelector: String?,
    repeat: Int,
    actionFunc: (() -> Boolean)?
): VisionElement {
    val scroll = scrollFunc ?: {
        val ms = Measure()
        suppressHandler {
            if (direction.isDown) {
                scrollDown(
                    scrollDurationSeconds = scrollDurationSeconds,
                    scrollIntervalSeconds = scrollIntervalSeconds,
                    startMarginRatio = startMarginRatio,
                    endMarginRatio = endMarginRatio,
                    repeat = repeat
                )
            } else if (direction.isUp) {
                scrollUp(
                    scrollDurationSeconds = scrollDurationSeconds,
                    scrollIntervalSeconds = scrollIntervalSeconds,
                    startMarginRatio = startMarginRatio,
                    endMarginRatio = endMarginRatio,
                    repeat = repeat
                )
            } else if (direction.isRight) {
                scrollRight(
                    scrollDurationSeconds = scrollDurationSeconds,
                    scrollIntervalSeconds = scrollIntervalSeconds,
                    startMarginRatio = startMarginRatio,
                    endMarginRatio = endMarginRatio,
                    repeat = repeat
                )
            } else if (direction.isLeft) {
                scrollLeft(
                    scrollDurationSeconds = scrollDurationSeconds,
                    scrollIntervalSeconds = scrollIntervalSeconds,
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

    if (TestDriver.isInitialized) {
        if (isKeyboardShown) {
            silent {
                hideKeyboard()
            }
        }

        if (actionFunc != null) {
            val result = actionFunc()
            if (result) {
                return lastElement
            }
        }

        val ms = Measure("doUntilScrollStop-loop")
        try {
            val scrollVisionElement = CodeExecutionContext.scrollVisionElement ?: rootElement
            for (i in 1..maxLoopCount) {

                val beforeVisionElement = scrollVisionElement.newVisionElement()
                scroll()
                screenshot()
                CodeExecutionContext.workingRegionElement = CodeExecutionContext.workingRegionElement.newVisionElement()
                val afterVisionElement = scrollVisionElement.newVisionElement()
                val endOfScroll = afterVisionElement.isScrollStopped(beforeVisionElement)
                TestLog.info("endOfScroll=$endOfScroll", log = PropertiesManager.enableSyncLog)
                if (endOfScroll) {
                    CodeExecutionContext.lastScreenshotImage?.saveImage(
                        TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_at_end_of_scroll.png").toString()
                    )
                    break
                }

                if (actionFunc != null) {
                    val result = actionFunc()
                    if (result) {
                        return lastElement
                    }
                }

                if (i < maxLoopCount && scrollIntervalSeconds > 0.0) {
                    Thread.sleep((scrollIntervalSeconds * 1000).toLong())
                }
            }
        } finally {
            ms.end()
        }
    }

    return lastElement
}

internal fun VisionDrive.edgeElementFound(
    expressions: List<String>,
): Boolean {

    for (expression in expressions) {
        val v =
            detect(
                expression = expression,
                allowScroll = false,
                throwsException = false,
            )
        if (v.isFound) {
            TestLog.info("edge element found. ($expression)")
            return true
        } else {
            TestLog.info("Finding edge element of '$expression'")
        }
    }
    return false
}

internal fun VisionDrive.withScroll(
    direction: ScrollDirection,
    scrollVisionElement: VisionElement = rootElement,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    scrollToEdgeBoost: Int = testContext.scrollToEdgeBoost,
    swipeToSafePosition: Boolean,
    proc: () -> Unit
): VisionElement {

    val command = "withScroll${direction}"
    val message = message(id = command)
    val context = TestDriverCommandContext(null)
    context.execWithScroll(
        command = command,
        scrollDirection = direction,
        scrollVisionElement = scrollVisionElement,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        scrollToEdgeBoost = scrollToEdgeBoost,
        swipeToSafePosition = swipeToSafePosition,

        message = message
    ) {
        proc()
    }

    return lastElement
}

/**
 * withScrollDown
 */
fun VisionDrive.withScrollDown(
    expression: String? = null,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    scrollToEdgeBoost: Int = testContext.scrollToEdgeBoost,
    proc: () -> Unit
): VisionElement {

    val scrollVisionElement = getScrollColumnElement(
        expression = expression,
    )
    if (TestMode.isNoLoadRun.not() && scrollVisionElement.isEmpty) {
        throw TestDriverException("Could not find scroll element.")
    }
    return withScroll(
        direction = ScrollDirection.Down,
        scrollVisionElement = scrollVisionElement,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        scrollToEdgeBoost = scrollToEdgeBoost,
        swipeToSafePosition = swipeToSafePosition,
        proc = proc
    )
}

/**
 * withScrollUp
 */
fun VisionDrive.withScrollUp(
    expression: String? = null,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    scrollToEdgeBoost: Int = testContext.scrollToEdgeBoost,
    proc: () -> Unit
): VisionElement {

    val scrollVisionElement = getScrollColumnElement(
        expression = expression,
    )
    if (scrollVisionElement.isEmpty) {
        throw TestDriverException("Could not find scroll element.")
    }
    return withScroll(
        direction = ScrollDirection.Up,
        scrollVisionElement = scrollVisionElement,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        scrollToEdgeBoost = scrollToEdgeBoost,
        swipeToSafePosition = swipeToSafePosition,
        proc = proc
    )
}

/**
 * withScrollRight
 */
fun VisionDrive.withScrollRight(
    expression: String? = null,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    scrollToEdgeBoost: Int = testContext.scrollToEdgeBoost,
    proc: () -> Unit
): VisionElement {

    val scrollVisionElement = getScrollLineElement(
        expression = expression,
    )
    if (scrollVisionElement.isEmpty) {
        throw TestDriverException("Could not find scroll element.")
    }
    return withScroll(
        direction = ScrollDirection.Right,
        scrollVisionElement = scrollVisionElement,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        scrollToEdgeBoost = scrollToEdgeBoost,
        swipeToSafePosition = swipeToSafePosition,
        proc = proc
    )
}

/**
 * withScrollLeft
 */
fun VisionDrive.withScrollLeft(
    expression: String? = null,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    scrollToEdgeBoost: Int = testContext.scrollToEdgeBoost,
    proc: () -> Unit
): VisionElement {

    val scrollVisionElement = getScrollLineElement(
        expression = expression,
    )
    if (scrollVisionElement.isEmpty) {
        throw TestDriverException("Could not find scroll element.")
    }
    return withScroll(
        direction = ScrollDirection.Left,
        scrollVisionElement = scrollVisionElement,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        scrollToEdgeBoost = scrollToEdgeBoost,
        swipeToSafePosition = swipeToSafePosition,
        proc = proc
    )
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

///**
// * scanElements
// */
//fun VisionDrive.scanElements(
//    scrollFrame: String = "",
//    direction: ScrollDirection = ScrollDirection.Down,
//    startMarginRatio: Double = testContext.getScrollStartMarginRatio(direction),
//    endMarginRatio: Double = testContext.getScrollEndMarginRatio(direction),
//    maxScrollTimes: Int = testContext.scrollMaxCount,
//    durationSeconds: Double = testContext.swipeDurationSeconds,
//): VisionElement {
//
//    val command = "scanElements"
//    val message = message(id = command, arg1 = direction.toString())
//
//    val context = TestDriverCommandContext(null)
//    context.execOperateCommand(command = command, message = message) {
//        useCache {
//            val lineNo = TestLog.nextLineNo
//
//            TestElementCache.scanResults.clear()
//            TestDriver.refreshCache()
//            TestElementCache.scanResults.add(
//                ScanRecord(
//                    lineNo = lineNo,
//                    sourceContent = TestElementCache.recognizedJson,
//                    contentType = ScanRecord.ContentType.json,
//                    element = TestElementCache.rootElement
//                )
//            )
//            TestDriver.autoScreenshot()
//
//            doUntilScrollStop(
//                repeat = 1,
//                maxLoopCount = maxScrollTimes,
//                direction = direction,
//                scrollDurationSeconds = durationSeconds,
//                startMarginRatio = startMarginRatio,
//                endMarginRatio = endMarginRatio,
//            ) {
//                val lastJson = TestElementCache.scanResults.last().sourceContent
//                val thisJson = TestElementCache.recognizedJson
//                if (thisJson != lastJson) {
//                    TestElementCache.scanResults.add(
//                        ScanRecord(
//                            lineNo = lineNo,
//                            sourceContent = thisJson,
//                            contentType = ScanRecord.ContentType.json,
//                            element = TestElement.emptyElement
//                        )
//                    )
//                }
//
//                false
//            }
//        }
//    }
//    TestLog.trace("scanElements completed.(pageCount=${TestElementCache.scanResults.count()})")
//
//    return lastElement
//}