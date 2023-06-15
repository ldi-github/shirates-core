package shirates.core.driver.commandextension

import org.openqa.selenium.InvalidElementStateException
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.*
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.proxy.AppiumProxy
import kotlin.math.max
import kotlin.math.min

/**
 * swipePointToPoint
 *
 * https://stackoverflow.com/questions/57550682/how-to-scroll-up-down-in-appium-android
 */
fun TestDrive.swipePointToPoint(
    startX: Int,
    startY: Int,
    endX: Int,
    endY: Int,
    withOffset: Boolean = false,
    offsetY: Int = PropertiesManager.swipeOffsetY,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    safeMode: Boolean = true,
): TestElement {

    val testElement = rootElement

    val sc = SwipeContext(
        swipeFrame = viewport,
        viewPort = viewport,
        startX = startX,
        startY = startY,
        endX = endX,
        endY = endY,
        safeMode = safeMode,
        durationSeconds = durationSeconds,
        repeat = repeat
    )
    if (withOffset) {
        sc.endY += offsetY
    }

    val command = "swipePointToPoint"
    val message = message(id = command, from = "(${sc.startX},${sc.startY})", to = "(${sc.endX},${sc.endY})")

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(
        command = command,
        message = message,
        arg1 = "(${sc.startX},${sc.startY})",
        arg2 = "(${sc.endX},${sc.endY})"
    ) {
        swipePointToPointCore(swipeContext = sc)
    }

    return lastElement
}

internal class SwipeContext(
    var swipeFrame: Bounds,
    var viewPort: Bounds,
    var startX: Int,
    var startY: Int,
    var endX: Int,
    var endY: Int,
    val safeMode: Boolean = true,
    val durationSeconds: Double = testContext.swipeDurationSeconds,
    val repeat: Int = 1
) {
    init {
        if (safeMode) {
            val leftEdge = max(swipeFrame.left, viewPort.left)
            val rightEdge = min(swipeFrame.right, viewPort.right)
            val topEdge = max(swipeFrame.top, viewPort.top)
            val bottomEdge = min(swipeFrame.bottom, viewPort.bottom)

            if (startX < leftEdge) {
                startX = leftEdge
            }
            if (startX > rightEdge - 1) {
                startX = rightEdge - 1
            }
            if (startY < topEdge) {
                startY = topEdge
            }
            if (startY > bottomEdge - 1) {
                startY = bottomEdge - 1
            }
            if (endX > rightEdge - 1) {
                endX = rightEdge - 1
            }
            if (endX < leftEdge) {
                endX = leftEdge
            }
            if (endY > bottomEdge - 1) {
                endY = bottomEdge - 1
            }
            if (endY < topEdge) {
                endY = topEdge
            }
        }
    }

}

internal fun TestDrive.swipePointToPointCore(
    swipeContext: SwipeContext,
): TestElement {

    fun swipeFunc() {

        val sc = swipeContext
        val json = """
{"actions":[{"action":"press","options":{"x":${sc.startX},"y":${sc.startY}}},{"action":"wait","options":{"ms":${sc.durationSeconds * 1000}}},{"action":"moveTo","options":{"x":${sc.endX},"y":${sc.endY}}},{"action":"release","options":{}}]}
        """.trimIndent()
        AppiumProxy.invoke("touch/perform", json)

        TestDriver.invalidateCache()
        if (testContext.onScrolling) {
            TestDriver.autoScreenshot()
        }
    }

    val original = CodeExecutionContext.isScrolling
    try {
        CodeExecutionContext.isScrolling = true

        for (i in 1..swipeContext.repeat) {

            if (swipeContext.repeat > 1) {
                TestLog.trace("$i")
            }

            try {
                swipeFunc()
            } catch (t: InvalidElementStateException) {
                if (testContext.useCache) {
                    // retry once
                    TestLog.info("Swipe did not complete successfully. Retrying once")
                    refreshCache()
                    swipeFunc()
                } else {
                    throw t
                }
            }
        }
    } finally {
        CodeExecutionContext.isScrolling = original
    }
    TestDriver.autoScreenshot()

    return lastElement
}

