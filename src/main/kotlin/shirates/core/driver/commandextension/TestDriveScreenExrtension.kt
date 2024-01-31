package shirates.core.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.*
import shirates.core.exception.TestDriverException
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.sync.SyncUtility

/**
 * screenName
 */
val TestDrive.screenName: String
    get() {
        return TestDriver.currentScreen
    }

/**
 * isScreen
 */
fun TestDrive.isScreen(
    screenName: String
): Boolean {

    if (TestMode.isNoLoadRun) {
        TestDriver.currentScreen = screenName
        return true
    }

    val r = TestDriver.isScreen(screenName = screenName)
    if (r) {
        TestDriver.currentScreen = screenName
    }

    return r
}

/**
 * isScreenOf
 */
fun TestDrive.isScreenOf(
    vararg screenNames: String,
): Boolean {

    if (TestMode.isNoLoadRun) {
        return true
    }

    for (screenName in screenNames) {
        if (isScreen(screenName = screenName)) {
            return true
        }
    }
    return false
}

/**
 * waitScreenOf
 */
fun TestDrive.waitScreenOf(
    vararg screenNames: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    irregularHandler: (() -> Unit)? = testContext.irregularHandler,
    onTrue: (() -> Unit)? = null
): TestElement {

    val command = "waitScreenOf"
    val message = message(id = command, subject = screenNames.joinToString(","))
    if (PropertiesManager.enableSyncLog) {
        TestLog.write(message = message, logType = LogType.INFO, scriptCommand = command)
    }
    waitScreenOfCore(
        screenNames = screenNames,
        waitSeconds = waitSeconds,
        irregularHandler = irregularHandler,
        onTrue = onTrue
    )

    return lastElement
}

internal fun TestDrive.waitScreenOfCore(
    vararg screenNames: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    irregularHandler: (() -> Unit)? = testContext.irregularHandler,
    onTrue: (() -> Unit)? = null
) {

    fun ofScreen(): String {
        for (screenName in screenNames) {
            if (TestDriver.currentScreen == screenName) {
                return screenName
            }
        }
        return ""
    }

    var currentScreenName = ofScreen()
    if (currentScreenName.isNotBlank()) {
        TestDriver.switchScreen(screenName = currentScreenName)
        onTrue?.invoke()
        return
    }

    var screenFound = false

    val context = SyncUtility.doUntilTrue(
        waitSeconds = waitSeconds,
        intervalSeconds = testContext.waitSecondsForAnimationComplete,
        throwOnError = false
    ) {
        for (screenName in testContext.screenHandlers.keys) {
            isScreen(screenName = screenName)   // Fire screen handler
        }

        refreshCache()
        irregularHandler?.invoke()

        currentScreenName = ofScreen()

        screenFound = currentScreenName.isNotBlank()
        if (screenFound.not()) {
            TestDriver.fireIrregularHandler()
        }
        screenFound
    }

    if (screenFound.not()) {
        val subject = screenNames.joinToString(" or ")
        throw TestDriverException(
            message(
                id = "waitScreenOfFailed",
                subject = subject,
                arg1 = TestDriver.currentScreen,
                arg2 = "$waitSeconds"
            )
        )
    }

    TestDriver.switchScreen(screenName = currentScreenName)

    onTrue?.invoke()
}

/**
 * waitScreen
 */
fun TestDrive.waitScreen(
    screenName: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    irregularHandler: (() -> Unit)? = testContext.irregularHandler,
    onTrue: (() -> Unit)? = null
): TestElement {

    val command = "waitScreen"
    val message = message(id = command, subject = screenName)
    if (PropertiesManager.enableSyncLog) {
        TestLog.write(message = message, logType = LogType.INFO, scriptCommand = command)
    }
    waitScreenOfCore(
        screenName,
        waitSeconds = waitSeconds,
        irregularHandler = irregularHandler,
        onTrue = onTrue
    )

    return lastElement
}
