package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestDriver
import shirates.core.driver.TestMode
import shirates.core.driver.testContext
import shirates.core.driver.vision
import shirates.core.exception.TestDriverException
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.sync.WaitUtility
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.configration.repository.VisionScreenPredicateRepository
import shirates.core.vision.configration.repository.VisionScreenRepository
import shirates.core.vision.driver.doUntilTrue
import shirates.core.vision.driver.lastElement
import shirates.core.vision.driver.syncScreen

/**
 * screenName
 */
val VisionDrive.screenName: String
    get() {
        return TestDriver.currentScreen
    }


/**
 * screenPredicate
 */
fun VisionDrive.screenPredicate(
    screenName: String,
    identifyFunc: () -> Boolean
): VisionElement {

    VisionScreenPredicateRepository.register(screenName = screenName, predicate = identifyFunc)

    return lastElement
}

/**
 * removeScreenPredicate
 */
fun VisionDrive.removeScreenPredicate(
    screenName: String,
): VisionElement {

    VisionScreenPredicateRepository.remove(screenName = screenName)

    return lastElement
}

/**
 * clearScreenPredicates
 */
fun VisionDrive.clearScreenPredicates() {

    VisionScreenPredicateRepository.clear()
}


/**
 * isScreen
 */
fun VisionDrive.isScreen(
    screenName: String,
    vararg verifyTexts: String,
    invalidateScreen: Boolean = false,
): Boolean {

    if (TestMode.isNoLoadRun) {
        TestDriver.currentScreen = screenName
        return true
    }

    if (verifyTexts.isEmpty()
        && VisionScreenRepository.isRegistered(screenName = screenName).not()
    ) {
        TestLog.warn("screenName is not registered. (screenName=$screenName)")
        return false
    }

    syncScreen(invalidateScreen = invalidateScreen)

    val predicate = VisionScreenPredicateRepository.getPredicate(screenName)
    if (predicate != null) {
        return predicate()
    }

    if (verifyTexts.any()) {
        return isScreenCore(verifyTexts = verifyTexts.toList())
    }

    val match = TestDriver.currentScreen == screenName

    return match
}

internal fun isScreenCore(
    verifyTexts: List<String>
): Boolean {

    if (verifyTexts.isEmpty()) {
        return false
    }
    for (text in verifyTexts) {
        val found = vision.canDetect(expression = text)
        if (found.not()) {
            return false
        }
    }
    return true
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
    syncScreen()
    return screenNames.any { screenName.contains(it) }
}

/**
 * waitScreenOf
 */
fun VisionDrive.waitScreenOf(
    vararg screenNames: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    throwOnError: Boolean = true,
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
        throwOnError = throwOnError,
        irregularHandler = irregularHandler,
        onTrue = onTrue
    )

    return lastElement
}

internal fun VisionDrive.waitScreenOfCore(
    vararg screenNames: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    throwOnError: Boolean,
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
        screenFound = isScreenOf(screenNames = screenNames) // Retry for timeout
    }

    if (screenFound.not() && throwOnError) {
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
    vararg texts: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    throwOnError: Boolean = true,
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

    fun hasAnyTexts(): Boolean {
        for (text in texts) {
            if (canDetect(text).not()) {
                return false
            }
        }
        return true
    }

    var found = false
    doUntilTrue(
        waitSeconds = waitSeconds,
        throwOnFinally = false,
    ) {
        waitScreenOfCore(
            screenName,
            waitSeconds = waitSeconds,
            throwOnError = throwOnError,
            irregularHandler = irregularHandler,
            onTrue = onTrue
        )
        found = if (texts.any()) {
            hasAnyTexts()
        } else {
            true
        }
        found
    }
    if (found.not() && throwOnError) {
        val msgForTexts = if (texts.isEmpty()) "" else "(texts=${texts.joinToString(", ")})"
        val subject = "$screenName$msgForTexts"
        throw TestDriverException(
            message(
                id = "waitScreenOfFailed",
                subject = subject,
                arg1 = TestDriver.currentScreen,
                arg2 = "$waitSeconds"
            )
        )
    }

    return rootElement
}
