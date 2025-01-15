package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.ScrollDirection
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.testContext
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
    language: String = PropertiesManager.logLanguage,
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
            throwsException = false,
            swipeToSafePosition = false,
        )

        swipeElementToElement(
            startElement = this,
            endElement = v,
            durationSeconds = durationSeconds,
            marginRatio = marginRatio,
            adjust = adjust,
            repeat = repeat
        )
    }

    v = v.newVisionElement()
    return v
}

/**
 * swipeToAdjust
 */
fun VisionElement.swipeToAdjust(
    expression: String,
    language: String = PropertiesManager.logLanguage,
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

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject, arg1 = "$endY") {
        val b = bounds
        swipePointToPoint(
            startX = b.centerX,
            startY = b.y1,
            endX = b.centerX,
            endY = endY,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this
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

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject, arg1 = "$endX") {
        val b = bounds
        swipePointToPoint(
            startX = b.x1,
            startY = b.centerY,
            endX = endX,
            endY = b.centerY,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this
}

/**
 * swipeToTop
 */
fun VisionElement.swipeToTop(
    ofScreen: Boolean = false,
    topOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): VisionElement {

    val frameBounds =
        if (ofScreen) rootElement.bounds
        else CodeExecutionContext.workingRegionRect.toBoundsWithRatio()

    val command = "swipeToTop"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        val b = this.bounds
        val topOffsetY = (topOffsetRatio * rootElement.bounds.height).toInt()
        val endMargin = if (stickToEdge) b.height / 2 else 0
        swipePointToPoint(
            startX = b.centerX,
            startY = b.centerY,
            endX = b.centerX,
            endY = frameBounds.top + endMargin + topOffsetY,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this
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
        ofScreen = true,
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
    ofScreen: Boolean = false,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): VisionElement {

    return swipeToTop(
        ofScreen = ofScreen,
        durationSeconds = durationSeconds,
        repeat = repeat,
        stickToEdge = false
    )
}

/**
 * flickToTop
 */
fun VisionElement.flickToTop(
    ofScreen: Boolean = false,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1
): VisionElement {

    val command = "flickToTop"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        swipeOutTop(
            ofScreen = ofScreen,
            durationSeconds = durationSeconds,
            repeat = repeat,
        )
    }

    return this
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

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        val b = this.bounds
        val bottomOffsetY = (bottomOffsetRatio * rootElement.bounds.height).toInt()
        val endMargin = if (stickToEdge) b.height / 2 else 0
        swipePointToPoint(
            startX = b.centerX,
            startY = b.centerY + bottomOffsetY,
            endX = b.centerX,
            endY = frameBounds.bottom - endMargin,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this
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

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        swipeOutBottom(
            ofScreen = ofScreen,
            bottomOffsetRatio = bottomOffsetRatio,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this
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
        if (ofScreen) rootElement.rect
        else CodeExecutionContext.workingRegionRect
    val endX = frame.centerX
    val endY = frame.centerY

    val command = "swipeToCenter"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject) {

        val r = this.bounds.toRectWithRatio()
        swipePointToPointByPixel(
            startPixelX = r.centerX,
            startPixelY = r.centerY,
            endPixelX = endX,
            endPixelY = endY,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this
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

    val frame =
        if (ofScreen) lastScreenshotImage!!.rect
        else CodeExecutionContext.workingRegionRect

    val command = "swipeToRight"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        val r = this.bounds.toRectWithRatio()
        val offsetX = (startOffsetRatio * r.width).toInt()
        val endMargin = if (stickToEdge) r.width / 2 else 0
        swipePointToPointByPixel(
            startPixelX = r.centerX + offsetX,
            startPixelY = r.centerY,
            endPixelX = frame.right - endMargin,
            endPixelY = r.centerY,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this
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

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        swipeOutRight(
            startOffsetRatio = startOffsetRatio,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this
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

    val frame =
        if (ofScreen) lastScreenshotImage!!.rect
        else CodeExecutionContext.workingRegionRect

    val command = "swipeToLeft"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, arg1 = subject) {
        val r = this.bounds.toRectWithRatio()
        val offsetX = (startOffsetRatio * r.width).toInt()
        val endMargin = if (stickToEdge) r.width / 2 else 0
        swipePointToPointByPixel(
            startPixelX = r.centerX + offsetX,
            startPixelY = r.centerY,
            endPixelX = frame.left + endMargin,
            endPixelY = r.centerY,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this
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

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        swipeOutLeft(
            startOffsetRatio = startOffsetRatio,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this
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

    when (direction) {
        ScrollDirection.Down -> if (screenBounds.centerY < this.bounds.top) {
            this.swipeVerticalTo((screenBounds.height * 0.2).toInt())
        }

        ScrollDirection.Up -> if (this.bounds.bottom < screenBounds.height * 0.2) {
            this.swipeVerticalTo((screenBounds.height * 0.8).toInt())
        }

        ScrollDirection.Left -> if (screenBounds.width * 0.8 < this.bounds.right) {
            this.swipeHorizontalTo((screenBounds.width * 0.2).toInt())
        }

        ScrollDirection.Right -> if (this.bounds.left < screenBounds.width * 0.2) {
            this.swipeHorizontalTo((screenBounds.width * 0.8).toInt())
        }

        else -> {}
    }
    return this
}