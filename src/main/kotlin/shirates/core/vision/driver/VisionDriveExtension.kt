package shirates.core.vision.driver

import shirates.core.driver.TestDriver
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement

/**
 * it
 */
val VisionDrive.it: VisionElement
    get() {
        return TestDriver.lastVisionElement
    }

/**
 * lastElement
 */
var VisionDrive.lastElement: VisionElement
    get() {
        return TestDriver.lastVisionElement
    }
    set(value) {
        TestDriver.lastVisionElement = value
    }

