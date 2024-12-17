package shirates.core.vision.driver

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.TestDriver
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.testContext
import shirates.core.exception.TestDriverException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.rect
import shirates.core.utility.sync.SyncUtility
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.branchextension.lastScreenshotImage
import shirates.core.vision.driver.commandextension.canDetectCore
import shirates.core.vision.driver.commandextension.getThisOrIt

/**
 * wait
 */
fun VisionDrive.wait(
    waitSeconds: Double
): VisionElement {

    val testElement = getThisOrIt()

    val command = "wait"
    val message = message(id = command, subject = "$waitSeconds")

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, fireEvent = false) {
        TestLog.trace("waiting for ${waitSeconds} seconds")

        Thread.sleep((waitSeconds * 1000).toLong())
    }

    return testElement
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
    expression: String? = null,
    language: String = PropertiesManager.logLanguage,
    rect: Rectangle = lastScreenshotImage!!.rect,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    intervalSeconds: Double = testContext.syncIntervalSeconds,
    throwsException: Boolean = true
): VisionElement {

    val testElement = TestDriver.it

    val sel: Selector
    if (expression == null) {
        if (testElement.selector == null) {
            throw TestDriverException("selector is null")
        } else {
            sel = testElement.selector!!
        }
    } else {
        sel = getSelector(expression = expression)
    }

    val context = TestDriverCommandContext(testElement)
    context.execSelectCommand(selector = sel, subject = sel.nickname) {

        var found = false

        SyncUtility.doUntilTrue(
            waitSeconds = waitSeconds,
            refreshCache = testContext.useCache
        ) {
            found = canDetectCore(
                selector = sel,
                language = language,
                rect = rect,
                waitSeconds = waitSeconds,
                intervalSeconds = intervalSeconds,
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
    language: String = PropertiesManager.logLanguage,
    rect: Rectangle = CodeExecutionContext.region,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    intervalSeconds: Double = testContext.syncIntervalSeconds,
    throwsException: Boolean = true
): VisionElement {

    val testElement = TestDriver.it

    val sel = getSelector(expression = expression)
    val context = TestDriverCommandContext(testElement)
    context.execSelectCommand(selector = sel, subject = sel.nickname) {

        val found = canDetectCore(
            selector = sel,
            language = language,
            rect = rect,
            waitSeconds = waitSeconds,
            intervalSeconds = intervalSeconds,
            allowScroll = false,
        )

        if (throwsException && found.not()) {
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