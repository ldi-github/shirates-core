package shirates.core.vision.driver.commandextension

import org.openqa.selenium.InvalidElementStateException
import org.openqa.selenium.interactions.Pause
import org.openqa.selenium.interactions.PointerInput
import shirates.core.Const
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.*
import shirates.core.driver.TestDriver.appiumDriver
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.image.rect
import shirates.core.utility.load.CpuLoadService
import shirates.core.utility.time.StopWatch
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.branchextension.lastScreenshotImage
import shirates.core.vision.driver.lastElement
import java.time.Duration
import kotlin.math.max
import kotlin.math.min

/**
 * swipePointToPoint
 */
fun VisionDrive.swipePointToPoint(
    startX: Int,
    startY: Int,
    endX: Int,
    endY: Int,
    withOffset: Boolean = false,
    offsetY: Int = PropertiesManager.swipeOffsetY,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    intervalSeconds: Double = Const.SWIPE_INTERVAL_SECONDS,
    repeat: Int = 1,
): VisionElement {

    val sc = SwipeContext(
        swipeFrame = lastScreenshotImage!!.rect.toBoundsWithRatio(),
        viewport = lastScreenshotImage!!.rect.toBoundsWithRatio(),
        startX = startX,
        startY = startY,
        endX = endX,
        endY = endY,
        scrollDurationSeconds = durationSeconds,
        repeat = repeat,
        scrollIntervalSeconds = intervalSeconds
    )
    if (withOffset) {
        sc.endY += offsetY
    }

    val command = "swipePointToPoint"
    val message = message(id = command, from = "(${startX},${startY})", to = "(${endX},${endY})")

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(
        command = command,
        message = message,
        arg1 = "(${sc.startX},${sc.startY})",
        arg2 = "(${sc.endX},${sc.endY})"
    ) {
        if (sc.startX == sc.endX && sc.startY == sc.endY) {
            return@execOperateCommand
        }
        swipePointToPointCore(swipeContext = sc)
    }
    val thisObject = getThisOrIt()
    val sel = thisObject.selector
    if (sel != null) {
        val v = detect(expression = sel.toString(), throwsException = false)
        lastElement = v
    }

    return lastElement
}


internal class SwipeContext(
    var swipeFrame: Bounds,
    var viewport: Bounds,
    var startX: Int,
    var startY: Int,
    var endX: Int,
    var endY: Int,
    val safeMode: Boolean = true,
    val scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    val scrollIntervalSeconds: Double = Const.SWIPE_INTERVAL_SECONDS,
    val repeat: Int = 1,
) {
    init {
        if (safeMode) {
            val leftEdge = max(swipeFrame.left, viewport.left)
            val rightEdge = min(swipeFrame.right, viewport.right)
            val topEdge = max(swipeFrame.top, viewport.top)
            val bottomEdge = min(swipeFrame.bottom, viewport.bottom)

            if (startX < leftEdge) {
                startX = leftEdge
            }
            if (startX > rightEdge - 1) {
                startX = rightEdge - 1
            }
            if (startY < topEdge) {
                startY = topEdge
            }
            if (startY > bottomEdge - 1) {
                startY = bottomEdge - 1
            }
            if (endX > rightEdge - 1) {
                endX = rightEdge - 1
            }
            if (endX < leftEdge) {
                endX = leftEdge
            }
            if (endY > bottomEdge - 1) {
                endY = bottomEdge - 1
            }
            if (endY < topEdge) {
                endY = topEdge
            }
        }
    }

}

