package shirates.core.hand.devicetest

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisIsFalse
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.driver.rootElement
import shirates.core.testcode.UITest
import shirates.core.utility.android.AdbUtility
import shirates.core.utility.misc.ShellUtility
import shirates.core.utility.sync.SyncUtility

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class AdbUtilityTest : UITest() {

    @Test
    fun reboot() {

        val packageName = "com.google.android.calculator"

        scenario {
            case(1) {
                condition {
                    AdbUtility.startApp(udid = "emulator-5554", packageName = packageName)
                    Thread.sleep(3 * 1000)
                    val result = ShellUtility.executeCommand("adb", "-s", "emulator-5554", "shell", "ps")
                    val isRunning = result.resultString.contains(packageName)
                    isRunning.thisIsTrue()
                }.action {
                    AdbUtility.reboot(udid = "emulator-5554")
                    SyncUtility.doUntilTrue(intervalSecond = 5.0) {
                        try {
                            rootElement.packageName == "com.google.android.apps.nexuslauncher"
                        } catch (t: Throwable) {
                            false
                        }
                    }
                }.expectation {
                    val result = ShellUtility.executeCommand("adb", "-s", "emulator-5554", "shell", "ps")
                    val isRunning = result.resultString.contains(packageName)
                    isRunning.thisIsFalse()
                }
            }
        }
    }

    @Test
    fun startApp() {

        val packageName = "com.android.settings"
        val activity = "com.android.settings.Settings"

        scenario {
            case(1) {
                condition {
                    AdbUtility.stopApp(udid = "emulator-5554", packageName = packageName)
                }.action {
                    AdbUtility.startApp(udid = "emulator-5554", packageName = packageName, activityName = activity)
                }.expectation {
                }
            }
            case(2) {

            }
        }

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