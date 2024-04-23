package shirates.core.driver.commandextension

import shirates.core.Const
import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.exception.TestNGException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Message
import shirates.core.logging.TestLog
import shirates.core.utility.image.ImageMatchResult
import shirates.core.utility.image.saveImage

/**
 * isImage
 */
fun TestElement.isImage(
    expression: String,
    threshold: Double = PropertiesManager.imageMatchingThreshold,
    cropImage: Boolean = true,
): ImageMatchResult {

    val testElement = this

    var sel = TestDriver.screenInfo.getSelector(expression = expression)
    if (sel.image.isNullOrBlank()) {
        sel = Selector("image=$expression.png")
    }

    val context = TestDriverCommandContext(testElement)
    var imageMatchResult = ImageMatchResult(result = false, templateSubject = expression)
    context.execBooleanCommand(subject = expression) {

        imageMatchResult = isImageCore(
            selector = sel,
            threshold = threshold,
            cropImage = cropImage
        )
    }

    TestDriver.lastElement = testElement

    return imageMatchResult
}

internal fun TestElement.isImageCore(
    selector: Selector,
    threshold: Double = PropertiesManager.imageMatchingThreshold,
    cropImage: Boolean = true
): ImageMatchResult {
    if (this.isEmpty) {
        return ImageMatchResult(result = false, templateSubject = selector.toString())
    }
    if (cropImage) {
        silent {
            this.cropImage(save = true)
        }
    }
    val image = this.lastCropInfo?.croppedImage
    val imageMatchResult = selector.evaluateImageEqualsTo(image = image, threshold = threshold)
    if (imageMatchResult.result.not() && cropImage) {
        silent {
            val s = selector.image!!.removeSuffix(".png")
            val fileName = "$s${testDrive.imageProfile}.png"
            imageMatchResult.image?.saveImage(TestLog.directoryForLog.resolve(fileName).toFile())

            val fileName2 = "${TestLog.lines.count() + 1}_$fileName"
            imageMatchResult.templateImage?.saveImage(TestLog.directoryForLog.resolve(fileName2).toFile())
        }
    }
    imageMatchResult.templateSubject = selector.toString()
    return imageMatchResult
}

/**
 * isContainingImage
 */
fun TestElement.isContainingImage(
    expression: String,
    threshold: Double = PropertiesManager.imageMatchingThreshold,
): ImageMatchResult {

    val testElement = this

    var sel = TestDriver.screenInfo.getSelector(expression = expression)
    if (sel.image.isNullOrBlank()) {
        sel = Selector("image=$expression")
    }

    val context = TestDriverCommandContext(testElement)
    var r = ImageMatchResult(result = false, templateSubject = expression)
    context.execBooleanCommand(subject = expression) {

        silent {
            testElement.cropImage(save = true)
            val image = testElement.lastCropInfo?.croppedImage
            r = sel.evaluateImageContainedIn(image = image, threshold = threshold)
            r.templateSubject = expression
        }
    }

    TestDriver.lastElement = testElement

    return r
}

/**
 * imageContains
 */
fun TestElement.imageContains(
    expression: String,
    waitSeconds: Double = testContext.waitSecondsForAnimationComplete,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val command = "imageContains"

    val testElement = this
    val assertMessage = Message.message(
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
        durationSeconds = waitSeconds,
        action = action
    )

    TestDriver.lastElement = testElement

    if (func != null) {
        func(this)
    }

    return TestDriver.lastElement
}

/**
 * imageIs
 */
fun TestElement.imageIs(
    expression: String = this.selector.toString(),
    waitSeconds: Double = testContext.shortWaitSeconds,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val command = "imageIs"

    val testElement = this
    val assertMessage = Message.message(id = command, subject = testElement.selector.toString(), expected = expression)
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
        durationSeconds = waitSeconds,
        action = action
    )

    TestDriver.lastElement = testElement

    if (func != null) {
        func(this)
    }

    return TestDriver.lastElement
}

/**
 * imageIsNot
 */
