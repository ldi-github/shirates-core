package shirates.core.hand.devicetest

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.testcode.UnitTest
import shirates.core.utility.android.AndroidAppUtility
import shirates.core.utility.android.AndroidDeviceUtility

class AndroidAppUtilityTest : UnitTest() {

    val UDID = "emulator-5554"

    override fun beforeAll(context: ExtensionContext?) {

        if (AndroidDeviceUtility.isDeviceRunning(udid = UDID).not()) {
            throw IllegalStateException("$UDID is not running.")
        }
    }

    @Test
    @Order(1)
    fun getMainActivity() {

        run {
            // Action
            val activity = AndroidAppUtility.getMainActivity(udid = UDID, packageName = "com.google.android.calculator")
            // Assert
            assertThat(activity).isEqualTo("com.google.android.calculator/com.android.calculator2.Calculator")
        }
        run {
            // Action
            val activity = AndroidAppUtility.getMainActivity(udid = UDID, packageName = "com.android.settings")
            // Assert
            assertThat(activity).isEqualTo("com.android.settings/.Settings")
        }
    }

    @Test
    @Order(2)
    fun startApp_isAppRunning_reboot() {

        val packageName = "com.google.android.calculator"

        if (AndroidDeviceUtility.isDeviceRunning(udid = UDID).not()) {
            throw IllegalStateException("$UDID is not running.")
        }

        // Arrange
        run {
            AndroidAppUtility.startApp(udid = UDID, packageName = packageName, log = true)
            val isAppRunning = AndroidAppUtility.isAppRunning(udid = UDID, packageName = packageName)
            assertThat(isAppRunning).isTrue()
            Thread.sleep(1000)
        }
        // Act
        run {
            val r = AndroidDeviceUtility.reboot(udid = UDID, log = true)
            println(r)
        }
        // Assert
        run {
            val isDeviceRunning = AndroidDeviceUtility.isDeviceRunning(udid = UDID)
            Assertions.assertThat(isDeviceRunning).isTrue()

            val isAppRunning = AndroidAppUtility.isAppRunning(udid = UDID, packageName = packageName)
            assertThat(isAppRunning).isFalse()
        }

    }

}