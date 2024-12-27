package shirates.core.vision.testcode

import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestDriver
import shirates.core.testcode.UITestBase
import shirates.core.utility.toPath
import shirates.core.vision.SrvisionProxy
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement

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

        SrvisionProxy.setupImageFeaturePrintConfig(
            inputDirectory = PropertiesManager.visionDirectory.toPath().resolve("screens").toString(),
            log = true
        )
    }

}