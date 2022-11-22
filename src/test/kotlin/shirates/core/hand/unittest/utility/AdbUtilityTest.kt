package shirates.core.hand.unittest.utility

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisIsFalse
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.utility.android.AdbUtility
import shirates.core.utility.misc.ShellUtility

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class AdbUtilityTest {

    @Test
    fun reboot() {

        val packageName = "com.google.android.calculator"
        val activity = "com.android.calculator2.Calculator"

        // Arrange
        run {
            AdbUtility.startApp(udid = "emulator-5554", packageName = packageName, activityName = activity)
            Thread.sleep(3 * 1000)
            val result = ShellUtility.executeCommand("adb", "-s", "emulator-5554", "shell", "ps")
            val isRunning = result.resultString.contains(packageName)
            isRunning.thisIsTrue()
        }
        // Act
        run {
            AdbUtility.reboot(udid = "emulator-5554")
            Thread.sleep(15 * 1000)
        }
        // Assert
        run {
            val result = ShellUtility.executeCommand("adb", "-s", "emulator-5554", "shell", "ps")
            val isRunning = result.resultString.contains(packageName)
            isRunning.thisIsFalse()
        }
    }

    @Test
    fun startApp() {

        val packageName = "com.android.settings"
        val activity = "com.android.settings.Settings"

        val result = AdbUtility.startApp(udid = "emulator-5554", packageName = packageName, activityName = activity)
        println(result)
    }

    @Test
    fun stopApp() {

        val packageName = "com.android.settings"
        AdbUtility.stopApp(udid = "emulator-5554", packageName = packageName)
    }

    @Test
    fun killServer() {

        AdbUtility.killServer(udid = "emulator-5554")
    }

    @Test
    fun startServer() {

        AdbUtility.startServer(udid = "emulator-5554")
    }

    @Test
    fun restartServer() {

        AdbUtility.restartServer(udid = "emulator-5554")
    }
}