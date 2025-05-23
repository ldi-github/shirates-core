package shirates.core.vision.driver.eventextension

import shirates.core.driver.TestContext
import shirates.core.driver.testContext
import shirates.core.logging.TestLog
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement

/**
 * onScreen
 */
fun VisionDrive.onScreen(
    screenName: String,
    permanent: Boolean = false,
    onTrue: (VisionDriveOnScreenContext) -> Unit
): VisionElement {

    val entry = TestContext.VisionScreenHandlerEntry(
        handler = onTrue,
        permanent = permanent,
    )
    testContext.visionDriveScreenHandlers[screenName] = entry

    return lastElement
}

/**
 * removeScreenHandler
 */
fun VisionDrive.removeScreenHandler(
    vararg screenNames: String,
    force: Boolean = false,
): VisionElement {

    val list = screenNames.toMutableList()

    for (screenName in list) {
        if (testContext.visionDriveScreenHandlers.containsKey(screenName)) {
            val entry = testContext.visionDriveScreenHandlers[screenName]!!
            if (entry.permanent && force.not()) {
                TestLog.warn("Screen handler could not be removed because it is registered as permanent. Use force = true. (screenName=$screenName)")
            } else {
                testContext.visionDriveScreenHandlers.remove(screenName)
            }
        } else {
            TestLog.warn("Screen handler could not be removed because it is not registered or already removed. (screenName=$screenName)")
        }
    }

    return lastElement
}

/**
 * clearScreenHandlers
 */
fun VisionDrive.clearScreenHandlers(
    removePermanent: Boolean = false
): VisionElement {

    if (removePermanent) {
        testContext.visionDriveScreenHandlers.clear()
        return lastElement
    }

    val keys = testContext.visionDriveScreenHandlers.keys.toList()
    for (name in keys) {
        val entry = testContext.visionDriveScreenHandlers[name]!!
        if (entry.permanent.not()) {
            testContext.visionDriveScreenHandlers.remove(name)
        }
    }

    return lastElement
}