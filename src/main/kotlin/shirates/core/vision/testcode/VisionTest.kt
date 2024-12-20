package shirates.core.vision.testcode

import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestDriver
import shirates.core.logging.TestLog
import shirates.core.testcode.UITestBase
import shirates.core.utility.toPath
import shirates.core.vision.SrvisionProxy
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.commandextension.disableCache
import shirates.core.vision.driver.commandextension.screenshot

/**
 * VisionTest
 */
abstract class VisionTest : UITestBase(), VisionDrive {

    val it: VisionElement
        get() {
            return TestDriver.lastVisionElement
        }

    override fun beforeAllAfterSetup(context: ExtensionContext?) {
        super.beforeAllAfterSetup(context)

        SrvisionProxy.callImageFeaturePrintConfigurator(
            inputDirectory = PropertiesManager.visionDirectory.toPath().resolve("screens").toString(),
            log = true
        )
    }

    /**
     * beforeEach
     */
    override fun beforeEach(context: ExtensionContext?) {

        TestLog.trace()

        disableCache()
        screenshot()
    }

}