package shirates.core.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.exception.TestNGException
import shirates.core.logging.Message
import shirates.core.logging.TestLog
import shirates.core.utility.image.ImageMatchResult
import shirates.core.utility.image.saveImage
import shirates.core.utility.sync.SyncUtility

/**
 * isImage
 */
fun TestElement.isImage(
    expression: String,
    cropImage: Boolean = true
): ImageMatchResult {

    val testElement = this

    var sel = TestDriver.screenInfo.getSelector(expression = expression)
    if (sel.image.isNullOrBlank()) {
        sel = Selector("image=$expression.png")
    }

    val context = TestDriverCommandContext(testElement)
    var r = ImageMatchResult(result = false)
    context.execBooleanCommand(subject = expression) {

        r = isImageCore(
            selector = sel,
            cropImage = cropImage
        )
    }

    TestDriver.lastElement = testElement

    return r
}

internal fun TestElement.isImageCore(
    selector: Selector,
    cropImage: Boolean = true
): ImageMatchResult {
    if (this.isEmpty) {
        return ImageMatchResult(result = false)
    }
    if (cropImage) {
        silent {
            this.cropImage(save = true)
        }
    }
    val image = this.lastCropInfo?.croppedImage
    val imageMatchResult = selector.evaluateImageEqualsTo(image = image)
    if (imageMatchResult.result.not() && cropImage) {
        silent {
            val s = selector.image!!.removeSuffix(".png")
            val fileName = "$s${testDrive.imageSizeProfile}.png"
            imageMatchResult.image?.saveImage(TestLog.directoryForLog.resolve(fileName).toFile())

            val fileName2 = "${TestLog.lines.count() + 1}_$fileName"
            imageMatchResult.templateImage?.saveImage(TestLog.directoryForLog.resolve(fileName2).toFile())
        }
    }
    return imageMatchResult
}

/**
 * isContainingImage
 */
fun TestElement.isContainingImage(
    expression: String
): ImageMatchResult {

    val testElement = this

    var sel = TestDriver.screenInfo.getSelector(expression = expression)
    if (sel.image.isNullOrBlank()) {
        sel = Selector("image=$expression")
    }

    val context = TestDriverCommandContext(testElement)
    var r = ImageMatchResult(result = false)
    context.execBooleanCommand(subject = expression) {

        silent {
            testElement.cropImage(save = true)
            val image = testElement.lastCropInfo?.croppedImage
            r = sel.evaluateImageContainedIn(image = image)
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
        waitSeconds = waitSeconds,
        action = action
    )

    TestDriver.lastElement = testElement

    return TestDriver.lastElement
}

/**
 * imageIs
 */
fun TestElement.imageIs(
    expression: String = this.selector.toString(),
    waitSeconds: Double = testContext.waitSecondsForAnimationComplete,
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
        waitSeconds = waitSeconds,
        action = action
    )

    TestDriver.lastElement = testElement

    return TestDriver.lastElement
}

/**
 * imageIsNot
 */
fun TestElement.imageIsNot(
    expression: String,
    waitSeconds: Double = testContext.waitSecondsForAnimationComplete,
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
        waitSeconds = waitSeconds,
        action = action,
        negation = true
    )

    TestDriver.lastElement = refreshLastElement()

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
