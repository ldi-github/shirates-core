package shirates.core.vision

import shirates.core.driver.Drive
import shirates.core.driver.TestDriver

interface VisionDrive : Drive {

    /**
     * lastElement
     */
    var lastElement: VisionElement
        get() {
            return TestDriver.lastVisionElement
        }
        set(value) {
            TestDriver.lastVisionElement = value
        }

    /**
     * toVisionElement
     */
    val toVisionElement: VisionElement
        get() {
            if (this is VisionElement) {
                return this
            }
            return VisionElement.emptyElement
        }
}
