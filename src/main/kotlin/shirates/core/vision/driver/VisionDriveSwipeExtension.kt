package shirates.core.vision.driver

import org.openqa.selenium.InvalidElementStateException
import org.openqa.selenium.interactions.Pause
import org.openqa.selenium.interactions.PointerInput
import shirates.core.Const
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.*
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.rect
import shirates.core.utility.load.CpuLoadService
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.branchextension.lastScreenshotImage
import java.time.Duration
import kotlin.math.max
import kotlin.math.min

/**
 * swipePointToPoint
 *
 */
fun VisionDrive.swipePointToPointByPixel(
    startPixelX: Int,
    startPixelY: Int,
    endPixelX: Int,
    endPixelY: Int,
    withOffset: Boolean = false,
    offsetPixelY: Int = PropertiesManager.swipeOffsetY * testContext.boundsToRectRatio,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    intervalSeconds: Double = Const.SWIPE_INTERVAL_SECONDS,
    repeat: Int = 1,
): VisionElement {

    val ratio = testContext.boundsToRectRatio

    val startX = startPixelX / ratio
    val startY = startPixelY / ratio
    val endX = endPixelX + ratio
    val endY = endPixelY + ratio
    val offsetY = offsetPixelY / ratio

    val sc = SwipeContext(
        swipeFrame = lastScreenshotImage!!.rect.toBoundsWithRatio(),
        viewport = lastScreenshotImage!!.rect.toBoundsWithRatio(),
        startX = startX,
        startY = startY,
        endX = endX,
        endY = endY,
        durationSeconds = durationSeconds,
        repeat = repeat,
        intervalSeconds = intervalSeconds
    )
    if (withOffset) {
        sc.endY += offsetY * ratio
    }

    val command = "swipePointToPoint"
    val message = message(id = command, from = "(${startPixelX},${startPixelY})", to = "(${endPixelX},${endPixelY})")

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

    return lastElement
}

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
        durationSeconds = durationSeconds,
        repeat = repeat,
        intervalSeconds = intervalSeconds
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
    val durationSeconds: Double = testContext.swipeDurationSeconds,
    val repeat: Int = 1,
    val intervalSeconds: Double = Const.SWIPE_INTERVAL_SECONDS,
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

    fun swipeFunc() {

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
                Duration.ofMillis((sc.durationSeconds * 1000).toLong()),
                PointerInput.Origin.viewport(), sc.endX, sc.endY
            )
        )
        sequence.addAction(
            finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg())
        )
        try {
            driver.appiumDriver.perform(mutableListOf(sequence))
        } catch (t: InvalidElementStateException) {
            TestLog.trace(t.message ?: t.stackTraceToString())
            //  https://github.com/appium/java-client/issues/2045
        }

//        TestDriver.invalidateCache()
        if (testContext.onScrolling) {
            TestDriver.autoScreenshot()
        }
    }

    CpuLoadService.waitForCpuLoadUnder()

    for (i in 1..swipeContext.repeat) {
        if (swipeContext.repeat > 1) {
            TestLog.trace("$i")
            if (swipeContext.intervalSeconds > 0.0) {
                Thread.sleep((swipeContext.intervalSeconds * 1000).toLong())
            }
        }
        swipeFunc()
    }
    TestDriver.autoScreenshot()

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
    rect: Rectangle = lastScreenshotImage!!.rect,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
): VisionElement {

    val command = "swipeCenterToTop"
    val message = message(id = command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val b = rect.toBoundsWithRatio()
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

    return lastElement
}

/**
 * flickCenterToTop
 */
fun VisionDrive.flickCenterToTop(
    rect: Rectangle = lastScreenshotImage!!.rect,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
): VisionElement {

    val command = "flickCenterToTop"
    val message = message(id = command)
    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        swipeCenterToTop(
            rect = rect,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
        )
    }

    return lastElement
}

/**
 * swipeCenterToBottom
 */
