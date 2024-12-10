package shirates.core.vision.driver

import shirates.core.driver.*
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.commandextension.silent
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement

/**
 * Returns last accessed VisionElement
 */
var lastVisionElement: VisionElement
    get() {
        return TestDriver.lastVisionElement
    }
    set(value) {
        TestDriver.lastVisionElement = value
    }

/**
 * getThisOrLastVisionElement
 */
fun VisionDrive.getThisOrLastVisionElement(): VisionElement {

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

    val v = getThisOrLastVisionElement()


    return lastVisionElement
}

/**
 * tap
 */
fun VisionDrive.tap(
    expression: String,
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

        val targetElement = detectCore(selector = sel)

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

