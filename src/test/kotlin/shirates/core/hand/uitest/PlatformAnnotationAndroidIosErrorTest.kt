package shirates.core.hand.uitest

import org.junit.jupiter.api.Test
import shirates.core.testcode.android
import shirates.core.testcode.ios
import shirates.core.vision.testcode.VisionTest

/**
 * shirates.core.exception.TestConfigException: Do not use @android annotation and @ios annotation at the same time.
 */
@android
@ios
class PlatformAnnotationAndroidIosErrorTest : VisionTest() {

    @Test
    fun error() {
    }
}