fun VisionDrive.swipeCenterToBottom(
    rect: Rectangle = lastScreenshotImage!!.rect,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
): VisionElement {

    val command = "swipeCenterToBottom"
    val message = message(id = command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val b = rect.toBoundsWithRatio()
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

    return lastElement
}

/**
 * flickCenterToBottom
 */
fun VisionDrive.flickCenterToBottom(
    rect: Rectangle = lastScreenshotImage!!.rect,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
): VisionElement {

    val command = "flickCenterToBottom"
    val message = message(id = command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        swipeCenterToBottom(
            rect = rect,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
        )
    }

    return lastElement
}

/**
 * swipeCenterToLeft
 */
fun VisionDrive.swipeCenterToLeft(
    rect: Rectangle = lastScreenshotImage!!.rect,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
): VisionElement {

    val command = "swipeCenterToLeft"
    val message = message(id = command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val b = rect.toBoundsWithRatio()
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

    return lastElement
}

/**
 * flickCenterToLeft
 */
fun VisionDrive.flickCenterToLeft(
    rect: Rectangle = lastScreenshotImage!!.rect,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
): VisionElement {

    val command = "flickCenterToLeft"
    val message = message(id = command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        swipeCenterToLeft(
            rect = rect,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
        )
    }

    return lastElement
}

/**
 * swipeCenterToRight
 */
fun VisionDrive.swipeCenterToRight(
    rect: Rectangle = lastScreenshotImage!!.rect,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
): VisionElement {

    val command = "swipeCenterToRight"
    val message = message(id = command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val b = rect.toBoundsWithRatio()
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

    return lastElement
}

/**
 * flickCenterToRight
 */
fun VisionDrive.flickCenterToRight(
    rect: Rectangle = lastScreenshotImage!!.rect,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
): VisionElement {

    val command = "flickCenterToRight"
    val message = message(id = command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        swipeCenterToRight(
            rect = rect,
            durationSeconds = durationSeconds,
            intervalSeconds = intervalSeconds,
            repeat = repeat,
        )
    }

    return lastElement
}

/**
 * swipeLeftToRight
 */
fun VisionDrive.swipeLeftToRight(
    rect: Rectangle = lastScreenshotImage!!.rect,
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
): VisionElement {

    val command = "swipeLeftToRight"
    val message = message(id = command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val b = rect.toBoundsWithRatio()
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

    return lastElement
}

/**
 * flickLeftToRight
 */
fun VisionDrive.flickLeftToRight(
    rect: Rectangle = lastScreenshotImage!!.rect,
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
    safeMode: Boolean = true
): VisionElement {

    val command = "flickLeftToRight"
    val message = message(id = command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        swipeLeftToRight(
            rect = rect,
            startMarginRatio = startMarginRatio,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
        )
    }

    return lastElement
}

/**
 * swipeRightToLeft
 */
fun VisionDrive.swipeRightToLeft(
    rect: Rectangle = lastScreenshotImage!!.rect,
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
): VisionElement {

    val command = "swipeRightToLeft"
    val message = message(id = command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val b = rect.toBoundsWithRatio()
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

    return lastElement
}

/**
 * flickRightToLeft
 */
fun VisionDrive.flickRightToLeft(
    rect: Rectangle = lastScreenshotImage!!.rect,
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
): VisionElement {

    val command = "flickRightToLeft"
    val message = message(id = command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        swipeRightToLeft(
            rect = rect,
            startMarginRatio = startMarginRatio,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
        )
    }

    return lastElement
}

/**
 * swipeBottomToTop
 */
fun VisionDrive.swipeBottomToTop(
    rect: Rectangle = lastScreenshotImage!!.rect,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
): VisionElement {

    val command = "swipeBottomToTop"
    val message = message(id = command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val b = rect.toBoundsWithRatio()
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

    return lastElement
}

/**
 * flickBottomToTop
 */
fun VisionDrive.flickBottomToTop(
    rect: Rectangle = lastScreenshotImage!!.rect,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
): VisionElement {

    val command = "flickBottomToTop"
    val message = message(id = command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        swipeBottomToTop(
            rect = rect,
            startMarginRatio = startMarginRatio,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
        )
    }

    return lastElement
}

/**
 * flickAndGoDown
 */
fun VisionDrive.flickAndGoDown(
    rect: Rectangle = lastScreenshotImage!!.rect,
    durationSeconds: Double = testContext.flickDurationSeconds,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
): VisionElement {

    val command = "flickAndGoDown"
    val message = message(id = command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val originalOnScrolling = testContext.onScrolling
        try {
            testContext.onScrolling = false
            scrollDown(
                rect = rect,
                durationSeconds = durationSeconds,
                startMarginRatio = startMarginRatio,
                endMarginRatio = endMarginRatio,
                repeat = repeat,
                intervalSeconds = intervalSeconds
            )
        } finally {
            testContext.onScrolling = originalOnScrolling
        }
    }

    return lastElement
}

/**
 * flickAndGoDownTurbo
 */
fun VisionDrive.flickAndGoDownTurbo(
    rect: Rectangle = lastScreenshotImage!!.rect,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
): VisionElement {

    return flickAndGoDown(
        rect = rect,
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
    rect: Rectangle = lastScreenshotImage!!.rect,
    durationSeconds: Double = testContext.flickDurationSeconds,
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
): VisionElement {

    val command = "flickAndGoRight"
    val message = message(id = command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val originalOnScrolling = testContext.onScrolling
        try {
            testContext.onScrolling = false
            scrollRight(
                rect = rect,
                durationSeconds = durationSeconds,
                startMarginRatio = startMarginRatio,
                endMarginRatio = endMarginRatio,
                repeat = repeat,
                intervalSeconds = intervalSeconds
            )
        } finally {
            testContext.onScrolling = originalOnScrolling
        }
    }

    return lastElement
}

/**
 * flickAndGoLeft
 */
fun VisionDrive.flickAndGoLeft(
    rect: Rectangle = lastScreenshotImage!!.rect,
    durationSeconds: Double = testContext.flickDurationSeconds,
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS
): VisionElement {

    val command = "flickAndGoLeft"
    val message = message(id = command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val originalOnScrolling = testContext.onScrolling
        try {
            testContext.onScrolling = false
            scrollLeft(
                rect = rect,
                durationSeconds = durationSeconds,
                startMarginRatio = startMarginRatio,
                endMarginRatio = endMarginRatio,
                repeat = repeat,
                intervalSeconds = intervalSeconds
            )
        } finally {
            testContext.onScrolling = originalOnScrolling
        }
    }

    return lastElement
}

/**
 * swipeTopToBottom
 */
fun VisionDrive.swipeTopToBottom(
    rect: Rectangle = lastScreenshotImage!!.rect,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
): VisionElement {

    val command = "swipeTopToBottom"
    val message = message(id = command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val b = rect.toBoundsWithRatio()
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

    return lastElement
}

/**
 * flickAndGoUp
 */
fun VisionDrive.flickAndGoUp(
    rect: Rectangle = lastScreenshotImage!!.rect,
    durationSeconds: Double = testContext.flickDurationSeconds,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS
): VisionElement {

    val command = "flickAndGoUp"
    val message = message(id = command)
    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val originalOnScrolling = testContext.onScrolling
        try {
            testContext.onScrolling = false
            scrollUp(
                rect = rect,
                durationSeconds = durationSeconds,
                startMarginRatio = startMarginRatio,
                endMarginRatio = endMarginRatio,
                repeat = repeat,
                intervalSeconds = intervalSeconds
            )
        } finally {
            testContext.onScrolling = originalOnScrolling
        }
    }

    return lastElement
}

/**
 * flickAndGoUpTurbo
 */
fun VisionDrive.flickAndGoUpTurbo(
    rect: Rectangle = lastScreenshotImage!!.rect,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS
): VisionElement {

    return flickAndGoUp(
        rect = rect,
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
    rect: Rectangle = lastScreenshotImage!!.rect,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
): VisionElement {

    val command = "flickTopToBottom"
    val message = message(id = command)
    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        swipeTopToBottom(
            rect = rect,
            startMarginRatio = startMarginRatio,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
        )
    }

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