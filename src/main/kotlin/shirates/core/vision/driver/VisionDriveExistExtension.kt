package shirates.core.vision.driver

import shirates.core.configuration.Selector
import shirates.core.driver.Bounds
import shirates.core.driver.TestDriver.testContext
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.commandextension.getSelector
import shirates.core.exception.TestNGException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.toPath
import shirates.core.vision.SrvisionProxy
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement

/**
 * existImage
 */
fun VisionDrive.existImage(
    expression: String,
    skinThickness: Int = 1,
    margin: Int = 10,
    throwsException: Boolean = true,
    waitSeconds: Double = testContext.syncWaitSeconds,
    frame: Bounds? = null,
): VisionElement {

    val testElement = getThisOrLastVisionElement()

    val command = "existImage"
    val s = runCatching { getSelector(expression = expression) }.getOrNull()
    val sel = s ?: try {
        // Try getting registered selector in current screen again
        getSelector(expression = expression)
    } catch (t: Throwable) {
        // If there is no selector, create an image selector.
        Selector(expression.removeSuffix(".png") + ".png")
    }
    val message = message(id = command, subject = expression)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = "$sel") {

        val screenshotFile = CodeExecutionContext.lastScreenshotFile
        val templateFile = expression.toPath().toString()
        val result = SrvisionProxy.getTemplateMatchingRectangle(
            imageFile = screenshotFile,
            templateFile = templateFile,
            margin = margin,
            skinThickness = skinThickness,
        )

        val v = result.primaryCandidate.createVisionElement()
        v.selector = sel
        lastVisionElement = v
        if (result.primaryCandidate.distance > 0.5) {
            val error = TestNGException(message = "$message ($result.primaryCandidate)")
            v.lastError = error
            v.lastResult = LogType.NG
            throw error
        }
        TestLog.ok(message = "$message ($result.primaryCandidate)")
    }

    return lastVisionElement
}