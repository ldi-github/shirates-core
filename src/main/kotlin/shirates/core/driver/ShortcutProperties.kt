package shirates.core.driver

import io.appium.java_client.AppiumDriver
import shirates.core.configuration.PropertiesManager.statBarHeight
import shirates.core.configuration.TestProfile
import shirates.core.driver.TestMode.isAndroid
import shirates.core.testcode.UITestCallbackExtension
import shirates.core.vision.VisionDrive
import shirates.core.vision.driver.VisionDriveObject
import shirates.core.vision.driver.commandextension.rootElement

/**
 * drive
 */
val drive: Drive
    get() {
        return DriveObject
    }

/**
 * testDrive
 */
val testDrive: TestDrive
    get() {
        return TestDriveObject
    }

/**
 * visionDrive
 */
val visionDrive: VisionDrive
    get() {
        return VisionDriveObject
    }

/**
 * driver
 */
val driver: TestDriver
    get() {
        return TestDriver
    }

/**
 * appiumDriver
 */
val appiumDriver: AppiumDriver
    get() {
        return TestDriver.appiumDriver
    }

/**
 * testContext
 */
val testContext: TestContext
    get() {
        return TestDriver.testContext
    }

/**
 * testProfile
 */
val testProfile: TestProfile
    get() {
        return UITestCallbackExtension.uiTestBase!!.testProfile
    }

/**
 * packageName
 */
val packageName: String
    get() {
        if (testContext.useCache) {
            return testDrive.rootElement.packageName
        } else {
            return visionDrive.rootElement.packageName
        }
    }

/**
 * viewBounds
 */
val viewBounds: Bounds
    get() {
        val b = if (testContext.useCache) {
            if (isAndroid) {
                TestElementCache.hierarchyBounds
            } else {
                testDrive.rootElement.bounds
            }
        } else {
            visionDrive.rootElement.bounds
        }
        return Bounds(left = b.left, top = statBarHeight, width = b.width, height = b.height - statBarHeight)
    }

