package shirates.core.vision

import shirates.core.driver.*
import shirates.core.driver.commandextension.syncCache
import shirates.core.vision.driver.VisionDriveObject
import shirates.core.vision.driver.lastElement

/**
 * visionScope
 */
fun TestDrive.visionScope(
    func: (VisionDrive) -> Unit
): TestElement {
    val originalEnableCache = testContext.enableCache
    val originalIsVisionTest = TestMode.isVisionTest
    try {
        testContext.enableCache = false
        TestMode.isVisionTest = true
        func(VisionDriveObject)
    } finally {
        testContext.enableCache = originalEnableCache
        TestMode.isVisionTest = originalIsVisionTest
    }

    return lastElement
}

/**
 * visionScope
 */
fun VisionDrive.visionScope(
    func: (VisionDrive) -> Unit
): VisionElement {
    val originalEnableCache = testContext.enableCache
    val originalIsVisionTest = TestMode.isVisionTest
    try {
        testContext.enableCache = false
        TestMode.isVisionTest = true
        func(VisionDriveObject)
    } finally {
        testContext.enableCache = originalEnableCache
        TestMode.isVisionTest = originalIsVisionTest
    }

    return lastElement
}

/**
 * classicScope
 */
fun VisionDrive.classicScope(
    useCache: Boolean = true,
    func: (TestDrive) -> Unit
): VisionElement {
    classic.syncCache(force = true)
    val originalEnableCache = testContext.enableCache
    val originalIsVisionTest = TestMode.isVisionTest
    try {
        testContext.enableCache = useCache
        TestMode.isVisionTest = false
        func(TestDriveObject)
    } finally {
        testContext.enableCache = originalEnableCache
        TestMode.isVisionTest = originalIsVisionTest
    }

    return lastElement
}
