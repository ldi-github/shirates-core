package shirates.core.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.exception.TestNGException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.image.ImageMatchResult
import shirates.core.utility.image.saveImage
import shirates.core.utility.sync.SyncUtility

/**
 * findImage
 */
fun TestDrive.findImage(
    expression: String,
    scroll: Boolean = CodeExecutionContext.withScrollDirection != null,
    direction: ScrollDirection = CodeExecutionContext.withScrollDirection ?: ScrollDirection.Down,
    throwsException: Boolean = false,
    syncCache: Boolean = true,
    log: Boolean = true
): ImageMatchResult {

    val command = "findImage"

    return findImageCore(
        command = command,
        expression = expression,
        scroll = scroll,
        direction = direction,
        throwsException = throwsException,
        syncCache = syncCache,
        log = log
    )
}

internal fun TestDrive.findImageCore(
    command: String,
    expression: String,
    scroll: Boolean = false,
    direction: ScrollDirection = ScrollDirection.Down,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = false,
    syncCache: Boolean = true,
    log: Boolean = false
): ImageMatchResult {

    val testElement = getTestElement()

    val sel = TestDriver.expandExpression(expression = expression)
    val message = message(id = command, subject = sel.toString())

    val context = TestDriverCommandContext(testElement)
    var r = ImageMatchResult(result = false)
    val action = {
        context.execOperateCommand(command = command, message = message, subject = sel.toString()) {
            r = TestDriver.findImage(
                expression = expression,
                scroll = scroll,
                direction = direction,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollMaxCount = scrollMaxCount,
                throwsException = throwsException,
                syncCache = syncCache
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
    scrollStartMarginRatio: Double = testContext.scrollVerticalMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
    syncCache: Boolean = true,
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
        scrollMaxCount = scrollMaxCount,
        throwsException = throwsException,
        syncCache = syncCache,
        log = log
    )
}

/**
 * findImageWithScrollUp
 */
fun TestDrive.findImageWithScrollUp(
    expression: String,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
    syncCache: Boolean = true,
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
        scrollMaxCount = scrollMaxCount,
        throwsException = throwsException,
        syncCache = syncCache,
        log = log
    )
}

/**
 * findImageWithScrollRight
 */
fun TestDrive.findImageWithScrollRight(
    expression: String,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
    syncCache: Boolean = true,
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
        scrollMaxCount = scrollMaxCount,
        throwsException = throwsException,
        syncCache = syncCache,
        log = log
    )
}

/**
 * findImageWithScrollLeft
 */
fun TestDrive.findImageWithScrollLeft(
    expression: String,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
    syncCache: Boolean = true,
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
        scrollMaxCount = scrollMaxCount,
        throwsException = throwsException,
        syncCache = syncCache,
        log = log
    )
}

/**
 * isImage
 */
fun TestDrive.isImage(
    expression: String
): ImageMatchResult {

    val testElement = getTestElement()

    var sel = TestDriver.screenInfo.getSelector(expression = expression)
    if (sel.image.isNullOrBlank()) {
        sel = Selector("image=$expression")
    }

    val context = TestDriverCommandContext(testElement)
    var r = ImageMatchResult(result = false)
    context.execBooleanCommand(subject = expression) {

        silent {
            testElement.cropImage(save = true)
        }
        val image = testElement.lastCropInfo?.croppedImage
        r = sel.evaluateImageEqualsTo(image = image)
    }

    TestDriver.lastElement = testElement

    return r
}

/**
 * isContainingImage
 */
fun TestDrive.isContainingImage(
    expression: String
): ImageMatchResult {

    val testElement = getTestElement()

    var sel = TestDriver.screenInfo.getSelector(expression = expression)
    if (sel.image.isNullOrBlank()) {
        sel = Selector("image=$expression")
    }

    val context = TestDriverCommandContext(testElement)
    var r = ImageMatchResult(result = false)
    context.execBooleanCommand(subject = expression) {

        silent {
            testElement.cropImage(save = true)
        }
        val image = testElement.lastCropInfo?.croppedImage

        r = sel.evaluateImageContainedIn(image = image)
    }

    TestDriver.lastElement = testElement

    return r
}

/**
 * imageContains
 */
