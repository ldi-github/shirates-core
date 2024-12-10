package shirates.core.vision.driver.branchextension

import shirates.core.driver.TestDriver
import shirates.core.driver.TestMode
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.printInfo
import shirates.core.vision.VisionDrive
import shirates.core.vision.driver.classifyScreen
import shirates.core.vision.driver.screenshot
import java.awt.image.BufferedImage

/**
 * lastScreenshotImage
 */
val VisionDrive.lastScreenshotImage: BufferedImage?
    get() {
        return CodeExecutionContext.lastScreenshotImage
    }

/**
 * lastScreenshotFile
 */
val VisionDrive.lastScreenshotFile: String
    get() {
        return CodeExecutionContext.lastScreenshotFile
    }


/**
 * isScreen
 */
fun VisionDrive.isScreen(
    screenName: String
): Boolean {

    if (TestMode.isNoLoadRun) {
        TestDriver.currentScreen = screenName
        return true
    }

    if (lastScreenshotImage == null) {
        screenshot(force = true)
    }

    val label = classifyScreen(imageFile = lastScreenshotFile)
    printInfo("classified as $label")

    val r = label == screenName
    if (r) {
        TestDriver.currentScreen = screenName
    }

    return r
}