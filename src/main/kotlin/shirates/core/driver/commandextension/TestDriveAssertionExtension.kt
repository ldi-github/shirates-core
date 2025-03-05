package shirates.core.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.configuration.repository.ScreenRepository
import shirates.core.driver.*
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestNGException
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.load.CpuLoadService
import shirates.core.utility.misc.AppNameUtility
import shirates.core.utility.sync.SyncContext
import shirates.core.utility.sync.SyncUtility

/**
 * keyboardIsShown
 */
fun TestDrive.keyboardIsShown(): TestElement {

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
fun TestDrive.keyboardIsNotShown(): TestElement {

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
fun TestDrive.packageIs(
    expected: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
): TestElement {

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

        var actual = rootElement.packageName
        if (actual != expected) {
            doUntilTrue(
                waitSeconds = waitSeconds,
                throwOnError = false
            ) {
                actual = rootElement.packageName
                actual == expected
            }
        }

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
fun TestDrive.appIs(
    appNameOrAppId: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    useCache: Boolean = testContext.useCache,
    onIrregular: (() -> Unit)? = { TestDriver.fireIrregularHandler() }
): TestElement {

    val testElement = TestDriver.it

    val command = "appIs"
    val subject = Selector(appNameOrAppId).toString()
    val assertMessage = message(id = command, subject = subject)

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {

        var result = false
        val actionFunc = {
            result = isApp(appNameOrAppId)
            result
        }

        actionFunc()

        if (result.not()) {
            val sc = doUntilActionResultTrue(
                waitSeconds = waitSeconds,
                useCache = useCache,
                actionFunc = actionFunc,
                onIrregular = onIrregular
            )
            if (sc.isTimeout) {
                TestLog.warn(message(id = "timeout", subject = "appIs", submessage = "${sc.error?.message}"))
                // Retry once on an unexpectedly long processing times occurred
                actionFunc()
            } else {
                sc.throwIfError()
            }
        }

        if (result) {
            TestLog.ok(message = assertMessage, arg1 = appNameOrAppId)
        } else {
            lastElement.lastResult = LogType.NG

            val appName = AppNameUtility.getCurrentAppName()
            val actual = if (appName.isBlank()) "?" else appName
            val ex = TestNGException("$assertMessage (actual=$actual)")
            throw ex
        }
    }

    lastElement = rootElement
    return lastElement
}

/**
 * screenIs
 */
fun TestDrive.screenIs(
    screenName: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    useCache: Boolean = testContext.useCache,
    onIrregular: (() -> Unit)? = { TestDriver.fireIrregularHandler() },
    func: (() -> Unit)? = null
): TestElement {

    val testElement = TestDriver.it

    val command = "screenIs"
    val assertMessage = message(id = command, subject = screenName)

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = screenName) {

        var result = false
        TestDriver.currentScreen = "?"
        val actionFunc = {
            result = isScreen(screenName = screenName)
            result
        }

        actionFunc()

        if (result.not()) {
            val sc = doUntilActionResultTrue(
                waitSeconds = waitSeconds,
                useCache = useCache,
                actionFunc = actionFunc,
                onIrregular = onIrregular
            )
            if (sc.isTimeout) {
                TestLog.info(message(id = "timeout", subject = "screenIs"))
                // Retry once on an unexpectedly long processing times occurred
                actionFunc()
            } else {
                sc.throwIfError()
            }
        }

        if (result) {
            TestLog.ok(message = assertMessage, arg1 = screenName)
            if (useCache.not()) {
                invalidateCache()   // matched, but not synced yet.
            }
        } else {
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

    syncCache()
    lastElement = rootElement
    return lastElement
}

/**
 * screenIs
 */
fun TestDrive.screenIs(
    screenName: String,
    waitSeconds: Int,
    useCache: Boolean = testContext.useCache,
    func: (() -> Unit)? = null
): TestElement {

    return screenIs(
        screenName = screenName,
        waitSeconds = waitSeconds.toDouble(),
        useCache = useCache,
        func = func
    )
}

internal fun TestDrive.doUntilActionResultTrue(
    actionFunc: () -> Boolean,
    waitSeconds: Double,
    useCache: Boolean,
    onIrregular: (() -> Unit)?
): SyncContext {
    CpuLoadService.waitForCpuLoadUnder()

    val sc = SyncUtility.doUntilTrue(
        waitSeconds = waitSeconds,
        intervalSeconds = PropertiesManager.screenshotIntervalSeconds,
        refreshCache = useCache,
        throwOnError = false
    ) {
        val r = actionFunc()
        TestDriver.refreshCurrentScreen(log = false)
        TestLog.info("currentScreen=$screenName")
        if (r.not()) {
            classic.screenshot()
            if (onIrregular != null) {
                onIrregular.invoke()
                refreshCache()
            }
            if (testContext.enableIrregularHandler && testContext.onScreenErrorHandler != null) {
                classic.withoutScroll {
                    testContext.onScreenErrorHandler!!.invoke()
                }
                refreshCache()
            }
        }
        r
    }
    return sc
}

/**
 * screenIsOf
 */
fun TestDrive.screenIsOf(
    vararg screenNames: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    useCache: Boolean = testContext.useCache,
    onIrregular: (() -> Unit)? = { TestDriver.fireIrregularHandler() },
    func: (() -> Unit)? = null
): TestElement {

    val testElement = TestDriver.it

    val command = "screenIsOf"
    val assertMessage = message(id = command, subject = screenNames.joinToString(","))

    val context = TestDriverCommandContext(testElement)
    context.execCheckCommand(command = command, message = assertMessage, subject = screenNames.joinToString(",")) {
        screenIsOfCore(
            screenNames = screenNames,
            waitSeconds = waitSeconds,
            assertMessage = assertMessage,
            useCache = useCache,
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
fun TestDrive.screenIsOf(
    vararg screenNames: String,
    waitSeconds: Int,
    useCache: Boolean = testContext.useCache,
    onIrregular: (() -> Unit)? = { TestDriver.fireIrregularHandler() },
    func: (() -> Unit)? = null
): TestElement {

    return screenIsOf(
        screenNames = screenNames,
        waitSeconds = waitSeconds.toDouble(),
        useCache = useCache,
        onIrregular = onIrregular,
        func = func
    )
}

internal fun TestDrive.screenIsOfCore(
    vararg screenNames: String,
    assertMessage: String,
    waitSeconds: Double,
    useCache: Boolean = testContext.useCache,
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
        if (useCache.not()) {
            invalidateCache()   // matched, but not synced yet.
        }
    } else {
        lastElement.lastResult = LogType.NG

        val screenIdentities = mutableMapOf<String, String>()
        for (name in screenNames) {
            val identity = ScreenRepository.get(name).identityElements.joinToString("")
            screenIdentities[name] = identity
        }
        val expected = screenIdentities.map { "${it.key}.identity=${it.value}" }.joinToString(",")
        val message = "$assertMessage(currentScreen=${TestDriver.currentScreen}, expected $expected)"
        val ex = TestNGException(message, lastElement.lastError)
        throw ex
    }
}

/**
 * verify
 */
fun TestDrive.verify(
    message: String,
    func: (() -> Unit)
): TestElement {
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

