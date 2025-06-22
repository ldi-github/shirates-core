package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.*
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.commandextension.select
import shirates.core.logging.Message.message
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.image.rect
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.branchextension.lastScreenshotImage
import shirates.core.vision.driver.lastElement
import shirates.core.vision.driver.silent
import kotlin.math.abs

/**
 * swipeTo
 */
fun VisionElement.swipeTo(
    expression: String,
    language: String = testContext.visionOCRLanguage,
    looseMatch: Boolean = testContext.visionLooseMatch,
    mergeBoundingBox: Boolean = testContext.visionMergeBoundingBox,
    lineSpacingRatio: Double = testContext.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
    last: Boolean = false,
    waitSeconds: Double = testContext.waitSecondsForAnimationComplete,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    marginRatio: Double = testContext.swipeMarginRatio,
    adjust: Boolean = false,
    repeat: Int = 1,
): VisionElement {

    val command = "swipeTo"
    val sel = getSelector(expression = expression)
    val message = message(id = command, subject = subject, to = "$sel")
    var v = VisionElement.emptyElement

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject, arg1 = expression) {

        v = detectCore(
            selector = sel,
            language = language,
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
            autoImageFilter = autoImageFilter,
            last = last,
            allowScroll = false,
            waitSeconds = waitSeconds,
            throwsException = false,
            swipeToSafePosition = false,
        )

        v = swipeElementToElement(
            startElement = this,
            endElement = v,
            durationSeconds = durationSeconds,
            marginRatio = marginRatio,
            adjust = adjust,
            repeat = repeat
        )
    }
    return v
}

/**
 * swipeToAdjust
 */
fun VisionElement.swipeToAdjust(
    expression: String,
    language: String = testContext.visionOCRLanguage,
    looseMatch: Boolean = testContext.visionLooseMatch,
    mergeBoundingBox: Boolean = testContext.visionMergeBoundingBox,
    lineSpacingRatio: Double = testContext.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    marginRatio: Double = testContext.swipeMarginRatio,
): VisionElement {

    return swipeTo(
        expression = expression,
        language = language,
        looseMatch = looseMatch,
        mergeBoundingBox = mergeBoundingBox,
        lineSpacingRatio = lineSpacingRatio,
        autoImageFilter = autoImageFilter,
        durationSeconds = durationSeconds,
        marginRatio = marginRatio,
        adjust = true,
    )
}

/**
 * swipeVerticalTo
 */
fun VisionElement.swipeVerticalTo(
    endY: Int,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    avoidTapping: Boolean = true,
): VisionElement {

    val b = bounds
    if (avoidTapping && abs(b.centerY - endY) < 10) {
        return this
    }

    val command = "swipeVerticalTo"
    val message = message(id = command, subject = subject, to = "$endY")
    var v = VisionElement.emptyElement

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject, arg1 = "$endY") {
        v = swipePointToPoint(
            startX = b.centerX,
            startY = b.centerY,
            endX = b.centerX,
            endY = endY,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }
    return v
}

/**
 * swipeHorizontalTo
 */
fun VisionElement.swipeHorizontalTo(
    endX: Int,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    avoidTapping: Boolean = true,
): VisionElement {

    val b = bounds
    if (avoidTapping && abs(b.x1 - endX) < 10) {
        return this
    }

    val command = "swipeHorizontalTo"
    val message = message(id = command, subject = subject, to = "$endX")
    var v = VisionElement.emptyElement

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject, arg1 = "$endX") {
        v = swipePointToPoint(
            startX = b.x1,
            startY = b.centerY,
            endX = endX,
            endY = b.centerY,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }
    return v
}

/**
 * swipeToTop
 */
fun VisionElement.swipeToTop(
    topOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): VisionElement {

    val command = "swipeToTop"
    val message = message(id = command, subject = subject)
    var v = VisionElement.emptyElement

    val navigationBar =
        if (TestMode.isiOS) classic.select(".XCUIElementTypeNavigationBar", throwsException = false)
        else TestElement.emptyElement
    val top =
        if (topOffsetRatio != 0.0 && navigationBar.isFound) navigationBar.bounds.bottom + 1
        else PropertiesManager.statBarHeight

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        val b = this.bounds
        val topOffsetY = (topOffsetRatio * rootElement.bounds.height).toInt()
        val stickToEdgeMargin = if (stickToEdge) b.height / 2 else 0
        val endY = top + stickToEdgeMargin + topOffsetY
        v = swipePointToPoint(
            startX = b.centerX,
            startY = b.centerY,
            endX = b.centerX,
            endY = endY,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }
    return v
}

