package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.Bounds
import shirates.core.driver.TestDriver
import shirates.core.driver.TestDriver.lastVisionElement
import shirates.core.driver.TestDriver.visionRootElement
import shirates.core.driver.testContext
import shirates.core.driver.vision
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
 * screenBounds
 */
val VisionDrive.screenBounds: Bounds
    get() {
        return visionRootElement.bounds
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

/**
 * getSafeArea
 */
fun VisionDrive.getSafeArea(): VisionElement {

    val rootElement = vision.rootElement
    val top = (rootElement.rect.height * PropertiesManager.visionSafeAreaTopRatio).toInt()
    val bottom = (rootElement.rect.height * PropertiesManager.visionSafeAreaBottomRatio).toInt()
    val height = bottom - top
    val rect = Rectangle(0, top, rootElement.rect.width, height)
    val v = rect.toVisionElement()
    return v
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

    return TestDriver.visionRootElement
}

