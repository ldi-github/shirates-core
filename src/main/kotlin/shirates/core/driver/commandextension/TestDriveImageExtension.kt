package shirates.core.driver.commandextension

import shirates.core.driver.*
import shirates.core.logging.Message.message
import shirates.core.utility.image.ImageMatchResult

/**
 * findImage
 */
fun TestDrive.findImage(
    expression: String,
    throwsException: Boolean = false,
    useCache: Boolean = testContext.useCache,
    log: Boolean = true
): ImageMatchResult {

    val command = "findImage"

    return findImageCore(
        command = command,
        expression = expression,
        throwsException = throwsException,
        useCache = useCache,
        log = log
    )
}

internal fun TestDrive.findImageCore(
    command: String,
    expression: String,
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
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
    useCache: Boolean = testContext.useCache,
    log: Boolean = true
): ImageMatchResult {

    val command = "findImageWithScrollDown"

    var result = ImageMatchResult(false, templateSubject = expression)
    withScrollDown(
        scrollFrame = scrollFrame,
        scrollableElement = scrollableElement,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
    ) {
        result = findImageCore(
            command = command,
            expression = expression,
            throwsException = throwsException,
            useCache = useCache,
            log = log
        )
    }
    return result
}

/**
 * findImageWithScrollUp
 */
fun TestDrive.findImageWithScrollUp(
    expression: String,
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
    useCache: Boolean = testContext.useCache,
    log: Boolean = true
): ImageMatchResult {

    val command = "findImageWithScrollUp"

    var result = ImageMatchResult(result = false, templateSubject = expression)
    withScrollUp(
        scrollFrame = scrollFrame,
        scrollableElement = scrollableElement,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
    ) {
        result = findImageCore(
            command = command,
            expression = expression,
            throwsException = throwsException,
            useCache = useCache,
            log = log
        )
    }
    return result
}

/**
 * findImageWithScrollRight
 */
fun TestDrive.findImageWithScrollRight(
    expression: String,
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
    useCache: Boolean = testContext.useCache,
    log: Boolean = true
): ImageMatchResult {

    val command = "findImageWithScrollRight"

    var result = ImageMatchResult(result = false, templateSubject = expression)
    withScrollRight(
        scrollFrame = scrollFrame,
        scrollableElement = scrollableElement,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
    ) {
        result = findImageCore(
            command = command,
            expression = expression,
            throwsException = throwsException,
            useCache = useCache,
            log = log
        )
    }
    return result
}

/**
 * findImageWithScrollLeft
 */
fun TestDrive.findImageWithScrollLeft(
    expression: String,
    scrollFrame: String = "",
    scrollableElement: TestElement? = null,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollHorizontalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollHorizontalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
    useCache: Boolean = testContext.useCache,
    log: Boolean = true
): ImageMatchResult {

    val command = "findImageWithScrollLeft"

    var result = ImageMatchResult(result = false, templateSubject = expression)
    withScrollLeft(
        scrollFrame = scrollFrame,
        scrollableElement = scrollableElement,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
    ) {
        result = findImageCore(
            command = command,
            expression = expression,
            throwsException = throwsException,
            useCache = useCache,
            log = log
        )
    }
    return result
}
