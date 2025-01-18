package shirates.core.vision

import shirates.core.driver.TestDriver.lastVisionElement
import shirates.core.driver.TestElement
import shirates.core.driver.commandextension.syncCache
import shirates.core.driver.testContext
import shirates.core.driver.testDrive
import shirates.core.driver.visionDrive

/**
 * visionScope
 */
fun visionScope(
    func: (VisionElement) -> Unit
) {
    val originalEnableCache = testContext.enableCache
    try {
        testContext.enableCache = false
        visionDrive.apply {
            func(lastVisionElement)
        }
    } finally {
        testContext.enableCache = originalEnableCache
    }
}

/**
 * testDriveScope
 */
fun testDriveScope(
    func: (TestElement) -> Unit
) {
    if (testContext.enableCache.not()) {
        testDrive.syncCache(force = true)
    }
    val originalEnableCache = testContext.enableCache
    try {
        testContext.enableCache = true
        testDrive.apply {
            func(testDrive.lastElement)
        }
    } finally {
        testContext.enableCache = originalEnableCache
    }
}
