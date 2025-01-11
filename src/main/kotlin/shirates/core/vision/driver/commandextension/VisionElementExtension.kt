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
        lastElement = v
    }

    return lastElement
}