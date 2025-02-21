package shirates.core.vision.unittest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Selector
import shirates.core.vision.VisionElement

class VisionElementTest {

    @Test
    fun subject() {

        // Arrange
        val v = VisionElement(capture = false)
        v.selector = Selector("<Text1>:belowText")
        // Act, Assert
        assertThat(v.subject).isEqualTo("<Text1>:belowText")
    }

}