internal fun VisionDrive.swipePointToPointCore(
    swipeContext: SwipeContext,
): VisionElement {

    CodeExecutionContext.setScreenDirty()

    fun swipeFunc() {

        invalidateScreen()

        val sc = swipeContext
        val finger = PointerInput(PointerInput.Kind.TOUCH, "finger")
        val sequence = org.openqa.selenium.interactions.Sequence(finger, 0)

        sequence.addAction(
            finger.createPointerMove(
                Duration.ofMillis(0),
                PointerInput.Origin.viewport(),
                sc.startX,
                sc.startY
            )
        )
        sequence.addAction(
            finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg())
        )
        sequence.addAction(
            Pause(finger, Duration.ofMillis(0))
        )
        sequence.addAction(
            finger.createPointerMove(
                Duration.ofMillis((sc.scrollDurationSeconds * 1000).toLong()),
                PointerInput.Origin.viewport(), sc.endX, sc.endY
            )
        )
        sequence.addAction(
            finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg())
        )
        try {
            appiumDriver.perform(mutableListOf(sequence))
        } catch (t: InvalidElementStateException) {
            TestLog.trace(t.message ?: t.stackTraceToString())
            //  https://github.com/appium/java-client/issues/2045
        }

        if (testContext.onScrolling) {
            screenshot()
        }
    }

    CpuLoadService.waitForCpuLoadUnder()

    for (i in 1..swipeContext.repeat) {
        if (swipeContext.repeat > 1) {
            TestLog.trace("$i")
            if (swipeContext.scrollIntervalSeconds > 0.0) {
                Thread.sleep((swipeContext.scrollIntervalSeconds * 1000).toLong())
            }
        }
        invalidateScreen()
        swipeFunc()
    }

    return lastElement
}

///**
// * swipePointToPoint
// *
// * https://stackoverflow.com/questions/57550682/how-to-scroll-up-down-in-appium-android
// */
//fun VisionDrive.swipePointToPoint(
//    startX: Int,
//    startY: Int,
//    endX: Int,
//    endY: Int,
//    withOffset: Boolean = false,
//    offsetY: Int = PropertiesManager.swipeOffsetY,
//    durationSeconds: Int,
//    repeat: Int = 1,
//): VisionElement {
//
//    return swipePointToPoint(
//        startPixelX = startX,
//        startPixelY = startY,
//        endPixelX = endX,
//        endPixelY = endY,
//        withOffset = withOffset,
//        offsetY = offsetY,
//        durationSeconds = durationSeconds.toDouble(),
//        repeat = repeat,
//    )
//}

/**
 * swipeCenterToTop
 */
fun VisionDrive.swipeCenterToTop(
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
): VisionElement {

    val command = "swipeCenterToTop"
    val message = message(id = command)
    val sw = StopWatch(command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val b = CodeExecutionContext.workingRegionRect.toBoundsWithRatio()
        swipePointToPoint(
            b.centerX,
            b.centerY,
            b.centerX,
            b.top,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
        )
    }
    sw.stop()
    return lastElement
}

/**
 * flickCenterToTop
 */
fun VisionDrive.flickCenterToTop(
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
): VisionElement {

    val command = "flickCenterToTop"
    val message = message(id = command)
    val sw = StopWatch(command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        swipeCenterToTop(
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
        )
    }
    sw.stop()
    return lastElement
}

/**
 * swipeCenterToBottom
 */
fun VisionDrive.swipeCenterToBottom(
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
): VisionElement {

    val command = "swipeCenterToBottom"
    val message = message(id = command)
    val sw = StopWatch(command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val b = CodeExecutionContext.workingRegionRect.toBoundsWithRatio()
        swipePointToPoint(
            b.centerX,
            b.centerY,
            b.centerX,
            b.bottom,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
        )
    }
    sw.stop()
    return lastElement
}

/**
 * flickCenterToBottom
 */
fun VisionDrive.flickCenterToBottom(
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
): VisionElement {

    val command = "flickCenterToBottom"
    val message = message(id = command)
    val sw = StopWatch(command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        swipeCenterToBottom(
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
        )
    }
    sw.stop()
    return lastElement
}

/**
 * swipeCenterToLeft
 */
fun VisionDrive.swipeCenterToLeft(
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
): VisionElement {

    val command = "swipeCenterToLeft"
    val message = message(id = command)
    val sw = StopWatch(command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val b = CodeExecutionContext.workingRegionRect.toBoundsWithRatio()
        swipePointToPoint(
            b.centerX,
            b.centerY,
            b.left,
            b.centerY,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
        )
    }
    sw.stop()
    return lastElement
}

