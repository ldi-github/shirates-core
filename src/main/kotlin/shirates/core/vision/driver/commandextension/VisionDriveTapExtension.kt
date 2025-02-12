package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.commandextension.tap
import shirates.core.driver.commandextension.toVisionElement
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.logging.printWarn
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.image.rect
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.branchextension.lastScreenshotImage
import shirates.core.vision.driver.doUntilTrue
import shirates.core.vision.driver.lastElement
import shirates.core.vision.driver.silent
import shirates.core.vision.driver.wait

/**
 * tap
 */
fun VisionDrive.tap(
    expression: String,
    language: String = PropertiesManager.visionOCRLanguage,
    last: Boolean = false,
    holdSeconds: Double = TestDriver.testContext.tapHoldSeconds,
    waitSeconds: Double = 0.0,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    waitForElementFocused: Boolean = false,
    directAccess: Boolean = false,
    removeRedundantText: Boolean = false,
): VisionElement {

    if (directAccess) {
        val e = testDrive.tap(
            expression = expression,
            holdSeconds = holdSeconds,
            safeElementOnly = false,
        )
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
    v.selector = sel
    context.execOperateCommand(command = command, message = message, subject = "$sel") {

        v = detectCore(
            selector = sel,
            language = language,
            last = last,
            allowScroll = null,
            waitSeconds = waitSeconds,
            swipeToSafePosition = swipeToSafePosition,
            removeRedundantText = removeRedundantText,
            throwsException = true,
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
    if (waitForElementFocused) {
        waitForElementFocused(
            waitSeconds = waitSeconds,
            throwOnError = true,
        )
    }

    if (testContext.irregularHandler != null) {
        printWarn("irregularHandler is not supported on vision mode.")
    }

    return v
}

internal fun VisionDrive.waitForElementFocused(
    waitSeconds: Double,
    throwOnError: Boolean,
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
    language: String,
    last: Boolean,
    waitSeconds: Double,
    removeRedundantText: Boolean,
    throwsException: Boolean,
): VisionElement {

    fun getElement(): VisionElement {
        return detectCore(
            selector = selector,
            language = language,
            last = last,
            allowScroll = null,
            waitSeconds = waitSeconds,
            throwsException = throwsException,
            swipeToSafePosition = true,
            removeRedundantText = removeRedundantText,
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

    wait(waitSeconds = testContext.waitSecondsForAnimationComplete)

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

    lastElement = v.newVisionElement()
    return lastElement
}

private fun VisionDrive.tapWithScrollCommandCore(
    expression: String,
    language: String,
    last: Boolean,
    command: String,
    direction: ScrollDirection,
    scrollDurationSeconds: Double,
    scrollStartMarginRatio: Double,
    scrollEndMarginRatio: Double,
    scrollIntervalSeconds: Double,
    scrollMaxCount: Int,
    holdSeconds: Double,
    swipeToSafePosition: Boolean,
    removeRedundantText: Boolean,
): VisionElement {

    val selector = getSelector(expression = expression)
    val message = message(id = command, subject = "$selector")
    var v = VisionElement.emptyElement
    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {

        v = detectWithScrollCore(
            selector = selector,
            language = language,
            last = last,
            direction = direction,
            scrollDurationSeconds = scrollDurationSeconds,
            startMarginRatio = scrollStartMarginRatio,
            endMarginRatio = scrollEndMarginRatio,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollMaxCount = scrollMaxCount,
            throwsException = true,
            swipeToSafePosition = swipeToSafePosition,
            removeRedundantText = removeRedundantText,
        )
        TestDriver.autoScreenshot(force = testContext.onExecOperateCommand)
        v = v.tap(holdSeconds = holdSeconds)
    }
    return v
}

/**
 * tapImage
 */
fun VisionDrive.tapImage(
    label: String,
    threshold: Double? = PropertiesManager.visionFindImageThreshold,
    segmentMarginHorizontal: Int = PropertiesManager.segmentMarginHorizontal,
    segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    skinThickness: Int = 2,
    binaryThreshold: Int = PropertiesManager.visionFindImageBinaryThreshold,
    waitSeconds: Double = 0.0,
    holdSeconds: Double = testContext.tapHoldSeconds,
    throwsException: Boolean = true,
): VisionElement {

    val v = findImage(
        label = label,
        threshold = threshold,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        mergeIncluded = mergeIncluded,
        skinThickness = skinThickness,
        binaryThreshold = binaryThreshold,
        waitSeconds = waitSeconds,
        throwsException = throwsException,
    )

    v.tap(holdSeconds = holdSeconds)

    lastElement = v.newVisionElement()
    return lastElement
}

/**
 * tapWithScrollDown
 */
fun VisionDrive.tapWithScrollDown(
    expression: String,
    language: String = PropertiesManager.visionOCRLanguage,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    holdSeconds: Double = testContext.tapHoldSeconds,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    removeRedundantText: Boolean = true,
): VisionElement {

    val command = "tapWithScrollDown"
    val direction = ScrollDirection.Down

    val v = tapWithScrollCommandCore(
        expression = expression,
        language = language,
        last = false,
        command = command,
        direction = direction,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollMaxCount = scrollMaxCount,
        holdSeconds = holdSeconds,
        swipeToSafePosition = swipeToSafePosition,
        removeRedundantText = removeRedundantText,
    )

    return v
}

/**
 * tapWithScrollUp
 */
fun VisionDrive.tapWithScrollUp(
    expression: String,
    language: String = PropertiesManager.visionOCRLanguage,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    holdSeconds: Double = testContext.tapHoldSeconds,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    removeRedundantText: Boolean = true,
): VisionElement {

    val command = "tapWithScrollUp"
    val direction = ScrollDirection.Up

    val v = tapWithScrollCommandCore(
        expression = expression,
        language = language,
        last = false,
        command = command,
        direction = direction,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollMaxCount = scrollMaxCount,
        holdSeconds = holdSeconds,
        swipeToSafePosition = swipeToSafePosition,
        removeRedundantText = removeRedundantText,
    )

    return v
}

/**
 * tapWithScrollRight
 */
fun VisionDrive.tapWithScrollRight(
    expression: String,
    language: String = PropertiesManager.visionOCRLanguage,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    holdSeconds: Double = testContext.tapHoldSeconds,
    removeRedundantText: Boolean = true,
): VisionElement {

    val command = "tapWithScrollRight"
    val direction = ScrollDirection.Right

    val v = tapWithScrollCommandCore(
        expression = expression,
        language = language,
        last = false,
        command = command,
        direction = direction,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollMaxCount = scrollMaxCount,
        holdSeconds = holdSeconds,
        swipeToSafePosition = false,
        removeRedundantText = removeRedundantText,
    )

    return v
}

/**
 * tapWithScrollLeft
 */
fun VisionDrive.tapWithScrollLeft(
    expression: String,
    language: String = PropertiesManager.visionOCRLanguage,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    holdSeconds: Double = testContext.tapHoldSeconds,
    removeRedundantText: Boolean = true,
): VisionElement {

    val command = "tapWithScrollLeft"
    val direction = ScrollDirection.Left

    val v = tapWithScrollCommandCore(
        expression = expression,
        language = language,
        last = false,
        command = command,
        direction = direction,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollMaxCount = scrollMaxCount,
        holdSeconds = holdSeconds,
        swipeToSafePosition = false,
        removeRedundantText = removeRedundantText,
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
    language: String = PropertiesManager.visionOCRLanguage,
    holdSeconds: Double = testContext.tapHoldSeconds,
    repeat: Int = 1,
): VisionElement {

    val testElement = detect(
        expression = expression,
        language = language,
    )

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
    language: String = PropertiesManager.visionOCRLanguage,
    allowScroll: Boolean? = null,
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
            language = language,
            allowScroll = allowScroll,
            swipeToSafePosition = swipeToSafePosition,
            throwsException = true,
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
    language: String = PropertiesManager.visionOCRLanguage,
    allowScroll: Boolean? = null,
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
            language = language,
            allowScroll = allowScroll,
            swipeToSafePosition = swipeToSafePosition,
            throwsException = true,
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
