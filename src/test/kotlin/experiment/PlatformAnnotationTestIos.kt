package experiment

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.testcode.ios
import shirates.core.vision.testcode.VisionTest

@ios
class PlatformAnnotationTestIos : VisionTest() {

    @Test
    fun test1() {

        assertThat(testProfile.isiOS).isTrue()
    }
}