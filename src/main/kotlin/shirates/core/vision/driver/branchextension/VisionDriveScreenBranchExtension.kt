package shirates.core.vision.driver.branchextension

import shirates.core.driver.TestMode
import shirates.core.testcode.CodeExecutionContext
import shirates.core.vision.VisionDrive
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


