package shirates.core.driver.eventextension

import shirates.core.driver.TestDrive
import shirates.core.driver.TestElement
import shirates.core.driver.testContext

/**
 * onScreen
 */
fun TestDrive.onScreen(
    vararg screenNames: String,
    onTrue: (TestDriverOnScreenContext) -> Unit
): TestElement {

    for (screenName in screenNames) {
        if (testContext.screenHandlers.containsKey(screenName).not()) {
            testContext.screenHandlers[screenName] = onTrue
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

    for (screenName in list) {
        if (testContext.screenHandlers.containsKey(screenName)) {
            testContext.screenHandlers.remove(screenName)
        }
    }

    return lastElement
}

/**
 * clearScreenHandlers
 */
fun TestDrive.clearScreenHandlers(): TestElement {

    testContext.screenHandlers.clear()

    return lastElement
}