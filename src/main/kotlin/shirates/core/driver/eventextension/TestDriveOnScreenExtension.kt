package shirates.core.driver.eventextension

import shirates.core.driver.TestDrive
import shirates.core.driver.TestElement
import shirates.core.driver.testContext

/**
 * onScreen
 */
fun TestDrive.onScreen(
    vararg screenNames: String,
    onTrue: (TestDriveOnScreenContext) -> Unit
): TestElement {

    for (screenName in screenNames) {
        if (testContext.testDriveScreenHandlers.containsKey(screenName).not()) {
            testContext.testDriveScreenHandlers[screenName] = onTrue
        }
    }

    return lastElement
}

/**
 * removeScreenHandler
 */
fun TestDrive.removeScreenHandler(
    screenName: String,
    vararg screenNames: String
): TestElement {

    val list = screenNames.toMutableList()
    list.add(0, screenName)

    for (name in list) {
        if (testContext.testDriveScreenHandlers.containsKey(name)) {
            testContext.testDriveScreenHandlers.remove(name)
        }
    }

    return lastElement
}

/**
 * clearScreenHandlers
 */
fun TestDrive.clearScreenHandlers(): TestElement {

    testContext.testDriveScreenHandlers.clear()

    return lastElement
}