package shirates.core.vision.testcode

import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.driver.TestDriver
import shirates.core.logging.TestLog
import shirates.core.testcode.UITestBase
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.commandextension.disableCache

/**
 * VisionTest
 */
abstract class VisionTest : UITestBase(), VisionDrive {

    /**
     * vision
     */
    val vision: VisionElement
        get() {
            return TestDriver.lastVisionElement
        }

    /**
     * beforeEach
     */
    override fun beforeEach(context: ExtensionContext?) {

        TestLog.trace()

        disableCache()
    }

}