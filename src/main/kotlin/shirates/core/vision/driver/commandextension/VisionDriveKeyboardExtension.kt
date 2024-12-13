package shirates.core.vision.driver.commandextension

import io.appium.java_client.android.nativekey.AndroidKey
import shirates.core.driver.TestDriver
import shirates.core.driver.TestMode
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.commandextension.*
import shirates.core.driver.testContext
import shirates.core.driver.testDrive
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement

/**
 * isKeyboardShown
 */
val VisionDrive.isKeyboardShown: Boolean
    get() {
        if (TestMode.isNoLoadRun) {
            return true
        }

        if (isAndroid) {
            return TestDriver.androidDriver.isKeyboardShown
        } else {
            return TestDriver.iosDriver.isKeyboardShown
        }
    }

/**
 * hideKeyboard
 */
fun VisionDrive.hideKeyboard(
    waitSeconds: Double = testContext.shortWaitSeconds
): VisionElement {

    testDrive.hideKeyboard(
        waitSeconds = waitSeconds
    )
    return lastElement
}

/**
 * pressBack
 */
fun VisionDrive.pressBack(
    waitSeconds: Double = testContext.shortWaitSeconds
): VisionElement {

    testDrive.pressBack(
        waitSeconds = waitSeconds
    )
    return lastElement
}

/**
 * pressHome
 */
fun VisionDrive.pressHome(
    waitSeconds: Double = testContext.shortWaitSeconds
): VisionElement {

    testDrive.pressHome(
        waitSeconds = waitSeconds
    )
    return lastElement
}

/**
 * pressEnter
 */
fun VisionDrive.pressEnter(
    waitSeconds: Double = testContext.shortWaitSeconds
): VisionElement {

    testDrive.pressEnter()
    return lastElement
}

/**
 * pressSearch
 */
fun VisionDrive.pressSearch(
    waitSeconds: Double = testContext.shortWaitSeconds
): VisionElement {

    testDrive.pressSearch(
        waitSeconds = waitSeconds
    )
    return lastElement
}

/**
 * pressTab
 */
fun VisionDrive.pressTab(
    waitSeconds: Double = testContext.shortWaitSeconds
): VisionElement {

    testDrive.pressTab(
        waitSeconds = waitSeconds
    )
    return lastElement
}

/**
 * pressAndroid
 */
fun VisionDrive.pressAndroid(
    key: AndroidKey,
    waitSeconds: Double = testContext.shortWaitSeconds
): VisionElement {

    testDrive.pressAndroid(key = key, waitSeconds = waitSeconds)
    return lastElement
}

/**
 * pressKeys
 */
fun VisionDrive.pressKeys(
    keys: String,
    waitSeconds: Double = testContext.shortWaitSeconds
): VisionElement {

    testDrive.pressKeys(keys = keys, waitSeconds = waitSeconds)
    return lastElement
}