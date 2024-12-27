package shirates.core.vision.driver.commandextension

import shirates.core.driver.TestDriver
import shirates.core.driver.TestDriver.lastVisionElement
import shirates.core.driver.TestDriver.visionRootElement
import shirates.core.driver.testContext
import shirates.core.logging.TestLog
import shirates.core.utility.image.Rectangle
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement

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

