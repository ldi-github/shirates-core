package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.testcode.ios
import shirates.core.vision.testcode.VisionTest

/**
 * Do not use @ios annotation and @Testrun annotation at the same time.
 */
@ios
@Testrun("testConfig/vision/android/androidSettings/testrun.properties")
class PlatformAnnotationTestIosAndTestrunTest : VisionTest() {

    @Test
    fun test1() {

    }
}