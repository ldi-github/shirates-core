package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestDriverException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.image.rect
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.branchextension.lastScreenshotImage
import shirates.core.vision.driver.doUntilTrue
import shirates.core.vision.driver.lastElement
import shirates.core.vision.driver.silent

/**
 * tap
 */
fun VisionDrive.tap(
    expression: String,
    useCache: Boolean = testContext.useCache,
    remove: String? = null,
    language: String = PropertiesManager.logLanguage,
    allowScroll: Boolean? = null,
    waitSeconds: Double = testContext.syncWaitSeconds,
    intervalSeconds: Double = testContext.syncIntervalSeconds,
    holdSeconds: Double = TestDriver.testContext.tapHoldSeconds,
    swipeToSafePosition: Boolean = true,
    waitForElementFocused: Boolean = false,
): VisionElement {

    if (useCache) {
        var e = TestElement.emptyElement
        useCache {
            e = testDrive.tap(
                expression = expression,
                holdSeconds = holdSeconds,
            )
        }
        if (waitForElementFocused) {
            waitForElementFocused()
        }
        return e.toVisionElement()
    }

    if (CodeExecutionContext.isInCell && this is VisionElement) {
        throw NotImplementedError()
    }

    val sel = getSelector(expression = expression)

    val command = "tap"
    val message = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    var v = VisionElement.emptyElement
    context.execOperateCommand(command = command, message = message, subject = "$sel") {

        if (swipeToSafePosition) {
            v = detectCoreWithSwipeToSafePosition(
                selector = sel,
                remove = remove,
                language = language,
                waitSeconds = waitSeconds,
                intervalSeconds = intervalSeconds,
                allowScroll = allowScroll,
                throwsException = true,
            )
        } else {
            v = detectCore(
                selector = sel,
                remove = remove,
                language = language,
                waitSeconds = waitSeconds,
                intervalSeconds = intervalSeconds,
                allowScroll = null,
                throwsException = true,
            )
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
    if (waitForElementFocused) {
        waitForElementFocused()
    }

    return v
}

internal fun VisionDrive.waitForElementFocused(
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    throwOnError: Boolean = true,
): VisionElement {

    if (TestMode.isNoLoadRun) {
        return VisionElement.emptyElement
    }

    var v = VisionElement.emptyElement
    doUntilTrue(
        waitSeconds = waitSeconds,
        throwOnFinally = false
    ) {
        v = getFocusedElement()
        v.isFound
    }
    if (v.isFound.not() && throwOnError) {
        throw TestDriverException(message = "Focused element not found.")
    }
    return v
}

internal fun VisionDrive.detectWithAdjustingPosition(
    selector: Selector,
    remove: String? = null,
    language: String = PropertiesManager.logLanguage,
    waitSeconds: Double = testContext.syncWaitSeconds,
    intervalSeconds: Double = testContext.syncIntervalSeconds,
): VisionElement {

    fun getElement(): VisionElement {
        return detectCore(
            selector = selector,
            remove = remove,
            language = language,
            waitSeconds = waitSeconds,
            intervalSeconds = intervalSeconds,
            allowScroll = null,
            throwsException = true
        )
    }

    var v = getElement()

    if (CodeExecutionContext.withScroll == true && CodeExecutionContext.scrollDirection == ScrollDirection.Down &&
        screenRect.bottom * 0.8 < v.rect.top
    ) {
        silent {
            v.swipeVerticalTo(endY = (screenRect.toBoundsWithRatio().height * 0.2).toInt())
        }
        v = getElement()
    }

    return v
}

/**
 * tap
 */
fun VisionDrive.tap(
    x: Int,
    y: Int,
    offsetX: Int = 1,
    offsetY: Int = 1,
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
            endX = x + offsetX,
            endY = y + offsetY,
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

        if (PropertiesManager.enableTapElementImageLog) {
            val fileName = "${TestLog.nextLineNo}_tap_${tappedElement.subject}_${tappedElement.bounds}"
            tappedElement.saveImage(fileName = fileName)
        }
        v.tap(x = v.bounds.centerX, y = v.bounds.centerY, holdSeconds = holdSeconds)
    }

    lastElement = v.refresh()
    return lastElement
}

private fun VisionDrive.tapWithScrollCommandCore(
    expression: String,
    remove: String?,
    language: String,
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

        v = detectWithScrollCore(
            selector = selector,
            remove = remove,
            language = language,
            direction = direction,
            scrollDurationSeconds = scrollDurationSeconds,
            startMarginRatio = scrollStartMarginRatio,
            endMarginRatio = scrollEndMarginRatio,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollMaxCount = scrollMaxCount,
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
    useCache: Boolean = testContext.useCache,
    remove: String? = null,
    language: String = PropertiesManager.logLanguage,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    holdSeconds: Double = testContext.tapHoldSeconds,
): VisionElement {

    if (useCache) {
        var e = TestElement.emptyElement
        useCache {
            e = testDrive.tapWithScrollDown(
                expression = expression,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
                holdSeconds = holdSeconds,
            )
        }
        return e.toVisionElement()
    }

    val command = "tapWithScrollDown"
    val direction = ScrollDirection.Down

    val v = tapWithScrollCommandCore(
        expression = expression,
        remove = remove,
        language = language,
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
    useCache: Boolean = testContext.useCache,
    remove: String? = null,
    language: String = PropertiesManager.logLanguage,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    holdSeconds: Double = testContext.tapHoldSeconds,
): VisionElement {

    if (useCache) {
        var e = TestElement.emptyElement
        useCache {
            e = testDrive.tapWithScrollUp(
                expression = expression,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
                holdSeconds = holdSeconds,
            )
        }
        return e.toVisionElement()
    }

    val command = "tapWithScrollUp"
    val direction = ScrollDirection.Up

    val v = tapWithScrollCommandCore(
        expression = expression,
        remove = remove,
        language = language,
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
    useCache: Boolean = testContext.useCache,
    remove: String? = null,
    language: String = PropertiesManager.logLanguage,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    holdSeconds: Double = testContext.tapHoldSeconds,
): VisionElement {

    if (useCache) {
        var e = TestElement.emptyElement
        useCache {
            e = testDrive.tapWithScrollRight(
                expression = expression,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
                holdSeconds = holdSeconds,
            )
        }
        return e.toVisionElement()
    }

    val command = "tapWithScrollRight"
    val direction = ScrollDirection.Right

    val v = tapWithScrollCommandCore(
        expression = expression,
        remove = remove,
        language = language,
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
    useCache: Boolean = testContext.useCache,
    remove: String? = null,
    language: String = PropertiesManager.logLanguage,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    holdSeconds: Double = testContext.tapHoldSeconds,
): VisionElement {

    if (useCache) {
        var e = TestElement.emptyElement
        useCache {
            e = testDrive.tapWithScrollLeft(
                expression = expression,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
                holdSeconds = holdSeconds,
            )
        }
        return e.toVisionElement()
    }

    val command = "tapWithScrollLeft"
    val direction = ScrollDirection.Left

    val v = tapWithScrollCommandCore(
        expression = expression,
        remove = remove,
        language = language,
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

/**
 * tapBelow
 */
fun VisionDrive.tapBelow(
    expression: String? = null,
    horizontalMargin: Int = PropertiesManager.segmentMarginHorizontal,
    verticalMargin: Int = PropertiesManager.segmentMarginVertical,
    useCache: Boolean = testContext.useCache,
    remove: String? = null,
    language: String = PropertiesManager.logLanguage,
    allowScroll: Boolean? = null,
    waitSeconds: Double = testContext.syncWaitSeconds,
    intervalSeconds: Double = testContext.syncIntervalSeconds,
    holdSeconds: Double = TestDriver.testContext.tapHoldSeconds,
    swipeToSafePosition: Boolean = true,
): VisionElement {

    if (expression == null) {
        var v = getThisOrIt().belowItem()
        v = v.tap(holdSeconds = holdSeconds)
        return v
    }

    val sel = getSelector(expression = expression)

    val command = "tapBelow"
    val message = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    var v = VisionElement.emptyElement
    context.execOperateCommand(command = command, message = message, subject = "$sel") {

        val labelElement = detect(
            expression = expression,
            useCache = useCache,
            remove = remove,
            language = language,
            allowScroll = allowScroll,
            swipeToSafePosition = swipeToSafePosition,
            throwsException = true,
            waitSeconds = waitSeconds,
            intervalSeconds = intervalSeconds,
        )
        v = labelElement.belowItem(
            segmentMarginHorizontal = horizontalMargin,
            segmentMarginVertical = verticalMargin,
        )
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
 * tapRight
 */
fun VisionDrive.tapRight(
    expression: String? = null,
    horizontalMargin: Int = PropertiesManager.segmentMarginHorizontal,
    verticalMargin: Int = PropertiesManager.segmentMarginVertical,
    useCache: Boolean = testContext.useCache,
    remove: String? = null,
    language: String = PropertiesManager.logLanguage,
    allowScroll: Boolean? = null,
    waitSeconds: Double = testContext.syncWaitSeconds,
    intervalSeconds: Double = testContext.syncIntervalSeconds,
    holdSeconds: Double = TestDriver.testContext.tapHoldSeconds,
    swipeToSafePosition: Boolean = true,
): VisionElement {

    if (expression == null) {
        var v = getThisOrIt().rightItem()
        v = v.tap(holdSeconds = holdSeconds)
        return v
    }

    val sel = getSelector(expression = expression)

    val command = "tapRight"
    val message = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    var v = VisionElement.emptyElement
    context.execOperateCommand(command = command, message = message, subject = "$sel") {

        val labelElement = detect(
            expression = expression,
            useCache = useCache,
            remove = remove,
            language = language,
            allowScroll = allowScroll,
            swipeToSafePosition = swipeToSafePosition,
            throwsException = true,
            waitSeconds = waitSeconds,
            intervalSeconds = intervalSeconds,
        )
        v = labelElement.rightItem(
            segmentMarginHhorizontal = horizontalMargin,
            segmentMarginVertical = verticalMargin,
        )
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