/**
 * swipePointToPoint
 *
 * https://stackoverflow.com/questions/57550682/how-to-scroll-up-down-in-appium-android
 */
fun TestDrive.swipePointToPoint(
    startX: Int,
    startY: Int,
    endX: Int,
    endY: Int,
    withOffset: Boolean = false,
    offsetY: Int = PropertiesManager.swipeOffsetY,
    durationSeconds: Int,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    return swipePointToPoint(
        startX = startX,
        startY = startY,
        endX = endX,
        endY = endY,
        withOffset = withOffset,
        offsetY = offsetY,
        durationSeconds = durationSeconds.toDouble(),
        repeat = repeat,
        safeMode = safeMode
    )
}

private fun TestDrive.getSwipeTarget(): TestElement {

    val testElement = getTestElement()

    if (testElement.isScrollable && testElement.isEmpty.not())
        return testElement

    return rootElement
}

/**
 * swipeCenterToTop
 */
fun TestDrive.swipeCenterToTop(
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    val testElement = getSwipeTarget()

    val command = "swipeCenterToTop"
    val message = message(id = command)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        val b = testElement.bounds
        swipePointToPoint(
            b.centerX,
            b.centerY,
            b.centerX,
            b.top,
            durationSeconds = durationSeconds,
            repeat = repeat,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * flickCenterToTop
 */
fun TestDrive.flickCenterToTop(
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    val testElement = getSwipeTarget()

    val command = "flickCenterToTop"
    val message = message(id = command)
    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        swipeCenterToTop(
            durationSeconds = durationSeconds,
            repeat = repeat,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * swipeCenterToBottom
 */
fun TestDrive.swipeCenterToBottom(
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    val testElement = getSwipeTarget()

    val command = "swipeCenterToBottom"
    val message = message(id = command)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        val b = testElement.bounds
        swipePointToPoint(
            b.centerX,
            b.centerY,
            b.centerX,
            b.bottom,
            durationSeconds = durationSeconds,
            repeat = repeat,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * flickCenterToBottom
 */
fun TestDrive.flickCenterToBottom(
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    val testElement = getSwipeTarget()

    val command = "flickCenterToBottom"
    val message = message(id = command)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        swipeCenterToBottom(
            durationSeconds = durationSeconds,
            repeat = repeat,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * swipeCenterToLeft
 */
fun TestDrive.swipeCenterToLeft(
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    val testElement = getSwipeTarget()

    val command = "swipeCenterToLeft"
    val message = message(id = command)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        val b = testElement.bounds
        swipePointToPoint(
            b.centerX,
            b.centerY,
            b.left,
            b.centerY,
            durationSeconds = durationSeconds,
            repeat = repeat,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * flickCenterToLeft
 */
fun TestDrive.flickCenterToLeft(
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    val testElement = getSwipeTarget()

    val command = "flickCenterToLeft"
    val message = message(id = command)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        swipeCenterToLeft(
            durationSeconds = durationSeconds,
            repeat = repeat,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * swipeCenterToRight
 */
fun TestDrive.swipeCenterToRight(
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    val testElement = getSwipeTarget()

    val command = "swipeCenterToRight"
    val message = message(id = command)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        val b = testElement.bounds
        swipePointToPoint(
            b.centerX,
            b.centerY,
            b.right,
            b.centerY,
            durationSeconds = durationSeconds,
            repeat = repeat,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * flickCenterToRight
 */
fun TestDrive.flickCenterToRight(
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    val testElement = getSwipeTarget()

    val command = "flickCenterToRight"
    val message = message(id = command)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        swipeCenterToRight(
            durationSeconds = durationSeconds,
            repeat = repeat,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * swipeLeftToRight
 */
fun TestDrive.swipeLeftToRight(
    startMarginRatio: Double = testContext.scrollHorizontalMarginRatio,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    val testElement = getSwipeTarget()

    val command = "swipeLeftToRight"
    val message = message(id = command)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        val b = testElement.bounds
        val startX = (b.right * startMarginRatio).toInt()
        swipePointToPoint(
            startX = startX,
            startY = b.centerY,
            endX = b.right,
            endY = b.centerY,
            durationSeconds = durationSeconds,
            repeat = repeat,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * flickLeftToRight
 */
fun TestDrive.flickLeftToRight(
    startMarginRatio: Double = testContext.scrollHorizontalMarginRatio,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    val testElement = getSwipeTarget()

    val command = "flickLeftToRight"
    val message = message(id = command)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        swipeLeftToRight(
            startMarginRatio = startMarginRatio,
            durationSeconds = durationSeconds,
            repeat = repeat,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * swipeRightToLeft
 */
fun TestDrive.swipeRightToLeft(
    startMarginRatio: Double = testContext.scrollHorizontalMarginRatio,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    val testElement = getSwipeTarget()

    val command = "swipeRightToLeft"
    val message = message(id = command)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        val b = testElement.bounds
        val startX = (b.right * (1 - startMarginRatio)).toInt()
        swipePointToPoint(
            startX = startX,
            startY = b.centerY,
            endX = b.left,
            endY = b.centerY,
            durationSeconds = durationSeconds,
            repeat = repeat,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * flickRightToLeft
 */
fun TestDrive.flickRightToLeft(
    startMarginRatio: Double = testContext.scrollHorizontalMarginRatio,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    val testElement = getSwipeTarget()

    val command = "flickRightToLeft"
    val message = message(id = command)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        swipeRightToLeft(
            startMarginRatio = startMarginRatio,
            durationSeconds = durationSeconds,
            repeat = repeat,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * swipeBottomToTop
 */
fun TestDrive.swipeBottomToTop(
    startMarginRatio: Double = testContext.scrollVerticalMarginRatio,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    val testElement = getSwipeTarget()

    val command = "swipeBottomToTop"
    val message = message(id = command)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        val b = testElement.bounds
        val startY = (b.bottom * (1 - startMarginRatio)).toInt()

        swipePointToPoint(
            startX = b.centerX,
            startY = startY,
            endX = b.centerX,
            endY = b.top,
            durationSeconds = durationSeconds,
            repeat = repeat,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * flickBottomToTop
 */
fun TestDrive.flickBottomToTop(
    startMarginRatio: Double = testContext.scrollVerticalMarginRatio,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    val testElement = getSwipeTarget()

    val command = "flickBottomToTop"
    val message = message(id = command)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        swipeBottomToTop(
            startMarginRatio = startMarginRatio,
            durationSeconds = durationSeconds,
            repeat = repeat,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * flickAndGoDown
 */
fun TestDrive.flickAndGoDown(
    startMarginRatio: Double = testContext.scrollVerticalMarginRatio,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    val testElement = getSwipeTarget()

    val command = "flickAndGoDown"
    val message = message(id = command)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        swipeBottomToTop(
            startMarginRatio = startMarginRatio,
            durationSeconds = durationSeconds,
            repeat = repeat,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * flickAndGoRight
 */
fun TestDrive.flickAndGoRight(
    startMarginRatio: Double = testContext.scrollHorizontalMarginRatio,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    val testElement = getSwipeTarget()

    val command = "flickAndGoRight"
    val message = message(id = command)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        swipeRightToLeft(
            startMarginRatio = startMarginRatio,
            durationSeconds = durationSeconds,
            repeat = repeat,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * flickAndGoLeft
 */
fun TestDrive.flickAndGoLeft(
    startMarginRatio: Double = testContext.scrollHorizontalMarginRatio,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    val testElement = getSwipeTarget()

    val command = "flickAndGoLeft"
    val message = message(id = command)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        swipeLeftToRight(
            startMarginRatio = startMarginRatio,
            durationSeconds = durationSeconds,
            repeat = repeat,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * swipeTopToBottom
 */
fun TestDrive.swipeTopToBottom(
    startMarginRatio: Double = testContext.scrollVerticalMarginRatio,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    val testElement = getSwipeTarget()

    val command = "swipeTopToBottom"
    val message = message(id = command)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        val b = testElement.bounds
        val startY = (b.bottom * startMarginRatio).toInt()

        swipePointToPoint(
            startX = b.centerX,
            startY = startY,
            endX = b.centerX,
            endY = b.bottom,
            durationSeconds = durationSeconds,
            repeat = repeat,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * flickAndGoUp
 */
fun TestDrive.flickAndGoUp(
    startMarginRatio: Double = testContext.scrollVerticalMarginRatio,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    val testElement = getSwipeTarget()

    val command = "flickAndGoUp"
    val message = message(id = command)
    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        swipeTopToBottom(
            startMarginRatio = startMarginRatio,
            durationSeconds = durationSeconds,
            repeat = repeat,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * flickTopToBottom
 */
fun TestDrive.flickTopToBottom(
    startMarginRatio: Double = testContext.scrollVerticalMarginRatio,
    durationSeconds: Double = testContext.flickDurationSeconds,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    val testElement = getSwipeTarget()

    val command = "flickTopToBottom"
    val message = message(id = command)
    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        swipeTopToBottom(
            startMarginRatio = startMarginRatio,
            durationSeconds = durationSeconds,
            repeat = repeat,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * swipeElementToElement
 */
fun TestDrive.swipeElementToElement(
    startElement: TestElement,
    endElement: TestElement,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    marginRatio: Double = testContext.swipeMarginRatio,
    adjust: Boolean = false,
    withOffset: Boolean = false,
    offsetY: Int = PropertiesManager.swipeOffsetY,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    val testElement = rootElement

    val command = "swipeElementToElement"
    val message = message(id = command, subject = startElement.subject, to = endElement.subject)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(
        command = command,
        message = message,
        subject = startElement.subject,
        arg1 = endElement.subject
    ) {
        val b1 = startElement.bounds
        val b2 = endElement.bounds
        val marginX = ((b2.centerX - b1.centerX) * marginRatio).toInt()
        val marginY = ((b2.centerY - b1.centerY) * marginRatio).toInt()

        swipePointToPoint(
            startX = b1.centerX,
            startY = b1.centerY,
            endX = b2.centerX - marginX,
            endY = b2.centerY - marginY,
            withOffset = withOffset,
            offsetY = offsetY,
            durationSeconds = durationSeconds,
            repeat = repeat,
            safeMode = safeMode
        )

        val m = TestDriver.select(selector = startElement.selector!!, throwsException = false)
        if (adjust) {
            TestDriver.syncCache(force = true)
            if (m.isEmpty.not()) {
                swipePointToPoint(
                    startX = m.bounds.centerX,
                    startY = m.bounds.centerY,
                    endX = b2.centerX,
                    endY = b2.centerY,
                    withOffset = true,
                    offsetY = offsetY,
                    durationSeconds = durationSeconds,
                    safeMode = safeMode
                )
            }
        }

        TestDriver.syncCache(force = true)
        TestDriver.select(selector = startElement.selector!!)
    }

    if (TestDriver.skip)
        lastElement = startElement
    return lastElement
}

/**
 * swipeElementToElementAdjust
 */
fun TestDrive.swipeElementToElementAdjust(
    startElement: TestElement,
    endElement: TestElement,
    durationSeconds: Double = testContext.swipeDurationSeconds,
    marginRatio: Double = testContext.swipeMarginRatio,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    return swipeElementToElement(
        startElement = startElement,
        endElement = endElement,
        durationSeconds = durationSeconds,
        marginRatio = marginRatio,
        adjust = true,
        offsetY = PropertiesManager.swipeOffsetY,
        repeat = repeat,
        safeMode = safeMode
    )
}