/**
 * flickCenterToLeft
 */
fun VisionDrive.flickCenterToLeft(
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
): VisionElement {

    val command = "flickCenterToLeft"
    val message = message(id = command)
    val sw = StopWatch(command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        swipeCenterToLeft(
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
        )
    }
    sw.stop()
    return lastElement
}

/**
 * swipeCenterToRight
 */
fun VisionDrive.swipeCenterToRight(
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
): VisionElement {

    val command = "swipeCenterToRight"
    val message = message(id = command)
    val sw = StopWatch(command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val b = CodeExecutionContext.workingRegionRect.toBoundsWithRatio()
        swipePointToPoint(
            b.centerX,
            b.centerY,
            b.right,
            b.centerY,
            durationSeconds = durationSeconds,
            intervalSeconds = intervalSeconds,
            repeat = repeat,
        )
    }
    sw.stop()
    return lastElement
}

/**
 * flickCenterToRight
 */
fun VisionDrive.flickCenterToRight(
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
): VisionElement {

    val command = "flickCenterToRight"
    val message = message(id = command)
    val sw = StopWatch(command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        swipeCenterToRight(
            durationSeconds = durationSeconds,
            intervalSeconds = intervalSeconds,
            repeat = repeat,
        )
    }
    sw.stop()
    return lastElement
}

/**
 * swipeLeftToRight
 */
fun VisionDrive.swipeLeftToRight(
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
): VisionElement {

    val command = "swipeLeftToRight"
    val message = message(id = command)
    val sw = StopWatch(command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val b = CodeExecutionContext.workingRegionRect.toBoundsWithRatio()
        val startX = (b.right * startMarginRatio).toInt()
        swipePointToPoint(
            startX = startX,
            startY = b.centerY,
            endX = b.right,
            endY = b.centerY,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
        )
    }
    sw.stop()
    return lastElement
}

/**
 * flickLeftToRight
 */
fun VisionDrive.flickLeftToRight(
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
): VisionElement {

    val command = "flickLeftToRight"
    val message = message(id = command)
    val sw = StopWatch(command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        swipeLeftToRight(
            startMarginRatio = startMarginRatio,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
        )
    }
    sw.stop()
    return lastElement
}

/**
 * swipeRightToLeft
 */
fun VisionDrive.swipeRightToLeft(
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
): VisionElement {

    val command = "swipeRightToLeft"
    val message = message(id = command)
    val sw = StopWatch(command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val b = CodeExecutionContext.workingRegionRect.toBoundsWithRatio()
        val startX = (b.right * (1 - startMarginRatio)).toInt()
        swipePointToPoint(
            startX = startX,
            startY = b.centerY,
            endX = b.left,
            endY = b.centerY,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
        )
    }
    sw.stop()
    return lastElement
}

/**
 * flickRightToLeft
 */
fun VisionDrive.flickRightToLeft(
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
): VisionElement {

    val command = "flickRightToLeft"
    val message = message(id = command)
    val sw = StopWatch(command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        swipeRightToLeft(
            startMarginRatio = startMarginRatio,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
        )
    }
    sw.stop()
    return lastElement
}

/**
 * swipeBottomToTop
 */
fun VisionDrive.swipeBottomToTop(
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
): VisionElement {

    val command = "swipeBottomToTop"
    val message = message(id = command)
    val sw = StopWatch(command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val b = CodeExecutionContext.workingRegionRect.toBoundsWithRatio()
        val startY = (b.bottom * (1 - startMarginRatio)).toInt()

        swipePointToPoint(
            startX = b.centerX,
            startY = startY,
            endX = b.centerX,
            endY = b.top,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
        )
    }
    sw.stop()
    return lastElement
}

/**
 * flickBottomToTop
 */
