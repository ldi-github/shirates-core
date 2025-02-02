package shirates.core.hand.uitest

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.testcode.android
import shirates.core.vision.testcode.VisionTest

/**
 * shirates.core.exception.TestConfigException: Do not use @android annotation and @Testrun annotation at the same time.
 */
@android
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class PlatformAnnotationAndroidTestrunErrorTest : VisionTest() {

    @Test
    fun error() {
    }
}