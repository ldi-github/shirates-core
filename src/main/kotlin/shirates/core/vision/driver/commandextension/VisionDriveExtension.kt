package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestDriver
import shirates.core.driver.TestDriver.lastVisionElement
import shirates.core.driver.TestDriver.visionRootElement
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.commandextension.silent
import shirates.core.driver.testContext
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.image.Rectangle
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement

/**
 * it
 */
val VisionDrive.it: VisionElement
    get() {
        return lastVisionElement
    }

/**
 * rootElement
 */
val VisionDrive.rootElement: VisionElement
    get() {
        return TestDriver.visionRootElement
    }

/**
 * screenRect
 */
val VisionDrive.screenRect: Rectangle
    get() {
        return visionRootElement.rect
    }

/**
 * getThisOrIt
 */
fun VisionDrive.getThisOrIt(): VisionElement {

    if (this is VisionElement) {
        return this
    }
    return TestDriver.lastVisionElement
}

/**
 * getThisOrRoot
 */
fun VisionDrive.getThisOrRoot(): VisionElement {

    if (this is VisionElement) {
        return this
    }
    return rootElement
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
    withXmlSource: Boolean = TestLog.enableXmlSourceDump,
): VisionElement {

    TestDriver.screenshot(
        force = force,
        onChangedOnly = onChangedOnly,
        filename = filename,
        withXmlSource = withXmlSource,
    )

    val v = getThisOrIt()


    return TestDriver.visionRootElement
}

/**
 * tap
 */
fun VisionDrive.tap(
    expression: String,
    removeChars: String? = null,
    language: String = PropertiesManager.logLanguage,
    directAccessCompletion: Boolean = true,
    waitSeconds: Double = testContext.syncWaitSeconds,
    intervalSeconds: Double = testContext.syncIntervalSeconds,
    holdSeconds: Double = TestDriver.testContext.tapHoldSeconds,
): VisionElement {

    if (CodeExecutionContext.isInCell && this is VisionElement) {
        throw NotImplementedError()
    }

    val sel = getSelector(expression = expression)

    val command = "tap"
    val message = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    var v = VisionElement.emptyElement
    context.execOperateCommand(command = command, message = message, subject = "$sel") {

        val targetElement = detectCore(
            selector = sel,
            removeChars = removeChars,
            language = language,
            directAccessCompletion = directAccessCompletion,
            waitSeconds = waitSeconds,
            intervalSeconds = intervalSeconds,
            allowScroll = false,
            swipeToCenter = false,
            throwsException = true
        )

        val tapFunc = {
            silent {
                v = targetElement.tap(holdSeconds = holdSeconds)
            }
        }

        tapFunc()
    }
    if (TestMode.isNoLoadRun) {
        lastElement = v
    }

    return v
}

