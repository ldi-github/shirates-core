package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.*
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.commandextension.select
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Message.message
import shirates.core.utility.image.rect
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.branchextension.lastScreenshotImage

/**
 * swipeTo
 */
fun VisionElement.swipeTo(
    expression: String,
    language: String = PropertiesManager.visionOCRLanguage,
    waitSeconds: Double = 0.0,
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
    language: String = PropertiesManager.visionOCRLanguage,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    marginRatio: Double = testContext.swipeMarginRatio,
): VisionElement {

    return swipeTo(
        expression = expression,
        language = language,
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
    repeat: Int = 1
): VisionElement {

    val command = "swipeVerticalTo"
    val message = message(id = command, subject = subject, to = "$endY")
    var v = VisionElement.emptyElement

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject, arg1 = "$endY") {
        val b = bounds
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
    repeat: Int = 1
): VisionElement {

    val command = "swipeHorizontalTo"
    val message = message(id = command, subject = subject, to = "$endX")
    var v = VisionElement.emptyElement

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject, arg1 = "$endX") {
        val b = bounds
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
        if (TestMode.isiOS) testDrive.select(".XCUIElementTypeNavigationBar", throwsException = false)
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
): VisionElement {

    val frame =
        if (ofScreen) rootElement.bounds
        else CodeExecutionContext.workingRegionRect.toBoundsWithRatio()
    val endX = frame.centerX
    val endY = frame.centerY

    val command = "swipeToCenter"
    val message = message(id = command, subject = subject)
    var v = VisionElement.emptyElement

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject) {

        val b = this.bounds
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
): VisionElement {

    return swipeToCenter(
        ofScreen = true,
        durationSeconds = durationSeconds,
        repeat = repeat,
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
 * swipeToSafePosition
 */
fun VisionElement.swipeToSafePosition(
    direction: ScrollDirection = CodeExecutionContext.scrollDirection ?: ScrollDirection.Down,
): VisionElement {
    if (CodeExecutionContext.withScroll == false) {
        return this
    }

    var v = VisionElement.emptyElement

    when (direction) {
        ScrollDirection.Down -> if (screenBounds.centerY < this.bounds.top) {
            v = this.swipeVerticalTo((screenBounds.height * 0.2).toInt())
        }

        ScrollDirection.Up -> if (this.bounds.bottom < screenBounds.height * 0.2) {
            v = this.swipeVerticalTo((screenBounds.height * 0.8).toInt())
        }

        ScrollDirection.Left -> if (screenBounds.width * 0.8 < this.bounds.right) {
            v = this.swipeHorizontalTo((screenBounds.width * 0.2).toInt())
        }

        ScrollDirection.Right -> if (this.bounds.left < screenBounds.width * 0.2) {
            v = this.swipeHorizontalTo((screenBounds.width * 0.8).toInt())
        }

        else -> {}
    }
    return v
}