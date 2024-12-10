package shirates.core.driver

import io.appium.java_client.AppiumDriver
import shirates.core.vision.VisionElement

interface Drive {

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
     * Returns lastElement after syncing cache.
     */
    val it: TestElement
        get() {
            return TestDriver.it
        }

    /**
     * vision
     */
    val vision: VisionElement
        get() {
            return TestDriver.lastVisionElement
        }

}