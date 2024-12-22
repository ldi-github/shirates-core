package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestDriver
import shirates.core.driver.TestMode
import shirates.core.driver.testContext
import shirates.core.exception.TestDriverException
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.sync.WaitUtility
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement

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

//    val file = VisionMLModelRepository.screenClassifierRepository.getFile(label = screenName)
//        ?: return false

    val r = (TestDriver.currentScreen == screenName)
    return r
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
    val screenName = TestDriver.currentScreen
    return screenNames.contains(screenName)
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

    return lastElement
}
