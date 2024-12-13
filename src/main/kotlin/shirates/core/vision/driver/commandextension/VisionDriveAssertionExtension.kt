package shirates.core.vision.driver.commandextension

import shirates.core.configuration.repository.ScreenRepository
import shirates.core.driver.*
import shirates.core.driver.TestDriver.refreshCache
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestNGException
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.*
import shirates.core.vision.driver.branchextension.isScreen

internal fun VisionDrive.checkImageLabelContains(
    containedText: String,
    message: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen
): VisionElement {

    var v = getThisOrIt()
    var contains = false

    doUntilTrue(
        waitSeconds = waitSeconds,
        onBeforeRetry = {
            screenshot(force = true)
            v = v.createFromScreenshot()
        }
    ) {
        val label = v.classify()

        printInfo("label: $label")
        contains = label.contains(containedText)
        contains
    }

    contains.thisIsTrue(message = message)

    lastElement = v
    return v
}

/**
 * keyboardIsShown
 */
fun VisionDrive.keyboardIsShown(): VisionElement {

    val testElement = TestDriver.it

    val command = "keyboardIsShown"
    val assertMessage = message(id = command)

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = "keyboard") {
        if (isKeyboardShown) {
            TestLog.ok(message = assertMessage)
        } else {
            lastElement.lastError = TestNGException(assertMessage)
            throw lastElement.lastError!!
        }
    }

    return lastElement
}

/**
 * keyboardIsNotShown
 */
fun VisionDrive.keyboardIsNotShown(): VisionElement {

    val testElement = TestDriver.it

    val command = "keyboardIsNotShown"
    val assertMessage = message(id = command)

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = "keyboard") {
        if (isKeyboardShown.not()) {
            TestLog.ok(message = assertMessage)
        } else {
            lastElement.lastError = TestNGException(assertMessage)
            throw lastElement.lastError!!
        }
    }

    return lastElement
}

/**
 * packageIs
 */
fun VisionDrive.packageIs(expected: String): VisionElement {

    val testElement = TestDriver.it

    val command = "packageIs"
    val assertMessage = message(id = command, expected = expected)

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(
        command = command,
        message = assertMessage,
        subject = testElement.subject,
        arg1 = expected
    ) {
        if (TestMode.isiOS) {
            throw NotImplementedError("packageIs function is for Android.")
        }

        val actual = rootElement.packageName
        if (actual == expected) {
            TestLog.ok(
                message = assertMessage,
                arg1 = expected
            )
        } else {
            lastElement.lastError = TestNGException("$assertMessage (actual=\"$actual\")")
            throw lastElement.lastError!!
        }
    }

    return lastElement
}

/**
 * appIs
 *
 * @param appNameOrAppId
 * Nickname [App1]
 * or appName App1
 * or packageOrBundleId com.example.app1
 */
fun VisionDrive.appIs(
    appNameOrAppId: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    useCache: Boolean = testContext.useCache,
): VisionElement {

    refreshCache()
    testDrive.appIs(
        appNameOrAppId = appNameOrAppId,
        waitSeconds = waitSeconds,
        useCache = useCache,
    )

    return lastElement
}

/**
 * screenIs
 */
fun VisionDrive.screenIs(
    screenName: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    onIrregular: (() -> Unit)? = { TestDriver.fireIrregularHandler() },
    func: (() -> Unit)? = null
): VisionElement {

    val command = "screenIs"
    val assertMessage = message(id = command, subject = screenName)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = screenName) {

        var match = isScreen(screenName = screenName)
        if (match.not()) {
            val wc = doUntilTrue(
                waitSeconds = waitSeconds,
                onBeforeRetry = {
                    onIrregular?.invoke()
                },
            ) {
                screenshot(force = true)
                match = isScreen(screenName = screenName)
                match
            }
            wc.throwIfError()
        }

        if (match) {
            TestDriver.currentScreen = screenName
            TestLog.ok(message = assertMessage, arg1 = screenName)
        } else {
            TestDriver.currentScreen = "?"
            lastElement.lastResult = LogType.NG

            val identity = ScreenRepository.get(screenName).identityElements.joinToString("")
            val message = "$assertMessage(currentScreen=${TestDriver.currentScreen}, expected identity=$identity)"
            val ex = TestNGException(message, lastElement.lastError)
            throw ex
        }
    }
    if (func != null) {
        func()
    }

    return lastElement
}

/**
 * screenIs
 */
fun VisionDrive.screenIs(
    screenName: String,
    waitSeconds: Int,
    func: (() -> Unit)? = null
): VisionElement {

    return screenIs(
        screenName = screenName,
        waitSeconds = waitSeconds.toDouble(),
        func = func
    )
}


