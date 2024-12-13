package shirates.core.vision.driver

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.*
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.commandextension.silent
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.rect
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.branchextension.lastScreenshotImage

/**
 * getThisOrIt
 */
fun VisionDrive.getThisOrIt(): VisionElement {

    if (this is VisionElement) {
        return this
    }
    return TestDriver.lastVisionElement
}

///**
// * getSelector
// */
//fun VisionDrive.getSelector(expression: String): Selector {
//
//    return testDrive.getSelector(expression = expression)
//}

/**
 * screenshot
 */
fun VisionDrive.screenshot(
    force: Boolean = false,
    onChangedOnly: Boolean = testContext.onChangedOnly,
    filename: String? = null,
    withXmlSource: Boolean = TestLog.enableXmlSourceDump
): VisionElement {

    TestDriver.screenshot(
        force = force,
        onChangedOnly = onChangedOnly,
        filename = filename,
        withXmlSource = withXmlSource
    )

    val v = getThisOrIt()


    return lastElement
}

/**
 * tap
 */
fun VisionDrive.tap(
    expression: String,
    language: String = PropertiesManager.logLanguage,
    rect: Rectangle = lastScreenshotImage!!.rect,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    holdSeconds: Double = TestDriver.testContext.tapHoldSeconds,
): VisionElement {

    if (CodeExecutionContext.isInCell && this is VisionElement) {
        throw NotImplementedError()
//        val e = this.innerWidget(expression = expression)
//        e.tap()
//        return lastElement
    }

    val testElement = rootElement

    val sel = getSelector(expression = expression)

    val command = "tap"
    val message = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(testElement)
    var v = VisionElement.emptyElement
    context.execOperateCommand(command = command, message = message, subject = "$sel") {

        val targetElement = detectCore(
            selector = sel,
            language = language,
            rect = rect,
            waitSeconds = waitSeconds,
            allowScroll = false,
            swipeToCenter = false,
            throwsException = true
        )

        val tapFunc = {
            testDrive.silent {
                v = targetElement.tap(holdSeconds = holdSeconds)
            }
        }

        tapFunc()
    }
    if (TestMode.isNoLoadRun) {
        vision.lastElement = v
    }

    return v
}

