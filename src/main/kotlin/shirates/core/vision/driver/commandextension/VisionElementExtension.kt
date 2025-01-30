package shirates.core.vision.driver.commandextension

import shirates.core.driver.TestDriverCommandContext
import shirates.core.logging.Message.message
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement

/**
 * clearInput
 */
fun VisionElement.clearInput(): VisionElement {

    val command = "clearInput"
    val message = message(id = command)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val v = getFocusedElement(throwsException = true)
        val we = v.testElement!!.webElement!!
        we.clear()
        invalidateScreen()
        screenshot()
        lastElement = v.newVisionElement()
    }

    return lastElement
}

/**
 * isInTopArea
 */
fun VisionElement.isInTopArea(rate: Double = 0.25): Boolean {

    val topLine = screenRect.height * rate
    return this.rect.top < topLine
}

/**
 * isInBottomArea
 */
fun VisionElement.isInBottomArea(rate: Double = 0.75): Boolean {

    val bottomLine = screenRect.height * rate
    return bottomLine < this.rect.bottom
}
