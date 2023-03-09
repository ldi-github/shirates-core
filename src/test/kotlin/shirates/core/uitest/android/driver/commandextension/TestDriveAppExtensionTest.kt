package shirates.core.uitest.android.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriveObjectAndroid
import shirates.core.driver.commandextension.*
import shirates.core.driver.testProfile
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestDriveAppExtensionTest : UITest() {

    @Test
    fun isAppInstalled() {

        scenario {
            case(1) {
                expectation {
                    it.isAppInstalled(appNickname = "[Settings]").thisIsTrue()
                    it.isAppInstalled(appNickname = "[Maps]").thisIsTrue()
                    it.isAppInstalled(appNickname = "[App1]").thisIsFalse()
                }
            }
            case(2) {
                expectation {
                    it.isAppInstalled(packageOrBundleId = "com.android.settings").thisIsTrue()
                    it.isAppInstalled(packageOrBundleId = "com.google.android.apps.maps").thisIsTrue()
                    it.isAppInstalled(packageOrBundleId = "example.com.app1").thisIsFalse()
                }
            }
        }
    }

    @Test
    fun launchApp_terminateApp_isApp_appIs() {

        scenario {
            case(1) {
                condition {
                    it.terminateApp("[Settings]")
                    it.isApp("[Settings]").thisIsFalse()
                }.action {
                    it.launchApp("[Settings]")  // By Nickname in apps.json
                }.expectation {
                    it.appIs("[Settings]")  // App Nickname
                    it.appIs("Settings")    // App Nickname Text (Settings -> [Settings])
                    it.appIs("com.android.settings")   // Package Name
                }
            }
            case(2) {
                condition {
                    it.terminateApp("Settings")
                    it.isApp("Settings").thisIsFalse()
                }.action {
                    it.launchApp()  // By Package Name in androidSettingsConfig.json
                }.expectation {
                    it.appIs("[Settings]")  // App Nickname
                    it.appIs("Settings")    // App Nickname Text (Settings -> [Settings])
                    it.appIs("com.android.settings")   // Package Name
                }
            }
            case(3) {
                condition {
                    it.terminateApp("com.android.settings")
                    it.isApp("com.android.settings").thisIsFalse()
                }.action {
                    it.launchApp("com.android.settings")   // By Package Name
                }.expectation {
                    it.appIs("[Settings]")  // App Nickname
                    it.appIs("Settings")    // App Nickname Text (Settings -> [Settings])
                    it.appIs("com.android.settings")   // Package Name
                }
            }
            case(4) {
                condition {
                    it.terminateApp("[Maps]")
                    it.isApp("[Maps]").thisIsFalse()
                }.action {
                    it.launchApp("[Maps]")
                }.expectation {
                    it.appIs("[Maps]")  // App Nickname
                    it.appIs("Maps")    // App Nickname Text (Maps -> [Maps])
                    it.appIs("com.google.android.apps.maps")   // Package Name
                }
            }
        }
    }

    @Test
    fun getMainActivity() {

        val udid = testProfile.udid

        scenario {
            case(1) {
                action {
                    s1 = TestDriveObjectAndroid.getMainActivity(
                        udid = udid,
                        packageName = "com.google.android.calculator"
                    )
                }.expectation {
                    s1.thisIs("com.google.android.calculator/com.android.calculator2.Calculator")
                }
            }
            case(2) {
                action {
                    s1 =
                        TestDriveObjectAndroid.getMainActivity(udid = udid, packageName = "com.google.android.calendar")
                }.expectation {
                    s1.thisIs("com.google.android.calendar/com.android.calendar.AllInOneActivity")
                }
            }
            case(3) {
                action {
                    s1 = TestDriveObjectAndroid.getMainActivity(udid = udid, packageName = "com.android.chrome")
                }.expectation {
                    s1.thisIs("com.android.chrome/com.google.android.apps.chrome.Main")
                }
            }
            case(4) {
                action {
                    s1 = TestDriveObjectAndroid.getMainActivity(
                        udid = udid,
                        packageName = "com.google.android.apps.maps"
                    )
                }.expectation {
                    s1.thisIs("com.google.android.apps.maps/com.google.android.maps.MapsActivity")
                }
            }
            case(5) {
                action {
                    s1 = TestDriveObjectAndroid.getMainActivity(
                        udid = udid,
                        packageName = "com.google.android.deskclock"
                    )
                }.expectation {
                    s1.thisIs("com.google.android.deskclock/com.android.deskclock.DeskClock")
                }
            }
            case(6) {
                action {
                    s1 = TestDriveObjectAndroid.getMainActivity(udid = udid, packageName = "com.android.settings")
                }.expectation {
                    s1.thisIs("com.android.settings/.Settings")
                }
            }
            case(7) {
                action {
                    s1 = TestDriveObjectAndroid.getMainActivity(udid = udid, packageName = "com.android.vending")
                }.expectation {
                    s1.thisIs("com.android.vending/.AssetBrowserActivity")
                }
            }
            case(8) {
                action {
                    s1 = TestDriveObjectAndroid.getMainActivity(udid = udid, packageName = "com.google.android.youtube")
                }.expectation {
                    s1.thisIs("com.google.android.youtube/com.google.android.apps.youtube.app.watchwhile.WatchWhileActivity")
                }
            }
            case(9) {
                action {
                    s1 = TestDriveObjectAndroid.getMainActivity(
                        udid = udid,
                        packageName = "com.google.android.apps.messaging"
                    )
                }.expectation {
                    s1.thisIs("com.google.android.apps.messaging/.main.MainActivity")
                }
            }
            case(10) {
                action {
                    s1 = TestDriveObjectAndroid.getMainActivity(udid = udid, packageName = "com.android.camera2")
                }.expectation {
                    s1.thisIs("com.android.camera2/com.android.camera.CameraLauncher")
                }
            }
            case(11) {
                action {
                    s1 = TestDriveObjectAndroid.getMainActivity(
                        udid = udid,
                        packageName = "com.google.android.apps.docs"
                    )
                }.expectation {
                    s1.thisIs("com.google.android.apps.docs/.app.NewMainProxyActivity")
                }
            }
            case(12) {
                action {
                    s1 = TestDriveObjectAndroid.getMainActivity(udid = udid, packageName = "com.android.dialer")
                }.expectation {
                    s1.thisIs("com.android.dialer/.main.impl.MainActivity")
                }
            }
            case(13) {
                action {
                    s1 = TestDriveObjectAndroid.getMainActivity(
                        udid = udid,
                        packageName = "com.google.android.apps.youtube.music"
                    )
                }.expectation {
                    s1.thisIs("com.google.android.apps.youtube.music/.activities.MusicActivity")
                }
            }
        }
    }

}