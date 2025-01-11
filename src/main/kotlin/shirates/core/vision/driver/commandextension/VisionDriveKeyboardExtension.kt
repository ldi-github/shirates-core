package shirates.core.vision.driver.commandextension

import com.google.common.collect.ImmutableMap
import io.appium.java_client.android.nativekey.AndroidKey
import io.appium.java_client.android.nativekey.KeyEvent
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.driver.TestDriver.androidDriver
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.TestMode.isiOS
import shirates.core.driver.commandextension.hideKeyboard
import shirates.core.driver.commandextension.pressKeys
import shirates.core.driver.commandextension.toVisionElement
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
                TestDriver.androidDriver.pressKey(KeyEvent(AndroidKey.BACK))
            }
            TestDriver.invalidateCache()
        } else {
            tap(x = 50, y = 10)
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
            TestDriver.androidDriver.pressKey(KeyEvent(AndroidKey.HOME))
        } else {
            TestDriver.appiumDriver.executeScript("mobile: pressButton", ImmutableMap.of("name", "home"))
        }
        invalidateScreen()
        wait(waitSeconds = waitSeconds)
    }

    return lastElement
}

///**
// * pressEnter
// */
//fun VisionDrive.pressEnter(
//    waitSeconds: Double = testContext.shortWaitSeconds
//): VisionElement {
//
//    val command = "pressEnter"
//    val message = message(id = command)
//
//    val context = TestDriverCommandContext(null)
//    context.execOperateCommand(command = command, message = message) {
//        if (isAndroid) {
//            TestDriver.androidDriver.pressKey(KeyEvent(AndroidKey.ENTER))
//        } else {
//            tapSoftwareKey("#Return||#Go||#Search||#Done")  // Keys.ENTER never works. So tap software key.
//        }
//        invalidateScreenshot()
//        wait(waitSeconds = waitSeconds)
//    }
//    return lastElement
//}

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
            TestDriver.androidDriver.pressKey(KeyEvent(AndroidKey.TAB))
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

        TestDriver.androidDriver.pressKey(KeyEvent(key))
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
        val v = waitForElementFocused()
        if (v.isEmpty) {
            throw TestDriverException("Focused element not found.")
        }
        val we = v.testElement?.webElement ?: throw TestDriverException("Focused element not found.")
        we.sendKeys(keysToSend)

        invalidateScreen()
        lastElement = getFocusedElement()
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
    throwsException: Boolean = false
): VisionElement {

    if (TestMode.isNoLoadRun) {
        return lastElement
    }

    screenshot()

    if (isAndroid) {
        try {
            val xpath = "//*[@focused='true']"
            val sel = Selector(xpath)
            val focused = TestDriver.appiumDriver.findElement(By.xpath(xpath))
            val e = focused.toTestElement(sel)
            val v = e.toVisionElement()
            return v
        } catch (t: Throwable) {
            if (throwsException) {
                throw TestDriverException(message(id = "focusedElementNotFound"))
            } else {
                return VisionElement.emptyElement
            }
        }
    } else {
        val e = try {
            (TestDriver.appiumDriver.switchTo().activeElement() as WebElement).toTestElement()
        } catch (t: Throwable) {
            if (throwsException) {
                throw TestDriverException(message(id = "focusedElementNotFound"))
            } else {
                return VisionElement.emptyElement
            }
        }
        val v = e.toVisionElement()
        return v
    }
}
