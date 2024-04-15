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
    scrollable: String = TestDriver.screenInfo.scrollInfo.scrollable,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): TestElement {

    val scrollFrame =
        if (ofScreen) viewElement
        else getScrollableElement(scrollable)
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
            endY = scrollFrame.bounds.top + endMargin + headerBottom,
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
    scrollable: String = TestDriver.screenInfo.scrollInfo.scrollable,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): TestElement {

    return swipeToTop(
        ofScreen = ofScreen,
        scrollable = scrollable,
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
    scrollable: String = TestDriver.screenInfo.scrollInfo.scrollable,
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
            scrollable = scrollable,
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
    scrollable: String = TestDriver.screenInfo.scrollInfo.scrollable,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): TestElement {

    val scrollFrame =
        if (ofScreen) viewElement
        else getScrollableElement(scrollable)

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
            endY = scrollFrame.bounds.bottom - endMargin,
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
    scrollable: String = TestDriver.screenInfo.scrollInfo.scrollable,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): TestElement {

    return swipeToBottom(
        ofScreen = ofScreen,
        scrollable = scrollable,
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
    scrollable: String = TestDriver.screenInfo.scrollInfo.scrollable,
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
            scrollable = scrollable,
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
    scrollable: String = TestDriver.screenInfo.scrollInfo.scrollable,
    axis: Axis = Axis.Vertical,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
): TestElement {

    val scrollFrame =
        if (ofScreen) viewElement
        else getScrollableElement(scrollable)
    val endX = scrollFrame.bounds.centerX
    val endY =
        if (ofScreen) viewBounds.height / 2
        else scrollFrame.bounds.centerY

    val command = "swipeToCenter"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(this)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        val b = bounds
        val startX = b.centerX
        val startY = b.centerY
        val skip = (axis == Axis.Vertical && Math.abs(startY - endY) < 16) ||
                (axis == Axis.Horizontal && Math.abs(startX - endX) < 16)

        if (skip.not()) {
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
    scrollable: String = TestDriver.screenInfo.scrollInfo.scrollable,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
): TestElement {

    return swipeToCenter(
        ofScreen = ofScreen,
        scrollable = scrollable,
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
    scrollable: String = TestDriver.screenInfo.scrollInfo.scrollable,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
): TestElement {

    return swipeToCenter(
        ofScreen = ofScreen,
        scrollable = scrollable,
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
    scrollable: String = TestDriver.screenInfo.scrollInfo.scrollable,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): TestElement {

    val scrollFrame =
        if (ofScreen) viewElement
        else getScrollableElement(scrollable)

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
            endX = scrollFrame.bounds.right - endMargin,
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
    scrollable: String = TestDriver.screenInfo.scrollInfo.scrollable,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): TestElement {

    return swipeToRight(
        ofScreen = ofScreen,
        scrollable = scrollable,
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
    scrollable: String = TestDriver.screenInfo.scrollInfo.scrollable,
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
            scrollable = scrollable,
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
    scrollable: String = TestDriver.screenInfo.scrollInfo.scrollable,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): TestElement {

    val scrollFrame =
        if (ofScreen) viewElement
        else getScrollableElement(scrollable)

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
            endX = scrollFrame.bounds.left + endMargin,
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
    scrollable: String = TestDriver.screenInfo.scrollInfo.scrollable,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): TestElement {

    return swipeToLeft(
        ofScreen = ofScreen,
        scrollable = scrollable,
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
    scrollable: String = TestDriver.screenInfo.scrollInfo.scrollable,
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
            scrollable = scrollable,
            startOffsetRatio = startOffsetRatio,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this.refreshThisElement()
}