fun VisionDrive.flickBottomToTop(
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
): VisionElement {

    val command = "flickBottomToTop"
    val message = message(id = command)
    val sw = StopWatch(command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        swipeBottomToTop(
            startMarginRatio = startMarginRatio,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
        )
    }
    sw.stop()
    return lastElement
}

/**
 * flickAndGoDown
 */
fun VisionDrive.flickAndGoDown(
    durationSeconds: Double = testContext.flickDurationSeconds,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
): VisionElement {

    val command = "flickAndGoDown"
    val message = message(id = command)
    val sw = StopWatch(command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val originalOnScrolling = testContext.onScrolling
        try {
            testContext.onScrolling = false
            scrollDown(
                scrollDurationSeconds = durationSeconds,
                startMarginRatio = startMarginRatio,
                endMarginRatio = endMarginRatio,
                repeat = repeat,
                scrollIntervalSeconds = intervalSeconds
            )
        } finally {
            testContext.onScrolling = originalOnScrolling
        }
    }
    sw.stop()
    return lastElement
}

/**
 * flickAndGoDownTurbo
 */
fun VisionDrive.flickAndGoDownTurbo(
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
): VisionElement {

    return flickAndGoDown(
        durationSeconds = 0.1,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio,
        repeat = repeat,
        intervalSeconds = intervalSeconds,
    )
}

/**
 * flickAndGoRight
 */
fun VisionDrive.flickAndGoRight(
    durationSeconds: Double = testContext.flickDurationSeconds,
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
): VisionElement {

    val command = "flickAndGoRight"
    val message = message(id = command)
    val sw = StopWatch(command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val originalOnScrolling = testContext.onScrolling
        val scrollVisionElement = getScrollLineElement(expression = null)
        val originalScrollVisionElement = CodeExecutionContext.scrollVisionElement
        try {
            testContext.onScrolling = false
            CodeExecutionContext.scrollVisionElement = scrollVisionElement
            scrollRight(
                scrollDurationSeconds = durationSeconds,
                startMarginRatio = startMarginRatio,
                endMarginRatio = endMarginRatio,
                repeat = repeat,
                scrollIntervalSeconds = intervalSeconds
            )
        } finally {
            testContext.onScrolling = originalOnScrolling
            CodeExecutionContext.scrollVisionElement = originalScrollVisionElement
        }
    }
    sw.stop()
    return lastElement
}

/**
 * flickAndGoLeft
 */
fun VisionDrive.flickAndGoLeft(
    durationSeconds: Double = testContext.flickDurationSeconds,
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS
): VisionElement {

    val command = "flickAndGoLeft"
    val message = message(id = command)
    val sw = StopWatch(command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val originalOnScrolling = testContext.onScrolling
        try {
            testContext.onScrolling = false
            scrollLeft(
                scrollDurationSeconds = durationSeconds,
                startMarginRatio = startMarginRatio,
                endMarginRatio = endMarginRatio,
                repeat = repeat,
                scrollIntervalSeconds = intervalSeconds
            )
        } finally {
            testContext.onScrolling = originalOnScrolling
        }
    }
    sw.stop()
    return lastElement
}

/**
 * swipeTopToBottom
 */
fun VisionDrive.swipeTopToBottom(
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
): VisionElement {

    val command = "swipeTopToBottom"
    val message = message(id = command)
    val sw = StopWatch(command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val b = CodeExecutionContext.workingRegionRect.toBoundsWithRatio()
        val startY = (b.bottom * startMarginRatio).toInt()

        swipePointToPoint(
            startX = b.centerX,
            startY = startY,
            endX = b.centerX,
            endY = b.bottom,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
        )
    }
    sw.stop()
    return lastElement
}

/**
 * flickAndGoUp
 */
