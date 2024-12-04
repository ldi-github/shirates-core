package shirates.core.vision.driver

import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.commandextension.SwipeContext
import shirates.core.driver.commandextension.swipePointToPointCore
import shirates.core.driver.testContext
import shirates.core.driver.testDrive
import shirates.core.driver.viewBounds
import shirates.core.logging.Message.message
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement

/**
 * tap
 */
fun VisionDrive.tap(
    x: Int,
    y: Int,
    holdSeconds: Double = testContext.tapHoldSeconds,
    repeat: Int = 1,
    safeMode: Boolean = false
): VisionDrive {

    val testElement = getThisOrLastVisionElement()

    val command = "tap"
    val message = message(id = command, subject = "($x,$y)")

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = testElement.subject) {

        val sc = SwipeContext(
            swipeFrame = viewBounds,
            viewport = viewBounds,
            startX = x,
            startY = y,
            endX = x,
            endY = y,
            safeMode = safeMode,
            durationSeconds = holdSeconds,
            repeat = repeat,
        )

        testDrive.swipePointToPointCore(swipeContext = sc)
    }

//    return refreshLastElement()
    return this
}

/**
 * tap
 */
fun VisionDrive.tap(
    holdSeconds: Double = testContext.tapHoldSeconds
): VisionElement {

    val tappedElement = getThisOrLastVisionElement()

    val command = "tap"
    val message = message(id = command, subject = tappedElement.subject)

    val context = TestDriverCommandContext(null)
    var v = tappedElement
    context.execOperateCommand(command = command, message = message, subject = tappedElement.subject) {
//        e = tappedElement.tapCore(holdSeconds = holdSeconds, tapMethod = tapMethod)

        println("tap ${v.bounds.centerX}, ${v.bounds.centerY}")
        v.tap(x = v.bounds.centerX, y = v.bounds.centerY, holdSeconds = holdSeconds)
    }

    lastVisionElement = v
    return lastVisionElement
}

///**
// * existImage
// */
//fun VisionDrive.existImage(
//    expression: String,
//    throwsException: Boolean = true,
//    waitSeconds: Double = testContext.syncWaitSeconds,
//    frame: Bounds? = null,
//): VisionElement {
//
//    val element = getThisOrLastVisionElement()
//
//    val command = "existImage"
//    val s = runCatching { testDrive.getSelector(expression = expression) }.getOrNull()
//    val sel = s ?: try {
//        // Try getting registered selector in current screen again
//        testDrive.getSelector(expression = expression)
//    } catch (t: Throwable) {
//        // If there is no selector, create an image selector.
//        Selector(expression.removeSuffix(".png") + ".png")
//    }
//    val message = message(id = command, subject = expression)
//
//    val context = TestDriverCommandContext(null)
//    context.execOperateCommand(command = command, message = message, subject = "$sel") {
//
//        val image = BoofCVUtility.findMatches()
//    }
//
//    lastVisionElement = v
//    return lastVisionElement
//
//}