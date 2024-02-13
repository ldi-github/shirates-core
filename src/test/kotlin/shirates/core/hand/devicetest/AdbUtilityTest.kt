package shirates.core.hand.devicetest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.utility.android.AdbUtility

class AdbUtilityTest {

    val UDID = "emulator-5554"

    @Test
    @Order(10)
    fun startServer_killServer_restartServer_default() {

        run {
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
        run {
            // Act
            val r = AdbUtility.killServer(log = true)
            println(r)
            // Assert
            val r2 = AdbUtility.ps(udid = UDID)
            println(r2)
            assertThat(r2).contains("* daemon not running; starting now at tcp:5037")

            // Arrange
            Thread.sleep(1000)  // daemon restarts
            // Assert
            val r3 = AdbUtility.ps(udid = UDID)
            println(r3)
            val header = r3.split(System.lineSeparator()).first()
            assertThat(header).startsWith("USER")
            assertThat(header).contains("PID")
        }
        run {
            // Arrange
            AdbUtility.startServer(log = true)
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

    @Test
    @Order(20)
    fun startServer_killServer_restartServer_5038() {

        val PORT = 5038

        run {
            // Act
            val r = AdbUtility.startServer(port = PORT, log = true)
            println(r)
            // Assert
            val r2 = AdbUtility.ps(udid = UDID)
            println(r2)
            val header = r2.split(System.lineSeparator()).first()
            assertThat(header).startsWith("USER")
            assertThat(header).contains("PID")
        }
        run {
            // Act
            val r = AdbUtility.killServer(port = PORT, log = true)
            println(r)
            // Assert
            val r2 = AdbUtility.ps(udid = UDID)
            println(r2)
            assertThat(r2).contains("* daemon not running; starting now at tcp:$PORT")

            // Arrange
            Thread.sleep(1000)  // daemon restarts
            // Assert
            val r3 = AdbUtility.ps(udid = UDID)
            println(r3)
            val header = r3.split(System.lineSeparator()).first()
            assertThat(header).startsWith("USER")
            assertThat(header).contains("PID")
        }
        run {
            // Arrange
            AdbUtility.startServer(log = true)
            // Act
            val r = AdbUtility.restartServer(port = PORT, log = true)
            println(r)
            // Assert
            val r2 = AdbUtility.ps(udid = UDID)
            println(r2)
            val header = r2.split(System.lineSeparator()).first()
            assertThat(header).startsWith("USER")
            assertThat(header).contains("PID")
        }
    }

}