package shirates.core.vision.driver.commandextension

import com.google.common.collect.ImmutableMap
import ifCanDetect
import io.appium.java_client.android.nativekey.AndroidKey
import io.appium.java_client.android.nativekey.KeyEvent
import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.driver.TestDriver.androidDriver
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.TestMode.isiOS
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement
import shirates.core.vision.driver.wait

/**
 * isKeyboardShown
 */
val VisionDrive.isKeyboardShown: Boolean
    get() {
        if (TestMode.isNoLoadRun) {
            return true
        }

        if (isAndroid) {
            return androidDriver.isKeyboardShown
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

    val command = "hideKeyboard"
    val message = message(id = command)
    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        if (isiOS) {
            // hideKeyboard() fails in iOS. https://github.com/appium/appium/issues/15073
            val selector = Selector(".XCUIElementTypeKeyboard")
            val e = TestDriver.selectDirectByIosClassChain(selector, frame = null)
            if (e.isFound) {
                swipePointToPoint(
                    startX = 10,
                    startY = viewBounds.centerY,
                    endX = 10,
                    endY = viewBounds.centerY - 10,
                    durationSeconds = 1.0
                )
            }
        } else {
            TestDriver.appiumDriver.hideKeyboard()
        }
        invalidateScreen()
        wait(waitSeconds = waitSeconds)
    }
    return lastElement
}

/**
 * pressBack
 */
fun VisionDrive.pressBack(
    expression: String? = null,
    waitSeconds: Double = testContext.shortWaitSeconds
): VisionElement {

    val command = "pressBack"
    val message = message(id = command)
    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        if (isAndroid) {
            if (platformMajorVersion <= 9) {
                androidDriver.navigate().back()
            } else {
                androidDriver.pressKey(KeyEvent(AndroidKey.BACK))
            }
            TestDriver.invalidateCache()
        } else {
            if (expression != null) {
                if (classic.canSelect(expression)) {
                    classic.it.tap()
                } else {
                    onTopRegion {
                        ifCanDetect(expression) {
                            it.tap()
                        }
                    }
                }
            } else {
                fun pressBackIos() {

                    val backButton = classic.select("#BackButton", throwsException = false, waitSeconds = 0.0)
                    if (backButton.isFound) {
                        backButton.tap()
                        return
                    }
                    val anyButton = classic.select(".XCUIElementTypeButton", throwsException = false, waitSeconds = 0.0)
                    if (anyButton.isFound) {
                        val v = anyButton.toVisionElement()
                        val topRegion = getTopRegion()
                        if (v.rect.isIncludedIn(topRegion.rect)) {
                            v.tap()
                            return
                        }
                    }

                    tap(x = 50, y = 10)
                }
                pressBackIos()
            }
        }
        invalidateScreen()
        wait(waitSeconds = waitSeconds)
    }
    return lastElement
}

/**
 * pressHome
 */
fun VisionDrive.pressHome(
    waitSeconds: Double = testContext.shortWaitSeconds
): VisionElement {

    val command = "pressHome"
    val message = message(id = command)
    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, fireEvent = false) {
        if (isAndroid) {
            androidDriver.pressKey(KeyEvent(AndroidKey.HOME))
        } else {
            TestDriver.appiumDriver.executeScript("mobile: pressButton", ImmutableMap.of("name", "home"))
        }
        invalidateScreen()
        wait(waitSeconds = waitSeconds)
    }
    return lastElement
}

/**
 * pressEnter
 */
fun VisionDrive.pressEnter(
    waitSeconds: Double = testContext.shortWaitSeconds
): VisionElement {

    val command = "pressEnter"
    val message = message(id = command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        if (isAndroid) {
            androidDriver.pressKey(KeyEvent(AndroidKey.ENTER))
        } else {
            sendKeys("\n")
        }
        invalidateScreen()
        wait(waitSeconds = waitSeconds)
    }
    return lastElement
}

