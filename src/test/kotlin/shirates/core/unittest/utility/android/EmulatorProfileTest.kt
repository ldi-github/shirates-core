package shirates.core.unittest.utility.android

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.Const
import shirates.core.utility.android.EmulatorProfile

class EmulatorProfileTest {

    @Test
    fun init() {

        run {
            // Act
            val emulatorProfile = EmulatorProfile(
                profileName = "Pixel 3a(Android 12)-01",
                emulatorOptions = Const.EMULATOR_OPTIONS.split(" ").toMutableList()
            )
            // Assert
            assertThat(emulatorProfile.profileName).isEqualTo("Pixel 3a(Android 12)-01")
            assertThat(emulatorProfile.avdName).isEqualTo("Pixel_3a_Android_12_-01")
            assertThat(emulatorProfile.platformVersion).isEqualTo("12")
        }
        run {
            // Act
            val emulatorProfile = EmulatorProfile(
                profileName = "Android *",
                emulatorOptions = Const.EMULATOR_OPTIONS.split(" ").toMutableList()
            )
            // Assert
            assertThat(emulatorProfile.profileName).isEqualTo("Android *")
            assertThat(emulatorProfile.avdName).isEqualTo("Android_*")
            assertThat(emulatorProfile.platformVersion).isEqualTo("*")
        }
    }
}