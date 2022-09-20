package shirates.core.unittest.tool

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.core.utility.tool.SimctlUtility

class IosDeviceInfoTest : UnitTest() {

    @Test
    fun init() {

        // Arrange
        val line = "    iPhone 13(iOS 15)-01 (F3F403A0-4AF4-4CEC-A35B-D2E26F6DDF6C) (Booted) "
        // Act
        val info = SimctlUtility.IosDeviceInfo(line)
        // Assert
        assertThat(info.devicename).isEqualTo("iPhone 13(iOS 15)-01")
        assertThat(info.udid).isEqualTo("F3F403A0-4AF4-4CEC-A35B-D2E26F6DDF6C")
        assertThat(info.status).isEqualTo("Booted")
    }

    @Test
    fun init_with_no_udid() {

        // Arrange
        val line = "    iPhone 13(iOS 15)-01 (xxxx) (Booted) "
        // Act
        val info = SimctlUtility.IosDeviceInfo(line)
        // Assert
        assertThat(info.devicename).isEmpty()
        assertThat(info.udid).isEmpty()
        assertThat(info.status).isEmpty()
    }
}