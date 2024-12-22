package shirates.core.vision.driver.eventextension

import shirates.core.driver.testContext
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement

/**
 * onScreen
 */
fun VisionDrive.onScreen(
    vararg screenNames: String,
    onTrue: (VisionDriveOnScreenContext) -> Unit
): VisionElement {

    for (screenName in screenNames) {
        if (testContext.visionDriveScreenHandlers.containsKey(screenName).not()) {
            testContext.visionDriveScreenHandlers[screenName] = onTrue
        }
    }

    return lastElement
}

/**
 * removeScreenHandler
 */
fun VisionDrive.removeScreenHandler(
    screenName: String,
    vararg screenNames: String
): VisionElement {

    val list = screenNames.toMutableList()
    list.add(0, screenName)

    for (name in list) {
        if (testContext.visionDriveScreenHandlers.containsKey(name)) {
            testContext.visionDriveScreenHandlers.remove(name)
        }
    }

    return lastElement
}

/**
 * clearScreenHandlers
 */
fun VisionDrive.clearScreenHandlers(): VisionElement {

    testContext.visionDriveScreenHandlers.clear()

    return lastElement
}