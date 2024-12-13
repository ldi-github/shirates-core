package shirates.core.vision.driver

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestDriver
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.testContext
import shirates.core.logging.Message.message
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.rect
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.branchextension.lastScreenshotImage

/**
 * swipeTo
 */
fun VisionElement.swipeTo(
    expression: String,
    language: String = PropertiesManager.logLanguage,
    rect: Rectangle = lastScreenshotImage!!.rect,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
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
            rect = rect,
            waitSeconds = waitSeconds,
            allowScroll = false,
            swipeToCenter = false,
            throwsException = false
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

    v = v.createFromScreenshot()
    return v
}

/**
 * swipeToAdjust
 */
fun VisionElement.swipeToAdjust(
    expression: String,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    marginRatio: Double = testContext.swipeMarginRatio,
): VisionElement {

    return swipeTo(
        expression = expression,
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
    rect: Rectangle = lastScreenshotImage!!.rect,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): VisionElement {

    val frame =
        if (ofScreen) lastScreenshotImage!!.rect
        else rect
    val headerBottom = TestDriver.screenInfo.scrollInfo.getHeaderBottom()

    val command = "swipeToTop"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        val b = this.bounds
        val startOffsetY = (startOffsetRatio * b.height).toInt()
        val endMargin = if (stickToEdge) b.height / 2 else 0
        swipePointToPoint(
            startX = b.centerX,
            startY = b.centerY + startOffsetY,
            endX = b.centerX,
            endY = frame.top + endMargin + headerBottom,
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
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): VisionElement {

    return swipeToTop(
        ofScreen = true,
        startOffsetRatio = startOffsetRatio,
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
    rect: Rectangle = lastScreenshotImage!!.rect,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): VisionElement {

    return swipeToTop(
        ofScreen = ofScreen,
        rect = rect,
        startOffsetRatio = startOffsetRatio,
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
    rect: Rectangle = lastScreenshotImage!!.rect,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1
): VisionElement {

    val command = "flickToTop"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        swipeOutTop(
            ofScreen = ofScreen,
            rect = rect,
            startOffsetRatio = startOffsetRatio,
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
    rect: Rectangle = lastScreenshotImage!!.rect,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): VisionElement {

    val frame =
        if (ofScreen) lastScreenshotImage!!.rect
        else rect

    val command = "swipeToBottom"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        val r = this.bounds.toRectWithRatio()
        val startOffsetY = (startOffsetRatio * r.height).toInt()
        val endMargin = if (stickToEdge) r.height / 2 else 0
        swipePointToPointByPixel(
            startPixelX = r.centerX,
            startPixelY = r.centerY + startOffsetY,
            endPixelX = r.centerX,
            endPixelY = frame.bottom - endMargin,
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
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): VisionElement {

    return swipeToBottom(
        ofScreen = true,
        startOffsetRatio = startOffsetRatio,
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
    rect: Rectangle = lastScreenshotImage!!.rect,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): VisionElement {

    return swipeToBottom(
        ofScreen = ofScreen,
        rect = rect,
        startOffsetRatio = startOffsetRatio,
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
    rect: Rectangle = lastScreenshotImage!!.rect,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1
): VisionElement {

    val command = "flickToBottom"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        swipeOutBottom(
            ofScreen = ofScreen,
            rect = rect,
            startOffsetRatio = startOffsetRatio,
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
    rect: Rectangle = lastScreenshotImage!!.rect,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    force: Boolean = false
): VisionElement {

    val frame =
        if (ofScreen) lastScreenshotImage!!.rect
        else rect
    val endX = frame.centerX
    val endY = frame.centerY

    val command = "swipeToCenter"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject) {

        val r = this.bounds.toRectWithRatio()

        if (force) {
            swipePointToPointByPixel(
                startPixelX = r.centerX,
                startPixelY = r.centerY,
                endPixelX = endX,
                endPixelY = endY,
                durationSeconds = durationSeconds,
                repeat = repeat
            )
        }
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
    rect: Rectangle = lastScreenshotImage!!.rect,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): VisionElement {

    val frame = rect

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
    ofScreen: Boolean = false,
    rect: Rectangle = lastScreenshotImage!!.rect,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): VisionElement {

    return swipeToRight(
        rect = rect,
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
    rect: Rectangle = lastScreenshotImage!!.rect,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1
): VisionElement {

    val command = "flickToRight"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        swipeOutRight(
            rect = rect,
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
    rect: Rectangle = lastScreenshotImage!!.rect,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): VisionElement {

    val frame = rect

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
    rect: Rectangle = lastScreenshotImage!!.rect,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): VisionElement {

    return swipeToLeft(
        rect = rect,
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
    rect: Rectangle = lastScreenshotImage!!.rect,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1
): VisionElement {

    val command = "flickToLeft"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        swipeOutLeft(
            rect = rect,
            startOffsetRatio = startOffsetRatio,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this
}