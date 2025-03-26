package shirates.core.vision.unittest.driver

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.driver.vision
import shirates.core.testcode.UnitTest
import shirates.core.vision.driver.tempSelector
import shirates.core.vision.driver.tempValue

class VisionDriveExtensionTest : UnitTest() {

    @Test
    fun tempSelector_tempValue() {

        vision.tempSelector("Nickname1", "Value1")
        assertThat(vision.tempValue("Nickname1")).isEqualTo("Value1")
    }
}