package shirates.core.vision.uitest.android.testcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.Const
import shirates.core.configuration.PropertiesManager
import shirates.core.vision.testcode.VisionTest

class VisionTestTest : VisionTest() {

    @Test
    fun default() {

        assertThat(PropertiesManager.getDefaultTestrunFile()).isEqualTo(Const.TESTRUN_PROPERTIES)
        assertThat(PropertiesManager.testrunFile).isEqualTo(Const.TESTRUN_PROPERTIES)
    }
}