fun VisionDrive.flickAndGoUp(
    durationSeconds: Double = testContext.flickDurationSeconds,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS
): VisionElement {

    val command = "flickAndGoUp"
    val message = message(id = command)
    val sw = StopWatch(command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val originalOnScrolling = testContext.onScrolling
        try {
            testContext.onScrolling = false
            scrollUp(
                scrollDurationSeconds = durationSeconds,
                startMarginRatio = startMarginRatio,
                endMarginRatio = endMarginRatio,
                repeat = repeat,
                scrollIntervalSeconds = intervalSeconds
            )
        } finally {
            testContext.onScrolling = originalOnScrolling
        }
    }
    sw.stop()
    return lastElement
}

/**
 * flickAndGoUpTurbo
 */
fun VisionDrive.flickAndGoUpTurbo(
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS
): VisionElement {

    return flickAndGoUp(
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio,
        repeat = repeat,
        intervalSeconds = intervalSeconds
    )
}

/**
 * flickTopToBottom
 */
fun VisionDrive.flickTopToBottom(
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
): VisionElement {

    val command = "flickTopToBottom"
    val message = message(id = command)
    val sw = StopWatch(command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        swipeTopToBottom(
            startMarginRatio = startMarginRatio,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
        )
    }
    sw.stop()
    return lastElement
}

/**
 * swipeElementToElement
 */
fun VisionDrive.swipeElementToElement(
    startElement: VisionElement,
    endElement: VisionElement,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    marginRatio: Double = testContext.swipeMarginRatio,
    adjust: Boolean = false,
    withOffset: Boolean = false,
    offsetY: Int = PropertiesManager.swipeOffsetY,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
): VisionElement {

    val command = "swipeElementToElement"
    val message = message(id = command, subject = startElement.subject, to = endElement.subject)
    val sw = StopWatch(command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(
        command = command,
        message = message,
        subject = startElement.subject,
        arg1 = endElement.subject
    ) {
        val b1 = startElement.bounds
        val b2 = endElement.bounds
        val marginX = ((b2.centerX - b1.centerX) * marginRatio).toInt()
        val marginY = ((b2.centerY - b1.centerY) * marginRatio).toInt()

        if (b1.centerX == b2.centerX && b1.centerY == b2.centerY) {
            return@execOperateCommand
        }

        swipePointToPoint(
            startX = b1.centerX,
            startY = b1.centerY,
            endX = b2.centerX - marginX,
            endY = b2.centerY - marginY,
            withOffset = withOffset,
            offsetY = offsetY,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
        )

        if (adjust) {
            TestDriver.syncCache(force = true)
            val m = TestDriver.findImageOrSelectCore(
                selector = startElement.selector!!,
                allowScroll = false,
                swipeToCenter = false,
                safeElementOnly = false,
                throwsException = false
            )
            if (m.isEmpty.not() && m.bounds.centerX != b2.centerX && m.bounds.centerY != b2.centerY) {
                swipePointToPoint(
                    startX = m.bounds.centerX,
                    startY = m.bounds.centerY,
                    endX = b2.centerX,
                    endY = b2.centerY,
                    withOffset = true,
                    offsetY = offsetY,
                    durationSeconds = durationSeconds,
                )
            }
        }

//        TestDriver.syncCache(force = true)
//        TestDriver.findImageOrSelectCore(
//            selector = startElement.selector!!,
//            swipeToCenter = false,
//            safeElementOnly = false
//        )
    }

    if (TestMode.isSkippingScenario || TestMode.isSkippingCase || TestMode.isManualingScenario || TestMode.isManualingCase)
        lastElement = startElement
    sw.stop()
    return lastElement
}

/**
 * swipeElementToElementAdjust
 */
fun VisionDrive.swipeElementToElementAdjust(
    startElement: VisionElement,
    endElement: VisionElement,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    marginRatio: Double = testContext.swipeMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
): VisionElement {

    return swipeElementToElement(
        startElement = startElement,
        endElement = endElement,
        durationSeconds = durationSeconds,
        marginRatio = marginRatio,
        adjust = true,
        offsetY = PropertiesManager.swipeOffsetY,
        repeat = repeat,
        intervalSeconds = intervalSeconds,
    )
}