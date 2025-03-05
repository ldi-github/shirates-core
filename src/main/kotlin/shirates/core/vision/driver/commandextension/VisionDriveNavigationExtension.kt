package shirates.core.vision.driver.commandextension

import goPreviousApp
import shirates.core.driver.classic
import shirates.core.driver.testContext
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement

/**
 * goPreviousApp
 */
fun VisionDrive.goPreviousApp(
    waitSeconds: Double = testContext.shortWaitSeconds
): VisionElement {

    classic.goPreviousApp(
        waitSeconds = waitSeconds,
    )
    return lastElement
}