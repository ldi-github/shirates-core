package shirates.core.driver.commandextension

import shirates.core.driver.*
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
        val e = TestDriver.select(selector = sel, useCache = useCache)

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
    scrollable: String = TestDriver.screenInfo.scrollInfo.scrollable,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): TestElement {

    val scrollableElement = getScrollableElement(scrollable)

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
            endY = scrollableElement.bounds.top + endMargin,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this.refreshThisElement()
}

/**
 * swipeOutTop
 */
fun TestElement.swipeOutTop(
    scrollable: String = TestDriver.screenInfo.scrollInfo.scrollable,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): TestElement {

    return swipeToTop(
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
    scrollable: String = TestDriver.screenInfo.scrollInfo.scrollable,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): TestElement {

    val scrollableElement = getScrollableElement(scrollable)

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
            endY = scrollableElement.bounds.bottom - endMargin,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this.refreshThisElement()
}

/**
 * swipeOutBottom
 */
fun TestElement.swipeOutBottom(
    scrollable: String = TestDriver.screenInfo.scrollInfo.scrollable,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): TestElement {

    return swipeToBottom(
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
    scrollable: String = TestDriver.screenInfo.scrollInfo.scrollable,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): TestElement {

    val scrollableElement = getScrollableElement(scrollable)

    val command = "swipeToCenter"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(this)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        val b = bounds
        swipePointToPoint(
            startX = b.centerX,
            startY = b.centerY,
            endX = scrollableElement.bounds.centerX,
            endY = scrollableElement.bounds.centerY,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this.refreshThisElement()
}

/**
 * swipeToRight
 */
fun TestElement.swipeToRight(
    scrollable: String = TestDriver.screenInfo.scrollInfo.scrollable,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): TestElement {

    val scrollableElement = getScrollableElement(scrollable)

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
            endX = scrollableElement.bounds.right - endMargin,
            endY = b.centerY,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this.refreshThisElement()
}

/**
 * swipeOutRight
 */
fun TestElement.swipeOutRight(
    scrollable: String = TestDriver.screenInfo.scrollInfo.scrollable,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): TestElement {

    return swipeToRight(
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
    scrollable: String = TestDriver.screenInfo.scrollInfo.scrollable,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    stickToEdge: Boolean = true
): TestElement {

    val scrollableElement = getScrollableElement(scrollable)

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
            endX = scrollableElement.bounds.left + endMargin,
            endY = b.centerY,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this.refreshThisElement()
}

/**
 * swipeOutLeft
 */
fun TestElement.swipeOutLeft(
    scrollable: String = TestDriver.screenInfo.scrollInfo.scrollable,
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): TestElement {

    return swipeToLeft(
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
            scrollable = scrollable,
            startOffsetRatio = startOffsetRatio,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this.refreshThisElement()
}
