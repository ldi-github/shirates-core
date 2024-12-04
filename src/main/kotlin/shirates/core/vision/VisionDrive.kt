package shirates.core.vision

import io.appium.java_client.AppiumDriver
import shirates.core.driver.TestDriver
import shirates.core.driver.TestElement

interface VisionDrive {

    /**
     * Shortcut for TestDriver object
     */
    val driver: TestDriver
        get() {
            return TestDriver
        }

    /**
     * Shortcut for AppiumDriver object
     */
    val appiumDriver: AppiumDriver
        get() {
            return TestDriver.appiumDriver
        }

    /**
     * Returns last accessed element
     */
    var lastElement: TestElement
        get() {
            return TestDriver.lastElement
        }
        set(value) {
            TestDriver.lastElement = value
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
