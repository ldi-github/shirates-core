package shirates.core.vision.testcode

import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.logging.TestLog
import shirates.core.testcode.UITestBase
import shirates.core.vision.VisionDrive
import shirates.core.vision.driver.commandextension.disableCache
import shirates.core.vision.driver.screenshot

/**
 * VisionTest
 */
abstract class VisionTest : UITestBase(), VisionDrive {

    /**
     * beforeEach
     */
    override fun beforeEach(context: ExtensionContext?) {

        TestLog.trace()

        disableCache()
        screenshot()
    }

}