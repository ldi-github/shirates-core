package shirates.core.vision

import shirates.core.driver.*
import shirates.core.driver.TestDriver.lastVisionElement
import shirates.core.driver.commandextension.syncCache
import shirates.core.vision.driver.lastElement

/**
 * visionScope
 */
fun TestDrive.visionScope(
    func: (VisionElement) -> Unit
): TestElement {
    val originalEnableCache = testContext.enableCache
    try {
        testContext.enableCache = false
        visionDrive.apply {
            func(lastVisionElement)
        }
    } finally {
        testContext.enableCache = originalEnableCache
    }

    return lastElement
}

/**
 * visionScope
 */
fun VisionDrive.visionScope(
    func: (VisionElement) -> Unit
): VisionElement {
    val originalEnableCache = testContext.enableCache
    try {
        testContext.enableCache = false
        visionDrive.apply {
            func(lastVisionElement)
        }
    } finally {
        testContext.enableCache = originalEnableCache
    }

    return lastElement
}

/**
 * classicScope
 */
fun VisionDrive.classicScope(
    useCache: Boolean = true,
    func: (TestElement) -> Unit
): VisionElement {
    testDrive.syncCache(force = true)
    val originalEnableCache = testContext.enableCache
    try {
        testContext.enableCache = useCache
        testDrive.apply {
            func(testDrive.lastElement)
        }
    } finally {
        testContext.enableCache = originalEnableCache
    }

    return lastElement
}
