package shirates.core.vision.driver.commandextension

import shirates.core.driver.*
import shirates.core.driver.TestDriver.currentScreen
import shirates.core.driver.commandextension.appIs
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.driver.commandextension.toVisionElement
import shirates.core.exception.TestConfigException
import shirates.core.exception.TestNGException
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.sync.SyncUtility
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.classicScope
import shirates.core.vision.configration.repository.VisionTextIndexRepository
import shirates.core.vision.driver.*

internal fun VisionDrive.checkImageLabelContains(
    containedText: String,
    message: String,
    classifierName: String,
    waitSeconds: Double,
    fullLabel: Boolean,
    threshold: Double
): VisionElement {

    var v = getThisOrIt()
    var result = false

    screenshot()

    doUntilTrue(
        waitSeconds = waitSeconds,
        intervalSeconds = testContext.waitSecondsForAnimationComplete,
        throwOnFinally = false,
        onBeforeRetry = {
            screenshot()
            v = v.newVisionElement()
        }
    ) {
        if (fullLabel) {
            val fullLabel = v.classifyFull(classifierName = classifierName, threshold = threshold)
            printInfo("fullLabel: $fullLabel")
            result = fullLabel == containedText
        } else {
            val label = v.classify(classifierName = classifierName, threshold = threshold)
            printInfo("label: $label")
            result = label.contains(containedText)
        }

        result
    }

    v.saveImage("${TestLog.currentLineNo}_${v}")

    result.thisIsTrue(message = message)

    lastElement = v
    return v
}

internal fun VisionDrive.checkIsCore(
    containedText: String,
    message: String,
    classifierName: String,
    waitSeconds: Double,
    threshold: Double
): VisionElement {

    return checkImageLabelContains(
        containedText = containedText,
        message = message,
        classifierName = classifierName,
        waitSeconds = waitSeconds,
        fullLabel = false,
        threshold = threshold
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

    classicScope {
        classic.appIs(
            appNameOrAppId = appNameOrAppId,
            waitSeconds = waitSeconds,
            useCache = useCache,
        )
    }

    return lastElement
}

/**
 * screenIs
 */
fun VisionDrive.screenIs(
    screenName: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    onIrregular: (() -> Unit)? = { TestDriver.fireIrregularHandler() },
    message: String? = null,
    verifyFunc: (() -> Unit)? = null
): VisionElement {

    if (testContext.useCache) {
        val e = classic.screenIs(
            screenName = screenName,
            waitSeconds = waitSeconds,
            onIrregular = onIrregular,
            func = verifyFunc,
        )
        return e.toVisionElement()
    }

    val command = "screenIs"
    val assertMessage = message ?: message(id = command, subject = screenName)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = screenName) {

        TestDriver.currentScreen = "?"

        if (isScreenRegistered(screenName = screenName).not()) {
            throw TestConfigException("screenName is not registered in ScreenClassifier. (screenName=$screenName)")
        }

        var match = isScreen(screenName = screenName)
        if (match.not()) {
            doUntilTrue(
                waitSeconds = waitSeconds,
                throwOnFinally = false,
                onBeforeRetry = {
                    onIrregular?.invoke()
                },
            ) {
                match = isScreen(screenName = screenName, invalidateScreen = true)
                match
            }
        }

        if (match.not()) {
            match = isScreen(screenName = screenName)   // Retry for timeout
            if (match.not()) {
                TestDriver.currentScreen = "?"
                lastElement.lastResult = LogType.NG

                val textIndexList = VisionTextIndexRepository.getList(screenName = screenName)
                if (textIndexList.any()) {
                    for (textIndex in textIndexList) {
                        TestLog.warn("Expecting textIdex: $textIndex")
                    }
                    if (rootElement.visionContext.recognizeTextObservations.isEmpty()) {
                        rootElement.visionContext.recognizeText()
                    }
                    TestLog.info("recognizeTexts: ${rootElement.visionContext.recognizeTextObservations.map { "\"${it.text}\"" }}")
                }

                val msg = "$assertMessage(${TestDriver.lastRecognizeScreenResult})"
                val ex = TestNGException(msg, lastElement.lastError)
                throw ex
            }
        }

        TestDriver.currentScreen = screenName
        TestLog.ok(message = assertMessage, arg1 = screenName)
    }
    if (verifyFunc != null) {
        silent {
            verifyFunc()
        }
    }

    return lastElement
}

/**
 * screenIs
 */
fun VisionDrive.screenIs(
    screenName: String,
    waitSeconds: Int,
    message: String? = null,
    func: (() -> Unit)? = null
): VisionElement {

    return screenIs(
        screenName = screenName,
        waitSeconds = waitSeconds.toDouble(),
        message = message,
        verifyFunc = func
    )
}

/**
 * screenIsOf
 */
fun VisionDrive.screenIsOf(
    vararg screenNames: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    onIrregular: (() -> Unit)? = { TestDriver.fireIrregularHandler() },
    message: String? = null,
    func: (() -> Unit)? = null
): VisionElement {

    val command = "screenIsOf"
    val assertMessage = message ?: message(id = command, subject = screenNames.joinToString(","))

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
    message: String? = null,
    func: (() -> Unit)? = null
): VisionElement {

    return screenIsOf(
        screenNames = screenNames,
        waitSeconds = waitSeconds.toDouble(),
        onIrregular = onIrregular,
        message = message,
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
        SyncUtility.doUntilTrue(
            waitSeconds = waitSeconds,
            throwOnError = false,
            throwOnOverMaxLoopCount = false
        ) {
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
        checkScreen()   // Retry for timeout
        if (isScreenResult.not()) {
            lastElement.lastResult = LogType.NG

            val ex = TestNGException("${assertMessage} (actual=${currentScreen})", lastElement.lastError)
            throw ex
        }
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

