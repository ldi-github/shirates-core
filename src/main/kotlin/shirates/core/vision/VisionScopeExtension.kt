package shirates.core.vision

import shirates.core.driver.TestDriver.lastVisionElement

/**
 * visionScope
 */
fun visionScope(
    func: (VisionElement) -> Unit
) {

    func(lastVisionElement)
}
