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
    syncCache: Boolean = true
): TestElement {

    val command = "swipeTo"
    val sel = getSelector(expression = expression)
    val message = message(id = command, subject = subject, to = "$sel")

    val context = TestDriverCommandContext(this)
    context.execOperateCommand(command = command, message = message, subject = subject, arg1 = expression) {
        val e = TestDriver.select(selector = sel, syncCache = syncCache)

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
    syncCache: Boolean = true
): TestElement {

    return swipeTo(
        expression = expression,
        durationSeconds = durationSeconds,
        marginRatio = marginRatio,
        adjust = true,
        syncCache = syncCache
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
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): TestElement {

    val command = "swipeToTop"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(this)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        val b = this.bounds
        val startOffsetY = (startOffsetRatio * b.height).toInt()
        swipePointToPoint(
            startX = b.centerX,
            startY = b.centerY + startOffsetY,
            endX = b.centerX,
            endY = viewport.top,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this.refreshThisElement()
}

/**
 * flickToTop
 */
fun TestElement.flickToTop(
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1
): TestElement {

    val command = "flickToTop"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(this)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        swipeToTop(
            startOffsetRatio = startOffsetRatio,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this.refreshThisElement()
}

/**
 * swipeToBottom
 */
fun TestElement.swipeToBottom(
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): TestElement {

    val command = "swipeToBottom"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(this)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        val b = bounds
        val startOffsetY = (startOffsetRatio * b.height).toInt()
        swipePointToPoint(
            startX = b.centerX,
            startY = b.centerY + startOffsetY,
            endX = b.centerX,
            endY = viewport.bottom,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this.refreshThisElement()
}

/**
 * flickToBottom
 */
fun TestElement.flickToBottom(
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1
): TestElement {

    val command = "flickToBottom"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(this)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        swipeToBottom(
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
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): TestElement {

    val command = "swipeToCenter"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(this)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        val b = bounds
        swipePointToPoint(
            startX = b.centerX,
            startY = b.centerY,
            endX = viewport.centerX,
            endY = viewport.centerY,
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
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): TestElement {

    val command = "swipeToRight"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(this)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        val b = bounds
        val offsetX = (startOffsetRatio * b.width).toInt()
        swipePointToPoint(
            startX = b.centerX + offsetX,
            startY = b.centerY,
            endX = viewport.right,
            endY = b.centerY,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this.refreshThisElement()
}

/**
 * flickToRight
 */
fun TestElement.flickToRight(
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1
): TestElement {

    val command = "flickToRight"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(this)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        swipeToRight(
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
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1
): TestElement {

    val command = "swipeToLeft"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(this)
    context.execOperateCommand(command = command, message = message, arg1 = subject) {
        val b = bounds
        val offsetX = (startOffsetRatio * b.width).toInt()
        swipePointToPoint(
            startX = b.centerX + offsetX,
            startY = b.centerY,
            endX = viewport.left,
            endY = b.centerY,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this.refreshThisElement()
}

/**
 * flickToLeft
 */
fun TestElement.flickToLeft(
    startOffsetRatio: Double = 0.0,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1
): TestElement {

    val command = "flickToLeft"
    val message = message(id = command, subject = subject)

    val context = TestDriverCommandContext(this)
    context.execOperateCommand(command = command, message = message, subject = subject) {
        swipeToLeft(
            startOffsetRatio = startOffsetRatio,
            durationSeconds = durationSeconds,
            repeat = repeat
        )
    }

    return this.refreshThisElement()
}
