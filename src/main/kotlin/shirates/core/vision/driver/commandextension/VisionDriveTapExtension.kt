package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.*
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.commandextension.silent
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Message.message
import shirates.core.utility.image.rect
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.branchextension.lastScreenshotImage
import shirates.core.vision.driver.lastElement

/**
 * tap
 */
fun VisionDrive.tap(
    expression: String,
    removeChars: String? = null,
    language: String = PropertiesManager.logLanguage,
    directAccessCompletion: Boolean = true,
    waitSeconds: Double = testContext.syncWaitSeconds,
    intervalSeconds: Double = testContext.syncIntervalSeconds,
    holdSeconds: Double = TestDriver.testContext.tapHoldSeconds,
): VisionElement {

    if (CodeExecutionContext.isInCell && this is VisionElement) {
        throw NotImplementedError()
    }

    val sel = getSelector(expression = expression)

    val command = "tap"
    val message = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    var v = VisionElement.emptyElement
    context.execOperateCommand(command = command, message = message, subject = "$sel") {

        fun getElement(): VisionElement {
            return detectCore(
                selector = sel,
                removeChars = removeChars,
                language = language,
                directAccessCompletion = directAccessCompletion,
                waitSeconds = waitSeconds,
                intervalSeconds = intervalSeconds,
                allowScroll = null,
                swipeToCenter = false,
                throwsException = true
            )
        }

        v = getElement()

        if (CodeExecutionContext.withScroll == true && CodeExecutionContext.scrollDirection == ScrollDirection.Down) {
            v.swipeVerticalTo(endY = screenRect.toBoundsWithRatio().height / 5)
            v = getElement()
        }

        val tapFunc = {
            silent {
                v = v.tap(holdSeconds = holdSeconds)
            }
        }

        tapFunc()
    }
    if (TestMode.isNoLoadRun) {
        lastElement = v
    }

    return v
}

/**
 * tap
 */
fun VisionDrive.tap(
    x: Int,
    y: Int,
    holdSeconds: Double = testContext.tapHoldSeconds,
    repeat: Int = 1,
): VisionDrive {

    val testElement = getThisOrIt()

    val command = "tap"
    val message = message(id = command, subject = "($x,$y)")

    invalidateScreen()

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = testElement.subject) {

        val bounds = screenRect.toBoundsWithRatio()

        val sc = SwipeContext(
            swipeFrame = bounds,
            viewport = bounds,
            startX = x,
            startY = y,
            endX = x,
            endY = y,
            scrollDurationSeconds = holdSeconds,
            repeat = repeat,
        )
        swipePointToPointCore(swipeContext = sc)
    }

    return this
}

/**
 * tap
 */
fun VisionDrive.tap(
    holdSeconds: Double = testContext.tapHoldSeconds
): VisionElement {

    val tappedElement = getThisOrIt()

    val command = "tap"
    val message = message(id = command, subject = tappedElement.subject)

    val context = TestDriverCommandContext(null)
    val v = tappedElement
    context.execOperateCommand(command = command, message = message, subject = tappedElement.subject) {
//        e = tappedElement.tapCore(holdSeconds = holdSeconds, tapMethod = tapMethod)

        println("tap ${v.bounds.centerX}, ${v.bounds.centerY}")
        v.tap(x = v.bounds.centerX, y = v.bounds.centerY, holdSeconds = holdSeconds)
    }

    lastElement = v
    return lastElement
}

private fun VisionDrive.tapWithScrollCommandCore(
    expression: String,
    removeChars: String?,
    language: String,
    directAccessCompletion: Boolean,
    command: String,
    direction: ScrollDirection,
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
            removeChars = removeChars,
            language = language,
            directAccessCompletion = directAccessCompletion,
            direction = direction,
            scrollDurationSeconds = scrollDurationSeconds,
            startMarginRatio = scrollStartMarginRatio,
            endMarginRatio = scrollEndMarginRatio,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollMaxCount = scrollMaxCount,
            swipeToCenter = false,
            throwsException = true,
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
    removeChars: String? = null,
    language: String = PropertiesManager.logLanguage,
    directAccessCompletion: Boolean = true,
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
        removeChars = removeChars,
        language = language,
        directAccessCompletion = directAccessCompletion,
        command = command,
        direction = direction,
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
    removeChars: String? = null,
    language: String = PropertiesManager.logLanguage,
    directAccessCompletion: Boolean = true,
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
        removeChars = removeChars,
        language = language,
        directAccessCompletion = directAccessCompletion,
        command = command,
        direction = direction,
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
    removeChars: String? = null,
    language: String = PropertiesManager.logLanguage,
    directAccessCompletion: Boolean = true,
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
        removeChars = removeChars,
        language = language,
        directAccessCompletion = directAccessCompletion,
        command = command,
        direction = direction,
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
    removeChars: String? = null,
    language: String = PropertiesManager.logLanguage,
    directAccessCompletion: Boolean = true,
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
        removeChars = removeChars,
        language = language,
        directAccessCompletion = directAccessCompletion,
        command = command,
        direction = direction,
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