fun TestElement.imageIsNot(
    expression: String,
    waitSeconds: Double = testContext.waitSecondsForAnimationComplete,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val command = "imageIsNot"

    val testElement = this
    val assertMessage = Message.message(id = command, subject = testElement.selector.toString(), expected = expression)
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
        durationSeconds = waitSeconds,
        action = action,
        negation = true
    )

    TestDriver.lastElement = refreshLastElement()

    if (func != null) {
        func(this)
    }

    return TestDriver.lastElement
}

internal fun imageAssertionCore(
    command: String,
    expectedSelector: Selector,
    testElement: TestElement,
    scrollableElement: TestElement? = null,
    scrollFunc: (() -> Unit)? = null,
    direction: ScrollDirection = CodeExecutionContext.withScrollDirection ?: ScrollDirection.Down,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    startMarginRatio: Double = testContext.scrollStartMarginRatio(direction),
    endMarginRatio: Double = testContext.scrollEndMarginRatio(direction),
    edgeSelector: String? = null,
    maxLoopCount: Int = testContext.scrollMaxCount,
    repeat: Int = 1,
    intervalSeconds: Double = Const.SWIPE_INTERVAL_SECONDS,
    assertMessage: String,
    action: () -> ImageMatchResult,
    negation: Boolean = false
) {

    imageAssertionCoreCore(
        command = command,
        expectedSelector = expectedSelector,
        testElement = testElement,
        scrollableElement = scrollableElement,
        scrollFunc = scrollFunc,
        direction = direction,
        durationSeconds = durationSeconds,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio,
        edgeSelector = edgeSelector,
        maxLoopCount = maxLoopCount,
        repeat = repeat,
        intervalSeconds = intervalSeconds,
        assertMessage = assertMessage,
        action = action,
        negation = negation
    )
}

internal fun imageAssertionCore(
    command: String,
    expression: String,
    testElement: TestElement,
    scrollableElement: TestElement? = null,
    scrollFunc: (() -> Unit)? = null,
    direction: ScrollDirection = CodeExecutionContext.withScrollDirection ?: ScrollDirection.Down,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    startMarginRatio: Double = testContext.scrollStartMarginRatio(direction),
    endMarginRatio: Double = testContext.scrollEndMarginRatio(direction),
    edgeSelector: String? = null,
    maxLoopCount: Int = testContext.scrollMaxCount,
    repeat: Int = 1,
    intervalSeconds: Double = Const.SWIPE_INTERVAL_SECONDS,
    assertMessage: String,
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
        scrollableElement = scrollableElement,
        scrollFunc = scrollFunc,
        direction = direction,
        durationSeconds = durationSeconds,
        startMarginRatio = startMarginRatio,
        endMarginRatio = endMarginRatio,
        edgeSelector = edgeSelector,
        maxLoopCount = maxLoopCount,
        repeat = repeat,
        intervalSeconds = intervalSeconds,
        assertMessage = assertMessage,
        action = action,
        negation = negation
    )
}

internal fun imageAssertionCoreCore(
    command: String,
    expectedSelector: Selector,
    testElement: TestElement,
    scrollFunc: (() -> Unit)? = null,
    scrollableElement: TestElement?,
    direction: ScrollDirection,
    durationSeconds: Double,
    startMarginRatio: Double,
    endMarginRatio: Double,
    edgeSelector: String?,
    maxLoopCount: Int,
    repeat: Int,
    intervalSeconds: Double,
    assertMessage: String,
    negation: Boolean,
    action: () -> ImageMatchResult,
) {
    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = testElement.selector.toString()) {

        var matchResult = ImageMatchResult(result = false, templateSubject = expectedSelector.toString())
        var r = false
        testDrive.doUntilScrollStopCore(
            scrollFunc = scrollFunc,
            direction = direction,
            scrollableElement = scrollableElement,
            durationSeconds = durationSeconds,
            startMarginRatio = startMarginRatio,
            endMarginRatio = endMarginRatio,
            edgeSelector = edgeSelector,
            maxLoopCount = maxLoopCount,
            repeat = repeat,
            intervalSeconds = intervalSeconds
        ) {
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
