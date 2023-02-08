package shirates.core.driver.commandextension

import com.google.common.collect.ImmutableMap
import io.appium.java_client.android.nativekey.AndroidKey
import io.appium.java_client.android.nativekey.KeyEvent
import shirates.core.driver.*
import shirates.core.driver.TestDriver.lastElement
import shirates.core.driver.TestMode.isAndroid
import shirates.core.logging.Message.message
import shirates.core.utility.android.AndroidKeyboardUtility

/**
 * isKeyboardShown
 */
val TestDrive?.isKeyboardShown: Boolean
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
fun TestDrive?.hideKeyboard(
    waitSeconds: Double = testContext.shortWaitSeconds
): TestElement {

    val testElement = getTestElement()

    val command = "hideKeyboard"
    val message = message(id = command)
    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        try {
            // hideKeyboard() may fail. https://github.com/appium/appium/issues/15073
            TestDriver.appiumDriver.hideKeyboard()
        } catch (t: Throwable) {
            // workaround
            rootElement.getWebElement().click()
        }
        invalidateCache()
        wait(waitSeconds = waitSeconds)
        TestDriver.autoScreenshot(onChangedOnly = false)
    }

    lastElement = this.focusedElement
    return lastElement
}

/**
 * pressBack
 */
fun TestDrive?.pressBack(
    waitSeconds: Double = testContext.shortWaitSeconds
): TestElement {

    val testElement = getTestElement()

    val command = "pressBack"
    val message = message(id = command)
    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        if (isAndroid) {
            TestDriver.androidDriver.pressKey(KeyEvent(AndroidKey.BACK))
            TestDriver.invalidateCache()
        } else {
//            tap("${driver.appIconName}")
            tap(x = 50, y = 10)
        }
        invalidateCache()
        wait(waitSeconds = waitSeconds)
    }

    return TestElement.emptyElement
}

/**
 * pressHome
 */
fun TestDrive?.pressHome(
    waitSeconds: Double = testContext.shortWaitSeconds
): TestElement {

    val testElement = getTestElement()

    val command = "pressHome"
    val message = message(id = command)
    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message, fireEvent = false) {
        if (isAndroid) {
            TestDriver.androidDriver.pressKey(KeyEvent(AndroidKey.HOME))
        } else {
            TestDriver.appiumDriver.executeScript("mobile: pressButton", ImmutableMap.of("name", "home"))
        }
        invalidateCache()
        wait(waitSeconds = waitSeconds)
    }

    return TestElement.emptyElement
}

/**
 * pressEnter
 */
fun TestDrive?.pressEnter(
    waitSeconds: Double = testContext.shortWaitSeconds
): TestElement {

    val testElement = getTestElement()

    val command = "pressEnter"
    val message = message(id = command)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        if (isAndroid) {
            TestDriver.androidDriver.pressKey(KeyEvent(AndroidKey.ENTER))
        } else {
            tap("#Return||#Go||#Search||#Done")  // Keys.ENTER never works. So tap software key.
        }
        invalidateCache()
        wait(waitSeconds = waitSeconds)
    }

    lastElement = this.focusedElement
    return lastElement
}

/**
 * pressSearch
 */
fun TestDrive?.pressSearch(
    waitSeconds: Double = testContext.shortWaitSeconds
): TestElement {

    val testElement = getTestElement()

    val command = "pressSearch"
    val message = message(id = command)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        if (isAndroid) {
            TestDriver.appiumDriver.executeScript(
                "mobile: performEditorAction",
                mutableMapOf("action" to "search")
            )

        } else {
            TestDriver.appiumDriver.keyboard.sendKeys("\n")
        }
        invalidateCache()
        wait(waitSeconds = waitSeconds)
    }

    lastElement = this.focusedElement
    return lastElement
}

/**
 * pressTab
 */
fun TestDrive?.pressTab(
    waitSeconds: Double = testContext.shortWaitSeconds
): TestElement {

    val testElement = getTestElement()

    val command = "pressTab"
    val message = message(id = command)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        if (isAndroid) {
            TestDriver.androidDriver.pressKey(KeyEvent(AndroidKey.TAB))
        } else {
            tap("#Next")
        }
        invalidateCache()
        wait(waitSeconds = waitSeconds)
    }

    lastElement = this.focusedElement
    return lastElement
}

/**
 * pressAndroid
 */
fun TestDrive?.pressAndroid(
    key: AndroidKey,
    waitSeconds: Double = testContext.shortWaitSeconds
): TestElement {

    val testElement = getTestElement()

    val command = "pressAndroid"
    val message = message(id = command, key = "$key")

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(
        command = command,
        message = message,
        subject = testElement.subject,
        arg1 = key.toString()
    ) {
        if (isAndroid.not())
            throw UnsupportedOperationException("pressAndroid function is for Android.")

        TestDriver.androidDriver.pressKey(KeyEvent(key))
        invalidateCache()
        wait(waitSeconds = waitSeconds)
    }

    lastElement = this.focusedElement
    return lastElement
}

/**
 * pressKeys
 */
fun TestDrive?.pressKeys(
    keys: String,
    waitSeconds: Double = testContext.shortWaitSeconds
): TestElement {

    val testElement = getTestElement()

    val command = "pressKeys"

    val message = message(id = command, key = keys)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(
        command = command,
        message = message,
        subject = testElement.subject,
        arg1 = keys
    ) {
        if (isAndroid) {
            AndroidKeyboardUtility.pressKeys(keys = keys)
        } else {
            sendKeys(keysToSend = keys)
        }
        invalidateCache()
        wait(waitSeconds = waitSeconds)
    }

    lastElement = this.focusedElement
    return lastElement
}
