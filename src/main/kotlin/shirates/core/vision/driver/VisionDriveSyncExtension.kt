package shirates.core.vision.driver

import org.openqa.selenium.OutputType
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestDriver
import shirates.core.driver.TestDriver.appiumDriver
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.testContext
import shirates.core.exception.TestDriverException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.image.isSame
import shirates.core.utility.toBufferedImage
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.commandextension.canDetectCore


/**
 * syncScreen
 */
fun VisionDrive.syncScreen(): VisionElement {

    if (TestDriver.isScreenshotSyncing) {
        return lastElement
    }
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
                CodeExecutionContext.lastScreenshotImage =
                    appiumDriver.getScreenshotAs(OutputType.BYTES).toBufferedImage()
            }
        } else {
            /**
             * iOS
             * has sync mechanism in itself
             */
            CodeExecutionContext.lastScreenshotImage = appiumDriver.getScreenshotAs(OutputType.BYTES).toBufferedImage()
        }
        CodeExecutionContext.screenshotSynced = true
        return lastElement
    } finally {
        TestDriver.isScreenshotSyncing = false
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
    throwsException: Boolean = true
): VisionElement {

    val sel = getSelector(expression = expression)

    val context = TestDriverCommandContext(null)
    context.execSelectCommand(selector = sel, subject = sel.nickname) {

        val found = canDetectCore(
            selector = sel,
            language = language,
            waitSeconds = waitSeconds,
            allowScroll = false,
        )
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
    throwsException: Boolean = true
): VisionElement {

    val testElement = TestDriver.it

    val sel = getSelector(expression = expression)
    val context = TestDriverCommandContext(testElement)
    context.execSelectCommand(selector = sel, subject = sel.nickname) {

        val found = canDetectCore(
            selector = sel,
            language = language,
            waitSeconds = waitSeconds,
            allowScroll = false,
        )
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