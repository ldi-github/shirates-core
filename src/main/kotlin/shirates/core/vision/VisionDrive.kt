package shirates.core.vision

import shirates.core.driver.Drive
import shirates.core.driver.TestDrive
import shirates.core.driver.TestDriveObject
import shirates.core.driver.TestDriver

interface VisionDrive : Drive {

    /**
     * it
     */
    val it: VisionElement
        get() {
            return TestDriver.lastVisionElement
        }

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

    /**
     * testDrive
     */
    val testDrive: TestDrive
        get() {
            return TestDriveObject
        }
}
