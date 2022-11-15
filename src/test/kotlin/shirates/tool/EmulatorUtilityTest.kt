package shirates.tool

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.utility.tool.EmulatorUtility

class EmulatorUtilityTest {

    @Test
    fun getAvdName() {

        run {
            val actual = EmulatorUtility.getAvdName(profileName = "Pixel 3a(Android 12)")
            assertThat(actual).isEqualTo("Pixel_3a_Android_12_")
        }
    }

}