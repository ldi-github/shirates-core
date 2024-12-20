package shirates.core.vision.driver.commandextension

import shirates.core.configuration.repository.ScreenRepository
import shirates.core.driver.*
import shirates.core.driver.TestDriver.currentScreen
import shirates.core.driver.TestDriver.refreshCache
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestNGException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.utility.sync.SyncUtility
import shirates.core.utility.toPath
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.configration.repository.VisionMLModelRepository
import shirates.core.vision.driver.*

internal fun VisionDrive.checkImageLabelContains(
    containedText: String,
    message: String,
    mlmodelFile: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
): VisionElement {

    var v = getThisOrIt()
    var contains = false

    screenshot()

    doUntilTrue(
        waitSeconds = waitSeconds,
        onBeforeRetry = {
            screenshot()
            v = v.createFromScreenshot()
        }
    ) {
        val label = v.classify(mlmodelFile = mlmodelFile)

        printInfo("label: $label")
        contains = label.contains(containedText)
        contains
    }

    contains.thisIsTrue(message = message)

    lastElement = v
    return v
}

internal fun VisionDrive.checkSwitchState(
    containedText: String,
    message: String,
    mlmodelFile: String? = null,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
): VisionElement {

    val rep = VisionMLModelRepository.getRepository(classifierName = "SwitchStateClassifier")
    rep.classifierName

    return checkImageLabelContains(
        containedText = containedText,
        message = message,
        mlmodelFile = mlmodelFile ?: rep.imageClassifierDirectory.toPath().resolve("${rep.classifierName}.mlmodel")
            .toString(),
        waitSeconds = waitSeconds
    )
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

    val command = "packageIs"
    val assertMessage = message(id = command, expected = expected)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(
        command = command,
        message = assertMessage,
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
    withTextMatching: Boolean = false,
    onIrregular: (() -> Unit)? = { TestDriver.fireIrregularHandler() },
    func: (() -> Unit)? = null
): VisionElement {

    val command = "screenIs"
    val assertMessage = message(id = command, subject = screenName)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = screenName) {

        var textMatchingMode = withTextMatching
        var match = isScreen(screenName = screenName)
        if (match.not()) {
            doUntilTrue(
                waitSeconds = waitSeconds,
                onBeforeRetry = {
                    textMatchingMode = true
                    onIrregular?.invoke()
                },
            ) {
                screenshot(force = true, withTextMatching = textMatchingMode)
                match = isScreen(screenName = screenName)
                match
            }
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

/**
 * screenIsOf
 */
fun VisionDrive.screenIsOf(
    vararg screenNames: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    onIrregular: (() -> Unit)? = { TestDriver.fireIrregularHandler() },
    func: (() -> Unit)? = null
): VisionElement {

    val command = "screenIsOf"
    val assertMessage = message(id = command, subject = screenNames.joinToString(","))

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = screenNames.joinToString(",")) {
        screenIsOfCore(
            screenNames = screenNames,
            waitSeconds = waitSeconds,
            assertMessage = assertMessage,
            onIrregular = onIrregular
        )
    }
    if (func != null) {
        func()
    }

    return lastElement
}

/**
 * screenIsOf
 */
fun VisionDrive.screenIsOf(
    vararg screenNames: String,
    waitSeconds: Int,
    onIrregular: (() -> Unit)? = { TestDriver.fireIrregularHandler() },
    func: (() -> Unit)? = null
): VisionElement {

    return screenIsOf(
        screenNames = screenNames,
        waitSeconds = waitSeconds.toDouble(),
        onIrregular = onIrregular,
        func = func
    )
}

internal fun VisionDrive.screenIsOfCore(
    vararg screenNames: String,
    assertMessage: String,
    waitSeconds: Double,
    onIrregular: (() -> Unit)?
) {
    var isScreenResult = false
    var matchedScreenName = ""
    val checkScreen = {
        for (name in screenNames) {
            isScreenResult = isScreen(name)
            if (isScreenResult) {
                matchedScreenName = name
                break
            }
        }
        isScreenResult
    }

    checkScreen()

    if (isScreenResult.not()) {
        SyncUtility.doUntilTrue(waitSeconds = waitSeconds) {
            checkScreen()
            if (isScreenResult.not()) {
                onIrregular?.invoke()
            }
            isScreenResult
        }
    }

    if (isScreenResult) {
        TestLog.ok(message = assertMessage, arg1 = matchedScreenName)
    } else {
        lastElement.lastResult = LogType.NG

        val ex = TestNGException("${assertMessage} (actual=${currentScreen})", lastElement.lastError)
        throw ex
    }
}

/**
 * verify
 */
fun VisionDrive.verify(
    message: String,
    func: (() -> Unit)
): VisionElement {
    val testElement = TestDriver.it

    val command = "verify"

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = message) {

        val startCount = TestLog.allLines.count()
        val original = CodeExecutionContext.isInOperationCommand
        try {
            CodeExecutionContext.isInOperationCommand = true
            func()
            CodeExecutionContext.isInOperationCommand = original

            val endCount = TestLog.allLines.count()
            val takeCount = endCount - startCount
            val lines = TestLog.allLines.takeLast(takeCount)
            if (lines.any() { it.logType == LogType.CHECK || it.logType.isOKType }) {
                TestLog.ok(message = message)
            } else {
                TestLog.info("verify block should include one or mode assertion.")
                TestLog.manual(message = message)
            }
        } catch (t: NotImplementedError) {
            throw t
        } catch (t: Throwable) {
            val ex = TestNGException(message = message, cause = t)
            throw ex
        } finally {
            CodeExecutionContext.isInOperationCommand = original
        }
    }

    return lastElement
}
