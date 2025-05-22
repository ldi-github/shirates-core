package shirates.core.vision.driver

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestDriver
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.testContext
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.commandextension.canDetectCore
import shirates.core.vision.driver.commandextension.invalidateScreen
import shirates.core.vision.driver.commandextension.screenshot


/**
 * syncScreen
 */
fun VisionDrive.syncScreen(
    invalidateScreen: Boolean = false,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    syncIntervalSeconds: Double = testContext.syncIntervalSeconds,
    maxLoopCount: Int = 100,
): VisionElement {

    testContext.saveState()
    try {
        testContext.waitSecondsOnIsScreen = waitSeconds
        testContext.syncIntervalSeconds = syncIntervalSeconds
        testContext.syncMaxLoopCount = maxLoopCount
        if (invalidateScreen) {
            invalidateScreen()
        }
        screenshot(
            force = false,
            onChangedOnly = true,
        )
    } finally {
        testContext.resumeState()
    }

    return lastElement
}

/**
 * wait
 */
fun VisionDrive.wait(
    waitSeconds: Double
): VisionElement {

    val command = "wait"
    val message = message(id = command, subject = "$waitSeconds")

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, fireEvent = false) {
        TestLog.trace("waiting for ${waitSeconds} seconds")

        invalidateScreen()
        Thread.sleep((waitSeconds * 1000).toLong())
    }

    return lastElement
}

/**
 * wait
 */
fun VisionDrive.wait(
    waitSeconds: Long
): VisionElement {

    val secondsInDouble = waitSeconds.toDouble()
    return this.wait(waitSeconds = secondsInDouble)
}

/**
 * wait
 */
fun VisionDrive.wait(): VisionElement {

    return wait(waitSeconds = testContext.shortWaitSeconds)
}

/**
 * waitForClose
 */
fun VisionDrive.waitForClose(
    expression: String,
    language: String = PropertiesManager.visionOCRLanguage,
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    throwsException: Boolean = true
): VisionElement {

    val sel = getSelector(expression = expression)

    val context = TestDriverCommandContext(null)
    context.execSelectCommand(selector = sel, subject = sel.nickname) {

        var found = false
        doUntilTrue(
            waitSeconds = waitSeconds,
            throwOnFinally = false,
            onBeforeRetry = {
                screenshot(force = true, onChangedOnly = true)
            }
        ) {
            found = canDetectCore(
                selector = sel,
                language = language,
                looseMatch = looseMatch,
                mergeBoundingBox = mergeBoundingBox,
                lineSpacingRatio = lineSpacingRatio,
                autoImageFilter = autoImageFilter,
                last = false,
                waitSeconds = 0.0,
                allowScroll = false,
            )
            found.not()
        }
        if (throwsException && found) {
            throw TestDriverException(
                message = message(
                    id = "waitForCloseFailed",
                    subject = sel.getElementExpression(),
                    arg1 = "$waitSeconds"
                )
            )
        }
    }

    return lastElement
}

/**
 * waitForDisplay
 */
fun VisionDrive.waitForDisplay(
    expression: String,
    language: String = PropertiesManager.visionOCRLanguage,
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    throwsException: Boolean = true,
): VisionElement {

    val testElement = TestDriver.it

    val sel = getSelector(expression = expression)
    val context = TestDriverCommandContext(testElement)
    context.execSelectCommand(selector = sel, subject = sel.nickname) {

        var found = false
        doUntilTrue(
            waitSeconds = waitSeconds,
            throwOnFinally = false,
            onBeforeRetry = {
                screenshot(force = true, onChangedOnly = true)
            }
        ) {
            found = canDetectCore(
                selector = sel,
                language = language,
                looseMatch = looseMatch,
                mergeBoundingBox = mergeBoundingBox,
                lineSpacingRatio = lineSpacingRatio,
                autoImageFilter = autoImageFilter,
                last = false,
                waitSeconds = waitSeconds,
                allowScroll = false,
            )
            found
        }
        if (found.not() && throwsException) {
            throw TestDriverException(
                message = message(
                    id = "waitForDisplayFailed",
                    subject = sel.getElementExpression(),
                    arg1 = "$waitSeconds"
                )
            )
        }
    }

    return lastElement
}

/**
 * usingWaitSeconds
 */
fun VisionDrive.usingWaitSeconds(
    implicitlyWaitSeconds: Double,
    func: () -> Unit
) {
    TestLog.trace()

    val originalImplicitlyWaitSeconds = TestDriver.implicitlyWaitSeconds
    try {
        TestDriver.implicitlyWaitSeconds = implicitlyWaitSeconds
        func()
    } finally {
        TestDriver.implicitlyWaitSeconds = originalImplicitlyWaitSeconds
    }
}