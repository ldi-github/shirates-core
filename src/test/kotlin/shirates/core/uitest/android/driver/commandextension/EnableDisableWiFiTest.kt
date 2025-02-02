package shirates.core.uitest.android.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.screenshot
import shirates.core.driver.commandextension.thisIs
import shirates.core.driver.commandextension.thisIsFalse
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.driver.wait
import shirates.core.testcode.UITest
import shirates.core.utility.android.AndroidDeviceUtility

@Testrun("testConfig/android/androidSettings/testrun.properties")
class EnableDisableWiFiTest : UITest() {

    @Test
    @Order(10)
    fun enableDisableWiFi() {

        scenario {
            case(1) {
                action {
                    s1 = AndroidDeviceUtility.enableWiFi()
                }.expectation {
                    s1.thisIs("adb -s ${testProfile.udid} shell svc wifi enable")

                    val r = AndroidDeviceUtility.isWiFiEnabled()
                    wait()
                    screenshot(force = true, onChangedOnly = false)
                    r.thisIsTrue("WiFi is enabled.")
                }
            }
            case(2) {
                action {
                    s1 = AndroidDeviceUtility.disableWiFi()
                }.expectation {
                    s1.thisIs("adb -s ${testProfile.udid} shell svc wifi disable")

                    val r = AndroidDeviceUtility.isWiFiEnabled()
                    wait()
                    screenshot(force = true, onChangedOnly = false)
                    r.thisIsFalse("WiFi is disabled.")
                }
            }
            case(3) {
                action {
                    s1 = AndroidDeviceUtility.enableWiFi()
                }.expectation {
                    s1.thisIs("adb -s ${testProfile.udid} shell svc wifi enable")

                    val r = AndroidDeviceUtility.isWiFiEnabled()
                    wait()
                    screenshot(force = true, onChangedOnly = false)
                    r.thisIsTrue("WiFi is enabled.")
                }
            }
        }
    }

}