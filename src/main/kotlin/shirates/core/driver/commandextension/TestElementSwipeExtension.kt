package shirates.core.driver.commandextension

import shirates.core.driver.*
import shirates.core.driver.TestElementCache.viewElement
import shirates.core.logging.Message.message

/**
 * swipeTo
 */
fun TestElement.swipeTo(
    expression: String,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    marginRatio: Double = testContext.swipeMarginRatio,
    adjust: Boolean = false,
    repeat: Int = 1,
    useCache: Boolean = testContext.useCache
): TestElement {

    val command = "swipeTo"
    val sel = getSelector(expression = expression)
    val message = message(id = command, subject = subject, to = "$sel")

    val context = TestDriverCommandContext(this)
    context.execOperateCommand(command = command, message = message, subject = subject, arg1 = expression) {
        val e = TestDriver.select(
            swipeToCenter = false,
            selector = sel,
            useCache = useCache
        )

        swipeElementToElement(
            startElement = this,
            endElement = e,
            durationSeconds = durationSeconds,
            marginRatio = marginRatio,
            adjust = adjust,
            repeat = repeat
        )
    }

    return this.refreshThisElement()
}

/**
 * swipeToAdjust
 */
fun TestElement.swipeToAdjust(
    expression: String,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    marginRatio: Double = testContext.swipeMarginRatio,
    useCache: Boolean = testContext.useCache
): TestElement {

    return swipeTo(
        expression = expression,
        durationSeconds = durationSeconds,
        marginRatio = marginRatio,
        adjust = true,
        useCache = useCache
    )
}

/**
 * swipeVerticalTo
 */
fun TestElement.swipeVerticalTo(
    endY: Int,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): TestElement {

    val command = "swipeVerticalTo"
    val message = message(id = command, subject = subject, to = "$endY")

    val context = TestDriverCommandContext(this)
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

    return this.refreshThisElement()
}

/**
 * swipeHorizontalTo
 */
fun TestElement.swipeHorizontalTo(
    endX: Int,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): TestElement {

    val command = "swipeHorizontalTo"
    val message = message(id = command, subject = subject, to = "$endX")

    val context = TestDriverCommandContext(this)
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

    return this.refreshThisElement()
}

/**
 * swipeToTop
 */
