package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.testcode.android
import shirates.core.vision.testcode.VisionTest

/**
 * Do not use @android annotation and @Testrun annotation at the same time.
 */
@android
@Testrun("testConfig/vision/android/androidSettings/testrun.properties")
class PlatformAnnotationTestAndroidAndTestrunTest : VisionTest() {

    @Test
    fun test1() {

    }
}