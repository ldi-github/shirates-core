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
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = true,
    last: Boolean = false,
    holdSeconds: Double = testContext.tapHoldSeconds,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    allowScroll: Boolean? = CodeExecutionContext.withScroll,
    swipeToSafePosition: Boolean = CodeExecutionContext.withScroll ?: CodeExecutionContext.swipeToSafePosition,
    waitForElementFocused: Boolean = false,
    directAccess: Boolean = false,
): VisionElement {

    if (directAccess) {
        val e = classic.tap(
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
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            autoImageFilter = autoImageFilter,
            last = last,
            allowScroll = allowScroll,
            waitSeconds = waitSeconds,
            swipeToSafePosition = swipeToSafePosition,
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

    return v
}

/**
 * tapLast
 */
fun VisionDrive.tapLast(
    expression: String,
    language: String = PropertiesManager.visionOCRLanguage,
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = true,
    holdSeconds: Double = testContext.tapHoldSeconds,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    allowScroll: Boolean? = CodeExecutionContext.withScroll,
    swipeToSafePosition: Boolean = CodeExecutionContext.withScroll ?: CodeExecutionContext.swipeToSafePosition,
    waitForElementFocused: Boolean = false,
    directAccess: Boolean = false,
): VisionElement {

    return this.tap(
        expression = expression,
        language = language,
        looseMatch = looseMatch,
        mergeBoundingBox = mergeBoundingBox,
        lineSpacingRatio = lineSpacingRatio,
        autoImageFilter = autoImageFilter,
        last = true,
        holdSeconds = holdSeconds,
        waitSeconds = waitSeconds,
        allowScroll = allowScroll,
        swipeToSafePosition = swipeToSafePosition,
        waitForElementFocused = waitForElementFocused,
        directAccess = directAccess,
    )
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
): VisionElement {

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
    screenshot()

    return testElement.newVisionElement()
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
            val subject = tappedElement.subject.take(100)
            val fileName = "${TestLog.nextLineNo}_tap_${subject}_${tappedElement.bounds}"
            tappedElement.saveImage(fileName = fileName)
        }
        val sel = v.selector
        var b = v.bounds
        if (sel != null && v.text.isNotBlank()) {
            val searchText = sel.text ?: sel.textStartsWith ?: sel.textContains ?: sel.textEndsWith ?: ""
            if (searchText != v.text) {
                val p = v.text.indexOf(searchText).toDouble() / v.text.length
                if (p >= 0.0) {
                    val offset = (b.width * p).toInt()
                    val width = (b.width * (searchText.length.toDouble() / v.text.length)).toInt()
                    b = Bounds(b.left + offset, b.top, width, b.height)
                }
            }
        }
        lastElement = v.tap(x = b.centerX, y = b.centerY, holdSeconds = holdSeconds)
    }

    return lastElement
}

