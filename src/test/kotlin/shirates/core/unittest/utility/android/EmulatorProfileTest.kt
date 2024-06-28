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
                profileName = "Pixel 8(Android 14)-01",
                emulatorOptions = Const.EMULATOR_OPTIONS.split(" ").toMutableList(),
                emulatorPort = 5556
            )
            // Assert
            assertThat(emulatorProfile.profileName).isEqualTo("Pixel 8(Android 14)-01")
            assertThat(emulatorProfile.avdName).isEqualTo("Pixel_8_Android_14_-01")
            assertThat(emulatorProfile.platformVersion).isEqualTo("14")
            assertThat(emulatorProfile.emulatorPort).isEqualTo(5556)
        }
        run {
            // Act
            val emulatorProfile = EmulatorProfile(
                profileName = "Android *",
                emulatorOptions = Const.EMULATOR_OPTIONS.split(" ").toMutableList(),
                emulatorPort = 5556
            )
            // Assert
            assertThat(emulatorProfile.profileName).isEqualTo("Android *")
            assertThat(emulatorProfile.avdName).isEqualTo("Android_*")
            assertThat(emulatorProfile.platformVersion).isEqualTo("")
            assertThat(emulatorProfile.emulatorPort).isEqualTo(5556)
        }
    }
}