///**
// * pressSearch
// */
//fun VisionDrive.pressSearch(
//    waitSeconds: Double = testContext.shortWaitSeconds
//): VisionElement {
//
//    val command = "pressSearch"
//    val message = message(id = command)
//
//    val context = TestDriverCommandContext(null)
//    context.execOperateCommand(command = command, message = message) {
//        if (isAndroid) {
//            TestDriver.appiumDriver.executeScript(
//                "mobile: performEditorAction",
//                mutableMapOf("action" to "search")
//            )
//
//        } else {
//            TestDriver.lastElement.sendKeys("\n")
//        }
//        invalidateScreenshot()
//        wait(waitSeconds = waitSeconds)
//    }
//    return lastElement
//}

/**
 * pressTab
 */
fun VisionDrive.pressTab(
    waitSeconds: Double = testContext.shortWaitSeconds
): VisionElement {

    val command = "pressTab"
    val message = message(id = command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        if (isAndroid) {
            androidDriver.pressKey(KeyEvent(AndroidKey.TAB))
        } else {
            tap("#Next")
        }
        invalidateScreen()
        wait(waitSeconds = waitSeconds)
    }
    return lastElement
}

/**
 * pressAndroid
 */
fun VisionDrive.pressAndroid(
    key: AndroidKey,
    waitSeconds: Double = testContext.shortWaitSeconds
): VisionElement {

    val command = "pressAndroid"
    val message = message(id = command, key = "$key")

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(
        command = command,
        message = message,
        arg1 = key.toString()
    ) {
        if (isAndroid.not())
            throw UnsupportedOperationException("pressAndroid function is for Android.")

        androidDriver.pressKey(KeyEvent(key))
        invalidateScreen()
        wait(waitSeconds = waitSeconds)
    }
    return lastElement
}

/**
 * pressKeys
 */
fun VisionDrive.pressKeys(
    keys: String,
    waitSeconds: Double = testContext.shortWaitSeconds
): VisionElement {

    val command = "pressKeys"

    val message = message(id = command, key = keys)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(
        command = command,
        message = message,
        arg1 = keys
    ) {
        if (isAndroid) {
            TestDriveObjectAndroid.pressKeys(keys = keys)
        } else {
            sendKeys(keysToSend = keys)
        }
        invalidateScreen()
        wait(waitSeconds = waitSeconds)
    }
    return lastElement
}

/**
 * sendKeys
 */
fun VisionDrive.sendKeys(
    keysToSend: CharSequence,
): VisionElement {

    val command = "sendKeys"
    val message = message(id = command, key = "$keysToSend")

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val testElement = TestDriver.getFocusedElement()
        if (testElement.isEmpty) {
            throw TestDriverException("Focused element not found.")
        }
        val we = testElement.webElement ?: testElement.getWebElement()
        we.sendKeys(keysToSend)

        invalidateScreen()
        lastElement = getFocusedElement()
        invalidateScreen()
    }

    return lastElement
}

/**
 * typeChars
 */
fun VisionElement.typeChars(
    charsToSend: String,
): VisionElement {

    val command = "typeChars"
    val message = message(id = command, key = charsToSend)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {

        for (c in charsToSend) {
            sendKeys(keysToSend = c.toString() as CharSequence)
        }
    }

    return this
}

/**
 * getFocusedElement
 */
fun VisionDrive.getFocusedElement(
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    throwsException: Boolean = false
): VisionElement {

    if (TestMode.isNoLoadRun) {
        return lastElement
    }

    val e = TestDriver.getFocusedElement(waitSeconds = waitSeconds, throwsException = throwsException)
    if (e.isEmpty) {
        if (throwsException) {
            throw TestDriverException(message(id = "focusedElementNotFound"))
        } else {
            return VisionElement.emptyElement
        }
    }
    var v = e.toVisionElement()
    screenshot(force = true)
    v = v.newVisionElement()
    v.testElement = e
    return v
}