fun TestElement.swipeToTop(
    ofScreen: Boolean = false,
    scrollFrame: String = TestDriver.screenInfo.scrollInfo.scrollFrame,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): TestElement {

    val frame =
        if (ofScreen) viewElement
        else getScrollableElement(scrollFrame)
    val headerBottom = TestDriver.screenInfo.scrollInfo.getHeaderBottom()

    val command = "swipeToTop"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(this)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        val b = bounds
        val startOffsetY = (startOffsetRatio * b.height).toInt()
        val endMargin = if (stickToEdge) b.height / 2 else 0
        swipePointToPoint(
            startX = b.centerX,
            startY = b.centerY + startOffsetY,
            endX = b.centerX,
            endY = frame.bounds.top + endMargin + headerBottom,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this.refreshThisElement()
}

/**
 * swipeToTopOfScreen
 */
fun TestElement.swipeToTopOfScreen(
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): TestElement {

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
fun TestElement.swipeOutTop(
    ofScreen: Boolean = false,
    scrollFrame: String = TestDriver.screenInfo.scrollInfo.scrollFrame,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): TestElement {

    return swipeToTop(
        ofScreen = ofScreen,
        scrollFrame = scrollFrame,
        startOffsetRatio = startOffsetRatio,
        durationSeconds = durationSeconds,
        repeat = repeat,
        stickToEdge = false
    )
}

/**
 * flickToTop
 */
fun TestElement.flickToTop(
    ofScreen: Boolean = false,
    scrollFrame: String = TestDriver.screenInfo.scrollInfo.scrollFrame,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1
): TestElement {

    val command = "flickToTop"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(this)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        swipeOutTop(
            ofScreen = ofScreen,
            scrollFrame = scrollFrame,
            startOffsetRatio = startOffsetRatio,
            durationSeconds = durationSeconds,
            repeat = repeat,
        )
    }

    return this.refreshThisElement()
}

/**
 * swipeToBottom
 */
fun TestElement.swipeToBottom(
    ofScreen: Boolean = false,
    scrollFrame: String = TestDriver.screenInfo.scrollInfo.scrollFrame,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): TestElement {

    val frame =
        if (ofScreen) viewElement
        else getScrollableElement(scrollFrame)

    val command = "swipeToBottom"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(this)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        val b = bounds
        val startOffsetY = (startOffsetRatio * b.height).toInt()
        val endMargin = if (stickToEdge) b.height / 2 else 0
        swipePointToPoint(
            startX = b.centerX,
            startY = b.centerY + startOffsetY,
            endX = b.centerX,
            endY = frame.bounds.bottom - endMargin,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this.refreshThisElement()
}

/**
 * swipeToBottomOfScreen
 */
fun TestElement.swipeToBottomOfScreen(
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): TestElement {

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
fun TestElement.swipeOutBottom(
    ofScreen: Boolean = false,
    scrollFrame: String = TestDriver.screenInfo.scrollInfo.scrollFrame,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): TestElement {

    return swipeToBottom(
        ofScreen = ofScreen,
        scrollFrame = scrollFrame,
        startOffsetRatio = startOffsetRatio,
        durationSeconds = durationSeconds,
        repeat = repeat,
        stickToEdge = false
    )
}

/**
 * flickToBottom
 */
fun TestElement.flickToBottom(
    ofScreen: Boolean = false,
    scrollFrame: String = TestDriver.screenInfo.scrollInfo.scrollFrame,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1
): TestElement {

    val command = "flickToBottom"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(this)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        swipeOutBottom(
            ofScreen = ofScreen,
            scrollFrame = scrollFrame,
            startOffsetRatio = startOffsetRatio,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this.refreshThisElement()
}

/**
 * swipeToCenter
 */
fun TestElement.swipeToCenter(
    ofScreen: Boolean = false,
    scrollFrame: String = TestDriver.screenInfo.scrollInfo.scrollFrame,
    axis: Axis = Axis.Vertical,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    force: Boolean = false
): TestElement {

    val frame =
        if (ofScreen) viewElement
        else getScrollableElement(scrollFrame)
    val endX =
        if (ofScreen) viewBounds.width / 2 else
            frame.bounds.centerX
    val endY =
        if (ofScreen) viewBounds.height / 2
        else frame.bounds.centerY

    val command = "swipeToCenter"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(this)
    context.execOperateCommand(command = command, message = message, subject = subject) {

        val b = bounds
        val startX = b.centerX
        val startY = b.centerY

        val skip = (axis == Axis.Vertical && Math.abs(startY - endY) < 24) ||
                (axis == Axis.Horizontal && Math.abs(startX - endX) < 24)

        if (force || skip.not()) {
            swipePointToPoint(
                startX = startX,
                startY = startY,
                endX = endX,
                endY = endY,
                durationSeconds = durationSeconds,
                repeat = repeat
            )
        }
    }

    return this.refreshThisElement()
}

/**
 * swipeToCenterVertical
 */
fun TestElement.swipeToCenterVertical(
    ofScreen: Boolean = false,
    scrollFrame: String = TestDriver.screenInfo.scrollInfo.scrollFrame,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
): TestElement {

    return swipeToCenter(
        ofScreen = ofScreen,
        scrollFrame = scrollFrame,
        axis = Axis.Vertical,
        durationSeconds = durationSeconds,
        repeat = repeat
    )
}

/**
 * swipeToCenterHorizontal
 */
fun TestElement.swipeToCenterHorizontal(
    ofScreen: Boolean = false,
    scrollFrame: String = TestDriver.screenInfo.scrollInfo.scrollFrame,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
): TestElement {

    return swipeToCenter(
        ofScreen = ofScreen,
        scrollFrame = scrollFrame,
        axis = Axis.Horizontal,
        durationSeconds = durationSeconds,
        repeat = repeat
    )
}


/**
 * swipeToCenterOfScreen
 */
fun TestElement.swipeToCenterOfScreen(
    durationSeconds: Double = testContext.swipeDurationSeconds,
    axis: Axis = Axis.Vertical,
    repeat: Int = 1,
): TestElement {

    return swipeToCenter(
        ofScreen = true,
        axis = axis,
        durationSeconds = durationSeconds,
        repeat = repeat,
    )
}

/**
 * swipeToRight
 */
fun TestElement.swipeToRight(
    ofScreen: Boolean = false,
    scrollFrame: String = TestDriver.screenInfo.scrollInfo.scrollFrame,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): TestElement {

    val frame =
        if (ofScreen) viewElement
        else getScrollableElement(scrollFrame)

    val command = "swipeToRight"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(this)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        val b = bounds
        val offsetX = (startOffsetRatio * b.width).toInt()
        val endMargin = if (stickToEdge) b.width / 2 else 0
        swipePointToPoint(
            startX = b.centerX + offsetX,
            startY = b.centerY,
            endX = frame.bounds.right - endMargin,
            endY = b.centerY,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this.refreshThisElement()
}

/**
 * swipeToRightOfScreen
 */
fun TestElement.swipeToRightOfScreen(
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): TestElement {

    return swipeToRight(
        ofScreen = true,
        startOffsetRatio = startOffsetRatio,
        durationSeconds = durationSeconds,
        repeat = repeat,
        stickToEdge = stickToEdge
    )
}

/**
 * swipeOutRight
 */
fun TestElement.swipeOutRight(
    ofScreen: Boolean = false,
    scrollFrame: String = TestDriver.screenInfo.scrollInfo.scrollFrame,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): TestElement {

    return swipeToRight(
        ofScreen = ofScreen,
        scrollFrame = scrollFrame,
        startOffsetRatio = startOffsetRatio,
        durationSeconds = durationSeconds,
        repeat = repeat,
        stickToEdge = false
    )
}

/**
 * flickToRight
 */
fun TestElement.flickToRight(
    ofScreen: Boolean = false,
    scrollFrame: String = TestDriver.screenInfo.scrollInfo.scrollFrame,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1
): TestElement {

    val command = "flickToRight"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(this)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        swipeOutRight(
            ofScreen = ofScreen,
            scrollFrame = scrollFrame,
            startOffsetRatio = startOffsetRatio,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this.refreshThisElement()
}

/**
 * swipeToLeft
 */
fun TestElement.swipeToLeft(
    ofScreen: Boolean = false,
    scrollFrame: String = TestDriver.screenInfo.scrollInfo.scrollFrame,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): TestElement {

    val frame =
        if (ofScreen) viewElement
        else getScrollableElement(scrollFrame)

    val command = "swipeToLeft"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(this)
    context.execOperateCommand(command = command, message = message, arg1 = subject) {
        val b = bounds
        val offsetX = (startOffsetRatio * b.width).toInt()
        val endMargin = if (stickToEdge) b.width / 2 else 0
        swipePointToPoint(
            startX = b.centerX + offsetX,
            startY = b.centerY,
            endX = frame.bounds.left + endMargin,
            endY = b.centerY,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this.refreshThisElement()
}

/**
 * swipeToLeftOfScreen
 */
fun TestElement.swipeToLeftOfScreen(
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): TestElement {

    return swipeToLeft(
        ofScreen = true,
        startOffsetRatio = startOffsetRatio,
        durationSeconds = durationSeconds,
        repeat = repeat,
        stickToEdge = stickToEdge
    )
}

/**
 * swipeOutLeft
 */
fun TestElement.swipeOutLeft(
    ofScreen: Boolean = false,
    scrollFrame: String = TestDriver.screenInfo.scrollInfo.scrollFrame,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): TestElement {

    return swipeToLeft(
        ofScreen = ofScreen,
        scrollFrame = scrollFrame,
        startOffsetRatio = startOffsetRatio,
        durationSeconds = durationSeconds,
        repeat = repeat,
        stickToEdge = false
    )
}

/**
 * flickToLeft
 */
fun TestElement.flickToLeft(
    ofScreen: Boolean = false,
    scrollFrame: String = TestDriver.screenInfo.scrollInfo.scrollFrame,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1
): TestElement {

    val command = "flickToLeft"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(this)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        swipeOutLeft(
            ofScreen = ofScreen,
            scrollFrame = scrollFrame,
            startOffsetRatio = startOffsetRatio,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this.refreshThisElement()
}
