package shirates.core.hand.uitest

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.testcode.ios
import shirates.core.vision.testcode.VisionTest

/**
 * shirates.core.exception.TestConfigException: Do not use @ios annotation and @Testrun annotation at the same time.
 */
@ios
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class PlatformAnnotationIosTestrunErrorTest : VisionTest() {

    @Test
    fun error() {

    }
}