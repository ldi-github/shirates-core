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
 * classic
 */
val classic: TestDrive
    get() {
        return TestDriveObject
    }

/**
 * vision
 */
val vision: VisionDrive
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
            return classic.rootElement.packageName
        } else {
            return vision.rootElement.packageName
        }
    }

/**
 * rootBounds
 */
val rootBounds: Bounds
    get() {
        if (_rootBounds != null) {
            return _rootBounds!!
        }
        if (TestMode.isClassicTest) {
            if (isAndroid) {
                _rootBounds = TestElementCache.hierarchyBounds
            } else {
                _rootBounds = classic.rootElement.bounds
            }
        } else {
            _rootBounds = vision.rootElement.bounds
        }
        return _rootBounds!!
    }
private var _rootBounds: Bounds? = null

/**
 * viewBounds
 */
val viewBounds: Bounds
    get() {
        if (_viewBounds != null) {
            return _viewBounds!!
        }
        val b = rootBounds
        _viewBounds = Bounds(left = b.left, top = statBarHeight, width = b.width, height = b.height - statBarHeight)
        return _viewBounds!!
    }
private var _viewBounds: Bounds? = null
