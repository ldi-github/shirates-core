package shirates.core.hand.devicetest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.utility.android.AdbUtility

class AdbUtilityTest {

    val UDID = "emulator-5554"

    @Test
    @Order(1)
    fun killServer() {

        // Act
        AdbUtility.killServer(log = true)
        // Assert
        val r = AdbUtility.ps(udid = UDID)
        assertThat(r).contains("adb: device")
    }

    @Test
    @Order(2)
    fun startServer() {

        // Act
        val r = AdbUtility.startServer(log = true)
        println(r)
        // Assert
        val r2 = AdbUtility.ps(udid = UDID)
        println(r2)
        val header = r2.split(System.lineSeparator()).first()
        assertThat(header).startsWith("USER")
        assertThat(header).contains("PID")
    }

    @Test
    @Order(3)
    fun restartServer() {

        // Act
        val r = AdbUtility.restartServer(log = true)
        println(r)
        // Assert
        val r2 = AdbUtility.ps(udid = UDID)
        println(r2)
        val header = r2.split(System.lineSeparator()).first()
        assertThat(header).startsWith("USER")
        assertThat(header).contains("PID")
    }
}