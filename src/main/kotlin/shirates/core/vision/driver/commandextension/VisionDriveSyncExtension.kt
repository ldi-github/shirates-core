package shirates.core.vision.driver.commandextension

import shirates.core.driver.TestDrive
import shirates.core.driver.TestElement
import shirates.core.driver.branchextension.result.BooleanCompareResult
import shirates.core.driver.commandextension.*
import shirates.core.driver.testDrive
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement

/**
 * useCache
 */
fun VisionDrive.useCache(func: () -> Unit): VisionElement {

    testDrive.useCache(func = func)
    return lastElement
}

/**
 * suppressCache
 */
fun TestDrive.suppressCache(func: () -> Unit): TestElement {

    testDrive.suppressCache(func = func)
    return lastElement
}

/**
 * enableCache
 */
fun VisionDrive.enableCache(): VisionElement {

    testDrive.enableCache()
    return lastElement
}

/**
 * disableCache
 */
fun VisionDrive.disableCache(): VisionElement {

    testDrive.disableCache()
    return lastElement
}

/**
 * switchScreen
 */
fun VisionDrive.switchScreen(screenName: String): VisionElement {

    testDrive.switchScreen(screenName = screenName)
    return lastElement
}

/**
 * onCache
 */
fun VisionDrive.onCache(
    func: () -> Unit
): BooleanCompareResult {

    return testDrive.onCache(func = func)
}

/**
 * onDirectAccess
 */
fun VisionDrive.onDirectAccess(
    func: () -> Unit
): BooleanCompareResult {

    return testDrive.onDirectAccess(func = func)
}
