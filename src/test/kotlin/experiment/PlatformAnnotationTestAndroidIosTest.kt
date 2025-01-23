package experiment

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.testcode.android
import shirates.core.vision.testcode.VisionTest

@android
class PlatformAnnotationTestAndroidIosTest : VisionTest() {

    @Test
    fun test1() {

        assertThat(testProfile.isAndroid).isTrue()
    }
}