package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestDriver
import shirates.core.driver.TestMode
import shirates.core.driver.testContext
import shirates.core.exception.TestDriverException
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.sync.WaitUtility
import shirates.core.vision.ScreenRecognizer
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.configration.repository.VisionScreenRepository
import shirates.core.vision.driver.lastElement

/**
 * screenName
 */
val VisionDrive.screenName: String
    get() {
        return TestDriver.currentScreen
    }

/**
 * isScreen
 */
fun VisionDrive.isScreen(
    screenName: String
): Boolean {

    if (TestMode.isNoLoadRun) {
        TestDriver.currentScreen = screenName
        return true
    }

    if (VisionScreenRepository.isRegistered(screenName = screenName).not()) {
        return false
    }

    updateCurrentScreen()

    val r = (TestDriver.currentScreen == screenName)
    return r
}

/**
 * updateCurrentScreen
 */
fun VisionDrive.updateCurrentScreen() {

    screenshot(force = true, onChangedOnly = true)

    /**
     * Recognize screen
     */
    val oldScreenName = TestDriver.currentScreen
    TestDriver.currentScreen = ""
    TestDriver.currentScreen = ScreenRecognizer.recognizeScreen(
        screenImageFile = CodeExecutionContext.lastScreenshotFile!!,
    )

    if (TestDriver.currentScreen != oldScreenName) {
        TestLog.printInfo("currentScreen=${TestDriver.currentScreen}")
    }
}

/**
 * isScreenOf
 */
fun VisionDrive.isScreenOf(
    vararg screenNames: String,
): Boolean {

    if (TestMode.isNoLoadRun) {
        return true
    }

    invalidateScreen()
    screenshot()
    updateCurrentScreen()
    return screenNames.any { screenName.contains(it) }
}

/**
 * waitScreenOf
 */
fun VisionDrive.waitScreenOf(
    vararg screenNames: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    irregularHandler: (() -> Unit)? = testContext.irregularHandler,
    onTrue: (() -> Unit)? = null
): VisionElement {

    if (TestMode.isNoLoadRun) {
        return lastElement
    }

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

internal fun VisionDrive.waitScreenOfCore(
    vararg screenNames: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    irregularHandler: (() -> Unit)? = testContext.irregularHandler,
    onTrue: (() -> Unit)? = null
) {

    var screenFound = false

    WaitUtility.doUntilTrue(
        waitSeconds = waitSeconds,
        intervalSeconds = testContext.waitSecondsForAnimationComplete,
        throwOnFinally = false
    ) {
        withoutScroll {
            irregularHandler?.invoke()
        }

        screenFound = isScreenOf(screenNames = screenNames)
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

    onTrue?.invoke()
}

/**
 * waitScreen
 */
fun VisionDrive.waitScreen(
    screenName: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    irregularHandler: (() -> Unit)? = testContext.irregularHandler,
    onTrue: (() -> Unit)? = null
): VisionElement {

    if (TestMode.isNoLoadRun) {
        return lastElement
    }

    val command = "waitScreen"
    val message = message(id = command, subject = screenName)
    if (PropertiesManager.enableSyncLog) {
        TestLog.write(message = message, logType = LogType.INFO, scriptCommand = command)
    }

    if (TestMode.isNoLoadRun) {
        return lastElement
    }

    waitScreenOfCore(
        screenName,
        waitSeconds = waitSeconds,
        irregularHandler = irregularHandler,
        onTrue = onTrue
    )

    return rootElement
}
