package shirates.core.vision

import shirates.core.driver.TestDriver.lastVisionElement

/**
 * it
 */
val VisionDrive.it: VisionElement
    get() {
        return lastVisionElement
    }