package shirates.core.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class TestDriveAppExtensionTest : UITest() {

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
                    it.isApp("com.apple.Preferences").thisIsFalse()
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
}
