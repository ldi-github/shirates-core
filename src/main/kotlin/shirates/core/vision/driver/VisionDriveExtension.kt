package shirates.core.vision.driver

import shirates.core.driver.TestDriver
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