/**
 * swipeToTopOfScreen
 */
fun VisionElement.swipeToTopOfScreen(
    topOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): VisionElement {

    return swipeToTop(
        topOffsetRatio = topOffsetRatio,
        durationSeconds = durationSeconds,
        repeat = repeat,
        stickToEdge = stickToEdge
    )
}


/**
 * swipeOutTop
 */
fun VisionElement.swipeOutTop(
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): VisionElement {

    return swipeToTop(
        durationSeconds = durationSeconds,
        repeat = repeat,
        stickToEdge = false
    )
}

/**
 * flickToTop
 */
fun VisionElement.flickToTop(
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1
): VisionElement {

    val command = "flickToTop"
    val message = message(id = command, subject = subject)
    var v = VisionElement.emptyElement

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        v = swipeOutTop(
            durationSeconds = durationSeconds,
            repeat = repeat,
        )
    }
    return v
}

/**
 * swipeToBottom
 */
fun VisionElement.swipeToBottom(
    ofScreen: Boolean = false,
    bottomOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): VisionElement {

    val frameBounds =
        if (ofScreen) rootElement.bounds
        else CodeExecutionContext.workingRegionRect.toBoundsWithRatio()

    val command = "swipeToBottom"
    val message = message(id = command, subject = subject)
    var v = VisionElement.emptyElement

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        val b = this.bounds
        val bottomOffsetY = (bottomOffsetRatio * rootElement.bounds.height).toInt()
        val stickToEdgeMargin = if (stickToEdge) b.height / 2 else 0
        val endY = frameBounds.bottom - stickToEdgeMargin - bottomOffsetY
        v = swipePointToPoint(
            startX = b.centerX,
            startY = b.centerY,
            endX = b.centerX,
            endY = endY,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }
    return v
}

/**
 * swipeToBottomOfScreen
 */
fun VisionElement.swipeToBottomOfScreen(
    bottomOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): VisionElement {

    return swipeToBottom(
        ofScreen = true,
        bottomOffsetRatio = bottomOffsetRatio,
        durationSeconds = durationSeconds,
        repeat = repeat,
        stickToEdge = stickToEdge
    )
}

/**
 * swipeOutBottom
 */
fun VisionElement.swipeOutBottom(
    ofScreen: Boolean = false,
    bottomOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): VisionElement {

    return swipeToBottom(
        ofScreen = ofScreen,
        bottomOffsetRatio = bottomOffsetRatio,
        durationSeconds = durationSeconds,
        repeat = repeat,
        stickToEdge = false
    )
}

/**
 * flickToBottom
 */
fun VisionElement.flickToBottom(
    ofScreen: Boolean = false,
    bottomOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1
): VisionElement {

    val command = "flickToBottom"
    val message = message(id = command, subject = subject)
    var v = VisionElement.emptyElement

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        v = swipeOutBottom(
            ofScreen = ofScreen,
            bottomOffsetRatio = bottomOffsetRatio,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }
    return v
}

/**
 * swipeToCenter
 */
fun VisionElement.swipeToCenter(
    ofScreen: Boolean = false,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    avoidTapping: Boolean = true,
): VisionElement {

    val frame =
        if (ofScreen) rootElement.bounds
        else CodeExecutionContext.workingRegionRect.toBoundsWithRatio()
    val endX = frame.centerX
    val endY = frame.centerY

    val b = this.bounds
    if (avoidTapping && abs(b.centerY - endY) < 10) {
        return this
    }

    val command = "swipeToCenter"
    val message = message(id = command, subject = subject)
    var v = VisionElement.emptyElement

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject) {

        v = swipePointToPoint(
            startX = b.centerX,
            startY = b.centerY,
            endX = endX,
            endY = endY,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }
    return v
}

/**
 * swipeToCenterOfScreen
 */
fun VisionElement.swipeToCenterOfScreen(
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    avoidTapping: Boolean = true,
): VisionElement {

    return swipeToCenter(
        ofScreen = true,
        durationSeconds = durationSeconds,
        repeat = repeat,
        avoidTapping = avoidTapping,
    )
}

/**
 * swipeToRight
 */