private fun VisionDrive.tapWithScrollCommandCore(
    expression: String,
    language: String,
    looseMatch: Boolean,
    mergeBoundingBox: Boolean,
    lineSpacingRatio: Double,
    autoImageFilter: Boolean,
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
): VisionElement {

    val selector = getSelector(expression = expression)
    val message = message(id = command, subject = "$selector")
    var v = VisionElement.emptyElement
    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {

        v = detectWithScrollCore(
            selector = selector,
            language = language,
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            autoImageFilter = autoImageFilter,
            last = last,
            direction = direction,
            scrollDurationSeconds = scrollDurationSeconds,
            startMarginRatio = scrollStartMarginRatio,
            endMarginRatio = scrollEndMarginRatio,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollMaxCount = scrollMaxCount,
            throwsException = true,
            swipeToSafePosition = swipeToSafePosition,
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
    threshold: Double = testContext.visionFindImageThreshold,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    skinThickness: Int = 2,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = testContext.visionFindImageAspectRatioTolerance,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    holdSeconds: Double = testContext.tapHoldSeconds,
    allowScroll: Boolean? = CodeExecutionContext.withScroll,
    throwsException: Boolean = true,
): VisionElement {

    val sel = Selector(expression = label)

    val command = "tapImage"
    val message = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    var v = VisionElement.emptyElement
    v.selector = sel
    context.execOperateCommand(command = command, message = message, subject = "$sel") {
        v = findImage(
            label = label,
            threshold = threshold,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            mergeIncluded = mergeIncluded,
            skinThickness = skinThickness,
            binaryThreshold = binaryThreshold,
            aspectRatioTolerance = aspectRatioTolerance,
            waitSeconds = waitSeconds,
            allowScroll = allowScroll,
            throwsException = throwsException,
        )

        v.tap(holdSeconds = holdSeconds)
    }

    lastElement = v.newVisionElement()
    return lastElement
}

/**
 * tapWithScrollDown
 */
fun VisionDrive.tapWithScrollDown(
    expression: String,
    last: Boolean = true,
    language: String = PropertiesManager.visionOCRLanguage,
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    holdSeconds: Double = testContext.tapHoldSeconds,
    swipeToSafePosition: Boolean = true,
): VisionElement {

    val command = "tapWithScrollDown"
    val direction = ScrollDirection.Down

    val v = tapWithScrollCommandCore(
        expression = expression,
        language = language,
        looseMatch = looseMatch,
        mergeBoundingBox = mergeBoundingBox,
        lineSpacingRatio = lineSpacingRatio,
        autoImageFilter = autoImageFilter,
        last = last,
        command = command,
        direction = direction,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollMaxCount = scrollMaxCount,
        holdSeconds = holdSeconds,
        swipeToSafePosition = swipeToSafePosition,
    )

    return v
}

/**
 * tapWithScrollUp
 */
fun VisionDrive.tapWithScrollUp(
    expression: String,
    last: Boolean = false,
    language: String = PropertiesManager.visionOCRLanguage,
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    holdSeconds: Double = testContext.tapHoldSeconds,
    swipeToSafePosition: Boolean = true,
): VisionElement {

    val command = "tapWithScrollUp"
    val direction = ScrollDirection.Up

    val v = tapWithScrollCommandCore(
        expression = expression,
        language = language,
        looseMatch = looseMatch,
        mergeBoundingBox = mergeBoundingBox,
        lineSpacingRatio = lineSpacingRatio,
        autoImageFilter = autoImageFilter,
        last = last,
        command = command,
        direction = direction,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollMaxCount = scrollMaxCount,
        holdSeconds = holdSeconds,
        swipeToSafePosition = swipeToSafePosition,
    )

    return v
}

/**
 * tapWithScrollRight
 */
fun VisionDrive.tapWithScrollRight(
    expression: String,
    last: Boolean = true,
    language: String = PropertiesManager.visionOCRLanguage,
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
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
        language = language,
        looseMatch = looseMatch,
        mergeBoundingBox = mergeBoundingBox,
        lineSpacingRatio = lineSpacingRatio,
        autoImageFilter = autoImageFilter,
        last = last,
        command = command,
        direction = direction,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollMaxCount = scrollMaxCount,
        holdSeconds = holdSeconds,
        swipeToSafePosition = false,
    )

    return v
}

/**
 * tapWithScrollLeft
 */
fun VisionDrive.tapWithScrollLeft(
    expression: String,
    last: Boolean = false,
    language: String = PropertiesManager.visionOCRLanguage,
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
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
        language = language,
        looseMatch = looseMatch,
        mergeBoundingBox = mergeBoundingBox,
        lineSpacingRatio = lineSpacingRatio,
        autoImageFilter = autoImageFilter,
        last = last,
        command = command,
        direction = direction,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollMaxCount = scrollMaxCount,
        holdSeconds = holdSeconds,
        swipeToSafePosition = false,
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
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = true,
    last: Boolean = false,
    holdSeconds: Double = testContext.tapHoldSeconds,
    repeat: Int = 1,
): VisionElement {

    val sel = getSelector(expression = expression)

    val command = "tapCenterOf"
    val message = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {

        val testElement = detect(
            expression = expression,
            language = language,
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            autoImageFilter = autoImageFilter,
            last = last,
        )
        val bounds = testElement.bounds
        tap(x = bounds.centerX, y = bounds.centerY, holdSeconds = holdSeconds, repeat = repeat)
    }

    return lastElement
}

/**
 * tapItemUnder
 */
fun VisionDrive.tapItemUnder(
    expression: String,
    pos: Int = 1,
    language: String = testContext.visionOCRLanguage,
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = true,
    last: Boolean = false,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    allowScroll: Boolean? = CodeExecutionContext.withScroll,
    holdSeconds: Double = TestDriver.testContext.tapHoldSeconds,
    swipeToSafePosition: Boolean = CodeExecutionContext.withScroll ?: CodeExecutionContext.swipeToSafePosition,
    removeHorizontalLine: Boolean = false,
    horizontalLineThreshold: Double = PropertiesManager.visionHorizontalLineThreshold,
    removeVerticalLine: Boolean = true,
    verticalLineThreshold: Double = PropertiesManager.visionVerticalLineThreshold,
): VisionElement {

    val sel = getSelector(expression = expression)

    val command = "tapItemUnder"
    val message = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    var v = VisionElement.emptyElement
    context.execOperateCommand(command = command, message = message, subject = "$sel") {

        val labelElement = detect(
            expression = expression,
            language = language,
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            autoImageFilter = autoImageFilter,
            last = last,
            allowScroll = allowScroll,
            swipeToSafePosition = swipeToSafePosition,
            waitSeconds = 0.0,
            throwsException = true,
        )
        v = labelElement.belowItem(
            pos = pos,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            removeHorizontalLine = removeHorizontalLine,
            horizontalLineThreshold = horizontalLineThreshold,
            removeVerticalLine = removeVerticalLine,
            verticalLineThreshold = verticalLineThreshold,
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
 * tapTextUnder
 */
fun VisionDrive.tapTextUnder(
    expression: String,
    last: Boolean = false,
    language: String = testContext.visionOCRLanguage,
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = true,
    allowScroll: Boolean? = CodeExecutionContext.withScroll,
    holdSeconds: Double = TestDriver.testContext.tapHoldSeconds,
    swipeToSafePosition: Boolean = CodeExecutionContext.withScroll ?: CodeExecutionContext.swipeToSafePosition,
): VisionElement {

    val sel = getSelector(expression = expression)

    val command = "tapTextUnder"
    val message = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    var v = VisionElement.emptyElement
    context.execOperateCommand(command = command, message = message, subject = "$sel") {

        val labelElement = detect(
            expression = expression,
            last = last,
            language = language,
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            autoImageFilter = autoImageFilter,
            allowScroll = allowScroll,
            swipeToSafePosition = swipeToSafePosition,
            waitSeconds = 0.0,
            throwsException = true,
        )
        v = labelElement.belowText()
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
 * tapItemOver
 */
fun VisionDrive.tapItemOver(
    expression: String,
    pos: Int = 1,
    language: String = testContext.visionOCRLanguage,
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = true,
    last: Boolean = false,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    allowScroll: Boolean? = CodeExecutionContext.withScroll,
    holdSeconds: Double = TestDriver.testContext.tapHoldSeconds,
    swipeToSafePosition: Boolean = CodeExecutionContext.withScroll ?: CodeExecutionContext.swipeToSafePosition,
    removeHorizontalLine: Boolean = false,
    horizontalLineThreshold: Double = PropertiesManager.visionHorizontalLineThreshold,
    removeVerticalLine: Boolean = false,
    verticalLineThreshold: Double = PropertiesManager.visionVerticalLineThreshold,
): VisionElement {

    val sel = getSelector(expression = expression)

    val command = "tapItemOver"
    val message = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    var v = VisionElement.emptyElement
    context.execOperateCommand(command = command, message = message, subject = "$sel") {

        val labelElement = detect(
            expression = expression,
            language = language,
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            autoImageFilter = autoImageFilter,
            last = last,
            allowScroll = allowScroll,
            swipeToSafePosition = swipeToSafePosition,
            waitSeconds = 0.0,
            throwsException = true,
        )
        v = labelElement.aboveItem(
            pos = pos,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            removeHorizontalLine = removeHorizontalLine,
            horizontalLineThreshold = horizontalLineThreshold,
            removeVerticalLine = removeVerticalLine,
            verticalLineThreshold = verticalLineThreshold,
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
 * tapTextOver
 */
fun VisionDrive.tapTextOver(
    expression: String,
    language: String = testContext.visionOCRLanguage,
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = true,
    last: Boolean = false,
    allowScroll: Boolean? = CodeExecutionContext.withScroll,
    holdSeconds: Double = TestDriver.testContext.tapHoldSeconds,
    swipeToSafePosition: Boolean = CodeExecutionContext.withScroll ?: CodeExecutionContext.swipeToSafePosition,
): VisionElement {

    val sel = getSelector(expression = expression)

    val command = "tapTextOver"
    val message = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    var v = VisionElement.emptyElement
    context.execOperateCommand(command = command, message = message, subject = "$sel") {

        val labelElement = detect(
            expression = expression,
            language = language,
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            autoImageFilter = autoImageFilter,
            last = last,
            allowScroll = allowScroll,
            swipeToSafePosition = swipeToSafePosition,
            waitSeconds = 0.0,
            throwsException = true,
        )
        v = labelElement.aboveText()
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
 * tapItemRightOf
 */
fun VisionDrive.tapItemRightOf(
    expression: String,
    pos: Int = 1,
    language: String = testContext.visionOCRLanguage,
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = true,
    last: Boolean = false,
    horizontalMargin: Int = testContext.segmentMarginHorizontal,
    verticalMargin: Int = testContext.segmentMarginVertical,
    allowScroll: Boolean? = CodeExecutionContext.withScroll,
    holdSeconds: Double = testContext.tapHoldSeconds,
    swipeToSafePosition: Boolean = CodeExecutionContext.withScroll ?: CodeExecutionContext.swipeToSafePosition,
): VisionElement {

    val sel = getSelector(expression = expression)

    val command = "tapItemRightOf"
    val message = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    var v = VisionElement.emptyElement
    context.execOperateCommand(command = command, message = message, subject = "$sel") {

        val labelElement = detect(
            expression = expression,
            language = language,
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            autoImageFilter = autoImageFilter,
            last = last,
            allowScroll = allowScroll,
            swipeToSafePosition = swipeToSafePosition,
            waitSeconds = 0.0,
            throwsException = true,
        )
        v = labelElement.rightItem(
            pos = pos,
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
 * tapItemLeftOf
 */
fun VisionDrive.tapItemLeftOf(
    expression: String,
    pos: Int = 1,
    language: String = testContext.visionOCRLanguage,
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = true,
    last: Boolean = false,
    horizontalMargin: Int = testContext.segmentMarginHorizontal,
    verticalMargin: Int = testContext.segmentMarginVertical,
    allowScroll: Boolean? = CodeExecutionContext.withScroll,
    holdSeconds: Double = testContext.tapHoldSeconds,
    swipeToSafePosition: Boolean = CodeExecutionContext.withScroll ?: CodeExecutionContext.swipeToSafePosition,
): VisionElement {

    val sel = getSelector(expression = expression)

    val command = "tapItemLeftOf"
    val message = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    var v = VisionElement.emptyElement
    context.execOperateCommand(command = command, message = message, subject = "$sel") {

        val labelElement = detect(
            expression = expression,
            language = language,
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            autoImageFilter = autoImageFilter,
            last = last,
            allowScroll = allowScroll,
            swipeToSafePosition = swipeToSafePosition,
            waitSeconds = 0.0,
            throwsException = true,
        )
        v = labelElement.leftItem(
            pos = pos,
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
