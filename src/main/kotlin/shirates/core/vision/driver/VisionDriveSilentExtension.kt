package shirates.core.vision.driver

import shirates.core.driver.TestDriverCommandContext
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement

/**
 * silent
 */
fun VisionDrive.silent(
    proc: () -> Unit
): VisionElement {

    val context = TestDriverCommandContext(null)
    context.execSilentCommand() {
        proc()
    }
    return lastElement
}
