package shirates.core.driver.commandextension

import org.openqa.selenium.InvalidElementStateException
import org.openqa.selenium.interactions.Pause
import org.openqa.selenium.interactions.PointerInput
import org.openqa.selenium.interactions.Sequence
import shirates.core.Const
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.*
import shirates.core.driver.TestDriver.appiumDriver
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.load.CpuLoadService
import java.time.Duration
import kotlin.math.max
import kotlin.math.min

/**
 * swipePointToPoint
 *
 * https://stackoverflow.com/questions/57550682/how-to-scroll-up-down-in-appium-android
 */
fun TestDrive.swipePointToPoint(
    startX: Int,
    startY: Int,
    endX: Int,
    endY: Int,
    withOffset: Boolean = false,
    offsetY: Int = PropertiesManager.swipeOffsetY,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    intervalSeconds: Double = Const.SWIPE_INTERVAL_SECONDS,
    repeat: Int = 1,
    safeMode: Boolean = true,
): TestElement {

    val testElement = rootElement

    val sc = SwipeContext(
        swipeFrame = viewBounds,
        viewport = viewBounds,
        startX = startX,
        startY = startY,
        endX = endX,
        endY = endY,
        safeMode = safeMode,
        scrollDurationSeconds = durationSeconds,
        repeat = repeat,
        scrollIntervalSeconds = intervalSeconds
    )
    if (withOffset) {
        sc.endY += offsetY
    }

    val command = "swipePointToPoint"
    val message = message(id = command, from = "(${sc.startX},${sc.startY})", to = "(${sc.endX},${sc.endY})")

    val context = TestDriverCommandContext(testElement)
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

internal fun TestDrive.swipePointToPointCore(
    swipeContext: SwipeContext,
): TestElement {

    fun swipeFunc() {

        val sc = swipeContext
        val finger = PointerInput(PointerInput.Kind.TOUCH, "finger")
        val sequence = Sequence(finger, 0)

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

        TestDriver.invalidateCache()
        if (testContext.onScrolling) {
            TestDriver.autoScreenshot()
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
        swipeFunc()
    }
    TestDriver.autoScreenshot()

    return lastElement
}

/**
 * swipePointToPoint
 *
 * https://stackoverflow.com/questions/57550682/how-to-scroll-up-down-in-appium-android
 */
fun TestDrive.swipePointToPoint(
    startX: Int,
    startY: Int,
    endX: Int,
    endY: Int,
    withOffset: Boolean = false,
    offsetY: Int = PropertiesManager.swipeOffsetY,
    durationSeconds: Int,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    return swipePointToPoint(
        startX = startX,
        startY = startY,
        endX = endX,
        endY = endY,
        withOffset = withOffset,
        offsetY = offsetY,
        durationSeconds = durationSeconds.toDouble(),
        repeat = repeat,
        safeMode = safeMode
    )
}

/**
 * swipeCenterToTop
 */
fun TestDrive.swipeCenterToTop(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
    safeMode: Boolean = true
): TestElement {

    val command = "swipeCenterToTop"
    val message = message(id = command)

    val context = TestDriverCommandContext(this.toTestElement)
    context.execOperateCommand(command = command, message = message) {
        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)
        val b = sc.bounds
        swipePointToPoint(
            b.centerX,
            b.centerY,
            b.centerX,
            b.top,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * flickCenterToTop
 */
fun TestDrive.flickCenterToTop(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
    safeMode: Boolean = true
): TestElement {

    val command = "flickCenterToTop"
    val message = message(id = command)
    val context = TestDriverCommandContext(this.toTestElement)
    context.execOperateCommand(command = command, message = message) {
        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)
        swipeCenterToTop(
            scrollableElement = sc,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * swipeCenterToBottom
 */
fun TestDrive.swipeCenterToBottom(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
    safeMode: Boolean = true
): TestElement {

    val command = "swipeCenterToBottom"
    val message = message(id = command)

    val context = TestDriverCommandContext(this.toTestElement)
    context.execOperateCommand(command = command, message = message) {
        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)
        val b = sc.bounds
        swipePointToPoint(
            b.centerX,
            b.centerY,
            b.centerX,
            b.bottom,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * flickCenterToBottom
 */
fun TestDrive.flickCenterToBottom(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
    safeMode: Boolean = true
): TestElement {

    val command = "flickCenterToBottom"
    val message = message(id = command)

    val context = TestDriverCommandContext(this.toTestElement)
    context.execOperateCommand(command = command, message = message) {
        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)
        swipeCenterToBottom(
            scrollableElement = sc,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * swipeCenterToLeft
 */
fun TestDrive.swipeCenterToLeft(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
    safeMode: Boolean = true
): TestElement {

    val command = "swipeCenterToLeft"
    val message = message(id = command)

    val context = TestDriverCommandContext(this.toTestElement)
    context.execOperateCommand(command = command, message = message) {
        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)
        val b = sc.bounds
        swipePointToPoint(
            b.centerX,
            b.centerY,
            b.left,
            b.centerY,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * flickCenterToLeft
 */
fun TestDrive.flickCenterToLeft(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
    safeMode: Boolean = true
): TestElement {

    val command = "flickCenterToLeft"
    val message = message(id = command)

    val context = TestDriverCommandContext(this.toTestElement)
    context.execOperateCommand(command = command, message = message) {
        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)
        swipeCenterToLeft(
            scrollableElement = sc,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * swipeCenterToRight
 */
fun TestDrive.swipeCenterToRight(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
    safeMode: Boolean = true
): TestElement {

    val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)

    val command = "swipeCenterToRight"
    val message = message(id = command)

    val context = TestDriverCommandContext(this.toTestElement)
    context.execOperateCommand(command = command, message = message) {
        val b = sc.bounds
        swipePointToPoint(
            b.centerX,
            b.centerY,
            b.right,
            b.centerY,
            durationSeconds = durationSeconds,
            intervalSeconds = intervalSeconds,
            repeat = repeat,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * flickCenterToRight
 */
fun TestDrive.flickCenterToRight(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
    safeMode: Boolean = true
): TestElement {

    val command = "flickCenterToRight"
    val message = message(id = command)

    val context = TestDriverCommandContext(this.toTestElement)
    context.execOperateCommand(command = command, message = message) {
        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)
        swipeCenterToRight(
            scrollableElement = sc,
            durationSeconds = durationSeconds,
            intervalSeconds = intervalSeconds,
            repeat = repeat,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * swipeLeftToRight
 */
fun TestDrive.swipeLeftToRight(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
    safeMode: Boolean = true
): TestElement {

    val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)

    val command = "swipeLeftToRight"
    val message = message(id = command)

    val context = TestDriverCommandContext(this.toTestElement)
    context.execOperateCommand(command = command, message = message) {
        val b = sc.bounds
        val startX = (b.right * startMarginRatio).toInt()
        swipePointToPoint(
            startX = startX,
            startY = b.centerY,
            endX = b.right,
            endY = b.centerY,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * flickLeftToRight
 */
fun TestDrive.flickLeftToRight(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
    safeMode: Boolean = true
): TestElement {

    val command = "flickLeftToRight"
    val message = message(id = command)

    val context = TestDriverCommandContext(this.toTestElement)
    context.execOperateCommand(command = command, message = message) {
        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)
        swipeLeftToRight(
            scrollableElement = sc,
            startMarginRatio = startMarginRatio,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * swipeRightToLeft
 */
fun TestDrive.swipeRightToLeft(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
    safeMode: Boolean = true
): TestElement {

    val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)

    val command = "swipeRightToLeft"
    val message = message(id = command)

    val context = TestDriverCommandContext(this.toTestElement)
    context.execOperateCommand(command = command, message = message) {
        val b = sc.bounds
        val startX = (b.right * (1 - startMarginRatio)).toInt()
        swipePointToPoint(
            startX = startX,
            startY = b.centerY,
            endX = b.left,
            endY = b.centerY,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * flickRightToLeft
 */
fun TestDrive.flickRightToLeft(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
    safeMode: Boolean = true
): TestElement {

    val command = "flickRightToLeft"
    val message = message(id = command)

    val context = TestDriverCommandContext(this.toTestElement)
    context.execOperateCommand(command = command, message = message) {
        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)
        swipeRightToLeft(
            scrollableElement = sc,
            startMarginRatio = startMarginRatio,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * swipeBottomToTop
 */
fun TestDrive.swipeBottomToTop(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
    safeMode: Boolean = true
): TestElement {

    val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)

    val command = "swipeBottomToTop"
    val message = message(id = command)

    val context = TestDriverCommandContext(this.toTestElement)
    context.execOperateCommand(command = command, message = message) {
        val b = sc.bounds
        val startY = (b.bottom * (1 - startMarginRatio)).toInt()

        swipePointToPoint(
            startX = b.centerX,
            startY = startY,
            endX = b.centerX,
            endY = b.top,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * flickBottomToTop
 */
fun TestDrive.flickBottomToTop(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
    safeMode: Boolean = true
): TestElement {

    val command = "flickBottomToTop"
    val message = message(id = command)

    val context = TestDriverCommandContext(this.toTestElement)
    context.execOperateCommand(command = command, message = message) {
        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)
        swipeBottomToTop(
            scrollableElement = sc,
            startMarginRatio = startMarginRatio,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * flickAndGoDown
 */
fun TestDrive.flickAndGoDown(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    durationSeconds: Double = testContext.flickDurationSeconds,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
): TestElement {

    val command = "flickAndGoDown"
    val message = message(id = command)

    val context = TestDriverCommandContext(this.toTestElement)
    context.execOperateCommand(command = command, message = message) {
        val originalOnScrolling = testContext.onScrolling
        try {
            testContext.onScrolling = false
            val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)
            scrollDown(
                scrollableElement = sc,
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

    return lastElement
}

/**
 * flickAndGoDownTurbo
 */
fun TestDrive.flickAndGoDownTurbo(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
): TestElement {

    val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)

    return flickAndGoDown(
        scrollableElement = sc,
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
fun TestDrive.flickAndGoRight(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    durationSeconds: Double = testContext.flickDurationSeconds,
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
): TestElement {

    val command = "flickAndGoRight"
    val message = message(id = command)

    val context = TestDriverCommandContext(this.toTestElement)
    context.execOperateCommand(command = command, message = message) {
        val originalOnScrolling = testContext.onScrolling
        try {
            testContext.onScrolling = false
            val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)
            scrollRight(
                scrollableElement = sc,
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

    return lastElement
}

/**
 * flickAndGoLeft
 */
fun TestDrive.flickAndGoLeft(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    durationSeconds: Double = testContext.flickDurationSeconds,
    startMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS
): TestElement {

    val command = "flickAndGoLeft"
    val message = message(id = command)

    val context = TestDriverCommandContext(this.toTestElement)
    context.execOperateCommand(command = command, message = message) {
        val originalOnScrolling = testContext.onScrolling
        try {
            testContext.onScrolling = false
            val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)
            scrollLeft(
                scrollableElement = sc,
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

    return lastElement
}

/**
 * swipeTopToBottom
 */
fun TestDrive.swipeTopToBottom(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
    safeMode: Boolean = true
): TestElement {

    val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)

    val command = "swipeTopToBottom"
    val message = message(id = command)

    val context = TestDriverCommandContext(this.toTestElement)
    context.execOperateCommand(command = command, message = message) {
        val b = sc.bounds
        val startY = (b.bottom * startMarginRatio).toInt()

        swipePointToPoint(
            startX = b.centerX,
            startY = startY,
            endX = b.centerX,
            endY = b.bottom,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * flickAndGoUp
 */
fun TestDrive.flickAndGoUp(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    durationSeconds: Double = testContext.flickDurationSeconds,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS
): TestElement {

    val command = "flickAndGoUp"
    val message = message(id = command)
    val context = TestDriverCommandContext(this.toTestElement)
    context.execOperateCommand(command = command, message = message) {
        val originalOnScrolling = testContext.onScrolling
        try {
            testContext.onScrolling = false
            val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)
            scrollUp(
                scrollableElement = sc,
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

    return lastElement
}

/**
 * flickAndGoUpTurbo
 */
fun TestDrive.flickAndGoUpTurbo(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    endMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS
): TestElement {

    val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)
    return flickAndGoUp(
        scrollableElement = sc,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio,
        repeat = repeat,
        intervalSeconds = intervalSeconds
    )
}


/**
 * flickTopToBottom
 */
fun TestDrive.flickTopToBottom(
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    startMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    intervalSeconds: Double = Const.FLICK_INTERVAL_SECONDS,
    safeMode: Boolean = true
): TestElement {

    val command = "flickTopToBottom"
    val message = message(id = command)
    val context = TestDriverCommandContext(this.toTestElement)
    context.execOperateCommand(command = command, message = message) {
        val sc = scrollableElement ?: getScrollableElement(scrollFrame = scrollFrame)
        swipeTopToBottom(
            scrollableElement = sc,
            startMarginRatio = startMarginRatio,
            durationSeconds = durationSeconds,
            repeat = repeat,
            intervalSeconds = intervalSeconds,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * swipeElementToElement
 */
fun TestDrive.swipeElementToElement(
    startElement: TestElement,
    endElement: TestElement,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    marginRatio: Double = testContext.swipeMarginRatio,
    adjust: Boolean = false,
    withOffset: Boolean = false,
    offsetY: Int = PropertiesManager.swipeOffsetY,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
    safeMode: Boolean = true
): TestElement {

    val testElement = rootElement

    val command = "swipeElementToElement"
    val message = message(id = command, subject = startElement.subject, to = endElement.subject)

    val context = TestDriverCommandContext(testElement)
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
            safeMode = safeMode
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
                    safeMode = safeMode
                )
            }
        }

        TestDriver.syncCache(force = true)
        TestDriver.findImageOrSelectCore(
            selector = startElement.selector!!,
            allowScroll = false,
            swipeToCenter = false,
            safeElementOnly = false
        )
    }

    if (TestMode.isSkippingScenario || TestMode.isSkippingCase || TestMode.isManualingScenario || TestMode.isManualingCase)
        lastElement = startElement
    return lastElement
}

/**
 * swipeElementToElementAdjust
 */
fun TestDrive.swipeElementToElementAdjust(
    startElement: TestElement,
    endElement: TestElement,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    marginRatio: Double = testContext.swipeMarginRatio,
    repeat: Int = 1,
    intervalSeconds: Double = testContext.scrollIntervalSeconds,
    safeMode: Boolean = true
): TestElement {

    return swipeElementToElement(
        startElement = startElement,
        endElement = endElement,
        durationSeconds = durationSeconds,
        marginRatio = marginRatio,
        adjust = true,
        offsetY = PropertiesManager.swipeOffsetY,
        repeat = repeat,
        intervalSeconds = intervalSeconds,
        safeMode = safeMode
    )
}
