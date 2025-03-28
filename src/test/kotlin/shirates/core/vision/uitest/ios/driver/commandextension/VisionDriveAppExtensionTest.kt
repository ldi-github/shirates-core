package shirates.core.vision.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.isApp
import shirates.core.driver.commandextension.thisIsFalse
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.driver.classic
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.driver.isApp
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class VisionDriveAppExtensionTest : VisionTest() {

    @Test
    fun isAppInstalled() {

        scenario {
            case(1) {
                expectation {
                    it.isAppInstalled(appNickname = "[Settings]").thisIsTrue("isAppInstalled([Settings]) is true")
                    it.isAppInstalled(appNickname = "[Maps]").thisIsTrue("isAppInstalled([Maps]) is true")
                    it.isAppInstalled(appNickname = "[App1]").thisIsFalse("isAppInstalled([App1]) is false")
                }
            }
            case(2) {
                expectation {
                    it.isAppInstalled(packageOrBundleId = "com.apple.Preferences")
                        .thisIsTrue("isAppInstalled(com.apple.Preferences) is true")
                    it.isAppInstalled(packageOrBundleId = "com.apple.Maps")
                        .thisIsTrue("isAppInstalled(com.apple.Maps) is true")
                    it.isAppInstalled(packageOrBundleId = "example.com.app1")
                        .thisIsFalse("isAppInstalled(example.com.app1) is false")
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
                    it.appIs("Settings")    // App Icon Name
                    it.appIs("com.apple.Preferences")   // BundleId
                }
            }
            case(2) {
                condition {
                    it.terminateApp("Settings")
                    it.isApp("Settings").thisIsFalse()
                }.action {
                    it.launchApp()  // By bundleId in iOSSettingsConfig.json
                }.expectation {
                    it.appIs("[Settings]")  // App Nickname
                    it.appIs("Settings")    // App Icon Name
                    it.appIs("com.apple.Preferences")   // BundleId
                }
            }
            case(3) {
                condition {
                    it.terminateApp("com.apple.Preferences")
                    classic.isApp("com.apple.Preferences").thisIsFalse()
                }.action {
                    it.launchApp("com.apple.Preferences")   // By BundleId
                }.expectation {
                    it.appIs("[Settings]")  // App Nickname
                    it.appIs("Settings")    // App Icon Name
                    it.appIs("com.apple.Preferences")   // BundleId
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
                    it.appIs("Maps")    // App Icon Name
                    it.appIs("com.apple.Maps")   // BundleId
                }
            }
        }
    }

    @Test
    fun launchByMethod() {

        scenario {
            case(1) {
                /**
                 * shell(default)
                 */
                condition {
                    it.appIs("Settings")
                    it.screenIs("[iOS Settings Top Screen]")
                        .tap("[General]")
                        .screenIs("[General Screen]")
                }.action {
                    launchApp()
                }.expectation {
                    it.appIs("Settings")
                    it.screenIs("[iOS Settings Top Screen]")
                }
            }
            case(2) {
                /**
                 * shell
                 */
                condition {
                    it.tap("[General]")
                        .screenIs("[General Screen]")
                }.action {
                    launchApp("Settings", launchAppMethod = "shell")
                }.expectation {
                    it.appIs("Settings")
                    it.screenIs("[iOS Settings Top Screen]")
                }
            }
            case(3) {
                /**
                 * tapAppIcon
                 */
                condition {
                    it.tap("[General]")
                        .screenIs("[General Screen]")
                }.action {
                    launchApp("Settings", launchAppMethod = "tapAppIcon")
                }.expectation {
                    it.appIs("Settings")
                    it.screenIs("[iOS Settings Top Screen]")
                }
            }
            case(4) {
                /**
                 * [macro name] (default)
                 */
                condition {
                    it.tap("[General]")
                        .screenIs("[General Screen]")
                }.action {
                    launchApp(launchAppMethod = "[My Launch Macro]")
                }.expectation {
                    it.appIs("Settings")
                    it.screenIs("[iOS Settings Top Screen]")
                }
            }
            case(5) {
                /**
                 * [macro name] Safari
                 */
                action {
                    launchApp("Safari", launchAppMethod = "[My Launch Macro]")
                }.expectation {
                    it.appIs("Safari")
                }
            }
            case(6) {
                /**
                 * auto
                 */
                action {
                    launchApp("com.apple.Preferences", launchAppMethod = "auto")
                }.expectation {
                    it.appIs("com.apple.Preferences")
                }
            }
            case(7) {
                /**
                 * auto
                 */
                action {
                    launchApp("Maps", launchAppMethod = "auto")
                }.expectation {
                    it.appIs("Maps")
                }
            }
        }
    }

}
