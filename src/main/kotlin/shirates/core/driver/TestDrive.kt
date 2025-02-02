package shirates.core.driver

import shirates.core.vision.VisionDrive
import shirates.core.vision.driver.VisionDriveObject

interface TestDrive : Drive {

    /**
     * it
     */
    val it: TestElement
        get() {
            return TestDriver.lastElement
        }

    /**
     * lastElement
     */
    var lastElement: TestElement
        get() {
            return TestDriver.lastElement
        }
        set(value) {
            TestDriver.lastElement = value
        }

    /**
     * toTestElement
     */
    val toTestElement: TestElement
        get() {
            if (this is TestElement) {
                return this
            }
            return TestElement.emptyElement
        }

    /**
     * vision
     */
    val vision: VisionDrive
        get() {
            return VisionDriveObject
        }

}