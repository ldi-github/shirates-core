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
            println(activity)
            assertThat(activity).isEqualTo("com.google.android.calculator/com.android.calculator2.Calculator")
        }
        run {
            // Action
            val activity = AndroidAppUtility.getMainActivity(udid = UDID, packageName = "com.google.android.calendar")
            // Assert
            println(activity)
            assertThat(activity).isEqualTo("com.google.android.calendar/com.android.calendar.AllInOneActivity")
        }
        run {
            // Action
            val activity = AndroidAppUtility.getMainActivity(udid = UDID, packageName = "com.android.chrome")
            // Assert
            println(activity)
            assertThat(activity).isEqualTo("com.android.chrome/com.google.android.apps.chrome.Main")
        }
        run {
            // Action
            val activity = AndroidAppUtility.getMainActivity(udid = UDID, packageName = "com.google.android.apps.maps")
            // Assert
            println(activity)
            assertThat(activity).isEqualTo("com.google.android.apps.maps/com.google.android.maps.MapsActivity")
        }
        run {
            // Action
            val activity = AndroidAppUtility.getMainActivity(udid = UDID, packageName = "com.google.android.deskclock")
            // Assert
            println(activity)
            assertThat(activity).isEqualTo("com.google.android.deskclock/com.android.deskclock.DeskClock")
        }
        run {
            // Action
            val activity = AndroidAppUtility.getMainActivity(udid = UDID, packageName = "com.android.settings")
            // Assert
            println(activity)
            assertThat(activity).isEqualTo("com.android.settings/.Settings")
        }
        run {
            // Action
            val activity = AndroidAppUtility.getMainActivity(udid = UDID, packageName = "com.android.vending")
            // Assert
            println(activity)
            assertThat(activity).isEqualTo("com.android.vending/.AssetBrowserActivity")
        }
        run {
            // Action
            val activity = AndroidAppUtility.getMainActivity(udid = UDID, packageName = "com.google.android.youtube")
            // Assert
            println(activity)
            assertThat(activity).isEqualTo("com.google.android.youtube/com.google.android.apps.youtube.app.watchwhile.WatchWhileActivity")
        }
        run {
            // Action
            val activity =
                AndroidAppUtility.getMainActivity(udid = UDID, packageName = "com.google.android.apps.messaging")
            // Assert
            println(activity)
            assertThat(activity).isEqualTo("com.google.android.apps.messaging/.ui.ConversationListActivity")
        }
        run {
            // Action
            val activity =
                AndroidAppUtility.getMainActivity(udid = UDID, packageName = "com.android.camera2")
            // Assert
            println(activity)
            assertThat(activity).isEqualTo("com.android.camera2/com.android.camera.CameraLauncher")
        }
        run {
            // Action
            val activity =
                AndroidAppUtility.getMainActivity(udid = UDID, packageName = "com.google.android.apps.docs")
            // Assert
            println(activity)
            assertThat(activity).isEqualTo("com.google.android.apps.docs/.app.NewMainProxyActivity")
        }
        run {
            // Action
            val activity =
                AndroidAppUtility.getMainActivity(udid = UDID, packageName = "com.android.dialer")
            // Assert
            println(activity)
            assertThat(activity).isEqualTo("com.android.dialer/.main.impl.MainActivity")
        }
        run {
            // Action
            val activity =
                AndroidAppUtility.getMainActivity(udid = UDID, packageName = "com.google.android.apps.youtube.music")
            // Assert
            println(activity)
            assertThat(activity).isEqualTo("com.google.android.apps.youtube.music/.activities.MusicActivity")
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
            AndroidAppUtility.startApp(udid = UDID, packageNameOrActivityName = packageName, log = true)
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