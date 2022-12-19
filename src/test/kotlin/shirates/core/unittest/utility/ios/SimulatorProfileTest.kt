package shirates.core.unittest.utility.ios

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.utility.ios.SimulatorProfile

class SimulatorProfileTest {

    @Test
    fun init() {

        run {
            // Action
            val simulatorProfile = SimulatorProfile(profileName = "iPhone 14(iOS 16.0)-01")
            // Assert
            assertThat(simulatorProfile.profileName).isEqualTo("iPhone 14(iOS 16.0)-01")
            assertThat(simulatorProfile.model).isEqualTo("iPhone 14")
            assertThat(simulatorProfile.platformVersion).isEqualTo("16.0")
        }
        run {
            // Action
            val simulatorProfile = SimulatorProfile(profileName = "iPhone 14")
            // Assert
            assertThat(simulatorProfile.profileName).isEqualTo("iPhone 14")
            assertThat(simulatorProfile.model).isEqualTo("iPhone 14")
            assertThat(simulatorProfile.platformVersion).isEqualTo("")
        }

    }
}