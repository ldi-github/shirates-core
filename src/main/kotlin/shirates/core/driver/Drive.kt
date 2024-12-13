package shirates.core.driver

import io.appium.java_client.AppiumDriver

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
}