fun VisionElement.swipeToRight(
    ofScreen: Boolean = false,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): VisionElement {

    val frameBounds =
        if (ofScreen) lastScreenshotImage!!.rect.toBoundsWithRatio()
        else CodeExecutionContext.workingRegionRect.toBoundsWithRatio()

    val command = "swipeToRight"
    val message = message(id = command, subject = subject)
    var v = VisionElement.emptyElement

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        val b = this.bounds
        val offsetX = (startOffsetRatio * b.width).toInt()
        val endMargin = if (stickToEdge) b.width / 2 else 0
        v = swipePointToPoint(
            startX = b.centerX + offsetX,
            startY = b.centerY,
            endX = frameBounds.right - endMargin,
            endY = b.centerY,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }
    return v
}

/**
 * swipeOutRight
 */
fun VisionElement.swipeOutRight(
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): VisionElement {

    return swipeToRight(
        startOffsetRatio = startOffsetRatio,
        durationSeconds = durationSeconds,
        repeat = repeat,
        stickToEdge = false
    )
}

/**
 * flickToRight
 */
fun VisionElement.flickToRight(
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1
): VisionElement {

    val command = "flickToRight"
    val message = message(id = command, subject = subject)
    var v = VisionElement.emptyElement

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        v = swipeOutRight(
            startOffsetRatio = startOffsetRatio,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }
    return v
}

/**
 * swipeToLeft
 */
fun VisionElement.swipeToLeft(
    ofScreen: Boolean = false,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): VisionElement {

    val frameBounds =
        if (ofScreen) lastScreenshotImage!!.rect.toBoundsWithRatio()
        else CodeExecutionContext.workingRegionRect.toBoundsWithRatio()

    val command = "swipeToLeft"
    val message = message(id = command, subject = subject)
    var v = VisionElement.emptyElement

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, arg1 = subject) {
        val b = this.bounds
        val offsetX = (startOffsetRatio * b.width).toInt()
        val endMargin = if (stickToEdge) b.width / 2 else 0
        v = swipePointToPoint(
            startX = b.centerX + offsetX,
            startY = b.centerY,
            endX = frameBounds.left + endMargin,
            endY = b.centerY,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }
    return v
}

/**
 * swipeOutLeft
 */
fun VisionElement.swipeOutLeft(
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): VisionElement {

    return swipeToLeft(
        startOffsetRatio = startOffsetRatio,
        durationSeconds = durationSeconds,
        repeat = repeat,
        stickToEdge = false
    )
}

/**
 * flickToLeft
 */
fun VisionElement.flickToLeft(
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1
): VisionElement {

    val command = "flickToLeft"
    val message = message(id = command, subject = subject)
    var v = VisionElement.emptyElement

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        v = swipeOutLeft(
            startOffsetRatio = startOffsetRatio,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }
    return v
}

/**
 * isSafePosition
 */
fun VisionElement.isSafePosition(
    direction: ScrollDirection = CodeExecutionContext.scrollDirection ?: ScrollDirection.Down
): Boolean {

    if (this.isEmpty) {
        return true
    }

    if (direction.isVertical) {
        val safeArea = getSafeArea()
        val isInSafeArea = this.rect.isIncludedIn(safeArea.rect)
        return isInSafeArea
    }
    return true
}

/**
 * swipeToSafePosition
 */
fun VisionElement.swipeToSafePosition(
    durationSeconds: Double = testContext.swipeDurationSeconds / 2,
    direction: ScrollDirection = CodeExecutionContext.scrollDirection ?: ScrollDirection.Down,
    action: (() -> Unit)? = null
): VisionElement {

    if (this.isEmpty) {
        return this
    }
    if (this.isSafePosition(direction = direction)) {
        return this
    }

    val safeArea = getSafeArea()

    silent {
        if (direction.isVertical) {
            val verticalMovement = this.bounds.centerY - safeArea.bounds.centerY
            swipePointToPoint(
                startX = screenBounds.centerX,
                startY = screenBounds.centerY,
                endX = screenBounds.centerX,
                endY = this.bounds.centerY - verticalMovement,
                durationSeconds = durationSeconds,
            )
        } else {
            val horizontalMovement = this.bounds.centerX - safeArea.bounds.centerX
            swipePointToPoint(
                startX = screenBounds.centerX,
                startY = screenBounds.centerY,
                endX = this.bounds.centerX - screenBounds.centerX,
                endY = screenBounds.centerY - horizontalMovement,
                durationSeconds = durationSeconds,
            )
        }
    }

    if (action != null) {
        action()
        return lastElement
    }
    if (this.selector?.expression != null) {
        val v = detect(expression = this.selector!!.expression!!)
        return v
    }

    return VisionElement.emptyElement
}