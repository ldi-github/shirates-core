package shirates.core.vision.driver

import org.openqa.selenium.OutputType
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.*
import shirates.core.driver.TestDriver.appiumDriver
import shirates.core.driver.commandextension.getSelector
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.image.BufferedImageUtility
import shirates.core.utility.image.isSame
import shirates.core.utility.ios.IosDeviceUtility
import shirates.core.utility.time.StopWatch
import shirates.core.utility.toBufferedImage
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.commandextension.canDetectCore
import shirates.core.vision.driver.commandextension.screenshot


/**
 * syncScreen
 */
fun VisionDrive.syncScreen(): VisionElement {

    if (TestDriver.isScreenshotSyncing) {
        return lastElement
    }
    val sw = StopWatch("syncScreen")
    try {
        TestDriver.isScreenshotSyncing = true

        if (TestMode.isAndroid) {
            /**
             * Android
             * has no sync mechanism in itself
             */
            val oldImage = CodeExecutionContext.lastScreenshotImage
            val newImage = appiumDriver.getScreenshotAs(OutputType.BYTES).toBufferedImage()
            if (newImage.isSame(oldImage)) {
                CodeExecutionContext.lastScreenshotImage = newImage
            } else {
                if (CodeExecutionContext.shouldOutputLog) {
                    TestLog.info("Syncing screen.")
                }
                Thread.sleep(testContext.waitSecondsForAnimationComplete.toLong())
                val byteArray = appiumDriver.getScreenshotAs(OutputType.BYTES)
                CodeExecutionContext.lastScreenshotImage = byteArray.toBufferedImage()
            }
        } else {
            /**
             * iOS
             * has sync mechanism in itself
             */
            if (testContext.screenshotWithSimctl) {
                val tempScreenshotFile = TestLog.directoryForLog.resolve("tempScreenshotFile.png").toString()
                IosDeviceUtility.getScreenshot(udid = testProfile.udid, file = tempScreenshotFile)
                CodeExecutionContext.lastScreenshotImage = BufferedImageUtility.getBufferedImage(tempScreenshotFile)
            } else {
                CodeExecutionContext.lastScreenshotImage =
                    appiumDriver.getScreenshotAs(OutputType.BYTES).toBufferedImage()
            }
        }
        CodeExecutionContext.screenshotSynced = true
        return lastElement
    } finally {
        TestDriver.isScreenshotSyncing = false
        sw.stop()
    }
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
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    removeRedundantText: Boolean = true,
    mergeBoundingBox: Boolean = true,
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
                last = false,
                waitSeconds = 0.0,
                allowScroll = false,
                removeRedundantText = removeRedundantText,
                mergeBoundingBox = mergeBoundingBox,
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
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    removeRedundantText: Boolean = true,
    mergeBoundingBox: Boolean = true,
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
                last = false,
                waitSeconds = waitSeconds,
                allowScroll = false,
                removeRedundantText = removeRedundantText,
                mergeBoundingBox = mergeBoundingBox,
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