fun TestDrive.imageContains(
    expression: String,
    waitSeconds: Double = testContext.waitSecondsForAnimationComplete,
): TestElement {

    val command = "imageContains"

    val testElement = getTestElement()
    val assertMessage = message(
        id = command,
        subject = testElement.selector?.toString() ?: testElement.getUniqueSelector().toString(),
        expected = expression
    )
    if (PropertiesManager.enableImageAssertion.not()) {
        return manual(message = assertMessage)
    }

    val action = {
        isContainingImage(expression)
    }

    imageAssertionCore(
        command = command,
        expression = expression,
        testElement = testElement,
        assertMessage = assertMessage,
        waitSeconds = waitSeconds,
        action = action
    )

    TestDriver.lastElement = testElement

    return TestDriver.lastElement
}

/**
 * imageIs
 */
fun TestDrive.imageIs(
    expression: String,
    waitSeconds: Double = testContext.waitSecondsForAnimationComplete,
): TestElement {

    val command = "imageIs"

    val testElement = getTestElement()
    val assertMessage = message(id = command, subject = testElement.selector.toString(), expected = expression)
    if (PropertiesManager.enableImageAssertion.not()) {
        return manual(message = assertMessage)
    }

    val action = {
        isImage(expression)
    }

    imageAssertionCore(
        command = command,
        expression = expression,
        testElement = testElement,
        assertMessage = assertMessage,
        waitSeconds = waitSeconds,
        action = action
    )

    TestDriver.lastElement = testElement

    return TestDriver.lastElement
}

/**
 * imageIsNot
 */
fun TestDrive.imageIsNot(
    expression: String,
    waitSeconds: Double = testContext.waitSecondsForAnimationComplete,
): TestElement {

    val command = "imageIsNot"

    val testElement = getTestElement()
    val assertMessage = message(id = command, subject = testElement.selector.toString(), expected = expression)
    if (PropertiesManager.enableImageAssertion.not()) {
        return manual(message = assertMessage)
    }

    val action = {
        isImage(expression)
    }

    imageAssertionCore(
        command = command,
        expression = expression,
        testElement = testElement,
        assertMessage = assertMessage,
        waitSeconds = waitSeconds,
        action = action,
        negation = true
    )

    TestDriver.lastElement = getTestElement()

    return TestDriver.lastElement
}

internal fun imageAssertionCore(
    command: String,
    expression: String,
    testElement: TestElement,
    assertMessage: String,
    waitSeconds: Double,
    action: () -> ImageMatchResult,
    negation: Boolean = false
) {

    var sel = TestDriver.screenInfo.getSelector(expression = expression)
    if (sel.image.isNullOrBlank()) {
        sel = Selector("image=$expression")
    }
    imageAssertionCoreCore(
        command = command,
        expectedSelector = sel,
        testElement = testElement,
        assertMessage = assertMessage,
        waitSeconds = waitSeconds,
        action = action,
        negation = negation
    )
}

internal fun imageAssertionCoreCore(
    command: String,
    expectedSelector: Selector,
    testElement: TestElement,
    assertMessage: String,
    waitSeconds: Double,
    action: () -> ImageMatchResult,
    negation: Boolean
) {
    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = testElement.selector.toString()) {

        var matchResult = ImageMatchResult(result = false)
        var r = false
        SyncUtility.doUntilTrue(waitSeconds = waitSeconds) {
            matchResult = action()
            r = matchResult.result
            if (negation) {
                r = r.not()
            }
            r
        }
        if (r) {
            TestLog.ok(
                message = assertMessage,
                arg1 = expectedSelector.toString()
            )
        } else {
            val croppedImageFileName = "${TestLog.lines.count()}_cropped_image"
            testElement.lastCropInfo?.croppedImage?.saveImage("${TestLog.directoryForLog.resolve(croppedImageFileName)}")

            val templateImageFileName = "${TestLog.lines.count()}_template_image"
            expectedSelector.templateImage?.saveImage("${TestLog.directoryForLog.resolve(templateImageFileName)}")

            TestDriver.lastElement.lastError = TestNGException("$assertMessage ($matchResult)")
            throw TestDriver.lastElement.lastError!!
        }
    }
}
