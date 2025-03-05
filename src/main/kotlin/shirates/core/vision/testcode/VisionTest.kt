package shirates.core.vision.testcode

import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.driver.TestDriver
import shirates.core.testcode.UITestBase
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.commandextension.disableCache
import shirates.core.vision.driver.commandextension.invalidateScreen
import shirates.core.vision.driver.commandextension.screenshot
import shirates.core.vision.driver.lastElement

/**
 * VisionTest
 */
abstract class VisionTest : UITestBase(), VisionDrive {

    val it: VisionElement
        get() {
            return TestDriver.lastVisionElement
        }

    var v1 = VisionElement(capture = false)
    var v2 = VisionElement(capture = false)
    var v3 = VisionElement(capture = false)
    var v4 = VisionElement(capture = false)
    var v5 = VisionElement(capture = false)


    /**
     * beforeEach
     */
    override fun beforeEach(context: ExtensionContext?) {

        super.beforeEach(context)

        disableCache()
        invalidateScreen()
        lastElement = VisionElement.emptyElement
        screenshot()
    }

}