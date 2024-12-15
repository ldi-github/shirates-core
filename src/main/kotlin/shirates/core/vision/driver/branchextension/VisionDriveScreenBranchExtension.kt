package shirates.core.vision.driver.branchextension

import shirates.core.driver.TestDriver
import shirates.core.driver.TestMode
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.printInfo
import shirates.core.vision.VisionDrive
import shirates.core.vision.configration.repository.VisionMLModelRepository
import shirates.core.vision.driver.classifyScreen
import java.awt.image.BufferedImage

/**
 * lastScreenshotImage
 */
val VisionDrive.lastScreenshotImage: BufferedImage?
    get() {
        if (TestMode.isNoLoadRun) {
            return null
        }
        return CodeExecutionContext.lastScreenshotImage
    }

/**
 * lastScreenshotFile
 */
val VisionDrive.lastScreenshotFile: String?
    get() {
        if (TestMode.isNoLoadRun) {
            return null
        }
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

    if (lastScreenshotFile == null) {
        TestDriver.screenshot(force = true, log = false)
    }
    val file = VisionMLModelRepository.screenClassifierRepository.getFile(label = screenName)
        ?: return false

    val label = classifyScreen(imageFile = lastScreenshotFile!!)
    printInfo("classified as $label. ($file)")

    val r = (label == screenName)
    if (r) {
        TestDriver.currentScreen = screenName
    }

    return r
}