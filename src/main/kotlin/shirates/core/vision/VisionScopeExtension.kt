package shirates.core.vision

import shirates.core.driver.TestDriver.lastVisionElement
import shirates.core.driver.visionDrive

/**
 * visionScope
 */
fun visionScope(
    func: (VisionElement) -> Unit
) {
    visionDrive.apply {
        func(lastVisionElement)
    }
}
