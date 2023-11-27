package shirates.core.driver.commandextension

import shirates.core.driver.*
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Message.message
import shirates.core.utility.image.ImageMatchResult

/**
 * findImage
 */
fun TestDrive.findImage(
    expression: String,
    scroll: Boolean = CodeExecutionContext.withScrollDirection != null,
    direction: ScrollDirection = CodeExecutionContext.withScrollDirection ?: ScrollDirection.Down,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollStartMarginRatio(direction),
    scrollEndMarginRatio: Double = testContext.scrollEndMarginRatio(direction),
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = false,
    useCache: Boolean = testContext.useCache,
    log: Boolean = true
): ImageMatchResult {

    val command = "findImage"

    return findImageCore(
        command = command,
        expression = expression,
        scroll = scroll,
        direction = direction,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        throwsException = throwsException,
        useCache = useCache,
        log = log
    )
}

internal fun TestDrive.findImageCore(
    command: String,
    expression: String,
    scroll: Boolean,
    direction: ScrollDirection,
    scrollDurationSeconds: Double,
    scrollStartMarginRatio: Double,
    scrollEndMarginRatio: Double,
    scrollMaxCount: Int,
    throwsException: Boolean,
    useCache: Boolean,
    log: Boolean
): ImageMatchResult {

    val testElement = TestDriver.it

    val sel = TestDriver.expandExpression(expression = expression)
    val message = message(id = command, subject = sel.toString())

    val context = TestDriverCommandContext(testElement)
    var r = ImageMatchResult(result = false, templateSubject = expression)
    val action = {
        context.execOperateCommand(command = command, message = message, subject = sel.toString()) {
            r = TestDriver.findImage(
                expression = expression,
                scroll = scroll,
                direction = direction,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
                throwsException = throwsException,
                useCache = useCache
            )
            TestDriver.autoScreenshot()
        }
    }
    if (log) {
        action()
    } else {
        silent {
            action()
        }
    }

    return r
}

/**
 * findImageWithScrollDown
 */
fun TestDrive.findImageWithScrollDown(
    expression: String,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
    useCache: Boolean = testContext.useCache,
    log: Boolean = true
): ImageMatchResult {

    val command = "findImageWithScrollDown"
    val direction = ScrollDirection.Down

    return findImageCore(
        command = command,
        expression = expression,
        scroll = true,
        direction = direction,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        throwsException = throwsException,
        useCache = useCache,
        log = log
    )
}

/**
 * findImageWithScrollUp
 */
fun TestDrive.findImageWithScrollUp(
    expression: String,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
    useCache: Boolean = testContext.useCache,
    log: Boolean = true
): ImageMatchResult {

    val command = "findImageWithScrollUp"
    val direction = ScrollDirection.Up

    return findImageCore(
        command = command,
        expression = expression,
        scroll = true,
        direction = direction,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        throwsException = throwsException,
        useCache = useCache,
        log = log
    )
}

/**
 * findImageWithScrollRight
 */
fun TestDrive.findImageWithScrollRight(
    expression: String,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
    useCache: Boolean = testContext.useCache,
    log: Boolean = true
): ImageMatchResult {

    val command = "findImageWithScrollRight"
    val direction = ScrollDirection.Right

    return findImageCore(
        command = command,
        expression = expression,
        scroll = true,
        direction = direction,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        throwsException = throwsException,
        useCache = useCache,
        log = log
    )
}

/**
 * findImageWithScrollLeft
 */
fun TestDrive.findImageWithScrollLeft(
    expression: String,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
    useCache: Boolean = testContext.useCache,
    log: Boolean = true
): ImageMatchResult {

    val command = "findImageWithScrollLeft"
    val direction = ScrollDirection.Left

    return findImageCore(
        command = command,
        expression = expression,
        scroll = true,
        direction = direction,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        throwsException = throwsException,
        useCache = useCache,
        log = log
    )
}
