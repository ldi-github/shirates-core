package shirates.core.vision.driver

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.*
import shirates.core.driver.commandextension.SwipeContext
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.commandextension.swipePointToPointCore
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Message.message
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.rect
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.branchextension.lastScreenshotImage

/**
 * tap
 */
fun VisionDrive.tap(
    x: Int,
    y: Int,
    holdSeconds: Double = testContext.tapHoldSeconds,
    repeat: Int = 1,
): VisionDrive {

    val testElement = getThisOrLastVisionElement()

    val command = "tap"
    val message = message(id = command, subject = "($x,$y)")

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = testElement.subject) {

        val bounds = lastScreenshotImage!!.rect.toBoundsWithRatio()

        val sc = SwipeContext(
            swipeFrame = bounds,
            viewport = bounds,
            startX = x,
            startY = y,
            endX = x,
            endY = y,
            durationSeconds = holdSeconds,
            repeat = repeat,
        )
        testDrive.swipePointToPointCore(swipeContext = sc)
        CodeExecutionContext.lastScreenshotImage = null
        CodeExecutionContext.lastScreenshotName = ""
    }

//    return refreshLastElement()
    return this
}

/**
 * tap
 */
fun VisionDrive.tap(
    holdSeconds: Double = testContext.tapHoldSeconds
): VisionElement {

    val tappedElement = getThisOrLastVisionElement()

    val command = "tap"
    val message = message(id = command, subject = tappedElement.subject)

    val context = TestDriverCommandContext(null)
    var v = tappedElement
    context.execOperateCommand(command = command, message = message, subject = tappedElement.subject) {
//        e = tappedElement.tapCore(holdSeconds = holdSeconds, tapMethod = tapMethod)

        println("tap ${v.bounds.centerX}, ${v.bounds.centerY}")
        v.tap(x = v.bounds.centerX, y = v.bounds.centerY, holdSeconds = holdSeconds)
    }

    lastVisionElement = v
    return lastVisionElement
}

private fun VisionDrive.tapWithScrollCommandCore(
    expression: String,
    command: String,
    direction: ScrollDirection,
    rect: Rectangle,
    scrollDurationSeconds: Double,
    scrollStartMarginRatio: Double,
    scrollEndMarginRatio: Double,
    scrollIntervalSeconds: Double,
    scrollMaxCount: Int,
    holdSeconds: Double,
): VisionElement {

    val selector = getSelector(expression = expression)
    val message = message(id = command, subject = "$selector")
    var v = VisionElement.emptyElement
    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {

        v = detectWithScroll(
            selector = selector,
            rect = rect,
            direction = direction,
            durationSeconds = scrollDurationSeconds,
            startMarginRatio = scrollStartMarginRatio,
            endMarginRatio = scrollEndMarginRatio,
            intervalSeconds = scrollIntervalSeconds,
            scrollMaxCount = scrollMaxCount,
            swipeToCenter = false,
        )
        TestDriver.autoScreenshot(force = testContext.onExecOperateCommand)
        v = v.tap(holdSeconds = holdSeconds)
    }
    return v
}

/**
 * tapWithScrollDown
 */
fun VisionDrive.tapWithScrollDown(
    expression: String,
    rect: Rectangle = lastScreenshotImage!!.rect,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    holdSeconds: Double = testContext.tapHoldSeconds,
): VisionElement {

    val command = "tapWithScrollDown"
    val direction = ScrollDirection.Down

    val v = tapWithScrollCommandCore(
        expression = expression,
        command = command,
        direction = direction,
        rect = rect,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollMaxCount = scrollMaxCount,
        holdSeconds = holdSeconds,
    )

    return v
}

/**
 * tapWithScrollUp
 */
fun VisionDrive.tapWithScrollUp(
    expression: String,
    rect: Rectangle = lastScreenshotImage!!.rect,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    holdSeconds: Double = testContext.tapHoldSeconds,
): VisionElement {

    val command = "tapWithScrollUp"
    val direction = ScrollDirection.Up

    val v = tapWithScrollCommandCore(
        expression = expression,
        command = command,
        direction = direction,
        rect = rect,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollMaxCount = scrollMaxCount,
        holdSeconds = holdSeconds,
    )

    return v
}

/**
 * tapWithScrollRight
 */
fun VisionDrive.tapWithScrollRight(
    expression: String,
    rect: Rectangle = lastScreenshotImage!!.rect,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    holdSeconds: Double = testContext.tapHoldSeconds,
): VisionElement {

    val command = "tapWithScrollRight"
    val direction = ScrollDirection.Right

    val v = tapWithScrollCommandCore(
        expression = expression,
        command = command,
        direction = direction,
        rect = rect,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollMaxCount = scrollMaxCount,
        holdSeconds = holdSeconds,
    )

    return v
}

/**
 * tapWithScrollLeft
 */
fun VisionDrive.tapWithScrollLeft(
    expression: String,
    rect: Rectangle = lastScreenshotImage!!.rect,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    holdSeconds: Double = testContext.tapHoldSeconds,
): VisionElement {

    val command = "tapWithScrollLeft"
    val direction = ScrollDirection.Left

    val v = tapWithScrollCommandCore(
        expression = expression,
        command = command,
        direction = direction,
        rect = rect,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollMaxCount = scrollMaxCount,
        holdSeconds = holdSeconds,
    )

    return v
}

/**
 * tapCenterOfScreen
 */
fun VisionDrive.tapCenterOfScreen(
    holdSeconds: Double = testContext.tapHoldSeconds,
    repeat: Int = 1,
): VisionElement {

    val command = "tapCenterOfScreen"
    val message = message(id = command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {

        val bounds = lastScreenshotImage!!.rect.toBoundsWithRatio()
        tap(x = bounds.centerX, y = bounds.centerY, holdSeconds = holdSeconds, repeat = repeat)
    }

    return lastElement
}

/**
 * tapTopOfScreen
 */
fun VisionDrive.tapTopOfScreen(
    margin: Int = 20,
    holdSeconds: Double = testContext.tapHoldSeconds,
    repeat: Int = 1,
): VisionElement {

    val command = "tapTopOfScreen"
    val message = message(id = command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {

        val bounds = lastScreenshotImage!!.rect.toBoundsWithRatio()
        tap(
            x = bounds.centerX,
            y = (PropertiesManager.statBarHeight + margin),
            holdSeconds = holdSeconds,
            repeat = repeat,
        )
    }

    return lastElement
}

/**
 * tapCenterOf
 */
fun VisionDrive.tapCenterOf(
    expression: String,
    holdSeconds: Double = testContext.tapHoldSeconds,
    repeat: Int = 1,
): VisionElement {

    val testElement = detect(expression = expression)

    val command = "tapCenterOf"
    val message = message(id = command, subject = testElement.subject)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {

        val bounds = testElement.bounds
        tap(x = bounds.centerX, y = bounds.centerY, holdSeconds = holdSeconds, repeat = repeat)
    }

    return lastElement
}