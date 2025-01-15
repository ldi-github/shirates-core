package shirates.core.vision

import shirates.core.driver.TestDriver.lastVisionElement
import shirates.core.driver.TestElement
import shirates.core.driver.testDrive
import shirates.core.driver.visionDrive

/**
 * visionScope
 */
fun visionScope(
    func: (VisionElement) -> Unit
) {
    visionDrive.apply {
        func(lastVisionElement)
    }
}

/**
 * testDriveScope
 */
fun testDriveScope(
    func: (TestElement) -> Unit
) {
    testDrive.apply {
        func(testDrive.lastElement)
    }
}
